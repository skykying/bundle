/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* Copyright (c) 2017 LEMBED
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
* LEMBED - adapter for LiteSTUDIO, add the toolchain setup functions
*******************************************************************************/

package com.lembed.lite.studio.device.toolchain;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.runtime.IAdaptable;

import com.lembed.lite.studio.device.core.build.IBuildSettings;


/**
 * Interface that sets IConfiguration build options according to IBuildSettings.
 * It extends IAdapdable interface to allow even more flexibility.  
 */
public interface ILiteToolChainAdapter extends IAdaptable {

	/**
	 * Sets tool chain build options for given IConfiguration according to supplied IRteBuildSettings
	 * @param configuration destination IConfiguration to set options to
	 * @param buildSettings source IBuildSettings
	 */
	void setToolChainOptions(IConfiguration configuration,  IBuildSettings buildSettings);


	/**
	 * Sets initial tool chain build options for given IConfiguration according to supplied IRteBuildSettings.<br>
	 * This function is called when device settings are changed (e.g. when new project is created)
	 * @param configuration destination IConfiguration to set options to
	 * @param buildSettings source IBuildSettings
	 */
	void setInitialToolChainOptions(IConfiguration configuration,  IBuildSettings buildSettings);


	/**
	 * Returns Linker script generator associated with the adapter
	 * @return ILinkerScriptGenerator or null if no generator is available
	 */
	ILinkerScriptGenerator getLinkerScriptGenerator();


	/**
	 * init tool chain setup option to apply to project
	 * @param configuration the configure of the cdt project
	 * @param buildSettings the build setting of the lite project
	 */
	void setUpToolChainOptions(IConfiguration configuration, IBuildSettings buildSettings);


	/**
	 * set the default tool chain index, the index must be the index of the cross toolchain package
	 * @param i tool chain index
	 */
	void setToolChainIndex(int i);


	void resetToolChainOptions(IConfiguration configuration, IBuildSettings buildSettings) throws BuildException;

}
