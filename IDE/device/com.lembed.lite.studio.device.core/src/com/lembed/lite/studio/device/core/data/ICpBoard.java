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

package com.lembed.lite.studio.device.core.data;

import java.util.Collection;

import com.lembed.lite.studio.device.generic.IAttributes;

/**
 *  Interface describing board
 */
public interface ICpBoard extends ICpItem {

	
	/**
	 * Checks if this board contains mounted or compatible device matching suppled device attributes
	 * @param deviceAttributes attributes of device to match
	 * @return
	 */
	boolean hasCompatibleDevice(IAttributes deviceAttributes); 
	
	/**
	 * @return mounted Devices on this board. return empty list if no mounted
	 *         devices.
	 */
	Collection<ICpItem> getMountedDevices();

	/**
	 * @return compatible Devices on this board. return empty list if no
	 *         compatible devices.
	 */
	Collection<ICpItem> getCompatibleDevices();
}
