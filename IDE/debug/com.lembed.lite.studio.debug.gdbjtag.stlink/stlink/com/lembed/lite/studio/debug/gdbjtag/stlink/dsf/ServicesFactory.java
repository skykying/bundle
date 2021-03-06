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
package com.lembed.lite.studio.debug.gdbjtag.stlink.dsf;

import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmDebuggerCommandsService;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmGdbServerBackend;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmServicesFactory;
import com.lembed.lite.studio.debug.gdbjtag.stlink.STlinkPlugin;

import org.eclipse.cdt.dsf.mi.service.IMIBackend;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.ILaunchConfiguration;

public class ServicesFactory extends GnuArmServicesFactory {

	// ------------------------------------------------------------------------

	public ServicesFactory(String version, String mode) {
		super(version, mode);

		STlinkPlugin.log("ServicesFactory(" + version + "," + mode + ") " + this);
	}

	// ------------------------------------------------------------------------

	@Override
	protected IMIBackend createBackendGDBService(DsfSession session, ILaunchConfiguration lc) {
		return new GdbBackend(session, lc);
	}

	@Override
	protected GnuArmGdbServerBackend createGdbServerBackendService(DsfSession session, ILaunchConfiguration lc) {
		return new GdbServerBackend(session, lc);
	}

	@Override
	protected GnuArmDebuggerCommandsService createDebuggerCommandsService(DsfSession session, ILaunchConfiguration lc,
			String mode) {
		return new DebuggerCommands(session, lc, mode);
	}

	// ------------------------------------------------------------------------
}
