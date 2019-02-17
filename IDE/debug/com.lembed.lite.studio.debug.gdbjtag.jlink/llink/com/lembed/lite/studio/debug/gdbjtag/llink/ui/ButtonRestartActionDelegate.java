/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.llink.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;

/**
 * The Class ButtonRestartActionDelegate.
 */
public class ButtonRestartActionDelegate implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		LlinkPlugin.log("Restart.run(" + action + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		LlinkPlugin.log("Restart.selectionChanged(" + action + "," + selection + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	public void dispose() {
		LlinkPlugin.log("Restart.dispose()"); //$NON-NLS-1$
	}

	@Override
	public void init(IWorkbenchWindow window) {
		LlinkPlugin.log("Restart.init()"); //$NON-NLS-1$
	}

}
