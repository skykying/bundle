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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.lembed.lite.studio.device.core.CpStrings;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.info.ICpComponentInfo;
import com.lembed.lite.studio.device.core.info.ICpPackInfo;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;

/**
 * Default implementation of IRteDependencyResult
 */
public class LiteDependencyResult extends LiteDependencyItem implements ILiteDependencyResult {

	Set<ILiteDependency> fDependencies = new LinkedHashSet<ILiteDependency>();
	
	/**
	 *	Constructs DependencyResult with  associated component item 
	 */
	public LiteDependencyResult(ILiteComponentItem componentItem) {
		super(componentItem);
	}
	
	@Override
	public Collection<ILiteDependency> getChildren() {
		return getDependencies();
	}
	
	@Override
	public Collection<ILiteDependency> getDependencies() {
		return fDependencies;
	}


	@Override
	public void addDependency(ILiteDependency dependency) {
		if(dependency == null)
			return;
		if(fDependencies.contains(dependency))
			return;
		if(dependency.isResolved())
			return;
		fDependencies.add(dependency);
	}

	@Override
	public void removeDependency(ILiteDependency dependency) {
		if(dependency == null)
			return;
		if(!fDependencies.contains(dependency))
			return;
		fDependencies.remove(dependency);
	}


	@Override
	public void setEvaluationResult(EEvaluationResult result) {
		super.setEvaluationResult(result);
		purgeDependencies();
		
	}

	/**
	 *  Removes dependencies that are greater than overall result since they are irrelevant
	 */
	protected void purgeDependencies() {
		int thisOrdinal = getEvaluationResult().ordinal();
		for (Iterator<ILiteDependency> iterator = fDependencies.iterator(); iterator.hasNext();) {
			ILiteDependency d = iterator.next();
			EEvaluationResult r = d.getEvaluationResult();
			if(r.ordinal() > thisOrdinal) {
				iterator.remove();
				cachedChildArray = null;
			}
		}
	}


	@Override
	public String getDescription() {
		EEvaluationResult res = getEvaluationResult();
		if(!fDependencies.isEmpty()) {
			switch(res) {
			case INSTALLED:
			case MISSING:
			case SELECTABLE:
			case UNAVAILABLE:
			case UNAVAILABLE_PACK:
				return CpStrings.LiteDependencyResult_AdditionalComponentRequired;
			case INCOMPATIBLE:
			case FAILED:
				return CpStrings.LiteDependencyResult_ComponentConficts;
			default:
				break;
			}
		}
		String s = null; 
		if( fComponentItem != null && !res.isFulfilled() ) {
			ICpComponentInfo ci = fComponentItem.getActiveCpComponentInfo();
			if(ci != null && ci.getComponent() == null) {
				s = EEvaluationResult.MISSING.getDescription();
				s += ". "; //$NON-NLS-1$
				s += res.getDescription();
				if(res == EEvaluationResult.UNAVAILABLE_PACK) {
					s += ": ";  //$NON-NLS-1$
					ICpPackInfo pi = ci.getPackInfo();
					String id = pi.isVersionFixed() ? pi.getId() : pi.getPackFamilyId();
					s += id;
				}
				return s;
			}
		} 
		s = res.getDescription();
		
		if(s != null)
			return s;
		return super.getDescription();
	}

}
