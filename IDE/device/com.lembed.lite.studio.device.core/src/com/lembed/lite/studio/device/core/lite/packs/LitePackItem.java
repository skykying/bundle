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

package com.lembed.lite.studio.device.core.lite.packs;

import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;

/**
 *  Base class for LITE pack tree
 */
public abstract class LitePackItem implements ILitePackItem {

	protected ILitePackItem fParent = null;

	public LitePackItem(ILitePackItem parent) {
		fParent = parent;
	}

	@Override
	public void clear() {
		fParent = null;
	}


	@Override
	public ILitePackCollection getRoot() {
		if(fParent != null)
			return fParent.getRoot();
		return null;
	}
	
	@Override
	public ILitePackFamily getFamily() {
		if(fParent != null)
			return fParent.getFamily();
		return null;
	}


	@Override
	public ILitePackItem getParent() {
		return fParent;
	}

	@Override
	public EVersionMatchMode getVersionMatchMode() {
		if(fParent != null)
			return fParent.getVersionMatchMode();
		return null;
	}

	@Override
	public String getUrl() {
		ICpItem item = getCpItem();
		if(item != null)
			return item.getUrl();
		return null;
	}
	

	@Override
	public String getDescription() {
		ICpPack pack = getPack();
		if(pack != null)
			return pack.getDescription();
		return CpStrings.LitePackIsNotInstalled;
	}

	@Override
	public void destroy() {
		clear();
		fParent = null;
	}

	@Override
	public ICpPack getPack() {
		return null;
	}

	@Override
	public ICpPackInfo getPackInfo() {
		return null;
	}

	@Override
	public boolean isUseAllLatestPacks() {
		if(fParent != null){
			return fParent.isUseAllLatestPacks();
		}
		return false;
	}
}
