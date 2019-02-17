package org.panda.logicanalyzer.core.util.byteInterpreter;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * Determines the bit length by searching for the shortest low or high.
 */
public abstract class BaseBitInterpretationStrategy implements IBitInterpretationStrategy {

	/**
	 * The channel we're reading from
	 */
	private final int channelNr;

	/**
	 * The current frame
	 */
	private IFrame currentFrame;

	/**
	 * The next frame
	 */
	private IFrame nextFrame;

	/**
	 * The frame iterator used to sample the signal
	 */
	private final Iterator<IFrame> frames;

	/**
	 * The current sampling time
	 */
	private long currentTime;

	/**
	 * The end time of the packet
	 */
	private long endTime;


	public BaseBitInterpretationStrategy(IDataPacket packet, int channelNr) {
		this.channelNr = channelNr;
		this.frames = channelNr < 0 || channelNr >= packet.getAmountOfChannels() ?  null : packet.getFrames().iterator();
		this.currentTime = packet.getStartTime();
		this.endTime = packet.getEndTime();

		this.currentFrame = frames.next();
		this.nextFrame = frames.next();
	}

	/**
	 * Returns the time slice between the bits. The signal will be sampled with
	 * that period.
	 *
	 * @return the length of a single bit in nanoseconds
	 */
	public abstract long getBitLength();


	public boolean hasNext() {
		return currentTime < endTime && currentFrame != null && nextFrame != null && getBitLength() > 0;
	}


	public Byte next() {
		if (!hasNext()) throw new NoSuchElementException();

		while (nextFrame.getTime() < currentTime && frames.hasNext()) {
			currentFrame = nextFrame;
			nextFrame = frames.next();
		}
		currentTime += getBitLength();

		return (byte)(currentFrame.getValue(channelNr) ? 0x01 : 0x00);
	}


	public void remove() {
		throw new UnsupportedOperationException();
	}


	public long getCurrentTime() {
		return currentTime - getBitLength();
	}

}
