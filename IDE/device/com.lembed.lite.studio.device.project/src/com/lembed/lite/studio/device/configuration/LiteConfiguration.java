/*******************************************************************************
 * Copyright (c) 2017 LEMBED
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * LEMBED - adapter for LiteSTUDIO
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.configuration;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.build.IBuildSettings;
import com.lembed.lite.studio.device.core.data.CpCodeTemplate;
import com.lembed.lite.studio.device.core.data.ICpCodeTemplate;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpDebugConfiguration;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpFile;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPack.PackState;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.enums.EFileCategory;
import com.lembed.lite.studio.device.core.enums.EFileRole;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.info.ICpFileInfo;
import com.lembed.lite.studio.device.core.info.ICpItemInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.core.lite.ILiteModel;
import com.lembed.lite.studio.device.core.lite.LiteModel;
import com.lembed.lite.studio.device.core.lite.build.LiteBuildSettings;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependencyItem;
import com.lembed.lite.studio.device.project.CpVariableResolver;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.project.utils.LitePathComparator;
import com.lembed.lite.studio.device.project.utils.ProjectUtils;
import com.lembed.lite.studio.device.utils.AlnumComparator;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;
import com.lembed.lite.studio.device.utils.Utils;

/**
 * Default implementation of ILiteConfiguration interface
 */
public class LiteConfiguration extends PlatformObject implements ILiteConfiguration {

	protected ILiteModel fModel = null;
	// underlying model that is source of information
	protected ICpConfigurationInfo fConfigInfo = null;
	/** meta-information that is stored project description storage and used to transfer information
	  * to/from model
	 */

	protected LiteBuildSettings liteBuildSettings = new LiteBuildSettings();

	// source files included in project: project relative path -> absPath
	protected Map<String, ICpFileInfo> projectSourceFiles = new HashMap<String, ICpFileInfo>();

	protected Map<String, String> projectIncludeFiles = new HashMap<String, String>();

	// root of the code template hierarchy
	protected ICpCodeTemplate fCodeTemplateRoot = new CpCodeTemplate(null);
	// paths to library sources (for debugger)
	protected Set<String> libSourcePaths = new TreeSet<String>(new LitePathComparator());
	// pieces of code put into RTE_Components.h file
	protected List<String> liteComponentsH = new LinkedList<String>();
	// SCVD files for component viewer: project relative path -> absPath
	protected Map<String, ICpFileInfo> fScvdFiles = new HashMap<String, ICpFileInfo>();
	// header -> comment (for editor)
	protected Map<String, String> headers = new TreeMap<String, String>(new AlnumComparator(false));
	// documentation files relevant to configuration
	protected Map<String, String> docs = new TreeMap<String, String>(new AlnumComparator(false));

	// svd file path
	protected Map<String, String> fSVDFiles = new HashMap<String, String>();

	protected ICpComponentInfo deviceStartupComponent = null;
	protected ICpComponentInfo cmsisCoreComponent = null;
	protected ICpComponentInfo cmsisRtosComponent = null;

	// device header name without path
	protected String deviceHeader = null;

	protected Collection<ICpPackInfo> fMissingPacks = new HashSet<ICpPackInfo>();

	protected Map<String, EFileCategory> fExtPaths = new HashMap<String, EFileCategory>();

	protected Map<String, String> symbols = new HashMap<String, String>();

	boolean valid = true;
	// flag that indicates that device and all required components are resolved

	public LiteConfiguration() {
	}

	/**
	 * clear all configuration
	 */
	protected void clear() {
		liteBuildSettings.clear();
		projectSourceFiles.clear();
		libSourcePaths.clear();

		liteComponentsH.clear();

		headers.clear();
		docs.clear();
		deviceHeader = null;
		deviceStartupComponent = null;
		cmsisCoreComponent = null;
		cmsisRtosComponent = null;
		valid = true;

		fMissingPacks.clear();
		fScvdFiles.clear();

		//log("clear");
	}

	@Override
	public ICpConfigurationInfo getConfigurationInfo() {
		return fConfigInfo;
	}

	@Override
	public ICpDeviceInfo getDeviceInfo() {
		return fConfigInfo != null ? fConfigInfo.getDeviceInfo() : null;
	}

	@Override
	public ICpDebugConfiguration getDebugConfiguration() {
		ICpDeviceInfo di = getDeviceInfo();
		if (di != null) {
			return di.getDebugConfiguration();
		}
		return null;
	}

	@Override
	public ICpPack getDfp() {
		if (fConfigInfo == null) {
			return null;
		}
		return fConfigInfo.getPack();
	}

	@Override
	public String getDfpPath() {
		if (fConfigInfo == null) {
			return null;
		}
		return fConfigInfo.getDfpPath();
	}

	@Override
	public IBuildSettings getBuildSettings() {
		return liteBuildSettings;
	}

	@Override
	public Map<String, ICpFileInfo> getProjectSourceFiles() {
		return projectSourceFiles;
	}

	@Override
	public Map<String, String> getProjectIncludeFiles() {
		return projectIncludeFiles;
	}


	@Override
	public Map<String, EFileCategory> getProjectExtFiles() {
		return fExtPaths;
	}

	@Override
	public Map<String, ICpFileInfo> getScvdFiles() {
		return fScvdFiles;
	}

	@Override
	public Map<String, String> getSymbols() {
		return symbols;
	}

	/**
	 * get file information from file name in project object
	 * @param  fileName file name
	 * @return          file information
	 */
	@Override
	public ICpFileInfo getProjectFileInfo(String fileName) {
		return projectSourceFiles.get(fileName);
	}

	@Override
	public ICpFileInfo[] getProjectFileInfos(String fileName) {
		Collection<ICpFileInfo> fileInfos = new LinkedList<>();
		for (String key : projectSourceFiles.keySet()) {
			if (key.matches(fileName)) {
				fileInfos.add(projectSourceFiles.get(key));
			}
		}
		ICpFileInfo[] infos = new ICpFileInfo[fileInfos.size()];
		return fileInfos.toArray(infos);
	}

	@Override
	public Collection<String> getLibSourcePaths() {
		return libSourcePaths;
	}

	@Override
	public Collection<String> getLiteComponentsHCode() {
		return liteComponentsH;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public Map<String, String> getDocs() {
		return docs;
	}

	@Override
	public String getDeviceHeader() {
		return deviceHeader;
	}

	@Override
	public Map<String, String> getSvdFiles() {
		return fSVDFiles;
	}

	@Override
	public ICpComponentInfo getDeviceStartupComponent() {
		return deviceStartupComponent;
	}

	@Override
	public ICpComponentInfo getCmsisCoreComponent() {
		return cmsisCoreComponent;
	}

	@Override
	public ICpComponentInfo getCmsisRtosComponent() {
		return cmsisRtosComponent;
	}

	@Override
	public ICpCodeTemplate getCmsisCodeTemplate() {
		return fCodeTemplateRoot;
	}

	/**
	 * set the configuration information to project description file
	 * only be called by CreateLiteProject class.
	 * @param info
	 * 				the configuration will be write to
	 */
	@Override
	public synchronized void setConfigurationInfo(ICpConfigurationInfo info) {

		if (fConfigInfo == info) {
			return;
		}
		fConfigInfo = info;
		if (fModel == null && fConfigInfo != null) {
			fModel = new LiteModel();
		}

		// use ILiteModel to validate the dependences, set the configuration
		// information to ILiteModel
		if (fModel != null) {
			fModel.setConfigurationInfo(info);
			fConfigInfo = fModel.getConfigurationInfo();
		}

		// collect all settings and fill this instance
		collectSettings();
	}

	/**
	 * filter the CMSIS file, which will be copy to project, some others not
	 *
	 * @param  fi {@link ICpFileInfo}
	 * @return    true if add to project
	 */
	@Override
	public boolean isSourceFileToProject(ICpFileInfo fi) {
		if (fi == null) {
			return false;
		}
		if (isGeneratedAndRelativeToProject(fConfigInfo, fi)) {
			return true;
		}

		EFileRole role = fi.getRole();
		boolean includeInProject = false;
		switch (role) {
		case INTERFACE:
		case TEMPLATE:
			return false;
		case CONFIG:
		case COPY:
			includeInProject = true;
		case NONE:
		default:
			break;
		}

		EFileCategory cat = fi.getCategory();
		switch (cat) {
		case SOURCE:
		case SOURCE_ASM:
		case SOURCE_C:
		case SOURCE_CPP:
		case LINKER_SCRIPT:
		case LIBRARY:
		case OBJECT:
			return true;
		case INCLUDE:
			return false;
		case HEADER:
		default:
			break;
		}
		return includeInProject;
	}

	public boolean isIncludeFileToProject(ICpFileInfo fi) {
		if (fi == null) {
			return false;
		}
		if (isGeneratedAndRelativeToProject(fConfigInfo, fi)) {
			return true;
		}

		EFileCategory cat = fi.getCategory();
		switch (cat) {
		case SOURCE:
		case SOURCE_ASM:
		case SOURCE_C:
		case SOURCE_CPP:
		case LINKER_SCRIPT:
		case LIBRARY:
		case OBJECT:
			return false;
		case INCLUDE:
			return true;
		case HEADER:
			return false;
		default:
			break;
		}
		return false;
	}

	public boolean isHeaderFileToProject(ICpFileInfo fi) {
		if (fi == null) {
			return false;
		}
		if (isGeneratedAndRelativeToProject(fConfigInfo, fi)) {
			return true;
		}

		EFileCategory cat = fi.getCategory();
		switch (cat) {
		case SOURCE:
		case SOURCE_ASM:
		case SOURCE_C:
		case SOURCE_CPP:
		case LINKER_SCRIPT:
		case LIBRARY:
		case OBJECT:
			return false;
		case INCLUDE:
			return false;
		case HEADER:
			return true;
		default:
			break;
		}
		return false;
	}

	/**
	 * validate the required components and return the status.
	 * put the missed components to miss list .
	 * @return [status]
	 */
	@Override
	public Collection<String> validate() {

		EEvaluationResult res = fModel.getEvaluationResult();
		if (res.ordinal() >= EEvaluationResult.MISSING.ordinal()) {
			valid = true;
			return null;
		}
		List<String> errors = new LinkedList<String>();
		Collection<? extends ILiteDependencyItem> results = fModel.getDependencyItems();
		Set<String> familyIds = new HashSet<String>();
		for (ILiteDependencyItem item : results) {
			String msg = item.getName() + " - " + item.getDescription(); //$NON-NLS-1$
			errors.add(msg);
			ICpItem cpItem = item.getCpItem();
			if (cpItem instanceof ICpItemInfo && item.getEvaluationResult() != EEvaluationResult.FULFILLED) {
				ICpItemInfo ci = (ICpItemInfo) cpItem;
				ICpPackInfo pi = ci.getPackInfo();
				if (familyIds.add(pi.getPackFamilyId())) {
					fMissingPacks.add(pi);
				}
			}
		}
		valid = false;
		return errors;
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public Collection<ICpPackInfo> getMissingPacks() {
		return fMissingPacks;
	}

	@Override
	public EEvaluationResult getEvaluationResult() {
		if (fModel != null) {
			return fModel.getEvaluationResult();
		}
		return EEvaluationResult.UNDEFINED;
	}

	@Override
	public void setEvaluationResult(EEvaluationResult result) {
		if (fModel != null) {
			fModel.setEvaluationResult(result);
		}
	}

	@Override
	public boolean isGeneratedPackUsed(String gpdsc) {
		if (fModel != null) {
			return fModel.isGeneratedPackUsed(gpdsc);
		}
		return false;
	}

	/************************************************ sub functions ********************************************/

	/**
	 * collect settings from LITE project setup steps and store in BuildSetting instance
	 * and then the tool chain adapter will get the
	 * setting item include API items, startup component items and it's sub items,
	 * device items
	 */
	protected void collectSettings() {
		// reset all
		clear();

		if (getDeviceInfo() == null) {
			return;
		}
		// insert default settings
		headers.put(CmsisConstants.LITE_LITE_Components_h, Messages.LiteConfiguration_ComponentSelection);

		// collect api items setting informations, this is parent api item
		ICpItem apisItem = fConfigInfo.getFirstChild(CmsisConstants.APIS_TAG);
		if (apisItem != null) {
			collectComponentSettings(apisItem);
		}

		// collect component setting informations, this is parent components
		ICpItem componentsItem = fConfigInfo.getFirstChild(CmsisConstants.COMPONENTS_TAG);
		if (componentsItem != null) {
			collectComponentSettings(componentsItem);
		}

		// collect device setting informations
		ICpDeviceInfo info = getDeviceInfo();
		if (info != null) {
			collectDeviceSettings(info);
			collectSVDFiles(info);
		}
	}

	protected void collectSVDFiles(ICpDeviceInfo di) {

		ICpItem props = di.getEffectiveProperties();
		if (props == null) {
			return;
		}

		Collection<? extends ICpItem> children = props.getChildren();
		for (ICpItem p : children) {
			String tag = p.getTag();

			if (tag.equals(CmsisConstants.DEBUG_TAG)) {
				String svdFile = p.getEffectiveAttribute(CmsisConstants.SVD);
				String abs = p.getAbsolutePath(svdFile);

				ICpDeviceItem d = di.getDevice();
				String name = d.getDeviceName();

				fSVDFiles.put(name, abs);
			}
		}

	}
	/**
	 * collect the "subFamily" element info of the devices
	 * include "define, pdefine, compile and header" information
	 *
	 * @param di ICpDeviceInfo instance
	 */
	protected void collectDeviceSettings(ICpDeviceInfo di) {
		ICpDeviceItem d = di.getDevice();
		if (d == null) {
			return;
		}

		String devName = d.getDeviceName();
		liteBuildSettings.setDeviceAttributes(di.attributes());
		ICpItem props = di.getEffectiveProperties();
		if (props == null) {
			return;
		}

		Collection<? extends ICpItem> children = props.getChildren();
		for (ICpItem p : children) {
			String tag = p.getTag();

			if (tag.equals(CmsisConstants.COMPILE_TAG)) {
				String define = p.getAttribute(CmsisConstants.DEFINE);
				String[] defines = Utils.splitStringBlank(define);
				for (String s : defines) {
					symbols.put(s, devName);
				}

				String pdefine = p.getAttribute(CmsisConstants.PDEFINE);
				String[] pdefines = Utils.splitStringBlank(pdefine);
				for (String sp : pdefines) {
					symbols.put(sp, devName);
				}

				String headerPath = p.getAttribute(CmsisConstants.HEADER);
				if (headerPath != null && !headerPath.isEmpty()) {
					deviceHeader = DeviceUIUtils.extractFileName(headerPath);
					// check if header is already defined via device startup
					// component
					boolean inserted = false;
					for (String h : headers.keySet()) {
						if (DeviceUIUtils.extractFileName(h).equals(deviceHeader)) {
							inserted = true;
							break;
						}
					}
					if (!inserted) {
						headerPath = p.getAbsolutePath(headerPath);
						headerPath = CpVariableResolver.insertCmsisRootVariable(headerPath);

						String srcPath = ProjectUtils.removeLastPathSegment(headerPath);

						String className = p.getAttribute(CmsisConstants.CCLASS);
						log("HEADER 1 " + headerPath + " " + new Throwable().getStackTrace()[0].getLineNumber());

						headerPath = getHeaderPathRelativeToProject(fConfigInfo, headerPath, className, devName);

						log("HEADER 2 " + headerPath + " " + new Throwable().getStackTrace()[0].getLineNumber());
						headerPath = ProjectUtils.removeLastPathSegment(headerPath);
						log("HEADER 3 " + headerPath + " " + new Throwable().getStackTrace()[0].getLineNumber());

						projectIncludeFiles.put(srcPath, headerPath);
						headerPath = adjustRelativePath(headerPath);

						log("HEADER 4 " + headerPath + " " + new Throwable().getStackTrace()[0].getLineNumber());

						// first step to collect files
						collectExtFiles(fExtPaths, headers, headerPath, Messages.LiteConfiguration_DeviceHeader, EFileCategory.HEADER);

						// next step to add files to build tool options
						//ConfigurationUtils.applyOptionsToToolchain(fExtPaths, liteBuildSettings);
					}
				}
			}
		}
	}

	/**
	 * Collects SCVD files for component viewer
	 * @param fi ICpFileInfo
	 */
	protected void collectScvdFile(ICpFileInfo fi) {
		ICpPack pack = fi.getPack();
		if (fi.getCategory() == EFileCategory.OTHER && fi.getName().endsWith(CmsisConstants.EXT_SCVD)
		        && pack != null && pack.getPackState() == PackState.INSTALLED) {
			fScvdFiles.put(pack.getAbsolutePath(fi.getName()), fi);
		}

	}

	/**
	 * collect component setting and files to fill build settings
	 *
	 * @param componentsParent component parent item
	 */
	protected void collectComponentSettings(ICpItem componentsParent) {
		Collection<? extends ICpItem> components = componentsParent.getChildren();
		if (components == null || components.isEmpty()) {
			return;
		}
		for (ICpItem child : components) {
			if (child instanceof ICpComponentInfo) {
				collectComponentSettings((ICpComponentInfo) child);
			}
		}
	}

	/**
	 * collect component setting with component information
	 * TODO collect the files and transfer to template process, which will copy
	 * the source file to project file
	 * @param ci ICpComponentInfo instance
	 */
	protected void collectComponentSettings(ICpComponentInfo ci) {

		// collect specific components
		if (ci.isDeviceStartupComponent()) {
			deviceStartupComponent = ci;
		} else if (ci.isCmsisCoreComponent()) {
			cmsisCoreComponent = ci;
		} else if (ci.isCmsisRtosComponent()) {
			cmsisRtosComponent = ci;
		}
		ICpComponent c = ci.getComponent();
		int count = ci.getInstanceCount();
		if (c != null) {
			addLiteComponentsHCode(liteComponentsH, c, count);
		}

		// collect files of the components
		collectComponentFiles(ci);
	}

	protected void collectComponentFiles(ICpComponentInfo ci) {
		Collection<? extends ICpItem> children = ci.getChildren();
		if (children == null || children.isEmpty()) {
			return;
		}
		boolean bMultiInstance = ci.isMultiInstance();
		int count = ci.getInstanceCount();

		for (ICpItem child : children) {
			// filter file item
			if (!(child instanceof ICpFileInfo)) {
				continue;
			}

			ICpFileInfo fi = (ICpFileInfo) child;
			if (bMultiInstance && fi.getRole() == EFileRole.CONFIG) {
				for (int i = 0; i < count; i++) {
					collectFiles(fi, ci, i);
				}
			} else {
				// not configuration file
				collectFiles(fi, ci, -1);
			}
		}
	}

	/**
	 * Collects setting information from file elements
	 *
	 * @param fi
	 *            {@link ICpFileInfo}
	 * @param ci
	 *            parent {@link ICpComponentInfo}
	 * @param index
	 *            for MULTI-instance components : instance index, for others -1
	 */
	protected void collectFiles(ICpFileInfo fi, ICpComponentInfo ci, int index) {
		String name = fi.getName();

		ICpFile f = fi.getFile();
		String absPath = null;
		String effectivePath = null;
		if (f != null) {
			absPath = f.getAbsolutePath(name);
		}

		/**
		 * collect the source files, library files and others to project directory
		 */
		EFileRole role = fi.getRole();
		if (isSourceFileToProject(fi)) {
			String className = ci.getAttribute(CmsisConstants.CCLASS);
			String deviceName = fConfigInfo.getDeviceInfo().getDeviceName();

			effectivePath = getPathRelativeToProject(fConfigInfo, fi, className, deviceName, index);
			projectSourceFiles.put(effectivePath, fi);

			// library is added as source file to project directory
			if (fi.getCategory() != EFileCategory.LIBRARY) {
				if (fi.isGenerated() || (role != EFileRole.CONFIG && role != EFileRole.COPY)) {
					effectivePath = CpVariableResolver.insertCmsisRootVariable(absPath);
				}
			}

		} else if (isIncludeFileToProject(fi)) {
			String className = ci.getAttribute(CmsisConstants.CCLASS);
			String deviceName = fConfigInfo.getDeviceInfo().getDeviceName();

			effectivePath = getIncludePathRelativeToProject(fConfigInfo, fi, className, deviceName);
			log("INCLUDE 1 " + effectivePath + "||" + absPath + " " + new Throwable().getStackTrace()[0].getLineNumber());

			if (absPath != null) {
				IPath abs = new Path(absPath);
				if (abs.isAbsolute()) {
					File fs = new File(absPath);
					String srcPath = absPath;
					if (!fs.isDirectory()) {
						srcPath =  ProjectUtils.removeLastPathSegment(absPath);
					}
					projectIncludeFiles.put(srcPath, effectivePath);//@2017.7.20
				}
			}

			effectivePath = adjustRelativePath(effectivePath);
			log("INCLUDE 2 " + effectivePath + " " + new Throwable().getStackTrace()[0].getLineNumber());

		} else if ( isHeaderFileToProject(fi)) {
			String className = ci.getAttribute(CmsisConstants.CCLASS);
			String deviceName = fConfigInfo.getDeviceInfo().getDeviceName();

			effectivePath = getIncludePathRelativeToProject(fConfigInfo, fi, className, deviceName);

			if (effectivePath != null) {
				effectivePath = ProjectUtils.removeLastPathSegment(effectivePath);
			}
			log("HEADER 1 " + effectivePath + " " + new Throwable().getStackTrace()[0].getLineNumber());

			if (absPath != null) {
				IPath abs = new Path(absPath);
				if (abs.isAbsolute()) {
					File fs = new File(absPath);
					if (fs.isDirectory()) {
						String srcPath =  ProjectUtils.removeLastPathSegment(absPath);
						projectIncludeFiles.put(srcPath, effectivePath);
					}
				}
			}

			effectivePath = adjustRelativePath(effectivePath);
			log("HEADER 2 " + effectivePath + " " + new Throwable().getStackTrace()[0].getLineNumber());
		} else {
			effectivePath = CpVariableResolver.insertCmsisRootVariable(absPath);
			log(" OTHERS " + effectivePath + fi.getCategory());
		}

		// EFileCategory.LINKER_SCRIPT = "linkerScript"
		EFileCategory cat = fi.getCategory();
		//if (cat == EFileCategory.LINKER_SCRIPT && !ci.isDeviceStartupComponent()) {
		if (cat == EFileCategory.LINKER_SCRIPT ) {
			// filter the linker script, all linker script will be generated by tool chain
			return;
		}

		// collect source file paths
		String componentName = ci.getName();

		// first step to collect files
		collectExtFiles(fExtPaths, headers, effectivePath, componentName, cat);
		//ConfigurationUtils.applyOptionsToToolchain(fExtPaths, liteBuildSettings);

		// library file
		if (cat == EFileCategory.LIBRARY) {
			// collect source library path
			addLibrarySourcePaths(libSourcePaths, f);
		}

		ICpPack pack = ci.getPack();
		// filter only installed and template elements
		if (role == EFileRole.TEMPLATE && pack != null && (pack.getPackState() == PackState.INSTALLED)) {
			String className = ci.getAttribute(CmsisConstants.CCLASS);
			ICpCodeTemplate component = (ICpCodeTemplate) fCodeTemplateRoot.getFirstChild(className);
			if (component == null) {
				component = new CpCodeTemplate(fCodeTemplateRoot, className, ci);
				fCodeTemplateRoot.addChild(component);
			}

			String selectName = fi.getAttribute(CmsisConstants.SELECT);
			ICpCodeTemplate codeTemplate = (ICpCodeTemplate) component.getFirstChild(selectName);
			if (codeTemplate == null) {
				codeTemplate = new CpCodeTemplate(component, selectName, fi);
				component.addChild(codeTemplate);
			}

			codeTemplate.addCodeTemplate(fi.getAttribute(CmsisConstants.NAME));
		}

		collectScvdFile(fi);
	}

	/************************************************ Utilization functions ********************************************/
	/**
	 * add files to category, at this pointer, place file filter to collect and adjust project features
	 *
	 * @param docs              doc category
	 * @param headers           header category
	 * @param liteBuildSettings build setting container
	 * @param effectivePath     file path
	 * @param cat               file category tag
	 * @param comment           file comment section
	 * @param svdFile           SVD category
	 */
	protected void addFiles(Map<String, String> docs, Map<String, String> headers, LiteBuildSettings liteBuildSettings,
	                        String effectivePath, EFileCategory cat, String componentName) {

		if (effectivePath == null || effectivePath.isEmpty()) {
			return;
		}

		switch (cat) {
		case DOC:
			docs.put(effectivePath, componentName);
			break;
		case HEADER:
			headers.put(effectivePath, componentName);
			if (!effectivePath.isEmpty()) {
				effectivePath = ProjectUtils.removeLastPathSegment(effectivePath);
			}
		case INCLUDE:
			if (!effectivePath.isEmpty()) {
				effectivePath = adjustRelativePath(effectivePath);
				liteBuildSettings.addStringListValue(IBuildSettings.LITE_INCLUDE_PATH, DeviceUIUtils.removeTrailingSlash(effectivePath));
			}
			break;
		case IMAGE:
			break;
		case LIBRARY:
			// the library file need copy from pack to project
			effectivePath = adjustRelativePath(effectivePath);
			liteBuildSettings.addStringListValue(IBuildSettings.LITE_LIBRARIES, effectivePath);
			effectivePath = ProjectUtils.removeLastPathSegment(effectivePath);
			liteBuildSettings.addStringListValue(IBuildSettings.LITE_LIBRARY_PATHS,	DeviceUIUtils.removeTrailingSlash(effectivePath));
			break;
		case LINKER_SCRIPT:
			// linker script file come from pack
			effectivePath = adjustRelativePath(effectivePath);
			liteBuildSettings.addStringListValue(IBuildSettings.LITE_LINKER_SCRIPT, effectivePath);
			break;
		case OBJECT:
			//? some objects
			effectivePath = adjustRelativePath(effectivePath);
			liteBuildSettings.addStringListValue(IBuildSettings.LITE_OBJECTS, effectivePath);
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

	protected static void collectExtFiles(Map<String, EFileCategory> fExtPaths, Map<String, String> headers, String effectivePath, String componentName, EFileCategory cat) {

		if (effectivePath == null || effectivePath.isEmpty()) {
			return;
		}

		switch (cat) {
		case DOC:
			fExtPaths.put(effectivePath, cat);
			break;
		case HEADER:
			headers.put(effectivePath, componentName);
		case INCLUDE:
			fExtPaths.put(effectivePath, cat);
			break;
		case LIBRARY:
			// the library file need copy from pack to project
			effectivePath = adjustRelativePath(effectivePath);
			fExtPaths.put(DeviceUIUtils.removeTrailingSlash(effectivePath), cat);
			effectivePath = ProjectUtils.removeLastPathSegment(effectivePath);
			fExtPaths.put(DeviceUIUtils.removeTrailingSlash(effectivePath), EFileCategory.LIBRARY2);
			break;
		case LINKER_SCRIPT:
			// linker script file come from pack
			effectivePath = adjustRelativePath(effectivePath);
			fExtPaths.put(effectivePath, cat);
			break;
		case OBJECT:
			//? some objects
			effectivePath = adjustRelativePath(effectivePath);
			fExtPaths.put(effectivePath, cat);
			break;
		default:
			break;
		}
	}



	/**
	 * add library source path to file container
	 * @param set container
	 * @param f   ICpFile instance
	 */
	protected static void addLibrarySourcePaths(Set<String> set, ICpFile f) {
		if (f == null) {
			return;
		}
		String src = f.getAttribute(CmsisConstants.SRC);
		if (src == null || src.isEmpty()) {
			return;
		}

		String[] paths = src.split(";"); //$NON-NLS-1$
		if (paths == null || paths.length == 0) {
			return;
		}

		for (String p : paths) {
			if (p == null || p.isEmpty()) {
				continue;
			}

			String absPath = f.getAbsolutePath(p);
			String path = CpVariableResolver.insertCmsisRootVariable(absPath);
			set.add(path);
		}
	}

	/**
	 * Adds CmsisConstants.PROJECT_LOCAL_PATH prefix if path is relative
	 *
	 * @param path
	 *            path to adjust
	 * @return
	 */
	protected static String adjustRelativePath(String path) {

		if (path == null || path.isEmpty())
			return path;

		if (path.startsWith(CmsisConstants.LITE_INCLUDE_INDICATION)) {
			return CmsisConstants.PROJECT_LOCAL_PATH + path;
		}
		if (path.startsWith(CmsisConstants.LITE_SOURCE_INDICATION)) {
			return CmsisConstants.PROJECT_LOCAL_PATH + path;
		}
		if (path.startsWith(CmsisConstants.CMSIS_PACK_ROOT_VAR)) {
			return path;
		}
		if (path.startsWith(CmsisConstants.CMSIS_LITE_VAR)) {
			return path;
		}
		return CmsisConstants.CMSIS_LITE_VAR + path;
	}

	/**
	 * Adds piece of LiteComponents.h code for the component
	 *
	 * @param c
	 *            ICpComponent
	 * @param count
	 *            number of component instances
	 * @return code string
	 */
	protected static void addLiteComponentsHCode(List<String> container, ICpComponent c, int count) {
		String code = c.getLiteComponentsHCode();
		if (code == null || code.isEmpty()) {
			return;
		}
		// convert all line endings to Unix format
		code = code.replaceAll("\\\\r\\\\n", "\\\\n"); //$NON-NLS-1$ //$NON-NLS-2$
		int index = code.indexOf(CmsisConstants.pINSTANCEp);
		if (index >= 0) {
			for (int i = 0; i < count; i++) {
				String instance = String.valueOf(i);
				String tmp = code.replaceAll(CmsisConstants.pINSTANCEp, instance);
				container.add(tmp);
			}
		} else {
			container.add(code);
		}
	}


	/**
	 * Check if file is generated and relative to project (=> to CONFIG file
	 * directory)
	 *
	 * @param fi
	 *            {@link ICpFileInfo} to check
	 * @return true if file is resolved to a generated file that is relative to
	 *         project directory
	 */
	protected static boolean isGeneratedAndRelativeToProject(ICpConfigurationInfo configInfo, ICpFileInfo fi) {
		ICpFile f = fi.getFile();
		if (f != null && f.isGenerated()) {
			String abs = f.getAbsolutePath(f.getName());
			String base = configInfo.getDir(true);
			if (abs.startsWith(base)) {
				return true;
			}
		}
		return false;
	}

	/**
	 *
	 * @param fi
	 *            {@link ICpFileInfo}
	 * @param className
	 * @param deviceName
	 * @param index
	 * @return
	 */
	protected static String getPathRelativeToProject(ICpConfigurationInfo configInfo, ICpFileInfo fi, String className, String deviceName, int index) {
		if (fi == null) {
			return null;
		}
		if (fi.isGenerated()) {
			ICpFile f = fi.getFile();
			String absPath = f.getAbsolutePath(f.getName());
			String baseDir = configInfo.getDir(false);
			if (absPath.startsWith(baseDir)) {
				// the file is within project
				return DeviceUIUtils.makePathRelative(absPath, baseDir);
			}
		}

		// this is the base directory
		String path = CmsisConstants.LITE_SOURCE_INDICATION;
		path += '/';
		if (className != null && !className.isEmpty()) {
			path += DeviceUIUtils.wildCardsToX(className) + '/';
			// escape spaces with underscores
		}
		if (fi.isDeviceDependent() && deviceName != null && !deviceName.isEmpty()) {
			path += DeviceUIUtils.wildCardsToX(deviceName) + '/';
		}

		String fileName = DeviceUIUtils.extractFileName(fi.getName());
		if (index >= 0) {
			String ext = DeviceUIUtils.extractFileExtension(fileName);
			fileName = DeviceUIUtils.extractBaseFileName(fileName);
			fileName += "_" + String.valueOf(index); //$NON-NLS-1$
			if (ext != null) {
				fileName += "." + ext; //$NON-NLS-1$
			}
		}
		return path + fileName;
	}

	protected static String getHeaderPathRelativeToProject(ICpConfigurationInfo configInfo, String headerPath, String className, String deviceName) {
		if (headerPath == null) {
			return null;
		}

		// this is the base directory
		String path = CmsisConstants.LITE_INCLUDE_INDICATION;
		path += '/';
		if (className != null && !className.isEmpty()) {
			path += DeviceUIUtils.wildCardsToX(className) + '/';
			// escape spaces with underscores
		}
		if (deviceName != null && !deviceName.isEmpty()) {
			path += DeviceUIUtils.wildCardsToX(deviceName) + '/';
		}

		String fileName = DeviceUIUtils.extractFileName(headerPath);
		return path + fileName;
	}

	protected static String getIncludePathRelativeToProject(ICpConfigurationInfo configInfo, ICpFileInfo fi, String className, String deviceName) {
		if (fi == null) {
			return null;
		}

		if (fi.isGenerated()) {
			ICpFile f = fi.getFile();
			String absPath = f.getAbsolutePath(f.getName());
			String baseDir = configInfo.getDir(false);
			if (absPath.startsWith(baseDir)) {
				// the file is within project
				return DeviceUIUtils.makePathRelative(absPath, baseDir);
			}
		}

		// this is the base directory will be replace to project path is ConfigureationUtils class
		String path =  CmsisConstants.LITE_INCLUDE_INDICATION;
		path += '/';
		if (className != null && !className.isEmpty()) {
			path += DeviceUIUtils.wildCardsToX(className) + '/';
			// escape spaces with underscores
		}
		if (fi.isDeviceDependent() && deviceName != null && !deviceName.isEmpty()) {
			path += DeviceUIUtils.wildCardsToX(deviceName) + '/';
		}

		String fileName = DeviceUIUtils.extractFileName(fi.getName());
		return path + fileName;
	}

	private static void log(String msg) {
		System.out.println(msg);
	}
}
