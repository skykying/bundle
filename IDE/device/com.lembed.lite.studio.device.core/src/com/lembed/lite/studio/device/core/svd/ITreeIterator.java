/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial implementation.
 *******************************************************************************/

package com.lembed.lite.studio.device.core.svd;

import java.util.Iterator;

public interface ITreeIterator extends Iterator<Leaf>, Iterable<Leaf> {

	public void setTreeNode(Leaf node);

}
