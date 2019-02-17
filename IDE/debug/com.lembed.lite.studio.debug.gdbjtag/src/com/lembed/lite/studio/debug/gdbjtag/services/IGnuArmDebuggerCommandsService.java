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

package com.lembed.lite.studio.debug.gdbjtag.services;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;

/**
 * The Interface IGnuArmDebuggerCommandsService.
 */
public interface IGnuArmDebuggerCommandsService {

	/**
	 * Sets the attributes.
	 *
	 * @param attributes the attributes
	 */
	public void setAttributes(Map<String, Object> attributes);

	// ------------------------------------------------------------------------

	/**
	 * This step is part of the GROUP_TOP_LEVEL. Cannot use the IGDBJtagDevice
	 * object, it is not available at this moment.
	 * 
	 * @param commandsList List<String>
	 * @return Status.OK_STATUS or an IStatus object if error.
	 */
	public IStatus addGdbInitCommandsCommands(List<String> commandsList);

	// ------------------------------------------------------------------------

	/**
	 * Adds the gnu arm select remote commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addGnuArmSelectRemoteCommands(List<String> commandsList);

	/**
	 * Adds the gnu arm reset commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addGnuArmResetCommands(List<String> commandsList);

	/**
	 * Adds the gnu arm start commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addGnuArmStartCommands(List<String> commandsList);

	/**
	 * Adds the gnu arm restart commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addGnuArmRestartCommands(List<String> commandsList);

	// ------------------------------------------------------------------------

	/**
	 * Adds the first reset commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addFirstResetCommands(List<String> commandsList);

	/**
	 * Adds the load symbols commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addLoadSymbolsCommands(List<String> commandsList);

	/**
	 * Adds the load image commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addLoadImageCommands(List<String> commandsList);

	/**
	 * Used by both FinalLaunchSequence & RestartProcessSequence.
	 *
	 * @param doReset the do reset
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addStartRestartCommands(boolean doReset, List<String> commandsList);

	/**
	 * Adds the set pc commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addSetPcCommands(List<String> commandsList);

	/**
	 * Adds the stop at commands.
	 *
	 * @param commandsList the commands list
	 * @return the i status
	 */
	public IStatus addStopAtCommands(List<String> commandsList);

	// ------------------------------------------------------------------------
}
