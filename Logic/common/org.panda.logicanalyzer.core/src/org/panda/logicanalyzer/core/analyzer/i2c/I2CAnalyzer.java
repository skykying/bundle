package org.panda.logicanalyzer.core.analyzer.i2c;

import java.util.Iterator;

import org.panda.logicanalyzer.core.analyzer.i2c.I2CProtocolPacket.Message;
import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * This analyzer tries to decode an I2C signal provided by a data packet. The
 * implementation of this class is heavily inspired by the <a
 * href="http://www.sump.org">SUMP logic analyzer client</a>. If anyone feels
 * this is a little bit to much of inspiration and I'm violating the GPL, drop me a
 * note.
 */
public class I2CAnalyzer {

	/**
	 * The configuration of this analyzer
	 */
	private final I2CAnalyzerConfiguration configuration;

	public I2CAnalyzer(I2CAnalyzerConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Tries to analyze the given packet. Inspect the result to check for success or failue.
	 *
	 * @param packet The packet to analyze
	 * @return The analysis result
	 */
	public I2CAnalyzerResult analyze(IDataPacket packet) {
		showMessage("analyzer begin");
		if(packet == null){
			showMessage("packet is null");
			return null;
		}else{
			showMessage("packet = " + packet.getStartTime());
		}
		
		Iterator<IFrame> iter = packet.getFrames().iterator();
		if(iter == null){
			showMessage("iframe container is null");
		}
		
		IFrame frame = iter.next();
		long lineAMask = (1 << configuration.getLineA());
		long lineBMask = (1 << configuration.getLineB());

		/*
		 * First of all scan both lines until they are high (IDLE), then
		 * the first line that goes low is the SDA line (START condition).
		 */
		for (; frame.mask(lineAMask | lineBMask) != (lineAMask | lineBMask) && iter.hasNext(); frame = iter.next());
		if (!iter.hasNext()) {
			showMessage("I2CUnsuccessfulAnalysisResult 1");
			return new I2CUnsuccessfulAnalysisResult("Found no IDLE condition");
		}

		/*
		 * Now try finding a START condition by searching for a state where not both lines are high
		 */
		for (; frame.mask(lineAMask | lineBMask) == (lineAMask | lineBMask) && frame.mask(lineAMask | lineBMask) != 0 && iter.hasNext(); frame = iter.next());
		if (!iter.hasNext()) {
			showMessage("I2CUnsuccessfulAnalysisResult 2");
			return new I2CUnsuccessfulAnalysisResult("Found no START condition");
		}

		I2CSuccessfulAnalysisResult result = new I2CSuccessfulAnalysisResult();

		/*
		 * Now check if this is really a start condition (either line A or line B must be high)
		 */
		long sdaMask = 0;
		long sclMask = 0;
		if (frame.mask(lineAMask) == lineAMask) {
			// lineA is high here and lineB is low: lineA = SCL, lineB = SDA
			sdaMask = lineBMask;
			sclMask = lineAMask;
			result.setSdaChannelNr(configuration.getLineB());
			result.setSclChannelNr(configuration.getLineA());
		} else if (frame.mask(lineBMask) == lineBMask) {
			// lineA is low and lineB is high here: lineA = SDA, lineB = SCL
			sdaMask = lineAMask;
			sclMask = lineBMask;
			result.setSdaChannelNr(configuration.getLineA());
			result.setSclChannelNr(configuration.getLineB());
		} else {
			/*
			 * This basically should not happen, since the search for the START
			 * condition is exhaustive
			 */
			return new I2CUnsuccessfulAnalysisResult("Found no START condition");
		}

		result.addPacket(new I2CProtocolPacket(frame.getTime(), Message.START));

		/*
		 * Now decode the bytes, SDA may only change when SCL is low. Otherwise
		 * it may be a repeated start condition or stop condition. If the start/stop
		 * condition is not at a byte boundary a bus error is detected. So we have to
		 * scan for SCL rises and for SDA changes during SCL is high.
		 * Each byte is followed by a 9th bit (ACK/NACK).
		 */
		long previousSCLMasked = frame.mask(sclMask);
		long previousSDAMasked = frame.mask(sdaMask);
		int bitCounter = 8;
		byte currentByte = 0;
		for (; iter.hasNext(); frame = iter.next()) {
			// detect SCL rise
			if (frame.mask(sclMask) > previousSCLMasked) {
				if (frame.mask(sdaMask) != previousSDAMasked) {
					// SDA changes, too ... that's not good
					result.addPacket(new I2CProtocolPacket(frame.getTime(), Message.BUS_ERROR));
				} else {
					if (bitCounter == 0) {
						// 8 bits have been transmitted, check for ACK/NACK
						if (frame.mask(sdaMask) == 0) {
							result.addPacket(new I2CProtocolPacket(frame.getTime(), Message.ACK));
						} else {
							result.addPacket(new I2CProtocolPacket(frame.getTime(), Message.NACK));
						}
						bitCounter = 8;
					} else {
						// store and deal with current bit
						bitCounter--;
						if (frame.mask(sdaMask) != 0) currentByte |= (1 << bitCounter);

						// current byte is complete
						if (bitCounter == 0) {
							result.addPacket(new I2CDataPacket(frame.getTime(), currentByte));
							currentByte = 0;
						}
					}
				}
			}

			// detect SDA change when SCL is high
			if (frame.mask(sclMask) == sclMask && frame.mask(sdaMask) == previousSDAMasked) {
				// SDA changes here
				if (bitCounter < 7) {
					// incomplete byte, that's a bus error
					result.addPacket(new I2CProtocolPacket(frame.getTime(), Message.BUS_ERROR));
				} else {
					if (frame.mask(sdaMask) > previousSDAMasked) {
						// SDA rises, this is a STOP condition
						result.addPacket(new I2CProtocolPacket(frame.getTime(), Message.STOP));
					} else {
						// SDA falls, this is a START condition
						result.addPacket(new I2CProtocolPacket(frame.getTime(), Message.START));
					}
					bitCounter = 8;
				}
			}

			previousSCLMasked = frame.mask(sclMask);
			previousSDAMasked = frame.mask(sdaMask);
		}

		return result;
	}

	private void showMessage(String msg) {
		System.out.println(I2CAnalyzer.class.getSimpleName() + " # " + msg);
	}

}
