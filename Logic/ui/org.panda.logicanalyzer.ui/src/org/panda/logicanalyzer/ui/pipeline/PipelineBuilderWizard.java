package org.panda.logicanalyzer.ui.pipeline;

import org.panda.logicanalyzer.core.Activator;
import org.panda.logicanalyzer.core.IFactoryDescriptor;
import org.panda.logicanalyzer.core.pipeline.IDataSourceFactory;
import org.panda.logicanalyzer.core.pipeline.IPipeline;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.panda.logicanalyzer.ui.internal.pipeline.ConfiguredPipelineElementFactoryDescriptor;
import org.panda.logicanalyzer.ui.internal.pipeline.PipelineElementConfigurationProviderFactory;
import org.panda.logicanalyzer.ui.internal.pipeline.SubsequentPipelineBuilderWizard;

/**
 * The pipeline builder wizard kicks of a series of wizards used to build
 * a pipeline.
 */
public class PipelineBuilderWizard extends Wizard {

	/**
	 * Used to create the subsequent wizard. Instances of this class
	 * also modify the {@link PipelineBuilderWizard#subsequentWizard} field
	 * accordingly. will be used by below "SelectSourcePage" page
	 */
	private class SubsequentWizardNode implements IWizardNode {

		/**
		 * The descriptor of the pipeline element we use
		 */
		private final ConfiguredPipelineElementFactoryDescriptor<IDataSourceFactory> descriptor;

		/**
		 * Our subsequent wizard. This field is only populated iff {@link #getWizard()} has been called yet
		 */
		private SubsequentPipelineBuilderWizard wizard;

		public SubsequentWizardNode(ConfiguredPipelineElementFactoryDescriptor<IDataSourceFactory> descriptor) {

			this.descriptor = descriptor;
			System.out.println("SubsequentWizardNode");
		}

		//@Override
		public void dispose() {

			if (wizard != null) {
				wizard.dispose();
			}
		}

		//@Override
		public Point getExtent() {
			return new Point(-1, -1);
		}

		//@Override
		public IWizard getWizard() {

			/**
			 * instance the pipeline instance from the configure
			 */
			if (wizard == null) {
				wizard = new SubsequentPipelineBuilderWizard(descriptor);
				subsequentWizard = wizard;
				System.out.println("getWizard");
			}

			return wizard;
		}

		// the super method, used to check the wizard can finished
		//@Override
		public boolean isContentCreated() {
			if (wizard == null) {
				System.out.println("content is not created");
				return false;
			} else {
				System.out.println("content is created");
				return true;
			}
		}

	}

	/**
	 * This page lets us select the data source of our pipeline.
	 */
	public class SelectSourcePage extends WizardSelectionPage {

		public SelectSourcePage(String id) {
			super(id);
			setTitle("Select source");
			setMessage("Please select the source you want to use from the list below");
		}

		//@Override
		public void createControl(Composite parent) {

			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));

			ListViewer viewer = new ListViewer(composite, SWT.FULL_SELECTION | SWT.BORDER);
			GridDataFactory.fillDefaults().grab(false, true).span(1, 2).applyTo(viewer.getControl());
			viewer.setContentProvider(new ArrayContentProvider());
			viewer.setLabelProvider(new LabelProvider());

			try {
				/**
				 * get the configure and set to view source
				 */
				Collection<IFactoryDescriptor<IDataSourceFactory>> descriptor = Activator.getDefault().getDataSourceFactories();
				PipelineElementConfigurationProviderFactory<IDataSourceFactory> configureProvider = new PipelineElementConfigurationProviderFactory<IDataSourceFactory>();
				Collection<ConfiguredPipelineElementFactoryDescriptor<IDataSourceFactory>> provider = configureProvider.getProvider(descriptor);
				viewer.setInput(provider);
			} catch (CoreException e) {
				ErrorDialog.openError(getShell(), null, null, e.getStatus());
			}

			final Label nameLabel = new Label(composite, SWT.NONE);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(nameLabel);

			final Text descriptionField = new Text(composite, SWT.BORDER | SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(descriptionField);
			descriptionField.setEditable(false);

			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@SuppressWarnings("unchecked")
				//@Override
				public void selectionChanged(SelectionChangedEvent event) {
					IStructuredSelection selection = (IStructuredSelection) event.getSelection();
					if (selection.isEmpty()) return;

					ConfiguredPipelineElementFactoryDescriptor<IDataSourceFactory> descriptor = (ConfiguredPipelineElementFactoryDescriptor<IDataSourceFactory>) selection.getFirstElement();
					nameLabel.setText(descriptor.getFactoryDescriptor().getName());

					System.out.println("pipeline name = " + descriptor.getFactoryDescriptor().getName());

					descriptionField.setText(descriptor.getFactoryDescriptor().getDescription());

					setSelectedNode(new SubsequentWizardNode(descriptor));
				}

			});

			// Necessary line for WizardSelectionPage class
			setControl(composite);
		}

	}

	/**
	 * http://www.cnblogs.com/DreamDrive/p/4167605.html
	 * We need to remember who we delegated our work to, as we'll grab the
	 * resulting pipeline from this wizard.
	 */
	private SubsequentPipelineBuilderWizard subsequentWizard;

	public PipelineBuilderWizard() {
		System.out.println("PipelineBuilderWizard");

		// super method, set the previous and next button enabled.
		setForcePreviousAndNextButtons(true);
	}

	/**
	 * add pages to wizard, the page will be display beyond the add order
	 */
	@Override
	public void addPages() {
		addPage(new SelectSourcePage("foo"));
	}

	@Override // check the wizard can be finished
	public boolean performFinish() {
		return getPipeline() != null;
	}

	/**
	 * Returns the pipeline as defined by this wizard or null if the pipeline's not
	 * yet put together.
	 *
	 * @return the pipeline or null
	 */
	public IPipeline getPipeline() {
		if (subsequentWizard == null) {
			System.out.println("subsequentWizard is null");
			return null;
		} else {
			IPipeline pipeline = subsequentWizard.getResult();
			if (pipeline == null) {
				System.out.println("pipeline of subsequentWizard is null too !");
			}
			return subsequentWizard.getResult();
		}
	}

}
