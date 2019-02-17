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

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.DeviceVendor;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;

/**
 *  An artificial component that describes selected device. 
 */
public class LiteSelectedDeviceClass extends LiteComponentClass {

	ICpDeviceInfo fDeviceInfo = null;
	String vendorName = null;

	public LiteSelectedDeviceClass(ILiteComponentItem parent, ICpDeviceInfo deviceInfo) {
		super(parent, deviceInfo.getDeviceName());
		fDeviceInfo = deviceInfo;
	}

	@Override
	public boolean purge() {
		return false;
	}

	@Override
	public ICpItem getActiveCpItem() {
		return fDeviceInfo;
	}

	@Override
	public String getUrl() {
		return fDeviceInfo.getUrl();
	}

	@Override
	public String getDescription() {
		if(fDeviceInfo.getDevice() == null) {
			return CpStrings.DeviceNotFound; 
		}
		return fDeviceInfo.getSummary();
	}

	@Override
	public String getActiveVendor() {
		if(vendorName == null) {
			vendorName = DeviceVendor.getOfficialVendorName(fDeviceInfo.getVendor());
		}
		return vendorName;
	}

	@Override
	public String getActiveVersion() {
		return fDeviceInfo.getVersion();
	}

	
	@Override
	public boolean isUseLatestVersion() {
		return true;
	}


	@Override
	public String getKey() {
		return CmsisConstants.EMPTY_STRING;
	}


	@Override
	public boolean isSelected() {
		return true; // device is always selected
	}
	
}
