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

package com.lembed.lite.studio.device.core.lite.packs;

import java.util.Map;

import com.lembed.lite.studio.device.core.data.ICpPackFilter;
import com.lembed.lite.studio.device.core.info.ICpPackFilterInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;

/**
 *  Interface to represent RTE view on ICpPackCollecion  
 */
public interface ILitePackCollection extends ILitePackItem {

	/**
	 * Creates Pack filter based on selection
	 * @return new ICpPackFilter
	 */
	ICpPackFilter createPackFiler();
	
	
	/**
	 * Sets ICpPackFilterInfo to the collection to initialize selection
	 * @param packFilterInfo
	 */
	void setPackFilterInfo(ICpPackFilterInfo packFilterInfo);
	
	/**
	 * Creates ICpPackFilterInfo based on selection
	 * @return ICpPackFilterInfo
	 */
	ICpPackFilterInfo createPackFilterInfo();

	/**
	 * Sets if to use only latest versions of all installed packs 
	 * @param bUseLatest flag if to use latest 
	 */
	void setUseAllLatestPacks(boolean bUseLatest);

	/**
	 * Returns child IRtePackFamily for given family id 
	 * @param familyId pack family id   
	 * @return IRtePackFamily 
	 */
	ILitePackFamily getRtePackFamily(String familyId);

	/**
	 * Sets used packs to the collection
	 * @param map of used packs (id to ICpPackInfo) 
	 */
	void setUsedPacks(Map<String, ICpPackInfo> usedPackInfos);
	
	/**
	 * Checks if the pack with the given id is used 
	 * @param id pack id
	 * @return true if used
	 */
	boolean isPackUsed(String id);
	
}
