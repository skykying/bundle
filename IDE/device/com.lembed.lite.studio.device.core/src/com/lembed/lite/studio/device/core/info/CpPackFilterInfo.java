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

package com.lembed.lite.studio.device.core.info;

import java.util.Collection;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.CpItem;
import com.lembed.lite.studio.device.core.data.CpPackFilter;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPackFilter;
import com.lembed.lite.studio.device.core.enums.EVersionMatchMode;

/**
 * 
 */
public class CpPackFilterInfo extends CpItem implements ICpPackFilterInfo {

	public CpPackFilterInfo(ICpItem parent) {
		this(parent, CmsisConstants.PACKAGES_TAG);
	}

	public CpPackFilterInfo(ICpItem parent, String tag) {
		super(parent, tag);
	}


	@Override
	public ICpPackFilter createPackFilter(){
		ICpPackFilter packFilter = new CpPackFilter();
		
		boolean bUseLatest = attributes().getAttributeAsBoolean(CmsisConstants.USE_ALL_LATEST_PACKS, true);
		packFilter.setUseAllLatestPacks(bUseLatest);
		if(bUseLatest) {
			return packFilter;
		}
		Collection<? extends ICpItem> packInfos = getChildren();
		if(packInfos == null) 
			return packFilter;
		for(ICpItem item : packInfos) {
			if(!(item instanceof ICpPackInfo))
				continue;
			ICpPackInfo packInfo = (ICpPackInfo)item;
			String packId = packInfo.getId();
			EVersionMatchMode mode = packInfo.getVersionMatchMode();
			switch(mode){
			case EXCLUDED:
				packFilter.setExcluded(packId, true);
				break;
			case FIXED:
				packFilter.setFixed(packId, true);
				break;
			case LATEST:
				packFilter.setUseLatest(packId);
				break;
			default:
				break;
			
			}
		}
		return packFilter;
	}

	@Override
	public boolean isUseAllLatestPacks() {
		return  attributes().getAttributeAsBoolean(CmsisConstants.USE_ALL_LATEST_PACKS, true);
	}

	@Override
	public void setUseAllLatestPacks(boolean bUseLatest) {
		attributes().setAttribute(CmsisConstants.USE_ALL_LATEST_PACKS, bUseLatest);
	}
}
