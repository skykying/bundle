package org.panda.logicanalyzer.ui.internal.editor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.panda.logicanalyzer.ui.editor.IChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IOverlayAcceptor;

/**
 * The overlay manager takes care of managing the overlay factories for
 * a channel.
 */
public class OverlayManager implements IOverlayAcceptor {

	/**
	 * The visualizer we're working for
	 */
	private final ChannelVisualizer visualizer;

	/**
	 * The overlays that have been accepted
	 */
	private final List<IChannelOverlay> overlays = new LinkedList<IChannelOverlay>();

	/**
	 * The channel nr we're working on
	 */
	private final int channelNr;


	public OverlayManager(int channelNr, ChannelVisualizer visualizer) {
		this.channelNr = channelNr;
		this.visualizer = visualizer;
	}



	public synchronized void accept(int channelNr, IChannelOverlay overlay) {
		if (channelNr != this.channelNr) return;

		overlays.add(overlay);
	}

	/**
	 * @return the list of overlays maintained by this descriptor
	 */
	public Collection<IChannelOverlay> getOverlays() {
		return overlays;
	}

	/**
	 * Draws all current overlays on the given GC.
	 *
	 * @param gc The graphics context to draw on
	 * @param channelHeight The height of the raw channel drawing
	 */
	public void draw(GC gc, int channelHeight) {
		for (Iterator<IChannelOverlay> iter = overlays.iterator(); iter.hasNext(); ) {
			IChannelOverlay overlay = iter.next();
			if (overlay.isDisposed()) {
				iter.remove();
			} else {
				int width = (int) (overlay.getLength() / visualizer.getTimeScale());
				int height = overlay.getHeight(channelHeight);
				int x = (int) (overlay.getOffset() / visualizer.getTimeScale());

				overlay.draw(gc, new Rectangle(x, 0, width, height), channelHeight);
			}
		}
	}

	/**
	 * @param channelHeight The height of the raw channel drawing
	 * @return the height required to draw all overlays
	 */
	public int getHeight(int channelHeight) {
		int height = channelHeight;

		for (IChannelOverlay overlay : getOverlays()) {
			if (!overlay.isDisposed() && height < overlay.getHeight(channelHeight)) {
				height = overlay.getHeight(channelHeight);
			}
		}

		return height;
	}

	/**
	 * @return true if there are any non-disposed overlays managed by this
	 *         manager.
	 */
	public boolean hasOverlays() {
		for (Iterator<IChannelOverlay> iter = overlays.iterator(); iter.hasNext(); ) {
			IChannelOverlay overlay = iter.next();
			if (overlay.isDisposed()) {
				iter.remove();
			}
		}

		return !overlays.isEmpty();
	}

	/**
	 * Disposes this overlay manager and all its overlays
	 */
	public void dispose() {
		for (Iterator<IChannelOverlay> iter = overlays.iterator(); iter.hasNext(); ) {
			IChannelOverlay overlay = iter.next();

			if (!overlay.isDisposed()) {
				overlay.dispose();
			}

			iter.remove();
		}
	}

}
