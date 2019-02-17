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

import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.IHelpContextIds;
import com.lembed.lite.studio.device.ui.widgets.LiteComponentManagerWidget;

/**
 * Editor page that wraps LiteManagerWidget
 *
 */
public class LiteComponentPage extends LiteEditorPage {

	protected LiteComponentManagerWidget liteManagerWidget;
	IAction resolveAction = null;

	public LiteComponentPage() {
		liteManagerWidget = new LiteComponentManagerWidget();
	}

	@Override
	public void setModelController(ILiteModelController model) {
		super.setModelController(model);
		liteManagerWidget.setModelController(model);
		update();
	}

	@Override
	public Composite getFocusWidget() {
		return liteManagerWidget.getFocusWidget();
	}

	@Override
	public void createPageContent(Composite parent) {
		liteManagerWidget.createControl(parent);
		headerWidget.setFocusWidget(getFocusWidget());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getFocusWidget(), IHelpContextIds.COMPONENT_PAGE);
	}

	@Override
	protected void setupHeader() {
		headerWidget.setLabel(CpStringsUI.LiteManagerWidget_Components, CpPlugInUI.getImage(CpPlugInUI.ICON_LITE));

		resolveAction = new Action(CpStringsUI.LiteComponentTreeWidget_Resolve, IAction.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				ILiteModelController model = getModelController();
				if (model != null) {
					model.resolveComponentDependencies();
				}
			}
		};
		resolveAction.setToolTipText(CpStringsUI.LiteComponentTreeWidget_ResolveComponentDependencies);
		resolveAction.setImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_RESOLVE_CHECK_WARN));
		resolveAction.setDisabledImageDescriptor(CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_RESOLVE_CHECK_GREY));
		headerWidget.addAction(resolveAction, SWT.LEFT, true);

		super.setupHeader();
	}

	@Override
	public void handle(LiteEvent event) {
		switch (event.getTopic()) {
		case LiteEvent.COMPONENT_SELECTION_MODIFIED:
			update();
			return;
		default:
			super.handle(event);
		}
	}

	@Override
	public void update() {
		if (headerWidget != null && getModelController() != null) {
			bModified = getModelController().isComponentSelectionModified();
			headerWidget.setModified(bModified);
			EEvaluationResult res = getModelController().getEvaluationResult();
			resolveAction.setEnabled(res == EEvaluationResult.SELECTABLE);
		}
		super.update();
	}

	@Override
	public void refresh() {
	}
}
