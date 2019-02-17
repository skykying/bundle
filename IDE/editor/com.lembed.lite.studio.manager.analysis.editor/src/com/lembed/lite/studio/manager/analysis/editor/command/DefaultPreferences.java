/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.lembed.lite.studio.managedbuild.cross.CrossGccPlugin;

public class DefaultPreferences {

	/**
	 * The DefaultScope preference store.
	 */
	private static IEclipsePreferences fgPreferences;

	// ------------------------------------------------------------------------

	private static IEclipsePreferences getPreferences() {

		if (fgPreferences == null) {
			fgPreferences = DefaultScope.INSTANCE.getNode(CrossGccPlugin.PLUGIN_ID);
		}

		return fgPreferences;
	}

	/**
	 * Get a string preference value, or the default.
	 * 
	 * @param key
	 *            a string with the key to search.
	 * @param defaulValue
	 *            a string with the default, possibly null.
	 * @return a trimmed string, or a null default.
	 */
	public static String getString(String key, String defaulValue) {

		String value;
		value = getPreferences().get(key, defaulValue);

		if (value != null) {
			value = value.trim();
		}

		return value;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return getPreferences().getBoolean(key, defaultValue);
	}

	private static void putString(String key, String value) {
		getPreferences().put(key, value);
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the default toolchain name.
	 * 
	 * @return a trimmed string, possibly empty.
	 */
	public static String getToolchainName() {

		String key = PersistentPreferences.TOOLCHAIN_NAME_KEY;
		String value = getString(key, null);
		if (value == null) {

			// TODO: remove DEPRECATED
			try {
				Properties prop = getToolchainProperties();
				value = prop.getProperty(DEFAULT_NAME, "").trim();
			} catch (IOException e) {
				value = "";
			}
		}

		CrossGccPlugin.log("getToolchainName()=\"" + value + "\"");
		return value;
	}

	public static void putToolchainName(String value) {

		String key = PersistentPreferences.TOOLCHAIN_NAME_KEY;
		
		CrossGccPlugin.log("Default " + key + "=" + value);
		putString(key, value);
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the default toolchain path for a given toolchain name. Toolchains are
	 * identified by their absolute hash code.
	 * 
	 * @param toolchainName
	 *            a string.
	 * @return a trimmed string, possibly empty.
	 */
	public static String getToolchainPath(String toolchainName) {

		String key = PersistentPreferences.getToolchainKey(toolchainName);
		String value = getString(key, null);
		if (value == null) {

			// TODO: remove DEPRECATED
			try {
				Properties prop = getToolchainProperties();
				int hash = Math.abs(toolchainName.trim().hashCode());
				value = prop.getProperty(DEFAULT_PATH + "." + String.valueOf(hash), "").trim();
			} catch (IOException e) {
				value = "";
			}
		}
		log("getToolchainPath()=\"" + value + "\" (" + key + ")");
		
		return value;
	}

	public static void putToolchainPath(String toolchainName, String value) {

		String key = PersistentPreferences.getToolchainKey(toolchainName);
		log("Default " + key + "=" + value);
		
		putString(key, value);
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the default toolchain search path for a given toolchain name.
	 * Toolchains are identified by their absolute hash code.
	 * 
	 * @param toolchainName
	 *            a string.
	 * @return a trimmed string, possibly empty.
	 */
	public static String getToolchainSearchPath(String toolchainName) {

		String key = PersistentPreferences.getToolchainSearchKey(toolchainName);
		log("Check " + key + " for \"" + toolchainName + "\"");
		
		String value = getString(PersistentPreferences.getToolchainSearchKey(toolchainName), "");

		return value;
	}

	public static void putToolchainSearchPath(String toolchainName, String value) {

		String key = PersistentPreferences.getToolchainSearchKey(toolchainName);
		log("Default " + key + "=" + value);
		
		putString(key, value);
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the default toolchain search path for a given toolchain name and the
	 * current OS family. Toolchains are identified by their absolute hash code.
	 * 
	 * @param toolchainName
	 *            a string.
	 * @return a trimmed string, possibly empty.
	 */
	public static String getToolchainSearchPathOs(String toolchainName) {

		String value = getString(PersistentPreferences.getToolchainSearchOsKey(toolchainName), "");

		return value;
	}

	// ------------------------------------------------------------------------
	/**
	 * get the eclipse installed path
	 * 
	 * @return installed path or null
	 */
	public static String getEclipseInstalledPath() {

		URL path = Platform.getInstallLocation().getURL();
		
		return path.toString();
	}

		// ------------------------------------------------------------------------

	/**
	 * Get the default tool chain search path for a given tool chain name.
	 * Tool chains are identified by their absolute hash code.
	 * 
	 * @param toolchainName
	 *            a string.
	 * @return a trimmed string, possibly empty.
	 */
	public static String getBuildToolsSearchPath() {

		String key = PersistentPreferences.getBuildToolSearchKey();		
		log("Check " + key + " for \"" + "\"");
		
		String value = getString(PersistentPreferences.getBuildToolSearchKey(), "");

		return value;
	}

	public static void putBuildToolsSearchPath(String value) {

		String key = PersistentPreferences.getBuildToolSearchKey();
		
		CrossGccPlugin.log("Default " + key + "=" + value);
		putString(key, value);
	}

	// ------------------------------------------------------------------------

	/**
	 * Get the default toolchain search path for a given toolchain name and the
	 * current OS family. Toolchains are identified by their absolute hash code.
	 * 
	 * @param toolchainName
	 *            a string.
	 * @return a trimmed string, possibly empty.
	 */
	public static String getBuildToolSearchPathOs() {

		String value = getString(PersistentPreferences.getBuildToolSearchOsKey(), "");

		return value;
	}

	// ------------------------------------------------------------------------
	/**
	 * Get the default value for the build tools path.
	 * 
	 * @return a trimmed string, possibly empty.
	 */
	public static String getBuildToolsPath() {
		return getString(PersistentPreferences.getBuildToolsPathKey(), "");
	}

	public static void putBuildToolsPath(String value) {

		String key = PersistentPreferences.getBuildToolsPathKey();
		log("Default " + key + "=" + value);
		putString(key, value);
	}

	
	public static  String getBundleBuildToolsPath(String relPath) {
		String searchPath = "";
		URL path = Platform.getInstallLocation().getURL();
		log(path.toString());
		
		File installFile = new File(path.getFile());
		log(installFile.getPath());
		
		if(installFile.exists()) {			
			File parentFile = installFile.getParentFile();
			if (parentFile.isDirectory()) {
				String vpath = parentFile.getPath() + File.separator + relPath;
				log(vpath);
				File finalFile = new File(vpath);
				if(finalFile.exists()){
					searchPath = finalFile.getPath();
					log(searchPath);
				}				
			}
		}else{
			log("v");
		}

		return searchPath;
	}
	
	/**
	 * Return the last (in lexicographical order) folder that contain
	 * "bin/executable".
	 * 
	 * @param folder
	 * @param executableName
	 * @return a String with the folder absolute path, or null if not found.
	 */
	private static String getLastToolchain(String folder, final String executableName) {

		List<String> list = new ArrayList<String>();
		File local = new File(folder);
		if (!local.isDirectory()) {
			// System.out.println(folder + " not a folder");
			return null;
		}

		File[] files = local.listFiles(new FilenameFilter() {

			/**
			 * Filter to select only
			 */
			@Override
			public boolean accept(File dir, String name) {
				IPath path = (new Path(dir.getAbsolutePath())).append(name).append("bin").append(executableName);
				log(path.toOSString());

				if (path.toFile().isFile()) {
					log("path is file");
					return true;
				}
				return false;
			}
		});

		if (files == null || files.length == 0) {
			return null;
		}

		for (int i = 0; i < files.length; ++i) {
			list.add(files[i].getName());
		}

		// The sort criteria is the lexicographical order on folder name.
		Collections.sort(list);

		// Get the last name in ordered list.
		String last = list.get(list.size() - 1);

		log("last is "+last);
		IPath path = (new Path(folder)).append(last).append("bin");
		log(path.toOSString());
		log(path.toString());
		return path.toString();
	}

	private static String getLastExecutable(String folder, final String executableName) {

		List<String> list = new ArrayList<String>();
		File local = new File(folder);
		if (!local.isDirectory()) {
			// System.out.println(folder + " not a folder");
			return null;
		}

		File[] files = local.listFiles(new FilenameFilter() {

			/**
			 * Filter to select only
			 */
			@Override
			public boolean accept(File dir, String name) {
				IPath path = (new Path(dir.getAbsolutePath())).append(name).append("bin").append(executableName);
				log(path.toOSString());

				if (path.toFile().isFile()) {
					log("path is file");
					return true;
				}
				return false;
			}
		});

		if (files == null || files.length == 0) {
			return null;
		}

		for (int i = 0; i < files.length; ++i) {
			list.add(files[i].getName());
		}

		// The sort criteria is the lexicographical order on folder name.
		Collections.sort(list);

		// Get the last name in ordered list.
		String last = list.get(list.size() - 1);

		log("last is "+last);
		IPath path = (new Path(folder)).append(last).append("bin");
		log(path.toOSString());
		log(path.toString());
		return path.toString();
	}

	// ------------------------------------------------------------------------


	// TODO: remove DEPRECATED
	public static final String DEFAULT_NAME = "default.name";
	public static final String DEFAULT_PATH = "default.path";
	public static final String TOOLCHAIN = "toolchains.prefs";
	private static Properties fgToolchainProperties;

	// ------------------------------------------------------------------------

	// TODO: remove DEPRECATED
	// Non-standard location:
	// eclipse/configuration/com.lembed.lite.studio.managedbuild.cross/toolchain.prefs/name=value

	private static Properties getToolchainProperties() throws IOException {

		if (fgToolchainProperties == null) {

			URL url = Platform.getInstallLocation().getURL();

			IPath path = new Path(url.getPath());
			File file = path.append("configuration").append(CrossGccPlugin.PLUGIN_ID).append(TOOLCHAIN).toFile();
			InputStream is = new FileInputStream(file);

			Properties prop = new Properties();
			prop.load(is);

			fgToolchainProperties = prop;
		}

		return fgToolchainProperties;
	}

	private static void log(String msg){
		CrossGccPlugin.log(">>>>< " + msg +"\n"); 
	}
	// ------------------------------------------------------------------------

}
