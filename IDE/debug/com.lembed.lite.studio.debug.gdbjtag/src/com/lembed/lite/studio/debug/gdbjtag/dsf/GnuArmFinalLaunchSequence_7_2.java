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

package com.lembed.lite.studio.debug.gdbjtag.dsf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitorWithProgress;
import org.eclipse.cdt.dsf.gdb.service.IGDBProcesses;
import org.eclipse.cdt.dsf.gdb.service.command.IGDBControl;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;

/**
 * The Class GnuArmFinalLaunchSequence_7_2.
 */
public class GnuArmFinalLaunchSequence_7_2 extends GnuArmFinalLaunchSequence {

	// ------------------------------------------------------------------------

	private String[] jtagInitSteps = { "stepInitializeJTAGSequence_7_2" }; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	private DsfSession fSession;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm final launch sequence 7 2.
	 *
	 * @param session the session
	 * @param attributes the attributes
	 * @param mode the mode
	 * @param rm the rm
	 */
	public GnuArmFinalLaunchSequence_7_2(DsfSession session, Map<String, Object> attributes, String mode,
			RequestMonitorWithProgress rm) {
		super(session, attributes, mode, rm);
		fSession = session;
	}

	// ------------------------------------------------------------------------

	@Override
	protected String[] getExecutionOrder(String group) {

		// Initialise the list with the base class' steps
		// We need to create a list that we can modify, which is why we
		// create our own ArrayList.
		List<String> orderList = new ArrayList<>(Arrays.asList(super.getExecutionOrder(group)));

		if (GROUP_JTAG.equals(group)) {

			// Insert our steps right after the existing steps.
			orderList.addAll(orderList.indexOf("stepInitializeJTAGFinalLaunchSequence") + 1, //$NON-NLS-1$
					Arrays.asList(jtagInitSteps));

		}

		return orderList.toArray(new String[orderList.size()]);
	}

	// ------------------------------------------------------------------------

	/**
	 * Initialize the members of the DebugNewProcessSequence_7_2 class. This
	 * step is mandatory for the rest of the sequence to complete.
	 * @param rm RequestMonitor
	 */
	@Execute
	public void stepInitializeJTAGSequence_7_2(RequestMonitor rm) {
		DsfServicesTracker tracker = new DsfServicesTracker(GdbActivator.getInstance().getBundle().getBundleContext(),
				fSession.getId());
		IGDBControl gdbControl = tracker.getService(IGDBControl.class);
		IGDBProcesses procService = tracker.getService(IGDBProcesses.class);
		tracker.dispose();

		if (gdbControl == null || procService == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1, "Cannot obtain service", null)); //$NON-NLS-1$
			rm.done();
			return;
		}
		setContainerContext(procService.createContainerContextFromGroupId(gdbControl.getContext(), "i1")); //$NON-NLS-1$
		rm.done();
	}

	// ------------------------------------------------------------------------
}
