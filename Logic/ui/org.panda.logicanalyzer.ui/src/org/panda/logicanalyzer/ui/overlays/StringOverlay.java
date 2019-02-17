package org.panda.logicanalyzer.ui.overlays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.panda.logicanalyzer.ui.editor.AbstractChannelOverlay;

/**
 * This overlay simply draws a string below the selection.
 */
public class StringOverlay extends AbstractChannelOverlay {

	/**
	 * The text to draw
	 */
	private final String text;

	/**
	 * The start of the overlay in nanoseconds
	 */
	private final long from;

	/**
	 * The end of the overlay in nanoseconds
	 */
	private final long to;

	public StringOverlay(String text, long from, long to) {
		this.text = text;
		this.from = from;
		this.to = to;
	}

//	@Override
	public void draw(GC gc, Rectangle bounds, int channelHeight) {
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
		gc.drawText(text, bounds.x, channelHeight, true);
	}

//	@Override
	public int getHeight(int channelHeight) {
		return channelHeight + 20;
	}

//	@Override
	public long getLength() {
		return to - from;
	}

//	@Override
	public long getOffset() {
		return from;
	}

}
