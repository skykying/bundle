package org.panda.logicanalyzer.ui.internal.pipeline;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.panda.logicanalyzer.core.Activator;
import org.panda.logicanalyzer.core.IFactoryDescriptor;
import org.panda.logicanalyzer.core.pipeline.IDataSink;
import org.panda.logicanalyzer.core.pipeline.IDataSinkFactory;
import org.panda.logicanalyzer.core.pipeline.IDataSource;
import org.panda.logicanalyzer.core.pipeline.IDataSourceFactory;
import org.panda.logicanalyzer.core.pipeline.IFilter;
import org.panda.logicanalyzer.core.pipeline.IFilterFactory;
import org.panda.logicanalyzer.core.pipeline.IPipeline;
import org.panda.logicanalyzer.core.util.Pipeline;
import org.panda.logicanalyzer.ui.pipeline.IPipelineElementConfigurationProvider;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * This subsequent wizard is used during pipeline construction
 *
 *
 *
 */
public class SubsequentPipelineBuilderWizard extends Wizard {

	/**
	 * The filter wizard page lets the user add some filters
	 *
	 *
	 */
	@SuppressWarnings("unused")
	private static class FilterWizardPage extends WizardPage {

		public FilterWizardPage() {
			super("filters");

			setTitle("Add filter");
			setMessage("If you like you can now add some filter to the pipeline");
		}

		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));

			ListViewer availableFilter = new ListViewer(composite);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(availableFilter.getControl());
			availableFilter.setContentProvider(new ArrayContentProvider());
			availableFilter.setLabelProvider(new LabelProvider());
			try {
				availableFilter.setInput(new PipelineElementConfigurationProviderFactory<IFilterFactory>().getProvider(Activator.getDefault().getFilterFactories()));
			} catch (CoreException e) {
				ErrorDialog.openError(getShell(), null, null, e.getStatus());
			}

			Composite buttons = new Composite(composite, SWT.NONE);
			buttons.setLayout(new GridLayout(1, false));
			GridDataFactory.fillDefaults().grab(false, true).applyTo(buttons);

			Button addFilterButton = new Button(buttons, SWT.NONE);
			addFilterButton.setText("->");
			Button removeFilterButton = new Button(buttons, SWT.NONE);
			removeFilterButton.setText("<-");
			Button editFilterButton = new Button(buttons, SWT.NONE);
			editFilterButton.setText("conf");


			ListViewer selectedFilter = new ListViewer(composite);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(selectedFilter.getControl());
			selectedFilter.setContentProvider(new ArrayContentProvider());
			selectedFilter.setLabelProvider(new LabelProvider());

			setControl(composite);
		}

		@Override
		public boolean canFlipToNextPage() {
			return true;
		}

		public List<IFilter> getResult() {
			return Collections.emptyList();
		}

	}

	/**
	 * The sink wizard page lets the user select some sinks
	 *
	 *
	 */
	public class SinkWizardPage extends WizardPage {

		/**
		 * The result of this page
		 */
		private final List<ConfiguredPipelineElementFactoryDescriptor<IDataSinkFactory>> listsDescriptor = new LinkedList<ConfiguredPipelineElementFactoryDescriptor<IDataSinkFactory>>();

		public SinkWizardPage() {
			super("sinks");

			setTitle("Select sinks");
			setMessage("Please select at least one sink from the drop down list below");
		}

		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));

			final ComboViewer availableSinks = new ComboViewer(composite, SWT.READ_ONLY);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(availableSinks.getControl());
			availableSinks.setContentProvider(new ArrayContentProvider());
			availableSinks.setLabelProvider(new LabelProvider());
			
			try {
				Collection<IFactoryDescriptor<IDataSinkFactory>> descriptor = Activator.getDefault().getDataSinkFactories();
				PipelineElementConfigurationProviderFactory<IDataSinkFactory> configureProvider = new PipelineElementConfigurationProviderFactory<IDataSinkFactory>();
				Collection<ConfiguredPipelineElementFactoryDescriptor<IDataSinkFactory>> provider = configureProvider.getProvider(descriptor);
				availableSinks.setInput(provider);

				// availableSinks.setInput(new PipelineElementConfigurationProviderFactory<IDataSinkFactory>().getProvider(Activator.getDefault().getDataSinkFactories()));
			} catch (CoreException e) {
				ErrorDialog.openError(getShell(), null, null, e.getStatus());
			}

			Button addSinkButton = new Button(composite, SWT.NONE);
			addSinkButton.setText("Add");

			final ListViewer addedSinksViewer = new ListViewer(composite);
			GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(addedSinksViewer.getControl());
			addedSinksViewer.setContentProvider(new ArrayContentProvider());
			addedSinksViewer.setLabelProvider(new LabelProvider());
			addedSinksViewer.setInput(listsDescriptor);

			addSinkButton.addSelectionListener(new SelectionAdapter() {

				@SuppressWarnings("unchecked")
				@Override
				public void widgetSelected(SelectionEvent e) {
					IStructuredSelection selection = (IStructuredSelection) availableSinks.getSelection();
					if (selection.isEmpty()) {
						showMessage("SubsequentPipelineBuilderWizard widgetSelected !");
						return;
					}

					ConfiguredPipelineElementFactoryDescriptor<IDataSinkFactory> descriptor = (ConfiguredPipelineElementFactoryDescriptor<IDataSinkFactory>) selection.getFirstElement();
					listsDescriptor.add(descriptor);
					if (descriptor.getConfigurationProvider() != null && descriptor.getConfigurationProvider().getPage() != null) {
						addPage(descriptor.getConfigurationProvider().getPage());
						showMessage("SubsequentPipelineBuilderWizard add page " + descriptor.getConfigurationProvider().getPage().getTitle());
					}

					setPageComplete(true);
					addedSinksViewer.refresh();
				}

			});

			setPageComplete(false);

			setControl(composite);
		}

		public Collection<IDataSink> getResult() throws CoreException {
			List<IDataSink> links = new LinkedList<IDataSink>();

			for (ConfiguredPipelineElementFactoryDescriptor<IDataSinkFactory> descriptor : this.listsDescriptor) {
				links.add(descriptor.getFactoryDescriptor().getFactory().createSink(descriptor.getConfigurationProvider().getConfiguration()));
			}

			return links;
		}

	}

	/**
	 * The provider that has been selected on the previous page
	 */
	private final ConfiguredPipelineElementFactoryDescriptor<IDataSourceFactory> descriptor;

	/**
	 * The resulting pipeline. This is field is most likely to be null, if {@link #performFinish()} hasn't yet been called
	 */
	private IPipeline wpipeline;

	public SubsequentPipelineBuilderWizard(ConfiguredPipelineElementFactoryDescriptor<IDataSourceFactory> descriptor) {
		this.descriptor = descriptor;
		showMessage("SubsequentPipelineBuilderWizard");
	}

	@Override
	public void addPages() {

		WizardPage configurationPage = descriptor.getConfigurationProvider().getPage();
		if (configurationPage != null) {
			showMessage("add configuration page");
			addPage(configurationPage);
		}

		//addPage(new FilterWizardPage());
		addPage(new SinkWizardPage());
		showMessage("add SinkWizardPage page");
	}

	public IPipeline getResult() {

		if (wpipeline == null) {
			showMessage("pipeline is null");
		} else {
			showMessage("get pipeline = " +  wpipeline.getClass().getSimpleName());
		}

		return wpipeline;
	}

	@Override
	public boolean performFinish() {
		try {
			/**
			 * the createSource method was implement in class DeviceDataSource class in
			 * "org.panda.logicanalyzer.deviceintegration.internal" package
			 */
			IPipelineElementConfigurationProvider provider = descriptor.getConfigurationProvider();
			if (provider == null) {
				showMessage("provider is null");
			} else {
				showMessage("provider = " + provider.toString());
			}

			Map<String, Object> configuration = provider.getConfiguration();
			if (configuration == null) {
				showMessage("configure is null");
				return false;
			} else {
				showMessage("configuration = " + configuration.toString());
			}

			IFactoryDescriptor<IDataSourceFactory> factoryDescriptor = descriptor.getFactoryDescriptor();
			if (factoryDescriptor == null) {
				showMessage("factoryDescriptor is null");
				return false;
			} else {
				showMessage("factoryDescriptor = " + factoryDescriptor.getDescription());
			}

			IDataSourceFactory factory = factoryDescriptor.getFactory();
			if (factory == null) {
				showMessage("factory is null");
				return false;
			} else {
				showMessage("factory = " + factory.toString());
			}

			IDataSource source = factory.createSource(configuration);
			if (source == null) {
				showMessage("source is null");
				return false;
			} else {
				showMessage("source = " + source.getClass().getSimpleName());
			}
			
			/*
			 * That's what we'd use if filter were implemented:
			 * result = new Pipeline(source, ((FilterWizardPage) getPage("filters")).getResult(), ((SinkWizardPage) getPage("sinks")).getResult());
			 */
			List<IFilter> filter = Collections.emptyList();

			SinkWizardPage page = (SinkWizardPage) getPage("sinks");
			if (page == null) {
				showMessage("page is null");
				return false;
			}
			
			Collection<IDataSink> datasinks = page.getResult();
			if (datasinks == null) {
				showMessage("datasinks is null");
				return false;
			}

			wpipeline = new Pipeline(source, filter, datasinks);
			if (wpipeline == null) {
				showMessage("pipeline is null");
				return false;
			}
			
		} catch (CoreException e) {
			ErrorDialog.openError(getContainer().getShell(), null, null, e.getStatus());
			return false;
		}

		return true;
	}

	private void showMessage(String msg) {
		System.out.println(SubsequentPipelineBuilderWizard.class.getSimpleName() + " # " + msg);
	}

}
