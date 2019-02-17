package org.panda.logicanalyzer.deviceintegration.core;

/**
 * Classes implementing this interface can initialize a configuration.
 *
 *
 *
 */
public interface IConfigurationInitializer {

	/**
	 * Initialize the given configuration.
	 *
	 * @param configuration the configuration to initialize.
	 */
	public void initializeConfiguration(IMutableDeviceConfiguration configuration);

}
