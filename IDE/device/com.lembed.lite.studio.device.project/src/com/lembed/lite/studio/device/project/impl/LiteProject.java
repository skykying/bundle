/*******************************************************************************
* Copyright (c) 2017 Lembed
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
* LEMBED - adapter for LiteSTUDIO
*******************************************************************************/

package com.lembed.lite.studio.device.project.impl;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.info.ICpFileInfo;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.project.CpProjectPlugIn;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterInfo;
import com.lembed.lite.studio.device.ui.console.LiteConsole;

/**
 * Default implementation of ILiteProject interface
 */
public class LiteProject extends PlatformObject implements ILiteProject {

	private String fName = null;
	protected ILiteConfiguration fLiteConfiguration = null;
	protected LiteProjectStorage fLiteProjectStorage = null;
	private boolean bUpdateCompleted = false;
	protected LiteConsole liteConsole = null;
	
	private Boolean toSave = true;
	private Boolean toLoad = false;
	
	protected Map<String, String> features = new HashMap<String, String>(); 
	
	/**
	 * Constructs LiteProject for given project
	 */
	public LiteProject(IProject project) {
		setName(project.getName());
		fLiteProjectStorage = new LiteProjectStorage();
		liteConsole = LiteConsole.openConsole(project);
	}

	@Override
	public void destroy() {
		fLiteConfiguration = null;
		fLiteProjectStorage = null;
	}

	@Override
	synchronized public boolean isUpdateCompleted() {
		return bUpdateCompleted;
	}

	@Override
	synchronized public void setUpdateCompleted(boolean completed) {
		bUpdateCompleted = completed;
	}

	@Override
	public String getName() {
		return fName;
	}

	@Override
	public void setName(String name) {
		fName = name;
	}

	@Override
	public LiteToolChainAdapterInfo getToolChainAdapterInfo() {
		return fLiteProjectStorage.getToolChainAdapterInfo();
	}

	@Override
	public void setToolChainAdapterInfo(LiteToolChainAdapterInfo toolChainAdapterInfo) {
		fLiteProjectStorage.setToolChainAdapterInfo(toolChainAdapterInfo);
	}
	
	@Override
	public ILiteConfiguration getLiteConfiguration() {
		return fLiteConfiguration;
	}
	
	@Override
	public Map<String, String> getProjectFeatures() {
		return features;
	}

	@Override
	public IProject getProject() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getName());
		return project;
	}
	
	@Override
	public void setLiteConfiguration(ILiteConfiguration liteConf) {
		fLiteConfiguration = liteConf;
		fLiteProjectStorage.setLiteConfiguration(liteConf);
	}

	@Override
	public LiteProjectStorage getProjectStorage() {
		return fLiteProjectStorage;
	}

	/**
	 * callback by LiteProjectUpdater 
	 * LiteProject(update) -> LiteProjectManager(updateProject)->LiteProjectUpdater
	 * 
	 * @throws CoreException [description]
	 */
	@Override
	public void save() throws CoreException {
		Boolean toSave = true;
		processLiteStorages(toSave);
	}

	/**
	 * callback by LiteSetupParticipant
	 * actual used by IndexerSetupParticipant of LiteSetupParticipant
	 * 
	 * @throws CoreException [description]
	 */
	@Override
	public void load() throws CoreException {
		setUpdateCompleted(false);

		processLiteStorages(toLoad);
		//update(LiteProjectUpdater.LOAD_CONFIGS);
	}

	/**
	 * just do project index and generate the component.h file
	 * which handle by LiteProjectUpdater
	 */
	@Override
	public void init() {
		update(0);
	}

	/**
	 * callback by ResetBuildSettingsHandler
	 */
	@Override
	public void reload() {
		//update(LiteProjectUpdater.LOAD_CONFIGS | LiteProjectUpdater.UPDATE_TOOLCHAIN);
	}

	/**
	 * used local by LiteProjectManager
	 */
	@Override
	public void refresh() {
		//update(LiteProjectUpdater.LOAD_CONFIGS);
	}

	/**
	 * callback by CleanupHandler
	 */
	@Override
	public void cleanup() {
		//update(LiteProjectUpdater.LOAD_CONFIGS | LiteProjectUpdater.CLEANUP_RTE_FILES);
	}
	
	/**
	 * actual update process, an project update instance in project 
	 * manager class to handle all processes.
	 * 
	 * LiteProject(update) -> LiteProjectManager(updateProject) -> LiteProjectUpdater()
	 * @param updateFlags
	 */
	protected void update(int updateFlags) {
		CpProjectPlugIn.getLiteProjectManager().updateProject(this, updateFlags);
	}

	protected void processLiteStorages(boolean saveOrLoad) throws CoreException {
		IProject project = getProject();
		CoreModel model = CoreModel.getDefault();
		ICProjectDescription projDes = model.getProjectDescription(project);
		if(projDes == null){
			return;
		}

		if (saveOrLoad == toSave) {
			saveLiteStorage(projDes);
			model.setProjectDescription(project, projDes);
		} else {
			loadLiteStorage(projDes);
		}
	}

	/**
	 * save LiteStorage info to Cdt project description file
	 * @param  projDes       cdt project decripition need to write custom info to
	 * @throws CoreException error throw by info write process
	 */
	protected void saveLiteStorage(ICProjectDescription projDes) throws CoreException {
		if (fLiteProjectStorage != null) {
			fLiteProjectStorage.save(projDes);
		}
	}

	/**
	 * load lite project info from cdt project description file
	 * @param  projDes       cdt project description file
	 * @throws CoreException  error throw by info read process
	 */
	protected void loadLiteStorage(ICProjectDescription projDes) throws CoreException {
		if (fLiteProjectStorage == null) {
			fLiteProjectStorage = new LiteProjectStorage();
		}
		fLiteProjectStorage.load(projDes);
		
		Map<String, String> fMaps = fLiteProjectStorage.getProjectFeatures();
		if(fMaps != null){
			this.features = fMaps;
		}
		
		fLiteConfiguration = fLiteProjectStorage.getLiteConfiguration();
		if(fLiteConfiguration == null){
			log("load storage error");
		}
		
		setUpdateCompleted(true);
	}

	/**
	 * utils to check the file is used by project
	 * @param  fileName  file name
	 * @return          null
	 */
	@Override
	public boolean isFileUsed(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return false;
		}
		if (fileName.equals(CmsisConstants.LITE_LITE_Components_h)) {
			return true;
		}
		
		if (fLiteConfiguration != null) {			
			return fLiteConfiguration.getProjectFileInfo(fileName) != null;
		}
		return false;
	}

	/**
	 * get the project file info 
	 * @param  fileName file name
	 * @return          ICpFileInfo instance
	 */
	@Override
	public ICpFileInfo getProjectFileInfo(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return null;
		}
		if (fLiteConfiguration != null) {
			return fLiteConfiguration.getProjectFileInfo(fileName);
		}
		return null;
	}

	@Override
	public ICpFileInfo[] getProjectFileInfos(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return null;
		}
		if (fLiteConfiguration != null) {
			return fLiteConfiguration.getProjectFileInfos(fileName);
		}
		return null;
	}

	public  String getLiteSTUDIOInstalledDir() {
		String searchPath = CmsisConstants.EMPTY_STRING;
		URL path = Platform.getInstallLocation().getURL();
		String defaultPackRoot = "device";
		
		liteConsole.output(path.toString());

		File installFile = new File(path.getFile());		
		if((installFile != null ) && (installFile.exists())) {
			File parentFile = installFile.getParentFile();
			if (parentFile.isDirectory()) {
				String vpath = parentFile.getAbsolutePath() + File.separator + defaultPackRoot;
				File finalFile = new File(vpath);
				if(finalFile.exists()){
					searchPath = finalFile.getAbsolutePath();
					liteConsole.output(searchPath);
				}				
			}
		}

		return searchPath;
	}



	private static void log(String msg){
		System.out.println(msg);
	}
	
}
