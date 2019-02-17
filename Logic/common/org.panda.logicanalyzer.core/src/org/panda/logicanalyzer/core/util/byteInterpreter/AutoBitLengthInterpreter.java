package org.panda.logicanalyzer.core.util.byteInterpreter;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * This class tries to identify the bit length automatically by computing
 * the smalled low or high.
 */
class AutoBitLengthInterpreter extends BaseBitInterpretationStrategy {

	/**
	 * The previously computed bit length
	 */
	private final long bitLength;


	public AutoBitLengthInterpreter(IDataPacket packet, int channelNr) {
		super(packet, channelNr);

		this.bitLength = findBithLength(packet, channelNr);
	}

	private long findBithLength(IDataPacket packet, int channelNr) {
		long result = Long.MAX_VALUE;

		long prevTime = -1;
		boolean prevValue = false;
		for (IFrame frame : packet.getFrames()) {
			if (prevTime < 0) {
				prevValue = frame.getValue(channelNr);
				prevTime = frame.getTime();
			}

			if (prevValue != frame.getValue(channelNr)) {
				long dt = frame.getTime() - prevTime;
				if (prevTime >= 0 && dt < result) {
					result = dt;
				}

				prevTime = frame.getTime();
				prevValue = frame.getValue(channelNr);
			}
		}

		return result == Long.MAX_VALUE ? 0 : result;
	}

	@Override
	public long getBitLength() {
		return bitLength;
	}

}
