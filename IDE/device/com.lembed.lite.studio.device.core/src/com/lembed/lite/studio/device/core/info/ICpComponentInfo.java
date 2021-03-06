/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/
package com.lembed.lite.studio.device.core.info;

import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpFile;
import com.lembed.lite.studio.device.core.data.ICpGenerator;
import com.lembed.lite.studio.device.core.enums.IEvaluationResult;


/**
 * Interface that describes a component instantiated in the configuration
 */
public interface ICpComponentInfo extends ICpComponent, ICpItemInfo, IEvaluationResult {

	/**
	 * Returns actual CMSIS component corresponding to this info  
	 * @return actual component
	 */
	ICpComponent getComponent();
	
	
	/**
	 * Sets actual CMSIS component to this info  
	 */
	void setComponent(ICpComponent component);
	
	
	/**
	 * Returns number of instantiated components
	 * @return number of instantiated components
	 */
	int getInstanceCount();


	/**
	 * Searches for file info corresponding supplied ICpFile 
	 * @param f ICpFile 
	 * @return the resulting ICpFileInfo or null if not found
	 */
	ICpFileInfo getFileInfo(ICpFile f); 
	
	
	/**
	 * Checks if this component info has been saved in the configuration 
	 * @return true if saved
	 */
	boolean isSaved(); 
	
	/**
	 * Sets saved flag to the component info  
	 * @parameter saved flag value to set
	 */
	void setSaved(boolean saved);
	
	/**
	 * Returns gpdsc filename associated with the item
	 * @param bExpandToAbsolute flag indicating to expand to absolute path, otherwise the value returned as provided by corresponding {@link ICpGenerator}
	 * @return gpdsc filename or null 
	 */
	String getGpdsc(boolean bExpandToAbsolute); 
	
}
