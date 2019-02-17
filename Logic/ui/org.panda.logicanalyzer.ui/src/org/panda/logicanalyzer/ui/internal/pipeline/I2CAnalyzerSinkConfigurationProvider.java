package org.panda.logicanalyzer.ui.internal.pipeline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.panda.logicanalyzer.ui.pipeline.IPipelineElementConfigurationProvider;

/**
 * The configuration provider for the file sink.
 *
 *
 *
 */
public class I2CAnalyzerSinkConfigurationProvider implements IPipelineElementConfigurationProvider {

	/**
	 * The wizard page we provide.
	 *
	 *
	 *
	 */
	private static class Page extends WizardPage {

		/**
		 * The configuration object we're editing
		 */
		private final Map<String, Object> configuration = new HashMap<String, Object>();

		public Page() {
			super("I2CAnalyzerSinkConfigurationPage");
		}

		public void createControl(Composite parent) {
			setTitle("I2C analyzer sink");
			setMessage("Please configure the I2C analyzer sink");

			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));

			new Label(composite, SWT.NONE).setText("Line A channel nr:");
			final Text lineAText = new Text(composite, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(lineAText);
			lineAText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent event) {
					try {
						getConfiguration().put("channelA", Integer.parseInt(((Text) event.widget).getText()));
						validatePage();
					} catch (NumberFormatException e) {
						// don't care
					}
				}

			});

			new Label(composite, SWT.NONE).setText("Line B channel nr:");
			final Text lineBText = new Text(composite, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(lineBText);
			lineBText.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent event) {
					try {
						getConfiguration().put("channelB", Integer.parseInt(((Text) event.widget).getText()));
						validatePage();
					} catch (NumberFormatException e) {
						// don't care
					}
				}

			});

			setControl(composite);
			setPageComplete(false);
		}

		protected void validatePage() {
			String errorMessage = null;

			if (!(getConfiguration().get("channelA") instanceof Integer)) {
				errorMessage = "Line A channel nr is mandatory";
			} else if (!(getConfiguration().get("channelB") instanceof Integer)) {
				errorMessage = "Line B channel nr is mandatory";
			} else if ((Integer)getConfiguration().get("channelA") < 0) {
				errorMessage = "Line A channel nr must be a positive value";
			} else if ((Integer)getConfiguration().get("channelB") < 0) {
				errorMessage = "Line B channel nr must be a positive value";
			} else if ((Integer)getConfiguration().get("channelA") >= 64) {
				errorMessage = "Line A channel nr must not be greater than, equal to 64";
			} else if ((Integer)getConfiguration().get("channelB") >= 64) {
				errorMessage = "Line B channel nr must not be greater than, equal to 64";
			}

			setErrorMessage(errorMessage);
			if(errorMessage == null){
				showMessage("validatepage is ok");
			}
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

	public Map<String, Object> getConfiguration() {
		if(page ==null){
			showMessage("page is null");
			
			return Collections.emptyMap();
		}else{
			showMessage("configure = "+page.getConfiguration());
			
			return page.getConfiguration();
		}
	}

	public WizardPage getPage() {
		if (page == null) {
			showMessage("page is null @getPage");
			page = new Page();
		}

		return page;
	}

	private static void showMessage(String msg) {
		System.out.println(I2CAnalyzerSinkConfigurationProvider.class.getSimpleName() + " # " + msg);
	}
}
