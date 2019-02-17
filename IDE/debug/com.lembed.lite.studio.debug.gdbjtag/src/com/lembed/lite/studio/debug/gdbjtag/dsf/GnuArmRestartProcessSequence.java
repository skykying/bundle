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
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.DsfExecutor;
import org.eclipse.cdt.dsf.concurrent.IDsfStatusConstants;
import org.eclipse.cdt.dsf.concurrent.ReflectionSequence;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.debug.service.IRunControl.IContainerDMContext;
import org.eclipse.cdt.dsf.gdb.service.IGDBProcesses;
import org.eclipse.cdt.dsf.gdb.service.command.IGDBControl;
import org.eclipse.cdt.dsf.mi.service.IMICommandControl;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.DebugUtils;
import com.lembed.lite.studio.debug.gdbjtag.services.IGnuArmDebuggerCommandsService;

/**
 * The Class GnuArmRestartProcessSequence.
 */
public class GnuArmRestartProcessSequence extends ReflectionSequence {

	// ------------------------------------------------------------------------

	private IGDBControl fCommandControl;
	private CommandFactory fCommandFactory;
	private IGDBProcesses fProcService;
	// private IReverseRunControl fReverseService;
	// private IGDBBackend fBackend;
	// private IGDBJtagDevice fGdbJtagDevice;
	private IGnuArmDebuggerCommandsService fDebuggerCommands;

	private DsfServicesTracker fTracker;

	// This variable will be used to store the original container context,
	// but once the new process is started (restarted), it will contain the new
	// container context. This new container context has for parent the process
	// context, which holds the new pid.
	private IContainerDMContext fContainerDmc;

	// If the user requested a stop_on_main, this variable will hold the
	// breakpoint
	// private MIBreakpoint fUserBreakpoint;
	// Since the stop_on_main option allows the user to set the breakpoint on
	// any
	// symbol, we use this variable to know if the stop_on_main breakpoint was
	// really
	// on the main() method.
	// private boolean fUserBreakpointIsOnMain;

	

	// private final boolean fRestart;
	// private final DataRequestMonitor<IContainerDMContext>
	// fDataRequestMonitor;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm restart process sequence.
	 *
	 * @param executor the executor
	 * @param containerDmc the container dmc
	 * @param attributes the attributes
	 * @param restart the restart
	 * @param rm the rm
	 */
	public GnuArmRestartProcessSequence(DsfExecutor executor, IContainerDMContext containerDmc,
			Map<String, Object> attributes, boolean restart, DataRequestMonitor<IContainerDMContext> rm) {
		super(executor, rm);

		assert executor != null;
		assert containerDmc != null;

		fContainerDmc = containerDmc;
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the container context.
	 *
	 * @return the container context
	 */
	protected IContainerDMContext getContainerContext() {
		return fContainerDmc;
	}

	private void queueCommands(List<String> commands, RequestMonitor rm) {
		DebugUtils.queueCommands(commands, rm, fCommandControl, getExecutor());
	}

	@Override
	protected String[] getExecutionOrder(String group) {
		if (GROUP_TOP_LEVEL.equals(group)) {
			return new String[] { //
					"stepInitializeBaseSequence", //$NON-NLS-1$
					"stepRestartCommands", //$NON-NLS-1$
			};
		}
		return null;
	}

	/**
	 * Initialize the members of the StartOrRestartProcessSequence_7_0 class.
	 * This step is mandatory for the rest of the sequence to complete.
	 * @param rm RequestMonitor
	 */
	@Execute
	public void stepInitializeBaseSequence(RequestMonitor rm) {

		fTracker = new DsfServicesTracker(GdbActivator.getInstance().getBundle().getBundleContext(),
				fContainerDmc.getSessionId());
		fCommandControl = fTracker.getService(IGDBControl.class);
		IMICommandControl miCmdCtl = fTracker.getService(IMICommandControl.class);
		if(miCmdCtl != null) {
		    fCommandFactory = miCmdCtl.getCommandFactory();
		}
		fProcService = fTracker.getService(IGDBProcesses.class);
		fDebuggerCommands = fTracker.getService(IGnuArmDebuggerCommandsService.class);
		if (fCommandControl == null || fCommandFactory == null || fProcService == null || fDebuggerCommands == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, IDsfStatusConstants.INTERNAL_ERROR,
					"Cannot obtain service", null)); //$NON-NLS-1$
			rm.done();
			return;
		}

		// fBackend = fTracker.getService(IGDBBackend.class);
		// fGdbJtagDevice = DebugUtils.getGDBJtagDevice(fAttributes);
		rm.done();
	}

	/**
	 * Step restart commands.
	 *
	 * @param rm the rm
	 */
	@Execute
	public void stepRestartCommands(final RequestMonitor rm) {

		List<String> commandsList = new ArrayList<>();

		IStatus status = fDebuggerCommands.addGnuArmRestartCommands(commandsList);

		if (!status.isOK()) {
			rm.setStatus(status);
			rm.done();
			return;
		}

		queueCommands(commandsList, rm);
	}

	// ------------------------------------------------------------------------
}
