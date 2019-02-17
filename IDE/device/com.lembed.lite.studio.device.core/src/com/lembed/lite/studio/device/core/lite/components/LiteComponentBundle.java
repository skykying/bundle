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

import java.util.Collection;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;

/**
 * Class represent Cbundle component hierarchy level, contains collection of component vendors 
 */
public class LiteComponentBundle extends LiteComponentVariant implements ILiteComponentBundle{

	public LiteComponentBundle(ILiteComponentItem parent, String name) {
		super(parent, name);
	}

	@Override
	public void addComponent(ICpComponent cpComponent, int flags) {
		ICpComponentInfo ci = null;
		if(cpComponent instanceof ICpComponentInfo) {
			// consider error situation when components belong to different bundles  
			ci = (ICpComponentInfo)cpComponent;
		}
		
		// create bundle vendor and version items
		String vendor = CmsisConstants.EMPTY_STRING;
		String version = CmsisConstants.EMPTY_STRING;
		if(!getName().isEmpty()) { // bundle or component in a bundle
			vendor = cpComponent.getVendor();
			if(ci == null || ci.isVersionFixed()) 
				version = cpComponent.getVersion();
			else 
				version = null;
		} 
		
		ILiteComponentItem vendorItem = getChild(vendor);
		if( vendorItem == null) {
			vendorItem = new LiteComponentVendor(this, vendor);
			addChild(vendorItem);
		}

		ILiteComponentItem versionItem = vendorItem.getChild(version);
		if( versionItem == null) {
			if(ci == null || ci.isVersionFixed() || vendorItem.getFirstChild() == null) {
				versionItem = new LiteComponentBundleVersion(vendorItem, version);
				vendorItem.addChild(versionItem);
			} else {
				versionItem = vendorItem.getFirstChild();
				version = vendorItem.getFirstChildKey();
			}

			if(ci != null && ci.isVersionFixed()) {
				ci.setEvaluationResult(EEvaluationResult.MISSING_VERSION);
			}
		}
		versionItem.addComponent(cpComponent, flags);
		if(ci != null){
			setActiveChild(vendor);
			vendorItem.setActiveChild(version);
		}
	}
	
	@Override
	public ILiteComponentBundle getParentBundle() {
		return this;
	}
	
	@Override
	public Collection<String> getVendorStrings() {
		if(getName().isEmpty())
			return null;
		return super.getVendorStrings(); 
	}

	@Override
	public Collection<String> getVersionStrings() {
		if(getName().isEmpty())
			return null;
		return super.getVersionStrings();
	}
}
