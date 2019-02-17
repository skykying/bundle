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

package com.lembed.lite.studio.device.core.info;

import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPackFilter;

/**
 *  Interface to store/load pack filter
 */
public interface ICpPackFilterInfo extends ICpItem {

	/**
	 * Creates pack filter based on information stored in the info 
	 * @return ICpPackFilter
	 */
	ICpPackFilter createPackFilter();
	
	/**
	 * Check is to latest versions of all installed packs 
	 * @return true if the latest versions of packs should be used
	 */
	boolean isUseAllLatestPacks();
	
	/**
	 * Sets if to use only latest versions of all installed packs 
	 * @param bUseLatest flag if to use latest 
	 */
	void setUseAllLatestPacks(boolean bUseLatest);

	
	
}
