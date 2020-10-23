package org.panda.logicanalyzer.ui.internal.editor;

import java.awt.event.MouseWheelEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;
import org.panda.logicanalyzer.core.util.ArrayBackedFrame;
import org.panda.logicanalyzer.core.util.DataPacket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;

/**
 * A channel visualizer draws the data supplied to it in a {@link IDataPacket}
 */
public class ChannelVisualizer extends Canvas {


	//The drawing height if the channel is hidden in px
	private static final int HIDDEN_HEIGHT = 25;
	//The default drawing height of channel in px
	private static final int DEFAULT_HEIGHT = 50;
	//This class represents a selection on this channel.
	private class Selection {

		//The start of the selection in nanoseconds
		private final long from;
		//The end of the selection in nanoseconds
		private final long to;

		public Selection(long from, long to) {
			this.from = from;
			this.to = to;
		}

		/**
		 * @return the data of the current {@link ChannelVisualizer#packet} within this selection
		 */
		public IDataPacket getSelectedData() {
			List<IFrame> result = new LinkedList<IFrame>();

			for (IFrame frame : packet.getFrames()) {
				if (frame.getTime() > to) break;
				if (frame.getTime() > from)
					result.add(new ArrayBackedFrame(frame.getTime(),
					                                new boolean[] {
					                                    invert ? !frame.getValue(getChannelNr()) : frame.getValue(getChannelNr())
					                                })
					          );
			}

			return new DataPacket(result);
		}

		public long getFrom() {
			return from;
		}

		public long getTo() {
			return to;
		}

	}


	// The packet we take our data from
	private final IDataPacket packet;
	//The nr of the channel to draw
	private final int channelNr;
	//The vertical (time resolution) in nanoseconds per pixel
	private long nsPerPixel = 100;
	//If true, we'll draw the channel data inverted
	private boolean invert;
	//If true, we'll simply draw a thick line to mark this channel as hidden
	private boolean hidden;
	//The current selection (or null if there is no selection)
	private Selection selection;
	//The selection listener of this visualizer
	private final List<ISelectionListener> selectionListeners = new LinkedList<ISelectionListener>();
	//The overlay manager we use to draw overlays.
	private OverlayManager overlayManager;
	// Our background color
	private final Color bg = new Color(getDisplay(), 45, 36, 34);
	//The grid color
	private final Color grid = new Color(getDisplay(), 117, 101, 96);
	//The default foreground color
	private final Color fg = new Color(getDisplay(), 57, 164, 255);

	//The start of an ongoing selection or -1 if there is no selection ongoing at the momement
	private int selectionStart = -1;

	public ChannelVisualizer(final Composite parent, IDataPacket packet, int channelNr) {
		super(parent, SWT.NONE);
		this.packet = packet;
		this.channelNr = channelNr;

		addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				updateDiagram(e.gc, new Rectangle(e.x, e.x, e.width, getBounds().height));

				if (isHidden()) return;

				if (selection != null) {
					// Warning: we do not check for overflows, this might cause some nasty bugs
					int selectionWidth = (int) ((selection.getTo() - selection.getFrom()) / nsPerPixel);
					e.gc.setAdvanced(true);
					e.gc.setAlpha(50);
					e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

					//draw selection data
					IDataPacket packet = selection.getSelectedData();
					e.gc.fillRectangle((int) (selection.getFrom() / nsPerPixel), 0, selectionWidth, DEFAULT_HEIGHT);
					if (packet != null) {
						e.gc.setAlpha(100);
						e.gc.fillRectangle((int) (packet.getStartTime() / nsPerPixel), 0, (int) ((packet.getEndTime() - packet.getStartTime()) / nsPerPixel), DEFAULT_HEIGHT);
					}

					e.gc.setAlpha(255);
					e.gc.setAdvanced(false);
				}

				if (overlayManager == null) {
					e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));
					e.gc.fillRectangle(new Rectangle(0, 0, 10, 10));
				} else {
					int overlayHeight = overlayManager.getHeight(DEFAULT_HEIGHT);
					boolean hasOverlays = overlayManager.hasOverlays();
					if ((e.height < overlayHeight && hasOverlays) || (e.height > overlayHeight && !hasOverlays)) {
						refresh();
					}
					overlayManager.draw(e.gc, DEFAULT_HEIGHT);
				}
			}

		});
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				boolean resetSelection = e.x == selectionStart || selectionStart == -1;

				selectionStart = -1;
				if (resetSelection) {
					setSelection(-1, -1, true);
				}
				System.out.println("mouseUp");
			}

			@Override
			public void mouseDown(MouseEvent e) {
				selectionStart = e.x;
				System.out.println("mouseDown");
			}

		});
		addMouseMoveListener(new MouseMoveListener() {

			public void mouseMove(MouseEvent e) {
				System.out.println("MouseMoveListener");
				if (selectionStart >= 0) {
					long from = selectionStart * nsPerPixel;
					long to = e.x * nsPerPixel;
					setSelection(from, to, true);
				}
			}

		});
		
		addMouseWheelListener(new MouseWheelListener() {
			
			//"Count" here means number of scroll lines (3 or -3 by default in Windows)
			public void mouseScrolled(MouseEvent e) {
				System.out.println("MouseWheelListener "+ e.x +" "+ e.y +" " +e.count);
				
			}
			
			
		});

		overlayManager = new OverlayManager(channelNr, this);
	}

	/**
	 * Adds a selection listener to this class. If that exact same listener has
	 * already been registered nothing changes.
	 *
	 * @param listener The listener to add
	 */
	public void addSelectionListener(ISelectionListener listener) {
		if (listener == null || selectionListeners.contains(listener)) return;

		selectionListeners.add(listener);
	}

	/**
	 * Sets the time resolution for this view in nanoseconds per pixel. Be aware that
	 * callers must be running in the UI thread since this method modifies the UI.
	 * If the new value passed is less than or equal to zero, nothing changes.
	 *
	 * @param nsPerPixel The new value to use
	 */
	public void setTimeResolution(long nsPerPixel) {
		if (nsPerPixel <= 0 || nsPerPixel == this.nsPerPixel) return;
		this.nsPerPixel = nsPerPixel;

		getParent().pack(true);
	}

	/**
	 * Updates the {@link #diagramImage} and {@link #hiddenImage} thus regenerating the content of this widget. This
	 * method should rarely be needed to be called from outside this class, since e.g. {@link #setTimeResolution(long)}
	 * will call this method.
	 */
	private void updateDiagram(GC gc, Rectangle bounds) {
		if (isHidden()) {
			gc.setBackground(fg);
			gc.fillRectangle(new Rectangle(bounds.x, 0, bounds.width, bounds.height));
			return;
		}

		int height = DEFAULT_HEIGHT;
		int width = bounds.width;
		long offset = bounds.x * nsPerPixel;
		long endOfRegion = (bounds.x + bounds.width) * nsPerPixel;

		final int loY = (int) (height * 0.8);
		final int hiY = (int) (height * 0.2);

		gc.setBackground(bg);
		gc.fillRectangle(bounds.x, 0, width, bounds.height);

		gc.setForeground(grid);
		for (int i = bounds.x - (bounds.x % 50); i < bounds.x + bounds.width; i += 50) {
			gc.drawLine(i, 0, i, bounds.height);
		}

		gc.setForeground(fg);
		Iterator<IFrame> iter = packet.getFrames().iterator();
		IFrame cur = iter.next();
		IFrame prev = null;
		int prevPixel = -1, prevLinePixel = -1;
		while (iter.hasNext() && (prev == null || prev.getTime() < endOfRegion)) {
			if (cur.getTime() >= offset && prev != null) {
				int cVal = (invert ? !cur.getValue(getChannelNr()) : cur.getValue(getChannelNr())) ? hiY : loY;
				int pVal = (invert ? !prev.getValue(getChannelNr()) : prev.getValue(getChannelNr())) ? hiY : loY;
				int currentPixel = (int) (cur.getTime() / nsPerPixel);

				if (currentPixel != prevPixel) {
					gc.drawLine((int) Math.max(bounds.x, prev.getTime() / nsPerPixel), pVal, currentPixel, pVal);
				}
				prevPixel = currentPixel;

				if (currentPixel != prevLinePixel) {
					gc.drawLine(currentPixel, cVal, currentPixel, pVal);
					prevLinePixel = currentPixel;
				}
			}


			prev = cur;
			cur = iter.next();
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		// Possible overflow here, not checking
		final int width = (int) ((packet.getEndTime() - packet.getStartTime()) / nsPerPixel);
		int height = isHidden() ? HIDDEN_HEIGHT : DEFAULT_HEIGHT;

		if (overlayManager != null) {
			height = overlayManager.getHeight(height);
		}

		return new Point(width, height);
	}

	/**
	 * Changes the inverted setting. If this is set to true, the channel will be drawn inverted.
	 *
	 * @param invert true if the chan is to be inverted
	 */
	public void setInverted(boolean invert) {
		if (this.invert == invert) return;

		this.invert = invert;
		redraw();
	}

	/**
	 * @return true if this channel is shown inverted
	 */
	public boolean isInverted() {
		return invert;
	}

	/**
	 * Sets the current selection. If from or to is less than equal to zero, the
	 * selection is removed.
	 *
	 * @param from
	 *            The start of the selection in nanoseconds
	 * @param to
	 *            The end of the selection in nanoseconds
	 * @param notifyListener
	 *            If true the {@link #selectionListeners} are notified
	 */
	public void setSelection(long from, long to, boolean notifyListener) {
		if (selection != null && (selection.getFrom() == from && selection.getTo() == to))
			return;

		if (to < from) {
			long t = to;
			to = from;
			from = t;
		}

		selection = from <= 0 || to <= 0 || from - to == 0 ? null : new Selection(from, to);
		if (notifyListener) {
			for (ISelectionListener listener : selectionListeners)
				listener.selectionChanged(channelNr, from, to);
		}

		redraw();
		update();
	}

	/**
	 * Computes the currently selected data.
	 *
	 * @return the currently selected data (via {@link #setSelection(long, long, boolean)} or null if there currently is no selection
	 */
	public IDataPacket getSelectedData() {
		return selection == null ? null : selection.getSelectedData();
	}

	/**
	 * @return the channel we're drawing
	 */
	public int getChannelNr() {
		return channelNr;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		if (isHidden() == hidden) return;

		this.hidden = hidden;
	}

	/**
	 * Returns the data packet this visualizer works on.
	 *
	 * @return the data packet
	 */
	public IDataPacket getPacket() {
		return packet;
	}

	/**
	 * @return the current time scale in nanoseconds per pixel
	 */
	public long getTimeScale() {
		return nsPerPixel;
	}

	@Override
	public void dispose() {
		bg.dispose();
		fg.dispose();
		grid.dispose();

		overlayManager.dispose();
	}

	/**
	 * @return the overlay manager used by this class. Possibly null
	 */
	public OverlayManager getOverlayManager() {
		return overlayManager;
	}

	/**
	 * Does a full refresh of this visualizer. This is a pretty time and resource consuming task
	 * so be sure you only call when inevitable.
	 */
	public void refresh() {
		getParent().pack(true);
	}

}
