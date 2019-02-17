package org.panda.logicanalyzer.integration.sump.configuration;

/**
 * The serial trigger stage reads data along a single channel.
 *
 *
 *
 */
public class SerialTriggerStage extends TriggerStage {

	/**
	 * The channel to trigger on
	 */
	private int channelNr;

	/**
	 * @return the channelNr
	 */
	public int getChannelNr() {
		return channelNr;
	}

	/**
	 * @param channelNr the channelNr to set
	 */
	public void setChannelNr(int channelNr) {
		this.channelNr = channelNr;
	}

}
