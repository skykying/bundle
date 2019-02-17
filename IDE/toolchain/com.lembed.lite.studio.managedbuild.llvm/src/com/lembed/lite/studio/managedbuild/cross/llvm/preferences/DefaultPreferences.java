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
import com.lembed.lite.studio.managedbuild.cross.llvm.ui.ToolchainDefinition;

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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;


/**
 * The Class DefaultPreferences.
 */
@SuppressWarnings("javadoc")
public class DefaultPreferences {

    // ------------------------------------------------------------------------

    private static final String EXECUTABLE_NAME = "make.exe"; //$NON-NLS-1$

    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------

    /**
     * The DefaultScope preference store.
     */
    private static IEclipsePreferences fgPreferences;

    // ------------------------------------------------------------------------

    private static IEclipsePreferences getPreferences() {

        if (fgPreferences == null) {
            fgPreferences = DefaultScope.INSTANCE.getNode(LlvmUIPlugin.PLUGIN_ID);
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
                value = prop.getProperty(DEFAULT_NAME, "").trim(); //$NON-NLS-1$
            } catch (IOException e) {
                value = ""; //$NON-NLS-1$
            }
        }

       
        LlvmUIPlugin.log("getToolchainName()=\"" + value + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        return value;
    }

    public static void putToolchainName(String value) {

        String key = PersistentPreferences.TOOLCHAIN_NAME_KEY;
        
        LlvmUIPlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
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
                value = prop.getProperty(DEFAULT_PATH + "." + String.valueOf(hash), "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (IOException e) {
                value = ""; //$NON-NLS-1$
            }
        }
        LlvmUIPlugin.log("getToolchainPath()=\"" + value + "\" (" + key + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        return value;
    }

    public static void putToolchainPath(String toolchainName, String value) {

        String key = PersistentPreferences.getToolchainKey(toolchainName);
        LlvmUIPlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$

        putString(key, value);
    }

    // ------------------------------------------------------------------------

    /**
     * Get the default toolchain search path for a given toolchain name. Toolchains
     * are identified by their absolute hash code.
     * 
     * @param toolchainName
     *            a string.
     * @return a trimmed string, possibly empty.
     */
    public static String getToolchainSearchPath(String toolchainName) {

        String key = PersistentPreferences.getToolchainSearchKey(toolchainName);
        LlvmUIPlugin.log("Check " + key + " for \"" + toolchainName + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        String value = getString(PersistentPreferences.getToolchainSearchKey(toolchainName), ""); //$NON-NLS-1$

        return value;
    }

    public static void putToolchainSearchPath(String toolchainName, String value) {

        String key = PersistentPreferences.getToolchainSearchKey(toolchainName);
        LlvmUIPlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$

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

        String value = getString(PersistentPreferences.getToolchainSearchOsKey(toolchainName), ""); //$NON-NLS-1$

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
     * Get the default tool chain search path for a given tool chain name. Tool
     * chains are identified by their absolute hash code.
     * 
     * @param toolchainName
     *            a string.
     * @return a trimmed string, possibly empty.
     */
    public static String getBuildToolsSearchPath() {

        String key = PersistentPreferences.getBuildToolSearchKey();
        LlvmUIPlugin.log("Check " + key + " for \"" + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        String value = getString(PersistentPreferences.getBuildToolSearchKey(), ""); //$NON-NLS-1$

        return value;
    }

    public static void putBuildToolsSearchPath(String value) {

        String key = PersistentPreferences.getBuildToolSearchKey();
        
        LlvmUIPlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
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

        String value = getString(PersistentPreferences.getBuildToolSearchOsKey(), ""); //$NON-NLS-1$

        return value;
    }

    // ------------------------------------------------------------------------
    /**
     * Get the default value for the build tools path.
     * 
     * @return a trimmed string, possibly empty.
     */
    public static String getBuildToolsPath() {
        return getString(PersistentPreferences.getBuildToolsPathKey(), ""); //$NON-NLS-1$
    }

    public static void putBuildToolsPath(String value) {

        String key = PersistentPreferences.getBuildToolsPathKey();
        LlvmUIPlugin.log("Default " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
        putString(key, value);
    }

    /**
     * Find where the build tools might have been installed. The returned folder is
     * known to be an existing folder.
     * 
     * @return a trimmed string, possibly empty.
     */
    public static String discoverBuildToolsPath() {
        String value = null;

        String searchPath = getBundleBuildToolsPath(PersistentPreferences.getBuildToolsRelativePath());
        if (searchPath.isEmpty()) {
            searchPath = DefaultPreferences.getBuildToolsSearchPath();
        }

        if (searchPath.isEmpty()) {
            // If not defined, get the OS Specific default
            // from preferences.ini.
            searchPath = DefaultPreferences.getBuildToolSearchPathOs();
        }

        if (searchPath == null || searchPath.isEmpty()) {
            return null;
        }

        // Resolve ${user.home}
        String resolvedPath = searchPath;
        if (resolvedPath.indexOf("${user.home}") >= 0) { //$NON-NLS-1$
            resolvedPath = resolvedPath.replaceAll("\\$\\{user.home\\}", System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // If more macros remain, use the usual substituter.
        if (resolvedPath.indexOf("${") >= 0) { //$NON-NLS-1$
            IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
            try {
                resolvedPath = variableManager.performStringSubstitution(resolvedPath, false);
            } catch (CoreException e) {
                resolvedPath = null;
            }
        }

        if (resolvedPath == null || resolvedPath.isEmpty()) {
            return null;
        }

        // Split into multiple paths.
        String[] paths = resolvedPath.split(EclipseUtils.getPathSeparator());
        if (paths.length == 0) {
            return null;
        }

        // Try paths in order; return the first.
        for (int i = 0; i < paths.length; ++i) {
            value = getLastExecutable(paths[i], EXECUTABLE_NAME);
            LlvmUIPlugin.log(paths[i]);
            if (value != null && !value.isEmpty()) {
                LlvmUIPlugin.log(EXECUTABLE_NAME + paths[i] + value);
                return value;
            }
            LlvmUIPlugin.log(EXECUTABLE_NAME + paths[i] + "value is xx"); //$NON-NLS-1$
        }

        if (value != null) {
            value = value.trim();

            // Validate registry path. If folder does not exist, ignore.
            File file = new File(value);
            if (!file.isDirectory()) {
                value = ""; //$NON-NLS-1$
            } else {
                DefaultPreferences.putBuildToolsSearchPath(searchPath);
                LlvmUIPlugin.log("search path is " + searchPath); //$NON-NLS-1$
            }
        } else {
            value = ""; //$NON-NLS-1$
        }

        LlvmUIPlugin.log("DefaultPreferences.discoverBuildToolsPath()=\"" + value + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        return value;
    }

    public static String getBundleBuildToolsPath(String relPath) {
        String searchPath = ""; //$NON-NLS-1$
        URL path = Platform.getInstallLocation().getURL();
        LlvmUIPlugin.log(path.toString());

        File installFile = new File(path.getFile());
        LlvmUIPlugin.log(installFile.getPath());

        if (installFile.exists()) {
            File parentFile = installFile.getParentFile();
            if (parentFile.isDirectory()) {
                String vpath = parentFile.getPath() + File.separator + relPath;
                LlvmUIPlugin.log(vpath);
                File finalFile = new File(vpath);
                if (finalFile.exists()) {
                    searchPath = finalFile.getPath();
                    LlvmUIPlugin.log(searchPath);
                }
            }
        } else {
            LlvmUIPlugin.log("v"); //$NON-NLS-1$
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

        List<String> list = new ArrayList<>();
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
                IPath path = (new Path(dir.getAbsolutePath())).append(name).append("bin").append(executableName); //$NON-NLS-1$
                LlvmUIPlugin.log(path.toOSString());

                if (path.toFile().isFile()) {
                    LlvmUIPlugin.log("path is file"); //$NON-NLS-1$
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

        LlvmUIPlugin.log("last is " + last); //$NON-NLS-1$
        IPath path = (new Path(folder)).append(last).append("bin"); //$NON-NLS-1$
        LlvmUIPlugin.log(path.toOSString());
        LlvmUIPlugin.log(path.toString());
        return path.toString();
    }

    private static String getLastExecutable(String folder, final String executableName) {

        List<String> list = new ArrayList<>();
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
                IPath path = (new Path(dir.getAbsolutePath())).append(name).append("bin").append(executableName); //$NON-NLS-1$
                LlvmUIPlugin.log(path.toOSString());

                if (path.toFile().isFile()) {
                    LlvmUIPlugin.log("path is file"); //$NON-NLS-1$
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

        LlvmUIPlugin.log("last is " + last); //$NON-NLS-1$
        IPath path = (new Path(folder)).append(last).append("bin"); //$NON-NLS-1$
        LlvmUIPlugin.log(path.toOSString());
        LlvmUIPlugin.log(path.toString());
        return path.toString();
    }

    /**
     * Try to find a possible match for the toolchain, in the given path.
     * 
     * @param toolchainName
     * @param searchPath
     *            a string with a sequence of folders.
     * @return a String with the absolute folder path, or null if not found.
     */
    public static String discoverToolchainPath(String toolchainName, String searchPath) {

        if (searchPath == null || searchPath.isEmpty()) {
            return null;
        }

        // Resolve ${user.home}
        String resolvedPath = searchPath;
        if (resolvedPath.indexOf("${user.home}") >= 0) { //$NON-NLS-1$
            resolvedPath = resolvedPath.replaceAll("\\$\\{user.home\\}", System.getProperty("user.home")); //$NON-NLS-1$ //$NON-NLS-2$

        }

        // If more macros remain, use the usual substituter.
        if (resolvedPath.indexOf("${") >= 0) { //$NON-NLS-1$
            IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
            try {
                resolvedPath = variableManager.performStringSubstitution(resolvedPath, false);
            } catch (CoreException e) {
                resolvedPath = null;
            }
        }

        if (resolvedPath == null || resolvedPath.isEmpty()) {
            return null;
        }

        // Split into multiple paths.
        String[] paths = resolvedPath.split(EclipseUtils.getPathSeparator());
        if (paths.length == 0) {
            return null;
        }

        int ix;
        try {
            ix = ToolchainDefinition.findToolchainByName(toolchainName);
        } catch (IndexOutOfBoundsException e) {
            ix = ToolchainDefinition.getDefault();
        }

        String executableName = ToolchainDefinition.getToolchain(ix).getFullCmdC();
        if (EclipseUtils.isWindows() && !executableName.endsWith(".exe")) { //$NON-NLS-1$
            executableName += ".exe"; //$NON-NLS-1$
        }

        // Try paths in order; return the first.
        for (int i = 0; i < paths.length; ++i) {
            String value = getLastToolchain(paths[i], executableName);
            LlvmUIPlugin.log(paths[i]);
            if (value != null && !value.isEmpty()) {
                LlvmUIPlugin.log(executableName + paths[i] + value);
                return value;
            }
            LlvmUIPlugin.log(executableName + paths[i] + "value is x"); //$NON-NLS-1$
        }

        return null;
    }

    // ------------------------------------------------------------------------

    // TODO: remove DEPRECATED
    public static final String DEFAULT_NAME = "default.name"; //$NON-NLS-1$
    public static final String DEFAULT_PATH = "default.path"; //$NON-NLS-1$
    public static final String TOOLCHAIN = "toolchains.prefs"; //$NON-NLS-1$
    private static Properties fgToolchainProperties;

    // ------------------------------------------------------------------------

    // TODO: remove DEPRECATED
    // Non-standard location:
    // eclipse/configuration/com.lembed.lite.studio.managedbuild.cross/toolchain.prefs/name=value
    private static Properties getToolchainProperties() throws IOException {

        if (fgToolchainProperties == null) {

            URL url = Platform.getInstallLocation().getURL();

            IPath path = new Path(url.getPath());
            File file = path.append("configuration").append(LlvmUIPlugin.PLUGIN_ID).append(TOOLCHAIN).toFile(); //$NON-NLS-1$

            try (InputStream is = new FileInputStream(file)) {
                Properties prop = new Properties();
                prop.load(is);
                fgToolchainProperties = prop;
            }

        }

        return fgToolchainProperties;
    }

}
