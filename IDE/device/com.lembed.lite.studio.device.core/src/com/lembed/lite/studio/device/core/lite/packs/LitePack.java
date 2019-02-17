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

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.CpAttributes;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.generic.IAttributes;

/**
 *  
 */
public class LitePack extends LitePackItem implements ILitePack {

	protected ICpPack fPack = null;
	protected ICpPackInfo fPackInfo = null;
	protected IAttributes fAttributes = new CpAttributes();
	private boolean fbSelected = false;
	
	public LitePack(ILitePackItem parent, ICpItem packItem) {
		super(parent);
		addCpItem(packItem); 
	}

	@Override
	public void clear() {
		super.clear();
		fPack = null;
		fPackInfo = null;
		fbSelected = false;
	}
	
	
	@Override
	public boolean purge() {
		if(!isUsed() && !isInstalled()) {
			clear();
			return true;
		}
		return false;
	}
	
	
	@Override
	public void addCpItem(ICpItem item) {
		if(item instanceof ICpPack) {
			fPack = (ICpPack)item;
			fAttributes.setAttribute(CmsisConstants.NAME, fPack.getName());
			fAttributes.setAttribute(CmsisConstants.URL, fPack.getUrl()); 
			fAttributes.setAttribute(CmsisConstants.VENDOR, fPack.getVendor());
			fAttributes.setAttribute(CmsisConstants.VERSION, fPack.getVersion());
			
		} else if(item instanceof ICpPackInfo) {
			fPackInfo = (ICpPackInfo)item;
			if(fPack == null)
				fAttributes.setAttributes(fPackInfo.attributes());
		}
	}

	@Override
	public ICpItem getCpItem() {
		ICpPack pack = getPack();
		if(pack != null)
			return pack;
		return getPackInfo();
	}

	@Override
	public ICpPackInfo getPackInfo() {
		return fPackInfo;
	}
	
	@Override
	public ICpPack getPack() {
		return fPack;
	}

	@Override
	public String getId() {
		ICpItem item = getCpItem();
		if(item != null)
			return item.getId();
		return null;
	}
	
	@Override
	public boolean isSelected() {
		switch(getVersionMatchMode()){
		case EXCLUDED:
			return false;
		case LATEST:
			return isLatest();
		case FIXED:
		default:
			break;
		}
		return fbSelected;
	}

	
	
	@Override
	public boolean isExcluded() {
		return !isSelected();
	}

	@Override
	public boolean isExplicitlySelected() {
		return fbSelected;
	}
	

	@Override
	public boolean isUsed() {
		ILitePackCollection root = getRoot();
		if(root != null)
			return root.isPackUsed(getId());
		return false;
	}

	@Override
	public String getVersion() {
		ICpItem item = getCpItem();
		if(item != null)
			return item.getVersion();
		return null;
	}

	@Override
	public void setSelected(boolean selected) {
		fbSelected = selected;
	}

	@Override
	public boolean isInstalled() {
		return fPack != null;
	}
	
	@Override
	public boolean isLatest() {
		return fParent.getFirstChild() == this;
	}
	
	@Override
	public ILitePackItem getFirstChild() {
		return null;
	}
	
	@Override
	public boolean hasChildren() {
		return false;
	}
	@Override
	public Object[] getChildArray() {
		return EMPTY_OBJECT_ARRAY;
	}
	
	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public IAttributes getAttributes() {
		return fAttributes;
	}
}