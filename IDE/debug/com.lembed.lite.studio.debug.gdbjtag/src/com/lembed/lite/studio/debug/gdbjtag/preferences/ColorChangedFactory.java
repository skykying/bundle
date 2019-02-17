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
 * A factory for creating ColorChanged objects.
 */
public class ColorChangedFactory extends PeripheralsColorFactory {

	/**
	 * Instantiates a new color changed factory.
	 */
	public ColorChangedFactory() {
		super(PeripheralColumnLabelProvider.COLOR_CHANGED, PersistentPreferences.PERIPHERALS_COLOR_CHANGED);
	}
}
