/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.project;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;

import com.lembed.lite.studio.device.core.info.ICpFileInfo;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.project.impl.LiteProjectStorage;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterInfo;

/**
 * Interface defining a CMSIS RTE project than manages RTE Configurations
 */
public interface ILiteProject extends IAdaptable {

	/**
	 * Returns project name
	 * 
	 * @return project name
	 */
	String getName();

	/**
	 * Sets project name
	 * 
	 * @param set
	 *            new project name
	 */
	void setName(String name);

	/**
	 * Returns IProject resource associated with RTE project
	 * 
	 * @return IProject associated with RTE project
	 */
	IProject getProject();

	/**
	 * Destroys the IRteProject
	 */
	void destroy();

	/**
	 * Returns project's RTE configuration
	 * 
	 * @return IRteConfiguration if exists
	 */
	ILiteConfiguration getLiteConfiguration();

	/**
	 * Sets RTE configuration
	 * 
	 * @param rteConfigName
	 *            configuration name
	 * @param rteConfig
	 *            IRteConfiguration
	 */
	void setLiteConfiguration(ILiteConfiguration liteConfig);

	/**
	 * Returns RTE-related information stored in .cproject file
	 * 
	 * @return RteProjectStorage
	 */
	public LiteProjectStorage getProjectStorage();

	/**
	 * Returns RteToolChainAdapterInfo used by project
	 * 
	 * @return RteToolChainAdapterInfo used by project
	 */
	LiteToolChainAdapterInfo getToolChainAdapterInfo();

	/**
	 * Sets RteToolChainAdapterInfo to be used by project
	 * 
	 * @param toolChainAdapterInfo
	 *            RteToolChainAdapterInfo to be used by project
	 */
	void setToolChainAdapterInfo(LiteToolChainAdapterInfo toolChainAdapterInfo);

	/**
	 * Checks if file is used by the project RTE configuration
	 * 
	 * @param fileName
	 *            project-relative file name
	 * @return true if file is used
	 */
	boolean isFileUsed(String fileName);

	/**
	 * Returns ICpFileInfo associated with project file resource
	 * 
	 * @param fileName
	 *            project-relative file name
	 * @return ICpFileInfo if exists
	 */
	ICpFileInfo getProjectFileInfo(String fileName);

	/**
	 * Returns ICpFileInfos associated with project file resource
	 * 
	 * @param fileName
	 *            project-relative file name (can contain *)
	 * @return ICpFileInfos if exists
	 */
	ICpFileInfo[] getProjectFileInfos(String fileName);

	/**
	 * Initializes new project, triggers update of resources, dynamic files and
	 * toolchain settings.<br>
	 * This method is called for a new project.
	 */
	void init();

	/**
	 * Loads project data, triggers reload and update of resources, dynamic
	 * files and toolchain settings
	 * 
	 * @throws CoreException
	 */
	void load() throws CoreException;

	/**
	 * Reloads RTE configuration and performs update of resources, dynamic files
	 * and toolchain settings
	 */
	void reload();

	/**
	 * Triggers reload and full update of resources, dynamic files and toolchain
	 * settings.<br>
	 * This method is called for a "Refresh" menu command.
	 */
	void refresh();

	/**
	 * Clean-up project by removing excluded RTE config files. Default does
	 * nothing
	 */
	default void cleanup() {
		/* no action */ }

	/**
	 * Saves project data
	 * 
	 * @throws CoreException
	 */
	void save() throws CoreException;

	/**
	 * Checks if project is fully loaded and updated
	 * 
	 * @return true if project update is completed
	 */
	boolean isUpdateCompleted();

	/**
	 * Sets project complete state (should be called from an object that updates
	 * this project)
	 * 
	 * @param completed
	 *            flag indicating if update is completed
	 */
	void setUpdateCompleted(boolean completed);

	Map<String, String> getProjectFeatures();

}
