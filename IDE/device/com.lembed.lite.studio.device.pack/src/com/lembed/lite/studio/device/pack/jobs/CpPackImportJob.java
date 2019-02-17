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

package com.lembed.lite.studio.device.pack.jobs;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;

import com.lembed.lite.studio.device.core.ICpPackInstaller;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;

/**
 * Import a .pack or .zip file to the Pack Manager
 */
public class CpPackImportJob extends CpPackUnpackJob {

	protected String fImportSourceFile;

	/**
	 * @param name Job name
	 * @param installer Pack Installer
	 * @param packId Pack ID
	 * @param importSourceFile File path of the .pack/.zip file to be imported
	 */
	public CpPackImportJob(String name, ICpPackInstaller installer, String packId,
			String importSourceFile) {
		super(name, installer, packId, true);
		fImportSourceFile = importSourceFile;
	}

	@Override
	protected boolean copyToDownloadFolder(IProgressMonitor monitor) {
		try {
			DeviceUIUtils.copy(new File(fImportSourceFile), fSourceFilePath.toFile());
			return true;
		} catch (IOException e) {
			fResult.setErrorString(e.getMessage());
			return false;
		}
	}

}
