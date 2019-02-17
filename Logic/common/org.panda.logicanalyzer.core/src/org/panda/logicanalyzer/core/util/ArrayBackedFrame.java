package org.panda.logicanalyzer.core.util;

import java.util.Arrays;

import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * An implementation of the {@link IFrame} interface using an array as supporting
 * data structure.
 */
public class ArrayBackedFrame implements IFrame {

	/**
	 * The time of this frame
	 */
	private final long time;

	/**
	 * The data represented by this frame
	 */
	private final boolean[] data;

	public ArrayBackedFrame(long time, boolean[] data) {
		this.time = time;
		this.data = data;

	}


	public long getTime() {
		return time;
	}


	public boolean getValue(int channelNr) {
		return channelNr >= 0 && channelNr < getAmountOfChannels() ?
		       data[channelNr] : false;
	}


	public int getAmountOfChannels() {
		return data.length;
	}

	public String toString() {
		return Arrays.toString(data);
	}


	public long mask(long mask) {
		if (data.length > 64) throw new UnsupportedOperationException();

		long frame = 0;
		for (int i = 0; i < data.length && i < 63; i++)
			frame |= (data[i] ? 1 : 0) << i;

		return (frame & mask);
	}

}
