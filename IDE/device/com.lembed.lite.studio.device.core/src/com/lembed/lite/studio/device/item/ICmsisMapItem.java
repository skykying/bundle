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

package com.lembed.lite.studio.device.item;

import com.lembed.lite.studio.device.generic.ITreeItem;
import com.lembed.lite.studio.device.generic.ITreeMapItem;

/**
 * Basic generic interface for CMSIS items with String-to-Item map collections 
 */
public interface ICmsisMapItem<T extends ITreeItem<T> & ICmsisItem> extends ICmsisTreeItem<T>, ITreeMapItem<String, T > {
	 
}
 