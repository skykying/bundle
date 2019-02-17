package org.panda.logicanalyzer.ui.internal.editor;


import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IFrame;
import org.panda.logicanalyzer.core.util.ArrayBackedFrame;
import org.panda.logicanalyzer.core.util.DataPacket;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.panda.logicanalyzer.ui.editor.IChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IOverlayAcceptor;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;

/**
 * This widget visualizes all channels of the data packet.
 */
public class DataPacketVisualizer extends ScrolledComposite {

	/**
	 * This class is basically a two-tuple used to connect a data packet
	 * to a channel number. We only need this for data selection computation.
	 */
	private static class DataPacketToChannel {

		private final int channel;
		private final IDataPacket packet;

		public DataPacketToChannel(int channel, IDataPacket packet) {
			this.channel = channel;
			this.packet = packet;
		}

		public int getChannel() {
			return channel;
		}

		public IDataPacket getPacket() {
			return packet;
		}

	}

	/**
	 * The composite we use to build our widgets upon
	 */
	private Composite composite;

	/**
	 * Our current time scaling in nanoseconds per pixel. Defaults to one microsecond per pixel.
	 */
	private long scale = 1000L;

	/**
	 * The time ruler we use to show the current time scale
	 */
	private TimeRuler ruler;

	/**
	 * If this flag is true, selection made on one channel is propagated to all the other channels
	 */
	private boolean allChannelSelection;

	/**
	 * The selection listener we attach to all {@link ChannelVisualizer} to propagate selection
	 * to all channels.
	 */
	private final ISelectionListener selectionListener = new ISelectionListener() {

		/**
		 * Guard to prevent listener cycles caused by selection propagation
		 */
		private boolean isPropagating = false;

		public void selectionChanged(int channelNr, long from, long to) {
			if (isPropagating || !allChannelSelection) return;

			isPropagating = true;
			try {
				for (Control c : composite.getChildren()) {
					if (c instanceof ChannelVisualizer && !((ChannelVisualizer) c).isHidden()) {
						((ChannelVisualizer) c).setSelection(from, to, false);
					}
				}
			} finally {
				isPropagating = false;
			}
		}

	};

	/**
	 * The current input (possibly null if {@link #setInput(IDataPacket)} has not yet been set
	 */
	private IDataPacket input;

	/**
	 * The selection listener of this visualizer.
	 */
	private List<ISelectionListener> selectionListeners = new LinkedList<ISelectionListener>();

	/**
	 * Our tool manager or null if {@link #setInput(IDataPacket)} has not yet been called
	 */
	private SelectionBasedToolManager toolManager;

	/**
	 * The current channel visualizer or null if {@link #setInput(IDataPacket)}
	 * has not yet been called.
	 */
	private ChannelVisualizer[] currentVisualizer;


	public DataPacketVisualizer(Composite parent) {
		super(parent, SWT.V_SCROLL | SWT.H_SCROLL);

		composite  = new Composite(this, SWT.None);
		GridLayout layout = new GridLayout(2, false);
		layout.verticalSpacing = 0;
		composite.setLayout(layout);

		new Label(composite, SWT.NONE);
		ruler = new TimeRuler(composite);
		ruler.setScale(scale);

		setContent(composite);
	}

	/**
	 * Sets the time scale of this visualizer in nanoseconds per pixel. Any value less than equal to zero is ignored.
	 *
	 * @param nsPerPixel the new time scale
	 */
	public void setScale(final long nsPerPixel) {
		getDisplay().asyncExec(new Runnable() {

			public void run() {
				scale = nsPerPixel;
				ruler.setScale(scale);
				for (Control c : composite.getChildren()) {
					if (!c.isDisposed() && c instanceof ChannelVisualizer)
						((ChannelVisualizer)c).setTimeResolution(nsPerPixel);
				}
			}

		});
	}

	/**
	 * Calling this method will cause this widget to autoscale. That is setting scale to
	 * <code>widgetWidthInPx / frameLengthInNs</code>. If either the packet is null,
	 * or <code>widgetWidthInPx</code> is zero, nothing is done.
	 */
	public void autoScale() {
		if (input == null || getClientArea().width == 0) return;

		int width = 0;
		for (Control c : composite.getChildren()) {
			if (c instanceof ChannelMeta) {
				width = Math.max(width, c.getBounds().width);
			}
		}

		if (width != 0) {
			width = getClientArea().width - width;
			setScale((input.getEndTime() - input.getStartTime()) / width);
		}
	}

	/**
	 * @return the current time scale
	 */
	public long getScale() {
		return scale;
	}

	/**
	 * Sets the input for this visualizer. Callers must be running in
	 * the UI thread, since this method updates the UI of this visualizer.
	 *
	 * @param packet The packet to visualize
	 * @throws CoreException In case a tool reports a problem
	 */
	public void setInput(IDataPacket packet) throws CoreException {
		this.input = packet;
		this.toolManager = SelectionBasedToolManager.getManager(this);

		for (Control c : composite.getChildren())
			if (c instanceof ChannelVisualizer || c instanceof ChannelMeta) c.dispose();

		ruler.setLength(packet.getEndTime() - packet.getStartTime());

		final ISelectionListener listener = new ISelectionListener() {

			public void selectionChanged(int channelNr, long from, long to) {
				for (ISelectionListener listener : selectionListeners)
					listener.selectionChanged(allChannelSelection ? -1 : channelNr, from, to);
			}

		};

		int amountOfChannels = packet.getAmountOfChannels();
		currentVisualizer = new ChannelVisualizer[amountOfChannels];

		for (int i = 0; i < amountOfChannels; i++) {
			ChannelMeta meta = null;
			if (packet.isChannelEnabled(i)) {
				meta = new ChannelMeta(composite, "ch" + i);
			} else {
				new Label(composite, SWT.NONE);
			}

			ChannelVisualizer visualizer = new ChannelVisualizer(composite, packet, i);
			visualizer.setTimeResolution(scale);
			visualizer.addSelectionListener(selectionListener);
			visualizer.setHidden(!packet.isChannelEnabled(i));
			visualizer.addSelectionListener(listener);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(visualizer);
			currentVisualizer[i] = visualizer;

			if (packet.isChannelEnabled(i)) {
				meta.setVisualizer(visualizer);
			}
		}

		composite.pack();
	}

	/**
	 * Sets the current selection. If from or to is less than equal to zero, the selection
	 * is removed.
	 *
	 * @param from The start of the selection in nanoseconds
	 * @param to The end of the selection in nanoseconds
	 */
	public void setSelection(long from, long to) {
		for (Control c : composite.getChildren()) {
			if (c instanceof ChannelVisualizer) {
				((ChannelVisualizer) c).setSelection(from, to, true);
			}
		}
	}

	/**
	 * Computes the currently selected data. This implementation is truely unefficient (something linke O(n^3))
	 *
	 * @return the currently selected data (via {@link #setSelection(long, long)} or null if there currently is no selection
	 */
	public IDataPacket getSelectedData() {
		int maxChannel = 0;
		List<DataPacketToChannel> selections = new LinkedList<DataPacketToChannel>();
		for (Control c : composite.getChildren()) {
			if (c instanceof ChannelVisualizer) {
				ChannelVisualizer visualizer = (ChannelVisualizer) c;

				IDataPacket selection = visualizer.getSelectedData();
				if (selection != null) {
					selections.add(new DataPacketToChannel(visualizer.getChannelNr(), selection));
					if (visualizer.getChannelNr() + 1 > maxChannel)
						maxChannel = visualizer.getChannelNr() + 1;
				}
			}
		}

		List<IFrame> result = new LinkedList<IFrame>();
		long curTime = 0, nextTime = 0, startTime = -1;
		while (nextTime != -1) {
			boolean[] frameData = new boolean[maxChannel];
			nextTime = curTime;

			for (DataPacketToChannel selection : selections) {
				for (IFrame frame : selection.getPacket().getFrames()) {
					if (frame.getTime() == curTime) {
						if (startTime == -1) startTime = curTime;

						frameData[selection.getChannel()] = frame.getValue(selection.getChannel());
					}
					if (nextTime == 0 || (nextTime == curTime && frame.getTime() > curTime) || (frame.getTime() > curTime && frame.getTime() < nextTime))
						nextTime = frame.getTime();
				}
			}

			if (startTime != -1) result.add(new ArrayBackedFrame(curTime - startTime, frameData));
			if (nextTime == curTime) nextTime = -1;
			curTime = nextTime;
		}

		return new DataPacket(result);
	}

	/**
	 * If this flag is true, selection made on one channel is propagated to all the other channels
	 *
	 * @return the all channel selection flag
	 */
	public boolean isAllChannelSelection() {
		return allChannelSelection;
	}

	/**
	 * If this flag is true, selection made on one channel is propagated to all the other channels
	 *
	 * @param allChannelSelection true if selection propagation should be performed, false if not
	 */
	public void setAllChannelSelection(boolean allChannelSelection) {
		this.allChannelSelection = allChannelSelection;

		for (Control c : composite.getChildren()) {
			if (c instanceof ChannelVisualizer && !((ChannelVisualizer) c).isHidden()) {
				((ChannelVisualizer) c).setSelection(-1, -1, true);
			}
		}
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
	 * @return the tool manager of this visualizer
	 */
	public SelectionBasedToolManager getToolManager() {
		return toolManager;
	}

	/**
	 * @return the input currently set to this visualizer (possibly null)
	 */
	public IDataPacket getInput() {
		return input;
	}

	/**
	 * @return the overlay acceptor for this visualizer.
	 */
	public IOverlayAcceptor getOverlayAcceptor() {
		return new IOverlayAcceptor() {

			public void accept(int channelNr, IChannelOverlay overlay) {
				if (currentVisualizer != null
				        && channelNr < currentVisualizer.length
				        && channelNr >= 0
				        && !currentVisualizer[channelNr].isDisposed()) {

					currentVisualizer[channelNr].getOverlayManager().accept(channelNr, overlay);
				}
			}

		};
	}

}
