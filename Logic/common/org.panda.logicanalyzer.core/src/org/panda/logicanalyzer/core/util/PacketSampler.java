package org.panda.logicanalyzer.core.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * The packet sampler ensures that the time difference between all frames in a packet is equal.
 * One can either specify the frequency or otherwise the highest usefull frequency is used.
 */
public class PacketSampler {

	/**
	 * Samples the given data packet from the start on using,
	 * the lowest low/high period as frequency.
	 *
	 * @param packet The packet to sample
	 * @return the resampled packet
	 */
	public static IDataPacket sample(IDataPacket packet) {
		return sample(packet, findShortestPeriod(packet));
	}

	/**
	 * Finds the shortest low or high period on all channels. For this method
	 * to work, all frames are expected to have the same amount of channels. If
	 * that's not the case, an {@link IllegalArgumentException} is thrown.
	 *
	 * @param packet the pacet to search
	 * @return the shortest low or high period
	 * @throws IllegalArgumentException If the amount of channels differs allong the frames
	 */
	public static long findShortestPeriod(IDataPacket packet) throws IllegalArgumentException {
		long result = Long.MAX_VALUE;

		long prevTime = -1;
		boolean[] prevValue = null;
		for (IFrame frame : packet.getFrames()) {
			if (prevValue == null) prevValue = new boolean[frame.getAmountOfChannels()];
			else if (prevValue.length != frame.getAmountOfChannels()) throw new IllegalArgumentException();

			for (int channelNr = 0; channelNr < prevValue.length; channelNr++) {
				if (prevTime < 0) {
					prevValue[channelNr] = frame.getValue(channelNr);
					prevTime = frame.getTime();
				}

				if (prevValue[channelNr] != frame.getValue(channelNr)) {
					long dt = frame.getTime() - prevTime;
					if (prevTime >= 0 && dt < result) {
						result = dt;
					}

					prevTime = frame.getTime();
					prevValue[channelNr] = frame.getValue(channelNr);
				}
			}
		}

		return result == Long.MAX_VALUE ? 0 : result;
	}

	/**
	 * Samples the given data packet from start on with the given frequency.
	 *
	 * @param packet The packet containing the sampled data
	 * @param period The period of the sample rate
	 * @return the sampled packet
	 */
	public static IDataPacket sample(IDataPacket packet, long period) {
		List<IFrame> frames = new LinkedList<IFrame>();

		if (packet.getFrames().size() >= 2 && period > 0) {
			Iterator<IFrame> iter = packet.getFrames().iterator();
			IFrame currentFrame = iter.next();
			IFrame nextFrame = iter.next();

			for (long t = packet.getStartTime(); t <= packet.getEndTime() && iter.hasNext(); t += period) {
				while (nextFrame.getTime() < t && iter.hasNext()) {
					currentFrame = nextFrame;
					nextFrame = iter.next();
				}

				boolean[] data = new boolean[currentFrame.getAmountOfChannels()];
				for (int channel = 0; channel < data.length; channel++)
					data[channel] = currentFrame.getValue(channel);
				frames.add(new ArrayBackedFrame(t, data));
			}
		}

		return new DataPacket(frames);
	}

}
