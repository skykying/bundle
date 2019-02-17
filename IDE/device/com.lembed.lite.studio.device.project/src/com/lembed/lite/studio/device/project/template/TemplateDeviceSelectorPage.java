/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* Copyright (c) 2017 LEMBED
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
* LEMBED - adapter for LiteSTUDIO
*******************************************************************************/

package com.lembed.lite.studio.device.project.template;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.ui.templateengine.IWizardDataPage;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.build.IMemorySettings;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.project.IHelpContextIds;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.project.utils.ConfigurationUtils;
import com.lembed.lite.studio.device.toolchain.litestudio.LiteLinkerScriptGenerator;
import com.lembed.lite.studio.device.ui.wizards.LiteDeviceSelectorPage;

/**
 * Device selector page for new project wizard
 */
public class TemplateDeviceSelectorPage extends LiteDeviceSelectorPage implements IWizardDataPage {

	protected IWizardPage next;

	public TemplateDeviceSelectorPage() {
		super();
	}

	@Override
	public Map<String, String> getPageData() {
		Map<String, String> data = null;

		ICpDeviceInfo deviceInfo = getDeviceInfo();
		if (deviceInfo != null) {
			data = deviceInfo.attributes().getAttributesAsMap();
			LiteProjectTemplateProvider.setSelectedDeviceInfo(deviceInfo);
			
			IMemorySettings memsetting = ConfigurationUtils.createMemorySettings(deviceInfo);
			String script = LiteLinkerScriptGenerator.getMemoryBankInfo(memsetting);
			data.put(CmsisConstants.FEATURE_PROJECT_MEMBANK_INFO, script); 
			
		}
		
		if (data == null) {
			data = new HashMap<String, String>();
		}
		
		return data;
	}

	@Override
	public void createControl(Composite parent) {

		ICpPackManager packManager = CpPlugIn.getPackManager();
		if (packManager == null) {
			updateStatus(Messages.LiteTemplateDeviceSelectorPage_NoPackManagerIsAvailble);
			return;
		}

		ILiteDeviceItem devices = packManager.getInstalledDevices();
		setDevices(devices);

		// always clear the device info in a new wizard
		LiteProjectTemplateProvider.setSelectedDeviceInfo(null);

		// this will update status
		setDeviceInfo(LiteProjectTemplateProvider.getSelectedDeviceInfo());

		super.createControl(parent);
		if (devices == null || !devices.hasChildren()) {
			updateStatus(Messages.LiteTemplateDeviceSelectorPage_NoDevicesAreAvailable);
		} else if (getWizard() instanceof IDeviceInfoProvider) { 
			IDeviceInfoProvider dp = (IDeviceInfoProvider) getWizard();
			// the wizard already provides the device info
			setDeviceInfo(dp.getDeviceInfo());
		}

		// add context-sensitive help
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IHelpContextIds.CMSIS_DEVICE_SELECT_WIZARD);

	}

	@Override
	public void setNextPage(IWizardPage next) {
		this.next = next;
	}

	@Override
	public IWizardPage getNextPage() {
		if (next != null) {
			return next;
		}
		return super.getNextPage();
	}

	@SuppressWarnings("unused")
	private static void log(String msg){
		System.out.println("Temp >> " + msg.toString() +" = " + msg +"\n");
	}

}
