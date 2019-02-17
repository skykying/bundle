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

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler;
import org.eclipse.cdt.managedbuilder.internal.core.FolderInfo;

import com.lembed.lite.studio.core.AbstractUIActivator;

/**
 * The Class FloatABIManagedOptionHandleValue.
 */
@SuppressWarnings("restriction")
public class FloatABIManagedOptionHandleValue extends ManagedOptionValueHandler {

	// ------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.cdt.managedbuilder.core.IManagedOptionValueHandler#
	 * handleValue (IConfiguration,IToolChain,IOption,String,int)
	 */
	@Override
    public boolean handleValue(IBuildObject configuration, IHoldsOptions holder, IOption option, String extraArgument,
			int event) {

		// ManagedOptionValueHandlerDebug.dump(configuration, holder, option,
		// extraArgument, event);

		if (event == EVENT_APPLY) {

			if (configuration instanceof FolderInfo) {

				//FolderInfo oFolderInfo;
				//oFolderInfo = (FolderInfo) configuration;

				IConfiguration config = ((FolderInfo) configuration).getParent();
				IToolChain toolchain = config.getToolChain();

				IOption opt;
				String val;
				try {
					opt = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARM_TARGET_FAMILY);
					val = opt.getStringValue();
					
					LlvmUIPlugin.log(opt+""+val+"+++++++++++++++++++++"); //$NON-NLS-1$ //$NON-NLS-2$
					
					if (!val.endsWith(ClangOption.OPTION_ARM_MCPU_CORTEXM4)) {
						return false;
					}

					opt = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARM_TARGET_FLOAT_ABI);
					val = opt.getStringValue();

					if (!val.endsWith(ClangOption.OPTION_ARM_FPU_ABI_HARD)) {
						val = opt.getStringValue();
						if (!val.endsWith(ClangOption.OPTION_ARM_FPU_ABI_SOFTFP)) {

							opt = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARM_TARGET_FLOAT_UNIT);
							config.setOption(toolchain, opt, ClangOption.OPTION_ARM_FPU_UNIT_DEFAULT);

							return false; // neither hard nor softfp;
						}
					}
					opt = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARM_TARGET_FLOAT_UNIT);
					// opt.setValue(Option.OPTION_ARM_FPU_UNIT_FPV4SPD16);
					config.setOption(toolchain, opt, ClangOption.OPTION_ARM_FPU_UNIT_FPV4SPD16);

					return true;
				} catch (BuildException e) {
					AbstractUIActivator.log(e);
				}
				return false; // should we return true?
			}
		}

		return false;
	}

}
