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
import com.lembed.lite.studio.device.core.enums.IEvaluationResult;
import com.lembed.lite.studio.device.core.lite.components.ILiteComponentItem;
import com.lembed.lite.studio.device.item.ICmsisItem;

/**
 * Base interface for object constructing dependency tree 
 */
public interface ILiteDependencyItem extends ICmsisItem, IEvaluationResult {

	/**
	 * Returns associated component item if any
	 * @return associated component item
	 */
	ILiteComponentItem getComponentItem();

	/**
	 * Returns associated ICpItem that is:
	 *  <ul>
	 * <li> a source of dependency (an ICpExpresiion or an ICpApi) 
	 * <li> or an ICpComponent corresponding to associated  IRteComponentItem
	 * </ul>
	 * @return ICpItem that is source of dependency or underlying ICpCompoent 
	 */
	ICpItem getCpItem();
	

	/**
	 * Checks if this item is a master item that defines severity and icon 
	 * @return true if this item is a master of its children
	 */
	boolean isMaster();

	
	/**
	 * Checks if this item is evaluated in negative context: it denies the matching components  
	 * @return if the dependency is a deny one
	 */
	boolean isDeny();

	
	@Override
	Collection<? extends ILiteDependencyItem> getChildren();
	
}
