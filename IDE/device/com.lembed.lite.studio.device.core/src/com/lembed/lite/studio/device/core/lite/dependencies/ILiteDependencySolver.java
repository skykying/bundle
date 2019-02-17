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

import com.lembed.lite.studio.device.core.data.ICpConditionContext;
import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;

/**
 *
 */
public interface ILiteDependencySolver extends ICpConditionContext{

	
	/**
	 * Evaluates dependencies for selected components
	 * @return worst dependency evaluation result
	 */
	EEvaluationResult evaluateDependencies();
	
	
	/**
	 * Tries to resolve component dependencies
	 * @return evaluation result after dependency resolving 
	 */
	EEvaluationResult resolveDependencies();
	
	
	/**
	 * Returns dependency item for given component item (bundle, group or component) 
	 * @param component IRteComponentItem for which to get result 
	 * @return dependency result or null if component item has no unresolved dependencies
	 */
	ILiteDependencyItem getDependencyItem(ILiteComponentItem componentItem); 
	
	/**
	 * Returns dependency evaluation result for given item (class, group or component) 
	 * @param item IRteComponentItem for which to get result 
	 * @return condition result or IGNORED if item has no result
	 */
	EEvaluationResult getEvaluationResult(ILiteComponentItem item); 
	
	
	/**
	 * Returns collection of dependency results (items and dependencies)
	 * @return collection of dependency results
	 */
	Collection<? extends ILiteDependencyItem> getDependencyItems();
	
}
