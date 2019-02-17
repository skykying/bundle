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

package com.lembed.lite.studio.device.core.data;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.lembed.lite.studio.device.utils.Utils;

/**
 *   Default implementation of ICpRootItem interface
 */
public class CpRootItem extends CpItem implements ICpRootItem {

	protected String fileName = null;
	protected String directory = null;

	public CpRootItem() {
		super(NULL_CPITEM);
	}
	
	public CpRootItem(ICpItem parent) {
		super(parent);
	}

	public CpRootItem(ICpItem parent, String tag) {
		super(parent, tag);
	}

	public CpRootItem(String tag, String fileName) {
		super(NULL_CPITEM, tag);
		setFileName(fileName);
	}
	
	@Override
	public ICpRootItem getRootItem() {
		return this;
	}	
	
	@Override
	public void setFileName(String fileName) {
		if (fileName != null && !fileName.isEmpty()) {
			IPath p = new Path(fileName);
			this.fileName = p.toString();
		} else {
			this.fileName = fileName;
		}
	}
	
	
	@Override
	public String getDir(boolean keepSlash) {
		if(directory == null || !keepSlash) {
			String dir = Utils.extractPath(getFileName(), keepSlash);
			if(!keepSlash)
				return dir;
			directory = dir;
		}
		return directory;
	}

	
	@Override
	public String getRootDir(boolean keepSlash) {
		return getDir( keepSlash);
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	
	@Override
	public String getRootFileName() {
		return getFileName();
	}

	
}
