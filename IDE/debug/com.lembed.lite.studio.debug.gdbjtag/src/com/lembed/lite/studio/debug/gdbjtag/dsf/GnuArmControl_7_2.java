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

import java.util.Map;

import org.eclipse.cdt.dsf.concurrent.ImmediateRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitorWithProgress;
import org.eclipse.cdt.dsf.concurrent.Sequence;
import org.eclipse.cdt.dsf.datamodel.AbstractDMEvent;
import org.eclipse.cdt.dsf.gdb.service.command.GDBControl_7_2;
import org.eclipse.cdt.dsf.mi.service.IMIBackend;
import org.eclipse.cdt.dsf.mi.service.IMIBackend.BackendStateChangedEvent;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.service.DsfServiceEventHandler;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.ILaunchConfiguration;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.services.IGdbServerBackendService;
import com.lembed.lite.studio.debug.gdbjtag.services.IGdbServerBackendService.ServerBackendStateChangedEvent;

/**
 * The Class GnuArmControl_7_2.
 */
public class GnuArmControl_7_2 extends GDBControl_7_2 {

	// ========================================================================

	/**
	 * Event indicating that the CommandControl has terminated. There is not
	 * much custom functionality, the trick is to be a instance of
	 * ICommandControlShutdownDMEvent, this is used by GDBLaunch to hunt for
	 * this event.
	 */
	private static class GnuArmCommandControlShutdownDMEvent extends AbstractDMEvent<ICommandControlDMContext>
		implements ICommandControlShutdownDMEvent {

		public GnuArmCommandControlShutdownDMEvent(ICommandControlDMContext context) {
			super(context);
		}
	}

	// ------------------------------------------------------------------------

	private IGdbServerBackendService fServerBackend;
	
	/** The mode. */
	protected String fMode;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm control 7 2.
	 *
	 * @param session the session
	 * @param config the ILaunchConfiguration
	 * @param factory the CommandFactory
	 * @param mode the String mode
	 */
	public GnuArmControl_7_2(DsfSession session, ILaunchConfiguration config, CommandFactory factory, String mode) {
		super(session, config, factory);

		fMode = mode;
	}

	// ------------------------------------------------------------------------

	@Override
	public void initialize(final RequestMonitor rm) {
		super.initialize(new ImmediateRequestMonitor(rm) {
			@Override
			protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	private void doInitialize(final RequestMonitor rm) {

		fServerBackend = getServicesTracker().getService(IGdbServerBackendService.class);
		rm.done();
	}

	// ------------------------------------------------------------------------

	@Override
	protected Sequence getCompleteInitializationSequence(Map<String, Object> attributes,
	        RequestMonitorWithProgress rm) {
		return new GnuArmFinalLaunchSequence_7_2(getSession(), attributes, fMode, rm);
	}

	/**
	 * Be sure it is present and does nothing, otherwise the parent
	 * implementation will fire and dispatch the event.
	 */
	@Override
    @DsfServiceEventHandler
	public void eventDispatched(BackendStateChangedEvent e) {
		
	}

	/**
	 * Handle "GDB Exited" event, just relay to following event.
	 * @param e  ServerBackendStateChangedEvent
	 */
	@DsfServiceEventHandler
	public void eventDispatched(ServerBackendStateChangedEvent e) {

		GdbActivator.log("GnuArmControl_7_2.eventDispatched() " + e); //$NON-NLS-1$
		
		if (e.getState() == IMIBackend.State.TERMINATED && e.getSessionId().equals(getSession().getId())
		        && e.getBackendId().equals(fServerBackend.getId())) {

			// Will be captured by GdbLaunch (waiting for
			// ICommandControlShutdownDMEvent), which will trigger
			// sessionShutdown.
			getSession().dispatchEvent(new GnuArmCommandControlShutdownDMEvent(getContext()), getProperties());
		}
	}

}
