package org.panda.logicanalyzer.ui.internal.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;
import org.panda.logicanalyzer.core.util.TimeConverter;
import org.panda.logicanalyzer.core.util.TimeConverter.TimeUnit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.panda.logicanalyzer.ui.editor.AbstractChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IOverlayAcceptor;
import org.panda.logicanalyzer.ui.editor.ISelectionBasedTool;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;

/**
 * This factory creates overlays which meassure the period (and frequency) of
 * the first complete high or low in a selection.
 */
public class PeriodMeassurementTool implements ISelectionBasedTool, ISelectionListener {

	/**
	 * The overlay produced by the enclosing class.
	 */
	private static class Overlay extends AbstractChannelOverlay {

		/**
		 * The start of the low/high
		 */
		private final long from;

		/**
		 * The end of the low/high
		 */
		private final long to;

		/**
		 * Constructs a new overlay
		 *
		 * @param from the start of the low/high
		 * @param to the end of the low/high
		 */
		public Overlay(long from, long to) {
			this.from = from;
			this.to = to;
		}

		//@Override
		public void draw(GC gc, Rectangle bounds, int channelHeight) {
			gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
			gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));

			String periad = TimeConverter.toString(to - from);
			double frequency = 1.0 / TimeUnit.Seconds.convert(to - from);
			double frequencyScale = Math.log10(frequency);
			String frequencyString;
			if (frequencyScale <= 3) {
				frequencyString = String.format("%01.3fHz", frequency);
			} else if (frequencyScale <= 6) {
				frequencyString = String.format("%01.3fKHz", frequency / 1000);
			} else if (frequencyScale <= 9) {
				frequencyString = String.format("%01.3fMHz", frequency / 1000000);
			} else {
				frequencyString = String.format("%1.3fGHz", frequency / 1000000000);
			}


			/*
			gc.drawLine(bounds.x, 0, bounds.x, channelHeight);
			gc.drawLine(bounds.x + bounds.width, 0, bounds.x + bounds.width, channelHeight);
			*/

			int middle = channelHeight / 2;
			int start = bounds.x;
			int end = bounds.x + bounds.width;
			gc.fillPolygon(new int[] {
			                   start, middle,
			                   start + 5, middle + 5,
			                   start + 5, middle - 5
			               });
			gc.fillPolygon(new int[] {
			                   end, middle,
			                   end - 5, middle + 5,
			                   end - 5, middle - 5
			               });
			gc.drawLine(start, middle, end, middle);
			gc.drawText(periad  + ", " + frequencyString, start + 2, channelHeight, true);
		}

		//@Override
		public int getHeight(int channelHeight) {
			return channelHeight + 20;
		}

		//@Override
		public long getLength() {
			return to - from;
		}

		//@Override
		public long getOffset() {
			return from;
		}

	}

	/**
	 * The current overlay or null if there is no such overlay
	 */
	private final Map<Integer, IChannelOverlay> currentOverlays = new HashMap<Integer, IChannelOverlay>();

	/**
	 * The packet we're working on. Available after a call to {@link #initialize(IOverlayAcceptor, IDataPacket)}
	 */
	private IDataPacket packet;

	/**
	 * The acceptor we're working with. Available after a call to {@link #initialize(IOverlayAcceptor, IDataPacket)}
	 */
	private IOverlayAcceptor acceptor;


	//@Override
	public ISelectionListener getSelectionListener() {
		return this;
	}

	//@Override
	public void selectionChanged(int channelNr, long from, long to) {
		IChannelOverlay currentOverlay = currentOverlays.get(channelNr);
		if (currentOverlay != null && !currentOverlay.isDisposed()) {
			currentOverlay.dispose();
			currentOverlays.remove(channelNr);
		}
		if (from < 0 || to < 0) return;

		IDataPacket slice = packet.slice(from, to);

		long periodStart = -1;
		long periodEnd = -1;
		/*
		 * States are:
		 *   0     no signal change yet
		 *   1     inside period (periodStart has useful value)
		 *   2     period ended (periodEnd has useful value)
		 */
		int state = 0;

		Iterator<IFrame> iter = slice.getFrames().iterator();
		IFrame currentFrame = iter.next();
		boolean currentValue = currentFrame.getValue(channelNr);
		while (state != 2 && iter.hasNext()) {
			currentFrame = iter.next();
			boolean currentFrameValue = currentFrame.getValue(channelNr);

			if (currentValue != currentFrameValue) {
				if (state == 0) {
					periodStart = currentFrame.getTime();
					currentValue = currentFrameValue;
					state = 1;
				} else if (state == 1) {
					periodEnd = currentFrame.getTime();
					state = 2;
				}
			}
		}

		if (state == 2) {
			currentOverlay = new Overlay(periodStart, periodEnd);
			currentOverlays.put(channelNr, currentOverlay);
			acceptor.accept(channelNr, currentOverlay);
		}
	}

	//@Override
	public void createContents(Composite parent) {

	}

	//@Override
	public void disable() {

	}

	//@Override
	public void enable() throws CoreException {

	}

	//@Override
	public void initialize(IOverlayAcceptor acceptor, IDataPacket packet) {
		this.acceptor = acceptor;
		this.packet = packet;
	}

}
