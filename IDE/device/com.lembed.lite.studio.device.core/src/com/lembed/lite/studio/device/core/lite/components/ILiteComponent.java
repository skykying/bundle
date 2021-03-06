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

package com.lembed.lite.studio.device.core.lite.components;

import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpGenerator;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;

/**
 * Class that represents component entity that can be selected 
 */
public interface ILiteComponent extends ILiteComponentItem {

	/**
	 * Sets/resets component selection  
	 * @param count number of instances to select, 0 to reset the component selection 
	 * @return true is selection state has changed 
	 */
	boolean setSelected(int count); 
	
	/**
	 * Returns number of selected instances  
	 * @return number of selected instances
	 */
	int getSelectedCount();
	

	/**
	 * Returns maximum number of instances that can be selected for the component, default is 1
	 * @return maximum number of component instances  
	 */
	int getMaxInstanceCount();
	
	
	/**
	 * Checks if component belongs to bundle
	 * @return true if the component belongs to a bundle
	 */
	boolean hasBundle();
	

	/**
	 * Returns number of used (instantiated) instances  
	 * @return number of used instances
	 */
	int getUseCount();

	
	/**
	 * Sets/updates active component info, purges all non-active ones  
	 * @param ci {@link ICpComponentInfo}
	 */
	void setActiveComponentInfo(ICpComponentInfo ci);

	
	/**
	 * Check is this RTE component represents a generated {@link ICpComponent} 
	 * @return bootstrap ICpComponent or null
	 */
	boolean isGenerated();

	
	/**
	 * Check is this RTE component has associated bootstrap 
	 * @return bootstrap ICpComponent or null
	 */
	boolean isBootStrap();
	
	
	/**
	 * Returns id of {@link ICpGenerator} associated with the component either as generated or a bootstrap  
	 * @return generator id or null
	 */
	String getGeneratorId();
}
