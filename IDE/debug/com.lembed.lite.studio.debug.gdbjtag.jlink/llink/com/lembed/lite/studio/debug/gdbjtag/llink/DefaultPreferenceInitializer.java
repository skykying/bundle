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
package com.lembed.lite.studio.debug.gdbjtag.llink;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.preferences.Discoverer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.osgi.service.prefs.Preferences;

/**
 * Initialisations are executed in two different moments: as the first step
 * during bundle inits and after all defaults are loaded from all possible
 * sources
 * 
 */
public class DefaultPreferenceInitializer extends AbstractPreferenceInitializer {

	// ------------------------------------------------------------------------

	/** The Constant REG_SUBKEY. */
	// Current SEGGER versions use HKEY_CURRENT_USER
	//private static final String REG_SUBKEY = "\\Lembed\\L-Link"; //$NON-NLS-1$
	
	/** The Constant REG_NAME. */
	//private static final String REG_NAME = "InstallPath"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/**
	 * Early inits. Preferences set here might be overridden by plug-in
	 * preferences.ini, product .ini or command line option.
	 */
	@Override
	public void initializeDefaultPreferences() {

		LlinkPlugin.log("DefaultPreferenceInitializer.initializeDefaultPreferences()"); //$NON-NLS-1$
		
		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_SERVER_INTERFACE,
				DefaultPreferences.SERVER_INTERFACE_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_ENABLE_SEMIHOSTING,
				DefaultPreferences.ENABLE_SEMIHOSTING_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_ENABLE_SWO,
				DefaultPreferences.ENABLE_SWO_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.TAB_MAIN_CHECK_PROGRAM,
				DefaultPreferences.TAB_MAIN_CHECK_PROGRAM_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_SERVER_DO_START,
				DefaultPreferences.SERVER_DO_START_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_SERVER_ENDIANNESS,
				DefaultPreferences.SERVER_ENDIANNESS_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_SERVER_CONNECTION,
				DefaultPreferences.SERVER_CONNECTION_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_SERVER_CONNECTION_ADDRESS,
				DefaultPreferences.SERVER_CONNECTION_ADDRESS_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_SERVER_INITIAL_SPEED,
				DefaultPreferences.SERVER_INITIAL_SPEED_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_SERVER_OTHER_OPTIONS,
				DefaultPreferences.SERVER_OTHER_OPTIONS_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_CLIENT_COMMANDS,
				DefaultPreferences.CLIENT_COMMANDS_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_DO_INITIAL_RESET,
				DefaultPreferences.DO_INITIAL_RESET_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_STLINK_INITIAL_RESET_TYPE,
				DefaultPreferences.INITIAL_RESET_TYPE_DEFAULT);

		DefaultPreferences.putInt(PersistentPreferences.GDB_STLINK_INITIAL_RESET_SPEED,
				DefaultPreferences.INITIAL_RESET_SPEED_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_STLINK_SPEED, DefaultPreferences.STLINK_SPEED_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_ENABLE_FLASH_BREAKPOINTS,
				DefaultPreferences.ENABLE_FLASH_BREAKPOINTS_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_SEMIHOSTING_TELNET,
				DefaultPreferences.SEMIHOSTING_TELNET_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_SEMIHOSTING_CLIENT,
				DefaultPreferences.SEMIHOSTING_CLIENT_DEFAULT);

		DefaultPreferences.putInt(PersistentPreferences.GDB_STLINK_SWO_ENABLE_TARGET_CPU_FREQ,
				DefaultPreferences.SWO_ENABLE_TARGET_CPU_FREQ_DEFAULT);

		DefaultPreferences.putInt(PersistentPreferences.GDB_STLINK_SWO_ENABLE_TARGET_SWO_FREQ,
				DefaultPreferences.SWO_ENABLE_TARGET_SWO_FREQ_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_STLINK_SWO_ENABLE_TARGET_PORT_MASK,
				DefaultPreferences.SWO_ENABLE_TARGET_PORT_MASK_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_STLINK_INIT_OTHER, DefaultPreferences.INIT_OTHER_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_DO_DEBUG_IN_RAM,
				DefaultPreferences.DO_DEBUG_IN_RAM_DEFAULT);

		DefaultPreferences.putBoolean(PersistentPreferences.GDB_STLINK_DO_PRERUN_RESET,
				DefaultPreferences.DO_PRERUN_RESET_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_STLINK_PRERUN_RESET_TYPE,
				DefaultPreferences.PRERUN_RESET_TYPE_DEFAULT);

		DefaultPreferences.stlinkStorePutString(PersistentPreferences.GDB_STLINK_PRERUN_OTHER,
				DefaultPreferences.PRERUN_OTHER_DEFAULT);

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

			LlinkPlugin.log("LateInitializer.added() " + event + " " + event.getChild().name()); //$NON-NLS-1$ //$NON-NLS-2$

			if (LlinkPlugin.PLUGIN_ID.equals(event.getChild().name())) {

				finalizeInitializationsDefaultPreferences();

				// We're done, de-register listener.
				((IEclipsePreferences) (event.getSource())).removeNodeChangeListener(this);
			}
		}

		@Override
		public void removed(NodeChangeEvent event) {
			LlinkPlugin.log("LateInitializer.removed() " + event); //$NON-NLS-1$
		}

		/**
		 * The second step of defaults initialisation.
		 */
		public void finalizeInitializationsDefaultPreferences() {

			LlinkPlugin.log("LateInitializer.finalizeInitializationsDefaultPreferences()"); //$NON-NLS-1$
			
			// J-Link GDB Server executable name
			String name = DefaultPreferences.getExecutableName();
			if (name.isEmpty()) {
				// If not defined elsewhere, get platform specific name.
				name = DefaultPreferences.getExecutableNameOs();
				if (!name.isEmpty()) {
					DefaultPreferences.putExecutableName(name);
				}
			}

			String executableName = EclipseUtils.getVariableValue(VariableInitializer.VARIABLE_L_LINK_EXECUTABLE);
			if (executableName == null || executableName.isEmpty()) {
				executableName = DefaultPreferences.getExecutableName();
			}
			if (EclipseUtils.isWindows() && !executableName.endsWith(".exe")) { //$NON-NLS-1$
				executableName += ".exe"; //$NON-NLS-1$
			}

			// Check if the search path is defined in the default
			// preferences.
			String searchPath = DefaultPreferences.getSearchPath();
			if (searchPath.isEmpty()) {

				// If not defined, get the OS Specific default
				// from preferences.ini.
				searchPath = DefaultPreferences.getSearchPathOs();
				if (!searchPath.isEmpty()) {
					// Store the search path in the preferences
					DefaultPreferences.putSearchPath(searchPath);
				}
			}

			// J-Link GDB Server install folder
			// Check if the toolchain path is explictly defined in the
			// default preferences.
			String folder = DefaultPreferences.getInstallFolder();
			if (!folder.isEmpty()) {
				IPath path = (new Path(folder)).append(executableName);
				if (!path.toFile().isFile()) {
					// If the file does not exist, refuse the given folder
					// and prefer to search.
					folder = ""; //$NON-NLS-1$
				}
			}

			if (folder.isEmpty()) {
//@2017.4.11
//				// If the search path is known, discover tool chain.
//				folder = Discoverer.getRegistryInstallFolder(executableName, "bin", REG_SUBKEY, REG_NAME);

				folder = Discoverer.searchInstallFolder(executableName, searchPath, null);
			}

			if (folder != null && !folder.isEmpty()) {
				// If the install folder was finally discovered, store
				// it in the preferences.
				DefaultPreferences.putInstallFolder(folder);
			}
		}
	}

}
