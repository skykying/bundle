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

package com.lembed.lite.studio.device.core.lite.dependencies;

import java.io.File;

import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;

/**
 *  The class represent a result of missing gpdsc file required by a configuration  
 */
public class LiteMissingGpdscResult extends LiteDependencyResult {
	protected String fGpdscFile  = null; 
	protected boolean fExists = false; 
			
	public LiteMissingGpdscResult(ILiteComponentItem componentItem, String filename) {
		super(componentItem);
		fGpdscFile = filename;
		File f = new File (filename);
		fExists = f.exists();
	}

	@Override
	public String getDescription() {
		String descr = CpStrings.Required_Gpdsc_File + ' ' + fGpdscFile + ' ';
		if(!fExists)
			descr += CpStrings.IsMissing;
		else 
			descr += CpStrings.Failed_To_Load;
		return descr;
	}

	@Override
	public boolean isMaster() {
		return true;
	}
	
	
}
