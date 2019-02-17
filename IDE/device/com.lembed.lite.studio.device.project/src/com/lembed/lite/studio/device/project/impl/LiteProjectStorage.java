/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* Copyright (c) 2017 LEMBED
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICStorageElement;
import org.eclipse.cdt.core.settings.model.XmlStorageUtil;
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.core.parser.CpConfigParser;
import com.lembed.lite.studio.device.core.storage.CStorageConfigParser;
import com.lembed.lite.studio.device.generic.Attributes;
import com.lembed.lite.studio.device.generic.IAttributes;
import com.lembed.lite.studio.device.project.utils.ConfigurationUtils;
import com.lembed.lite.studio.device.toolchain.ILiteToolChainAdapter;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterFactory;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterInfo;

/**
 * The class responsible for storing/restoring lite-related information in
 * ICStorageElement
 */
public class LiteProjectStorage {

	protected String fLiteConfigurationName = null; 

	protected ILiteConfiguration liteConfiguration = null;
	protected IAttributes fConfigAttributes = null; 

	// associated IRteConfiguration name (by default equals project name)
	protected IAttributes fDeviceAttributes = null; 
	// device used by configuration

	protected String fToolChainAdapterId = null; 
	// associated IRteToolchainAdapter id
	protected String fToolChainAdapterName = null; 
	// associated IRteToolchainAdapter name
	
	protected String fToolChain = null; 
	// associated IRteToolchain
	protected String fToolChainToutputType = null; 
	// associated IRteToolchainToutputType

	protected LiteToolChainAdapterInfo fToolChainAdapterInfo = null;

	protected Map<String, String> fConfigFileVersions = new HashMap<String, String>(); 
	// config file: name to version
	
	protected Map<String, String> features = new HashMap<String, String>(); 

	public LiteProjectStorage() {
	}

	public LiteProjectStorage(String rteConfigurationName, LiteToolChainAdapterInfo adapterInfo) {
		setLiteConfigurationName(rteConfigurationName);
		setToolChainAdapterInfo(adapterInfo);
	}

	/**
	 * Return lite configuration name associated with the build configuration
	 * 
	 * @return lite configuration name
	 */
	public String getLiteConfigurationName() {
		return fLiteConfigurationName;
	}

	/**
	 * Associates lite configuration with build configuration
	 * 
	 * @param rteConfigurationName
	 *            name of lite configuration to associate
	 */
	public void setLiteConfigurationName(String liteConfigurationName) {
		fLiteConfigurationName = liteConfigurationName;
	}

	public void setLiteConfiguration(ILiteConfiguration liteConf){
		if(liteConf != null){
			ICpDeviceInfo deviceInfo = liteConf.getDeviceInfo();
			if(deviceInfo != null){
				setDeviceInfo(deviceInfo);
			}
		}
		liteConfiguration = liteConf;
	}


	public ILiteConfiguration getLiteConfiguration(){
		return  liteConfiguration;
	}

		/**
	 * Returns attributes of selected device
	 * 
	 * @return IAttributes
	 */
	public IAttributes getConfigurationAttributes() {
		return fConfigAttributes;
	}


	/**
	 * Returns toolchain adapter info associated with configuration
	 * 
	 * @return LiteToolChainAdapterInfo
	 */
	public LiteToolChainAdapterInfo getToolChainAdapterInfo() {
		return fToolChainAdapterInfo;
	}

	/**
	 * Associates tool chain adapter with the configuration
	 * 
	 * @param info
	 *            LiteToolChainAdapterInfo
	 */
	public void setToolChainAdapterInfo(LiteToolChainAdapterInfo info) {
		fToolChainAdapterInfo = info;
		if (info != null) {
			fToolChainAdapterId = info.getId();
			fToolChainAdapterName = info.getName();
		} else {
			fToolChainAdapterId = null;
			fToolChainAdapterName = null;
		}
	}

	/**
	 * Returns human-readable name of toolchain adapter associated with
	 * configuration
	 * 
	 * @return tool chain adapter name
	 */
	public String getToolChainAdapterName() {
		return fToolChainAdapterName;
	}

	public Map<String, String> getProjectFeatures() {
		return features;
	}
	
	public void setProjectFeatures(Map<String, String> features){
		this.features = features;
	}
	
	/**
	 * Returns id of tool chain adapter associated with configuration
	 * 
	 * @return tool chain adapter id
	 */
	public String getToolChainAdapterId() {
		return fToolChainAdapterId;
	}

	/**
	 * Returns tool chain adapter associated with configuration
	 * 
	 * @return IRteToolChainAdapter
	 */
	public ILiteToolChainAdapter getToolChainAdapter() {
		if (fToolChainAdapterInfo != null){
			return fToolChainAdapterInfo.getToolChainAdapter();
		}
		return null;
	}

	/**
	 * Returns attributes of selected device
	 * 
	 * @return IAttributes
	 */
	public IAttributes getDeviceAttributes() {
		return fDeviceAttributes;
	}

	/**
	 * Sets device information
	 * 
	 * @param deviceInfo
	 *            ICpDeviceInfo object
	 */
	public void setDeviceInfo(ICpDeviceInfo deviceInfo) {
		if (deviceInfo == null) {
			fDeviceAttributes = null;
		} else {
			fDeviceAttributes = new Attributes(deviceInfo.attributes());
			SetConfigVersion(deviceInfo);
		}
	}
	
	public void SetConfigVersion(ICpDeviceInfo deviceInfo){
		ICpPack pack = deviceInfo.getPack();
		String version = pack.getVersion();
		String id = pack.getPackId();
		setConfigFileVersion(id, version);
	}

	/**
	 * Returns version of a config file last copied to the project
	 * 
	 * @param name
	 *            project-relative filename
	 * @return file version string
	 */
	public String getConfigFileVersion(String name) {
		return fConfigFileVersions.get(name);
	}

	/**
	 * Sets version of config file copied to the project
	 * 
	 * @param name
	 *            project-relative filename
	 * @param version
	 *            file version
	 */
	public void setConfigFileVersion(String name, String version) {
		fConfigFileVersions.put(name, version);
	}

	/**
	 * Removes config file version information
	 * 
	 * @param name
	 *            project-relative filename
	 */
	public void removeConfigFileVersion(String name) {
		fConfigFileVersions.remove(name);
	}

	/**
	 * Loads lite-related information from ICConfigurationDescription
	 * 
	 * @throws CoreException
	 */
	public void load(ICProjectDescription projDesc) throws CoreException {
		ICStorageElement storage = projDesc.getStorage(CmsisConstants.LITE_STORAGE, false);
		if (storage == null) {
			// project not initialized jet => ignore
			return;
		}
		
		String xmlConf = null;
		CStorageConfigParser cparser = new CStorageConfigParser();
		CpConfigParser parser = new CpConfigParser();
		
		fDeviceAttributes = null;
		ICStorageElement[] elements = storage.getChildren();
		for (ICStorageElement e : elements) {
			String name = e.getName();
			switch (name) {
			case CmsisConstants.LITE_TOOLCHAIN_ADAPTER:
				fToolChainAdapterId = e.getAttribute(CmsisConstants.ID);
				fToolChainAdapterName = e.getAttribute(CmsisConstants.NAME);
				break;
			case CmsisConstants.LITE_TOOLCHAIN:
				fToolChain = e.getAttribute(CmsisConstants.TCOMPILER);
				fToolChainToutputType = e.getAttribute(CmsisConstants.TOUTPUT);
				break;
			case CmsisConstants.DEVICE_TAG: 
				fDeviceAttributes = loadAttributes(e);
				break;
			case CmsisConstants.LITE_PROJECT_FEATURE:
				features = loadAttributes(e).getAttributesAsMap();
				break;
			case CmsisConstants.CONFIGURATION_TAG:
				xmlConf = cparser.writeToXmlString(e);
				if (xmlConf != null) {					
					ICpConfigurationInfo info = (ICpConfigurationInfo) parser.parseXmlString(xmlConf);
					if(liteConfiguration == null){	
						liteConfiguration = ConfigurationUtils.updateConfiguration(info);
					}
					if(liteConfiguration != null){
						liteConfiguration.setConfigurationInfo(info);
					}
				}
				break;
			case CmsisConstants.FILES_TAG:
				loadConfigFileInfos(e);
				break;
			}
		}
		initializeToolChainAdapter();
	}

	protected IAttributes loadAttributes(ICStorageElement e) {
		IAttributes attributes = null;
		String[] names = e.getAttributeNames();
		if (names != null && names.length > 0) {
			attributes = new Attributes();
			for (String key : names) {
				String value = e.getAttribute(key);
				attributes.setAttribute(key, value);
			}
		} 
		return attributes;
	}

	protected void saveAttributes(ICStorageElement e, IAttributes attributes) {
		if (e == null || attributes == null){
			return;
		}
		Map<String, String> attrMap = attributes.getAttributesAsMap();
		if(attrMap == null){
			return;
		}
		for (Entry<String, String> a : attrMap.entrySet()) {
			e.setAttribute(a.getKey(), a.getValue());			
		}
	}

	protected void loadConfigFileInfos(ICStorageElement element) {
		ICStorageElement[] elements = element.getChildren();
		fConfigFileVersions.clear();
		for (ICStorageElement e : elements) {
			if (e.getName().equals(CmsisConstants.FILE_TAG)) {
				String name = e.getAttribute(CmsisConstants.NAME);
				String version = e.getAttribute(CmsisConstants.VERSION);
				fConfigFileVersions.put(name, version);
			}
		}
	}

	protected LiteToolChainAdapterInfo initializeToolChainAdapter() {
		fToolChainAdapterInfo = null;
		if (fToolChainAdapterId != null) {
			LiteToolChainAdapterFactory adapterFactory = LiteToolChainAdapterFactory.getInstance();
			fToolChainAdapterInfo = adapterFactory.getAdapterInfo(fToolChainAdapterId);
		}
		return fToolChainAdapterInfo;
	}

	public void saveConfigurationInfo(ILiteConfiguration lconf, ICStorageElement e){
		CpConfigParser confParser = new CpConfigParser();
		if(liteConfiguration != null){
			Document document = confParser.writeToXmlDocument(liteConfiguration.getConfigurationInfo());
			if(document != null){
				ICStorageElement se = XmlStorageUtil.createCStorageTree(document);
				e.importChild(se);
			}
		}
	}

	/**
	 * Saves lite-related information to ICConfigurationDescription
	 * 
	 * @param configDesc
	 *            ICConfigurationDescription to store lite info to
	 * @throws CoreException
	 */
	public void save(ICProjectDescription projDesc) throws CoreException {
		ICStorageElement storage = projDesc.getStorage(CmsisConstants.LITE_STORAGE, true);
		storage.clear(); // clear last values

		if (liteConfiguration != null) {
			saveConfigurationInfo(liteConfiguration,storage);
		}
		
		if(features != null){
			ICStorageElement featureSe = storage.createChild(CmsisConstants.LITE_PROJECT_FEATURE);
			for(String key : features.keySet()){
				featureSe.setAttribute(key, features.get(key));
			}
		}

		if (fToolChainAdapterId != null && !fToolChainAdapterId.isEmpty()) {
			ICStorageElement se = storage.createChild(CmsisConstants.LITE_TOOLCHAIN_ADAPTER);
			se.setAttribute("id", fToolChainAdapterId); //$NON-NLS-1$
			if (fToolChainAdapterName != null){
				se.setAttribute("name", fToolChainAdapterName); //$NON-NLS-1$
			}
		}

		if (fToolChain != null && !fToolChain.isEmpty()) {
			ICStorageElement se = storage.createChild(CmsisConstants.LITE_TOOLCHAIN);
			se.setAttribute("Tcompiler", fToolChain); //$NON-NLS-1$
			if (fToolChainToutputType != null){
				se.setAttribute("Toutput", fToolChainToutputType); //$NON-NLS-1$
			}
		}
	}

	private void log(String msg){
		System.out.println(">>>>" + msg);
	}

}
