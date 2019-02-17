/*******************************************************************************
 * Copyright (c) 2013 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.emulator;

import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.IValueVariableInitializer;

import com.lembed.lite.studio.debug.gdbjtag.emulator.ui.Messages;

public class VariableInitializer implements IValueVariableInitializer {

	// ------------------------------------------------------------------------

	public static final String VARIABLE_QEMU_EXECUTABLE = "emulator_executable";
	public static final String VARIABLE_QEMU_PATH = "emulator_path";

	static final String UNDEFINED_PATH = "undefined_path";

	// ------------------------------------------------------------------------

	@Override
	public void initialize(IValueVariable variable) {

		String value;

		if (VARIABLE_QEMU_EXECUTABLE.equals(variable.getName())) {

			value = DefaultPreferences.getExecutableName();
			if (value == null) {
				value = DefaultPreferences.SERVER_EXECUTABLE_DEFAULT_NAME;
			}
			variable.setValue(value);
			variable.setDescription(Messages.Variable_executable_description);

		} else if (VARIABLE_QEMU_PATH.equals(variable.getName())) {

			value = DefaultPreferences.getInstallFolder();
			if (value == null) {
				value = UNDEFINED_PATH;
			}
			variable.setValue(value);
			variable.setDescription(Messages.Variable_path_description);

		}
	}

	// ------------------------------------------------------------------------
}
