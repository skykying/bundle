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
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependency;

/**
 * Class represent Cversion hierarchy level (the end-leaf), contains references to ICpComponents.
  */
public class LiteComponentVersion extends LiteComponentItem {

	protected LinkedHashSet<ICpComponent> fComponents = new LinkedHashSet<ICpComponent>();
	protected ICpComponentInfo fComponentInfo = null;
	
	
	public LiteComponentVersion(ILiteComponentItem parent, String name) {
		super(parent, name);
	}
	
	
	@Override
	public void destroy() {
		super.destroy();
		fComponents.clear();
		fComponentInfo = null;
	}


	@Override
	public boolean purge() {
		if(!isSelected()) {
			fComponentInfo = null;
			if(fComponents.isEmpty()) {
				destroy();
				return true;
			}
		}
		return false;
	}

	@Override
	public EEvaluationResult findComponents(ILiteDependency dependency) {
		if(getEntityCount() > 1)
			return EEvaluationResult.INSTALLED;
		return EEvaluationResult.SELECTABLE;
	}
	
	@Override
	public void addComponent(ICpComponent cpComponent, int flags) {
		if(cpComponent instanceof ICpComponentInfo) {
			if(cpComponent != fComponentInfo ) {
				fComponentInfo = (ICpComponentInfo)cpComponent;
				fComponentInfo.setComponent(getFirstCpComponent());
			}
		} else if(! fComponents.contains(cpComponent)){
			fComponents.add(cpComponent);
		}
	}


	@Override
	public ICpItem getCpItem() {
		return getActiveCpComponent();
	}

	@Override
	public ICpComponent getActiveCpComponent() {
		ICpComponent cpComponent = getFirstCpComponent();
		if(cpComponent != null)
			return cpComponent;
		return fComponentInfo;
	}

	@Override
	public ICpComponentInfo getActiveCpComponentInfo() {
		return fComponentInfo;
	}

	protected ICpComponent getFirstCpComponent(){
		if(!fComponents.isEmpty())
			return fComponents.iterator().next();
		return null;
	}
	
	
	@Override
	public ICpComponent getApi() {
		ILiteComponentGroup group = getParentGroup();
		if(group != null) {
			ICpItem cpItem = getCpItem();
			if(cpItem != null && cpItem.hasAttribute(CmsisConstants.CAPIVERSION))
				return group.getApi(cpItem.getAttribute(CmsisConstants.CAPIVERSION)); // certain API version version
			return group.getApi(); // active API version
		}
		return null;
	}

	
	protected int getEntityCount() {
		return fComponents.size();
	}
}
