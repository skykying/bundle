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

package com.lembed.lite.studio.device.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;

/**
 * Wizard to select a device for configuration
 */
public class LiteDeviceSelectorWizard extends Wizard  {
	
	class DeviceSelectorPage extends LiteDeviceSelectorPage{

		}

	private LiteDeviceSelectorPage fDevicePage;
	private ILiteDeviceItem fDevices;
	private ICpDeviceInfo fDeviceInfo;

	public LiteDeviceSelectorWizard(String name, ILiteDeviceItem devices, ICpDeviceInfo deviceInfo) {
		fDevices = devices;
		fDeviceInfo = deviceInfo;
		setWindowTitle(name);
	}

	public ICpDeviceInfo getDeviceInfo() {
		return fDeviceInfo;
	}

	public void setDeviceInfo(ICpDeviceInfo deviceInfo) {
		fDeviceInfo = deviceInfo;
	}

	@Override
	public boolean performFinish() {
		fDeviceInfo = fDevicePage.getDeviceInfo();
		return fDeviceInfo != null;
	}

	@Override
	public void addPages() {
		fDevicePage = new DeviceSelectorPage();
		fDevicePage.setDevices(fDevices);
		fDevicePage.setDeviceInfo(fDeviceInfo);
		addPage(fDevicePage);
	}

}
