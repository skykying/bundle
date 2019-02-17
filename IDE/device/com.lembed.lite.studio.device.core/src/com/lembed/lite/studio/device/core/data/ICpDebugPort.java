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

import com.lembed.lite.studio.device.core.enums.EDebugProtocolType;


/**
 *  Convenience interface to access information under "debugport" device property   
 */
public interface ICpDebugPort extends ICpDeviceProperty {

	/**
	 * Check if debug protocol of given type is iplemented 
	 * @param protocolType protocol type as EDebugProtocolType value
	 * @return true if protocol is implemented
	 */
	boolean isProtocolImplemented(EDebugProtocolType protocolType);
	
	/**
	 * Returns debug protocol description if defined 
	 * @param protocolType protocol type as EDebugProtocolType value
	 * @return ICpDebugProtocol if defined for the port, null otherwise
	 */
	ICpDebugProtocol getProtocol(EDebugProtocolType protocolType);
	
	/**
	 * Returns debug protocol description if defined 
	 * @param protocolType protocol type as string value
	 * @return ICpDebugProtocol if defined for the port, null otherwise
	 */
	ICpDebugProtocol getProtocol(String protocolType);
	
	
}
