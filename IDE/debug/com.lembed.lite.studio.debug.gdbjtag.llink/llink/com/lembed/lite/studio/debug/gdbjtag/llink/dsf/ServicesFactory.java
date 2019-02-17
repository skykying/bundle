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
package com.lembed.lite.studio.debug.gdbjtag.llink.dsf;

import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmDebuggerCommandsService;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmGdbServerBackend;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmServicesFactory;
import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;

import org.eclipse.cdt.dsf.mi.service.IMIBackend;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * A factory for creating Services objects.
 */
public class ServicesFactory extends GnuArmServicesFactory {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new services factory.
	 *
	 * @param version the version
	 * @param mode the mode
	 */
	public ServicesFactory(String version, String mode) {
		super(version, mode);

		LlinkPlugin.log("ServicesFactory(" + version + "," + mode + ") " + this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	// ------------------------------------------------------------------------

	@Override
	protected IMIBackend createBackendGDBService(DsfSession session, ILaunchConfiguration lc) {
		return new GdbBackend(session, lc);
	}

	@Override
	protected GnuArmGdbServerBackend createGdbServerBackendService(DsfSession session, ILaunchConfiguration lc) {
		return new GdbServerBackendWithTrace(session, lc);
	}

	@Override
	protected GnuArmDebuggerCommandsService createDebuggerCommandsService(DsfSession session, ILaunchConfiguration lc,
			String mode) {
		return new STDebuggerCommands(session, lc, mode);
	}

	// ------------------------------------------------------------------------
}
