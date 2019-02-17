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

package com.lembed.lite.studio.device.core.storage;

import org.eclipse.cdt.core.settings.model.ICStorageElement;

/**
 *	Provides factory method to create items implementing ICpItem interface
 *  @see ICpItem
 */
public interface ICStorageElementFactory {
	/**
	 * Factory method to create ICpItem-derived instances
	 * @param parent item that contains this one 
	 * @param tag XML tag for the item 
	 * @return created or existing ICpItem 
	 */
	public ICStorageElement  createElement(ICStorageElement  parent, String tag);
}
