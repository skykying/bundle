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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import org.eclipse.cdt.debug.gdbjtag.core.IGDBJtagConstants;
import org.eclipse.cdt.dsf.concurrent.DefaultDsfExecutor;
import org.eclipse.cdt.dsf.concurrent.DsfRunnable;
import org.eclipse.cdt.dsf.concurrent.IDsfStatusConstants;
import org.eclipse.cdt.dsf.gdb.IGDBLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.gdb.IGdbDebugConstants;
import org.eclipse.cdt.dsf.gdb.internal.GdbPlugin;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.ISourceLocator;

import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmLaunch;
import com.lembed.lite.studio.debug.gdbjtag.llink.Configuration;
import com.lembed.lite.studio.debug.gdbjtag.llink.ConfigurationAttributes;
import com.lembed.lite.studio.debug.gdbjtag.llink.DefaultPreferences;
import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;

/**
 * The Class Launch.
 */
@SuppressWarnings("restriction")
public class Launch extends GnuArmLaunch {

	// ------------------------------------------------------------------------

	ILaunchConfiguration fConfig = null;
	private DsfSession fSession;
	private DsfServicesTracker fTracker;
	private DefaultDsfExecutor fExecutor;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new launch.
	 *
	 * @param launchConfiguration the launch configuration
	 * @param mode the mode
	 * @param locator the locator
	 */
	public Launch(ILaunchConfiguration launchConfiguration, String mode, ISourceLocator locator) {
		super(launchConfiguration, mode, locator);

		LlinkPlugin.log("Launch(" + launchConfiguration.getName() + "," + mode + ") " + this); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		fConfig = launchConfiguration;
		fExecutor = (DefaultDsfExecutor) getDsfExecutor();
		fSession = getSession();
	}

	// ------------------------------------------------------------------------

	@Override
	public void initialize() {

		LlinkPlugin.log("Launch.initialize() " + this); //$NON-NLS-1$

		super.initialize();

		Runnable initRunnable = new DsfRunnable() {
			@Override
			public void run() {
				fTracker = new DsfServicesTracker(GdbPlugin.getBundleContext(), fSession.getId());
				// fSession.addServiceEventListener(GdbLaunch.this, null);

				// fInitialized = true;
				// fireChanged();
			}
		};

		// Invoke the execution code and block waiting for the result.
		try {
			fExecutor.submit(initRunnable).get();
		} catch (InterruptedException e) {
			LlinkPlugin.log(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, IDsfStatusConstants.INTERNAL_ERROR,
					"Error initializing launch", e)); // $NON-NLS-1$ //$NON-NLS-1$
		} catch (ExecutionException e) {
			LlinkPlugin.log(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, IDsfStatusConstants.INTERNAL_ERROR,
					"Error initializing launch", e)); // $NON-NLS-1$ //$NON-NLS-1$
		}
	}

	@Override
	protected void provideDefaults(ILaunchConfigurationWorkingCopy config) throws CoreException {

		super.provideDefaults(config);

		if (!config.hasAttribute(IGDBJtagConstants.ATTR_IP_ADDRESS)) {
			config.setAttribute(IGDBJtagConstants.ATTR_IP_ADDRESS, "localhost"); //$NON-NLS-1$
		}

		if (!config.hasAttribute(IGDBJtagConstants.ATTR_JTAG_DEVICE)) {
			config.setAttribute(IGDBJtagConstants.ATTR_JTAG_DEVICE, ConfigurationAttributes.JTAG_DEVICE);
		}

		if (!config.hasAttribute(IGDBJtagConstants.ATTR_PORT_NUMBER)) {
			config.setAttribute(IGDBJtagConstants.ATTR_PORT_NUMBER,
					DefaultPreferences.GDB_SERVER_GDB_PORT_NUMBER_DEFAULT);
		}

		if (!config.hasAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME)) {
			config.setAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
					DefaultPreferences.stLinkGetGdbClientExecutablePath());
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Initialize server console.
	 *
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public void initializeServerConsole(IProgressMonitor monitor) throws CoreException {
		
		LlinkPlugin.log("Launch.initializeServerConsole()"); //$NON-NLS-1$

		IProcess newProcess;
		boolean doAddServerConsole = Configuration.getDoAddServerConsole(fConfig);

		if (doAddServerConsole) {

			// Add the GDB server process to the launch tree
			newProcess = addServerProcess(Configuration.getGdbServerCommandName(fConfig));
			newProcess.setAttribute(IProcess.ATTR_CMDLINE, Configuration.getGdbServerCommandLine(fConfig));

			monitor.worked(1);
		}
	}

	/**
	 * Initialize consoles.
	 *
	 * @param monitor the monitor
	 * @throws CoreException the core exception
	 */
	public void initializeConsoles(IProgressMonitor monitor) throws CoreException {

		LlinkPlugin.log("Launch.initializeConsoles()"); //$NON-NLS-1$
		IProcess newProcess;

		// Add the GDB client process to the launch tree.
		String gdbclient = Configuration.getGdbClientCommandName(fConfig);
		LlinkPlugin.log(gdbclient);
		
		newProcess = addClientProcess(gdbclient);
		String gdbcommand = Configuration.getGdbClientCommandLine(fConfig);
		LlinkPlugin.log(gdbcommand);
		
		newProcess.setAttribute(IProcess.ATTR_CMDLINE, gdbcommand);
		monitor.worked(1);

		boolean doAddSemihostingConsole = Configuration.getDoAddSemihostingConsole(fConfig);
		if (doAddSemihostingConsole) {
			LlinkPlugin.log("add semihosting monitor"); //$NON-NLS-1$
			// Add the special semihosting and SWV process to the launch tree
			newProcess = addSemihostingProcess("Semihosting Monitor"); //$NON-NLS-1$
			monitor.worked(1);
		}
		
		boolean doAddTraceConsole = Configuration.getDoEnableTrace(fConfig);
		if (doAddTraceConsole) {
			LlinkPlugin.log("add trace monitor"); //$NON-NLS-1$
			// Add the special trace process to the launch tree
			newProcess = addTraceProcess("Trace Monitor"); //$NON-NLS-1$
			monitor.worked(1);
		}
	}

	/**
	 * Adds the server process.
	 *
	 * @param label the label
	 * @return the i process
	 * @throws CoreException the core exception
	 */
	public IProcess addServerProcess(String label) throws CoreException {
		IProcess newProcess = null;
		try {
			// Add the server process object to the launch.
			Process serverProc = getDsfExecutor().submit(new Callable<Process>() {
				@Override
				public Process call() throws CoreException {
					GdbServerBackendWithTrace backend = fTracker.getService(GdbServerBackendWithTrace.class);
					if (backend != null) {
						return backend.getServerProcess();
					}
					return null;
				}
			}).get();

			// Need to go through DebugPlugin.newProcess so that we can use
			// the overrideable process factory to allow others to override.
			// First set attribute to specify we want to create the gdb process.
			// Bug 210366
			Map<String, String> attributes = new HashMap<>();
			if (true) {
				attributes.put(IGdbDebugConstants.PROCESS_TYPE_CREATION_ATTR,
						IGdbDebugConstants.GDB_PROCESS_CREATION_VALUE);
			}
			if (serverProc != null) {
				newProcess = DebugPlugin.newProcess(this, serverProc, label, attributes);
			}
		} catch (InterruptedException e) {
			throw new CoreException(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, 0,
					"Interrupted while waiting for get process callable.", e)); // $NON-NLS-1$ //$NON-NLS-1$
		} catch (ExecutionException e) {
			throw (CoreException) e.getCause();
		} catch (RejectedExecutionException e) {
			throw new CoreException(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, 0,
					"Debugger shut down before launch was completed.", e)); // $NON-NLS-1$ //$NON-NLS-1$
		}

		return newProcess;
	}

	/**
	 * Adds the semihosting process.
	 *
	 * @param label the label
	 * @return the i process
	 * @throws CoreException the core exception
	 */
	public IProcess addSemihostingProcess(String label) throws CoreException {
		IProcess newProcess = null;
		try {
			// Add the server process object to the launch.
			Process serverProc = getDsfExecutor().submit(new Callable<Process>() {
				@Override
				public Process call() throws CoreException {
					GdbServerBackendWithTrace backend = fTracker.getService(GdbServerBackendWithTrace.class);
					if (backend != null) {
						return backend.getSemihostingProcess();
					}
					return null;
				}
			}).get();

			// Need to go through DebugPlugin.newProcess so that we can use
			// the overrideable process factory to allow others to override.
			// First set attribute to specify we want to create the gdb process.
			// Bug 210366
			Map<String, String> attributes = new HashMap<>();

			// Not necessary, to simplify process factory
			// attributes.put(IGdbDebugConstants.PROCESS_TYPE_CREATION_ATTR,
			// IGdbDebugConstants.GDB_PROCESS_CREATION_VALUE);

			if (serverProc != null) {
				newProcess = DebugPlugin.newProcess(this, serverProc, label, attributes);
			}
		} catch (InterruptedException e) {
			throw new CoreException(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, 0,
					"Interrupted while waiting for get process callable.", e)); // $NON-NLS-1$ //$NON-NLS-1$
		} catch (ExecutionException e) {
			throw (CoreException) e.getCause();
		} catch (RejectedExecutionException e) {
			throw new CoreException(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, 0,
					"Debugger shut down before launch was completed.", e)); // $NON-NLS-1$ //$NON-NLS-1$
		}

		return newProcess;
	}
	
	/**
	 * Adds the trace process.
	 *
	 * @param label the label
	 * @return the i process
	 * @throws CoreException the core exception
	 */
	public IProcess addTraceProcess(String label) throws CoreException {
		IProcess newProcess = null;
		try {
			// Add the server process object to the launch.
			Process serverProc = getDsfExecutor().submit(new Callable<Process>() {
				@Override
				public Process call() throws CoreException {
					GdbServerBackendWithTrace backend = fTracker.getService(GdbServerBackendWithTrace.class);
					if (backend != null) {
						return backend.getTraceProcess();
					}
					return null;
				}
			}).get();

			// Need to go through DebugPlugin.newProcess so that we can use
			// the overrideable process factory to allow others to override.
			// First set attribute to specify we want to create the gdb process.
			// Bug 210366
			Map<String, String> attributes = new HashMap<>();

			// Not necessary, to simplify process factory
			// attributes.put(IGdbDebugConstants.PROCESS_TYPE_CREATION_ATTR,
			// IGdbDebugConstants.GDB_PROCESS_CREATION_VALUE);

			if (serverProc != null) {
				newProcess = DebugPlugin.newProcess(this, serverProc, label, attributes);
			}
		} catch (InterruptedException e) {
			throw new CoreException(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, 0,
					"Interrupted while waiting for get process callable.", e)); // $NON-NLS-1$ //$NON-NLS-1$
		} catch (ExecutionException e) {
			throw (CoreException) e.getCause();
		} catch (RejectedExecutionException e) {
			throw new CoreException(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, 0,
					"Debugger shut down before launch was completed.", e)); // $NON-NLS-1$ //$NON-NLS-1$
		}

		return newProcess;
	}
	
}
