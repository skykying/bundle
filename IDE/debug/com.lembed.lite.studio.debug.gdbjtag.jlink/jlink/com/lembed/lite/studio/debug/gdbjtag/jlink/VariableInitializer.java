package com.lembed.lite.studio.debug.gdbjtag.jlink;

import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.IValueVariableInitializer;
import com.lembed.lite.studio.debug.gdbjtag.jlink.ui.Messages;

public class VariableInitializer implements IValueVariableInitializer {

	public static final String VARIABLE_JLINK_EXECUTABLE = "jlink_gdbserver";
	public static final String VARIABLE_JLINK_PATH = "jlink_path";

	static final String UNDEFINED_PATH = "undefined_path";
	static final String UNDEFINED_NAME = "undefined";

	@Override
	public void initialize(IValueVariable variable) {

		String value;

		if (VARIABLE_JLINK_EXECUTABLE.equals(variable.getName())) {

			value = DefaultPreferences.getExecutableName();
			if (value == null) {
				value = UNDEFINED_NAME;
			}
			variable.setValue(value);
			variable.setDescription(Messages.Variable_executable_description);

		} else if (VARIABLE_JLINK_PATH.equals(variable.getName())) {

			value = DefaultPreferences.getInstallFolder();
			if (value == null) {
				value = UNDEFINED_PATH;
			}
			variable.setValue(value);
			variable.setDescription(Messages.Variable_path_description);

		}
	}

}
