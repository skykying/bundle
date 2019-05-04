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

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.StringUtils;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.device.DevicePlugin;
import com.lembed.lite.studio.debug.gdbjtag.DebugUtils;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmDebuggerCommandsService;
import com.lembed.lite.studio.debug.gdbjtag.llink.ConfigurationAttributes;
import com.lembed.lite.studio.debug.gdbjtag.llink.DefaultPreferences;


import java.util.List;
import java.util.Map;

import org.eclipse.cdt.debug.core.CDebugUtils;
import org.eclipse.cdt.debug.gdbjtag.core.IGDBJtagConstants;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.osgi.framework.BundleContext;

/**
 * The Class STDebuggerCommands.
 */
public class STDebuggerCommands extends GnuArmDebuggerCommandsService {


	/**
	 * Instantiates a new ST debugger commands.
	 *
	 * @param session the session
	 * @param lc the lc
	 * @param mode the mode
	 */
	public STDebuggerCommands(DsfSession session, ILaunchConfiguration lc, String mode) {
		super(session, lc, mode, true); // do double backslash
	}

	@Override
	protected BundleContext getBundleContext() {
		return GdbActivator.getInstance().getBundle().getBundleContext();
	}

	@Override
	public IStatus addGdbInitCommandsCommands(List<String> commandsList) {

		String otherInits = isConfigured(ConfigurationAttributes.GDB_CLIENT_OTHER_COMMANDS,
				DefaultPreferences.getGdbClientCommands()).trim();

		otherInits = DebugUtils.resolveAll(otherInits, getAttributes());
		DebugUtils.addMultiLine(otherInits, commandsList);

		DevicePlugin.log("61 " + otherInits + commandsList.toString()); //$NON-NLS-1$

		return Status.OK_STATUS;
	}

	private Map<String, Object> getAttributes() {
		return fAttributes;
	}

	@Override
	public IStatus addGnuArmResetCommands(List<String> commandsList) {

		IStatus status = addFirstResetCommands(commandsList);
		if (!status.isOK()) {
			return status;
		}

		status = addLoadSymbolsCommands(commandsList);

		if (!status.isOK()) {
			return status;
		}

		boolean doConnectToRunning = isConfigured(ConfigurationAttributes.DO_CONNECT_TO_RUNNING,
				DefaultPreferences.DO_CONNECT_TO_RUNNING_DEFAULT);

		if (!doConnectToRunning) {
			boolean isLoadImage = isConfigured(IGDBJtagConstants.ATTR_LOAD_IMAGE, IGDBJtagConstants.DEFAULT_LOAD_IMAGE);
			boolean isRAMApp = isConfigured(ConfigurationAttributes.DO_DEBUG_IN_RAM,DefaultPreferences.getSTLinkDebugInRam());
			
			if (isLoadImage	&& !isRAMApp) {

				DevicePlugin.log("A LOAD IMAGE 1"); //$NON-NLS-1$
				status = addLoadImageCommands(commandsList);
				DevicePlugin.log("A LOAD IMAGE 2"); //$NON-NLS-1$

				if (!status.isOK()) {
					return status;
				}
			}
		}
		DevicePlugin.log("addGnuArmResetCommands " + commandsList.toString()); //$NON-NLS-1$
		return Status.OK_STATUS;
	}

	@Override
	public IStatus addGnuArmStartCommands(List<String> commandsList) {

		boolean doReset = !isConfigured(ConfigurationAttributes.DO_CONNECT_TO_RUNNING,
				DefaultPreferences.DO_CONNECT_TO_RUNNING_DEFAULT);

		IStatus status = addStartRestartCommands(doReset, commandsList);

		if (!status.isOK()) {
			return status;
		}

		return Status.OK_STATUS;
	}

	@Override
	public IStatus addGnuArmRestartCommands(List<String> commandsList) {

		return addStartRestartCommands(true, commandsList);
	}

	// ------------------------------------------------------------------------

	@Override
	public IStatus addFirstResetCommands(List<String> commandsList) {

		String attr;
		String commandStr;

		/**
		 * set the DAP interface speed
		 */
		try {
			attr = String.valueOf(isConfigured(ConfigurationAttributes.FIRST_RESET_SPEED,
					DefaultPreferences.getSTLinkInitialResetSpeed()));
		} catch (Exception e) {
			String speed = String.valueOf(DefaultPreferences.getSTLinkInitialResetSpeed());
			attr = isConfigured(ConfigurationAttributes.FIRST_RESET_SPEED, speed);
		}
		if (!attr.isEmpty()) {
			commandsList.add(DefaultPreferences.INTERFACE_SPEED_FIXED_COMMAND + attr);
		}

		/**
		 * if the option "connect to running target" is set, the target do not need to
		 * reset
		 */
		boolean default_connect_to_running = DefaultPreferences.DO_CONNECT_TO_RUNNING_DEFAULT;
		boolean needResetTarget = isConfigured(ConfigurationAttributes.DO_CONNECT_TO_RUNNING,
				default_connect_to_running);

		if (needResetTarget == false) {
			if (isConfigured(ConfigurationAttributes.DO_FIRST_RESET, DefaultPreferences.getSTLinkDoInitialReset())) {

				// Since reset does not clear breakpoints, we do it explicitly
				commandStr = DefaultPreferences.CLRBP_COMMAND;
				commandsList.add(commandStr);

				commandStr = DefaultPreferences.DO_FIRST_RESET_COMMAND;
				String resetType = isConfigured(ConfigurationAttributes.FIRST_RESET_TYPE,
						DefaultPreferences.getSTLinkInitialResetType());
				commandsList.add(commandStr + resetType);

				// Although the manual claims that reset always does a
				// halt, better issue it explicitly
				commandStr = DefaultPreferences.HALT_COMMAND;
				commandsList.add(commandStr);

				// Also add a command to see the registers in the
				// location where execution halted
				commandStr = DefaultPreferences.REGS_COMMAND;
				commandsList.add(commandStr);

				// Flush registers, GDB should read them again
				commandStr = DefaultPreferences.FLUSH_REGISTERS_COMMAND;
				commandsList.add(commandStr);
			}
		}

		attr = isConfigured(ConfigurationAttributes.INTERFACE_SPEED, DefaultPreferences.INTERFACE_SPEED_AUTO);
		if (DefaultPreferences.INTERFACE_SPEED_AUTO.equals(attr)) {
			commandsList.add(DefaultPreferences.INTERFACE_SPEED_AUTO_COMMAND);
		} else if (DefaultPreferences.INTERFACE_SPEED_ADAPTIVE.equals(attr)) {
			commandsList.add(DefaultPreferences.INTERFACE_SPEED_ADAPTIVE_COMMAND);
		} else {
			commandsList.add(DefaultPreferences.INTERFACE_SPEED_FIXED_COMMAND + attr);
		}

		commandStr = DefaultPreferences.ENABLE_FLASH_BREAKPOINTS_COMMAND;
		if (isConfigured(ConfigurationAttributes.ENABLE_FLASH_BREAKPOINTS,
				DefaultPreferences.getSTLinkEnableFlashBreakpoints())) {
			commandStr += "1"; //$NON-NLS-1$
		} else {
			commandStr += "0"; //$NON-NLS-1$
		}
		commandsList.add(commandStr);

		if (isConfigured(ConfigurationAttributes.ENABLE_SEMIHOSTING, DefaultPreferences.getSTLinkEnableSemihosting())) {
			commandStr = DefaultPreferences.ENABLE_SEMIHOSTING_COMMAND;
			commandsList.add(commandStr);

			int ioclientMask = 0;
			if (isConfigured(ConfigurationAttributes.ENABLE_SEMIHOSTING_IOCLIENT_TELNET,
					DefaultPreferences.getSTLinkSemihostingTelnet())) {
				ioclientMask |= DefaultPreferences.ENABLE_SEMIHOSTING_IOCLIENT_TELNET_MASK;
			}
			if (CDebugUtils.getAttribute(getAttributes(), ConfigurationAttributes.ENABLE_SEMIHOSTING_IOCLIENT_GDBCLIENT,
					DefaultPreferences.getSTLinkSemihostingClient())) {
				ioclientMask |= DefaultPreferences.ENABLE_SEMIHOSTING_IOCLIENT_GDBCLIENT_MASK;
			}

			commandStr = DefaultPreferences.ENABLE_SEMIHOSTING_IOCLIENT_COMMAND + String.valueOf(ioclientMask);
			commandsList.add(commandStr);
		}

		attr = isConfigured(ConfigurationAttributes.GDB_SERVER_DEBUG_INTERFACE, DefaultPreferences.INTERFACE_SWD);
		if (DefaultPreferences.INTERFACE_SWD.equals(attr)) {

			if (isConfigured(ConfigurationAttributes.ENABLE_SWO, DefaultPreferences.getSTLinkEnableSwo())) {

				commandsList.add(DefaultPreferences.DISABLE_SWO_COMMAND);

				commandStr = DefaultPreferences.ENABLE_SWO_COMMAND;
				commandStr += isConfigured(ConfigurationAttributes.SWO_ENABLETARGET_CPUFREQ,
						DefaultPreferences.getSTLinkSwoEnableTargetCpuFreq());
				commandStr += " "; //$NON-NLS-1$
				commandStr += isConfigured(ConfigurationAttributes.SWO_ENABLETARGET_SWOFREQ,
						DefaultPreferences.getSTLinkSwoEnableTargetSwoFreq());
				commandStr += " "; //$NON-NLS-1$
				commandStr += isConfigured(ConfigurationAttributes.SWO_ENABLETARGET_PORTMASK,
						DefaultPreferences.getSTLinkSwoEnableTargetPortMask());
				commandStr += " 0"; //$NON-NLS-1$

				commandsList.add(commandStr);
			}
		}

		String otherInits = isConfigured(ConfigurationAttributes.OTHER_INIT_COMMANDS,
				DefaultPreferences.getSTLinkInitOther()).trim();

		otherInits = DebugUtils.resolveAll(otherInits, getAttributes());
		if (fDoDoubleBackslash && EclipseUtils.isWindows()) {
			otherInits = StringUtils.duplicateBackslashes(otherInits);
		}

		commandsList.clear();
		DevicePlugin.log("260 " + otherInits + commandsList.toString()); //$NON-NLS-1$
		DebugUtils.addMultiLine(otherInits, commandsList);

		return Status.OK_STATUS;
	}

	@Override
	public IStatus addStartRestartCommands(boolean doReset, List<String> commandsList) {
		DevicePlugin.log("addStartRestartCommands"); //$NON-NLS-1$
		String commandStr;
		Boolean DEBUG = true;

		if (doReset) {
			if (isConfigured(ConfigurationAttributes.DO_SECOND_RESET, DefaultPreferences.getSTLinkDoPreRunReset())) {

				// Since reset does not clear breakpoints, we do it
				// explicitly
				commandStr = DefaultPreferences.CLRBP_COMMAND;
				commandsList.add(commandStr);

				commandStr = DefaultPreferences.DO_SECOND_RESET_COMMAND;
				String resetType = isConfigured(ConfigurationAttributes.SECOND_RESET_TYPE,
						DefaultPreferences.getSTLinkPreRunResetType()).trim();
				commandsList.add(commandStr + resetType);

				// Although the manual claims that reset always does a
				// halt, better issue it explicitly
				commandStr = DefaultPreferences.HALT_COMMAND;
				commandsList.add(commandStr);
			}
		}

		if (isConfigured(IGDBJtagConstants.ATTR_LOAD_IMAGE, IGDBJtagConstants.DEFAULT_LOAD_IMAGE)
				&& isConfigured(ConfigurationAttributes.DO_DEBUG_IN_RAM, DefaultPreferences.getSTLinkDebugInRam())) {
			DevicePlugin.log("LOAD IMAGE 1"); //$NON-NLS-1$
			IStatus status = addLoadImageCommands(commandsList);
			DevicePlugin.log("LOAD IMAGE 2"); //$NON-NLS-1$
			if (!status.isOK()) {
				return status;
			}
		}

		String userCmd = isConfigured(ConfigurationAttributes.OTHER_RUN_COMMANDS,
				DefaultPreferences.getSTLinkPreRunOther()).trim();

		userCmd = DebugUtils.resolveAll(userCmd, getAttributes());

		if (fDoDoubleBackslash && EclipseUtils.isWindows()) {
			userCmd = StringUtils.duplicateBackslashes(userCmd);
		}

		DebugUtils.addMultiLine(userCmd, commandsList);

		addSetPcCommands(commandsList);

		addStopAtCommands(commandsList);

		if (!DEBUG) {
			// Also add a command to see the registers in the
			// location where execution halted
			commandStr = DefaultPreferences.REGS_COMMAND;
			commandsList.add(commandStr);

			// Flush registers, GDB should read them again
			commandStr = DefaultPreferences.FLUSH_REGISTERS_COMMAND;
			commandsList.add(commandStr);

		}
		if (isConfigured(ConfigurationAttributes.DO_CONTINUE, DefaultPreferences.DO_CONTINUE_DEFAULT)) {
			commandsList.add(DefaultPreferences.DO_CONTINUE_COMMAND);
		}
		// commandsList.clear();

		DevicePlugin.log("340 " + commandsList.toString()); //$NON-NLS-1$

		return Status.OK_STATUS;
	}

	@SuppressWarnings("unchecked")
    private <V> V isConfigured(String key, V defaultValue) {
		if (getAttributes() == null) {
			return defaultValue;
		}

		Object value = getAttributes().get(key);
		if (defaultValue.getClass().isInstance(value)) {
			return (V) value;
		}
		return defaultValue;
	}
}
