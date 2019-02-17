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
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.gdb.launching.ServicesLaunchSequence;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IProgressMonitor;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.services.IGdbServerBackendService;

/**
 * Insert the step of creating the GdbServerBackend before all other steps.
 */
public class GnuArmServicesLaunchSequence extends ServicesLaunchSequence {

	// ------------------------------------------------------------------------

	private DsfSession fSession;
	private GdbLaunch fLaunch;

	Step[] fOurSteps = new Step[] { new Step() {
			@Override
			public void execute(RequestMonitor requestMonitor) {
				fLaunch.getServiceFactory()
				.createService(IGdbServerBackendService.class, fSession, fLaunch.getLaunchConfiguration())
				.initialize(requestMonitor);
			}
		}
	};

	private Step[] fSteps = null;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm services launch sequence.
	 *
	 * @param session the session
	 * @param launch the launch
	 * @param pm the pm
	 */
	public GnuArmServicesLaunchSequence(DsfSession session, GdbLaunch launch, IProgressMonitor pm) {
		super(session, launch, pm);

		GdbActivator.log("GnuArmServicesLaunchSequence()"); //$NON-NLS-1$

		fSession = session;
		fLaunch = launch;
	}

	// ------------------------------------------------------------------------

	@Override
	public Step[] getSteps() {
		// GdbActivator.log("GnuArmServicesLaunchSequence.getSteps()");
		if (fSteps == null) {
			Step[] superSteps = super.getSteps();

			fSteps = new Step[fOurSteps.length + superSteps.length];
			System.arraycopy(fOurSteps, 0, fSteps, 0, fOurSteps.length);
			System.arraycopy(superSteps, 0, fSteps, fOurSteps.length, superSteps.length);
		}
		return fSteps;
	}

	// ------------------------------------------------------------------------
}
