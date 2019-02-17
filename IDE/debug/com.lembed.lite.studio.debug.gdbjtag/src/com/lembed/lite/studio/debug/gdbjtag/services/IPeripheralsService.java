/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version 
 *     		(many thanks to Code Red for providing the inspiration)
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.services;

import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.debug.service.IRunControl;
import org.eclipse.cdt.dsf.service.IDsfService;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.IPeripheralDMContext;

/**
 * The Interface IPeripheralsService.
 */
public interface IPeripheralsService extends IDsfService {

	// ------------------------------------------------------------------------

	/**
	 * Gets the peripherals.
	 *
	 * @param containerDMContext the container DM context
	 * @param dataRequestMonitor the data request monitor
	 */
	public abstract void getPeripherals(IRunControl.IContainerDMContext containerDMContext,
			DataRequestMonitor<IPeripheralDMContext[]> dataRequestMonitor);

	// ------------------------------------------------------------------------
}
