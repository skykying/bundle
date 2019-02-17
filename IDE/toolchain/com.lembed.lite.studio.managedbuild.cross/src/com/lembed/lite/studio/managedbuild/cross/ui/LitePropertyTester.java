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
package com.lembed.lite.studio.managedbuild.cross.ui;

import java.util.Objects;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IProject;
import com.lembed.lite.studio.managedbuild.cross.Option;

/**
 * Property tester for the Tools Paths page.
 * 
 * The project must be managed and the type must be one of the IDE defined type.
 */
public class LitePropertyTester extends PropertyTester {

	/** The Constant TYPE_PREFIX. */
	public static final String TYPE_PREFIX = "com.lembed.lite.studio.managedbuild.cross.target."; //$NON-NLS-1$

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

		if ("isLite".equals(property)) { //$NON-NLS-1$
			if (receiver instanceof IProject) {

				IProject project = (IProject) receiver;
				IManagedBuildInfo info = ManagedBuildManager.getBuildInfo(project);
				if (info == null) {
					return false; // Not managed build
				}

				IManagedProject managedProject = info.getManagedProject();
				if(Objects.isNull(managedProject)) {
				    return false;
				}
				
				IConfiguration[] cfgs = managedProject.getConfigurations();
				for (int i = 0; i < cfgs.length; ++i) {
					IToolChain toolchain = cfgs[i].getToolChain();
					if (toolchain == null) {
						continue;
					}
					IOption option = toolchain.getOptionBySuperClassId(Option.OPTION_TOOLCHAIN_NAME);
					if (option == null) {
						continue;
					}
					try {
						String name = option.getStringValue();
						// Might be empty
						if (name != null) {
							return true;
						}
					} catch (BuildException e) {
						e.printStackTrace();
					}

				}

				return false;

				// IProjectType projectType = managedProject.getProjectType();
				//
				// if (projectType == null
				// || !projectType.getId().startsWith(TYPE_PREFIX)) {
				// return false;
				// }
				// return true;
			}
		}
		return false;
	}

}
