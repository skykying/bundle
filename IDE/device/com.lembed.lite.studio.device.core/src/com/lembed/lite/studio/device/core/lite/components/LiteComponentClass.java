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
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpTaxonomy;
import com.lembed.lite.studio.device.core.enums.EComponentAttribute;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.lite.LiteConstants;
import com.lembed.lite.studio.device.core.lite.dependencies.ILiteDependency;
import com.lembed.lite.studio.device.core.lite.dependencies.LiteDependency;

/**
 * Class represents Cclass component hierarchy level, contains collection of bundles  
 */
public class LiteComponentClass extends LiteComponentItem implements ILiteComponentClass {

	/**
	 * @param parent
	 */
	public LiteComponentClass(ILiteComponentItem parent, String name) {
		super(parent, name);
		fComponentAttribute = EComponentAttribute.CBUNDLE;
	}
	

	@Override
	public ILiteComponentClass getParentClass() {
		return this;
	}


	@Override
	public void addComponent(ICpComponent cpComponent, int flags) {
		String bundleName = cpComponent.getBundleName();
		ICpComponentInfo ci = null;
		if(cpComponent instanceof ICpComponentInfo) {
			ci = (ICpComponentInfo)cpComponent;
		}
		// ensure childItem
		ILiteComponentItem bundleItem = getChild(bundleName); 
		if(bundleItem == null ) {
			if(ci != null && hasChildren()) {
				// there are some bundles, but not what is needed 
				ci.setEvaluationResult(EEvaluationResult.MISSING_BUNDLE);
			}
			bundleItem = new LiteComponentBundle(this, bundleName);
			addChild(bundleItem);
		}
		bundleItem.addComponent(cpComponent, flags);
		
		if(ci != null) {
			setActiveChild(bundleName);
		} else {
			ICpItem bundle = cpComponent.getParent(CmsisConstants.BUNDLE_TAG);
			if(bundle != null && bundle.isDefaultVariant()) {
				setActiveChild(bundleName);
			}
		}
	}

	
	@Override
	public void addCpItem(ICpItem cpItem) {
		if (cpItem instanceof ICpTaxonomy ){
			String cgroup = cpItem.getAttribute(CmsisConstants.CGROUP);
			if( cgroup == null || cgroup.isEmpty()) {
				if(getTaxonomy() == null) {
					fTaxonomy = cpItem;
				} 
				return;
			}
		}
		super.addCpItem(cpItem);
	}

	@Override
	public Collection<String> getVariantStrings() {
		return getKeys();
	}

	@Override
	public String getActiveVariant() {
		return getActiveChildName();
	}

	@Override
	public void setActiveVariant(String variant) {
		// store selected components to select them in a new bundle 
		Collection<ILiteComponent> components = getSelectedComponents(null); // the collection will be allocated 

		boolean changed = setActiveChild(variant);
		if(!changed || components == null || components.isEmpty())
			return;

		ILiteComponentItem activeBundle = getActiveChild();
		// try to select similar components in the new bundle 
		for(ILiteComponent liteComponent : components) {
			ICpComponent c = liteComponent.getActiveCpComponent();
			if(c == null)
				continue;
			ILiteDependency dep = new LiteDependency(c, LiteConstants.COMPONENT_IGNORE_ALL);
			EEvaluationResult res = activeBundle.findComponents(dep);
			if(res == EEvaluationResult.SELECTABLE) {
				ILiteComponent toSelect = dep.getBestMatch();
				if(toSelect != null) {
					int nsel = liteComponent.getSelectedCount();
					toSelect.setSelected(nsel);
				}
			}
		}
	}
}
