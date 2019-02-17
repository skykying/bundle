package org.panda.logicanalyzer.core.util.byteInterpreter;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;

/**
 * The fixed bit length interpreter has its bit length set by a third party.
 */
public class FixedBitLengthInterpreter extends BaseBitInterpretationStrategy {

	/**
	 * Our bit length
	 */
	private final long bitLength;

	public FixedBitLengthInterpreter(IDataPacket packet, int channelNr, long bitLength) {
		super(packet, channelNr);
		this.bitLength = bitLength;
	}

	@Override
	public long getBitLength() {
		return bitLength;
	}

}
