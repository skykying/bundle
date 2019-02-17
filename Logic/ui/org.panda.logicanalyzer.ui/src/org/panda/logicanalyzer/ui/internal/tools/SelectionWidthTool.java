package org.panda.logicanalyzer.ui.internal.tools;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;
import org.panda.logicanalyzer.ui.editor.IChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IOverlayAcceptor;
import org.panda.logicanalyzer.ui.editor.ISelectionBasedTool;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;
import org.panda.logicanalyzer.ui.overlays.StringOverlay;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.util.TimeConverter;

/**
 * The factory creating selection width overlays
 */
public class SelectionWidthTool implements ISelectionBasedTool, ISelectionListener {

	/**
	 * The overlay we created the last time
	 */
	private IChannelOverlay currentOverlay = null;

	/**
	 * The acceptor we're working with. Available after a call to {@link #initialize(IOverlayAcceptor, IDataPacket)}
	 */
	private IOverlayAcceptor acceptor;

//	@Override
	public ISelectionListener getSelectionListener() {
		return this;
	}

//	@Override
	public void selectionChanged(int channelNr, long from, long to) {
		if (currentOverlay != null) {
			currentOverlay.dispose();
		}

		if (channelNr == -1 && to > 0 && from > 0 && to - from > 0) {
			currentOverlay = new StringOverlay(TimeConverter.toString(to - from), from, to);
			acceptor.accept(0, currentOverlay);
		}
	}

//	@Override
	public void createContents(Composite parent) {

	}

//	@Override
	public void disable() {

	}

//	@Override
	public void enable() throws CoreException {

	}

//	@Override
	public void initialize(IOverlayAcceptor acceptor, IDataPacket packet) {
		this.acceptor = acceptor;
	}

}
