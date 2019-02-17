package org.panda.logicanalyzer.deviceintegration.core;

import org.eclipse.core.runtime.IStatus;

/**
 * A configuration validator does what its name says. It validates
 * configurations.
 *
 *
 *
 */
public interface IConfigurationValidator {

	/**
	 * Validates the configuration and returns a status which reflects the
	 * validity of the configuration. If the configuration is valid, an
	 * {@link IStatus#isOK()} status has to be returned.
	 *
	 * @param configuration the configuration to validate
	 * @return the status of the configurations validity
	 */
	public IStatus validateConfiguration(IDeviceConfiguration configuration);

}
