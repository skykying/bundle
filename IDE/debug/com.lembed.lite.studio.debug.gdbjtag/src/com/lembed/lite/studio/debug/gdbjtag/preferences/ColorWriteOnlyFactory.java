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
 * A factory for creating ColorWriteOnly objects.
 */
public class ColorWriteOnlyFactory extends PeripheralsColorFactory {

	/**
	 * Instantiates a new color write only factory.
	 */
	public ColorWriteOnlyFactory() {
		super(PeripheralColumnLabelProvider.COLOR_WRITEONLY, PersistentPreferences.PERIPHERALS_COLOR_WRITEONLY);
	}
}
