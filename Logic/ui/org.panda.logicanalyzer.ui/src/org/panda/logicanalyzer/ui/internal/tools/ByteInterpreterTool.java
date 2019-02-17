package org.panda.logicanalyzer.ui.internal.tools;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.util.byteInterpreter.ByteInterpreter;
import org.panda.logicanalyzer.core.util.byteInterpreter.ByteInterpreterConfiguration;
import org.panda.logicanalyzer.core.util.byteInterpreter.ByteInterpreterMode;
import org.panda.logicanalyzer.core.util.byteInterpreter.ByteInterpreterResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.panda.logicanalyzer.ui.editor.AbstractChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IOverlayAcceptor;
import org.panda.logicanalyzer.ui.editor.ISelectionBasedTool;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;
import org.panda.logicanalyzer.ui.overlays.StringOverlay;

/**
 * This class generates overlays which display the result of a {@link ByteInterpreter}.
 */
public class ByteInterpreterTool implements ISelectionBasedTool, ISelectionListener {

	/**
	 * The overlay we use
	 */
	private static class Overlay extends AbstractChannelOverlay {

		/**
		 * The interpreter result we're presenting
		 */
		private final ByteInterpreterResult interpreterResult;

		/**
		 * The start of the overlay
		 */
		private final long from;

		/**
		 * The end of the overlay
		 */
		private final long to;

		public Overlay(ByteInterpreterResult interpreterResult, long from, long to) {
			this.interpreterResult = interpreterResult;
			this.from = from;
			this.to = to;
		}

//		@Override
		public void draw(GC gc, Rectangle bounds, int channelHeight) {
			byte[] resultingBytes = interpreterResult.getResultingBytes();

			/*
			 * This is a pretty shaby way of getting the scale, since it's missing
			 * proper semantics and assumes that the length/width ratio is only
			 * influenced by the scale. We propably should provide an appropriate API for that
			 */
			long nsPerPixel = getLength() / bounds.width;
			boolean alternation = false;
			int i = 0;
			for (long t : interpreterResult.getBitStarts()) {
				int height = (bounds.height - 20) / 2;

				if (i % 8 == 0 && i / 8 < resultingBytes.length) {
					byte resultingByte = resultingBytes[i / 8];
					String text = " 0x" + String.format("%02x", resultingByte).toUpperCase();
					gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
					gc.drawText(text, (int) (t / nsPerPixel), channelHeight, true);

					if (interpreterResult.getBitsArray()[i]) height -= 10;
				}

				gc.setBackground(gc.getDevice().getSystemColor((i % 8 == 0) ? SWT.COLOR_BLACK : alternation ? SWT.COLOR_WHITE : SWT.COLOR_GRAY));
				i++;
				alternation = !alternation;
				int offset = (int)((t) / nsPerPixel);
				if (interpreterResult.getBitLength() >= 0) {
					gc.fillRectangle(new Rectangle(offset, height, (int)(interpreterResult.getBitLength() / nsPerPixel), 10));
				} else {
					gc.drawLine(offset, 0, offset, channelHeight);
				}
			}

			gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_RED));
			gc.drawText("BI not correct. Known Bug!", bounds.x, 0, true);
		}

//		@Override
		public int getHeight(int channelHeight) {
			return channelHeight + 20;
		}

//		@Override
		public long getLength() {
			return to - from;
		}

//		@Override
		public long getOffset() {
			return from;
		}

	}

	/**
	 * The byte interpreter we use
	 */
	private final ByteInterpreter interpreter;

	/**
	 * The current overlay (possibly null)
	 */
	private IChannelOverlay currentOverlay;

	/**
	 * The packet we're working on. Available after a call to {@link #initialize(IOverlayAcceptor, IDataPacket)}
	 */
	private IDataPacket packet;

	/**
	 * The acceptor we're working with. Available after a call to {@link #initialize(IOverlayAcceptor, IDataPacket)}
	 */
	private IOverlayAcceptor acceptor;

	/**
	 * The configuration of our {@link #interpreter}
	 */
	private final ByteInterpreterConfiguration interpreterConfiguration;


	public ByteInterpreterTool() {
		interpreterConfiguration = new ByteInterpreterConfiguration();
		interpreter = new ByteInterpreter(interpreterConfiguration);
	}

	/* (non-Javadoc)
	 * @see org.panda.logicanalyzer.ui.editor.AbstractOverlayFactory#getSelectionListener()
	 */
	//	@Override
	public ISelectionListener getSelectionListener() {
		return this;
	}

	//	@Override
	public void selectionChanged(int channelNr, long from, long to) {
		if (currentOverlay != null && !currentOverlay.isDisposed()) currentOverlay.dispose();
		if (from < 0 || to < 0) return;

		IChannelOverlay result;
		try {
			ByteInterpreterResult interpreterResult = interpreter.interpret(packet.slice(from, to), channelNr);
			result = new Overlay(interpreterResult, from, to);
		} catch (CoreException e) {
			result = new StringOverlay("ERROR " + e.getMessage(), from, to);
		}

		currentOverlay = result;
		acceptor.accept(channelNr, currentOverlay);
	}

	//	@Override
	public void createContents(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		new Label(parent, SWT.NONE).setText("Mode:");
		ComboViewer modeViewer = new ComboViewer(parent, SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(modeViewer.getControl());
		modeViewer.setContentProvider(new ArrayContentProvider());
		modeViewer.setLabelProvider(new LabelProvider());
		modeViewer.setInput(ByteInterpreterMode.values());

		new Label(parent, SWT.NONE);
		final Button showCharValueButton = new Button(parent, SWT.CHECK);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(showCharValueButton);
		showCharValueButton.setText("Show char value");
		showCharValueButton.setSelection(interpreterConfiguration.isShowCharValue());
		showCharValueButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				interpreterConfiguration.setShowCharValue(showCharValueButton.getSelection());
			}

		});

		new Label(parent, SWT.NONE);
		final Button litteEndianButton = new Button(parent, SWT.CHECK);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(showCharValueButton);
		litteEndianButton.setText("Little endian");
		litteEndianButton.setSelection(interpreterConfiguration.isLittleEndian());
		litteEndianButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				interpreterConfiguration.setLittleEndian(litteEndianButton.getSelection());
			}

		});

		new Label(parent, SWT.NONE).setText("Bit width [ns]:");
		final Spinner bitWidthSpinner = new Spinner(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(bitWidthSpinner);
		bitWidthSpinner.setMinimum(0);
		bitWidthSpinner.setIncrement(100);
		bitWidthSpinner.addModifyListener(new ModifyListener() {

			//@Override
			public void modifyText(ModifyEvent e) {
				interpreterConfiguration.setBitWidth(bitWidthSpinner.getSelection());
			}

		});

		new Label(parent, SWT.NONE).setText("Clock channel:");
		final ComboViewer clockChannelViewer = new ComboViewer(parent, SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(clockChannelViewer.getControl());
		clockChannelViewer.setContentProvider(new ArrayContentProvider());
		clockChannelViewer.setLabelProvider(new LabelProvider());
		Integer[] channels = packet == null ? new Integer[64] : new Integer[packet.getAmountOfChannels()];
		for (int i = 0; i < channels.length; i++) channels[i] = i;
		clockChannelViewer.setInput(channels);
		clockChannelViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			//@Override
			public void selectionChanged(SelectionChangedEvent event) {
				interpreterConfiguration.setClockChannelNr((Integer) ((IStructuredSelection) event.getSelection()).getFirstElement());
			}

		});
		clockChannelViewer.setSelection(new StructuredSelection(clockChannelViewer.getElementAt(0)));

		new Label(parent, SWT.NONE).setText("Clock channel:");
		final ComboViewer risingFallingEdgeViewer = new ComboViewer(parent, SWT.READ_ONLY | SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(risingFallingEdgeViewer.getControl());
		risingFallingEdgeViewer.setContentProvider(new ArrayContentProvider());
		risingFallingEdgeViewer.setLabelProvider(new LabelProvider());
		risingFallingEdgeViewer.setInput(new String[] { "rising edge", "falling edge" });
		risingFallingEdgeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			//@Override
			public void selectionChanged(SelectionChangedEvent event) {
				interpreterConfiguration.setOnRisingEdge("rising edge".equals(((IStructuredSelection) event.getSelection()).getFirstElement()));
			}

		});
		clockChannelViewer.setSelection(new StructuredSelection(clockChannelViewer.getElementAt(0)));


		modeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			//@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ByteInterpreterMode mode = (ByteInterpreterMode) ((IStructuredSelection) event.getSelection()).getFirstElement();
				interpreterConfiguration.setMode(mode);

				bitWidthSpinner.setEnabled(mode == ByteInterpreterMode.FixedLength);
				clockChannelViewer.getControl().setEnabled(mode == ByteInterpreterMode.ClockSignal);
				risingFallingEdgeViewer.getControl().setEnabled(mode == ByteInterpreterMode.ClockSignal);
			}

		});
		modeViewer.setSelection(new StructuredSelection(modeViewer.getElementAt(0)));
	}

	//@Override
	public void disable() {
		// don't care
	}

	//@Override
	public void enable() throws CoreException {
		// don't care
	}

	//	@Override
	public void initialize(IOverlayAcceptor acceptor, IDataPacket packet) {
		this.acceptor = acceptor;
		this.packet = packet;

	}

}
