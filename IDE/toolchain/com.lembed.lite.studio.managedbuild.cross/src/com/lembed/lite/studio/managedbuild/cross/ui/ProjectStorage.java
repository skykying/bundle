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

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import com.lembed.lite.studio.managedbuild.cross.CrossGccPlugin;

/**
 * DEPRECATED!
 * 
 * This storage is a per project binary file located in
 * .metadata/.plugins/org.eclipse
 * .core.resources/.projects/f4/.indexes/properties.index
 * 
 * The two values stored here (a flag and a path) were moved to preferences.
 */
public class ProjectStorage {

	// ------------------------------------------------------------------------

	private static String TOOLCHAIN_PATH = "toolchain.path"; //$NON-NLS-1$
	private static String IS_TOOLCHAIN_PATH_PER_PROJECT = "is.toolchain.path.per.project"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	// Was used from PathManagedOptionValueHandler

	/**
	 * Checks if is toolchain path per project.
	 *
	 * @param config the config
	 * @return true, if is toolchain path per project
	 */
	public static boolean isToolchainPathPerProject(IConfiguration config) {

		IProject project = (IProject) config.getManagedProject().getOwner();

		String value;
		try {
			value = project.getPersistentProperty(new QualifiedName(config.getId(), IS_TOOLCHAIN_PATH_PER_PROJECT));
		} catch (CoreException e) {
			CrossGccPlugin.log(e.getStatus());
			return false;
		}

		if (value == null) {
			value = ""; //$NON-NLS-1$
		}

		return "true".equalsIgnoreCase(value.trim()); //$NON-NLS-1$
	}

	/**
	 * Put toolchain path per project.
	 *
	 * @param config the config
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean putToolchainPathPerProject(IConfiguration config, boolean value) {

		IProject project = (IProject) config.getManagedProject().getOwner();

		try {
			project.setPersistentProperty(new QualifiedName(config.getId(), IS_TOOLCHAIN_PATH_PER_PROJECT),
					String.valueOf(value));
		} catch (CoreException e) {
			CrossGccPlugin.log(e.getStatus());
			return false;
		}

		return true;
	}

	/**
	 * Get the toolchain path for a given configuration.
	 * 
	 * @param config IConfiguration
	 * @return a string, possibly empty.
	 */
	public static String getToolchainPath(IConfiguration config) {

		IProject project = (IProject) config.getManagedProject().getOwner();

		String value;
		try {
			value = project.getPersistentProperty(new QualifiedName(config.getId(), TOOLCHAIN_PATH));
		} catch (CoreException e) {
			CrossGccPlugin.log(e.getStatus());
			return ""; //$NON-NLS-1$
		}

		if (value == null) {
			value = ""; //$NON-NLS-1$
		}

		return value.trim();
	}

	/**
	 * Put toolchain path.
	 *
	 * @param config the config
	 * @param value the value
	 * @return true, if successful
	 */
	public static boolean putToolchainPath(IConfiguration config, String value) {

		IProject project = (IProject) config.getManagedProject().getOwner();

		try {
			project.setPersistentProperty(new QualifiedName(config.getId(), TOOLCHAIN_PATH), value.trim());
		} catch (CoreException e) {
			CrossGccPlugin.log(e.getStatus());
			return false;
		}

		return true;
	}

	// ------------------------------------------------------------------------
}
