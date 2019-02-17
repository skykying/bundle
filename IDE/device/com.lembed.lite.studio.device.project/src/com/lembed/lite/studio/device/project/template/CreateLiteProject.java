/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * Copyright (c) 2017 Lembed Electronic inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 * Lembed Electronic - change the configuration file to project description file
 *******************************************************************************/

package com.lembed.lite.studio.device.project.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import org.eclipse.cdt.build.core.scannerconfig.ScannerConfigBuilder;
import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.configuration.LiteConfiguration;
import com.lembed.lite.studio.device.core.build.IBuildSettings;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.info.CpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.project.CpProjectPlugIn;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.project.impl.LiteProjectManager;
import com.lembed.lite.studio.device.project.impl.LiteProjectStorage;
import com.lembed.lite.studio.device.project.utils.ConfigurationUtils;
import com.lembed.lite.studio.device.project.utils.ProjectUtils;
import com.lembed.lite.studio.device.toolchain.ILiteToolChainAdapter;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterFactory;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterInfo;

/**
 * Process runner that creates new lite Project with default lite configuration
 * this will create child project configuration inside parent cdt project description file
 * and setup the child project extra configuration file in the project description file
 */
public class CreateLiteProject extends ProcessRunner {

	@Override
	public void process(TemplateCore template, ProcessArgument[] args, String processId, IProgressMonitor monitor)
			throws ProcessFailureException {
		String projectName = args[0].getSimpleValue();
		String compiler = args[1].getSimpleValue();
		String output = args[2].getSimpleValue();
		String adapterId = args[3].getSimpleValue();
		String lastStep = args[4].getSimpleValue();
		String includePath = args[5].getSimpleValue();

		// get the cdt parent project from the project name for setup the child project next
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project == null) {
			String msg = Messages.CreateLiteProject_ErrorCreatingLiteProject;
			msg += Messages.CreateLiteProject_EclipseProjectNotExists;
			msg += projectName;
			throw new ProcessFailureException(getProcessMessage(processId, IStatus.ERROR, msg));
		}

		// create the tool chain informations
		LiteToolChainAdapterInfo adapterInfo = createToolChainAdapter(adapterId);
		if (adapterInfo == null) {
			String msg = Messages.CreateLiteProject_ErrorCreatingLiteProject;
			msg += Messages.CreateLiteProject_ToolchainAdapterNotFound;
			msg += adapterId;
			throw new ProcessFailureException(getProcessMessage(processId, IStatus.ERROR, msg));
		}

		// create the configuration description file and write the configuration information to it
		LiteProjectManager liteProjectManager = CpProjectPlugIn.getLiteProjectManager();
		ILiteProject liteProject = liteProjectManager.createLiteProject(project);
		liteProject.setToolChainAdapterInfo(adapterInfo);

		ILiteConfiguration liteConf = createLiteConfiguration(compiler, output);
		liteProject.setLiteConfiguration(liteConf);		
		try {
			IFile f = ProjectUtils.createFile(project, includePath+File.separatorChar+CmsisConstants.LITE_LITE_Components_h, monitor);
			IPath p = f.getLocation();
			p.toFile().setWritable(true);
			PrintWriter pw = new PrintWriter(p.toOSString());
			pw.println("/*"); //$NON-NLS-1$
			pw.println(" * Define the Device Header File:"); //$NON-NLS-1$
			pw.println("*/"); //$NON-NLS-1$
			pw.println();
			pw.close();
		} catch (CoreException | FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setProjectFeatures(liteProject);
		
		if ("1".equals(lastStep)) { //$NON-NLS-1$
			// in some customized process, we should prevent the indexer from
			// indexing at this point
			// because it may still has some work to do (e.g. copy resources)
			// liteProject.init(); //@2017.4.7
			try {
				liteProject.save();
				updateToolChainOptions(project,adapterInfo,liteConf);					
			} catch (CoreException e) {				
				e.printStackTrace();
			}
		}
		
		//ConfigurationUtils.addResources(liteProject, monitor);
		ConfigurationUtils.applyBuildSettings(liteProject, true,monitor);
		ConfigurationUtils.updateIndex(liteProject);
	}

	private void updateToolChainOptions(IProject project, LiteToolChainAdapterInfo adapterInfo, ILiteConfiguration liteConf) {
		
		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		if (buildInfo == null){
			return;
		}

		IConfiguration[] configs = buildInfo.getManagedProject().getConfigurations();
		for (IConfiguration config : configs) {
			ILiteToolChainAdapter chainAdapter = adapterInfo.getToolChainAdapter();
			
			// the LiteConfiguration class will new a BuildSettings instance
			IBuildSettings setting = liteConf.getBuildSettings();
			chainAdapter.setUpToolChainOptions(config, setting);
			chainAdapter.setToolChainIndex(0);
			chainAdapter.setInitialToolChainOptions(config, setting);
		}
		
		Boolean saved = ManagedBuildManager.saveBuildInfo(project, true);
		if (saved) {
			for (IConfiguration config : configs) {
				ScannerConfigBuilder.build(config, ScannerConfigBuilder.PERFORM_CORE_UPDATE, new NullProgressMonitor());
			}
		}
	}
	

	/**
	 * return the tool chain information from the tool chain adapter id
	 * @param  adapterId tool chain adapter id
	 * @return           adapter information
	 */
	protected LiteToolChainAdapterInfo createToolChainAdapter(String adapterId) {
		LiteToolChainAdapterFactory adapterFactory = LiteToolChainAdapterFactory.getInstance();
		return adapterFactory.getAdapterInfo(adapterId);
	}

	/**
	 * [create Child project Configuration description]
	 * @param  compiler cross tool chain
	 * @param  output   exe or lib
	 * @return          project configuration 
	 */
	protected ILiteConfiguration createLiteConfiguration(String compiler, String output) {

		ICpDeviceInfo deviceInfo = LiteProjectTemplateProvider.getSelectedDeviceInfo();
		ICpItem toolchainInfo = LiteProjectTemplateProvider.createToolChainInfo(compiler, output);
		
		// create default configuration information
		boolean createDefaultTags = true;
		ICpConfigurationInfo cpInfo = new CpConfigurationInfo(deviceInfo, toolchainInfo, createDefaultTags);
		ILiteConfiguration liteConf = new LiteConfiguration();
		
		// this will initialization the liteConf instance
		liteConf.setConfigurationInfo(cpInfo);

		return liteConf;
	}

	protected void setProjectFeatures(ILiteProject liteProject){
		Map<String, String> features = LiteProjectTemplateProvider.getProjectFeatures();
		if(features!= null){
			LiteProjectStorage liteStorage = liteProject.getProjectStorage();
			liteStorage.setProjectFeatures(features);
		}		
	}
}
