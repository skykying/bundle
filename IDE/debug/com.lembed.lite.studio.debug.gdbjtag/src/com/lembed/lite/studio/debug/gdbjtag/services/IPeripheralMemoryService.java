/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.services;

import org.eclipse.cdt.core.IAddress;
import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.debug.service.IMemory.IMemoryDMContext;
import org.eclipse.cdt.dsf.service.IDsfService;
import org.eclipse.debug.core.model.MemoryByte;

/**
 * The Interface IPeripheralMemoryService.
 */
public interface IPeripheralMemoryService extends IDsfService {

	// ------------------------------------------------------------------------

	/**
	 * Initialize memory data.
	 *
	 * @param memContext the mem context
	 * @param rm the rm
	 */
	public abstract void initializeMemoryData(final IMemoryDMContext memContext, RequestMonitor rm);

	/**
	 * Checks if is big endian.
	 *
	 * @param context the context
	 * @return true, if is big endian
	 */
	public abstract boolean isBigEndian(IMemoryDMContext context);

	/**
	 * Gets the address size.
	 *
	 * @param context the context
	 * @return the address size
	 */
	public abstract int getAddressSize(IMemoryDMContext context);

	/**
	 * Gets the memory.
	 *
	 * @param memoryDMC the memory DMC
	 * @param address the address
	 * @param offset the offset
	 * @param word_size the word size
	 * @param word_count the word count
	 * @param drm the DataRequestMonitor
	 */
	public abstract void getMemory(IMemoryDMContext memoryDMC, IAddress address, long offset, int word_size,
			int word_count, DataRequestMonitor<MemoryByte[]> drm);

	/**
	 * Sets the memory.
	 *
	 * @param memoryDMC the memory DMC
	 * @param address the address
	 * @param offset the offset
	 * @param word_size the word size
	 * @param word_count the word count
	 * @param buffer the buffer
	 * @param rm the rm
	 */
	public abstract void setMemory(IMemoryDMContext memoryDMC, IAddress address, long offset, int word_size,
			int word_count, byte[] buffer, RequestMonitor rm);

	// ------------------------------------------------------------------------
}
