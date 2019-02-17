package org.panda.logicanalyzer.core.util.byteInterpreter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * This interpreter uses a second channel as clock line and reads data rising or falling edge.
 */
public class ClockLineBitInterpreter implements IBitInterpretationStrategy {

	/**
	 * The channel number of the data channel
	 */
	private final int dataChanNr;

	/**
	 * The channel number of the clock channel
	 */
	private final int clockChanNr;

	/**
	 * The iterator for the data line
	 */
	private final LinkedBlockingQueue<IFrame> dataLine;

	/**
	 * The current data frame
	 */
	private IFrame currentDataFrame;

	/**
	 * The iterator for the clock line
	 */
	private final Iterator<IFrame> clockLine;

	/**
	 * If true we'll read data on rising edge, if false on falling edge
	 */
	private final boolean risingEdge;

	/**
	 * The current time in the data packet
	 */
	private long currentTime;


	/**
	 * Creates a new clock line bit interpreter.
	 *
	 * @param packet The data to interpret
	 * @param dataChanNr The channel number of the data channel
	 * @param clockChanNr The channel number of the clock channel
	 * @param risingEdge If true we'll read data on rising edge, if false on falling edge
	 */
	public ClockLineBitInterpreter(IDataPacket packet, int dataChanNr, int clockChanNr, boolean risingEdge) {
		this.dataChanNr = dataChanNr;
		this.clockChanNr = clockChanNr;
		this.risingEdge = risingEdge;

		dataLine = new LinkedBlockingQueue<IFrame>(packet.getFrames());
		clockLine = packet.getFrames().iterator();
	}


	public boolean hasNext() {
		return clockLine.hasNext();
	}


	public Byte next() {
		if (!hasNext()) throw new NoSuchElementException();

		IFrame clkFrame = clockLine.next();
		currentTime = clkFrame.getTime();
		while (clkFrame.getValue(clockChanNr) != risingEdge && clockLine.hasNext())
			clkFrame = clockLine.next();

		return (byte) (synchronizeDataFrame(clkFrame.getTime()).getValue(dataChanNr) ? 1 : 0);
	}


	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Synchronizes the data line to match the clock line again
	 *
	 * @param t The time to synchronize the data line to
	 * @return The current data frame
	 */
	private IFrame synchronizeDataFrame(long t) {
		if (dataLine.isEmpty()) return currentDataFrame;

		boolean notAheadOfTime = dataLine.peek().getTime() > t;
		while (notAheadOfTime) {
			currentDataFrame = dataLine.poll();
			notAheadOfTime = dataLine.peek().getTime() > t;
		}

		return currentDataFrame;
	}


	public long getCurrentTime() {
		return currentTime;
	}


	public long getBitLength() {
		return -1;
	}

}
