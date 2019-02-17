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

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import com.lembed.lite.studio.managedbuild.cross.Option;

public class DefaultToolchain { 
	
	static final String OPTION_COMMAND_READELF = "readelf";

	public static String getToolChainPath(IProject project) {

		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		if (buildInfo == null) {
			return null;
		}
		IConfiguration configuration = buildInfo.getDefaultConfiguration();
		IToolChain toolchain = configuration.getToolChain();

		IOption option;
		option = toolchain.getOptionBySuperClassId(Option.OPTION_TOOLCHAIN_NAME); // $NON-NLS-1$
		String toolchainName = (String) option.getValue();

		String path = PersistentPreferences.getToolchainPath(toolchainName, project);
		File file = new File(path);
		file = file.getAbsoluteFile();
		path = file.getAbsolutePath();
		
		return path;
	}
	
	
	public static String getElfParserPath(IProject project) {
		String base = getToolChainPath(project);
		String suffix="",prefix="",cmd="";
		
		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		if (buildInfo == null) {
			return null;
		}
		IConfiguration configuration = buildInfo.getDefaultConfiguration();
		IToolChain toolchain = configuration.getToolChain();
		
		cmd = OPTION_COMMAND_READELF;
		
		IOption option = toolchain.getOptionBySuperClassId(Option.OPTION_COMMAND_PREFIX);
		try {
			prefix = option.getStringValue();
		} catch (BuildException e) {
			e.printStackTrace();
		}

		option = toolchain.getOptionBySuperClassId(Option.OPTION_COMMAND_SUFFIX);
		try {
			suffix = option.getStringValue();
		} catch (BuildException e) {
			e.printStackTrace();
		}

		String path = base + File.separator + prefix + cmd + suffix;
		log(path);
		File file = new File(path);
		if(file.exists()){
			return path;
		}
		
		path =  path +".exe"; //$NON-NLS-1$
		file = new File(path);
		if(file.exists()){
			return path;
		}
		
		return null;
	}
	
	
	public static String getObjDumpPath(IProject project) {
		String base = getToolChainPath(project);
		String suffix="",prefix="",cmd="";
		
		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		if (buildInfo == null) {
			return null;
		}
		IConfiguration configuration = buildInfo.getDefaultConfiguration();
		IToolChain toolchain = configuration.getToolChain();
		
		IOption option = toolchain.getOptionBySuperClassId(Option.OPTION_COMMAND_OBJDUMP);
		try {
			cmd = option.getStringValue();
		} catch (BuildException e) {
			e.printStackTrace();
		}
		
		option = toolchain.getOptionBySuperClassId(Option.OPTION_COMMAND_PREFIX);
		try {
			prefix = option.getStringValue();
		} catch (BuildException e) {
			e.printStackTrace();
		}

		option = toolchain.getOptionBySuperClassId(Option.OPTION_COMMAND_SUFFIX);
		try {
			suffix = option.getStringValue();
		} catch (BuildException e) {
			e.printStackTrace();
		}

		String path = base + File.separator + prefix + cmd + suffix;
		log(path);
		File file = new File(path);
		if(file.exists()){
			return path;
		}
		
		path =  path +".exe"; //$NON-NLS-1$
		file = new File(path);
		if(file.exists()){
			return path;
		}
		
		return null;
	}
	
	private static void log(String msg) {
		System.out.println(msg);
	}

}
