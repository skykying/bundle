package org.panda.logicanalyzer.deviceintegration.ui.internal;


import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.panda.logicanalyzer.ui.pipeline.IPipelineElementConfigurationProvider;

//import gnu.io.CommPortIdentifier;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.panda.logicanalyzer.deviceintegration.ui.internal.DecoratedValue.Alignment;

/**
 * This configuration provider serves the configuration for the device data
 * provider.
 */
public class DeviceDataSourceConfigurationProvider implements IPipelineElementConfigurationProvider {

	/**
	 * The main UI for configuring the device data source.
	 */
	private static class Page extends WizardPage {

		/**
		 * The command listeners
		 */
		private final List<IPageCommandListener> listeners = new LinkedList<IPageCommandListener>();

		/**
		 * The current device (or null if none has been selected yet).
		 */
		private DeviceDescriptor currentDescriptor = null;

		/**
		 * The name of the port the device is conencted to
		 */
		private String portName;

		/**
		 * The baud rate of the connection to the device
		 */
		private int baudRate;


		public Page() {
			super("DeviceDataSourceConfigurationProviderPage");
		}

		@Override
		public void createControl(Composite parent) {
			setTitle("Capturing device data source");
			setDescription("Please select and configure your capturing device");

			final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			scrolledComposite.setExpandVertical(true);
			final Composite composite = new Composite(scrolledComposite, SWT.NONE);
			GridLayout layout = new GridLayout(1, false);
			layout.verticalSpacing = 0;
			composite.setLayout(layout);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);

			createUI(composite);

			scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setExpandVertical(true);
			scrolledComposite.setContent(composite);
			setControl(scrolledComposite);

			listeners.add(new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					if (command == PageCommand.CompositeExpanded) {
						composite.layout();
						scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					} else if (command == PageCommand.ValidatePage) {
						validatePage();
					}
				}

			});

			setPageComplete(false);
		}

		private void createUI(Composite parent) {
			createConnectionSettings(parent);

			final ConfigurationComposite configurationComposite = new ConfigurationComposite(parent, new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					notifyCommandListener(command);
				}
			});
			GridDataFactory.fillDefaults().grab(true, true).applyTo(configurationComposite);

			listeners.add(configurationComposite);
			listeners.add(new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					if (command == PageCommand.DeviceChanged)
						configurationComposite.setInput(currentDescriptor);
				}

			});
		}

		private void createConnectionSettings(final Composite parent) {
			ExpandableComposite expandable = new ExpandableComposite(parent, ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(expandable);
			expandable.setText("Device");
			Composite composite = new Composite(expandable, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			expandable.setClient(composite);
			expandable.addExpansionListener(new ExpansionAdapter() {

				@Override
				public void expansionStateChanged(ExpansionEvent e) {
					notifyCommandListener(PageCommand.CompositeExpanded);
				}

			});

			new Label(composite, SWT.NONE).setText("Device");
			ComboViewer deviceCombo = new ComboViewer(composite, SWT.READ_ONLY | SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(deviceCombo.getControl());
			deviceCombo.setContentProvider(new ArrayContentProvider());
			deviceCombo.setLabelProvider(new LabelProvider());
			try {
				deviceCombo.setInput(DeviceDescriptor.getRegisteredDevices());
			} catch (CoreException e) {
				setErrorMessage(e.getMessage());
				return;
			}
			deviceCombo.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					if (selection.isEmpty()) return;

					currentDescriptor = (DeviceDescriptor) selection.getFirstElement();
					notifyCommandListener(PageCommand.DeviceChanged);
					notifyCommandListener(PageCommand.ValidatePage);
				}

			});

			new Label(composite, SWT.NONE).setText("Port:");
			final ComboViewer portCombo = new ComboViewer(composite, SWT.BORDER | SWT.READ_ONLY);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(portCombo.getControl());
			portCombo.setContentProvider(new ArrayContentProvider());
			portCombo.setLabelProvider(new LabelProvider());


//			Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
//			List<DecoratedValue<CommPortIdentifier>> portList = new LinkedList<DecoratedValue<CommPortIdentifier>>();
//			while (portIdentifiers.hasMoreElements()) {
//				CommPortIdentifier portId = (CommPortIdentifier) portIdentifiers.nextElement();
//				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//					portList.add(new DecoratedValue<CommPortIdentifier>(portId, portId.getName(), Alignment.Hide));
//				}
//			}
//			
//			System.out.println(portList.size()+"size");
//			
//			if(portList.size() > 0) {
//				portCombo.setInput(portList);
//				portCombo.setSelection(new StructuredSelection(portCombo.getElementAt(0)));
//				portCombo.addSelectionChangedListener(new ISelectionChangedListener() {
//	
//					@Override
//					public void selectionChanged(SelectionChangedEvent event) {
//						notifyCommandListener(PageCommand.ValidatePage);
//					}
//	
//				});
//			}

			new Label(composite, SWT.NONE).setText("Port speed:");
			final ComboViewer speedCombo = new ComboViewer(composite, SWT.READ_ONLY | SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(speedCombo.getControl());
			speedCombo.setContentProvider(new ArrayContentProvider());
			speedCombo.setLabelProvider(new LabelProvider());
			speedCombo.setInput(new Object[] {
			                        new DecoratedValue<Integer>(115200, "115200bps (LL)", Alignment.Hide),
			                        new DecoratedValue<Integer>(57600, "57600bps (LH)", Alignment.Hide),
			                        new DecoratedValue<Integer>(38400, "38400bps (HL)", Alignment.Hide),
			                        new DecoratedValue<Integer>(19200, "19200bps (HH)", Alignment.Hide),
			                    });
			speedCombo.setSelection(new StructuredSelection(speedCombo.getElementAt(0)));
			speedCombo.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					notifyCommandListener(PageCommand.ValidatePage);
				}

			});

			listeners.add(new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					switch (command) {
					case Initialize: initialize(); break;
					case UpdateModel: updateModel(); break;
					}
				}

				private void updateModel() {

//					CommPortIdentifier portIdentifier = (CommPortIdentifier) getSelectionValue(portCombo.getSelection());
//					if (portIdentifier != null) portName = portIdentifier.getName();
//
//					Integer baudRate = (Integer) getSelectionValue(speedCombo.getSelection());
//					if (baudRate != null) Page.this.baudRate = baudRate;
				}

				private void initialize() {
					portCombo.setSelection(new StructuredSelection(portCombo.getElementAt(0)));
					speedCombo.setSelection(new StructuredSelection(speedCombo.getElementAt(0)));
				}

			});


			expandable.setExpanded(true);
		}

		/**
		 * Notifies all command listeners for a new command.
		 *
		 * @param command the command to publish
		 */
		private void notifyCommandListener(PageCommand command) {
			for (IPageCommandListener listener : listeners)
				listener.commandReceived(command);
		}

		/**
		 * Returns the value of a selection or null if the selection is either
		 * empty or its value is not understood (e.g. if the selection is not a
		 * {@link IStructuredSelection}.
		 *
		 * @param selection
		 *            the selection
		 * @return the value of the selection or null
		 */
		@SuppressWarnings("unchecked")
		private Object getSelectionValue(ISelection selection) {
			if (selection.isEmpty() || !(selection instanceof IStructuredSelection)) return null;

			Object selectedElement = ((IStructuredSelection) selection).getFirstElement();
			if (selectedElement instanceof DecoratedValue) selectedElement =
				    ((DecoratedValue<Double>) selectedElement).getValue();

			return selectedElement;
		}

		/**
		 * @return the configuration as set by this page
		 * @throws CoreException If the device initialization fails
		 */
		public Map<String, Object> getConfiguration() {
			Map<String, Object> result = new HashMap<String, Object>();

			notifyCommandListener(PageCommand.UpdateModel);
			if (currentDescriptor != null) {
				result.put("device", currentDescriptor.getDevice());
				result.put("configuration", currentDescriptor.getConfiguration());
			}
			result.put("portName", portName);
			result.put("baudRate", baudRate);

			return result;
		}

		/**
		 * Validates this page and sets the message as well as the page can
		 * complete flag appropriately.
		 *
		 */
		private void validatePage() {
			IStatus status = Status.OK_STATUS;

			notifyCommandListener(PageCommand.UpdateModel);
			if (currentDescriptor == null) {
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Device is mandatory");
			} else if (portName == null) {
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Port is mandatory");
			} else if (baudRate == 0) {
				status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Baud rate is mandatory");
			} else {
				status = currentDescriptor.validateConfiguration();
			}

			if (status.isOK()) {
				setErrorMessage(null);
				setPageComplete(true);
			} else {
				setErrorMessage(status.getMessage());
				setPageComplete(false);
			}
		}

	}

	/**
	 * The current wizard page or null if {@link #getPage()} has not yet been
	 * called
	 */
	private Page page;

	@Override
	public Map<String, Object> getConfiguration() {
		Map<String, Object> result = Collections.emptyMap();

		if (page != null) result = page.getConfiguration();

		return result;
	}

	@Override
	public WizardPage getPage() {
		if (page == null) page = new Page();

		return page;
	}

}
