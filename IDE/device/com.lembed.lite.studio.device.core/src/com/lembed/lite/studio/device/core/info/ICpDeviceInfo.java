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

import java.util.Map;

import com.lembed.lite.studio.device.core.data.ICpDebugConfiguration;
import com.lembed.lite.studio.device.core.data.ICpDeviceItem;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.enums.IEvaluationResult;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;

/**
 *  Interface representing device used  in the configuration
 */
public interface ICpDeviceInfo extends ICpItemInfo, IEvaluationResult {

	/**
	 * Returns actual device represented by this info 
	 * @return actual device
	 */
	ICpDeviceItem getDevice(); 
	
	/**
	 * Sets actual device to this info
	 * @param device actual device to set
	 */
	void setDevice(ICpDeviceItem device);

	/**
	 * Sets actual device to this info using supplied IRteDeviceItem
	 * @param ILiteDeviceItem to access actual device
	 */
	void setLiteDevice(ILiteDeviceItem device);

	
	/**
	 * Returns effective device properties for associated processor
	 * @return effective device properties if any 
	 */
	ICpItem getEffectiveProperties();

	/**
	 * Returns device debug configuration for associated processor 
	 * @return ICpDebugConfiguration 
	 */
	ICpDebugConfiguration getDebugConfiguration();
	
	/**
	 * Returns brief device description that includes core, clock and memory 
	 * @return brief device description
	 */
	String getSummary();
	
	/**
	 * Returns brief clock description 
	 * @return brief clock description
	 */
	String getClockSummary();
	
	/**
	 * Returns brief description of device memory 
	 * @return brief memory description
	 */
	String getMemorySummary();
	
	/**
	 * Returns the memory offset with memory name
	 * @return map of the memory base offset
	 */
	Map<String,Long> getMemoryBankOffset();
	
	/**
	 * Returns the memory bank size with the bank name
	 * @return the map of the memory bank size
	 */
	Map<String,Long> getMemoryBankSize();
}
