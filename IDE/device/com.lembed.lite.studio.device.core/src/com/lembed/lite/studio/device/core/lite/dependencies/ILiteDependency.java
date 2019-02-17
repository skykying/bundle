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

import com.lembed.lite.studio.device.core.enums.EEvaluationResult;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponent;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;

/**
 * Interface for dependency evaluation and resolving  
 */
public interface ILiteDependency extends ILiteDependencyItem {
	
	/**
	 * Checks if this dependency is resolved 
	 * @return true if resolved
	 */
	boolean isResolved();
	
	
	/**
	 * Returns dependency evaluation result for specific component candidate
	 * @return dependency evaluation result if component found, otherwise EEvaluationResult.UNDEFINED
	 */
	EEvaluationResult getEvaluationResult(ILiteComponent component);

	
	/**
	 * Returns list of collected components which are candidates to resolve dependencies
	 * @return list of collected candidates to resolve dependencies  
	 */
	Collection<ILiteComponent> getComponents();
	
	/**
	 * Returns component that best matches dependency
	 * @return list of collected candidates to resolve dependencies  
	 */
	ILiteComponent getBestMatch();

	
	/**
	 * Adds component to the internal list of candidate components  
	 * @param component that is a candidate to fulfill dependency 
	 * @param result result of the evaluation showing to which extent the component fulfills the dependency  
	 */
	void addComponent(ILiteComponent component, EEvaluationResult result);
	
	/**
	 * Adds component hierarchy item that stopped dependency evaluation    
	 * @param item a component hierarchy at which evaluation has stopped
	 * @param result reason why evaluation has stopped
	 */
	void addStopItem(ILiteComponentItem item, EEvaluationResult result);
	
	
	int getFlags();
	
}
