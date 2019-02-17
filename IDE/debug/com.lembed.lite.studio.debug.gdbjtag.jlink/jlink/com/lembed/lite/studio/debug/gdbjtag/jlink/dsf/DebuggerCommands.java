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

package com.lembed.lite.studio.debug.gdbjtag.jlink.dsf;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.StringUtils;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.DebugUtils;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmDebuggerCommandsService;
import com.lembed.lite.studio.debug.gdbjtag.jlink.JlinkActivator;
import com.lembed.lite.studio.debug.gdbjtag.jlink.CfgAttributes;
import com.lembed.lite.studio.debug.gdbjtag.jlink.DefaultPreferences;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.cdt.debug.gdbjtag.core.IGDBJtagConstants;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.osgi.framework.BundleContext;

public class DebuggerCommands extends GnuArmDebuggerCommandsService {

	private boolean isJlinkOB = true;

	public DebuggerCommands(DsfSession session, ILaunchConfiguration lc, String mode) {
		super(session, lc, mode, true); // do double backslash
	}

	// ------------------------------------------------------------------------

	@Override
	protected BundleContext getBundleContext() {
		return GdbActivator.getInstance().getBundle().getBundleContext();
	}

	// ------------------------------------------------------------------------

	@Override
	public IStatus addGdbInitCommandsCommands(List<String> commandsList) {

		String otherInits = isConfigured(CfgAttributes.GDB_CLIENT_OTHER_COMMANDS,
				DefaultPreferences.getGdbClientCommands()).trim();

		otherInits = DebugUtils.resolveAll(otherInits, fAttributes);
		DebugUtils.addMultiLine(otherInits, commandsList);

		return Status.OK_STATUS;
	}

	// ------------------------------------------------------------------------

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

		boolean doConnectToRunning = isConfigured(CfgAttributes.DO_CONNECT_TO_RUNNING,
				DefaultPreferences.DO_CONNECT_TO_RUNNING_DEFAULT);

		if (!doConnectToRunning) {
			Boolean loadImage = isConfigured(IGDBJtagConstants.ATTR_LOAD_IMAGE, IGDBJtagConstants.DEFAULT_LOAD_IMAGE);
			Boolean isRAMApp = !isConfigured(CfgAttributes.DO_DEBUG_IN_RAM,	DefaultPreferences.getJLinkDebugInRam());

			if (loadImage && isRAMApp) {

				status = addLoadImageCommands(commandsList);

				if (!status.isOK()) {
					return status;
				}
			}
		}
		return Status.OK_STATUS;
	}

	@Override
	public IStatus addGnuArmStartCommands(List<String> commandsList) {

		boolean doReset = !isConfigured(CfgAttributes.DO_CONNECT_TO_RUNNING,
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
		List<String> cmdList = new LinkedList<String>();
		String attr;

		try {
			attr = String.valueOf(
					isConfigured(CfgAttributes.FIRST_RESET_SPEED, DefaultPreferences.getJLinkInitialResetSpeed()));
		} catch (Exception e) {
			attr = isConfigured(CfgAttributes.FIRST_RESET_SPEED,
					String.valueOf(DefaultPreferences.getJLinkInitialResetSpeed()));
		}
		if (!attr.isEmpty()) {
			cmdList.add(DefaultPreferences.INTERFACE_SPEED_FIXED_COMMAND + attr);
		}

		String commandStr;
		boolean noReset = isConfigured(CfgAttributes.DO_CONNECT_TO_RUNNING,
				DefaultPreferences.DO_CONNECT_TO_RUNNING_DEFAULT);
		if (!noReset) {
			if (isConfigured(CfgAttributes.DO_FIRST_RESET, DefaultPreferences.getJLinkDoInitialReset())) {

				// Since reset does not clear breakpoints, we do it explicitly
				commandStr = DefaultPreferences.CLRBP_COMMAND;
				if (!isJlinkOB) {
					cmdList.add(commandStr);
				}

				commandStr = DefaultPreferences.DO_FIRST_RESET_COMMAND;
				String resetType = isConfigured(CfgAttributes.FIRST_RESET_TYPE,
						DefaultPreferences.getJLinkInitialResetType());
				cmdList.add(commandStr + resetType);

				// Although the manual claims that reset always does a
				// halt, better issue it explicitly
				commandStr = DefaultPreferences.HALT_COMMAND;
				cmdList.add(commandStr);

				// Also add a command to see the registers in the
				// location where execution halted
				commandStr = DefaultPreferences.REGS_COMMAND;
				if (!isJlinkOB) {
					cmdList.add(commandStr);
				}

				// Flush registers, GDB should read them again
				commandStr = DefaultPreferences.FLUSH_REGISTERS_COMMAND;
				if (!isJlinkOB) {
					cmdList.add(commandStr);
				}
			}
		}

		attr = isConfigured(CfgAttributes.INTERFACE_SPEED, DefaultPreferences.INTERFACE_SPEED_AUTO);
		if (DefaultPreferences.INTERFACE_SPEED_AUTO.equals(attr)) {
			cmdList.add(DefaultPreferences.INTERFACE_SPEED_AUTO_COMMAND);
		} else if (DefaultPreferences.INTERFACE_SPEED_ADAPTIVE.equals(attr)) {
			cmdList.add(DefaultPreferences.INTERFACE_SPEED_ADAPTIVE_COMMAND);
		} else {
			cmdList.add(DefaultPreferences.INTERFACE_SPEED_FIXED_COMMAND + attr);
		}

		commandStr = DefaultPreferences.ENABLE_FLASH_BREAKPOINTS_COMMAND;
		if (isConfigured(CfgAttributes.ENABLE_FLASH_BREAKPOINTS, DefaultPreferences.getJLinkEnableFlashBreakpoints())) {
			commandStr += "1";
		} else {
			commandStr += "0";
		}
		if (!isJlinkOB) {
			cmdList.add(commandStr);
		}

		/**
		 * enable SEMIHOSTING feature
		 */
		if (isConfigured(CfgAttributes.ENABLE_SEMIHOSTING, DefaultPreferences.getJLinkEnableSemihosting())) {
			commandStr = DefaultPreferences.ENABLE_SEMIHOSTING_COMMAND;
			cmdList.add(commandStr);

			int ioclientMask = 0;
			if (isConfigured(CfgAttributes.ENABLE_SEMIHOSTING_IOCLIENT_TELNET,
					DefaultPreferences.getJLinkSemihostingTelnet())) {
				ioclientMask |= DefaultPreferences.ENABLE_SEMIHOSTING_IOCLIENT_TELNET_MASK;
			}

			if (isConfigured(CfgAttributes.ENABLE_SEMIHOSTING_IOCLIENT_GDBCLIENT,
					DefaultPreferences.getJLinkSemihostingClient())) {
				ioclientMask |= DefaultPreferences.ENABLE_SEMIHOSTING_IOCLIENT_GDBCLIENT_MASK;
			}

			commandStr = DefaultPreferences.ENABLE_SEMIHOSTING_IOCLIENT_COMMAND + String.valueOf(ioclientMask);
			cmdList.add(commandStr);
		}

		/**
		 * configure the DAP port, "SWD" or "JTAG" interface. set by TabDebugger class
		 */
		attr = isConfigured(CfgAttributes.GDB_SERVER_DEBUG_INTERFACE, DefaultPreferences.INTERFACE_SWD);
		if (DefaultPreferences.INTERFACE_SWD.equals(attr)) {

			if (isConfigured(CfgAttributes.ENABLE_SWO, DefaultPreferences.getJLinkEnableSwo())) {

				cmdList.add(DefaultPreferences.DISABLE_SWO_COMMAND);

				commandStr = DefaultPreferences.ENABLE_SWO_COMMAND;
				commandStr += isConfigured(CfgAttributes.SWO_ENABLETARGET_CPUFREQ,
						DefaultPreferences.getJLinkSwoEnableTargetCpuFreq());
				commandStr += " ";
				commandStr += isConfigured(CfgAttributes.SWO_ENABLETARGET_SWOFREQ,
						DefaultPreferences.getJLinkSwoEnableTargetSwoFreq());
				commandStr += " ";
				commandStr += isConfigured(CfgAttributes.SWO_ENABLETARGET_PORTMASK,
						DefaultPreferences.getJLinkSwoEnableTargetPortMask());
				commandStr += " 0";

				cmdList.add(commandStr);
			}
		}

		/**
		 * other init script in the TabDebugger widgets.
		 */
		String otherInits = isConfigured(CfgAttributes.OTHER_INIT_COMMANDS, DefaultPreferences.getJLinkInitOther())
				.trim();

		otherInits = DebugUtils.resolveAll(otherInits, fAttributes);
		if (fDoDoubleBackslash && EclipseUtils.isWindows()) {
			otherInits = StringUtils.duplicateBackslashes(otherInits);
		}
		DebugUtils.addMultiLine(otherInits, cmdList);

		commandsList.addAll(cmdList);
		JlinkActivator.log(cmdList.toString());

		return Status.OK_STATUS;
	}

	/**
	 * add the start and restart scripts, the values get from TabDebugger class.
	 */
	@Override
	public IStatus addStartRestartCommands(boolean doReset, List<String> commandsList) {
		List<String> cmdList = new LinkedList<String>();
		String commandStr = "";

		if (doReset) {
			if (isConfigured(CfgAttributes.DO_SECOND_RESET, DefaultPreferences.getJLinkDoPreRunReset())) {

				// Since reset does not clear breakpoints, we do it
				// explicitly
				commandStr = DefaultPreferences.CLRBP_COMMAND;
				cmdList.add(commandStr);

				commandStr = DefaultPreferences.DO_SECOND_RESET_COMMAND;
				String resetType = isConfigured(CfgAttributes.SECOND_RESET_TYPE,
						DefaultPreferences.getJLinkPreRunResetType()).trim();
				cmdList.add(commandStr + resetType);

				// Although the manual claims that reset always does a
				// halt, better issue it explicitly
				commandStr = DefaultPreferences.HALT_COMMAND;
				cmdList.add(commandStr);
			}
		}

		if (isConfigured(IGDBJtagConstants.ATTR_LOAD_IMAGE, IGDBJtagConstants.DEFAULT_LOAD_IMAGE)
				&& isConfigured(CfgAttributes.DO_DEBUG_IN_RAM, DefaultPreferences.getJLinkDebugInRam())) {

			IStatus status = addLoadImageCommands(cmdList);

			if (!status.isOK()) {
				return status;
			}
		}

		String userCmd = isConfigured(CfgAttributes.OTHER_RUN_COMMANDS, DefaultPreferences.getJLinkPreRunOther())
				.trim();

		userCmd = DebugUtils.resolveAll(userCmd, fAttributes);

		if (fDoDoubleBackslash && EclipseUtils.isWindows()) {
			userCmd = StringUtils.duplicateBackslashes(userCmd);
		}

		DebugUtils.addMultiLine(userCmd, cmdList);

		addSetPcCommands(cmdList);

		addStopAtCommands(cmdList);

		// Also add a command to see the registers in the
		// location where execution halted
		commandStr = DefaultPreferences.REGS_COMMAND;
		if (!isJlinkOB) {
			cmdList.add(commandStr);
		}

		// Flush registers, GDB should read them again
		commandStr = DefaultPreferences.FLUSH_REGISTERS_COMMAND;
		if (!isJlinkOB) {
			cmdList.add(commandStr);
		}

		if (isConfigured(CfgAttributes.DO_CONTINUE, DefaultPreferences.DO_CONTINUE_DEFAULT)) {
			cmdList.add(DefaultPreferences.DO_CONTINUE_COMMAND);
		}

		commandsList.addAll(cmdList);
		JlinkActivator.log(cmdList.toString());
		return Status.OK_STATUS;
	}

	private <V> V isConfigured(String key, V defaultValue) {
		if (fAttributes == null) {
			return defaultValue;
		}

		Object value = fAttributes.get(key);
		if (defaultValue.getClass().isInstance(value)) {
			return (V) value;
		}
		return defaultValue;
	}
}
