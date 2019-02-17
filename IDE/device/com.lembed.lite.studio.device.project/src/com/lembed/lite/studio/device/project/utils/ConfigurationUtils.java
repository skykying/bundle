/*******************************************************************************
 * Copyright (c) 2017 LEMBED
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package com.lembed.lite.studio.device.project.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.configuration.LiteConfiguration;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpEnvironmentProvider;
import com.lembed.lite.studio.device.core.build.IBuildSettings;
import com.lembed.lite.studio.device.core.build.IMemorySettings;
import com.lembed.lite.studio.device.core.build.MemorySettings;
import com.lembed.lite.studio.device.core.data.ICpDebugConfiguration;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpFile;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpMemory;
import com.lembed.lite.studio.device.core.enums.EFileCategory;
import com.lembed.lite.studio.device.core.enums.EFileRole;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpFileInfo;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.project.CpProjectPlugIn;
import com.lembed.lite.studio.device.project.CpVariableResolver;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.project.impl.LiteProjectStorage;
import com.lembed.lite.studio.device.toolchain.ILinkerScriptGenerator;
import com.lembed.lite.studio.device.toolchain.ILiteToolChainAdapter;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;

public class ConfigurationUtils {

	public static String getFileVersion(ILiteProject liteProject, String projectRelativePath) {
		LiteProjectStorage projectStorage = liteProject.getProjectStorage();
		return projectStorage.getConfigFileVersion(projectRelativePath);
	}

	public static BufferPrinter generateConfigurationFile(ILiteProject liteProject) throws CoreException, IOException {
		IProject project = liteProject.getProject();
		if (project == null) {
			return null;
		}
		BufferPrinter pw = new BufferPrinter();
		try {
			writeConfigurationFileHead(liteProject, pw);
			writeConfigurationFileBody(liteProject, pw);
			writeConfigurationFileTail(liteProject, pw);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pw;
	}

	protected static void writeConfigurationFileBody(ILiteProject liteProject, BufferPrinter pw) throws IOException {
		ILiteConfiguration liteConf = liteProject.getLiteConfiguration();
		if (liteConf == null) {
			return;
		}

		// write #define CMSIS_device_header
		String deviceHeader = liteConf.getDeviceHeader();
		if (deviceHeader != null && !deviceHeader.isEmpty()) {
			String s = "#define " + CmsisConstants.CMSIS_device_header + " \"" + deviceHeader + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			pw.println("/*"); //$NON-NLS-1$
			pw.println(" * Define the Device Header File:"); //$NON-NLS-1$
			pw.println("*/"); //$NON-NLS-1$
			pw.println(s);
			pw.println();
		}

		Collection<String> code = liteConf.getLiteComponentsHCode();
		for (String s : code) {
			pw.println(s);
		}
	}

	protected static void writeConfigurationFileHead(ILiteProject liteProject, BufferPrinter pw) throws IOException {

		pw.println("/*"); //$NON-NLS-1$
		pw.println(" * Auto generated Device Component Configuration File"); //$NON-NLS-1$
		pw.println(" *      *** Do not modify ! ***"); //$NON-NLS-1$
		pw.println(" *"); //$NON-NLS-1$
		pw.println(" * Project: " + liteProject.getName()); //$NON-NLS-1$
		pw.print(" * project configuration: "); //$NON-NLS-1$
		pw.print(" *       Device : " + liteProject.getLiteConfiguration().getDeviceInfo().getDeviceName());
		pw.print(" *       Description : " + liteProject.getLiteConfiguration().getDeviceInfo().getDescription());
		pw.println("*/"); //$NON-NLS-1$

		pw.println("#ifndef __LITE_COMPONENTS_H__"); //$NON-NLS-1$
		pw.println("#define __LITE_COMPONENTS_H__"); //$NON-NLS-1$
		pw.println();
	}

	protected static void writeConfigurationFileTail(ILiteProject liteProject, BufferPrinter pw) throws IOException {
		pw.println();
		pw.println("#endif /* __LITE_COMPONENTS_H__ */"); //$NON-NLS-1$
	}

	public static void writeLinkerScriptFile(IProject project, String fileName, String script,
	        IProgressMonitor monitor) {
		if (script == null || script.isEmpty()) {
			return;
		}

		try {
			IFile file = ProjectUtils.createFile(project, fileName, monitor);
			IPath loc = file.getLocation();
			File f = loc.toFile();
			if (f != null && f.exists()) {
				// destination file already exists
				return;
			}

			String osPath = loc.toOSString();
			PrintWriter pw = new PrintWriter(osPath);
			pw.println("MEMORY");
			pw.println("{");
			pw.println();
			pw.write(script);
			pw.println("}");
			pw.close();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void resetBuildSettings(ILiteProject liteProject, IProgressMonitor monitor) {
		LiteProjectStorage liteStorage = liteProject.getProjectStorage();
		if (liteStorage == null) {
			return;
		}

		ILiteToolChainAdapter adapter = liteStorage.getToolChainAdapter();
		if (adapter == null) {
			return;
		}

		IProject project = liteProject.getProject();
		if (project == null) {
			return;
		}

		ILiteConfiguration liteConfig = liteProject.getLiteConfiguration();
		IBuildSettings buildSettings = liteConfig.getBuildSettings();
		ICpConfigurationInfo info = liteConfig.getConfigurationInfo();
		if (info != null) {
			liteConfig.setConfigurationInfo(info);
			buildSettings = liteConfig.getBuildSettings();
		}


		/**
		 * the environment provider will overwrite the configuration info of
		 * project
		 */
		ICpEnvironmentProvider envProvider = CpPlugIn.getEnvironmentProvider();
		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);

		IConfiguration[] projectConfigs = buildInfo.getManagedProject().getConfigurations();
		for (IConfiguration cfg : projectConfigs) {

			envProvider.adjustBuildSettings(buildSettings, info);
			try {
				adapter.resetToolChainOptions(cfg, buildSettings);
			} catch (BuildException e) {
				e.printStackTrace();
			}

		}

//		String[] configNames = buildInfo.getConfigurationNames();
//		for (String name : configNames) {
//			IConfiguration config = ProjectUtils.getConfiguration(project, name);
//
//			envProvider.adjustBuildSettings(buildSettings, configInfo);
//			try {
//				adapter.resetToolChainOptions(config, buildSettings);
//			} catch (BuildException e) {
//				e.printStackTrace();
//			}
//
//		}
		/**
		 * save the configuration info to project
		 */
		Boolean forceOverWrite = true;
		ManagedBuildManager.saveBuildInfo(project, forceOverWrite);
	}

	public static void applyBuildSettings(ILiteProject liteProject, boolean bForceUpdateToolchain,
	                                      IProgressMonitor monitor) {
		LiteProjectStorage liteStorage = liteProject.getProjectStorage();
		if (liteStorage == null) {
			return;
		}

		ILiteToolChainAdapter adapter = liteStorage.getToolChainAdapter();
		if (adapter == null) {
			return;
		}

		IProject project = liteProject.getProject();
		if (project == null) {
			return;
		}

		ILiteConfiguration liteConfig = liteProject.getLiteConfiguration();
		IBuildSettings buildSettings = liteConfig.getBuildSettings();

		Map<String, String> symbs = liteConfig.getSymbols();
		applyToolchainSymbols(symbs, buildSettings);

		ICpDeviceInfo deviceInfo = liteConfig.getDeviceInfo();
		if (deviceInfo != null) {
			liteStorage.setDeviceInfo(deviceInfo);

			/**
			 * the linker script found in CMSIS pack, add by LiteConfiguration
			 * class which look up and add the pack linker file path to build
			 * setting
			 */
			String linkerScriptFile = buildSettings.getSingleLinkerScriptFile();

			/**
			 * if the LiteConfiguration can not found linker script file, get
			 * the linker file from tool chain adapter
			 */
			if (linkerScriptFile == null) {
				ILinkerScriptGenerator linkerGenerator = adapter.getLinkerScriptGenerator();
				if (linkerGenerator != null) {
					try {

						/**
						 * get the memory info from ICpDeviceInfo, and create
						 * the memory layout information.
						 */
						IMemorySettings memorySettings = createMemorySettings(deviceInfo);

						/**
						 * LiteToolChainAdapter will generator the memory part
						 * of linker script
						 */
						String script = linkerGenerator.generate(memorySettings);
						if (script != null && !script.isEmpty()) {

							/**
							 * the tool chain adapter fails, the last step is to
							 * get the linker script file from local project, if
							 * it not exists, just touch one.
							 */
							IPath linkerPath = getLinkerScriptFile(liteProject, linkerGenerator);
							if (linkerPath == null) {
								log(" ++ linkerScriptFile null");
								return;
							}

							linkerScriptFile = linkerPath.toString();
							log(" ++ linkerScriptFile " + linkerScriptFile);

							/**
							 * print the script content to linker script file
							 */
							ConfigurationUtils.writeLinkerScriptFile(project, linkerScriptFile, script, monitor);

							/**
							 * update the build setting, add the new touched
							 * linker script file to build option
							 */
							// String linkerScript =
							// CmsisConstants.PROJECT_LOCAL_PATH +
							// linkerScriptFile;
							// buildSettings.addStringListValue(IBuildSettings.LITE_LINKER_SCRIPT,linkerScript);
						}
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * the environment provider will overwrite the configuration info of
		 * project
		 */
		ICpEnvironmentProvider envProvider = CpPlugIn.getEnvironmentProvider();
		ICpConfigurationInfo configInfo = liteConfig.getConfigurationInfo();

		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		String[] configNames = buildInfo.getConfigurationNames();
		for (String name : configNames) {
			IConfiguration config = ProjectUtils.getConfiguration(project, name);

			/**
			 * this is the tool chain initialization
			 */
			if (bForceUpdateToolchain) {
				envProvider.adjustInitialBuildSettings(buildSettings, configInfo);
				adapter.setInitialToolChainOptions(config, buildSettings);
			} else {
				envProvider.adjustBuildSettings(buildSettings, configInfo);
				adapter.setToolChainOptions(config, buildSettings);
			}
		}
		/**
		 * save the configuration info to project
		 */
		Boolean forceOverWrite = true;
		ManagedBuildManager.saveBuildInfo(project, forceOverWrite);
	}

	/**
	 * get the linker script file if it exists, other wise touch a new one the
	 * script file path will be device name as it's name, extension is provider
	 * by tool chain adapter.
	 *
	 * @param liteProject
	 *            ILiteProject
	 * @param lsGen
	 *            ILinkerScriptGenerator
	 * @return LinkerScript file path
	 */
	protected static IPath getLinkerScriptFile(ILiteProject liteProject, ILinkerScriptGenerator linkerGenerator) {
		IProject project = liteProject.getProject();
		IPath res = null;

		List<String> dirs = collectBuildToolLinkerPaths(project);
		for (String d : dirs) {
			String directory = d.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
			directory = directory.replaceFirst("../", "");//$NON-NLS-1$ //$NON-NLS-2$

			List<String> files = searchBuildToolLinkerFiles(project);
			for (String file : files) {
				file = file.replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$
				IFile scriptFile = project.getFile(directory + "/" + file);

				if (!scriptFile.exists()) {
					continue;
				} else {
					try {
						File ff = new File(scriptFile.getLocation().toOSString());
						if (!ff.exists()) {
							log(" ++ script error " + ff.getPath());
							continue;
						}

						FileReader fr = new FileReader(ff);
						BufferedReader br = new BufferedReader(fr);
						String s;
						while ((s = br.readLine()) != null) {
							if (s.contains("MEMORY")) { //$NON-NLS-1$
								res = scriptFile.getProjectRelativePath();
								break;
							}
						}
						fr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (res != null) {
					return res;
				}
			}
		}
		return res;
	}

	public static void updateIndex(ILiteProject liteProject) {
		liteProject.setUpdateCompleted(true);

		IProject project = liteProject.getProject();
		CpProjectPlugIn.getLiteProjectManager().updateIndex(project);
	}

	public static void applyOptionsToToolchain(ILiteProject liteProject, Map<String, EFileCategory> fExtPaths,
	        IBuildSettings liteBuildSettings) {
		if (fExtPaths == null || fExtPaths.isEmpty()) {
			return;
		}

		for (String path : fExtPaths.keySet()) {
			EFileCategory cat = fExtPaths.get(path);

			path = adjustProjectPath(liteProject, path);

			switch (cat) {
			case HEADER:
			case INCLUDE:
				liteBuildSettings.addStringListValue(IBuildSettings.LITE_INCLUDE_PATH, path);
				break;
			case IMAGE:
				break;
			case LIBRARY:
				liteBuildSettings.addStringListValue(IBuildSettings.LITE_LIBRARIES, path);
				break;
			case LIBRARY2:
				liteBuildSettings.addStringListValue(IBuildSettings.LITE_LIBRARY_PATHS, path);
				break;
			case LINKER_SCRIPT:
				// remove directory path, only add linker script file name to build settings
				String filePath = ProjectUtils.removeLastPathSegment(path);
				path = path.replace(filePath, CmsisConstants.EMPTY_STRING);
				path =  path.replace("/", CmsisConstants.EMPTY_STRING); //$NON-NLS-1$
				liteBuildSettings.addStringListValue(IBuildSettings.LITE_LINKER_SCRIPT, path);
				break;
			case OBJECT:
				// ? some objects
				liteBuildSettings.addStringListValue(IBuildSettings.LITE_OBJECTS, path);
				break;
			case OTHER:
				break;
			case SOURCE:
				break;
			case SOURCE_ASM:
				break;
			case SOURCE_C:
				break;
			case SOURCE_CPP:
				break;
			case UTILITY:
				break;
			case SVD:
				break;
			default:
				break;
			}
		}

	}

	public static ILiteConfiguration updateConfiguration(ICpConfigurationInfo info) {
		if (info != null) {
			ILiteConfiguration liteConf = new LiteConfiguration();
			liteConf.setConfigurationInfo(info);
			return liteConf;
		} else {
			return null;
		}
	}

	/**
	 * Removes resources that are no longer belong to project and refreshes
	 * remaining ones, the resource all in LITE directory
	 *
	 * @throws CoreException
	 */
	public static void removeResources(ILiteProject liteProject, Boolean force, IProgressMonitor monitor)
	throws CoreException {
		ILiteConfiguration liteConf = liteProject.getLiteConfiguration();
		if (liteConf == null) {
			return;
		}

		IProject project = liteProject.getProject();
		IPath relativePath = project.getProjectRelativePath();

		Map<String, ICpFileInfo> fileMap = liteConf.getProjectSourceFiles();
		for (Entry<String, ICpFileInfo> e : fileMap.entrySet()) {
			String projectRelativePath = e.getKey();

			String filePath = relativePath + projectRelativePath;
			log("REMOVE RESOURCE......>  " + filePath + "  " + new Throwable().getStackTrace()[0].getLineNumber());

			filePath = adjustProjectPath(liteProject, filePath);
			removeFiles(liteProject, filePath, monitor);
		}

		Map<String, String> headerMap = liteConf.getProjectIncludeFiles();
		for (Entry<String, String> e : headerMap.entrySet()) {
			String dstPath = e.getValue();
			log("REMOVE RESOURCE......>  " + dstPath + "  " + new Throwable().getStackTrace()[0].getLineNumber());

			dstPath = adjustProjectPath(liteProject, dstPath);
			removeIncludes(liteProject, dstPath, monitor);
		}

	}

	protected static void removeIncludes(ILiteProject liteProject, String filePath, IProgressMonitor monitor)
	throws CoreException {
		IProject project = liteProject.getProject();
		if ((filePath == null) || (project == null)) {
			return;
		}

		IFolder res = project.getFolder(filePath);
		if (res.exists()) {
			res.delete(true, monitor);
		}

		return;
	}

	protected static void removeFiles(ILiteProject liteProject, String filePath, IProgressMonitor monitor)
	throws CoreException {
		IProject project = liteProject.getProject();
		if ((filePath == null) || (project == null)) {
			return;
		}

		IContainer parent = null;
		IFile res = project.getFile(filePath);
		int type = res.getType();

		// header file , source file, linker file, library file
		if (type == IResource.FILE) {
			IPath path = res.getProjectRelativePath();
			String dstFile = path.toString();

			if (ProjectUtils.isExcludedFromBuild(project, dstFile)) {
				//ProjectUtils.setExcludeFromBuild(project, dstFile, false);
			} else {
				if (res.isLinked()) {
					res.delete(IResource.FORCE, monitor);
				} else {
					res.delete(IResource.FORCE | IResource.KEEP_HISTORY, monitor);
				}
			}
			parent = res.getParent();
			while ((parent != null) && (parent.getType() == IResource.FOLDER)) {
				if (!parent.exists()) {
					return;
				}
				IResource[] members = parent.members();
				if (members.length == 0) {
					parent.delete(true, monitor);
				}
				parent = parent.getParent();
				if (parent.getName().equalsIgnoreCase(CmsisConstants.LITE)) {
					return;
				}
			}
		} else if (res.getType() == IResource.FOLDER) {
			IFolder f = (IFolder) res;
			IResource[] members = f.members();
			if (members.length == 0) {
				f.delete(true, true, monitor);
			} else {
				for (IResource r : members) {
					removeFiles(liteProject, r.getLocation().toOSString(), monitor);
				}
				f.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			}
		}
	}

	protected static void removeResources(ILiteProject liteProject, IResource res, Boolean force,
	                                      IProgressMonitor monitor) throws CoreException {
		IProject project = liteProject.getProject();
		if ((res == null) || (project == null)) {
			return;
		}

		int type = res.getType();
		// header file , source file, linker file, library file
		if (type == IResource.FILE) {
			IPath path = res.getProjectRelativePath();
			String dstFile = path.toString();
			if (!isFileUsed(liteProject, res)) {
				if (res.isLinked()) {
					res.delete(IResource.FORCE, monitor);
				} else if (force) {
					res.delete(IResource.FORCE | IResource.KEEP_HISTORY, monitor);
				} else {
					ProjectUtils.setExcludeFromBuild(project, dstFile, true);
				}
			} else if (ProjectUtils.isExcludedFromBuild(project, dstFile)) {
				ProjectUtils.setExcludeFromBuild(project, dstFile, false);
			}
		} else if (res.getType() == IResource.FOLDER) {
			IFolder f = (IFolder) res;
			IResource[] members = f.members();
			for (IResource r : members) {
				removeResources(liteProject, r, force, monitor);
			}
			f.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			if ((!f.getName().equals(CmsisConstants.LITE)) && (f.members().length == 0)) {
				f.delete(true, true, null);
			}
		}
	}

	/**
	 * check file is used by LITE project
	 *
	 * @param liteProject
	 *            LITE project
	 * @param res
	 *            resource file
	 * @return yes, no
	 */
	protected static boolean isFileUsed(ILiteProject liteProject, IResource res) {
		IPath path = res.getProjectRelativePath();
		String ext = path.getFileExtension();
		if (CmsisConstants.GPDSC_TAG.equals(ext)) {
			IPath gpdsc = res.getLocation();
			ILiteConfiguration liteConf = liteProject.getLiteConfiguration();
			return liteConf.isGeneratedPackUsed(gpdsc.toString());
		}
		return liteProject.isFileUsed(path.toString());
	}

	/*************************************
	 * add resource
	 *****************************************************/
	/**
	 * add file and directory to project,
	 *
	 * @param liteProject
	 * @param monitor
	 * @throws CoreException
	 */
	public static void addResources(ILiteProject liteProject, IProgressMonitor monitor) throws CoreException {
		ILiteConfiguration liteConf = liteProject.getLiteConfiguration();
		if (liteConf == null) {
			return;
		}

		Map<String, ICpFileInfo> fileMap = liteConf.getProjectSourceFiles();
		for (Entry<String, ICpFileInfo> e : fileMap.entrySet()) {
			String projectRelativePath = e.getKey();
			ICpFileInfo fi = e.getValue();

			ICpFile cpFile = fi.getFile();
			if (cpFile == null) {
				return;
			}
			// get source file path
			String srcFilePath = cpFile.getAbsolutePath(cpFile.getName());
			if (srcFilePath == null) {
				return;
			}

			projectRelativePath = adjustProjectPath(liteProject, projectRelativePath);
			/**
			 * add file to project, at this pointer, place a filter to
			 * collect the project features.
			 */
			addSourceFile(liteProject, liteConf, projectRelativePath, fi, monitor);
		}

		Map<String, String> headerMap = liteConf.getProjectIncludeFiles();
		for (Entry<String, String> e : headerMap.entrySet()) {
			String srcPath = e.getKey();
			String dstPath = e.getValue();
			dstPath = adjustProjectPath(liteProject, dstPath);

			updateCopyIncludeFolder(liteProject, srcPath, dstPath, monitor);
		}

		Map<String, EFileCategory> fExtPaths = liteConf.getProjectExtFiles();
		applyOptionsToToolchain(liteProject, fExtPaths, liteConf.getBuildSettings());

		/**
		 * final to refresh the project files
		 */
		IProject project = liteProject.getProject();
		project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
	}

	protected static String adjustProjectPath(ILiteProject liteProject, String spath) {
		String path = spath;

		LiteProjectStorage liteStorage = liteProject.getProjectStorage();
		if (liteStorage == null) {
			return null;
		}

		ILiteToolChainAdapter adapter = liteStorage.getToolChainAdapter();
		if (adapter == null) {
			return null;
		}

		String ext = adapter.getLinkerScriptGenerator().getFileExtension();

		Map<String, String> maps = liteStorage.getProjectFeatures();
		String includePath = maps.get(CmsisConstants.FEATURE_INCLUDE_DIR_KEY);
		String sysPath = maps.get(CmsisConstants.FEATURE_SYSTEM_DIR_KEY);
		String sourcePath = maps.get(CmsisConstants.FEATURE_SOURCE_DIR_KEY);
		String linkerPath = maps.get(CmsisConstants.FEATURE_LINKER_SCRIPT_DIR_KEY);
		String liteIncludePath = sysPath + "/" + includePath; //$NON-NLS-1$
		String liteSourcePath = sysPath + "/" + sourcePath; //$NON-NLS-1$


		if (path.contains(CmsisConstants.LITE_INCLUDE_INDICATION)) {
			path =  path.replace(CmsisConstants.LITE_INCLUDE_INDICATION, liteIncludePath);
		}

		if (path.contains(CmsisConstants.LITE_SOURCE_INDICATION)) {
			path =  path.replace(CmsisConstants.LITE_SOURCE_INDICATION, liteSourcePath);
			if (path.endsWith(ext)) {
				if (!path.isEmpty()) {
					String fpath = ProjectUtils.removeLastPathSegment(path);
					path = path.replace(fpath, linkerPath);
				}

			}
		}

		return path;
	}

	private static void updateCopyIncludeFolder(ILiteProject liteProject, String srcPath, String dstPath,
	        IProgressMonitor monitor) {

		IProject project = liteProject.getProject();
		if (project == null) {
			return;
		}

		String folderSourcePath = srcPath;
		String folderTargetPath = dstPath;
		String pattern = ".*[.](h|txt)".trim();//$NON-NLS-1$
		boolean replaceable = true; // $NON-NLS-1$

		folderSourcePath = CpVariableResolver.dynamicTransferCmsisRootVarToOsPath(folderSourcePath);

		log("HEADER COPY   " + folderSourcePath + "||" + folderTargetPath + " " + new Throwable().getStackTrace()[0].getLineNumber());

		File dir = new File(folderSourcePath);
		if (dir.isDirectory()) {
			for (File child : dir.listFiles()) {
				// System.out.println(child);
				// System.out.println(child.getName());

				String fileName = child.getName();
				if (pattern.length() > 0) {
					if (!fileName.matches(pattern)) {
						continue;
					}
				}

				if (child.isDirectory()) {
					continue;
				}

				URL sourceURL = null;
				try {
					sourceURL = child.toURL(); // using .toURI().toURL()
					// fails, due to spaces substitution
				} catch (MalformedURLException e2) {
					e2.printStackTrace();
				}

				if (sourceURL == null) {
					log("source url " + sourceURL + "is null");
					continue;
				}

				InputStream contents = null;
				try {
					contents = sourceURL.openStream();
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}

				try {
					IFolder iFolder = project.getFolder(folderTargetPath);
					if (!iFolder.exists()) {
						mkdirs(project, project.getFolder(iFolder.getProjectRelativePath()));
					}

					// Should be OK on Windows too
					File concat = new File(folderTargetPath, fileName);
					IFile iFile = project.getFile(concat.getPath());


					if (iFile.exists()) {
						// honor the replaceable flag and replace the
						// file
						// contents
						// if the file already exists.
						if (replaceable) {
							iFile.setContents(contents, true, true, null);
						}

					} else {
						iFile.create(contents, true, null);
						iFile.refreshLocal(IResource.DEPTH_ONE, null);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		} else {
			log(folderSourcePath + " is not directory !");
		}

	}

	public static void mkdirs(IProject projectHandle, IFolder parentFolder) throws CoreException {
		if (parentFolder.getProjectRelativePath().equals(projectHandle.getProjectRelativePath())) {
			return;
		}
		if (!parentFolder.getParent().exists()) {
			mkdirs(projectHandle, projectHandle.getFolder(parentFolder.getParent().getProjectRelativePath()));
		}
		parentFolder.create(true, true, null);
	}

	/**
	 * add the ICpFile to project file
	 *
	 * @param liteProject
	 *            LITE project
	 * @param liteConf
	 *            project configuration
	 * @param dstFile
	 *            DEST file path
	 * @param cpFileInfo
	 *            ICpFileInfo
	 * @param monitor
	 *            monitor
	 * @throws CoreException
	 *             exception
	 */
	protected static void addSourceFile(ILiteProject liteProject, ILiteConfiguration liteConf, String dstFilePath,
	                                    ICpFileInfo cpFileInfo, IProgressMonitor monitor) throws CoreException {

		int copySuccess = 1;
		int fileExists = -1;
		Boolean overWrite = false;

		IProject project = liteProject.getProject();
		ICpFile cpFile = cpFileInfo.getFile();
		if (cpFile == null) {
			return;
		}
		// get source file path
		String srcFilePath = cpFile.getAbsolutePath(cpFile.getName());
		if (srcFilePath == null) {
			return;
		}

		// check source file is generated
		boolean generated = cpFile.isGenerated();

		// get project configuration info
		ICpConfigurationInfo cpConfInfo = liteConf.getConfigurationInfo();
		if (cpConfInfo == null) {
			return;
		}

		/**
		 * filter the local file and no annotation file to prevent generated
		 * files to copy
		 */
		Boolean keepSlash = true;
		Boolean local = false;
		EFileRole role = cpFileInfo.getRole();
		String fileDirectory = cpConfInfo.getDir(keepSlash);
		if (fileDirectory != null) {
			local = srcFilePath.startsWith(fileDirectory);
			if (generated || local) {
				role = EFileRole.NONE;
			}
		}

		/**
		 * configuration file, startup and system configuration header file and
		 * source files
		 */
		if (role == EFileRole.CONFIG) {
			int beginIndex = -1;

			// get file category: doc, header, source, include path and other
			// file
			EFileCategory cat = cpFileInfo.getCategory();

			// header and source files
			if (cat.isHeader() || cat.isSource()) {
				String baseSrc = DeviceUIUtils.extractBaseFileName(srcFilePath);
				String baseDst = DeviceUIUtils.extractBaseFileName(dstFilePath);
				int srcPathLength = baseSrc.length() + 1;
				int dstPathLength = baseDst.length();

				// dest file path is sub directory of source file path
				if (dstPathLength > srcPathLength) {
					String instance = baseDst.substring(srcPathLength);
					try {
						beginIndex = Integer.decode(instance);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}

			/**
			 * Copy a local file to a local project folder. Destination file
			 * name can be different than the source one. Folder is
			 * automatically created if not existing.
			 */
			int bCopied = ProjectUtils.copyFile(project, srcFilePath, dstFilePath, beginIndex, monitor, overWrite);
			if (bCopied == copySuccess) {
				log("FILE COPY " + " > " + dstFilePath);
			} else if (bCopied == fileExists) {
				String savedVersion = ConfigurationUtils.getFileVersion(liteProject, dstFilePath);
				if (savedVersion != null) {
					cpFileInfo.setVersion(savedVersion);
				}
			}
		} else if (role == EFileRole.COPY) {
			int bCopied = ProjectUtils.copyFile(project, srcFilePath, dstFilePath, -1, monitor, overWrite);
			if (bCopied == copySuccess) {
				log("FILE COPY " + " > " + dstFilePath);
			} else if (bCopied == fileExists) {
				String savedVersion = ConfigurationUtils.getFileVersion(liteProject, dstFilePath);
				if (savedVersion != null) {
					cpFileInfo.setVersion(savedVersion);
				}
			}
		} else if (!local) {
			if (srcFilePath != null) {
				// ProjectUtils.createLink(project, srcFilePath, dstFilePath,
				// monitor);
				File src = new File(srcFilePath);
				if (src.exists()) {
					ProjectUtils.copyFile(project, srcFilePath, dstFilePath, -1, monitor, overWrite);
					log("FILE COPY " + " > " + dstFilePath);
				} else {
					log("NOT  COPY " + " >< " + dstFilePath);
				}

			}
		}

		// // some file need to excluded
		// if (ProjectUtils.isExcludedFromBuild(project, dstFilePath)) {
		// log(dstFilePath + " isExcludedFromBuild");
		// ProjectUtils.setExcludeFromBuild(project, dstFilePath, false);
		// }

	}

	public static void applyToolchainSymbols(Map<String, String> symbols, IBuildSettings buildSettings) {
		if (symbols == null || symbols.isEmpty()) {
			return;
		}

		for (String syk : symbols.keySet()) {
			String sym = symbols.get(syk);
			buildSettings.addStringListValue(IBuildSettings.LITE_DEFINES, sym);
		}

	}

	public static List<String> collectBuildToolSymbols(IProject project) {
		return collectBuildToolOptions(project, IOption.PREPROCESSOR_SYMBOLS, "compiler.defs"); //$NON-NLS-1$
	}

	public static List<String> collectBuildToolIncludePaths(IProject project) {
		return collectBuildToolOptions(project, IOption.INCLUDE_PATH, "include.paths"); //$NON-NLS-1$
	}

	public static List<String> collectBuildToolLinkerPaths(IProject project) {
		return collectBuildToolOptions(project, IOption.LIBRARY_PATHS, "linker.paths"); //$NON-NLS-1$
	}

	public static List<String> searchBuildToolLinkerFiles(IProject project) {
		IConfiguration[] projectConfigs = ManagedBuildManager.getBuildInfo(project).getManagedProject()
		                                  .getConfigurations();
		List<String> values = new ArrayList<String>();

		for (IConfiguration config : projectConfigs) {
			IToolChain toolChain = config.getToolChain();
			ITool[] tools = toolChain.getTools();

			for (ITool tool : tools) {
				IOption[] ots = tool.getOptions();
				for (IOption op : ots) {

					try {
						if (IOption.STRING_LIST == op.getValueType()) {
							String baseId = op.getBaseId();
							if (baseId.contains("linker.scriptfile")) { //$NON-NLS-1$
								List<String> subs = getCurrentStringListValue(op);

								if (!subs.isEmpty()) {
									values.addAll(subs);
								}

							}
						}
					} catch (BuildException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return values;
	}

	public static Map<String, List<String>> collectBuildToolOptionValues(IProject project, int type) {
		IConfiguration[] projectConfigs = ManagedBuildManager.getBuildInfo(project).getManagedProject().getConfigurations();
		Map<String, List<String>> values = new HashMap<String, List<String>>();

		for (IConfiguration config : projectConfigs) {
			IToolChain toolChain = config.getToolChain();
			ITool[] tools = toolChain.getTools();

			for (ITool tool : tools) {
				IOption[] ots = tool.getOptions();
				for (IOption op : ots) {

					try {
						if (type == op.getValueType()) {
							String id = op.getId();
							List<String> subs = getCurrentStringListValue(op);
							if (!subs.isEmpty()) {
								values.put(id, subs);
							}
						}
					} catch (BuildException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return values;
	}

	private static List<String> collectBuildToolOptions(IProject project, int type, String ext) {
		IConfiguration[] projectConfigs = ManagedBuildManager.getBuildInfo(project).getManagedProject()
		                                  .getConfigurations();
		List<String> values = new ArrayList<String>();

		for (IConfiguration config : projectConfigs) {
			IToolChain toolChain = config.getToolChain();
			ITool[] tools = toolChain.getTools();

			for (ITool tool : tools) {
				IOption[] ots = tool.getOptions();
				for (IOption op : ots) {

					try {
						if (type == op.getValueType()) {
							String baseId = op.getBaseId();
							if (baseId.contains(ext)) {
								List<String> subs = getCurrentStringListValue(op);

								if (!subs.isEmpty()) {
									values.addAll(subs);
								}
							}
						}
					} catch (BuildException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return values;
	}

	public static List<String> getCurrentStringListValue(IOption option) throws BuildException {
		String[] array = null;
		int type = option.getValueType();
		switch (type) {
		case IOption.PREPROCESSOR_SYMBOLS:
			array = option.getDefinedSymbols();
			break;
		case IOption.INCLUDE_PATH:
			array = option.getIncludePaths();
			break;
		case IOption.LIBRARY_PATHS:
			array = option.getLibraryPaths();
			break;
		case IOption.LIBRARIES:
			array = option.getLibraries();
			break;
		case IOption.OBJECTS:
			array = option.getUserObjects();
			break;
		case IOption.STRING_LIST:
			array = option.getStringListValue();
			break;
		default:
			break;
		}
		if (array == null) {
			return null;
		}
		return new ArrayList<String>(Arrays.asList(array));
	}

	/**
	 * Creates memory settings from device information
	 *
	 * @param deviceInfo
	 *            ICpDeviceInfo object
	 */
	public static IMemorySettings createMemorySettings(ICpDeviceInfo di) {

		ICpDeviceItem d = di.getDevice();
		if (d == null) {
			return null;
		}
		ICpItem props = di.getEffectiveProperties();
		if (props == null) {
			return null;
		}

		ICpDebugConfiguration dc = di.getDebugConfiguration();
		Map<String, ICpMemory> memoryItems = dc.getMemoryItems();
		return new MemorySettings(memoryItems);
	}

	private static void log(String msg) {
		System.out.println(msg);
	}
}
