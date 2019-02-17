package org.panda.logicanalyzer.core.util;

import java.util.ArrayList;
import java.util.Collection;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * This data packet is uses int values as internal data structure. It's mostly intended
 * to be used with capturing devices, such as a SUMP device or SCD.
 */
public class BinaryDataPacket implements IDataPacket {

	/**
	 * This frame uses the {@link BinaryDataPacket#frameData} array, the
	 * {@link BinaryDataPacket#frameDistance} and an index to dermine its values.
	 */
	private class Frame implements IFrame {

		/**
		 * The index of this frame
		 */
		private final int index;

		public Frame(int index) {
			this.index = index;
		}


		public int getAmountOfChannels() {
			return amountOfChannels;
		}


		public long getTime() {
			return startTime + (index * frameDistance);
		}


		public boolean getValue(int channelNr) {
			return mask(1 << channelNr) == (1 << channelNr);
		}


		public long mask(long mask) throws UnsupportedOperationException {
			return frameData[index] & mask;
		}

	}

	/**
	 * The captured data (frames, payload) of this packet
	 */
	private final int[] frameData;

	/**
	 * The trigger position in samples
	 */
	private final int triggerPosition;

	/**
	 * The amount of channels we captured on
	 */
	private final int amountOfChannels;

	/**
	 * The time between frames in nanoseconds
	 */
	private final long frameDistance;

	/**
	 * The start time of this frame in nanoseconds
	 */
	private final long startTime;

	/**
	 * The decorated frames used for {@link #getFrames()}
	 */
	private final ArrayList<IFrame> frames;

	/**
	 * The channel enabledment. If null, all channels are considered enabled.
	 * If {@link #channelEnablement}<code>.length &lt; </code>{@link #getAmountOfChannels()}
	 * all channels {@link #channelEnablement}<code>.length &lt; </code> are considered
	 * enabled.
	 */
	private final boolean[] channelEnablement;

	/**
	 * @param frames The captured data (frames, payload) of this packet
	 * @param triggerPosition The trigger position in samples (or -1 if no trigger available)
	 * @param frameDistance The time between frames in nanoseconds
	 * @param amountOfChannels The amount of channels we captured on
	 * @param channelEnablement the channel enablement
	 * @param startTime The start time of this frame in nanoseconds
	 */
	private BinaryDataPacket(int[] frames, int triggerPosition, long frameDistance, int amountOfChannels, boolean[] channelEnablement, long startTime) {
		this.frameData = frames;
		this.triggerPosition = triggerPosition;
		this.frameDistance = frameDistance;
		this.amountOfChannels = amountOfChannels;
		this.channelEnablement = channelEnablement;
		this.startTime = startTime;

		this.frames = new ArrayList<IFrame>(frameData.length);
		for (int i = 0; i < frames.length; i++) {
			this.frames.add(new Frame(i));
		}
	}

	/**
	 * @param frames The captured data (frames, payload) of this packet
	 * @param triggerPosition The trigger position in samples (or -1 if no trigger available)
	 * @param frameDistance The time between frames in nanoseconds
	 * @param amountOfChannels The amount of channels we captured on
	 * @param channelEnablement The channel enablement
	 */
	public BinaryDataPacket(int[] capturedData, int triggerPosition, long frameDistance, int amountOfChannels, boolean[] channelEnablement) {
		this(capturedData, triggerPosition, frameDistance, amountOfChannels, channelEnablement, 0L);
	}

	/**
	 * @param frames The captured data (frames, payload) of this packet
	 * @param triggerPosition The trigger position in samples (or -1 if no trigger available)
	 * @param frameDistance The time between frames in nanoseconds
	 * @param amountOfChannels The amount of channels we captured on
	 */
	public BinaryDataPacket(int[] capturedData, int triggerPosition, long frameDistance, int amountOfChannels) {
		this(capturedData, triggerPosition, frameDistance, amountOfChannels, null);
	}


	public int getAmountOfChannels() {
		return amountOfChannels;
	}


	public long getEndTime() {
		return startTime + (frameData.length * frameDistance);
	}


	public Collection<IFrame> getFrames() {
		return frames;
	}


	public long getStartTime() {
		return startTime;
	}


	public IDataPacket slice(long from, long to) {
		int start = (int)(from / frameDistance);
		int end = (int)(to / frameDistance);
		if (start < 0 || start > frameData.length || end < 0 || end > frameData.length || to < from) return null;

		int[] newData = new int[end - start];
		System.arraycopy(frameData, start, newData, 0, newData.length);
		return new BinaryDataPacket(newData, triggerPosition, frameDistance, amountOfChannels, channelEnablement, from);
	}


	public long getTriggerPosition() {
		long result = triggerPosition * frameDistance;
		return result > getEndTime() || result < getStartTime() ? -1 : result;
	}



	public boolean isChannelEnabled(int channelNr) {
		boolean channelIsKnown = channelNr >= 0 && channelNr < amountOfChannels;

		return channelIsKnown && (
		           channelEnablement == null
		           || channelNr > channelEnablement.length
		           || channelEnablement[channelNr]
		       );
	}

}
