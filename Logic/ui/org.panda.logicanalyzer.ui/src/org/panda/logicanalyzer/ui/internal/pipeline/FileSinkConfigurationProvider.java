package org.panda.logicanalyzer.ui.internal.pipeline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.panda.logicanalyzer.ui.pipeline.IPipelineElementConfigurationProvider;

/**
 * The configuration provider for the file sink.
 */
public class FileSinkConfigurationProvider implements IPipelineElementConfigurationProvider {

	/**
	 * The wizard page we provide.
	 */
	private static class Page extends WizardPage {

		/**
		 * The configuration object we're editing
		 */
		private final Map<String, Object> configuration = new HashMap<String, Object>();

		public Page() {
			super("FileSinkConfigurationPage");
		}

		public void createControl(Composite parent) {
			setTitle("File sink");
			setMessage("Please configure the file sink");

			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(3, false));

			new Label(composite, SWT.NONE).setText("Filename:");
			final Text filenameText = new Text(composite, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(filenameText);
			filenameText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					getConfiguration().put("file", ((Text) e.widget).getText());
					validatePage();
				}

			});

			Button fileDialogButton = new Button(composite, SWT.NONE);
			fileDialogButton.setText("...");
			fileDialogButton.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
					dialog.setText("Choose file");
					dialog.setFilterExtensions(new String[] { "*", "*.csv" });
					String filename = dialog.open();
					if (filename != null) filenameText.setText(filename);
				}

			});

			setControl(composite);
			setPageComplete(false);
		}

		protected void validatePage() {
			String errorMessage = null;

			if (!(getConfiguration().get("file") instanceof String)) {
				errorMessage = "File is mandatory";
			} else if (((String) getConfiguration().get("file")).length() == 0) {
				errorMessage = "File is mandatory";
			}

			setErrorMessage(errorMessage);
			setPageComplete(errorMessage == null);
		}

		public Map<String, Object> getConfiguration() {
			return configuration;
		}

	}

	/**
	 * The wizard page this provider uses
	 */
	private Page page;

	@SuppressWarnings("unchecked")
	public Map<String, Object> getConfiguration() {
		return (Map<String, Object>) (page == null ? Collections.emptyMap() : page.getConfiguration());
	}

	public WizardPage getPage() {
		if (page == null) {
			page = new Page();
		}

		return page;
	}

}
