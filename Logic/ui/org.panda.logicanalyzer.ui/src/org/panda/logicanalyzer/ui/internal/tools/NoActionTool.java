package org.panda.logicanalyzer.ui.internal.tools;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;
import org.panda.logicanalyzer.ui.editor.IOverlayAcceptor;
import org.panda.logicanalyzer.ui.editor.ISelectionBasedTool;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;

/**
 * This tool simply does nothing. It's used to implement the serial/parallel
 * selection tools.
 */
public class NoActionTool implements ISelectionBasedTool {

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
	public ISelectionListener getSelectionListener() {
		return null;
	}

//	@Override
	public void initialize(IOverlayAcceptor acceptor, IDataPacket packet) {

	}

}
