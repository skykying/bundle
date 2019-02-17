/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.toolchain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;

/**
 * Responsible for managing tool chain adapters contributed through the extension point
 *
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class LiteToolChainAdapterFactory {

	private static LiteToolChainAdapterFactory theFactory = null;
	
	public static final String ID = "id"; //$NON-NLS-1$
	public static final String TOOLCHAIN = "toolChain"; //$NON-NLS-1$
	public static final String TOOLCHAIN_ADAPTER = "toolChainAdapter"; //$NON-NLS-1$
	public static final String TOOLCHAIN_ADAPTER_EXTENSION_ID = Activator.PLUGIN_ID + ".ToolChainAdapter"; //$NON-NLS-1$
	public static final String TOOLCHAIN_ADAPTER_ASSOCIATIONS_EXTENSION_ID = Activator.PLUGIN_ID + ".ToolChainAdapterAssociation"; //$NON-NLS-1$
	
	
	private Map<String, LiteToolChainAdapterInfo> adapterInfos= new HashMap<String, LiteToolChainAdapterInfo>();  
	/**
	 *  Internal constructor 
	 */
	private LiteToolChainAdapterFactory() {
		initialize();
	}
	
	/**
	 * Singleton method that returns RteToolChainAdapterFactory instance
	 * @return RteToolChainAdapterFactory instance
	 */
	public static LiteToolChainAdapterFactory getInstance() {
		if(theFactory == null){
			theFactory = new LiteToolChainAdapterFactory();
		}
		return theFactory; 
	}
	
	
	/**
	 *  Destroys RteToolChainAdapterFactory singleton 
	 */
	public static void destroy() {
		theFactory = null;
	}
	
	
	/**
	 * Returns toolchain adapter for given ID 
	 * @param adapterId unique toolchain adapter ID 
	 * @return IRteToolChainAdapter object or null if not found
	 */
	public ILiteToolChainAdapter getAdapter(final String adapterId) {
		LiteToolChainAdapterInfo info = getAdapterInfo(adapterId);
		if(info != null){
			return info.getToolChainAdapter();
		}
		
		return null;
	}
	
	/**
	 * Returns toolchain adapter info for given ID 
	 * @param adapterId unique toolchain adapter ID 
	 * @return RteToolChainAdapterInfo object or null if not found
	 */
	public LiteToolChainAdapterInfo getAdapterInfo(final String adapterId) {
		return adapterInfos.get(adapterId);
	}
	

	/**
	 * Returns best matching adapter for given toolchain
	 * @param toolChain toolchain to get adapter for 
	 * @return RteToolChainAdapterInfo best matching given toolchain
	 */
	public LiteToolChainAdapterInfo getBestAdapterInfo(IToolChain toolChain) {
		LiteToolChainAdapterInfo bestInfo = null;
		int bestMatch = 99; 
		for(LiteToolChainAdapterInfo info :  adapterInfos.values()) {
			int match = info.matchToolChain(toolChain);
			if(match != -1 && match < bestMatch){
				bestInfo = info;
			}
		}
		return bestInfo;
	}


	/**
	 * Returns complete collection of adapters matching registed by the factory 
	 * @return entire collection of adapter infos 
	 */
	public Collection<LiteToolChainAdapterInfo> getAdapterInfos() {
		return adapterInfos.values();
	}

	
	/**
	 * Returns complete collection of adapters matching supplied toolchain ordered by relevance 
	 * @param toolChain toolchain to get adapters for
	 * @return collection of adapter infos matching given toolchain
	 */
	public Collection<LiteToolChainAdapterInfo> getAdapterInfos(IToolChain toolChain) {
		Map<String, LiteToolChainAdapterInfo> infos = new TreeMap<String, LiteToolChainAdapterInfo>();
		// first search only adapters with associations
		for(LiteToolChainAdapterInfo info :  adapterInfos.values()) {
			int match = info.matchToolChain(toolChain);
			if(match <= 0  || match == 99){
				continue;
			}
			String wheightId = String.valueOf(match); 
			wheightId += info.getId();
			infos.put(wheightId, info);
		}
		return infos.values();
	}

	/**
	 *  Loads extension point data
	 */
	private void initialize() {
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(TOOLCHAIN_ADAPTER_EXTENSION_ID).getExtensions();
		for(IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension.getConfigurationElements();
			for(IConfigurationElement config : configElements) {
				String configName = config.getName();
				if (configName.equals(TOOLCHAIN_ADAPTER)) { 
					LiteToolChainAdapterInfo info = new LiteToolChainAdapterInfo(config);
					String id = info.getId();
					if(getAdapterInfo(id) != null) {
						//TODO: issue warning
					}
					adapterInfos.put(id, info);
					
					// add toolchain associations
					IConfigurationElement[] toolChainConfigs = config.getChildren(TOOLCHAIN);
					for (IConfigurationElement toolChainConfig : toolChainConfigs) {
						info.addToolChainAssociation(toolChainConfig.getAttribute(ID));
					}
				}
			}
		}
		addToolChainAssociations();
	}

	private void addToolChainAssociations() {
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(TOOLCHAIN_ADAPTER_ASSOCIATIONS_EXTENSION_ID).getExtensions();
		for(IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension.getConfigurationElements();
			for(IConfigurationElement config : configElements) {
				String id = config.getAttribute(ID); 
				LiteToolChainAdapterInfo info = getAdapterInfo(id);
				if(info == null){
					continue;
				}
				
				IConfigurationElement[] toolChainConfigs = config.getChildren(TOOLCHAIN);
				for (IConfigurationElement toolChainConfig : toolChainConfigs) {
					info.addToolChainAssociation(toolChainConfig.getAttribute(ID));
				}
			}
		}
	}
	
	/**
	 * Checks if a toolchain (IToolchain) with given ID prefix is installed   
	 * @param toolchainPrefix a toolchain ID prefix 
	 * @return true if a toolchain is installed
	 */
	static public boolean isToolchainInstalled(String toolchainPrefix) {
		IToolChain[] toolChains = ManagedBuildManager.getExtensionToolChains();
		if(toolChains == null)
			return false;
		for(IToolChain tc : toolChains) {
			for(; tc != null; tc = tc.getSuperClass()) {
				String id = tc.getId();
				if(id.startsWith(toolchainPrefix)){
					return true;
				}
			}
		}
		return false;
	}
}
