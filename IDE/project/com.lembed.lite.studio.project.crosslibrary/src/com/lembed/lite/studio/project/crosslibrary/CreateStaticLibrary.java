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

package com.lembed.lite.studio.project.crosslibrary;

import org.eclipse.cdt.build.core.scannerconfig.ScannerConfigBuilder;
import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import com.lembed.lite.studio.device.toolchain.ILiteToolChainAdapter;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterFactory;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterInfo;

/**
 * Process runner that creates new lite Project with default lite configuration
 * this will create child project configuration inside parent cdt project description file
 * and setup the child project extra configuration file in the project description file
 */
public class CreateStaticLibrary extends ProcessRunner {

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

		updateToolChainOptions(project,adapterInfo);					
	}

	private void updateToolChainOptions(IProject project, LiteToolChainAdapterInfo adapterInfo) {
		
		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		if (buildInfo == null){
			return;
		}

		IConfiguration[] configs = buildInfo.getManagedProject().getConfigurations();
		for (IConfiguration config : configs) {
			ILiteToolChainAdapter chainAdapter = adapterInfo.getToolChainAdapter();
			
			// the LiteConfiguration class will new a BuildSettings instance
			chainAdapter.setUpToolChainOptions(config, null);
			chainAdapter.setToolChainIndex(0);
			chainAdapter.setInitialToolChainOptions(config, null);
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

}
