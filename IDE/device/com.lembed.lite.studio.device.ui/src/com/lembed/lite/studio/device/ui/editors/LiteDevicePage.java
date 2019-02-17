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

package com.lembed.lite.studio.device.ui.editors;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.IHelpContextIds;
import com.lembed.lite.studio.device.ui.widgets.LiteDeviceInfoWidget;
import com.lembed.lite.studio.device.ui.wizards.LiteDeviceSelectorWizard;
import com.lembed.lite.studio.device.ui.wizards.LiteWizardDialog;

/**
 * Editor page that wraps LiteManagerWidget
 *
 */
public class LiteDevicePage extends LiteEditorPage {

	private LiteDeviceInfoWidget deviceWidget = null;

	public LiteDevicePage() {
	}

	@Override
	public void setModelController(ILiteModelController model) {
		super.setModelController(model);
		deviceWidget.setModelController(model);
		update();
	}

	@Override
	public Composite getFocusWidget() {
		return deviceWidget;
	}

	@Override
	public void createPageContent(Composite parent) {
		deviceWidget = new LiteDeviceInfoWidget(parent);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		deviceWidget.setLayoutData(gd);

		deviceWidget.setSelectionAdapter(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				changeDevice();
			}
		});

		headerWidget.setFocusWidget(getFocusWidget());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFocusWidget(), IHelpContextIds.DEVICE_PAGE);
	}

	@Override
	protected void setupHeader() {
		headerWidget.setLabel(CpStringsUI.LiteDevicePage_Device, CpPlugInUI.getImage(CpPlugInUI.ICON_DEVICE));
		super.setupHeader();
	}

	protected void changeDevice() {
		ILiteModelController model = getModelController();
		if (model != null) {
			LiteDeviceSelectorWizard wizard = new LiteDeviceSelectorWizard(CpStringsUI.LiteDeviceSelectorPage_SelectDevice,
					model.getDevices(), model.getDeviceInfo());
			LiteWizardDialog dlg = new LiteWizardDialog(deviceWidget.getShell(), wizard);
			dlg.setPageSize(600, 400); // limit initial size

			if (dlg.open() == Window.OK) {
				ICpDeviceInfo deviceInfo = wizard.getDeviceInfo();
				// deviceWidget.setDeviceInfo(deviceInfo);
				model.setDeviceInfo(deviceInfo);
			}
		}
	}

	@Override
	public void update() {
		if (headerWidget != null && getModelController() != null) {
			bModified = getModelController().isDeviceModified();
			headerWidget.setModified(bModified);
		}
		refresh();
		super.update();
	}

	@Override
	public void refresh() {
		ILiteModelController modelController = getModelController();
		if (deviceWidget != null && modelController != null) {
			deviceWidget.setDeviceInfo(modelController.getDeviceInfo());
		}
	}
}
