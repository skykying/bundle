package org.panda.logicanalyzer.integration.sump.configuration;

import org.panda.logicanalyzer.integration.sump.ClockSource;

import org.panda.logicanalyzer.deviceintegration.core.IDevice;

/**
 * This class describes the configuration of a SUMP LA device.
 */
public class DeviceConfiguration {

	/**
	 * true if the noise filter is supposed to be enabled
	 */
	private boolean noiseFilterEnabled;
	
	/**
	 * the sampling rate to use for capturing
	 */
	private int samplingRate;
	
	/**
	 * the amount of samples to capture
	 */
	private int amountOfSamples;
	
	/**
	 * The clock source
	 */
	private ClockSource clockSource;

	/**
	 * The trigger stages. If the first element is not null, the trigger is
	 * considered enabled.
	 */
	private final TriggerStage[] triggerStages = new TriggerStage[4];
	
	/**
	 * The name of the port the device is conencted to
	 */
	private String portName;
	
	/**
	 * The baud rate the device uses
	 */
	private int baudRate;
	
	/**
	 * The channel group enablement
	 */
	private final boolean[] enabledChannelGroups = new boolean[] {
		true, true, true, true
	};
	
	/**
	 * The trigger ratio (before/after)
	 */
	private double ratio;


	/**
	 * @return true if the noise filter is enabled
	 */
	public boolean isNoiseFilterEnabled() {
		return noiseFilterEnabled;
	}

	/**
	 * @return the sampling rate to use for capturing. The value returned by
	 *         this method must be greater than zero and less than
	 *         {@link IDevice#getMaximumSamplingRate()}. It is supposed to be
	 *         a divisor of 200.000.000Hz
	 */
	public int getSamplingRate() {
		return samplingRate;
	}
	
	/**
	 * @return number of samples to capture in total, must be between 4 and 256*1024
	 */
	public int getAmountOfSamples() {
		return amountOfSamples;
	}
	
	/**
	 * @return the clock the device should use
	 */
	public ClockSource getClockSource() {
		return clockSource;
	}

	/**
	 * Sets a trigger stage. Pass null here to disable the trigger stage.
	 * 
	 * @param stageIndex The index of the stage
	 * @param stage the trigger stage or null
	 */
	public void setStage(int stageIndex, TriggerStage stage) {
		if(stageIndex < 0 || stageIndex > 3) return;
		this.triggerStages[stageIndex] = stage;
	}

	/**
	 * @return the trigger stages or null if the trigger is disabled
	 */
	public TriggerStage[] getStages() {
		return triggerStages;
	}

	/**
	 * @return the name of the port the device is conencted to
	 */
	public String getPortName() {
		return portName;
	}
	
	/**
	 * @return the baud rate the device uses
	 */
	public int getBaudRate() {
		return baudRate;
	}

	/**
	 * Sets the enablement state of a channel group.
	 * 
	 * @param nr The number of the channel group: <code>0 <= nr < 4</code>
	 * @param enabled True if the group is to be enabled, false if the group should be disabled
	 */
	public void setChannelGroupEnablement(int nr, boolean enabled) {
		if(nr >= 0 && nr < enabledChannelGroups.length)
			enabledChannelGroups[nr] = enabled;
	}
	
	/**
	 * @return an array denoting the enablement of the channel groups
	 */
	public boolean[] getChannelGroupEnablement() {
		return enabledChannelGroups;
	}

	/**
	 * Sets the trigger ratio. If the value given here is less than equal to zero or
	 * greater than one, it is ignored.
	 * 
	 * @param ratio the ratio to set
	 */
	public void setRatio(double ratio) {
		if(ratio <= 0 || ratio > 1) return;
		
		this.ratio = ratio;
	}

	/**
	 * @return the trigger ratio
	 */
	public double getRatio() {
		return ratio;
	}

	/**
	 * @return the triggerStages
	 */
	public TriggerStage[] getTriggerStages() {
		return triggerStages;
	}

	/**
	 * @param noiseFilterEnabled the noiseFilterEnabled to set
	 */
	public void setNoiseFilterEnabled(boolean noiseFilterEnabled) {
		this.noiseFilterEnabled = noiseFilterEnabled;
	}

	/**
	 * @param samplingRate the samplingRate to set
	 */
	public void setSamplingRate(int samplingRate) {
		this.samplingRate = samplingRate;
	}

	/**
	 * @param amountOfSamples the amountOfSamples to set
	 */
	public void setAmountOfSamples(int amountOfSamples) {
		this.amountOfSamples = amountOfSamples;
	}

	/**
	 * @param clockSource the clockSource to set
	 */
	public void setClockSource(ClockSource clockSource) {
		this.clockSource = clockSource;
	}

	/**
	 * @param portName the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}

	/**
	 * @param baudRate the baudRate to set
	 */
	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

}
