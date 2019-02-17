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

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.DeviceVendor;
import com.lembed.lite.studio.device.generic.Attributes;
import com.lembed.lite.studio.device.generic.IAttributes;

/**
 * Class that overrides some methods from generic Attributes 
 */
public class CpAttributes extends Attributes {

	public CpAttributes() {
		super();
	}

	/**
	 * Copy constructor
	 * @param copyFrom
	 */
	public CpAttributes(IAttributes copyFrom) {
		super(copyFrom);
	}
	
	
	@Override
	public boolean matchAttribute(String key, String value, String pattern) {
		if(key.equals(CmsisConstants.DVENDOR)) {
			return DeviceVendor.match(value, pattern);
		}
		return super.matchAttribute(key, value, pattern);
	}

}
