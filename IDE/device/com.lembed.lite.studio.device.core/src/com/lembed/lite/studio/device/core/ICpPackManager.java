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

package com.lembed.lite.studio.device.core;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpBoard;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.data.ICpPackCollection;
import com.lembed.lite.studio.device.core.data.ICpPackFamily;
import com.lembed.lite.studio.device.core.events.ILiteEventProxy;
import com.lembed.lite.studio.device.core.lite.boards.ILiteBoardDeviceItem;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.core.lite.examples.ILiteExampleItem;
import com.lembed.lite.studio.device.core.parser.ICpXmlParser;
import com.lembed.lite.studio.device.core.repository.CpRepositoryList;
import com.lembed.lite.studio.device.generic.IAttributes;

/**
 *  Interface to a Pack manager responsible for loading CMSIS-Packs
 */
public interface ICpPackManager {

	/**
	 * Sets IRteEventProxy to be used by this manger to fire notifications
	 * @param liteEventProxy IRteEventProxy object
	 */
	void setLiteEventProxy(ILiteEventProxy liteEventProxy);

	/**
	 * Returns IRteEventProxy object set by setRteEventProxy()
	 * @return IRteEventProxy object or null if none has been set
	 * @see #setLiteEventProxy(ILiteEventProxy)
	 */
	ILiteEventProxy getLiteEventProxy();


	/**
	 * Sets ICpPackInstaller to be used by this manger to install packs
	 * @param packInstaller ICpPackInstaller object
	 */
	void setPackInstaller(ICpPackInstaller packInstaller);


	/**
	 * Returns ICpPackInstaller object set by setPackInstaller()
	 * @return ICpPackInstaller object or null if none has been set
	 * @see #setLiteEventProxy(ILiteEventProxy)
	 */
	ICpPackInstaller getPackInstaller();


	/**
	 * Initializes XML parser, optionally sets XSD schema file to use
	 * @param xsdFile schema file or null if no schema check should be used
	 * @return true if successful
	 */
	boolean initParser(String xsdFile);


	/**
	 * Sets XML parser to be used to load packs
	 * @param xmlParser parser to use
	 */
	void setParser(ICpXmlParser xmlParser);

	/**
	 * Returns current XML parser used to load packs
	 * @return current XML parser
	 */
	ICpXmlParser getParser();


	/**
	 *  Clears the manager
	 */
	void clear();


	/**
	 *  Clears the manager and deletes the parser
	 */
	void destroy();

	/**
	 * Returns collection of all the packs
	 * @return all packs as ICpPackCollection
	 */
	ICpPackCollection getPacks();

	/**
	 * Returns collection of the installed packs
	 * @return collection of the installed packs
	 */
	ICpPackCollection getInstalledPacks();

	/**
	 * Returns collection of the device-specific packs
	 * @return all device specific installed packs as ICpPackCollection
	 */
	ICpPackCollection getDevicePacks();

	/**
	 * Returns collection of the generic packs
	 * @return all generic installed packs as ICpPackCollection
	 */
	ICpPackCollection getGenericPacks();

	/**
	 * Returns collection of the error packs
	 * @return all error packs as ICpPackCollection
	 */
	ICpPackFamily getErrorPacks();

	/**
	 * Returns hierarchical collection of all devices found in all packs
	 * @return device collection as IRteDeviceItem
	 */
	ILiteDeviceItem getDevices();

	/**
	 * Returns hierarchical collection of all devices found in installed packs
	 * @return
	 */
	ILiteDeviceItem getInstalledDevices();

	/**
	 * Returns collection of all board descriptions found in installed packs
	 * @return map of boards - id to ICpBoard item
	 */
	Map<String, ICpBoard> getBoards();

	/**
	 * Returns collection of all items, which contains
	 * all mounted and compatible devices
	 * @return IRteBoardDeviceItem root
	 */
	ILiteBoardDeviceItem getLiteBoardDevices();


	/**
	 * Returns collection of boards that contain mounted or compatible device matching suppled device attributes
	 * @return collection of compatible boards
	 */
	Collection<ICpBoard> getCompatibleBoards(IAttributes deviceAttributes);


	/**
	 * Returns collection of all available example descriptions
	 * @return IRteExampleItem
	 */
	ILiteExampleItem getExamples();

	/**
	 * Loads packs found in a supplied directory and sub-directories (up to 3 levels deep)
	 * @param rootDirectory directory to search for pdsc files
	 * @return true if all packs loaded successfully
	 */
	boolean loadPacks(String rootDirectory);

	/**
	 * Loads specified pdsc files
	 * @param fileNames collection of pdsc files to load
	 * @return true if all packs loaded successfully
	 */
	boolean loadPacks(Collection<String> fileNames);

	/**
	 * Parses  a single pdsc file
	 * @param absolute file pdsc file to load
	 * @return {@link ICpPack} is successful, null otherwise
	 */
	ICpPack readPack(String file);


	/**
	 * Readfs and loads a single gpdsc file if it is not yet loaded
	 * @param file absolute gpdsc file to load
	 * @return ICpPack if loaded successfully, null otherwise
	 */
	ICpPack loadGpdsc(String file);


	/**
	 * Returns CMSIS-Pack directory to load packs from
	 * @return the CMSIS-Pack directory
	 */
	String getCmsisPackRootDirectory();

	/**
	 * Return CMSIS-Pack Download Directory
	 * @return absolute download Directory of all the Packs
	 */
	default String getCmsisPackDownloadDir() {
		String root = getCmsisPackRootDirectory();
		if(root != null) {
			IPath path = new Path(root).append(CmsisConstants.DOT_DOWNLOAD);
			if (!path.toFile().exists()) {
				path.toFile().mkdirs();
			}
			return path.toOSString();
		}
		return null;
	}

	/**
	 * Return CMSIS-Pack Web directory (available packs)
	 * @return absolute web directory of all the Packs
	 */
	default String getCmsisPackWebDir() {
		String root = getCmsisPackRootDirectory();
		if(root != null) {
			IPath path = new Path(root).append(CmsisConstants.DOT_WEB);
			if (!path.toFile().exists()) {
				path.toFile().mkdirs();
			}
			return path.toOSString();
		}
		return null;
	}

	/**
	 * Returns CMSIS-Pack directory as URI
	 * @return CMSIS-Pack directory as URI
	 */
	URI getCmsisPackRootURI();

	/**
	 * Returns the list of CMSIS-Pack repository
	 * @return the list of CMSIS-Pack repository
	 */
	CpRepositoryList getCpRepositoryList();

	/**
	 * Sets CMSIS-Pack root directory to load packs from
	 * @param packRootDirectory pack root directory
	 */
	void setCmsisPackRootDirectory(String packRootDirectory);

	/**
	 * Checks is packs are already loaded
	 * @return true if packs are already loaded
	 */
	boolean arePacksLoaded();

	/**
	 *  Triggers reload of the pack if the have already been loaded
	 */
	void reload();
}