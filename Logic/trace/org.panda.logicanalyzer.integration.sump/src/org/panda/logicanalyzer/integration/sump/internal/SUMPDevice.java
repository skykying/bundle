package org.panda.logicanalyzer.integration.sump.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.util.BinaryDataPacket;
import org.panda.logicanalyzer.deviceintegration.core.IDevice;
import org.panda.logicanalyzer.deviceintegration.core.IDeviceConfiguration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.panda.logicanalyzer.integration.sump.Activator;
import org.panda.logicanalyzer.integration.sump.ClockSource;
import org.panda.logicanalyzer.integration.sump.configuration.DeviceConfiguration;
import org.panda.logicanalyzer.integration.sump.configuration.ParallelTriggerStage;
import org.panda.logicanalyzer.integration.sump.configuration.SerialTriggerStage;
import org.panda.logicanalyzer.integration.sump.configuration.TriggerStage;

/**
 * The concrete device implementation.
 */
public class SUMPDevice implements IDevice {

	/**
	 * The trigger state is build from a {@link DeviceConfiguration} and sets
	 * appropriate values which can be sent to the device.
	 */
	private class TriggerState {

		/**
		 * The trigger masks of all four stages
		 */
		private final int[] triggerMask = new int[SUMPDevice.TRIGGER_STAGES];

		/**
		 * The trigger values of all four stages
		 */
		private final int[] triggerValue = new int[SUMPDevice.TRIGGER_STAGES];

		/**
		 * The trigger configuration of all four stages
		 */
		private final int[] triggerConfig = new int[SUMPDevice.TRIGGER_STAGES];


		public TriggerState(TriggerStage[] stages) {
			for (int i = 0; i < SUMPDevice.TRIGGER_STAGES && i < stages.length; i++) {
				if (stages[i] instanceof ParallelTriggerStage) {
					setParallelTrigger(i, (ParallelTriggerStage) stages[i]);
				} else if (stages[i] instanceof SerialTriggerStage) {
					setSerialTrigger(i, (SerialTriggerStage) stages[i]);
				}
			}
		}


		/**
		 * Configures a serial trigger stage
		 *
		 * @param stageIndex The index of the stage
		 * @param stage the stage configuration
		 */
		private void setSerialTrigger(int stageIndex, SerialTriggerStage stage) {
			internalConfigureStage(stageIndex, stage);
			triggerConfig[stageIndex] |= (stage.getChannelNr() << 20) & TRIGGER_CHANNELMASK;
			triggerConfig[stageIndex] |= TRIGGER_SERIAL;
		}

		/**
		 * Configures the trigger stage based on the configuration given. This
		 * method does not distinct between {@link ParallelTriggerStage} and
		 * {@link SerialTriggerStage}. So if you want these special cases to be
		 * dealt with, deal with them yourself.
		 *
		 * @param stageIndex
		 *            The index of the stage to change
		 * @param stage
		 *            The stage to set
		 */
		private void internalConfigureStage(int stageIndex, TriggerStage stage) {
			if (!demux) {
				triggerMask[stageIndex] = stage.getMask();
				triggerValue[stageIndex] = stage.getValue();
			} else {
				triggerMask[stageIndex] = stage.getMask() & 0xffff;
				triggerValue[stageIndex] = stage.getValue() & 0xffff;
				triggerMask[stageIndex] |= triggerMask[stageIndex] << 16;
				triggerValue[stageIndex] |= triggerValue[stageIndex] << 16;
			}

			triggerConfig[stageIndex] = 0;
			triggerConfig[stageIndex] |= stage.getDelay() & TRIGGER_DELAYMASK;
			triggerConfig[stageIndex] |= (stage.getLevel() << 16) & TRIGGER_LEVELMASK;
			if (!stage.isAdvanceStage()) triggerConfig[stageIndex] |= TRIGGER_CAPTURE;
		}

		/**
		 * Configures a parallel trigger stage
		 *
		 * @param stageIndex The index of the stage
		 * @param stage the stage configuration
		 */
		private void setParallelTrigger(int stageIndex, ParallelTriggerStage stage) {
			internalConfigureStage(stageIndex, stage);
		}

		/**
		 * @return the triggerMask
		 */
		public int[] getTriggerMask() {
			return triggerMask;
		}

		/**
		 * @return the triggerValue
		 */
		public int[] getTriggerValue() {
			return triggerValue;
		}

		/**
		 * @return the triggerConfig
		 */
		public int[] getTriggerConfig() {
			return triggerConfig;
		}

	}

	/** set trigger mask */
	private final static int SETTRIGMASK = 0xc0;
	/** set trigger value */
	private final static int SETTRIGVAL = 0xc1;
	/** set trigger configuration */
	private final static int SETTRIGCFG = 0xc2;
	/** set clock divider */
	private final static int SETDIVIDER = 0x80;
	/** set sample counters */
	private final static int SETSIZE = 0x81;
	/** set flags */
	private final static int SETFLAGS = 0x82;

	/** reset analyzer */
	private final static int RESET = 0x00;
	/** arm trigger / run device */
	private final static int RUN = 0x01;
	/** ask for device id */
	private final static int ID = 0x02;
	/** continue data transmission to host */
//	private final static int XON = 0x11;
	/** pause data transmission to host */
//	private final static int XOFF = 0x13;


	private final static int FLAG_DEMUX = 0x00000001;		// demultiplex
	private final static int FLAG_FILTER = 0x00000002;		// noise filter
	private final static int FLAG_DISABLE_G0 = 0x00000004;	// disable channel group 0
//	private final static int FLAG_DISABLE_G1 = 0x00000008;	// disable channel group 1
	private final static int FLAG_DISABLE_G2 = 0x00000010;	// disable channel group 2
//	private final static int FLAG_DISABLE_G3 = 0x00000020;	// disable channel group 3
	private final static int FLAG_EXTERNAL = 0x00000040;	// disable channel group 3
	private final static int FLAG_INVERTED = 0x00000080;	// disable channel group 3

	private final static int TRIGGER_DELAYMASK = 0x0000ffff;// mask for delay value
	private final static int TRIGGER_LEVELMASK = 0x00030000;// mask for level value
	private final static int TRIGGER_CHANNELMASK = 0x01f00000;// mask for level value
	private final static int TRIGGER_SERIAL = 0x04000000;	// trigger operates in serial mode
	private final static int TRIGGER_CAPTURE = 0x08000000;	// trigger will start capture when fired

	public final static int CLOCK = 100000000;	// device clock in Hz
	public final static int TRIGGER_STAGES = 4; // number of trigger stages


	/**
	 * The configuration of this device.
	 */
	private DeviceConfiguration configuration;

	/**
	 * Whether we use the demux functionality or not
	 */
	private boolean demux;

	/**
	 * The trigger state. Set by {@link #initConfigurationState()}.
	 */
	private TriggerState trigger;

	/**
	 * The divider we'll use to set the sampling frequency. Set by {@link #initConfigurationState()}.
	 */
	private int divider;

	/**
	 * True if there is a capturing in progress
	 */
	private boolean running;


	/**
	 * Initializes the internal configuration state (such as {@link #demux}).
	 */
	private void initConfigurationState() {
		if (configuration.getSamplingRate() > CLOCK) {
			demux = true;
			divider = (2 * CLOCK / configuration.getSamplingRate()) - 1;
		} else {
			demux = false;
			divider = (CLOCK / configuration.getSamplingRate()) - 1;
		}

		boolean triggerEnabled = false;
		for (int i = 0; i < configuration.getStages().length; i++) {
			triggerEnabled |= configuration.getStages()[i] != null;
		}
		if (triggerEnabled) {
			trigger = new TriggerState(configuration.getStages());
		}
	}

	/**
	 * Sets the device configuration. This method must be called before calling
	 * {@link #capture(InputStream, OutputStream)}.
	 *
	 * @param configuration
	 *            The configuration to set
	 * @return The configurations validitiy. If the status is not
	 *         {@link IStatus#isOK()}, calling
	 *         {@link #capture(InputStream, OutputStream)} will most likely
	 *         result in an exception.
	 */
	private IStatus setConfiguration(DeviceConfiguration configuration) {
		this.configuration = configuration;
		initConfigurationState();
		return validateConfiguration();
	}

	public int getMaximumSamplingRate() {
		return (2 * CLOCK);
	}

	/**
	 * Validates the configuration of this device.
	 *
	 * @return A status describing the device validity. If the returned status
	 *         is {@link IStatus#OK}, the device is assumed to be ready to
	 *         capture data.
	 */
	private IStatus validateConfiguration() {
		if (configuration.getSamplingRate() > getMaximumSamplingRate()) {
			return createErrorStatus("Sampling rate too large");
		} else if (getAmountOfEnabledChannels() > getAvailableChannelCount()) {
			return createErrorStatus("Not enough channels available to statisfy the configuration");
		} else if (configuration.isNoiseFilterEnabled() && !isNoiseFilterAvailable()) {
			return createErrorStatus("Noise filter enabled although not available");
		}

		return Status.OK_STATUS;
	}

	/**
	 * Creates an error status with the given message.
	 *
	 * @param message The message to place in the status
	 * @return The newly created status object
	 */
	private IStatus createErrorStatus(String message) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, message);
	}

	/**
	 * Returns the number of available channels in current configuration.
	 *
	 * @return number of available channels
	 */
	private int getAvailableChannelCount() {
		int result;

		if (demux && configuration.getClockSource() == ClockSource.Internal) {
			result = 16;
		} else {
			result = 32;
		}

		return result;
	}

	/**
	 * @return the amount of channels enabled in the {@link #configuration}.
	 */
	private int getAmountOfEnabledChannels() {
		int result = 0;

		for (boolean enabled : configuration.getChannelGroupEnablement())
			if (enabled) result += 8;

		return result;
	}

	/**
	 * Returns wether or not the noise filter can be used in the current configuration.
	 *
	 * @return <code>true</code> when noise filter is available, <code>false</code> otherwise
	 */
	private boolean isNoiseFilterAvailable() {
		return (!demux && configuration.getClockSource() == ClockSource.Internal);
	}

	public IDataPacket capture(InputStream in, OutputStream out) throws CoreException {
		if (configuration == null) {
			throw new CoreException(createErrorStatus("No configuration set"));
		} else if (running) {
			throw new CoreException(createErrorStatus("Device is already running"));
		}

		// read dangling bytes from the serial line
		try {
			while (in.available() > 0) {
				in.read();
				Thread.sleep(1);
			}
		} catch (IOException e) {
		} catch (InterruptedException e) {
		}

		DeviceCommunicator communicator = new DeviceCommunicator(configuration, out, in);

		running = true;
		try {
			// send reset 5 times because in worst case first 4 are interpreted as data of long command
			for (int i = 0; i < 5; i++)
				communicator.sendCommand(RESET);

			// check if device is ready
			communicator.sendCommand(ID);
			int id = 0;
			try {
				id = communicator.readInteger();
			} catch (Exception e) { /* don't care */ }
			if (id == 0x534c4130) { // SLA0
				throw new CoreException(createErrorStatus("Device is obsolete. Please upgrade Firmware."));
			} else if (id != 0x534c4131) { // SLA1
				throw new CoreException(createErrorStatus("Device not found."));
			}

			// configure device
			int stopCounter = (int)(configuration.getAmountOfSamples() * configuration.getRatio());
			int readCounter = configuration.getAmountOfSamples();
			int effectiveStopCounter;
			if (trigger != null) {
				for (int i = 0; i < TRIGGER_STAGES; i++) {
					communicator.sendCommand(SETTRIGMASK + 4 * i, trigger.getTriggerMask()[i]);
					communicator.sendCommand(SETTRIGVAL + 4 * i, trigger.getTriggerValue()[i]);
					communicator.sendCommand(SETTRIGCFG + 4 * i, trigger.getTriggerConfig()[i]);
				}
				effectiveStopCounter = stopCounter;
			} else {
				communicator.sendCommand(SETTRIGMASK, 0);
				communicator.sendCommand(SETTRIGVAL, 0);
				communicator.sendCommand(SETTRIGCFG, TRIGGER_CAPTURE);
				effectiveStopCounter = readCounter;
			}
			communicator.sendCommand(SETDIVIDER, divider);

			int flags = 0;
			if (configuration.getClockSource().isExternal()) {
				flags |= FLAG_EXTERNAL;
				if (configuration.getClockSource() == ClockSource.ExternalFallingEdge)
					flags |= FLAG_INVERTED;
			}
			if (demux && !configuration.getClockSource().isExternal()) {
				flags |= FLAG_DEMUX;
				for (int i = 0; i < 2; i++)
					if (!configuration.getChannelGroupEnablement()[i]) {
						flags |= FLAG_DISABLE_G0 << i;
						flags |= FLAG_DISABLE_G2 << i;
					}
				communicator.sendCommand(SETSIZE, (((effectiveStopCounter - 8) & 0x7fff8) << 13) | (((readCounter & 0x7fff8) >> 3) - 1));
			} else {
				if (configuration.isNoiseFilterEnabled() && isNoiseFilterAvailable()){
					flags |= FLAG_FILTER;
				}
				for (int i = 0; i < configuration.getChannelGroupEnablement().length; i++){
					if (!configuration.getChannelGroupEnablement()[i]){
						flags |= FLAG_DISABLE_G0 << i;
					}
				}
				
				communicator.sendCommand(SETSIZE, (((effectiveStopCounter - 4) & 0x3fffc) << 14) | (((readCounter & 0x3fffc) >> 2) - 1));
			}
			communicator.sendCommand(SETFLAGS, flags);
			communicator.sendCommand(RUN);

			// check if data needs to be multiplexed
			int channels;
			int samples;
			if (demux && !configuration.getClockSource().isExternal()) {
				channels = 16;
				samples = (readCounter & 0xffff8);
			} else {
				channels = 32;
				samples = (readCounter & 0xffffc);
			}

			int[] buffer = new int[samples];
			// wait for first byte forever (trigger could cause long delay)
			buffer[samples - 1] =  communicator.readSample(channels);

			// read all other samples
			try {
				for (int i = samples - 2; i >= 0; i--) {
					buffer[i] = communicator.readSample(channels);
				}
			} catch (CoreException e) {
				// we don't care, for debugging only
			}

			// collect additional information for CapturedData
			int pos, rate;
			if (trigger == null) {
				pos = -1;
			} else {
				pos = readCounter - stopCounter - 3 - (4 / (divider + 1)) - (demux ? 5 : 0);
			}
			if (!configuration.getClockSource().isExternal()) {
				rate = demux ? 2 * CLOCK / (divider + 1) : CLOCK / (divider + 1);
			} else {
				rate = -1;
			}

			long frameDistance = 1000000000L / rate;
			boolean[] channelEnablement = new boolean[channels];
			for (int i = 0; i < channelEnablement.length; i++) {
				channelEnablement[i] = configuration.getChannelGroupEnablement()[i / 8];
			}

			return new BinaryDataPacket(buffer, pos, frameDistance, channels, channelEnablement);
		} finally {
			running = false;
		}
	}

	public void initialize(IDeviceConfiguration configuration) throws CoreException {
		DeviceConfiguration config = new DeviceConfiguration();

		config.setAmountOfSamples(Integer.parseInt((String) configuration.getProperty("analyzer.recordingSize")));
		for (int i = 0; i < 4; i++) {
			config.setChannelGroupEnablement(i, (Boolean) configuration.getProperty("analyzer.channelGroup0" + i));
		}
		config.setClockSource(ClockSource.valueOf((String) configuration.getProperty("analyzer.samplingClock")));
		config.setNoiseFilterEnabled((Boolean) configuration.getProperty("analyzer.noiseFilter"));
		config.setRatio(Double.parseDouble((String) configuration.getProperty("trigger.ratio")));
		config.setSamplingRate(Integer.parseInt((String) configuration.getProperty("analyzer.samplingRate")));
		if ((Boolean) configuration.getProperty("trigger.enable")) {
			for (int i = 0; i < 4; i++) {
				String stageId = "triggerStage0" + i + ".";
				String enablement = (String) configuration.getProperty(stageId + "enablement");

				TriggerStage stage = null;
				if ("Parallel".equals(enablement)) {
					stage = new ParallelTriggerStage();
					stage.setAdvanceStage("true".equals(configuration.getProperty(stageId + "mode")));
					stage.setDelay(Integer.parseInt(configuration.getProperty(stageId) + "delay"));
					stage.setMask((Integer) configuration.getProperty(stageId + "mask"));
					stage.setValue((Integer) configuration.getProperty(stageId + "value"));
				} else if ("Serial".equals(enablement)) {
					stage = new SerialTriggerStage();
					stage.setAdvanceStage("true".equals(configuration.getProperty(stageId + "mode")));
					stage.setDelay(Integer.parseInt((String)configuration.getProperty(stageId + "delay")));
					stage.setMask((Integer) configuration.getProperty(stageId + "mask"));
					stage.setValue((Integer) configuration.getProperty(stageId + "value"));
					((SerialTriggerStage) stage).setChannelNr(Integer.parseInt((String) configuration.getProperty(stageId + "channelNr")));
				}
				config.setStage(i, stage);
			}
		}

		setConfiguration(config);
	}

}
