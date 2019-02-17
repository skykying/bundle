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

import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.IValueVariableInitializer;

import com.lembed.lite.studio.debug.gdbjtag.llink.ui.Messages;

/**
 * The Class VariableInitializer.
 */
public class VariableInitializer implements IValueVariableInitializer {

	/** The Constant VARIABLE_L_LINK_EXECUTABLE. */
	public static final String VARIABLE_L_LINK_EXECUTABLE = "l_link_gdbserver"; //$NON-NLS-1$
	
	/** The Constant VARIABLE_L_LINK_PATH. */
	public static final String VARIABLE_L_LINK_PATH = "l_link_path";//$NON-NLS-1$

	static final String UNDEFINED_PATH = "undefined_path";//$NON-NLS-1$
	static final String UNDEFINED_NAME = "undefined";//$NON-NLS-1$

	@Override
	public void initialize(IValueVariable variable) {

		String value;

		if (VARIABLE_L_LINK_EXECUTABLE.equals(variable.getName())) {

			value = DefaultPreferences.getExecutableName();
			if (value == null) {
				value = UNDEFINED_NAME;
			}
			variable.setValue(value);
			variable.setDescription(Messages.Variable_executable_description);

		} else if (VARIABLE_L_LINK_PATH.equals(variable.getName())) {

			value = DefaultPreferences.getInstallFolder();
			if (value == null) {
				value = UNDEFINED_PATH;
			}
			variable.setValue(value);
			variable.setDescription(Messages.Variable_path_description);

		}
	}

}
