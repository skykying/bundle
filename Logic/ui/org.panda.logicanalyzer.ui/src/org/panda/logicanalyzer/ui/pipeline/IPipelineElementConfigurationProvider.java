package org.panda.logicanalyzer.ui.pipeline;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;

/**
 * An implementation of this interface is capable of providing configuration for
 * a certain pipeline element (such as data source, filter or data sink).
 */
public interface IPipelineElementConfigurationProvider {

	/**
	 * @return the wizard page providing the UI for the configuration or null if no configuration is required
	 */
	public WizardPage getPage();

	/**
	 * <b>It's ensured that this method is called after {@link #getPage()} has been called</b>
	 *
	 * @return The configuration provided by this implementation
	 */
	public Map<String, Object> getConfiguration();



}
