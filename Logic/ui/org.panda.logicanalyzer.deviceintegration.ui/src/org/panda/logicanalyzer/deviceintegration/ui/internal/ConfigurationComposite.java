package org.panda.logicanalyzer.deviceintegration.ui.internal;

import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.deviceintegration.core.IMutableDeviceConfiguration;
import org.panda.logicanalyzer.ui.util.IntegerBitSelector;
import org.panda.logicanalyzer.ui.util.IntegerBitSelector.IModificationListener;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

/**
 * The configuration composite renders the widgets required to change a certain
 * configuration.
 */
public class ConfigurationComposite extends Composite implements IPageCommandListener {

	/**
	 * The internal page listener
	 */
	private List<IPageCommandListener> listeners = new LinkedList<IPageCommandListener>();

	/**
	 * The configuration we're modifying
	 */
	private IMutableDeviceConfiguration configuration;

	/**
	 * The command listener we use to notify in case of page commands
	 */
	private final IPageCommandListener commandListenerAdapter;

	public ConfigurationComposite(Composite parent, IPageCommandListener commandListenerAdapter) {
		super(parent, SWT.NONE);
		this.commandListenerAdapter = commandListenerAdapter;
		GridLayoutFactory.fillDefaults().margins(0, 0).numColumns(1).applyTo(this);
	}

	public void setInput(DeviceDescriptor descriptor) {
		configuration = descriptor.getConfiguration();
		listeners.clear();
		updateUI(descriptor.getElement());
	}

	/**
	 * Builds the UI based on the configuration child element of the given
	 * configuration element.
	 *
	 * @param element the device extension element
	 */
	private void updateUI(IConfigurationElement element) {
		for (Control child : getChildren()) {
			child.dispose();
		}

		IConfigurationElement[] configurationElements = element.getChildren("configuration");
		if (configurationElements.length == 0) {
			return;
		}

		IConfigurationElement[] configurationGroupElements = configurationElements[0].getChildren();
		for (IConfigurationElement configurationGroupElement : configurationGroupElements) {
			buildPropertyGroupUI(configurationGroupElement);
		}

		pack();
	}

	/**
	 * Adds a new property group to this composite by creating an expandable
	 * composite on it and filling it with the appropriate widgets.
	 *
	 * @param element The configuration element describing the property group
	 */
	private void buildPropertyGroupUI(IConfigurationElement element) {
		final String name = element.getAttribute("name");
		final String id = element.getAttribute("id");

		if (name == null || id == null) {
			return;
		}

		ExpandableComposite expandable = new ExpandableComposite(this, ExpandableComposite.TWISTIE | ExpandableComposite.CLIENT_INDENT);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(expandable);
		expandable.setText(name);
		Composite composite = new Composite(expandable, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		expandable.setClient(composite);
		expandable.addExpansionListener(new ExpansionAdapter() {

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				commandListenerAdapter.commandReceived(PageCommand.CompositeExpanded);
			}

		});

		for (IConfigurationElement propertyElement : element.getChildren()) {
			buildPropertyUI(composite, id, propertyElement);
		}

		if ("true".equals(element.getAttribute("expanded"))) {
			expandable.setEnabled(true);
		}
	}

	/**
	 * Adds a widget for a certain configuration property
	 *
	 * @param element the extension point element describing the property
	 */
	private void buildPropertyUI(Composite parent, final String groupName, IConfigurationElement element) {
		final String propertyType = element.getName();
		final String propertyName = element.getAttribute("name");
		final String propertyId = element.getAttribute("id");
		final String id = groupName + "." + propertyId;
		if (propertyName == null || propertyId == null) {
			return;
		}

		if ("bits".equals(propertyType)) {

			new Label(parent, SWT.NONE).setText(propertyName);

			final IntegerBitSelector selector = new IntegerBitSelector(parent, IntegerBitSelector.WITH_TEXT);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(selector);
			listeners.add(new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					if (command == PageCommand.UpdateModel)
						configuration.setProperty(id, selector.getValue());
				}

			});

			selector.addModificationListener(new IModificationListener() {

				@Override
				public void valueChanged(IntegerBitSelector control, int newValue) {
					commandListenerAdapter.commandReceived(PageCommand.ValidatePage);
				}

			});

			if (configuration.isPropertyKnown(id) && configuration.getProperty(id) instanceof Integer) {
				selector.setValue((Integer) configuration.getProperty(id));
			}

		} else if ("choice".equals(propertyType)) {
			new Label(parent, SWT.NONE).setText(propertyName);

			final ComboViewer viewer = new ComboViewer(parent, SWT.BORDER | SWT.READ_ONLY);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(viewer.getControl());
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					String result;

					if (element instanceof IConfigurationElement && ((IConfigurationElement) element).getAttribute("label") != null) {
						result = ((IConfigurationElement) element).getAttribute("label");
					} else {
						result = super.getText(element);
					}

					return result;
				}

			});

			viewer.setInput(element.getChildren("item"));

			listeners.add(new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					if (command != PageCommand.UpdateModel) return;

					ISelection selection = viewer.getSelection();
					if (selection.isEmpty() || !(selection instanceof IStructuredSelection))
						return;

					configuration.setProperty(
					    id,
					    ((IConfigurationElement) ((IStructuredSelection) selection).getFirstElement()).getAttribute("value")
					);
				}

			});

			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					commandListenerAdapter.commandReceived(PageCommand.ValidatePage);
				}

			});

			if (configuration.isPropertyKnown(id) && configuration.getProperty(id) instanceof String) {
				String value = (String) configuration.getProperty(id);
				for (IConfigurationElement item : element.getChildren("item")) {
					if (value.equals(item.getAttribute("value")))
						viewer.setSelection(new StructuredSelection(item));
				}
			}

		} else if ("check".equals(propertyType)) {
			new Label(parent, SWT.NONE);

			final Button button = new Button(parent, SWT.CHECK);
			button.setText(propertyName);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(button);
			listeners.add(new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					if (command == PageCommand.UpdateModel)
						configuration.setProperty(id, button.getSelection());
				}

			});

			button.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					commandListenerAdapter.commandReceived(PageCommand.ValidatePage);
				}

			});

			if (configuration.isPropertyKnown(id) && configuration.getProperty(id) instanceof Boolean) {
				button.setSelection((Boolean) configuration.getProperty(id));
			}

		} else if ("text".equals(propertyType)) {
			new Label(parent, SWT.NONE).setText(propertyName);

			final Text text = new Text(parent, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
			listeners.add(new IPageCommandListener() {

				@Override
				public void commandReceived(PageCommand command) {
					if (command == PageCommand.UpdateModel)
						configuration.setProperty(id, text.getText());
				}

			});

			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					commandListenerAdapter.commandReceived(PageCommand.ValidatePage);
				}

			});

			if (configuration.isPropertyKnown(id) && configuration.getProperty(id) instanceof String) {
				text.setText((String) configuration.getProperty(id));
			}
		}
	}

	@Override
	public void commandReceived(PageCommand command) {

		for (IPageCommandListener listener : listeners) {
			listener.commandReceived(command);
		}
	}

}
