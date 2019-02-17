package org.panda.logicanalyzer.ui.internal.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

/**
 * Sets the {@link GraphingEditor#setAllChannelSelection(boolean)} flag.
 */
public class GraphingEditorAllChannelSelectionAction implements IEditorActionDelegate {

	/**
	 * The editor we're going to set the scale at
	 */
	private IEditorPart targetEditor;


	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}


	public void run(IAction action) {
		if (targetEditor == null) return;

		((GraphingEditor)targetEditor).setAllChannelSelection(action.isChecked());
	}


	public void selectionChanged(IAction action, ISelection selection) {
		// don't care
	}

}
