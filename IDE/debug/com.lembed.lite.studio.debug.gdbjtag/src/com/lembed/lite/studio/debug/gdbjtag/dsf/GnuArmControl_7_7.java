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

import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitorWithProgress;
import org.eclipse.cdt.dsf.concurrent.Sequence;
import org.eclipse.cdt.dsf.debug.service.IBreakpoints.IBreakpointsTargetDMContext;
import org.eclipse.cdt.dsf.debug.service.command.ICommandControlService;
import org.eclipse.cdt.dsf.gdb.service.command.GDBControlDMContext;
import org.eclipse.cdt.dsf.mi.service.command.CLIEventProcessor_7_7;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.mi.service.command.IEventProcessor;
import org.eclipse.cdt.dsf.mi.service.command.output.MIInfo;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * The Class GnuArmControl_7_7.
 */
public class GnuArmControl_7_7 extends GnuArmControl_7_4  {

	// ------------------------------------------------------------------------

	private ILaunchConfiguration fConfig;

	/**
	 * A command control context that is also a IBreakpointsTargetDMContext
	 */
	private class GDBControlDMContext_7_4 extends GDBControlDMContext implements IBreakpointsTargetDMContext {
		public GDBControlDMContext_7_4(String sessionId, String commandControlId) {
			super(sessionId, commandControlId);
		}
	}

	/**
	 * Instantiates a new gnu arm control 7 7.
	 *
	 * @param session the session
	 * @param config the config
	 * @param factory the factory
	 * @param mode the mode
	 */
	public GnuArmControl_7_7(DsfSession session, ILaunchConfiguration config, CommandFactory factory, String mode) {
		super(session, config, factory, mode);

		fConfig = config;
	}

	// Required by ILaunchConfigurationProvider

	@Override
	public ILaunchConfiguration getLaunchConfiguration() {
		return fConfig;
	}

	/** @since 4.4 */
    @Override
    protected ICommandControlDMContext createComandControlContext() {
		return new GDBControlDMContext_7_4(getSession().getId(), getId());
    }
    
    @Override
	protected IEventProcessor createCLIEventProcessor(ICommandControlService connection, ICommandControlDMContext controlDmc) {
		return new CLIEventProcessor_7_7(connection, controlDmc);
    }

    /** @since 4.4 */
	@Override
	public void setPrintPythonErrors(boolean enabled, RequestMonitor rm) {
		// With GDB 7.4, the command 'maintenance set python print-stack' has
		// been replaced by the new command
		// "set python print-stack none|full|message".
		// Bug 367788
		String errorOption = enabled ? "full" : "none"; //$NON-NLS-1$ //$NON-NLS-2$
		queueCommand(getCommandFactory().createMIGDBSetPythonPrintStack(getContext(), errorOption),
				new DataRequestMonitor<MIInfo>(getExecutor(), rm));
	}

	@Override
	protected Sequence getCompleteInitializationSequence(Map<String, Object> attributes, RequestMonitorWithProgress rm) {
			return new GnuArmFinalLaunchSequence_7_7(getSession(), attributes, fMode, rm);
	}
}
