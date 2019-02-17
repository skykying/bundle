package org.panda.logicanalyzer.integration.sump.internal;

import org.panda.logicanalyzer.deviceintegration.core.IConfigurationInitializer;
import org.panda.logicanalyzer.deviceintegration.core.IMutableDeviceConfiguration;

/**
 * Initializes the configuration with some useful default values
 */
public class ConfigurationInitializer implements IConfigurationInitializer {

	public void initializeConfiguration(IMutableDeviceConfiguration configuration) {
		configuration.setProperty("analyzer.samplingClock", "Internal");
		configuration.setProperty("analyzer.samplingRate", "100000000");
		configuration.setProperty("analyzer.channelGroup00", true);
		configuration.setProperty("analyzer.channelGroup01", true);
		configuration.setProperty("analyzer.channelGroup02", true);
		configuration.setProperty("analyzer.channelGroup03", true);
		configuration.setProperty("analyzer.recordingSize", "4096");
		configuration.setProperty("analyzer.noiseFilter", true);
		configuration.setProperty("trigger.enable", false);
		configuration.setProperty("trigger.ratio", "0.75");
		for (int i = 0; i < 4; i++) {
			configuration.setProperty("triggerStage0" + i + ".enablement", "Disabled");
			configuration.setProperty("triggerStage0" + i + ".mode", i == 0 ? "true" : "false");
			configuration.setProperty("triggerStage0" + i + ".delay", "0");
			configuration.setProperty("triggerStage0" + i + ".mask", 0x00);
			configuration.setProperty("triggerStage0" + i + ".value", 0x00);
			configuration.setProperty("triggerStage0" + i + ".channelNr", 0x00);
		}
	}

}
