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
package com.lembed.lite.studio.debug.gdbjtag.dsf;

import java.util.Collection;

import org.eclipse.cdt.debug.gdbjtag.core.jtagdevice.DefaultGDBJtagDeviceImpl;

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;

/**
 * The Class ArmJTagDevice.
 */
public class ArmJTagDevice extends DefaultGDBJtagDeviceImpl {

	@Override
	public void doDelay(int delay, Collection<String> commands) {
		GdbActivator.log("delay " + commands.toString()); //$NON-NLS-1$
	}

	@Override
	public void doHalt(Collection<String> commands) {
		GdbActivator.log("halt " + commands.toString()); //$NON-NLS-1$
	}

	@Override
	public void doReset(Collection<String> commands) {
		GdbActivator.log("reset " + commands.toString()); //$NON-NLS-1$
	}

}
