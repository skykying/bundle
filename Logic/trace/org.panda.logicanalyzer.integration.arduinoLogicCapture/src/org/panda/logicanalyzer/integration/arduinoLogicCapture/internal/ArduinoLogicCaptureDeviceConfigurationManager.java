package org.panda.logicanalyzer.integration.arduinoLogicCapture.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.panda.logicanalyzer.deviceintegration.core.IConfigurationInitializer;
import org.panda.logicanalyzer.deviceintegration.core.IConfigurationValidator;
import org.panda.logicanalyzer.deviceintegration.core.IDeviceConfiguration;
import org.panda.logicanalyzer.deviceintegration.core.IMutableDeviceConfiguration;

/**
 * The configuration initializer that provides our default configuration
 */
public class ArduinoLogicCaptureDeviceConfigurationManager implements IConfigurationInitializer, IConfigurationValidator {

	/**
	 * The ID of the plugin this class is contained in
	 */
	static final String PLUGIN_ID = "org.panda.logicanalyzer.integration.arduinoLogicCapture";

	@Override
	public void initializeConfiguration(IMutableDeviceConfiguration configuration) {
		configuration.setProperty("analyzer.time", "5");
		configuration.setProperty("analyzer.mode", "stateChange");
		configuration.setProperty("analyzer.level", "x");
		configuration.setProperty("analyzer.clockSpeed", "16000000");
		configuration.setProperty("analyzer.level", "x");
		configuration.setProperty("analyzer.prescaler", "1024");
	}

	@Override
	public IStatus validateConfiguration(IDeviceConfiguration configuration) {
		IStatus result = Status.OK_STATUS;
		int time = 0; int prescaler;

		try {
			time = Integer.parseInt((String) configuration.getProperty("analyzer.time"));
			if (time <= 0) {
				result = new Status(IStatus.ERROR, PLUGIN_ID, "Capture time must be greater than zero");
			}
		} catch (NumberFormatException e) {
			result = new Status(IStatus.ERROR, PLUGIN_ID, "Capture time is not a number");
		}

		if (configuration.getProperty("analyzer.prescaler") != null) {
			prescaler = Integer.parseInt((String) configuration.getProperty("analyzer.prescaler"));
			double maxCaptureTime = (Math.pow(2, 16) / (16000000 / prescaler)) * 1000;
			if (time > maxCaptureTime) {
				result = new Status(IStatus.ERROR, PLUGIN_ID, "Capture time too long for the selected sample rate (max. " + maxCaptureTime + " ms)");
			}
		}

		return result;
	}

}
