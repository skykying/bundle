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
import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;

@SuppressWarnings("javadoc")
public class DefaultPreferences {

	// ------------------------------------------------------------------------

	// Constants.
	public static final String INTERFACE_SWD = "swd"; //$NON-NLS-1$
	public static final String INTERFACE_JTAG = "jtag"; //$NON-NLS-1$

	public static final String ENDIANNESS_LITTLE = "little"; //$NON-NLS-1$
	public static final String ENDIANNESS_BIG = "big"; //$NON-NLS-1$

	public static final String GDB_SERVER_CONNECTION_USB = "usb"; //$NON-NLS-1$
	public static final String GDB_SERVER_CONNECTION_IP = "ip"; //$NON-NLS-1$

	public static final String INTERFACE_SPEED_AUTO = "auto"; //$NON-NLS-1$
	public static final String INTERFACE_SPEED_ADAPTIVE = "adaptive"; //$NON-NLS-1$
	public static final String INTERFACE_SPEED_AUTO_COMMAND = "monitor speed auto"; //$NON-NLS-1$
	public static final String INTERFACE_SPEED_ADAPTIVE_COMMAND = "monitor speed adaptive"; //$NON-NLS-1$
	public static final String INTERFACE_SPEED_FIXED_COMMAND = "monitor speed "; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	// Normally loaded from defaults, but also used in
	// DefaultPreferenceInitializer.
	protected static final boolean TAB_MAIN_CHECK_PROGRAM_DEFAULT = false;

	protected static final String GDB_SERVER_EXECUTABLE_DEFAULT = "${stlink_path}/${stlink_gdbserver}"; //$NON-NLS-1$
	protected static final String GDB_CLIENT_EXECUTABLE_DEFAULT = "${cross_prefix}gdb${cross_suffix}"; //$NON-NLS-1$

	protected static final String SERVER_OTHER_OPTIONS_DEFAULT = ""; //$NON-NLS-1$
	protected static final String SERVER_INTERFACE_DEFAULT = INTERFACE_SWD;
	protected static final boolean ENABLE_SWO_DEFAULT = true;
	protected static final boolean ENABLE_SEMIHOSTING_DEFAULT = true;
	protected static final boolean SERVER_DO_START_DEFAULT = true;
	protected static final String SERVER_ENDIANNESS_DEFAULT = ENDIANNESS_LITTLE;
	protected static final String SERVER_CONNECTION_DEFAULT = GDB_SERVER_CONNECTION_USB;
	protected static final String SERVER_CONNECTION_ADDRESS_DEFAULT = ""; //$NON-NLS-1$
	protected static final String SERVER_INITIAL_SPEED_DEFAULT = "1000"; // kHz //$NON-NLS-1$
	protected static final String CLIENT_OTHER_OPTIONS_DEFAULT = ""; //$NON-NLS-1$
	protected static final String CLIENT_COMMANDS_DEFAULT = ""; //$NON-NLS-1$
	protected static final boolean DO_INITIAL_RESET_DEFAULT = true;
	protected static final String INITIAL_RESET_TYPE_DEFAULT = ""; //$NON-NLS-1$
	protected static final int INITIAL_RESET_SPEED_DEFAULT = 1000;
	protected static final String STLINK_SPEED_DEFAULT = INTERFACE_SPEED_AUTO;
	protected static final boolean ENABLE_FLASH_BREAKPOINTS_DEFAULT = true;
	protected static final boolean SEMIHOSTING_TELNET_DEFAULT = true;
	protected static final boolean SEMIHOSTING_CLIENT_DEFAULT = false;
	protected static final int SWO_ENABLE_TARGET_CPU_FREQ_DEFAULT = 0;
	protected static final int SWO_ENABLE_TARGET_SWO_FREQ_DEFAULT = 0;
	protected static final String SWO_ENABLE_TARGET_PORT_MASK_DEFAULT = "0x1"; //$NON-NLS-1$
	protected static final String INIT_OTHER_DEFAULT = ""; //$NON-NLS-1$
	protected static final boolean DO_DEBUG_IN_RAM_DEFAULT = false;
	protected static final boolean DO_PRERUN_RESET_DEFAULT = true;
	protected static final String PRERUN_RESET_TYPE_DEFAULT = ""; //$NON-NLS-1$
	protected static final String PRERUN_OTHER_DEFAULT = ""; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	// Not yet preferences, only constants, but moved here for consistency.
	public static final boolean DO_CONNECT_TO_RUNNING_DEFAULT = false;
	public static final String FLASH_DEVICE_NAME_DEFAULT = ""; //$NON-NLS-1$

	public static final int GDB_SERVER_GDB_PORT_NUMBER_DEFAULT = 4242;// $NON-NLS-1$
	public static final int GDB_SERVER_SWO_PORT_NUMBER_DEFAULT = 4243;// $NON-NLS-1$
	public static final int GDB_SERVER_TELNET_PORT_NUMBER_DEFAULT = 4244;// $NON-NLS-1$
	public static final boolean DO_GDB_SERVER_VERIFY_DOWNLOAD_DEFAULT = true;
	public static final boolean DO_GDB_SERVER_INIT_REGS_DEFAULT = true;
	public static final boolean DO_GDB_SERVER_LOCAL_ONLY_DEFAULT = true;
	public static final boolean DO_GDB_SERVER_ENABLE_TRACE_DEFAULT = false;
	public static final String GDB_SERVER_LOG_DEFAULT = ""; //$NON-NLS-1$
	public static final boolean DO_GDB_SERVER_ALLOCATE_CONSOLE_DEFAULT = true;
	public static final boolean DO_GDB_SERVER_ALLOCATE_SEMIHOSTING_CONSOLE_DEFAULT = true;
	public static final boolean USE_REMOTE_TARGET_DEFAULT = true;
	public static final String REMOTE_IP_ADDRESS_LOCALHOST = "localhost"; //$NON-NLS-1$
	public static final String REMOTE_IP_ADDRESS_DEFAULT = REMOTE_IP_ADDRESS_LOCALHOST; // $NON-NLS-1$
	public static final int REMOTE_PORT_NUMBER_DEFAULT = GDB_SERVER_GDB_PORT_NUMBER_DEFAULT;
	public static final boolean UPDATE_THREAD_LIST_DEFAULT = false;

	public static final boolean DO_STOP_AT_DEFAULT = true;
	public static final boolean DO_CONTINUE_DEFAULT = true;

	// Debugger Commands
	public static final String ENABLE_SEMIHOSTING_COMMAND = "monitor semihosting enable";//$NON-NLS-1$

	public static final String DO_SECOND_RESET_COMMAND = "monitor reset "; //$NON-NLS-1$
	public static final String STOP_AT_NAME_DEFAULT = "main";//$NON-NLS-1$

	public static final String DO_FIRST_RESET_COMMAND = "monitor reset ";//$NON-NLS-1$

	// Usually these commands are issues together
	public static final String CLRBP_COMMAND = "monitor clrbp";//$NON-NLS-1$
	public static final String HALT_COMMAND = "monitor halt";//$NON-NLS-1$
	public static final String REGS_COMMAND = "monitor regs";//$NON-NLS-1$
	public static final String FLUSH_REGISTERS_COMMAND = "flushreg";//$NON-NLS-1$

	public static final String ENABLE_FLASH_BREAKPOINTS_COMMAND = "monitor flash breakpoints ";//$NON-NLS-1$

	public static final int ENABLE_SEMIHOSTING_IOCLIENT_TELNET_MASK = 1;
	public static final int ENABLE_SEMIHOSTING_IOCLIENT_GDBCLIENT_MASK = 2;

	public static final String ENABLE_SEMIHOSTING_IOCLIENT_COMMAND = "monitor semihosting IOClient "; //$NON-NLS-1$

	public static final String DISABLE_SWO_COMMAND = "monitor SWO DisableTarget 0xFFFFFFFF";//$NON-NLS-1$
	public static final String ENABLE_SWO_COMMAND = "monitor SWO EnableTarget ";//$NON-NLS-1$

	public static final String DO_CONTINUE_COMMAND = "continue";//$NON-NLS-1$

	// ------------------------------------------------------------------------

	// TODO: remove DEPRECATED
	// These values are deprecated. Use the definitions in PersistentValues.
	private static final String GDB_SERVER_EXECUTABLE_DEPRECATED = "gdb.server.executable.default";//$NON-NLS-1$
	private static final String GDB_CLIENT_EXECUTABLE_DEPRECATED = "gdb.client.executable.default";//$NON-NLS-1$

	private static final String STLINK_INTRFACE_DEPRECATED = "interface.default";//$NON-NLS-1$
	private static final String STLINK_ENABLE_SEMIHOSTING_DEPRECATED = "enableSemihosting.default";//$NON-NLS-1$
	private static final String STLINK_ENABLE_SWO_DEPRECATED = "enableSwo.default";//$NON-NLS-1$

	private static final String STLINK_GDBSERVER_DEPRECATED = "stlink_gdbserver.default";//$NON-NLS-1$
	private static final String STLINK_PATH_DEPRECATED = "stlink_path.default";//$NON-NLS-1$

	// ------------------------------------------------------------------------

	/**
	 * The DefaultScope preference store.
	 */
	private static IEclipsePreferences stlinkPreferences;

	// ------------------------------------------------------------------------

	public static IEclipsePreferences stlinkGetPreferences() {

		if (stlinkPreferences == null) {
			stlinkPreferences = DefaultScope.INSTANCE.getNode(DevicePlugin.PLUGIN_ID);
		}

		return stlinkPreferences;
	}

	/**
	 * Get a string preference value, or the default.
	 * 
	 * @param key
	 *            a string with the key to search.
	 * @param defaulValue
	 *            a string with the default, possibly null.
	 * @return a trimmed string, or a null default.
	 */
	private static String getString(String key, String defaulValue) {

		String value;
		value = stlinkGetPreferences().get(key, defaulValue);

		if (value != null) {
			value = value.trim();
		}

		return value;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {

		return stlinkGetPreferences().getBoolean(key, defaultValue);
	}

	private static int getInt(String name, int defValue) {

		return stlinkGetPreferences().getInt(name, defValue);
	}

	public static void stlinkStorePutString(String key, String value) {
		stlinkGetPreferences().put(key, value);
	}

	public static void putInt(String key, int value) {
		stlinkGetPreferences().putInt(key, value);
	}

	public static void putBoolean(String key, boolean value) {
		stlinkGetPreferences().putBoolean(key, value);
	}

	// ------------------------------------------------------------------------

	public static String stlinkGetGdbServerExecutablePath() {
		String value = getString(PersistentPreferences.GDB_SERVER_EXECUTABLE, null);
		if (value != null) {
			return value;
		}
		return getString(GDB_SERVER_EXECUTABLE_DEPRECATED, GDB_SERVER_EXECUTABLE_DEFAULT);
	}

	public static String stLinkGetGdbClientExecutablePath() {
		String value = getString(PersistentPreferences.GDB_CLIENT_EXECUTABLE, null);
		if (value != null) {
			return value;
		}
		return getString(GDB_CLIENT_EXECUTABLE_DEPRECATED, GDB_CLIENT_EXECUTABLE_DEFAULT);
	}

	// ------------------------------------------------------------------------

	public static String getExecutableName() {

		String key = PersistentPreferences.EXECUTABLE_NAME;
		String value = getString(key, null);
		if (value == null) {

			// TODO: remove DEPRECATED
			value = getString(PersistentPreferences.STLINK_GDBSERVER_DEPRECATED, null);
			if (value == null) {
				value = getString(STLINK_GDBSERVER_DEPRECATED, ""); //$NON-NLS-1$
			}
		}

		DevicePlugin.log("getExecutableName()=\"" + value + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		return value;
	}

	public static String getExecutableNameOs() {

		String key = EclipseUtils.getKeyOs(PersistentPreferences.EXECUTABLE_NAME_OS);

		String value = getString(key, ""); //$NON-NLS-1$

		DevicePlugin.log("getExecutableNameOs()=\"" + value + "\" (" + key + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return value;
	}

	public static void putExecutableName(String value) {

		String key = PersistentPreferences.EXECUTABLE_NAME;

		DevicePlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
		stlinkStorePutString(key, value);
	}

	// ------------------------------------------------------------------------

	public static String getInstallFolder() {

		String key = PersistentPreferences.INSTALL_FOLDER;
		String value = getString(key, null);
		if (value == null) {

			// TODO: remove DEPRECATED
			value = getString(PersistentPreferences.STLINK_PATH_DEPRECATED, null);
			if (value == null) {
				value = getString(STLINK_PATH_DEPRECATED, ""); //$NON-NLS-1$
			}
		}

		DevicePlugin.log("getInstallFolder()=\"" + value + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		return value;
	}

	public static void putInstallFolder(String value) {

		String key = PersistentPreferences.INSTALL_FOLDER;

		DevicePlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
		stlinkStorePutString(key, value);
	}

	// ------------------------------------------------------------------------

	public static String getSearchPath() {

		String key = PersistentPreferences.SEARCH_PATH;
		String value = getString(key, ""); //$NON-NLS-1$

		DevicePlugin.log("getSearchPath()=\"" + value + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		return value;
	}

	public static String getSearchPathOs() {

		String key = EclipseUtils.getKeyOs(PersistentPreferences.SEARCH_PATH_OS);
		String value = getString(key, ""); //$NON-NLS-1$

		DevicePlugin.log("getSearchPathOs()=\"" + value + "\" (" + key + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return value;
	}

	public static void putSearchPath(String value) {

		String key = PersistentPreferences.SEARCH_PATH;

		DevicePlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
		stlinkStorePutString(key, value);
	}

	// ------------------------------------------------------------------------

	public static String getGdbServerInterface() {
		String value = getString(PersistentPreferences.GDB_SERVER_INTERFACE, null);
		if (value != null) {
			return value;
		}
		return getString(STLINK_INTRFACE_DEPRECATED, SERVER_INTERFACE_DEFAULT);
	}

	public static boolean getSTLinkEnableSemihosting() {

		try {
			if (stlinkGetPreferences().nodeExists(PersistentPreferences.GDB_STLINK_ENABLE_SEMIHOSTING)) {
				return getBoolean(PersistentPreferences.GDB_STLINK_ENABLE_SEMIHOSTING, ENABLE_SEMIHOSTING_DEFAULT);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return getBoolean(STLINK_ENABLE_SEMIHOSTING_DEPRECATED, ENABLE_SEMIHOSTING_DEFAULT);
	}

	public static boolean getSTLinkEnableSwo() {
		try {
			if (stlinkGetPreferences().nodeExists(PersistentPreferences.GDB_STLINK_ENABLE_SWO)) {
				return getBoolean(PersistentPreferences.GDB_STLINK_ENABLE_SWO, ENABLE_SWO_DEFAULT);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		return getBoolean(STLINK_ENABLE_SWO_DEPRECATED, ENABLE_SWO_DEFAULT);
	}

	// ------------------------------------------------------------------------

	public static boolean getTabMainCheckProgram() {
		return getBoolean(PersistentPreferences.TAB_MAIN_CHECK_PROGRAM, TAB_MAIN_CHECK_PROGRAM_DEFAULT);
	}

	// ------------------------------------------------------------------------

	public static boolean getGdbServerDoStart() {
		return getBoolean(PersistentPreferences.GDB_SERVER_DO_START, SERVER_DO_START_DEFAULT);
	}

	public static String getGdbServerEndianness() {
		return getString(PersistentPreferences.GDB_SERVER_ENDIANNESS, SERVER_ENDIANNESS_DEFAULT);
	}

	public static String getGdbServerConnection() {
		return getString(PersistentPreferences.GDB_SERVER_CONNECTION, SERVER_CONNECTION_DEFAULT);
	}

	public static String getGdbServerConnectionAddress() {
		return getString(PersistentPreferences.GDB_SERVER_CONNECTION_ADDRESS, SERVER_CONNECTION_ADDRESS_DEFAULT);
	}

	public static String getGdbServerInitialSpeed() {
		return getString(PersistentPreferences.GDB_SERVER_INITIAL_SPEED, SERVER_INITIAL_SPEED_DEFAULT);
	}

	public static String getGdbServerOtherOptions() {
		return getString(PersistentPreferences.GDB_SERVER_OTHER_OPTIONS, SERVER_OTHER_OPTIONS_DEFAULT);
	}

	public static String getGdbClientOtherOptions() {
		return getString(PersistentPreferences.GDB_CLIENT_OTHER_OPTIONS, CLIENT_OTHER_OPTIONS_DEFAULT);
	}

	public static String getGdbClientCommands() {
		return getString(PersistentPreferences.GDB_CLIENT_COMMANDS, CLIENT_COMMANDS_DEFAULT);
	}

	public static boolean getSTLinkDoInitialReset() {
		return getBoolean(PersistentPreferences.GDB_STLINK_DO_INITIAL_RESET, DO_INITIAL_RESET_DEFAULT);
	}

	public static String getSTLinkInitialResetType() {
		return getString(PersistentPreferences.GDB_STLINK_INITIAL_RESET_TYPE, INITIAL_RESET_TYPE_DEFAULT);
	}

	public static int getSTLinkInitialResetSpeed() {
		return getInt(PersistentPreferences.GDB_STLINK_INITIAL_RESET_SPEED, INITIAL_RESET_SPEED_DEFAULT);
	}

	public static String getSTLinkSpeed() {
		return getString(PersistentPreferences.GDB_STLINK_SPEED, STLINK_SPEED_DEFAULT);
	}

	public static boolean getSTLinkEnableFlashBreakpoints() {
		return getBoolean(PersistentPreferences.GDB_STLINK_ENABLE_FLASH_BREAKPOINTS, ENABLE_FLASH_BREAKPOINTS_DEFAULT);
	}

	public static boolean getSTLinkSemihostingTelnet() {
		return getBoolean(PersistentPreferences.GDB_STLINK_SEMIHOSTING_TELNET, SEMIHOSTING_TELNET_DEFAULT);
	}

	public static boolean getSTLinkSemihostingClient() {
		return getBoolean(PersistentPreferences.GDB_STLINK_SEMIHOSTING_CLIENT, SEMIHOSTING_CLIENT_DEFAULT);
	}

	public static int getSTLinkSwoEnableTargetCpuFreq() {
		return getInt(PersistentPreferences.GDB_STLINK_SWO_ENABLE_TARGET_CPU_FREQ, SWO_ENABLE_TARGET_CPU_FREQ_DEFAULT);
	}

	public static int getSTLinkSwoEnableTargetSwoFreq() {
		return getInt(PersistentPreferences.GDB_STLINK_SWO_ENABLE_TARGET_SWO_FREQ, SWO_ENABLE_TARGET_SWO_FREQ_DEFAULT);
	}

	public static String getSTLinkSwoEnableTargetPortMask() {
		return getString(PersistentPreferences.GDB_STLINK_SWO_ENABLE_TARGET_PORT_MASK,
				SWO_ENABLE_TARGET_PORT_MASK_DEFAULT);
	}

	public static String getSTLinkInitOther() {
		return getString(PersistentPreferences.GDB_STLINK_INIT_OTHER, INIT_OTHER_DEFAULT);
	}

	public static boolean getSTLinkDebugInRam() {
		return getBoolean(PersistentPreferences.GDB_STLINK_DO_DEBUG_IN_RAM, DO_DEBUG_IN_RAM_DEFAULT);
	}

	public static boolean getSTLinkDoPreRunReset() {
		return getBoolean(PersistentPreferences.GDB_STLINK_DO_PRERUN_RESET, DO_PRERUN_RESET_DEFAULT);
	}

	public static String getSTLinkPreRunResetType() {
		return getString(PersistentPreferences.GDB_STLINK_PRERUN_RESET_TYPE, PRERUN_RESET_TYPE_DEFAULT);
	}

	public static String getSTLinkPreRunOther() {
		return getString(PersistentPreferences.GDB_STLINK_PRERUN_OTHER, PRERUN_OTHER_DEFAULT);
	}

}
