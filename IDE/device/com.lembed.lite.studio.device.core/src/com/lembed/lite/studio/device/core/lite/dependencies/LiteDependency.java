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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponent;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;

/**
 * Default implementation of ILiteDependency interface
 */
public class LiteDependency extends LiteDependencyItem implements ILiteDependency {

	protected ICpItem fCpItem = null; // component attributes to search for
	protected int fFlags = 0;         // RTE flags
	// collection to store candidates to resolve dependency  
	protected Map<ILiteComponent, ILiteDependencyItem> fComponentEntries = new LinkedHashMap<ILiteComponent, ILiteDependencyItem>();
	
	// list of component items that stop the search  
	protected Map<ILiteComponentItem, ILiteDependencyItem> fStopItems = null;
	
	boolean fbDeny = false;
	
	public LiteDependency( ICpItem item, boolean bDeny) {
		fCpItem = item;
		fbDeny = bDeny;
	}

	public LiteDependency( ICpItem item, int flags) {
		this(item, false);
		fFlags = flags;
	}
	
	
	@Override
	public int getFlags() {
		return fFlags;
	}

	@Override
	public boolean isMaster() {
		return true;
	}


	@Override
	public boolean isDeny() {
		return fbDeny;
	}

	@Override
	public boolean isResolved() {
		if(fResult == EEvaluationResult.IGNORED)
			return true;
		if(fResult == EEvaluationResult.FULFILLED)
			return true;
		if(fResult == EEvaluationResult.INCOMPATIBLE)
			return false;
		return isDeny();      
	}
	
	@Override
	public Collection<ILiteComponent> getComponents() {
		return fComponentEntries.keySet();
	}

	@Override
	public ICpItem getCpItem() {
		return fCpItem;
	}
	
	
	@Override
	public EEvaluationResult getEvaluationResult(ILiteComponent component) {
		ILiteDependencyItem entry = fComponentEntries.get(component);
		if(entry != null) {
			EEvaluationResult result = entry.getEvaluationResult();
			return result;
		}
		return EEvaluationResult.UNDEFINED;
	}

	@Override
	public ILiteComponent getBestMatch() {
		// TODO add bundle and variant calculations
		ILiteComponent bestComponent = null;
		//EEvaluationResult bestResult = EEvaluationResult.MISSING;
		for(Entry<ILiteComponent, ILiteDependencyItem> e : fComponentEntries.entrySet()) {
			ILiteComponent c = e.getKey();
			EEvaluationResult r = e.getValue().getEvaluationResult();
			if(r == EEvaluationResult.FULFILLED) {
				return c;
			} else if(r == EEvaluationResult.SELECTABLE) {
				if(bestComponent == null)
					bestComponent = c;
				else
					return null;
			}
		}
		return bestComponent;
	}

	@Override
	public void addComponent(ILiteComponent component, EEvaluationResult result) {
		ILiteDependencyItem de = new LiteDependencyItem(component, result);
		fComponentEntries.put(component, de);
		if(fResult.ordinal() < result.ordinal())
			fResult = result;
	}

	@Override
	public void addStopItem(ILiteComponentItem item, EEvaluationResult result) {
		if(fStopItems == null) 
			fStopItems = new LinkedHashMap<ILiteComponentItem, ILiteDependencyItem>();

		fStopItems.put(item, new LiteDependencyItem(item, result));
		if(fResult.ordinal() < result.ordinal())
			fResult = result;
	}

	@Override
	public Collection<? extends ILiteDependencyItem> getChildren() {
		return fComponentEntries.values();
	}


	@Override
	public String getDescription() {
		EEvaluationResult res = getEvaluationResult();
		switch(res) {
		case CONFLICT:
			return CpStrings.LiteDependency_Conflict;
		case INCOMPATIBLE_API:
			return CpStrings.LiteDependency_SelectCompatibleAPI;
		case INCOMPATIBLE:
		case INCOMPATIBLE_BUNDLE:
		case INCOMPATIBLE_VARIANT:
		case INCOMPATIBLE_VENDOR:
		case INCOMPATIBLE_VERSION:
			return CpStrings.LiteDependency_SelectCompatibleComponent;
		case INSTALLED:
			return CpStrings.LiteDependency_UpdatePackVariantOrBundleSelection;
		case MISSING:
			return CpStrings.LiteDependency_InstallMissingComponent;
		case MISSING_API:
			return CpStrings.LiteDependency_MissingAPI;
		case MISSING_BUNDLE:
			return CpStrings.LiteDependency_MissingBundle;
		case MISSING_VARIANT:
			return CpStrings.LiteDependency_MissingVariant;
		case MISSING_VENDOR:
			return CpStrings.LiteDependency_MissingVendor;
		case MISSING_VERSION:
			return CpStrings.LiteDependency_MissingVersion;
		case SELECTABLE:
			return CpStrings.LiteDependency_SelectComponentFromList;
		case UNAVAILABLE:
			return CpStrings.LiteDependency_ComponentNotAvailable;
		case UNAVAILABLE_PACK:
			return CpStrings.LiteDependency_PackNotSelected;
		
		case FULFILLED:
		case UNDEFINED:
		case ERROR:
		case FAILED:
		case IGNORED:
		case INACTIVE:
		default:
			break;
		}
		return super.getDescription();
	}
}
