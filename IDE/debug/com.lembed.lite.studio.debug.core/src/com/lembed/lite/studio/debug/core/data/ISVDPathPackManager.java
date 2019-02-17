/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.core.data;

import org.eclipse.core.runtime.IPath;

/**
 * The interface of the manager used to handle SVD paths.
 */
public interface ISVDPathPackManager {	

	/**
	 * Get the absolute path of a SVD file associated with the given device.
	 * <p>
	 * For unsupported devices, this should return null.
	 * 
	 * @param deviceVendorName
	 *            a string with the CMSIS device vendor name.
	 * @param deviceName
	 *            a string with the CMSIS device name.
	 * @return the absolute path to the SVD file, or null.
	 */

	IPath getSVDAbsolutePathByName(String deviceVendorName, String deviceName);
	
	/**
	 * Get the absolute path of a SVD file associated with the given device.
	 * <p>
	 * For unsupported devices, this should return null.
	 * 
	 * @param deviceVendorId
	 *            a string with the CMSIS device vendor id.
	 * @param deviceName
	 *            a string with the CMSIS device name.
	 * @return the absolute path to the SVD file, or null.
	 */
	IPath getSVDAbsolutePathById(String deviceVendorId, String deviceName);

}
