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

import java.util.Collection;
import java.util.Set;

import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;

/**
 *  Represents RTE view on a ICpPackFamily 
 */
public interface ILitePackFamily  extends ILitePackItem {

	/**
	 * Returns child IRtePack for given version 
	 * @param version requested pack version or null to get the latest  
	 * @return IRtePack for given packId 
	 */
	ILitePack getRtePack(String version);

	/**
	 * Returns latest IRtePack  
	 * @return latest IRtePack in the family 
	 */
	ILitePack getLatestRtePack();
	
	
	/**
	 * Returns set of selected pack versions 
	 * @return set of selected pack versions   
	 */
	Set<String> getSelectedVersions();
	
	/**
	 * Returns set of selected pack versions 
	 * @return set of selected pack versions   
	 */
	Collection<ILitePack> getSelectedPacks();

	/**
	 * Sets version match mode that should be use when resolving the packs in family 
	 * @param mode version match mode to set
	 */
	void setVersionMatchMode(EVersionMatchMode mode);
	

	/**
	 * Updates family version match mode according to selection of packs 
	 */
	void updateVersionMatchMode();
	
	
}
