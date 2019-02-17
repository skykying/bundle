/*******************************************************************************
* Copyright (c) 2016 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.refclient;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;

import com.lembed.lite.studio.device.core.data.ICpExample;
import com.lembed.lite.studio.device.pack.CpPackInstaller;

/**
 *  Sample custom pack installer
 */
public class RefClientPackInstaller extends CpPackInstaller {


	@Override
	protected boolean confirmCopyExample(ICpExample example, File destFile, IProject project) {
		
		if(!RefClientEnvironmentProvider.isLiteStduioToolchainInstalled()) {
			String message = "Required gcc Cross Toolchain is not installed.\nCopy the example anyway?";
			boolean res = MessageDialog.openQuestion(null, "Required Toolchain not Installed", message);
			if(!res)
				return false;
		}
		
		return super.confirmCopyExample(example, destFile, project);
	}
	
}
