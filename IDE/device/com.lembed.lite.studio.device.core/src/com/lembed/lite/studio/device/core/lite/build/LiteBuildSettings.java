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

package com.lembed.lite.studio.device.core.lite.build;

import com.lembed.lite.studio.device.core.build.BuildSettings;
import com.lembed.lite.studio.device.generic.IAttributes;

/**
 * This class extends generic BuildSettings to provide device attributes
 */
public class LiteBuildSettings extends BuildSettings {
	protected IAttributes deviceAttributes = null;

	public LiteBuildSettings() {
	}
 
	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public String getDeviceAttribute(String key) {
		if (deviceAttributes == null) {
			return null;
		}
		return deviceAttributes.getAttribute(key);
	}

	public void setDeviceAttributes(IAttributes deviceAttributes) {
		this.deviceAttributes = deviceAttributes;
	}
}
