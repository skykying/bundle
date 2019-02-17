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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnViewer;

import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.ui.tree.ColumnAdvisor;

/**
 * Extends ColumnAdvisor with IRteCobdelController support
 */
public abstract class LiteColumnAdvisor extends ColumnAdvisor implements ILiteColumnAdvisor {

	private ILiteModelController fRteModelController = null;

	public LiteColumnAdvisor(ColumnViewer columnViewer) {
		super(columnViewer);
	}

	public LiteColumnAdvisor(ColumnViewer columnViewer, ILiteModelController modelController) {
		this(columnViewer);
		fRteModelController = modelController;
	}

	@Override
	public void setModelController(ILiteModelController modelController) {
		fRteModelController = modelController;
	}

	@Override
	public ILiteModelController getModelController() {
		return fRteModelController;
	}

	@Override
	public void openUrl(String url) {
		if (fRteModelController != null) {
			String msg = fRteModelController.openUrl(url);
			if (msg != null) {
				String message = CpStringsUI.CannotOpenURL + url;
				message += "\n"; //$NON-NLS-1$
				message += msg;
				MessageDialog.openError(this.control != null ? this.control.getShell() : null,
						CpStringsUI.CannotOpenURL, message);
			}
		} else {
			super.openUrl(url);
		}
	}

}
