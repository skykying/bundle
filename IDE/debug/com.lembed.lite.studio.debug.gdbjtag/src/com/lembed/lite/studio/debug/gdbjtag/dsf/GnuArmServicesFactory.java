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

import org.eclipse.cdt.dsf.debug.service.IProcesses;
import org.eclipse.cdt.dsf.debug.service.command.ICommandControl;
import org.eclipse.cdt.dsf.gdb.service.GdbDebugServicesFactory;
// import org.eclipse.cdt.dsf.gdb.service.command.GDBControl_7_7;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.ILaunchConfiguration;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.services.IGdbServerBackendService;
import com.lembed.lite.studio.debug.gdbjtag.services.IGnuArmDebuggerCommandsService;
import com.lembed.lite.studio.debug.gdbjtag.services.IPeripheralMemoryService;
import com.lembed.lite.studio.debug.gdbjtag.services.IPeripheralsService;
import com.lembed.lite.studio.debug.gdbjtag.services.PeripheralMemoryService;
import com.lembed.lite.studio.debug.gdbjtag.services.PeripheralsService;

/**
 * Services factory intended to create the peripherals service.
 * <p>
 * To be used as parent class by actual implementations (J-Link and OpenOCD
 * factories).
 */
public abstract class GnuArmServicesFactory extends GdbDebugServicesFactory {

	// ------------------------------------------------------------------------

	/** The version. */
    //private final String fVersion; //7.10.1.20160923
	
	/** The mode. */
	private String fMode;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm services factory.
	 *
	 * @param version the version
	 * @param mode the mode
	 */
	public GnuArmServicesFactory(String version, String mode) {
		super(version, null);

		//fVersion = version;
		fMode = mode;
	}


	@Override
    @SuppressWarnings("unchecked")
	public <V> V createService(Class<V> clazz, DsfSession session, Object... optionalArguments) {

		if (IPeripheralsService.class.isAssignableFrom(clazz)) {
			return (V) createPeripheralsService(session);
		} else if (IPeripheralMemoryService.class.isAssignableFrom(clazz)) {
			for (Object arg : optionalArguments) {
				if (arg instanceof ILaunchConfiguration) {
					return (V) createPeripheralMemoryService(session, (ILaunchConfiguration) arg);
				}
			}
		} else if (IGnuArmDebuggerCommandsService.class.isAssignableFrom(clazz)) {
			for (Object arg : optionalArguments) {
				if (arg instanceof ILaunchConfiguration) {
					return (V) createDebuggerCommandsService(session, (ILaunchConfiguration) arg, fMode);
				}
			}
		} else if (IGdbServerBackendService.class.isAssignableFrom(clazz)) {
			for (Object arg : optionalArguments) {
				if (arg instanceof ILaunchConfiguration) {
					return (V) createGdbServerBackendService(session, (ILaunchConfiguration) arg);
				}
			}
		}
		return super.createService(clazz, session, optionalArguments);
	}

	// ------------------------------------------------------------------------

	/**
	 * Creates a new GnuArmServices object.
	 *
	 * @param session the session
	 * @param lc the lc
	 * @param mode the mode
	 * @return the gnu arm debugger commands service
	 */
	protected abstract GnuArmDebuggerCommandsService createDebuggerCommandsService(DsfSession session,
	        ILaunchConfiguration lc, String mode);

	/**
	 * Creates a new GnuArmServices object.
	 *
	 * @param session the session
	 * @param lc the ILaunchConfiguration
	 * @return the gnu arm gdb server backend
	 */
	protected abstract GnuArmGdbServerBackend createGdbServerBackendService(DsfSession session,
	        ILaunchConfiguration lc);

	// ------------------------------------------------------------------------

	private static PeripheralsService createPeripheralsService(DsfSession session) {
		return new PeripheralsService(session);
	}

	private static PeripheralMemoryService createPeripheralMemoryService(DsfSession session,
	        ILaunchConfiguration launchConfiguration) {
		return new PeripheralMemoryService(session, launchConfiguration);
	}

	@Override
	protected ICommandControl createCommandControl(DsfSession session, ILaunchConfiguration config) {

		
		GdbActivator.log("GnuArmServicesFactory.createCommandControl(" + session + "," + config.getName() + ") " + this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		

//		if (compareVersionWith(GDB_7_12_VERSION) >= 0) {
//			return new GnuArmControl_7_12(session, config, new GnuArmCommandFactory(), fMode);
//		}
//
//		if (compareVersionWith(GDB_7_7_VERSION) >= 0) {
//			return new GnuArmControl_7_7(session, config, new GnuArmCommandFactory(), fMode);
//		}

		if (compareVersionWith(GDB_7_4_VERSION) >= 0) {
			return new GnuArmControl_7_4(session, config, new GnuArmCommandFactory(), fMode);
		}

		return super.createCommandControl(session, config);
	}

	@Override
	protected IProcesses createProcessesService(DsfSession session) {

		
		GdbActivator.log("GnuArmServicesFactory.createProcessesService(" + session + ") " + this); //$NON-NLS-1$ //$NON-NLS-2$

//		if (compareVersionWith(GDB_7_12_VERSION) >= 0) {
//			return new GDBProcesses_7_12(session);
//		}
//		if (compareVersionWith(GDB_7_10_VERSION) >= 0) {
//			return new GnuArmProcesses_7_10(session);
//		}
//		if (compareVersionWith(GDB_7_4_VERSION) >= 0) {
//			return new GDBProcesses_7_4(session);
//		}
//		if (compareVersionWith(GDB_7_3_VERSION) >= 0) {
//			return new GDBProcesses_7_3(session);
//		}

		if (compareVersionWith(GDB_7_2_1_VERSION) >= 0) {
			return new GnuArmProcesses_7_2_1(session);
		}

		return super.createProcessesService(session);
	}

}
