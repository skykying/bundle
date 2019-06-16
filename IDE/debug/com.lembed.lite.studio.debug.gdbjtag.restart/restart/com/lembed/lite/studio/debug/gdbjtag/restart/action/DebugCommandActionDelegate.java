/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic & Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Liviu Ionescu - initial API and implementation
 *      LiteSTUDIO   -  document and bug fixed.
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.restart.action;

import org.eclipse.debug.ui.actions.DebugCommandAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * The Class DebugCommandActionDelegate.
 */
public abstract class DebugCommandActionDelegate implements
		IWorkbenchWindowActionDelegate, IActionDelegate2 {

	// ------------------------------------------------------------------------

	/**
	 * The real action for this delegate
	 */
	private DebugCommandAction fDebugAction;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new debug command action delegate. 
	 *
	 * @param action the action
	 */
	public DebugCommandActionDelegate(DebugCommandAction action) {
		fDebugAction = action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	@Override
	public void dispose() {
		fDebugAction.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate2#init(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void init(IAction action) {
		fDebugAction.setActionProxy(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.
	 * IWorkbenchWindow)
	 */
	@Override
	public void init(IWorkbenchWindow window) {
		fDebugAction.init(window);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		fDebugAction.run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate2#runWithEvent(org.eclipse.jface.action
	 * .IAction, org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void runWithEvent(IAction action, Event event) {
		run(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IAction action, ISelection s) {
		// do nothing
	}

	/**
	 * Gets the action.
	 *
	 * @return the action
	 */
	protected DebugCommandAction getAction() {
		return fDebugAction;
	}

	// ------------------------------------------------------------------------
}
