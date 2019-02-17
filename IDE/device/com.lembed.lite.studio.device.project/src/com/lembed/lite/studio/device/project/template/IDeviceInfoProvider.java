/*******************************************************************************
* Copyright (c) 2016 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.project.template;

import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;

/**
 * Interface that provide the device info.
 */
public interface IDeviceInfoProvider {

	/**
	 * Get the device info
	 * 
	 * @return the device info
	 */
	ICpDeviceInfo getDeviceInfo();
}
