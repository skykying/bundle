package org.panda.logicanalyzer.core.pipeline;

import java.util.Collection;
import java.util.Collections;

/**
 * Each time the DSo delivers data to the pipeline, this is done in form of such packets.
 * Basically packets cluster frames to a unit.
 */
public interface IDataPacket {

	/**
	 * An empty data packet
	 */
	public static final IDataPacket EMPTY = new IDataPacket() {
		public long getStartTime() { return 0; }


		public Collection<IFrame> getFrames() { return Collections.emptyList(); }


		public long getEndTime() { return 0; }


		public int getAmountOfChannels() { return 0; }


		public boolean isChannelEnabled(int channelNr) { return false; }


		public IDataPacket slice(long from, long to) { return null; }


		public long getTriggerPosition() { return -1; }
	};

	/**
	 * @return the start time (time of the first frame in this packet) of this packet since DSo run start in nanoseconds.
	 */
	public long getStartTime();

	/**
	 * @return the end time (time of the last frame in this packet) of this packet since DSo run start in nanoseconds
	 */
	public long getEndTime();

	/**
	 * @return the frames of this packet
	 */
	public Collection<IFrame> getFrames();

	/**
	 * @return the maximum amount of channels in this packet: <code>max(f.amountOfChannels, foreach frames)</code>
	 */
	public int getAmountOfChannels();

	/**
	 * It's possible for an LA to always deliver <code>n</code> channel wide
	 * data packets, of which only some channels are enabled. To reflect that
	 * posibility, use this method to check if a channel is enabled.
	 *
	 * @param channelNr the number of the channel
	 * @return true if the channel is enabled, false if not or the channelNr is unknown
	 */
	public boolean isChannelEnabled(int channelNr);

	/**
	 * Extracts a slice of this data packet (with at least including the bounds - if satisfiable). If the bounds
	 * are invalid (<code>from > to || from < {@link #getStartTime()} || to > {@link #getEndTime()}</code>), null is returned.
	 *
	 * @param from the lower bound
	 * @param to the upper bound
	 * @return the extracted slice or null
	 */
	public IDataPacket slice(long from, long to);

	/**
	 * Returns the position the trigger fired. If this information is not available, -1 is returned.
	 *
	 * @return the trigger fire position or -1
	 */
	public long getTriggerPosition();

}
