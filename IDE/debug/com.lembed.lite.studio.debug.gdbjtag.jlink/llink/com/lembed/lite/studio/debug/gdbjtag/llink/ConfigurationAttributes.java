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

import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;

/**
 * The Interface ConfigurationAttributes.
 */
public interface ConfigurationAttributes {

	// ------------------------------------------------------------------------

	/** The Constant PREFIX. */
	public static final String PREFIX = DevicePlugin.PLUGIN_ID;

	// ------------------------------------------------------------------------

	// TabDebugger

	/** The Constant JTAG_DEVICE. */
	// Must be in sync with plugin.xml definition
	public static final String JTAG_DEVICE = "ST STlink"; //$NON-NLS-1$

	/** The Constant INTERFACE_COMPAT. */
	public static final String INTERFACE_COMPAT = PREFIX + ".interface"; //$NON-NLS-1$
	
	/** The Constant GDB_SERVER_DEBUG_INTERFACE. */
	public static final String GDB_SERVER_DEBUG_INTERFACE = PREFIX + ".gdbServerDebugInterface"; //$NON-NLS-1$

	/** The Constant DO_CONNECT_TO_RUNNING. */
	public static final String DO_CONNECT_TO_RUNNING = PREFIX + ".doConnectToRunning"; //$NON-NLS-1$

	/** The Constant INTERFACE_SPEED. */
	public static final String INTERFACE_SPEED = PREFIX + ".interfaceSpeed"; //$NON-NLS-1$
	
	/** The Constant GDB_SERVER_SPEED_COMPAT. */
	public static final String GDB_SERVER_SPEED_COMPAT = PREFIX + ".gdbServerSpeed"; //$NON-NLS-1$
	
	/** The Constant GDB_SERVER_DEVICE_SPEED. */
	public static final String GDB_SERVER_DEVICE_SPEED = PREFIX + ".gdbServerDeviceSpeed"; //$NON-NLS-1$

	/** The Constant FLASH_DEVICE_NAME_COMPAT. */
	public static final String FLASH_DEVICE_NAME_COMPAT = PREFIX + ".flashDeviceName"; //$NON-NLS-1$
	
	/** The Constant GDB_SERVER_DEVICE_NAME. */
	public static final String GDB_SERVER_DEVICE_NAME = PREFIX + ".gdbServerDeviceName"; //$NON-NLS-1$

	/** The Constant ENDIANNESS_COMPAT. */
	public static final String ENDIANNESS_COMPAT = PREFIX + ".endianness"; //$NON-NLS-1$
	
	/** The Constant GDB_SERVER_DEVICE_ENDIANNESS. */
	public static final String GDB_SERVER_DEVICE_ENDIANNESS = PREFIX + ".gdbServerDeviceEndianness"; //$NON-NLS-1$

	/** The Constant DO_START_GDB_SERVER. */
	public static final String DO_START_GDB_SERVER = PREFIX + ".doStartGdbServer"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_EXECUTABLE. */
	public static final String GDB_SERVER_EXECUTABLE = PREFIX + ".gdbServerExecutable"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_CONNECTION. */
	public static final String GDB_SERVER_CONNECTION = PREFIX + ".gdbServerConnection"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_CONNECTION_ADDRESS. */
	public static final String GDB_SERVER_CONNECTION_ADDRESS = PREFIX + ".gdbServerConnectionAddress"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_GDB_PORT_NUMBER. */
	public static final String GDB_SERVER_GDB_PORT_NUMBER = PREFIX + ".gdbServerGdbPortNumber"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_SWO_PORT_NUMBER. */
	public static final String GDB_SERVER_SWO_PORT_NUMBER = PREFIX + ".gdbServerSwoPortNumber"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_TELNET_PORT_NUMBER. */
	public static final String GDB_SERVER_TELNET_PORT_NUMBER = PREFIX + ".gdbServerTelnetPortNumber"; //$NON-NLS-1$

	/** The Constant DO_GDB_SERVER_VERIFY_DOWNLOAD. */
	public static final String DO_GDB_SERVER_VERIFY_DOWNLOAD = PREFIX + ".doGdbServerVerifyDownload"; //$NON-NLS-1$

	/** The Constant DO_GDB_SERVER_INIT_REGS. */
	public static final String DO_GDB_SERVER_INIT_REGS = PREFIX + ".doGdbServerInitRegs"; //$NON-NLS-1$

	/** The Constant DO_GDB_SERVER_LOCAL_ONLY. */
	public static final String DO_GDB_SERVER_LOCAL_ONLY = PREFIX + ".doGdbServerLocalOnly"; //$NON-NLS-1$

	/** The Constant DO_GDB_SERVER_ENABLE_TRACE. */
	public static final String DO_GDB_SERVER_ENABLE_TRACE = PREFIX + ".doGdbServerEnableTrace"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_LOG. */
	public static final String GDB_SERVER_LOG = PREFIX + ".gdbServerLog"; //$NON-NLS-1$

	/** The Constant GDB_SERVER_OTHER. */
	public static final String GDB_SERVER_OTHER = PREFIX + ".gdbServerOther"; //$NON-NLS-1$

	/** The Constant DO_GDB_SERVER_ALLOCATE_CONSOLE. */
	public static final String DO_GDB_SERVER_ALLOCATE_CONSOLE = PREFIX + ".doGdbServerAllocateConsole"; //$NON-NLS-1$

	/** The Constant DO_GDB_SERVER_ALLOCATE_SEMIHOSTING_CONSOLE. */
	public static final String DO_GDB_SERVER_ALLOCATE_SEMIHOSTING_CONSOLE = PREFIX
			+ ".doGdbServerAllocateSemihostingConsole"; //$NON-NLS-1$

	/** The Constant GDB_CLIENT_OTHER_OPTIONS. */
	public static final String GDB_CLIENT_OTHER_OPTIONS = PREFIX + ".gdbClientOtherOptions"; //$NON-NLS-1$

	/** The Constant GDB_CLIENT_OTHER_COMMANDS. */
	public static final String GDB_CLIENT_OTHER_COMMANDS = PREFIX + ".gdbClientOtherCommands"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/** The Constant DO_FIRST_RESET. */
	// TabStartup
	public static final String DO_FIRST_RESET = PREFIX + ".doFirstReset"; //$NON-NLS-1$

	/** The Constant FIRST_RESET_TYPE. */
	public static final String FIRST_RESET_TYPE = PREFIX + ".firstResetType"; //$NON-NLS-1$

	/** The Constant FIRST_RESET_SPEED. */
	public static final String FIRST_RESET_SPEED = PREFIX + ".firstResetSpeed"; //$NON-NLS-1$

	/** The Constant ENABLE_FLASH_BREAKPOINTS. */
	public static final String ENABLE_FLASH_BREAKPOINTS = PREFIX + ".enableFlashBreakpoints"; //$NON-NLS-1$

	/** The Constant ENABLE_SEMIHOSTING. */
	public static final String ENABLE_SEMIHOSTING = PREFIX + ".enableSemihosting"; //$NON-NLS-1$

	/** The Constant ENABLE_SEMIHOSTING_IOCLIENT_TELNET. */
	public static final String ENABLE_SEMIHOSTING_IOCLIENT_TELNET = PREFIX + ".enableSemihostingIoclientTelnet"; //$NON-NLS-1$

	/** The Constant ENABLE_SEMIHOSTING_IOCLIENT_GDBCLIENT. */
	public static final String ENABLE_SEMIHOSTING_IOCLIENT_GDBCLIENT = PREFIX + ".enableSemihostingIoclientGdbClient"; //$NON-NLS-1$

	/** The Constant ENABLE_SWO. */
	public static final String ENABLE_SWO = PREFIX + ".enableSwo"; //$NON-NLS-1$

	/** The Constant SWO_ENABLETARGET_CPUFREQ. */
	public static final String SWO_ENABLETARGET_CPUFREQ = PREFIX + ".swoEnableTargetCpuFreq"; //$NON-NLS-1$

	/** The Constant SWO_ENABLETARGET_SWOFREQ. */
	public static final String SWO_ENABLETARGET_SWOFREQ = PREFIX + ".swoEnableTargetSwoFreq"; //$NON-NLS-1$

	/** The Constant SWO_ENABLETARGET_PORTMASK. */
	public static final String SWO_ENABLETARGET_PORTMASK = PREFIX + ".swoEnableTargetPortMask"; //$NON-NLS-1$

	/** The Constant OTHER_INIT_COMMANDS. */
	public static final String OTHER_INIT_COMMANDS = PREFIX + ".otherInitCommands"; //$NON-NLS-1$

	/** The Constant DO_DEBUG_IN_RAM. */
	public static final String DO_DEBUG_IN_RAM = PREFIX + ".doDebugInRam"; //$NON-NLS-1$

	/** The Constant DO_SECOND_RESET. */
	public static final String DO_SECOND_RESET = PREFIX + ".doSecondReset"; //$NON-NLS-1$

	/** The Constant SECOND_RESET_TYPE. */
	public static final String SECOND_RESET_TYPE = PREFIX + ".secondResetType"; //$NON-NLS-1$

	/** The Constant OTHER_RUN_COMMANDS. */
	public static final String OTHER_RUN_COMMANDS = PREFIX + ".otherRunCommands"; //$NON-NLS-1$

	/** The Constant DO_CONTINUE. */
	public static final String DO_CONTINUE = PREFIX + ".doContinue"; //$NON-NLS-1$

	// ------------------------------------------------------------------------
}
