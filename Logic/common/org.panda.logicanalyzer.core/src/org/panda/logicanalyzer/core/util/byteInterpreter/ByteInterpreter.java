package org.panda.logicanalyzer.core.util.byteInterpreter;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.panda.logicanalyzer.core.Activator;
import org.panda.logicanalyzer.core.pipeline.IDataPacket;

/**
 * The byte interpreter takes a data packet and tries to interpret a channel
 * value as bit stream.
 */
public class ByteInterpreter {

	/**
	 * Our current configuration
	 */
	private final ByteInterpreterConfiguration configuration;

	/**
	 * Creates a new interpreter using the {@link ByteInterpreter#DEFAULT} configuration
	 */
	public ByteInterpreter() {
		this(ByteInterpreterConfiguration.DEFAULT);
	}

	/**
	 * Creates a new byte interpreter with the given configuration
	 *
	 * @param configuration The configuration to use
	 */
	public ByteInterpreter(ByteInterpreterConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Tries to interpret the data on the denoted channel as byte stream.
	 *
	 * @param packet The data packet to interpret
	 * @param channelNr The channel to grab the data from
	 * @return the intepretation result
	 * @throws CoreException If something goes wrong during interpretation (mostly a config poblem)
	 */
	public ByteInterpreterResult interpret(IDataPacket packet, int channelNr) throws CoreException {
		if (packet.getFrames().size() < 2) return new ByteInterpreterResult(new byte[0], 0, new long[0], new boolean[0]);

		List<Byte> result = new LinkedList<Byte>();
		List<Long> bitStarts = new LinkedList<Long>();
		List<Boolean> bits = new LinkedList<Boolean>();
		IBitInterpretationStrategy strategy = chooseInterpretationStrategy(packet, channelNr);
		if (strategy == null) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Sorry, I don't know how to interpret that data");
			throw new CoreException(status);
		}

		byte currentByte = 0;
		int bitCounter = 0;
		while (strategy.hasNext()) {
			byte currentBit = (byte) (strategy.next() & 0x01);
			bits.add(currentBit > 0);

			bitStarts.add(strategy.getCurrentTime());
			if (configuration.isLittleEndian()) {
				currentByte |= (currentBit << (7 - bitCounter));
			} else {
				currentByte |= (currentBit << bitCounter);
			}
			bitCounter++;

			if (bitCounter == 8) {
				result.add(currentByte);
				currentByte = 0;
				bitCounter = 0;
			}
		}

		byte[] resultArray = new byte[result.size()];
		int i = 0;
		for (Byte b : result) resultArray[i++] = b;
		long[] timingsArray = new long[bitStarts.size()];
		i = 0;
		for (Long l : bitStarts) timingsArray[i++] = l;
		boolean[] bitsArray = new boolean[bits.size()];
		i = 0;
		for (Boolean b : bits) bitsArray[i++] = b;
		return new ByteInterpreterResult(resultArray, strategy.getBitLength(), timingsArray, bitsArray);
	}

	/**
	 * Chooses an interpretation strategy based on the current configuration.
	 *
	 * @return an appropriate strategy or null if none is appropriate
	 */
	private IBitInterpretationStrategy chooseInterpretationStrategy(IDataPacket packet, int channelNr) {
		IBitInterpretationStrategy result;

		if (configuration.getMode() == ByteInterpreterMode.ClockSignal) {
			result = new ClockLineBitInterpreter(packet, channelNr, configuration.getClockChannelNr(), configuration.isOnRisingEdge());
		} else if (configuration.getMode() == ByteInterpreterMode.FixedLength) {
			result = new FixedBitLengthInterpreter(packet, channelNr, configuration.getBitWidth());
		} else {
			result = new AutoBitLengthInterpreter(packet, channelNr);
		}

		return result;
	}

}
