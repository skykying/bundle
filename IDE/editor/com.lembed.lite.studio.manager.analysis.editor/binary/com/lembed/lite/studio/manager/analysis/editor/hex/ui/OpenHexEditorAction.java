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
package com.lembed.lite.studio.manager.analysis.editor.hex.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;


/**
 * The Class OpenHexEditorAction.
 */
public class OpenHexEditorAction implements IObjectActionDelegate {
	protected IFile file;
	/**
	 * Constructor for Action1.
	 */
	public OpenHexEditorAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	@Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	@Override
    public void run(IAction action) {
	    IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    IWorkbenchPage page = workbenchWindow.getActivePage();

	    // Open an editor on the new file.
	    //
	    try
	    {
	      page.openEditor
	        (new FileEditorInput(file), 
	          "com.lembed.lite.studio.manager.analysis.editor.hex.HexEditor"); //$NON-NLS-1$
	    }
	    catch (PartInitException exception)
	    {
	      MessageDialog.openError(workbenchWindow.getShell(), "Messages.OpenHexEditorAction_1", exception.getMessage());
	    }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	@Override
    public void selectionChanged(IAction action, ISelection selection) {
	    if (selection instanceof IStructuredSelection)
	    {
	      Object object = ((IStructuredSelection)selection).getFirstElement();
	      if (object instanceof IFile)
	      {
	        file = (IFile)object;
	        return;
	      }
	    }
	    file = null;
	    action.setEnabled(false);
	}

}
