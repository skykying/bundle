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

package com.lembed.lite.studio.device.core.lite.components;

import java.util.LinkedHashSet;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.enums.EComponentAttribute;

/**
 *  RClass represent a bundle version  
 */
public class LiteComponentBundleVersion extends LiteComponentItem {

	protected LinkedHashSet<ICpItem> fBundles = new LinkedHashSet<ICpItem>();

	public LiteComponentBundleVersion(ILiteComponentItem parent, String name) {
		super(parent, name);
		fComponentAttribute = EComponentAttribute.CGROUP;
		fbExclusive = false;
	}

	@Override
	public ICpItem getCpItem() {
		if(!fBundles.isEmpty())
			return fBundles.iterator().next();
		return null;
	}

	@Override
	public void addComponent(ICpComponent cpComponent, int flags) {
		
		ICpItem bundle = cpComponent.getParent(CmsisConstants.BUNDLE_TAG);
		if(bundle != null && !fBundles.contains(bundle)) {
			fBundles.add(bundle);
		}
		
		String groupName = cpComponent.getAttribute(CmsisConstants.CGROUP);
		ILiteComponentItem groupItem = getChild(groupName); 
		if(groupItem == null) {
			groupItem = new LiteComponentGroup(this, groupName);
			addChild(groupItem);
		}
		groupItem.addComponent(cpComponent, flags);
	}

	
	@Override
	public void addCpItem(ICpItem cpItem) {
		String groupName = cpItem.getAttribute(CmsisConstants.CGROUP);
		if(groupName == null || groupName.isEmpty())
			return; 
		// check if group exists 
		ILiteComponentItem groupItem = getChild(groupName); 
		if(groupItem == null ) {
			return; // no group => no add
		}
		groupItem.addCpItem(cpItem);
	}
}
