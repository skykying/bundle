package org.panda.logicanalyzer.ui.internal.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

/**
 * Autoscales the active graphing editor.
 */
public class GraphingEditorAutoScaleAction implements IEditorActionDelegate {

	/**
	 * The editor we're going to set the scale at
	 */
	private IEditorPart targetEditor;


	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}


	public void run(IAction action) {
		if (targetEditor == null) return;

		((GraphingEditor)targetEditor).autoScale();
	}


	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
