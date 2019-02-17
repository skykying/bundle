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

import com.lembed.lite.studio.debug.gdbjtag.DebugUtils;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmGdbBackend;
import com.lembed.lite.studio.debug.gdbjtag.llink.Configuration;
import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;

import java.io.File;
import java.io.IOException;

import org.eclipse.cdt.core.parser.util.StringUtil;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.utils.spawner.ProcessFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * The Oxygen CDT GDBBackend does not allow such a simple customization, we had
 * to copy a newer version locally and use it.
 */
public class GdbBackend extends GnuArmGdbBackend {

	// ------------------------------------------------------------------------

	private final ILaunchConfiguration fLaunchConfiguration;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gdb backend.
	 *
	 * @param session the session
	 * @param lc the lc
	 */
	public GdbBackend(DsfSession session, ILaunchConfiguration lc) {

		super(session, lc);

		LlinkPlugin.log("GdbBackend() " + this); //$NON-NLS-1$
		fLaunchConfiguration = lc;
	}

	// ------------------------------------------------------------------------

	@Override
	public void initialize(final RequestMonitor rm) {

		LlinkPlugin.log("GdbBackend.initialize() " + Thread.currentThread().getName()); //$NON-NLS-1$
		super.initialize(rm);
	}

	@Override
	public void destroy() {

		LlinkPlugin.log("GdbBackend.destroy() " + Thread.currentThread().getName()); //$NON-NLS-1$
		super.destroy();
	}

	@Override
	public void shutdown(final RequestMonitor rm) {

		LlinkPlugin.log("GdbBackend.shutdown() " + Thread.currentThread().getName()); //$NON-NLS-1$
		super.shutdown(rm);
	}

	// ------------------------------------------------------------------------

	/**
	 * Overridden to get the full command line, including all options, from the
	 * JLink configuration.
	 */
	@Override
    protected String[] getGDBCommandLineArray() {
		String[] commandLineArray = Configuration.getGdbClientCommandLineArray(fLaunchConfiguration);
		return commandLineArray;
	}

	/**
	 * Overridden to use our own launch environment and to add the working
	 * directory to exec(), although it is anyway set in a separate step
	 * (stepSetEnvironmentDirector in FinalLaunchSequence).
	 */
	@Override
	protected Process launchGDBProcess(String[] commandLine) throws CoreException {
		Process proc = null;
		File dir = null;
		IPath path = getGDBWorkingDirectory();
		if (path != null) {
			dir = new File(path.toOSString());
		}

		try {
			proc = ProcessFactory.getFactory().exec(commandLine, DebugUtils.getLaunchEnvironment(fLaunchConfiguration),
					dir);
		} catch (IOException e) {
			String message = "Error while launching command: " + StringUtil.join(commandLine, " "); //$NON-NLS-1$ //$NON-NLS-2$
			throw new CoreException(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, -1, message, e));
		}

		return proc;
	}

	/**
	 * Overridden to also try getProjectOsPath(), if getGDBWorkingDirectory() is
	 * not defined.
	 * 
	 * May return null.
	 */
	@Override
	public IPath getGDBWorkingDirectory() throws CoreException {

		IPath path;
		try {
			path = super.getGDBWorkingDirectory();
		} catch (CoreException e) {
			path = null;
		}

		if (path == null) {
			path = DebugUtils.getProjectOsPath(fLaunchConfiguration);
		}

		LlinkPlugin.log("getGDBWorkingDirectory() " + path); //$NON-NLS-1$
		return path;
	}

	// ------------------------------------------------------------------------
}
