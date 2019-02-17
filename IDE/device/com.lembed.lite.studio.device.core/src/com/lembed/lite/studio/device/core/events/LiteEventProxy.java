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

package com.lembed.lite.studio.device.core.events;

import com.lembed.lite.studio.device.generic.GenericListenerList;

/**
 * Default implementation of IRteEventProxy interface
 */
public class LiteEventProxy extends GenericListenerList<ILiteEventListener, LiteEvent> implements ILiteEventProxy {

	@Override
	public void emitLiteEvent(final String topic, Object data) {
		notifyListeners(new LiteEvent(topic, data));
	}

	@Override
	public void handle(LiteEvent event) { // proxy method 
		notifyListeners(event); 
	}
}
