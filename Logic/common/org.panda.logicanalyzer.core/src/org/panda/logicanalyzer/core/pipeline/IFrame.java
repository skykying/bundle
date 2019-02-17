package org.panda.logicanalyzer.core.pipeline;

/**
 * A frame is the atomic unit of measurement processed by the pipeline. Each frame
 * represents an ideally singular point in time where a measurement has been performed.
 */
public interface IFrame {

	/**
	 * Returns the value of a channel at this point in time. If the channel number
	 * is unknown false is returned.
	 *
	 * @param channelNr The number of the channel
	 * @return true if this channel is high, false if it's low or unknown
	 */
	public boolean getValue(int channelNr);

	/**
	 * @return the amount of channels contained in this frame. Any value greater
	 *         than equals zero and less than the returned one can be expected
	 *         to be known my {@link #getValue(int)}
	 */
	public int getAmountOfChannels();

	/**
	 * @return The point in time this frame has been acquired given nanoseconds
	 *         since acquisition start
	 */
	public long getTime();

	/**
	 * Returns true if the given mask matches the content of this frame along
	 * its channels. Let <code>F = [c1, c2, ..., cN]^T</code> be the data of
	 * this frame and <code>M = [b1, b2, ..., bN]</code> the mask vector, then
	 * this method returns <code>F &amp; M</code> where <code>&amp;</code> denotes
	 * the logic and operation between each component of the vector. If
	 * <code>N != M</code> the missing components are assumed to be zero.
	 *
	 * @param mask the mask to apply
	 * @return the masked frame
	 * @throws UnsupportedOperationException If this frame has more than 64 channels
	 */
	public long mask(long mask) throws UnsupportedOperationException;

}
