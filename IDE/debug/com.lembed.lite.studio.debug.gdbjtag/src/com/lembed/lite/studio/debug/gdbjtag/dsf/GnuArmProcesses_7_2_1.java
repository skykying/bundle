/*******************************************************************************
 * Copyright (c) 2011 Ericsson and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marc Khouzam (Ericsson) - initial API and implementation
 *     Liviu Ionescu - ARM version
 *******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.dsf;

import java.util.Map;

import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.DsfExecutor;
import org.eclipse.cdt.dsf.concurrent.ImmediateDataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.ImmediateRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.Immutable;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.Sequence;
import org.eclipse.cdt.dsf.datamodel.IDMContext;
import org.eclipse.cdt.dsf.debug.service.IRunControl.IContainerDMContext;
import org.eclipse.cdt.dsf.gdb.service.GDBProcesses_7_2_1;
import org.eclipse.cdt.dsf.gdb.service.IGDBBackend;
import org.eclipse.cdt.dsf.gdb.service.command.IGDBControl;
import org.eclipse.cdt.dsf.mi.service.IMIBackend.State;
import org.eclipse.cdt.dsf.mi.service.IMICommandControl;
import org.eclipse.cdt.dsf.mi.service.IMIContainerDMContext;
import org.eclipse.cdt.dsf.mi.service.IMIProcessDMContext;
import org.eclipse.cdt.dsf.mi.service.IMIRunControl;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.mi.service.command.output.MIInfo;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;

/**
 *  Used to redefine the ProcessSequence, where the reset happens
 * @author Keven
 *
 */
public class GnuArmProcesses_7_2_1 extends GDBProcesses_7_2_1 {

	// ========================================================================

	/**
	 * Event indicating that the server back end process has started or
	 * terminated.
	 */
	@Immutable
	public static class ProcessStateChangedEvent {

		// --------------------------------------------------------------------

		final private String fSessionId;
		final private State fState;

		// --------------------------------------------------------------------

		/**
		 * Instantiates a new process state changed event.
		 *
		 * @param sessionId the session id
		 * @param state the state
		 */
		public ProcessStateChangedEvent(String sessionId, State state) {
			fSessionId = sessionId;
			fState = state;
		}

		// --------------------------------------------------------------------

		/**
		 * Gets the session id.
		 *
		 * @return the session id
		 */
		public String getSessionId() {
			return fSessionId;
		}

		/**
		 * Gets the state.
		 *
		 * @return the state
		 */
		public State getState() {
			return fState;
		}

		// --------------------------------------------------------------------
	}

	// ------------------------------------------------------------------------

	private IGDBControl fCommandControl;
	private IGDBBackend fBackend;
	private CommandFactory fCommandFactory;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm processes 7 2 1.
	 *
	 * @param session the session
	 */
	public GnuArmProcesses_7_2_1(DsfSession session) {
		super(session);
	}

	// ------------------------------------------------------------------------

	@Override
	public void initialize(final RequestMonitor rm) {

		GdbActivator.log("GnuArmProcesses_7_2_1.initialize()"); //$NON-NLS-1$
		
		super.initialize(new ImmediateRequestMonitor(rm) {
			@Override
			protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	private void doInitialize(RequestMonitor rm) {

		fCommandControl = getServicesTracker().getService(IGDBControl.class);
		fBackend = getServicesTracker().getService(IGDBBackend.class);
		IMICommandControl miCommandCtl = getServicesTracker().getService(IMICommandControl.class);
		if(miCommandCtl != null) {
		    fCommandFactory = miCommandCtl.getCommandFactory();
		}
		rm.done();

		
		GdbActivator.log("GnuArmProcesses_7_2_1.initialize() done"); //$NON-NLS-1$
	}

	// ------------------------------------------------------------------------

	@Override
	protected Sequence getStartOrRestartProcessSequence(DsfExecutor executor, IContainerDMContext containerDmc,
	        Map<String, Object> attributes, boolean restart, DataRequestMonitor<IContainerDMContext> rm) {

		if (restart) {
			return new GnuArmRestartProcessSequence(executor, containerDmc, attributes, restart, rm);
		}

		return super.getStartOrRestartProcessSequence(executor, containerDmc, attributes, restart, rm);
	}

	@Override
	public void canDetachDebuggerFromProcess(IDMContext dmc, DataRequestMonitor<Boolean> rm) {
		rm.setData(false);
		rm.done();
	}

	/**
	 * Process termination.
	 */
	@Override
	public void terminate(IThreadDMContext thread, final RequestMonitor rm) {

		GdbActivator.log("GnuArmProcesses_7_2_1.terminate()"); //$NON-NLS-1$
		
		// For a core session, there is no concept of killing the inferior,
		// so lets kill GDB
		if (thread instanceof IMIProcessDMContext) {
			getDebuggingContext(thread, new ImmediateDataRequestMonitor<IDMContext>(rm) {
				@Override
				protected void handleSuccess() {
					if (getData() instanceof IMIContainerDMContext) {

						IMIRunControl runControl = getServicesTracker().getService(IMIRunControl.class);
                        if (runControl != null && !runControl.isTargetAcceptingCommands()) {
                        	
                        	GdbActivator.log("GnuArmProcesses_7_2_1.terminate() interrupt"); //$NON-NLS-1$
                        	fBackend.interrupt();
                        }

                        // Does nothing on terminate, just exit.
                        fCommandControl.queueCommand(
                            fCommandFactory.createMIInterpreterExecConsoleKill((IMIContainerDMContext) getData()),
                            
                        new ImmediateDataRequestMonitor<MIInfo>(rm) {
                        	@Override
                        	protected void handleSuccess() {
                        		
                        		GdbActivator.log("GnuArmProcesses_7_2_1.terminate() dispatchEvent(ProcessStateChangedEvent, TERMINATED)"); //$NON-NLS-1$
                        		

                        		getSession().dispatchEvent(new ProcessStateChangedEvent(
                        		                               getSession().getId(), State.TERMINATED), getProperties());

                        		GdbActivator.log("GnuArmProcesses_7_2_1.terminate() done"); //$NON-NLS-1$
                        		
                        		rm.done();
                        	}
                        });
					} else {
						rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, INTERNAL_ERROR,
						                        "Invalid process context.", null)); //$NON-NLS-1$
						rm.done();
					}
				}
			});
		} else {
			super.terminate(thread, rm);
		}
	}
	// ------------------------------------------------------------------------
}
