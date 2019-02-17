/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.dsf;

import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.Sequence;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IProgressMonitor;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.services.IGdbServerBackendService;

/**
 * Define the step of creating the GdbServerBackend.
 */
public class GnuArmServerServicesLaunchSequence extends Sequence {

	// ------------------------------------------------------------------------

	private DsfSession fSession;
	private GdbLaunch fLaunch;

	private Step[] fSteps = new Step[] { new Step() {
			@Override
			public void execute(RequestMonitor requestMonitor) {
				fLaunch.getServiceFactory()
				.createService(IGdbServerBackendService.class, fSession, fLaunch.getLaunchConfiguration())
				.initialize(requestMonitor);
			}
		}
	};

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm server services launch sequence.
	 *
	 * @param session the session
	 * @param launch the launch
	 * @param progressMonitor the progress monitor
	 */
	public GnuArmServerServicesLaunchSequence(DsfSession session, GdbLaunch launch, IProgressMonitor progressMonitor) {
		super(session.getExecutor(), progressMonitor, "Start Server", "Start Server Rollback"); //$NON-NLS-1$ //$NON-NLS-2$

		GdbActivator.log("GnuArmServerServicesLaunchSequence()"); //$NON-NLS-1$
		
		fSession = session;
		fLaunch = launch;
	}

	// ------------------------------------------------------------------------

	@Override
	public Step[] getSteps() {
		// GdbActivator.log("GnuArmServerServicesLaunchSequence.getSteps()");
		return fSteps;
	}

	// ------------------------------------------------------------------------
}
