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
package com.lembed.lite.studio.debug.gdbjtag.stlink.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.lembed.lite.studio.debug.gdbjtag.stlink.STlinkPlugin;

public class ButtonRestartActionDelegate implements IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		STlinkPlugin.log("Restart.run(" + action + ")");
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		STlinkPlugin.log("Restart.selectionChanged(" + action + "," + selection + ")");
	}

	@Override
	public void dispose() {
		STlinkPlugin.log("Restart.dispose()");
	}

	@Override
	public void init(IWorkbenchWindow window) {
		STlinkPlugin.log("Restart.init()");
	}

}
