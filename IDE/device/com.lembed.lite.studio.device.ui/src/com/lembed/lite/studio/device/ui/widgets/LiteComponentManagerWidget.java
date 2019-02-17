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
package com.lembed.lite.studio.device.ui.widgets;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.lite.ILiteModelController;

/**
 * This class implements functionality of component selector page
 */
public class LiteComponentManagerWidget extends LiteWidget {
	private SashForm sashForm = null;

	LiteComponentSelectorWidget liteComponentTreeWidget = null;
	LiteValidateWidget liteValidateWidget = null;

	public LiteComponentManagerWidget() {
		super();
		liteComponentTreeWidget = new LiteComponentSelectorWidget();
		liteValidateWidget = new LiteValidateWidget();
	}

	public SashForm getSashForm() {
		return sashForm;
	}

	@Override
	public Composite getFocusWidget() {
		TreeViewer viewer = liteComponentTreeWidget.getViewer();
		return viewer.getTree();
	}

	@Override
	public void setModelController(ILiteModelController model) {
		super.setModelController(model);
		liteComponentTreeWidget.setModelController(model);
		liteValidateWidget.setModelController(model);
		update();
	}

	@Override
	public Composite createControl(Composite parent) {
		sashForm = new SashForm(parent, SWT.VERTICAL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		sashForm.setSashWidth(3);
		liteComponentTreeWidget.createControl(sashForm);
		liteValidateWidget.createControl(sashForm);
		sashForm.setWeights(new int[] { 3, 1 });
		getFocusWidget().setFocus();

		return sashForm;
	}

	@Override
	public void handle(LiteEvent event) {
	}

	@Override
	public void refresh() {
	}

	@Override
	public void update() {
		if (sashForm != null) {
			liteComponentTreeWidget.update();
			liteValidateWidget.update();
		}
	}
}
