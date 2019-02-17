/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic.
 * Copyright (c) 2013 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Liviu Ionescu - initial version
 *     Lembed - fixed jtag device error
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.dsf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.debug.gdbjtag.core.jtagdevice.IGDBJtagDevice;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitorWithProgress;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.gdb.service.IGDBBackend;
import org.eclipse.cdt.dsf.gdb.service.command.IGDBControl;
import org.eclipse.cdt.dsf.mi.service.IMIProcesses;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchManager;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.DebugUtils;
import com.lembed.lite.studio.debug.gdbjtag.services.IGnuArmDebuggerCommandsService;
import com.lembed.lite.studio.debug.gdbjtag.services.IPeripheralMemoryService;
import com.lembed.lite.studio.debug.gdbjtag.services.IPeripheralsService;

/**
 * The Class GnuArmFinalLaunchSequence.
 */
public class GnuArmFinalLaunchSequence extends GDBJtagDSFFinalLaunchSequence {

	// ------------------------------------------------------------------------

	private Map<String, Object> fAttributes;
	private DsfSession fSession;

	private DsfServicesTracker fTracker;
	private IGDBBackend fGdbBackend;
	private IGDBControl fCommandControl;
	private IMIProcesses fProcService;
	private IGDBJtagDevice fGdbJtagDevice;
	private String fMode;

	private IGnuArmDebuggerCommandsService fDebuggerCommands;

	// ------------------------------------------------------------------------

	private String[] topPreInitSteps = { "stepCreatePeripheralService",  //$NON-NLS-1$
										 "stepCreatePeripheralMemoryService", //$NON-NLS-1$
	                                     "stepCreateDebuggerCommandsService" //$NON-NLS-1$
	                                   };

	private String[] topToRemove = { "stepRemoteConnection",  //$NON-NLS-1$
									 "stepAttachToProcess"}; //$NON-NLS-1$

	private String[] jtagPreInitSteps = {};

	private String[] jtagResetStep = { "stepGnuArmReset" }; //$NON-NLS-1$
	private String[] jtagStartStep = { "stepGnuArmStart" }; //$NON-NLS-1$

	private String[] jtagToRemove = { "stepLoadSymbols",  //$NON-NLS-1$
									  "stepResetBoard",  //$NON-NLS-1$
									  "stepDelayStartup",  //$NON-NLS-1$
									  "stepHaltBoard", //$NON-NLS-1$
	                                  "stepUserInitCommands",  //$NON-NLS-1$
	                                  "stepLoadImage",  //$NON-NLS-1$
	                                  "stepSetProgramCounter",  //$NON-NLS-1$
	                                  "stepStopScript",  //$NON-NLS-1$
	                                  "stepResumeScript", //$NON-NLS-1$
	                                  "stepUserDebugCommands" //$NON-NLS-1$
	                                };

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm final launch sequence.
	 *
	 * @param session the session
	 * @param attributes the attributes
	 * @param mode the mode
	 * @param rm the rm
	 */
	public GnuArmFinalLaunchSequence(DsfSession session, Map<String, Object> attributes, String mode,
	                                 RequestMonitorWithProgress rm) {
		super(session, attributes, rm);
		fAttributes = attributes;
		fSession = session;
		fMode = mode;
	}

	// ------------------------------------------------------------------------

	@Override
	protected String[] getExecutionOrder(String group) {


		GdbActivator.log("GnuArmFinalLaunchSequence.getExecutionOrder(" + group + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		// Initialise the list with the base class' steps
		// We need to create a list that we can modify, which is why we
		// create our own ArrayList.
		List<String> orderList = new ArrayList<>(Arrays.asList(super.getExecutionOrder(group)));

		if (GROUP_TOP_LEVEL.equals(group)) {

			for (int i = 0; i < topToRemove.length; ++i) {
				int ix = orderList.indexOf(jtagToRemove[i]);
				if (ix >= 0) {
					orderList.remove(ix);
				}
			}

			// Insert the new steps at he beginning
			orderList.addAll(0, Arrays.asList(topPreInitSteps));

		} else if (GROUP_JTAG.equals(group)) {

			for (int i = 0; i < jtagToRemove.length; ++i) {
				int ix = orderList.indexOf(jtagToRemove[i]);
				if (ix >= 0) {
					orderList.remove(ix);
				}
			}

			// Insert the new steps at he beginning
			orderList.addAll(0, Arrays.asList(jtagPreInitSteps));

			// Insert our steps right after the existing steps.
			orderList.addAll(orderList.indexOf("stepConnectToTarget") + 1, Arrays.asList(jtagResetStep)); //$NON-NLS-1$

			// Insert our steps right before the existing steps.
			orderList.addAll(orderList.indexOf("stepJTAGCleanup"), Arrays.asList(jtagStartStep)); //$NON-NLS-1$

		}

		return orderList.toArray(new String[orderList.size()]);
	}

	/**
	 * Step create peripheral service.
	 *
	 * @param rm the rm
	 */
	@Execute
	public void stepCreatePeripheralService(RequestMonitor rm) {

		GdbLaunch launch = ((GdbLaunch) this.fSession.getModelAdapter(ILaunch.class));
		IPeripheralsService service = launch.getServiceFactory()
		                              .createService(IPeripheralsService.class, launch.getSession(), new Object[0]);

		GdbActivator.log("GnuArmFinalLaunchSequence.stepCreatePeripheralService() " + service); //$NON-NLS-1$
		if (service != null) {
			service.initialize(rm);
		} else {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Unable to start PeripheralService")); //$NON-NLS-1$
			rm.done();
		}
	}

	/**
	 * Step create peripheral memory service.
	 *
	 * @param rm the RequestMonitor
	 */
	@Execute
	public void stepCreatePeripheralMemoryService(RequestMonitor rm) {

		GdbLaunch launch = ((GdbLaunch) this.fSession.getModelAdapter(ILaunch.class));
		IPeripheralMemoryService service = launch.getServiceFactory()
		                                   .createService(IPeripheralMemoryService.class, launch.getSession(), launch.getLaunchConfiguration());

		GdbActivator.log("GnuArmFinalLaunchSequence.stepCreatePeripheralMemoryService() " + service); //$NON-NLS-1$
		if (service != null) {
			service.initialize(rm);
		} else {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Unable to start PeripheralMemoryService")); //$NON-NLS-1$
			rm.done();
		}
	}

	/**
	 * Step create debugger commands service.
	 *
	 * @param rm the RequestMonitor
	 */
	@Execute
	public void stepCreateDebuggerCommandsService(RequestMonitor rm) {

		GdbLaunch launch = ((GdbLaunch) this.fSession.getModelAdapter(ILaunch.class));
		GnuArmDebuggerCommandsService service = (GnuArmDebuggerCommandsService) launch.getServiceFactory()
		                                        .createService(IGnuArmDebuggerCommandsService.class, launch.getSession(),
		                                                launch.getLaunchConfiguration());

		GdbActivator.log("GnuArmFinalLaunchSequence.stepCreateDebuggerCommandsService() " + service); //$NON-NLS-1$
		if (service != null) {
			service.initialize(rm);
		} else {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Unable to start GnuArmDebuggerCommandsService")); //$NON-NLS-1$
			rm.done();
		}
	}

	// This function is used to capture the private objects
	@Override
    @Execute
	public void stepInitializeFinalLaunchSequence(RequestMonitor rm) {

		GdbActivator.log("GnuArmFinalLaunchSequence.stepInitializeFinalLaunchSequence()"); //$NON-NLS-1$
		
		fTracker = new DsfServicesTracker(GdbActivator.getInstance().getBundle().getBundleContext(), fSession.getId());
		fGdbBackend = fTracker.getService(IGDBBackend.class);
		if (fGdbBackend == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1, "Cannot obtain GDBBackend service", null)); //$NON-NLS-1$
			rm.done();
			return;
		}

		fCommandControl = fTracker.getService(IGDBControl.class);
		if (fCommandControl == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1, "Cannot obtain control service", null)); //$NON-NLS-1$
			rm.done();
			return;
		}

		fCommandControl.getCommandFactory();
		fProcService = fTracker.getService(IMIProcesses.class);
		if (fProcService == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1, "Cannot obtain process service", null)); //$NON-NLS-1$
			rm.done();
			return;
		}

		fDebuggerCommands = fTracker.getService(IGnuArmDebuggerCommandsService.class);
		if (fDebuggerCommands == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1, "Cannot obtain debugger commands service", //$NON-NLS-1$
			                        null));
			rm.done();
			return;
		}
		fDebuggerCommands.setAttributes(fAttributes);

		super.stepInitializeFinalLaunchSequence(rm);
	}

	@Override
    @Execute
	public void stepInitializeJTAGFinalLaunchSequence(RequestMonitor rm) {

		GdbActivator.log("GnuArmFinalLaunchSequence.stepInitializeJTAGFinalLaunchSequence()"); //$NON-NLS-1$
		super.stepInitializeJTAGFinalLaunchSequence(rm);
	}

	// ------------------------------------------------------------------------

	private void queueCommands(List<String> commands, RequestMonitor rm) {
		if(fCommandControl == null) {
			rm.done();
			return;
		}
		DebugUtils.queueCommands(commands, rm, fCommandControl, getExecutor());		
	}

	// ------------------------------------------------------------------------

	/**
	 * These steps are part of the GROUP_TOP_LEVEL.
	 *
	 * [stepInitializeFinalLaunchSequence, stepSetEnvironmentDirectory,
	 * stepSetBreakpointPending, stepEnablePrettyPrinting, stepSetPrintObject,
	 * stepSetCharset, stepSourceGDBInitFile,
	 * stepSetAutoLoadSharedLibrarySymbols, stepSetSharedLibraryPaths,
	 * stepRemoteConnection, stepAttachToProcess, GROUP_JTAG,
	 * stepDataModelInitializationComplete, stepCleanup]
	 */

	@Override
    @Execute
	public void stepSourceGDBInitFile(final RequestMonitor rm) {

		final List<String> commandsList = new ArrayList<>();

		IStatus status = fDebuggerCommands.addGdbInitCommandsCommands(commandsList);
		if (!status.isOK()) {
			rm.setStatus(status);
			rm.done();
			return;
		}

		super.stepSourceGDBInitFile(new RequestMonitor(getExecutor(), rm) {

			@Override
            protected void handleSuccess() {
				queueCommands(commandsList, rm);
			}
		});
	}

	// ------------------------------------------------------------------------

	/**
	 * These steps are part of the GROUP_JTAG.
	 *
	 * [stepInitializeJTAGFinalLaunchSequence, stepRetrieveJTAGDevice,
	 * stepLoadSymbols, stepConnectToTarget, stepResetBoard, stepDelayStartup,
	 * stepHaltBoard, stepUserInitCommands, stepLoadImage, stepUpdateContainer,
	 * stepInitializeMemory, stepSetArguments, stepSetEnvironmentVariables,
	 * stepStartTrackingBreakpoints, stepSetProgramCounter, stepStopScript,
	 * stepResumeScript, stepUserDebugCommands, stepJTAGCleanup]
	 */

	/**
	 * Retrieve the IGDBJtagDevice instance
	 */
	@Override
    @Execute
	public void stepRetrieveJTAGDevice(final RequestMonitor rm) {
		try {
			//fGdbJtagDevice = getGDBJtagDevice();
			fGdbJtagDevice = new ArmJTagDevice();
			// fDebuggerCommands.setJtagDevice(fGdbJtagDevice);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		if (fGdbJtagDevice == null) {		
			/*
			 * when local instance jtag device failed, reinstance it in super class instance
			 * @2017.6.13. this is important bug fixed
			 * 
			 * @below is disabled, which is used before
			 * rm.setStatus(new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1, "Cannot get Jtag device", exception)); //$NON-NLS-1$
			 */
			super.stepRetrieveJTAGDevice(rm);
		} else {
			rm.done();
		}
	}

	// ------------------------------------------------------------------------

	@Override
    @Execute
	public void stepConnectToTarget(final RequestMonitor rm) {

		GdbActivator.log("stepConnectToTarget begin "); //$NON-NLS-1$
		List<String> commandsList = new ArrayList<>();

		IStatus status = fDebuggerCommands.addGnuArmSelectRemoteCommands(commandsList);
		if (!status.isOK()) {
			rm.setStatus(status);
			rm.done();
			return;
		}

		GdbActivator.log("stepConnectToTarget " + commandsList.toString()); //$NON-NLS-1$
		queueCommands(commandsList, rm);
	}

	/**
	 * Step gnu arm reset.
	 *
	 * @param rm the RequestMonitor
	 */
	@Execute
	public void stepGnuArmReset(RequestMonitor rm) {

		List<String> commandsList = new ArrayList<>();

		IStatus status = fDebuggerCommands.addGnuArmResetCommands(commandsList);
		if (!status.isOK()) {
			rm.setStatus(status);
			rm.done();
			return;
		}

		queueCommands(commandsList, rm);
	}

	/**
	 * Step gnu arm start.
	 *
	 * @param rm the rm
	 */
	@Execute
	public void stepGnuArmStart(RequestMonitor rm) {

		List<String> commandsList = new ArrayList<>();

		IStatus status = fDebuggerCommands.addGnuArmStartCommands(commandsList);
		if (!status.isOK()) {
			rm.setStatus(status);
			rm.done();
			return;
		}

		queueCommands(commandsList, rm);
	}

	@Override
    @Execute
	public void stepStartTrackingBreakpoints(final RequestMonitor rm) {
		if (fMode.equals(ILaunchManager.DEBUG_MODE)) {
			super.stepStartTrackingBreakpoints(rm);
		} else {
			rm.done();
		}
	}

}
