package org.panda.logicanalyzer.core.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * Simple default implementation of the data packet
 */
public class DataPacket implements IDataPacket {

	/**
	 * The frames of this packet
	 */
	private final List<IFrame> frames;

	/**
	 * The start time of this packet
	 */
	private final long startTime;

	/**
	 * The end time of this packet
	 */
	private final long endTime;

	/**
	 * The amount of channels this packet contains
	 */
	private final int amountOfChannels;

	/**
	 * The point in time when the trigger fired
	 */
	private final long triggerPosition;

	/**
	 * The channel enabledment. If null, all channels are considered enabled.
	 * If {@link #channelEnablement}<code>.length &lt; </code>{@link #getAmountOfChannels()}
	 * all channels {@link #channelEnablement}<code>.length &lt; </code> are considered
	 * enabled.
	 */
	private final boolean[] channelEnablement;


	public DataPacket(List<IFrame> frames) {
		this(frames, -1);
	}

	public DataPacket(List<IFrame> frames, long triggerPosition) {
		this(frames, triggerPosition, null);
	}

	public DataPacket(List<IFrame> frames, long triggerPosition, boolean[] channelEnablement) {
		this.frames = frames;
		this.triggerPosition = triggerPosition;
		this.channelEnablement = channelEnablement;

		int amountOfChannels = 0;
		long startTime = Long.MAX_VALUE;
		long endTime = Long.MIN_VALUE;
		for (IFrame frame : frames) {
			if (frame.getTime() < startTime) startTime = frame.getTime();
			else if (frame.getTime() > endTime) endTime = frame.getTime();

			if (amountOfChannels < frame.getAmountOfChannels())
				amountOfChannels = frame.getAmountOfChannels();
		}
		this.startTime = startTime;
		this.endTime = endTime == Long.MIN_VALUE ? startTime : endTime;
		this.amountOfChannels = amountOfChannels;
	}

	public long getEndTime() {
		return endTime;
	}


	public Collection<IFrame> getFrames() {
		return frames;
	}


	public long getStartTime() {
		return startTime;
	}


	public int getAmountOfChannels() {
		return amountOfChannels;
	}


	public IDataPacket slice(long from, long to) {
		List<IFrame> result;

		if (from > to || from < getStartTime() || to > getEndTime()) {
			result = null;
		} else {
			result = new LinkedList<IFrame>();

			IFrame prev = null;
			for (IFrame frame : getFrames()) {
				if (frame.getTime() > to && (prev == null || prev.getTime() > to)) break;
				if (prev != null && prev.getTime() >= from) result.add(prev);

				prev = frame;
			}
		}

		return result == null ? null : new DataPacket(result);
	}


	public long getTriggerPosition() {
		return triggerPosition;
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
