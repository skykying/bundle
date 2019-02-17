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
package com.lembed.lite.studio.managedbuild.cross.preferences;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.osgi.service.prefs.Preferences;

import com.lembed.lite.studio.managedbuild.cross.CrossGccPlugin;
import com.lembed.lite.studio.managedbuild.cross.ToolchainDefinition;
import com.lembed.lite.studio.managedbuild.cross.ui.PersistentPreferences;

/**
 * Initializations are executed in two different moments: as the first step
 * during bundle Initializations and after all defaults are loaded from all possible
 * sources
 * 
 */
public class DefaultPreferenceInitializer extends AbstractPreferenceInitializer {

	// ------------------------------------------------------------------------

	/**
	 * Early inits. Preferences set here might be overridden by plug-in
	 * preferences.ini, product .ini or command line option.
	 */
	@Override
	public void initializeDefaultPreferences() {

		
		CrossGccPlugin.log("DefaultPreferenceInitializer.initializeDefaultPreferences()"); //$NON-NLS-1$
		
		// Default toolchain name
		String toolchainName = ToolchainDefinition.DEFAULT_TOOLCHAIN_NAME;
		DefaultPreferences.putToolchainName(toolchainName);

		// When the 'com.lembed.lite.studio.managedbuild.cross' node is
		// completely
		// added to /default, a NodeChangeEvent is raised.
		// This is the moment when all final default values are in, possibly
		// set by product or command line.

		Preferences prefs = Platform.getPreferencesService().getRootNode().node(DefaultScope.SCOPE);
		if (prefs instanceof IEclipsePreferences) {
			((IEclipsePreferences) prefs).addNodeChangeListener(new LateInitializer());
		}
	}

	/**
	 * INodeChangeListener for late initialisations.
	 */
	private class LateInitializer implements INodeChangeListener {

		@Override
		public void added(NodeChangeEvent event) {

			
			CrossGccPlugin.log("LateInitializer.added() " + event + " " + event.getChild().name());			 //$NON-NLS-1$ //$NON-NLS-2$

			if (CrossGccPlugin.PLUGIN_ID.equals(event.getChild().name())) {

				finalizeInitializationsDefaultPreferences();

				// We're done, de-register listener.
				((IEclipsePreferences) (event.getSource())).removeNodeChangeListener(this);
			}
		}

		@Override
		public void removed(NodeChangeEvent event) {			
			CrossGccPlugin.log("LateInitializer.removed() " + event);			 //$NON-NLS-1$
		}

		/**
		 * The second step of defaults initialisation.
		 */
		public void finalizeInitializationsDefaultPreferences() {

			String value;

			// Build tools path
			value = DefaultPreferences.getBuildToolsPath();
			if (value.isEmpty()) {
				// If not defined elsewhere, discover build tools.
				value = DefaultPreferences.discoverBuildToolsPath();
				if (!value.isEmpty()) {
					DefaultPreferences.putBuildToolsPath(value);
					CrossGccPlugin.log(value);
				}else{
					// log("build tools not found");
				}
			}

			// Toolchains paths
			for (ToolchainDefinition toolchain : ToolchainDefinition.getList()) {

				String toolchainName = toolchain.getName();	
				
				// log(toolchainName +"(" + hash +")");

				// Check if the toolchain path is explictly defined in the
				// default preferences.
				String path = DefaultPreferences.getToolchainPath(toolchainName);
				if (!path.isEmpty()) {
					CrossGccPlugin.log(path + " | " + toolchainName); //$NON-NLS-1$
					continue; // Already defined, use it as is.
				}

				// Check if the search path is defined in the default
				// preferences.
				String searchPath = getBundleToolchainPath(PersistentPreferences.getToolchainRelativePath());
				if (searchPath.isEmpty()) {
					searchPath = DefaultPreferences.getToolchainSearchPath(toolchainName);
				}
				if (searchPath.isEmpty()) {

					// If not defined, get the OS Specific default
					// from preferences.ini.
					searchPath = DefaultPreferences.getToolchainSearchPathOs(toolchainName);
					if (!searchPath.isEmpty()) {
						// Store the search path in the preferences
						DefaultPreferences.putToolchainSearchPath(toolchainName, searchPath);
						// log(searchPath+"    searchpath");
					}else{
						// log("search path is null");
					}
					// log("search path is null ext");
				}
				
				if (!searchPath.isEmpty()) {
					// If the search path is known, discover toolchain.
					value = DefaultPreferences.discoverToolchainPath(toolchainName, searchPath);
					if (value != null && !value.isEmpty()) {
						// If the toolchain path was finally discovered, store
						// it in the preferences.						
						DefaultPreferences.putToolchainPath(toolchainName, value);
						CrossGccPlugin.log(toolchainName +"()" + value);						 //$NON-NLS-1$
					}
					// log("search path is null 2");
				}
			}
		}
	}
	
	/**
	 * Gets the bundle toolchain path.
	 *
	 * @param relPath the rel path
	 * @return the bundle toolchain path
	 */
	public  String getBundleToolchainPath(String relPath) {
		String searchPath = ""; //$NON-NLS-1$
		URL path = Platform.getInstallLocation().getURL();
		CrossGccPlugin.log(path.toString());
		
		File installFile = new File(path.getFile());
		CrossGccPlugin.log(installFile.getPath());
		
		if(installFile.exists()) {			
			File parentFile = installFile.getParentFile();
			if (parentFile.isDirectory()) {
				String vpath = parentFile.getPath() + File.separator + relPath;
				CrossGccPlugin.log(vpath);
				File finalFile = new File(vpath);
				if(finalFile.exists()){
					searchPath = finalFile.getPath();
					CrossGccPlugin.log(searchPath);
				}				
			}
		}else{
			CrossGccPlugin.log("v"); //$NON-NLS-1$
		}

		return searchPath;
	}

}
