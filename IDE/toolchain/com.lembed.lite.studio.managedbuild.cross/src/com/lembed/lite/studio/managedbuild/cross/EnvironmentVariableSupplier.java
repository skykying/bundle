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
package com.lembed.lite.studio.managedbuild.cross;

import com.lembed.lite.studio.core.EclipseUtils;

import java.io.File;

import org.eclipse.cdt.core.cdtvariables.CdtVariableException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.cdt.managedbuilder.envvar.IBuildEnvironmentVariable;
import org.eclipse.cdt.managedbuilder.envvar.IConfigurationEnvironmentVariableSupplier;
import org.eclipse.cdt.managedbuilder.envvar.IEnvironmentVariableProvider;
import org.eclipse.cdt.managedbuilder.macros.IBuildMacroProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import com.lembed.lite.studio.managedbuild.cross.ui.PersistentPreferences;
import com.lembed.lite.studio.managedbuild.cross.ui.ProjectStorage;

/**
 * The Class EnvironmentVariableSupplier.
 */
public class EnvironmentVariableSupplier implements IConfigurationEnvironmentVariableSupplier {

	@Override
    public IBuildEnvironmentVariable getVariable(String variableName, IConfiguration configuration,
			IEnvironmentVariableProvider provider) {
		if (PathEnvironmentVariable.isVar(variableName)) {
			return PathEnvironmentVariable.create(configuration);
		}
        CrossGccPlugin.log("getVariable(" + variableName +"" + configuration.getName()+""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return null;
	}

	@Override
    public IBuildEnvironmentVariable[] getVariables(IConfiguration configuration,
			IEnvironmentVariableProvider provider) {
		IBuildEnvironmentVariable path = PathEnvironmentVariable.create(configuration);
		if (path != null) {
			return new IBuildEnvironmentVariable[] { path };
		}
        CrossGccPlugin.log("getVariables(" + configuration.getName() + ") returns empty array"); //$NON-NLS-1$ //$NON-NLS-2$
        return new IBuildEnvironmentVariable[0];
	}

	private static class PathEnvironmentVariable implements IBuildEnvironmentVariable {

		public static String name = "PATH"; //$NON-NLS-1$
		private File path;

		private PathEnvironmentVariable(File path) {
			CrossGccPlugin.log("cpath=" + path); //$NON-NLS-1$
			this.path = path;
		}

		public static PathEnvironmentVariable create(IConfiguration configuration) {
			IToolChain toolchain = configuration.getToolChain();
			IProject project = (IProject) configuration.getManagedProject().getOwner();
			String path = PersistentPreferences.getBuildToolsPath(project);

			IOption option;
			option = toolchain.getOptionBySuperClassId(Option.OPTION_TOOLCHAIN_NAME); // $NON-NLS-1$
			String toolchainName = (String) option.getValue();

			String toolchainPath = null;

			{
				// TODO: remove DEPRECATED

				// Warning: since multiple per configuration paths are copied
				// into a single per project path, in case the configuration
				// paths were different, each usage will override the previous
				// values.
				boolean isPathPerProject = ProjectStorage.isToolchainPathPerProject(configuration);

				if (isPathPerProject) {
					// Get per configuration path
					toolchainPath = ProjectStorage.getToolchainPath(configuration);

					// Copy the toolchain path from the wrong storage to project
					// preferences.
					PersistentPreferences.putToolchainPath(toolchainName, toolchainPath, project);

					// Disable flag
					ProjectStorage.putToolchainPathPerProject(configuration, false);

					
					CrossGccPlugin.log("Path \"" + toolchainPath + "\" copied to project " + project.getName());					 //$NON-NLS-1$ //$NON-NLS-2$
				}
			}

			// Get the most specific toolchain path (project, workspace,
			// Eclipse, defaults).
			toolchainPath = PersistentPreferences.getToolchainPath(toolchainName, project);

			if (path.isEmpty()) {
				path = toolchainPath;
			} else {
				if (!toolchainPath.isEmpty()) {
					// Concatenate build tools path with toolchain path.
					path += EclipseUtils.getPathSeparator();
					path += toolchainPath;
				}
			}

			if (!path.isEmpty()) {

				// if present, substitute macros
				if (path.indexOf("${") >= 0) { //$NON-NLS-1$
					path = resolveMacros(path, configuration);
				}

				File sysroot = new File(path);
				File bin = new File(sysroot, "bin"); //$NON-NLS-1$
				if (bin.isDirectory()) {
					sysroot = bin;
				}

				//if (false) {					
				//	ToolchainPlugin.log("PATH=" + sysroot + " opt=" + path + " cfg=" + configuration + " prj="
				//				+ configuration.getManagedProject().getOwner().getName());					
				//}
				return new PathEnvironmentVariable(sysroot);
			}

			CrossGccPlugin.log("create(" + configuration.getName() + ") returns null"); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}

		private static String resolveMacros(String str, IConfiguration configuration) {

			String result = str;
			try {
				result = ManagedBuildManager.getBuildMacroProvider().resolveValue(str, "", " ", //$NON-NLS-1$ //$NON-NLS-2$
						IBuildMacroProvider.CONTEXT_CONFIGURATION, configuration); // $NON-NLS-1$
																					// //$NON-NLS-2$
			} catch (CdtVariableException e) {
				CrossGccPlugin.log("resolveMacros " + e.getMessage()); //$NON-NLS-1$
			}

			return result;
		}

		public static boolean isVar(String varName) {
			// Windows has case insensitive env var names
		    boolean isWin32 =  Platform.getOS().equals(Platform.OS_WIN32);
		    if(isWin32) {
		        return varName.equalsIgnoreCase(PathEnvironmentVariable.name);
		    }
		    return varName.equals(PathEnvironmentVariable.name);
		}

		@Override
        public String getDelimiter() {
			return Platform.getOS().equals(Platform.OS_WIN32) ? ";" : ":"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
        public String getName() {
			return name;
		}

		@Override
        public int getOperation() {
			return IBuildEnvironmentVariable.ENVVAR_PREPEND;
		}

		@Override
        public String getValue() {
			return path.getAbsolutePath();
		}

	}
	
}
