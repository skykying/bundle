package org.panda.logicanalyzer.ui.editor;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * A channel overlay adds some additional information to the channel
 * by overlaying it.
 */
public interface IChannelOverlay {

	/**
	 * Computes the height of this overlay. If one wants to draw something above
	 * or below the channel, this method must return a value greater than the channelHeight.
	 *
	 * @param channelHeight The height of the channel display
	 * @return the total height of this overlay
	 */
	public int getHeight(int channelHeight);

	/**
	 * Returns the length of this overlay in nanoseconds. If the value returned by this
	 * method is less than equal to zero, this overlay will never be called.
	 *
	 * @return the length of this overlay
	 */
	public long getLength();

	/**
	 * Returns the horizontal offset of this overlay in nanoseconds.
	 *
	 * @return the horizontal offset of this overlay
	 */
	public long getOffset();

	/**
	 * Draws the overlay on the GC. This method is most likely to be called from
	 * the UI thread, so be sure to spend as less time in this method as possible.
	 *
	 * @param gc The GC to draw on
	 * @param bounds The bounds of the drawing area
	 * @param channelHeight The height of the raw channel drawing
	 */
	public void draw(GC gc, Rectangle bounds, int channelHeight);

	/**
	 * Disposes this overlay. Once this method has been called, it's ensured that
	 * {@link #draw(GC, int, int)} is never called again.
	 */
	public void dispose();

	/**
	 * Determines whether this overlay is disposed or not. If this method returns
	 * true, it is guaranteed that {@link #draw(GC, int, int)} is never called again.
	 *
	 * @return true if this overlay is disposed
	 */
	public boolean isDisposed();

}
