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
package com.lembed.lite.studio.managedbuild.cross;

import java.util.ArrayList;

import org.eclipse.cdt.core.cdtvariables.ICdtVariable;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.macros.IBuildMacro;
import org.eclipse.cdt.managedbuilder.macros.IBuildMacroProvider;
import org.eclipse.cdt.managedbuilder.macros.IConfigurationBuildMacroSupplier;

/**
 * The Class ConfigurationBuildMacroSupplier.
 */
public class ConfigurationBuildMacroSupplier implements IConfigurationBuildMacroSupplier {

	// ------------------------------------------------------------------------

	private String[] fCmds = { "cross_prefix", "cross_suffix", "cross_c", "cross_cpp", "cross_ar", "cross_objcopy", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"cross_objdump", "cross_size", "cross_make", "cross_rm" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	private static String CROSS_FLAGS = "cross_toolchain_flags"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	@Override
	public IBuildMacro getMacro(String macroName, IConfiguration configuration, IBuildMacroProvider provider) {

		for (String sCmd : fCmds) {
			if (sCmd.equals(macroName)) {
				IToolChain toolchain = configuration.getToolChain();

				String sId = Option.OPTION_PREFIX + ".command." + sCmd.replace("cross_", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

				IOption option = toolchain.getOptionBySuperClassId(sId); // $NON-NLS-1$
				if (option != null) {
					String sVal = (String) option.getValue();

					CrossGccPlugin.log("Macro " + sCmd + "=" + sVal + " cfg=" + configuration + " prj=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							+ configuration.getManagedProject().getOwner().getName());

					return new BuildMacro(macroName, ICdtVariable.VALUE_TEXT, sVal);
				}

				CrossGccPlugin.log("Missing value of " + sId); //$NON-NLS-1$
				return null;
			}
		}

		if (CROSS_FLAGS.equals(macroName)) {
			String sValue = Option.getToolChainFlags(configuration);
			if (sValue != null && sValue.length() > 0) {
				return new BuildMacro(macroName, ICdtVariable.VALUE_TEXT, sValue);
			}
		}
		CrossGccPlugin.log("Missing value of " + macroName + " in " + configuration.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}

	// Generate a set of configuration specific macros to pass the
	// tool chain commands (like ${cross_c}) to the make generator.

	@Override
	public IBuildMacro[] getMacros(IConfiguration configuration, IBuildMacroProvider provider) {

		IToolChain toolchain = configuration.getToolChain();
		ArrayList<IBuildMacro> oMacrosList = new ArrayList<>();

		String sValue;
		for (String cmd : fCmds) {
			String sId = Option.OPTION_PREFIX + ".command." + cmd.replace("cross_", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			IOption option = toolchain.getOptionBySuperClassId(sId); // $NON-NLS-1$
			if (option != null) {
				sValue = (String) option.getValue();

				oMacrosList.add(new BuildMacro(cmd, ICdtVariable.VALUE_TEXT, sValue));
			}
		}

		sValue = Option.getToolChainFlags(configuration);
		if (sValue != null && sValue.length() > 0) {
			oMacrosList.add(new BuildMacro(CROSS_FLAGS, ICdtVariable.VALUE_TEXT, sValue));
		}

		return oMacrosList.toArray(new IBuildMacro[0]);
	}

	// ------------------------------------------------------------------------
}
