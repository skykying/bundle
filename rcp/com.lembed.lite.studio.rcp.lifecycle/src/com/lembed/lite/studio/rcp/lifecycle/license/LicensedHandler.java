/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.rcp.lifecycle.license;

import com.lembed.lite.studio.rcp.licensing.base.LicensingUtils;
import com.lembed.lite.studio.rcp.lifecycle.messages.Messages;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class LicensedHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (LicensingUtils.hasValidLicenseKey(new LicensedProduct())) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
					"Licensed", "Your LiteSTUDIO is under licenced, \n enjoy your life.");
		} else {
			MessageDialog.openError(HandlerUtil.getActiveShell(event),
					Messages.NO_LICENSE_VALIDATE_TITLE,
					Messages.NO_LICENSE_VALIDATE_DESCRIPTION);
		}
		return null;
	}

}
