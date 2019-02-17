/*******************************************************************************
 * Copyright (c) 2015 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.preferences;

import com.lembed.lite.studio.debug.gdbjtag.PeripheralsColorFactory;
import com.lembed.lite.studio.debug.gdbjtag.render.peripheral.PeripheralColumnLabelProvider;

/**
 * A factory for creating ColorChangedLight objects.
 */
public class ColorChangedLightFactory extends PeripheralsColorFactory {

	/**
	 * Instantiates a new color changed light factory.
	 */
	public ColorChangedLightFactory() {
		super(PeripheralColumnLabelProvider.COLOR_CHANGED_LIGHT, PersistentPreferences.PERIPHERALS_COLOR_CHANGED_LIGHT);
	}
}
