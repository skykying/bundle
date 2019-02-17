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
package com.lembed.lite.studio.managedbuild.cross.llvm.preferences;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.managedbuild.cross.llvm.LlvmUIPlugin;

import org.eclipse.cdt.core.templateengine.SharedDefaults;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

@SuppressWarnings("javadoc")
public class PersistentPreferences {

	// ------------------------------------------------------------------------

	
    public static final String  TOOLCHAIN_NAME_KEY 				= "toolchain.name"; //$NON-NLS-1$
	private static final String TOOLCHAIN_PATH_KEY 				= "toolchain.path.%d"; //$NON-NLS-1$
	public static final String  TOOLCHAIN_RELATIVE_PATH_KEY 	= "toolchain.path.relative"; //$NON-NLS-1$
	private static final String TOOLCHAIN_SEARCH_PATH_KEY 		= "toolchain.search.path.%d"; //$NON-NLS-1$
	private static final String TOOLCHAIN_SEARCH_PATH_OS_KEY 	= "toolchain.search.path.%s.%d"; //$NON-NLS-1$

	public static final String  BUILD_TOOLS_PATH_KEY 			= "buildTools.path"; //$NON-NLS-1$
	public static final String  BUILD_TOOLS_RELATIVE_PATH_KEY 	= "buildTools.path.relative"; //$NON-NLS-1$
	private static final String BUILD_TOOLS_SEARCH_PATH_KEY 	= "buildTools.search.path"; //$NON-NLS-1$
	private static final String BUILD_TOOLS_SEARCH_PATH_OS_KEY 	= "buildTools.search.path.%s"; //$NON-NLS-1$

	public static final String  GLOBAL_TOOLCHAIN_PATH_STRICT 	= "global.toolchain.path.strict"; //$NON-NLS-1$
	public static final String  GLOBAL_BUILDTOOLS_PATH_STRICT 	= "global.buildTools.path.strict"; //$NON-NLS-1$
	public static final String  WORKSPACE_TOOLCHAIN_PATH_STRICT = "workspace.toolchain.path.strict"; //$NON-NLS-1$
	public static final String  WORKSPACE_BUILDTOOLS_PATH_STRICT= "workspace.buildTools.path.strict"; //$NON-NLS-1$

	// Note: The shared defaults keys don't have "cross" in them because we want
	// to keep
	// compatibility with defaults that were saved when it used to be a template
	// static final String SHARED_CROSS_TOOLCHAIN_NAME =
	// SetCrossCommandWizardPage.CROSS_TOOLCHAIN_NAME;
	// static final String SHARED_CROSS_TOOLCHAIN_PATH =
	// SetCrossCommandWizardPage.CROSS_TOOLCHAIN_PATH;

	// ----- Getters ----------------------------------------------------------
	private static String getString(String key, String defaultValue, IProject project) {

		String value = EclipseUtils.getPreferenceValueForId(LlvmUIPlugin.PLUGIN_ID, key, null, project);
		if (value != null && !value.isEmpty()) {
			return value;
		}

		// TODO: remove DEPRECATED
		// Keep this a while for compatibility with the first versions
		// which erroneously stored values in the shared storage.
		value = SharedDefaults.getInstance().getSharedDefaultsMap().get(LlvmUIPlugin.PLUGIN_ID + "." + key); //$NON-NLS-1$

		if (value == null) {
			value = ""; //$NON-NLS-1$
		}

		value = value.trim();
		if (!value.isEmpty()) {
			return value;
		}

		return defaultValue;
	}

	protected static String getEclipseString(String key, String defaultValue) {

		// Access the Eclipse scope
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(LlvmUIPlugin.PLUGIN_ID);

		String value = preferences.get(key, defaultValue);
		return value;
	}

	private static String getWorkspaceString(String key, String defaultValue) {

		// Access the Eclipse scope
		Preferences preferences = InstanceScope.INSTANCE.getNode(LlvmUIPlugin.PLUGIN_ID);

		String value = preferences.get(key, defaultValue);
		return value;
	}

	// ----- Setters ----------------------------------------------------------

	private static void putString(String key, String value) {

		String oldValue = getWorkspaceString(key, null);
		if (oldValue != null) {
			putWorkspaceString(key, value);
		} else {
			putEclipseString(key, value);
		}
	}

	private static void putEclipseString(String key, String value) {

		String pvalue = value.trim();

		// Access the Eclipse scope
		Preferences preferences = ConfigurationScope.INSTANCE.getNode(LlvmUIPlugin.PLUGIN_ID);
		preferences.put(key, pvalue);
	}

	private static void putWorkspaceString(String key, String value) {

	    String pvalue = value.trim();

		// Access the Workspace scope
		Preferences preferences = InstanceScope.INSTANCE.getNode(LlvmUIPlugin.PLUGIN_ID);
		preferences.put(key, pvalue);
	}

	public static void flush() {

		try {
			ConfigurationScope.INSTANCE.getNode(LlvmUIPlugin.PLUGIN_ID).flush();
			InstanceScope.INSTANCE.getNode(LlvmUIPlugin.PLUGIN_ID).flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	private static void putProjectString(String key, String value, IProject project) {

	    String pvalue = value.trim();

		// Access the Eclipse scope
		Preferences preferences = new ProjectScope(project).getNode(LlvmUIPlugin.PLUGIN_ID);
		preferences.put(key, pvalue);
	}

	public static void flush(IProject project) {

		try {
			new ProjectScope(project).getNode(LlvmUIPlugin.PLUGIN_ID).flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the last used toolchain name.
	 * 
	 * @return a trimmed string, possibly empty.
	 */
	public static String getToolchainName() {

		String toolchainName = getString(TOOLCHAIN_NAME_KEY, null, null);
		if (toolchainName != null && !toolchainName.isEmpty()) {
			return toolchainName;
		}

		// TODO: remove DEPRECATED
		toolchainName = DefaultPreferences.getToolchainName();

		return toolchainName;
	}

	/**
	 * Store the toolchain name in the Workspace/Eclipse scope. Used in the
	 * project wizard, to maintain global persistency.
	 * 
	 * @param toolchainName
	 *            a string.
	 */
	public static void putToolchainName(String toolchainName) {
		putString(TOOLCHAIN_NAME_KEY, toolchainName);
	}

	// ------------------------------------------------------------------------

	public static String getToolchainKey(String toolchainName) {

		int hash = Math.abs(toolchainName.trim().hashCode());
		String key = String.format(TOOLCHAIN_PATH_KEY, hash);
		return key;
	}

	/**
	 * Get the toolchain path for a given toolchain name.
	 * 
	 * @param toolchainName
	 * @return a string, possibly empty.
	 */
	public static String getToolchainPath(String toolchainName, IProject project) {

		String value = getString(getToolchainKey(toolchainName), null, project);
		if (value != null && !value.isEmpty()) {
			return value;
		}

		value = ""; //$NON-NLS-1$
		{
			// TODO: remove DEPRECATED
			value = DefaultPreferences.getToolchainPath(toolchainName);
		}

		return value;
	}

	/**
	 * Store the toolchain path in the Workspace/Eclipse scope. Used in the
	 * project wizard, to maintain global persistency.
	 * 
	 * @param toolchainName
	 * @param path
	 */
	public static void putToolchainPath(String toolchainName, String path) {

		putString(getToolchainKey(toolchainName), path);
	}

	/**
	 * Store the toolchain path in the Project scope. Used in
	 * EnvironmentVariableSupplier to copy path from old storage to new storage.
	 * 
	 * @param toolchainName
	 * @param path
	 * @param project
	 */
	public static void putToolchainPath(String toolchainName, String path, IProject project) {

		putProjectString(getToolchainKey(toolchainName), path, project);
	}

	// ------------------------------------------------------------------------

	public static String getToolchainSearchKey(String toolchainName) {

		int hash = Math.abs(toolchainName.trim().hashCode());
		String key = String.format(TOOLCHAIN_SEARCH_PATH_KEY, hash);
		// System.out.println(key);
		return key;
	}

	public static String getToolchainSearchOsKey(String toolchainName) {

		int hash = Math.abs(toolchainName.trim().hashCode());
		String os = EclipseUtils.getOsFamily();
		String key = String.format(TOOLCHAIN_SEARCH_PATH_OS_KEY, os, hash);
		// System.out.println(key);
		return key;
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the build tools path. Search all possible scopes.
	 * 
	 * @return a string, possibly empty.
	 */
	public static String getBuildToolsPath(IProject project) {

		return getString(BUILD_TOOLS_PATH_KEY, "", project); //$NON-NLS-1$
	}

	public static String getBuildToolsPathKey() {

		return BUILD_TOOLS_PATH_KEY;
	}

	public static String getBuildToolSearchKey() {	
		return BUILD_TOOLS_SEARCH_PATH_KEY;
	}

	public static String getBuildToolSearchOsKey() {
		String os = EclipseUtils.getOsFamily();
		String key = String.format(BUILD_TOOLS_SEARCH_PATH_OS_KEY, os);
		// System.out.println(key);
		return key;
	}

	// ------------------------------------------------------------------------
	// 
	public static String getBuildToolsRelativeKey(){
		return BUILD_TOOLS_RELATIVE_PATH_KEY;
	}
	public static String getBuildToolsRelativePath(){
		return getString(BUILD_TOOLS_RELATIVE_PATH_KEY,"",null); //$NON-NLS-1$
	}
	
	public static String getToolchainRelativeKey(){
		return TOOLCHAIN_RELATIVE_PATH_KEY;
	}
	public static String getToolchainRelativePath(){
		return getString(TOOLCHAIN_RELATIVE_PATH_KEY,"",null); //$NON-NLS-1$
	}
}
