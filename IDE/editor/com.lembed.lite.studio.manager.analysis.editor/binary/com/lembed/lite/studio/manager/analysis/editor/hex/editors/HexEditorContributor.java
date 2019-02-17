/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.hex.editors;


import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * @author ganzhi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HexEditorContributor extends EditorActionBarContributor {
	private HexEditor fCurrentEditor;

	/**
	 * 
	 */
	public HexEditorContributor() {
		super();
	}
	
	@Override
    public void init(IActionBars bars) {
		super.init(bars);
		Action undoAction = new Action() {
		    @Override
            public void run() {
		    	if (fCurrentEditor != null)
		    		fCurrentEditor.undo();
		    }
		};
		bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undoAction);

		Action redoAction = new Action() {
		    @Override
            public void run() {
		    	if (fCurrentEditor != null)
		    		fCurrentEditor.redo();
		    }
		};
		bars.setGlobalActionHandler(ActionFactory.REDO.getId(), redoAction);

		Action findAction = new Action() {
			@Override
            public void run() {
				if (fCurrentEditor != null)
					fCurrentEditor.getControl().toggleRulerVisibility();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
		
		Action cutAction = new Action() {
			@Override
            public void run() {
				if (fCurrentEditor != null) {
					fCurrentEditor.getControl().cut();
				}
			}
		};
		bars.setGlobalActionHandler(ActionFactory.CUT.getId(), cutAction);

		Action copyAction = new Action() {
			@Override
            public void run() {
				if (fCurrentEditor != null)
					fCurrentEditor.getControl().copy();
			}
		};
		bars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

		Action pasteAction = new Action() {
			@Override
            public void run() {
				if (fCurrentEditor != null) {
					fCurrentEditor.getControl().paste();
				}
			}
		};
		bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteAction);

		Action deleteAction = new Action() {
			@Override
            public void run() {
				if (fCurrentEditor != null) {
					fCurrentEditor.getControl().deleteSelection();
				}
			}
		};
		bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);

		Action selectAllAction = new Action() {
			@Override
            public void run() {
				if (fCurrentEditor != null) {
					fCurrentEditor.getControl().getHexTable().selectAll();
				}
			}
		};
		bars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAllAction);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	@Override
    public void setActiveEditor(IEditorPart targetEditor) {
		super.setActiveEditor(targetEditor);
		
		if (!(targetEditor instanceof HexEditor))
			return;
		fCurrentEditor = (HexEditor) targetEditor;

		getActionBars().updateActionBars();
	}
}
