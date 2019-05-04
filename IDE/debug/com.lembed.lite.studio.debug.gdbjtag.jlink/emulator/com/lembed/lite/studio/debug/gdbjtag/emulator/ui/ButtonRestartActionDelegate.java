/*******************************************************************************
 * Copyright (c) 2013 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.emulator.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;

public class ButtonRestartActionDelegate implements
		IWorkbenchWindowActionDelegate {

	@Override
	public void run(IAction action) {
		if (DevicePlugin.getInstance().isDebugging()) {
			System.out.println("Restart.run(" + action + ")");
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (DevicePlugin.getInstance().isDebugging()) {
			System.out.println("Restart.selectionChanged(" + action + ","
					+ selection + ")");
		}
	}

	@Override
	public void dispose() {
		if (DevicePlugin.getInstance().isDebugging()) {
			System.out.println("Restart.dispose()");
		}
	}

	@Override
	public void init(IWorkbenchWindow window) {
		if (DevicePlugin.getInstance().isDebugging()) {
			System.out.println("Restart.init()");
		}
	}

}
