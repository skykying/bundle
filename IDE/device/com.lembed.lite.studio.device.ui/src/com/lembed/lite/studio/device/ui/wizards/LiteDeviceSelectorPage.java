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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.IStatusMessageListener;
import com.lembed.lite.studio.device.ui.widgets.LiteDeviceSelectorWidget;

/**
 * Wizard page that wraps device selector widget
 */
public abstract class LiteDeviceSelectorPage extends WizardPage implements IStatusMessageListener {

	private LiteDeviceSelectorWidget fDeviceWidget = null;
	private ILiteDeviceItem fDevices = null;
	private ICpDeviceInfo fDeviceInfo = null;
	private boolean fbInitialized = false;

	public LiteDeviceSelectorPage() {
		this(CpStringsUI.LiteDeviceWizard_PageName, CpStringsUI.LiteDeviceWizard_SelectDevice,
				CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_CHIP_48));
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public LiteDeviceSelectorPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		setPageComplete(false);
		CpPlugInUI.setSelectedDeviceInfo(null);
	}

	@Override
	public void createControl(Composite parent) {	
		
		fDeviceWidget = new LiteDeviceSelectorWidget(parent);
		fDeviceWidget.addListener(this);
		fDeviceWidget.setDevices(fDevices);

		setControl(fDeviceWidget);
		updateStatus(CpStringsUI.LiteDeviceSelectorPage_SelectDevice);
	}
	
	

	/**
	 * Returns internal device tree
	 * 
	 * @return the devices
	 */
	public ILiteDeviceItem getDevices() {
		return fDevices;
	}

	/**
	 * Assigns device tree
	 * 
	 * @param devices
	 *            the devices to set
	 */
	public void setDevices(ILiteDeviceItem devices) {
		fDevices = devices;
		if (fDeviceWidget != null) {
			fDeviceWidget.setDevices(fDevices);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		fDeviceWidget.setDeviceInfo(fDeviceInfo);
		fbInitialized = true;
		super.setVisible(visible);
	}

	@Override
	public void dispose() {
		super.dispose();
		fDevices = null;
		fDeviceWidget = null;
		fDeviceInfo = null;
	}

	@Override
	public void handle(String message) {
		updateStatus(message);
	}

	protected void updateStatus(String message) {
		setErrorMessage(message);
		if (fbInitialized) {
			fDeviceInfo = fDeviceWidget.getDeviceInfo();			
		}
		
		if(fDeviceInfo != null){
			CpPlugInUI.setSelectedDeviceInfo(fDeviceInfo);
		}
		
		setPageComplete(fDeviceInfo != null);
	}

	/**
	 * Returns selected device if any
	 * 
	 * @return the selected device
	 */
	public ILiteDeviceItem getDevice() {
		if (fDeviceWidget != null) {
			return fDeviceWidget.getSelectedDeviceItem();
		}
		return null;
	}

	/**
	 * Returns selected device info
	 * 
	 * @return
	 */
	public ICpDeviceInfo getDeviceInfo() {
		if (fbInitialized) {
			fDeviceInfo = fDeviceWidget.getDeviceInfo();
		}
		return fDeviceInfo;
	}

	/**
	 * Makes initial device selection
	 * 
	 * @param deviceInfo
	 *            ICpDeviceInfo to make initial selection
	 */
	public void setDeviceInfo(ICpDeviceInfo deviceInfo) {
		fDeviceInfo = deviceInfo;
		if (fDeviceWidget != null) {
			fDeviceWidget.setDeviceInfo(deviceInfo);
		}
	}

}
