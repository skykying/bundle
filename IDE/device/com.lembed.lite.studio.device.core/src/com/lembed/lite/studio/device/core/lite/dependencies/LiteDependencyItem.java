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

import java.util.Collection;

import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.item.CmsisItem;

/**
 *
 */
public class LiteDependencyItem extends CmsisItem implements ILiteDependencyItem {

	protected EEvaluationResult fResult = EEvaluationResult.UNDEFINED;
	protected ILiteComponentItem fComponentItem = null; 
	/**
	 * Default constructor 
	 */
	public LiteDependencyItem() {
	}


	public LiteDependencyItem(EEvaluationResult result) {
		setEvaluationResult(result);
	}

	
	/**
	 * Constructor
	 * @param component IRteComponent candidate component 
	 */
	public LiteDependencyItem(ILiteComponentItem componentItem) {
		fComponentItem = componentItem;
	}

	/**
	 * Constructor
	 * @param component IRteComponent candidate component 
	 */
	public LiteDependencyItem(ILiteComponentItem componentItem, EEvaluationResult result) {
		fComponentItem = componentItem;
		setEvaluationResult(result);
	}
	
	@Override
	public boolean isDeny() {
		// Default returns false
		return false;
	}


	@Override
	public EEvaluationResult getEvaluationResult() {
		return fResult ;
	}

	@Override
	public void setEvaluationResult(EEvaluationResult result) {
		fResult = result;
	}

	@Override
	public Collection<? extends ILiteDependencyItem> getChildren() {
		return null;
	}

	@Override
	public ILiteComponentItem getComponentItem() {
		return fComponentItem;
	}
	
	@Override
	public ICpItem getCpItem(){
		ILiteComponentItem componentItem = getComponentItem();
		if(componentItem != null) {
			return componentItem.getActiveCpItem();
		}
		return null;
	}

	@Override
	public String getName() {
		ICpItem cpItem = getCpItem();
		if(cpItem != null)
			return cpItem.getName();
		if(fComponentItem != null)
			return fComponentItem.getName();
		return super.getName();
	}
	
	@Override
	public String getDescription() {
		ICpItem cpItem = getCpItem();
		if(cpItem != null)
			return cpItem.getDescription();
		return super.getDescription();
	}

	@Override
	public String getUrl() {
		ICpItem cpItem = getCpItem();
		if(cpItem != null)
			return cpItem.getUrl();
		return super.getUrl();
	}


	@Override
	public boolean isMaster() {
		return hasChildren();
	}
	
}
