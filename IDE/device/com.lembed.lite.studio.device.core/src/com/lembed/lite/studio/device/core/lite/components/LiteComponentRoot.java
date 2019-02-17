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

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.ICpComponent;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.enums.EComponentAttribute;

/**
 * Class represents root of component hierarchy
 * Contains collection of groups 
 */
public class LiteComponentRoot extends LiteComponentItem {
	/**
	 * Default root constructors 
	 */
	public LiteComponentRoot() {
		this(CmsisConstants.EMPTY_STRING);
	}

	public LiteComponentRoot(String name) {
		super(null, "root"); //$NON-NLS-1$
		fComponentAttribute = EComponentAttribute.CCLASS;
		fbExclusive = false;
		fName = name;
	}
	
	
	@Override
	public void addComponent(ICpComponent cpComponent, int flags) {
		String className = cpComponent.getAttribute(CmsisConstants.CCLASS);
		if(className == null || className.isEmpty())
			return; 
		// ensure childItem
		ILiteComponentItem classItem = getChild(className); 
		
		if(classItem == null ) {
			classItem = new LiteComponentClass(this, className);
			addChild(classItem);
		}
		classItem.addComponent(cpComponent, flags);
	}
	
	
	@Override
	public void addCpItem(ICpItem cpItem) {
		String className = cpItem.getAttribute(CmsisConstants.CCLASS);
		if(className == null || className.isEmpty())
			return; 
		// check if class exists 
		ILiteComponentItem classItem = getChild(className); 
		if(classItem == null ) {
			return; // no class => no add
		}
		classItem.addCpItem(cpItem);
	}
}
