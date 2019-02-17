package org.panda.logicanalyzer.core.analyzer.i2c;

import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.core.analyzer.AnalysisResultProperty;
import org.panda.logicanalyzer.core.analyzer.IAnalysisResult;
import org.panda.logicanalyzer.core.analyzer.Severity;
import org.panda.logicanalyzer.core.analyzer.TimedFrame;
import org.panda.logicanalyzer.core.analyzer.i2c.I2CProtocolPacket.Message;

/**
 * This class describes an unsuccessful analysis result. You may grab the resulting
 * data using the various getter methods of this class.
 */
public class I2CSuccessfulAnalysisResult extends I2CAnalyzerResult implements IAnalysisResult {

	/**
	 * The number of the SDA channel
	 */
	private int sdaChannelNr;

	/**
	 * The number of the SCL channel
	 */
	private int sclChannelNr;

	/**
	 * The packets discovered during analysis
	 */
	private final List<I2CPacket> packets = new LinkedList<I2CPacket>();

	/**
	 * Adds a packet to this result. If the packet's already in this result it
	 * will be added again.
	 *
	 * @param packet
	 *            the packet to add
	 */
	void addPacket(I2CPacket packet) {
		packets.add(packet);
	}

	void setSdaChannelNr(int sdaChannelNr) {
		this.sdaChannelNr = sdaChannelNr;
	}

	/**
	 * @return The number of the SDA channel
	 */
	public int getSDAChannelNr() {
		return sdaChannelNr;
	}

	void setSclChannelNr(int sclChannelNr) {
		this.sclChannelNr = sclChannelNr;
	}

	/**
	 * @return the number of the SCL channel
	 */
	public int getSCLChannelNr() {
		return sclChannelNr;
	}

	/**
	 * @return the packets discovered during the analysis
	 */
	public List<I2CPacket> getPackets() {
		return packets;
	}

	@Override
	public String getTitle() {
		return "I2C analysis results";
	}

	@Override
	public List<TimedFrame> getFrames() {
		List<TimedFrame> result = new LinkedList<TimedFrame>();

		for (I2CPacket packet : packets) {
			Object adapter = packet.getAdapter(TimedFrame.class);
			if (adapter instanceof TimedFrame) result.add((TimedFrame) adapter);
		}

		return result;
	}

	@SuppressWarnings("serial")
	public List<AnalysisResultProperty> getProperties() {
		int nonFinalAmountOfBusErrors = 0;
		int nonFinalAmountOfBytesReceived = 0;
		for (I2CPacket packet : packets) {
			if (packet instanceof I2CProtocolPacket) {
				I2CProtocolPacket protocolPacket = ((I2CProtocolPacket) packet);
				if (protocolPacket.getMessage() == Message.BUS_ERROR) nonFinalAmountOfBusErrors++;
			} else if (packet instanceof I2CDataPacket) {
				nonFinalAmountOfBytesReceived++;
			}
		}
		final int amountOfBusErrors = nonFinalAmountOfBusErrors;
		final int amountOfBytesReceived = nonFinalAmountOfBytesReceived;

		return new LinkedList<AnalysisResultProperty>() {
			{
				add(new AnalysisResultProperty(Severity.Info, "SDA channel", String.valueOf(sdaChannelNr)));
				add(new AnalysisResultProperty(Severity.Info, "SCL channel", String.valueOf(sclChannelNr)));
				if (amountOfBusErrors > 0) add(new AnalysisResultProperty(Severity.Warning, "# bus errors", String.valueOf(amountOfBusErrors)));
				add(new AnalysisResultProperty(Severity.Info, "bytes received", String.valueOf(amountOfBytesReceived)));
			}
		};
	}



}
