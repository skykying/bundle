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

import org.eclipse.cdt.core.templateengine.SharedDefaults;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;

/**
 * Manage a workspace preference file stored in:
 * 
 * <pre>
 * workspace/.metadata/.plugins/org.eclipse.core.runtime/.settings/<plug-in-id>.prefs
 * </pre>
 *
 * Some of the values may be retrieved from the EclipseDefaults.
 */
@SuppressWarnings("javadoc")
public class PersistentPreferences {
	
	
    public static final String DEFAULT_GDB_COMMAND = "arm-none-eabi-gdb"; //$NON-NLS-1$

	// Tab Debugger
	// GDB Server Setup
	public static final String FLASH_DEVICE_NAME = "flashDeviceName";//$NON-NLS-1$

	public static final String GDB_SERVER = "gdb.server.";//$NON-NLS-1$

	public static final String GDB_SERVER_DO_START = GDB_SERVER + "doStart";//$NON-NLS-1$

	public static final String GDB_SERVER_EXECUTABLE = GDB_SERVER + "executable";//$NON-NLS-1$

	public static final String GDB_SERVER_ENDIANNESS = GDB_SERVER + "endianness";//$NON-NLS-1$

	public static final String GDB_SERVER_CONNECTION = GDB_SERVER + "connection";//$NON-NLS-1$
	public static final String GDB_SERVER_CONNECTION_ADDRESS = GDB_SERVER + "connection.address";//$NON-NLS-1$

	public static final String GDB_SERVER_INTERFACE = GDB_SERVER + "interface";//$NON-NLS-1$

	public static final String GDB_SERVER_INITIAL_SPEED = GDB_SERVER + "speed";//$NON-NLS-1$

	public static final String GDB_SERVER_OTHER_OPTIONS = GDB_SERVER + "other";//$NON-NLS-1$

	// GDB Client Setup
	public static final String GDB_CLIENT = "gdb.client.";//$NON-NLS-1$

	public static final String GDB_CLIENT_EXECUTABLE = GDB_CLIENT + "executable";//$NON-NLS-1$

	public static final String GDB_CLIENT_OTHER_OPTIONS = GDB_CLIENT + "other";//$NON-NLS-1$

	public static final String GDB_CLIENT_COMMANDS = GDB_CLIENT + "commands";//$NON-NLS-1$

	// Tab Startup
	// Initialisation Commands
	public static final String GDB_STLINK = "gdb.stlink.";//$NON-NLS-1$

	public static final String GDB_STLINK_DO_INITIAL_RESET = GDB_STLINK + "doInitialReset";//$NON-NLS-1$
	public static final String GDB_STLINK_INITIAL_RESET_TYPE = GDB_STLINK + "initialReset.type";//$NON-NLS-1$
	public static final String GDB_STLINK_INITIAL_RESET_SPEED = GDB_STLINK + "initialReset.speed";//$NON-NLS-1$

	public static final String GDB_STLINK_SPEED = GDB_STLINK + "speed";//$NON-NLS-1$

	public static final String GDB_STLINK_ENABLE_FLASH_BREAKPOINTS = GDB_STLINK + "enableFlashBreakpoints";//$NON-NLS-1$
	public static final String GDB_STLINK_ENABLE_SEMIHOSTING = GDB_STLINK + "enableSemihosting";//$NON-NLS-1$
	public static final String GDB_STLINK_SEMIHOSTING_TELNET = GDB_STLINK + "semihosting.telnet";//$NON-NLS-1$
	public static final String GDB_STLINK_SEMIHOSTING_CLIENT = GDB_STLINK + "semihosting.client";//$NON-NLS-1$

	public static final String GDB_STLINK_ENABLE_SWO = GDB_STLINK + "enableSwo";//$NON-NLS-1$

	public static final String GDB_STLINK_SWO_ENABLE_TARGET_CPU_FREQ = GDB_STLINK + "swoEnableTarget.cpuFreq";//$NON-NLS-1$
	public static final String GDB_STLINK_SWO_ENABLE_TARGET_SWO_FREQ = GDB_STLINK + "swoEnableTarget.swoFreq";//$NON-NLS-1$
	public static final String GDB_STLINK_SWO_ENABLE_TARGET_PORT_MASK = GDB_STLINK + "swoEnableTarget.portMask";//$NON-NLS-1$

	public static final String GDB_STLINK_INIT_OTHER = GDB_STLINK + "init.other";//$NON-NLS-1$

	// Run Commands
	public static final String GDB_STLINK_DO_DEBUG_IN_RAM = GDB_STLINK + "doDebugInRam";//$NON-NLS-1$

	public static final String GDB_STLINK_DO_PRERUN_RESET = GDB_STLINK + "doPreRunReset";//$NON-NLS-1$
	public static final String GDB_STLINK_PRERUN_RESET_TYPE = GDB_STLINK + "preRunReset.type";//$NON-NLS-1$

	public static final String GDB_STLINK_PRERUN_OTHER = GDB_STLINK + "preRun.other";//$NON-NLS-1$

	// ----- Defaults ---------------------------------------------------------

	public static final String EXECUTABLE_NAME = "executable.name";//$NON-NLS-1$
	public static final String EXECUTABLE_NAME_OS = EXECUTABLE_NAME + ".%s";//$NON-NLS-1$
	public static final String INSTALL_FOLDER = "install.folder";//$NON-NLS-1$
	public static final String SEARCH_PATH = "search.path";//$NON-NLS-1$
	public static final String SEARCH_PATH_OS = SEARCH_PATH + ".%s";//$NON-NLS-1$

	public static final String FOLDER_STRICT = "folder.strict";//$NON-NLS-1$

	public static final String TAB_MAIN_CHECK_PROGRAM = "tab.main.checkProgram";//$NON-NLS-1$

	// TODO: remove DEPRECATED
	public static final String STLINK_GDBSERVER_DEPRECATED = "stlink_gdbserver";//$NON-NLS-1$
	public static final String STLINK_PATH_DEPRECATED = "stlink_path";//$NON-NLS-1$

	// ----- Getters ----------------------------------------------------------

	private static String getString(String id, String defaultValue) {

		String value, pid =id;
		value = Platform.getPreferencesService().getString(DevicePlugin.PLUGIN_ID, id, null, null);
		// System.out.println("Value of " + id + " is " + value);

		if (value != null) {
			return value;
		}

		// For compatibility reasons, still keep this for a while, on older
		// versions preferences were erroneously saved in the shared defaults.
		pid = DevicePlugin.PLUGIN_ID + "." + pid; //$NON-NLS-1$

		value = SharedDefaults.getInstance().getSharedDefaultsMap().get(pid);

		if (value == null)
			value = ""; //$NON-NLS-1$

		value = value.trim();
		if (!value.isEmpty()) {
			return value;
		}

		return defaultValue;
	}

	// ----- Setters ----------------------------------------------------------

	private static void putWorkspaceString(String id, String value) {

		String tvalue = value.trim();

		// Access the instanceScope
		Preferences preferences = InstanceScope.INSTANCE.getNode(DevicePlugin.PLUGIN_ID);
		preferences.put(id, tvalue);
	}

	public static void flush() {

		try {
			InstanceScope.INSTANCE.getNode(DevicePlugin.PLUGIN_ID).flush();
		} catch (BackingStoreException e) {
			DevicePlugin.log(e);
		}
	}

	// ----- gdb server doStart -----------------------------------------------
	public static boolean getGdbServerDoStart() {

		return Boolean.valueOf(getString(GDB_SERVER_DO_START, Boolean.toString(DefaultPreferences.SERVER_DO_START_DEFAULT)));
	}

	public static void putGdbServerDoStart(boolean value) {

		putWorkspaceString(GDB_SERVER_DO_START, Boolean.toString(value));
	}

	// ----- gdb server executable --------------------------------------------
	public static String getGdbServerExecutable() {

		String value = getString(GDB_SERVER_EXECUTABLE, null);
		if (value != null) {
			return value;
		}
		return DefaultPreferences.stlinkGetGdbServerExecutablePath();
	}

	public static void putGdbServerExecutable(String value) {

		putWorkspaceString(GDB_SERVER_EXECUTABLE, value);
	}

	// ----- flash device id --------------------------------------------------
	public static String getFlashDeviceName() {

		return getString(FLASH_DEVICE_NAME, DefaultPreferences.FLASH_DEVICE_NAME_DEFAULT);
	}

	public static void putFlashDeviceName(String value) {

		putWorkspaceString(FLASH_DEVICE_NAME, value);
	}

	// ----- gdb server endianness --------------------------------------------
	public static String getGdbServerEndianness() {

		return getString(GDB_SERVER_ENDIANNESS, DefaultPreferences.SERVER_ENDIANNESS_DEFAULT);
	}

	public static void putGdbServerEndianness(String value) {

		putWorkspaceString(GDB_SERVER_ENDIANNESS, value);
	}

	// ----- gdb server connection --------------------------------------------
	public static String getGdbServerConnection() {

		return getString(GDB_SERVER_CONNECTION, DefaultPreferences.SERVER_CONNECTION_DEFAULT);
	}

	public static void putGdbServerConnection(String value) {

		putWorkspaceString(GDB_SERVER_CONNECTION, value);
	}

	// ----- gdb server connection address ------------------------------------
	public static String getGdbServerConnectionAddress() {

		return getString(GDB_SERVER_CONNECTION_ADDRESS, DefaultPreferences.SERVER_CONNECTION_ADDRESS_DEFAULT);
	}

	public static void putGdbServerConnectionAddress(String value) {

		putWorkspaceString(GDB_SERVER_CONNECTION_ADDRESS, value);
	}

	// ----- gdb server interface ---------------------------------------------
	public static String getGdbServerInterface() {

		String value = getString(GDB_SERVER_INTERFACE, null);
		if (value != null) {
			return value;
		}
		return DefaultPreferences.getGdbServerInterface();
	}

	public static void putGdbServerInterface(String value) {

		putWorkspaceString(GDB_SERVER_INTERFACE, value);
	}

	// ----- gdb server initial speed -----------------------------------------
	public static String getGdbServerInitialSpeed() {

		return getString(GDB_SERVER_INITIAL_SPEED, DefaultPreferences.SERVER_INITIAL_SPEED_DEFAULT);
	}

	public static void putGdbServerInitialSpeed(String value) {

		putWorkspaceString(GDB_SERVER_INITIAL_SPEED, value);
	}

	// ----- gdb server other options -----------------------------------------
	public static String getGdbServerOtherOptions() {

		return getString(GDB_SERVER_OTHER_OPTIONS, DefaultPreferences.SERVER_OTHER_OPTIONS_DEFAULT);
	}

	public static void putGdbServerOtherOptions(String value) {

		putWorkspaceString(GDB_SERVER_OTHER_OPTIONS, value);
	}

	// ----- gdb client executable --------------------------------------------
	public static String getGdbClientExecutable() {

		String value = getString(GDB_CLIENT_EXECUTABLE, null);
		if (value != null) {
			return value;
		}
		return DefaultPreferences.stLinkGetGdbClientExecutablePath();
	}

	public static void putGdbClientExecutable(String value) {

		putWorkspaceString(GDB_CLIENT_EXECUTABLE, value);
	}

	// ----- gdb client other options -----------------------------------------
	public static String getGdbClientOtherOptions() {

		return getString(GDB_CLIENT_OTHER_OPTIONS, DefaultPreferences.CLIENT_OTHER_OPTIONS_DEFAULT);
	}

	public static void putGdbClientOtherOptions(String value) {

		putWorkspaceString(GDB_CLIENT_OTHER_OPTIONS, value);
	}

	// ----- gdb client commands ----------------------------------------------
	public static String getGdbClientCommands() {

		return getString(GDB_CLIENT_COMMANDS, DefaultPreferences.CLIENT_COMMANDS_DEFAULT);
	}

	public static void putGdbClientCommands(String value) {

		putWorkspaceString(GDB_CLIENT_COMMANDS, value);
	}

	// ----- STlink do initial reset -------------------------------------------
	public static boolean getSTLinkDoInitialReset() {

		return Boolean.valueOf(
				getString(GDB_STLINK_DO_INITIAL_RESET, Boolean.toString(DefaultPreferences.DO_INITIAL_RESET_DEFAULT)));
	}

	public static void putSTLinkDoInitialReset(boolean value) {

		putWorkspaceString(GDB_STLINK_DO_INITIAL_RESET, Boolean.toString(value));
	}

	// ----- stlink initial reset type -----------------------------------------
	public static String getSTLinkInitialResetType() {

		return getString(GDB_STLINK_INITIAL_RESET_TYPE, DefaultPreferences.INITIAL_RESET_TYPE_DEFAULT);
	}

	public static void putSTLinkInitialResetType(String value) {

		putWorkspaceString(GDB_STLINK_INITIAL_RESET_TYPE, value);
	}

	// ----- stlink initial reset speed ----------------------------------------
	public static int getSTLinkInitialResetSpeed() {

		return Integer.valueOf(getString(GDB_STLINK_INITIAL_RESET_SPEED,
				Integer.toString(DefaultPreferences.INITIAL_RESET_SPEED_DEFAULT)));
	}

	public static void putSTLinkInitialResetSpeed(int value) {

		putWorkspaceString(GDB_STLINK_INITIAL_RESET_SPEED, Integer.toString(value));
	}

	// ----- stlink speed ------------------------------------------------------
	public static String getSTLinkSpeed() {

		return getString(GDB_STLINK_SPEED, DefaultPreferences.STLINK_SPEED_DEFAULT);
	}

	public static void putSTLinkSpeed(String value) {

		putWorkspaceString(GDB_STLINK_SPEED, value);
	}

	// ----- stlink enable flash breakpoints -----------------------------------
	public static boolean getSTLinkEnableFlashBreakpoints() {

		return Boolean.valueOf(getString(GDB_STLINK_ENABLE_FLASH_BREAKPOINTS,
				Boolean.toString(DefaultPreferences.ENABLE_FLASH_BREAKPOINTS_DEFAULT)));
	}

	public static void putSTLinkEnableFlashBreakpoints(boolean value) {

		putWorkspaceString(GDB_STLINK_ENABLE_FLASH_BREAKPOINTS, Boolean.toString(value));
	}

	// ----- stlink enable semihosting -----------------------------------------
	public static boolean getSTLinkEnableSemihosting() {

		String value = getString(GDB_STLINK_ENABLE_SEMIHOSTING, null);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return DefaultPreferences.getSTLinkEnableSemihosting();
	}

	public static void putSTLinkEnableSemihosting(boolean value) {

		putWorkspaceString(GDB_STLINK_ENABLE_SEMIHOSTING, Boolean.toString(value));
	}

	// ----- STlink semihosting telnet -----------------------------------------
	public static boolean getSTLinkSemihostingTelnet() {

		return Boolean.valueOf(getString(GDB_STLINK_SEMIHOSTING_TELNET,
				Boolean.toString(DefaultPreferences.ENABLE_SEMIHOSTING_DEFAULT)));
	}

	public static void putSTLinkSemihostingTelnet(boolean value) {

		putWorkspaceString(GDB_STLINK_SEMIHOSTING_TELNET, Boolean.toString(value));
	}

	// ----- STlink semihosting client -----------------------------------------
	public static boolean getSTLinkSemihostingClient() {

		return Boolean.valueOf(getString(GDB_STLINK_SEMIHOSTING_CLIENT,
				Boolean.toString(DefaultPreferences.SEMIHOSTING_CLIENT_DEFAULT)));
	}

	public static void putSTLinkSemihostingClient(boolean value) {

		putWorkspaceString(GDB_STLINK_SEMIHOSTING_CLIENT, Boolean.toString(value));
	}

	// ----- stlink enable swo -------------------------------------------------
	public static boolean getSTLinkEnableSwo() {

		String value = getString(GDB_STLINK_ENABLE_SWO, null);
		if (value != null) {
			return Boolean.valueOf(value);
		}
		return DefaultPreferences.getSTLinkEnableSwo();
	}

	public static void putSTLinkEnableSwo(boolean value) {

		putWorkspaceString(GDB_STLINK_ENABLE_SWO, Boolean.toString(value));
	}

	// ----- stlink swo cpu frequency ------------------------------------------
	public static int getSTLinkSwoEnableTargetCpuFreq() {

		return Integer.valueOf(getString(GDB_STLINK_SWO_ENABLE_TARGET_CPU_FREQ,
				Integer.toString(DefaultPreferences.SWO_ENABLE_TARGET_CPU_FREQ_DEFAULT)));
	}

	public static void putSTLinkSwoEnableTargetCpuFreq(int value) {

		putWorkspaceString(GDB_STLINK_SWO_ENABLE_TARGET_CPU_FREQ, Integer.toString(value));
	}

	// ----- stlink swo frequency ----------------------------------------------
	public static int getSTLinkSwoEnableTargetSwoFreq() {

		return Integer.valueOf(getString(GDB_STLINK_SWO_ENABLE_TARGET_SWO_FREQ,
				Integer.toString(DefaultPreferences.SWO_ENABLE_TARGET_SWO_FREQ_DEFAULT)));
	}

	public static void putSTLinkSwoEnableTargetSwoFreq(int value) {

		putWorkspaceString(GDB_STLINK_SWO_ENABLE_TARGET_SWO_FREQ, Integer.toString(value));
	}

	// ----- stlink swo mask ---------------------------------------------------
	public static String getSTLinkSwoEnableTargetPortMask() {

		return getString(GDB_STLINK_SWO_ENABLE_TARGET_PORT_MASK, DefaultPreferences.SWO_ENABLE_TARGET_PORT_MASK_DEFAULT);
	}

	public static void putSTLinkSwoEnableTargetPortMask(String value) {

		putWorkspaceString(GDB_STLINK_SWO_ENABLE_TARGET_PORT_MASK, value);
	}

	// ----- stlink init other -------------------------------------------------
	public static String getSTLinkInitOther() {

		return getString(GDB_STLINK_INIT_OTHER, DefaultPreferences.INIT_OTHER_DEFAULT);
	}

	public static void putSTLinkInitOther(String value) {

		putWorkspaceString(GDB_STLINK_INIT_OTHER, value);
	}

	// ----- stlink debug in ram -----------------------------------------------
	public static boolean getSTLinkDebugInRam() {

		return Boolean.valueOf(
				getString(GDB_STLINK_DO_DEBUG_IN_RAM, Boolean.toString(DefaultPreferences.DO_DEBUG_IN_RAM_DEFAULT)));
	}

	public static void putSTLinkDebugInRam(boolean value) {

		putWorkspaceString(GDB_STLINK_DO_DEBUG_IN_RAM, Boolean.toString(value));
	}

	// ----- stlink do prerun reset -----------------------------------
	public static boolean getSTLinkDoPreRunReset() {

		return Boolean.valueOf(
				getString(GDB_STLINK_DO_PRERUN_RESET, Boolean.toString(DefaultPreferences.DO_PRERUN_RESET_DEFAULT)));
	}

	public static void putSTLinkDoPreRunReset(boolean value) {

		putWorkspaceString(GDB_STLINK_DO_PRERUN_RESET, Boolean.toString(value));
	}

	// ----- stlink prerun reset type ------------------------------------------
	public static String getSTLinkPreRunResetType() {

		return getString(GDB_STLINK_PRERUN_RESET_TYPE, DefaultPreferences.PRERUN_RESET_TYPE_DEFAULT);
	}

	public static void putSTLinkPreRunResetType(String value) {

		putWorkspaceString(GDB_STLINK_PRERUN_RESET_TYPE, value);
	}

	// ----- stlink init other -------------------------------------------------
	public static String getSTLinkPreRunOther() {

		return getString(GDB_STLINK_PRERUN_OTHER, DefaultPreferences.PRERUN_OTHER_DEFAULT);
	}

	public static void putSTLinkPreRunOther(String value) {

		putWorkspaceString(GDB_STLINK_PRERUN_OTHER, value);
	}

}
