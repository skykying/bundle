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
package com.lembed.lite.studio.managedbuild.cross.llvm;

import org.eclipse.cdt.managedbuilder.core.IManagedCommandLineInfo;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedCommandLineGenerator;
import org.eclipse.cdt.managedbuilder.internal.core.ManagedCommandLineInfo;

/**
 * The Class CompilerCommandLineGenerator.
 */
@SuppressWarnings("restriction")
public class CompilerCommandLineGenerator extends ManagedCommandLineGenerator {

	// ------------------------------------------------------------------------

	@Override
	public IManagedCommandLineInfo generateCommandLineInfo(ITool tool, String commandName, String[] flags,
			String outputFlag, String outputPrefix, String outputName, String[] inputResources,
			String commandLinePattern) {
		IManagedCommandLineInfo lineInfo;
		lineInfo = super.generateCommandLineInfo(tool, commandName, flags, outputFlag, outputPrefix, outputName,
				inputResources, commandLinePattern);

		String newCommandLine = lineInfo.getCommandLine();
		newCommandLine = updateMT(newCommandLine);
		String newFlags = lineInfo.getFlags();
		newFlags = updateMT(newFlags);

		return new ManagedCommandLineInfo(newCommandLine, lineInfo.getCommandLinePattern(), lineInfo.getCommandName(),
				newFlags, lineInfo.getOutputFlag(), lineInfo.getOutputPrefix(), lineInfo.getOutput(),
				lineInfo.getInputs());
	}

	/**
	 * Update MT.
	 *
	 * @param s the s
	 * @return the string
	 */
	private static String updateMT(String s) {
		return s.replace("-MT\"$(@:%.o=%.d)\"", "-MT\"$@\""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// ------------------------------------------------------------------------
}
