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
package com.lembed.lite.studio.debug.gdbjtag.llink;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.StringUtils;
import com.lembed.lite.studio.debug.gdbjtag.DebugUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.dsf.gdb.IGDBLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.gdb.IGdbDebugPreferenceConstants;
import org.eclipse.cdt.dsf.gdb.internal.GdbPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * The Class Configuration.
 */
@SuppressWarnings("restriction")
public class Configuration {

	// ------------------------------------------------------------------------

	/**
	 * Gets the gdb server command.
	 *
	 * @param configuration the configuration
	 * @return the gdb server command
	 */
	public static String getGdbServerCommand(ILaunchConfiguration configuration) {

		String executable = null;

		try {
			if (!configuration.getAttribute(ConfigurationAttributes.DO_START_GDB_SERVER,
			                                DefaultPreferences.getGdbServerDoStart())) {
				return null;
			}

			executable = configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_EXECUTABLE,
			                                        DefaultPreferences.stlinkGetGdbServerExecutablePath());
			
			// executable = Utils.escapeWhitespaces(executable).trim();
			executable = executable.trim();
			if (executable.length() == 0) {
				return null;
			}
			
			executable = DebugUtils.resolveAll(executable, configuration.getAttributes());

			ICConfigurationDescription buildConfig = EclipseUtils.getBuildConfigDescription(configuration);
			if (buildConfig != null) {
				executable = DebugUtils.resolveAll(executable, buildConfig);
			}

		} catch (CoreException e) {
			LlinkPlugin.log(e);
			return null;
		}

		return executable;
	}

	/**
	 * Gets the gdb server command line.
	 *
	 * @param configuration the configuration
	 * @return the gdb server command line
	 */
	public static String getGdbServerCommandLine(ILaunchConfiguration configuration) {

		String cmdLineArray[] = getGdbServerCommandLineArray(configuration);

		return StringUtils.join(cmdLineArray, " "); //$NON-NLS-1$
	}

	/**
	 * Gets the gdb server command line array.
	 *
	 * @param configuration the configuration
	 * @return the gdb server command line array
	 */
	public static String[] getGdbServerCommandLineArray(ILaunchConfiguration configuration) {

		List<String> lst = new ArrayList<>();

		try {
			if (!configuration.getAttribute(ConfigurationAttributes.DO_START_GDB_SERVER,
			                                DefaultPreferences.getGdbServerDoStart())) {
				return null;
			}

			String executable = getGdbServerCommand(configuration);
			if (executable == null || executable.length() == 0) {
				LlinkPlugin.log("gdb server path not found !"); //$NON-NLS-1$
				return null;
			}

			lst.add(executable);

			String connection = configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_CONNECTION,
			                    DefaultPreferences.getGdbServerConnection());
			String connectionAddress = configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_CONNECTION_ADDRESS,
			                           DefaultPreferences.getGdbServerConnectionAddress());

			connectionAddress = DebugUtils.resolveAll(connectionAddress, configuration.getAttributes());
			if (connectionAddress.length() > 0) {
				if (DefaultPreferences.GDB_SERVER_CONNECTION_USB.equals(connection)) {
					lst.add("-select"); //$NON-NLS-1$
					lst.add("usb=" + connectionAddress); //$NON-NLS-1$
				} else if (DefaultPreferences.GDB_SERVER_CONNECTION_IP.equals(connection)) {
					lst.add("-select"); //$NON-NLS-1$
					lst.add("ip=" + connectionAddress); //$NON-NLS-1$
				}
			}

			lst.add("-if"); //$NON-NLS-1$
			lst.add(configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_DEBUG_INTERFACE,
			                                   DefaultPreferences.getGdbServerInterface()));
			String defaultName = configuration.getAttribute(ConfigurationAttributes.FLASH_DEVICE_NAME_COMPAT,
			                     DefaultPreferences.FLASH_DEVICE_NAME_DEFAULT);

			String name = configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_DEVICE_NAME, defaultName)
			              .trim();
			name = DebugUtils.resolveAll(name, configuration.getAttributes());
			if (name.length() > 0) {
				lst.add("-device"); //$NON-NLS-1$
				lst.add(name);
			}

			lst.add("-endian"); //$NON-NLS-1$
			lst.add(configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_DEVICE_ENDIANNESS,
			                                   DefaultPreferences.getGdbServerEndianness()));

			lst.add("-speed"); //$NON-NLS-1$
			lst.add(configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_DEVICE_SPEED,
			                                   DefaultPreferences.getGdbServerInitialSpeed()));

			lst.add("-port"); //$NON-NLS-1$
			lst.add(Integer.toString(configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_GDB_PORT_NUMBER,
			                         DefaultPreferences.GDB_SERVER_GDB_PORT_NUMBER_DEFAULT)));

			lst.add("-swoport"); //$NON-NLS-1$
			lst.add(Integer.toString(configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_SWO_PORT_NUMBER,
			                         DefaultPreferences.GDB_SERVER_SWO_PORT_NUMBER_DEFAULT)));

			lst.add("-telnetport"); //$NON-NLS-1$
			lst.add(Integer.toString(configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_TELNET_PORT_NUMBER,
			                         DefaultPreferences.GDB_SERVER_TELNET_PORT_NUMBER_DEFAULT)));

			if (configuration.getAttribute(ConfigurationAttributes.DO_GDB_SERVER_VERIFY_DOWNLOAD,
			                               DefaultPreferences.DO_GDB_SERVER_VERIFY_DOWNLOAD_DEFAULT)) {
				lst.add("-vd"); //$NON-NLS-1$
			}

			if (configuration.getAttribute(ConfigurationAttributes.DO_CONNECT_TO_RUNNING,
			                               DefaultPreferences.DO_CONNECT_TO_RUNNING_DEFAULT)) {
				lst.add("-noreset"); //$NON-NLS-1$
				lst.add("-noir"); //$NON-NLS-1$
			} else {
				if (configuration.getAttribute(ConfigurationAttributes.DO_GDB_SERVER_INIT_REGS,
				                               DefaultPreferences.DO_GDB_SERVER_INIT_REGS_DEFAULT)) {
					lst.add("-ir"); //$NON-NLS-1$
				} else {
					lst.add("-noir"); //$NON-NLS-1$
				}
			}

			lst.add("-localhostonly"); //$NON-NLS-1$
			if (configuration.getAttribute(ConfigurationAttributes.DO_GDB_SERVER_LOCAL_ONLY,
			                               DefaultPreferences.DO_GDB_SERVER_LOCAL_ONLY_DEFAULT)) {
				lst.add("1"); //$NON-NLS-1$
			} else {
				lst.add("0"); //$NON-NLS-1$
			}

			if (configuration.getAttribute(ConfigurationAttributes.DO_GDB_SERVER_ENABLE_TRACE,
			                               DefaultPreferences.DO_GDB_SERVER_ENABLE_TRACE_DEFAULT)) {
				lst.add("-silent"); //$NON-NLS-1$
			}

			String logFile = configuration
			                 .getAttribute(ConfigurationAttributes.GDB_SERVER_LOG, DefaultPreferences.GDB_SERVER_LOG_DEFAULT)
			                 .trim();

			logFile = DebugUtils.resolveAll(logFile, configuration.getAttributes());
			if (logFile.length() > 0) {
				lst.add("-log"); //$NON-NLS-1$

				// lst.add(Utils.escapeWhitespaces(logFile));
				lst.add(logFile);
			}

			String other = configuration.getAttribute(ConfigurationAttributes.GDB_SERVER_OTHER,
			               DefaultPreferences.getGdbServerOtherOptions()).trim();
			other = DebugUtils.resolveAll(other, configuration.getAttributes());
			if (other.length() > 0) {
				lst.addAll(StringUtils.splitCommandLineOptions(other));
			}

			// @2017.6.11
			// remove all options for gdb server,
			lst.clear();
			lst.add(executable);

		} catch (CoreException e) {
			LlinkPlugin.log(e);
			return null;
		}

		return lst.toArray(new String[0]);
	}

	/**
	 * Gets the gdb server command name.
	 *
	 * @param config the config
	 * @return the gdb server command name
	 */
	public static String getGdbServerCommandName(ILaunchConfiguration config) {

		String fullCommand = getGdbServerCommand(config);
		return StringUtils.extractNameFromPath(fullCommand);
	}

	/**
	 * Gets the gdb server device name.
	 *
	 * @param config the config
	 * @return the gdb server device name
	 * @throws CoreException the core exception
	 */
	public static String getGdbServerDeviceName(ILaunchConfiguration config) throws CoreException {

		return config.getAttribute(ConfigurationAttributes.GDB_SERVER_DEVICE_NAME,
		                           DefaultPreferences.FLASH_DEVICE_NAME_DEFAULT).trim();
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the gdb client command.
	 *
	 * @param configuration the configuration
	 * @return the gdb client command
	 */
	public static String getGdbClientCommand(ILaunchConfiguration configuration) {

		String executable = null;
		try {
			String defaultGdbCommand = Platform.getPreferencesService().getString(GdbPlugin.PLUGIN_ID,
			                           IGdbDebugPreferenceConstants.PREF_DEFAULT_GDB_COMMAND,
			                           IGDBLaunchConfigurationConstants.DEBUGGER_DEBUG_NAME_DEFAULT, null);

			executable = configuration.getAttribute(IGDBLaunchConfigurationConstants.ATTR_DEBUG_NAME,
			                                        defaultGdbCommand);
			
			executable = DebugUtils.resolveAll(executable, configuration.getAttributes());

			ICConfigurationDescription buildConfig = EclipseUtils.getBuildConfigDescription(configuration);
			if (buildConfig != null) {
				executable = DebugUtils.resolveAll(executable, buildConfig);
			}

		} catch (CoreException e) {
			LlinkPlugin.log(e);
			return null;
		}

		return executable;
	}

	/**
	 * Gets the gdb client command line array.
	 *
	 * @param configuration the configuration
	 * @return the gdb client command line array
	 */
	public static String[] getGdbClientCommandLineArray(ILaunchConfiguration configuration) {

		List<String> lst = new ArrayList<>();

		String executable = getGdbClientCommand(configuration);
		if (executable == null || executable.length() == 0) {
			LlinkPlugin.log("gdb client can not found !"); //$NON-NLS-1$
			return null;
		}

		lst.add(executable);

		// We currently work with MI version 2. Don't use just 'mi' because
		// it points to the latest MI version, while we want mi2 specifically.
		lst.add("--interpreter=mi2"); //$NON-NLS-1$

		// Don't read the gdbinit file here. It is read explicitly in
		// the LaunchSequence to make it easier to customise.
		lst.add("--nx"); //$NON-NLS-1$

		String other;
		try {
			other = configuration.getAttribute(ConfigurationAttributes.GDB_CLIENT_OTHER_OPTIONS,
			                                   DefaultPreferences.getGdbClientOtherOptions()).trim();
			other = DebugUtils.resolveAll(other, configuration.getAttributes());
			if (other.length() > 0) {
				lst.addAll(StringUtils.splitCommandLineOptions(other));
			}
		} catch (CoreException e) {
			LlinkPlugin.log(e);
		}

		return lst.toArray(new String[0]);
	}

	/**
	 * Gets the gdb client command line.
	 *
	 * @param configuration the configuration
	 * @return the gdb client command line
	 */
	public static String getGdbClientCommandLine(ILaunchConfiguration configuration) {

		String cmdLineArray[] = getGdbClientCommandLineArray(configuration);
		return StringUtils.join(cmdLineArray, " "); //$NON-NLS-1$
	}

	/**
	 * Gets the gdb client command name.
	 *
	 * @param config the config
	 * @return the gdb client command name
	 */
	public static String getGdbClientCommandName(ILaunchConfiguration config) {

		String fullCommand = getGdbClientCommand(config);
		return StringUtils.extractNameFromPath(fullCommand);
	}

	/**
	 * Gets the do start gdb server.
	 *
	 * @param config the config
	 * @return the do start gdb server
	 * @throws CoreException the core exception
	 */
	public static boolean getDoStartGdbServer(ILaunchConfiguration config) throws CoreException {

		return config.getAttribute(ConfigurationAttributes.DO_START_GDB_SERVER,
		                           DefaultPreferences.getGdbServerDoStart());
	}

	/**
	 * Gets the do add server console.
	 *
	 * @param config the config
	 * @return the do add server console
	 * @throws CoreException the core exception
	 */
	public static boolean getDoAddServerConsole(ILaunchConfiguration config) throws CoreException {

		return getDoStartGdbServer(config)
		       && config.getAttribute(ConfigurationAttributes.DO_GDB_SERVER_ALLOCATE_CONSOLE,
		                              DefaultPreferences.DO_GDB_SERVER_ALLOCATE_CONSOLE_DEFAULT);
	} 

	/**
	 * Gets the do add semihosting console.
	 *
	 * @param config the config
	 * @return the do add semihosting console
	 * @throws CoreException the core exception
	 */
	public static boolean getDoAddSemihostingConsole(ILaunchConfiguration config) throws CoreException {

		return getDoStartGdbServer(config)
		       && config.getAttribute(ConfigurationAttributes.DO_GDB_SERVER_ALLOCATE_SEMIHOSTING_CONSOLE,
		                              DefaultPreferences.DO_GDB_SERVER_ALLOCATE_SEMIHOSTING_CONSOLE_DEFAULT);
	}

	/**
	 * Gets the do enable trace.
	 *
	 * @param config the config
	 * @return the do enable trace
	 * @throws CoreException the core exception
	 */
	public static boolean getDoEnableTrace(ILaunchConfiguration config) throws CoreException {
		return config.getAttribute(ConfigurationAttributes.DO_GDB_SERVER_ENABLE_TRACE, DefaultPreferences.DO_GDB_SERVER_ENABLE_TRACE_DEFAULT);
	}
}
