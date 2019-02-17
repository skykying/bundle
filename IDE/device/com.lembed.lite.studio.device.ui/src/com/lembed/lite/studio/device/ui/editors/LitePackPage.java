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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.IHelpContextIds;
import com.lembed.lite.studio.device.ui.widgets.LitePackSelectorWidget;

/**
 * Editor page that wraps LiteManagerWidget
 *
 */
public class LitePackPage extends LiteEditorPage {

	protected LitePackSelectorWidget litePackSelectorTree = null;
	IAction useLatestAction = null;

	public LitePackPage() {
		litePackSelectorTree = new LitePackSelectorWidget();
	}

	@Override
	public void setModelController(ILiteModelController model) {
		super.setModelController(model);
		litePackSelectorTree.setModelController(model);
		update();
	}

	@Override
	public Composite getFocusWidget() {
		return litePackSelectorTree.getFocusWidget();
	}

	@Override
	public void createPageContent(Composite parent) {
		litePackSelectorTree.createControl(parent);
		headerWidget.setFocusWidget(getFocusWidget());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFocusWidget(), IHelpContextIds.PACKS_PAGE);
	}

	@Override
	protected void setupHeader() {
		headerWidget.setLabel(CpStringsUI.LiteConfigurationEditor_PacksTab,
				CpPlugInUI.getImage(CpPlugInUI.ICON_PACKAGES_FILTER));

		useLatestAction = new Action(CpStringsUI.UseAllLatestPacks, IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				setUseAllLatest(isChecked());
			}
		};
		useLatestAction.setToolTipText(CpStringsUI.UseAllLatestPacksTooltip);
		headerWidget.addAction(useLatestAction, SWT.LEFT, true);

		super.setupHeader();
	}

	void setUseAllLatest(boolean bUse) {
		if (fModelController != null) {
			fModelController.setUseAllLatestPacks(bUse);
		}
	}

	void updateUseAllLatest() {
		boolean bUse = true;
		if (fModelController != null) {
			bUse = fModelController.isUseAllLatestPacks();
		}
		useLatestAction.setChecked(bUse);
		if (bUse) {
			useLatestAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_CHECKED));
		} else {
			useLatestAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_UNCHECKED));
		}
	}

	@Override
	public void handle(LiteEvent event) {
		switch (event.getTopic()) {
		case LiteEvent.FILTER_MODIFIED:
			update();
			return;
		default:
			super.handle(event);
		}
	}

	@Override
	public void update() {
		if (getModelController() != null && litePackSelectorTree != null) {
			bModified = getModelController().isPackFilterModified();
			headerWidget.setModified(bModified);
		}
		refresh();
		super.update();
	}

	@Override
	public void refresh() {
		updateUseAllLatest();
		litePackSelectorTree.refresh();
	}

}
