package org.panda.logicanalyzer.ui.internal.editor;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

/**
 * Sets the scale on a {@link GraphingEditor}.
 */
public class GraphingEditorSetScaleAction implements IEditorActionDelegate {

	/**
	 * The editor we're going to set the scale at
	 */
	private IEditorPart targetEditor;


	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		this.targetEditor = targetEditor;
	}


	public void run(IAction action) {
		if (!(targetEditor instanceof GraphingEditor)) return;

		InputDialog dialog = new InputDialog(
		    targetEditor.getEditorSite().getShell(),
		    "Set scale",
		    "Please enter a new scale in [ns/px]:",
		    String.valueOf(((GraphingEditor) targetEditor).getScale()),
			new IInputValidator() {
	
	
				public String isValid(String newText) {
					try {
						long value = Long.parseLong(newText);
						if (value <= 0) return "Value must be greater unequal zero";
					} catch (NumberFormatException e) {
						return newText + " is not a number";
					}
	
					return null;
				}
	
			});
		if (dialog.open() != InputDialog.OK) return;

		((GraphingEditor) targetEditor).setScale(Long.parseLong(dialog.getValue()));
	}


	public void selectionChanged(IAction action, ISelection selection) {
		// don't care
	}

}
