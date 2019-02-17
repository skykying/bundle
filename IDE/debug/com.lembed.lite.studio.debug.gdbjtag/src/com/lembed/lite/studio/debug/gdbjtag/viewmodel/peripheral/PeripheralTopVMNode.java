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

package com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral;

import java.math.BigInteger;

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdPeripheralDMNode;
import com.lembed.lite.studio.debug.gdbjtag.memory.PeripheralMemoryBlockExtension;

/**
 * The Class PeripheralTopVMNode.
 */
public class PeripheralTopVMNode extends PeripheralGroupVMNode {

	// ------------------------------------------------------------------------

	private PeripheralMemoryBlockExtension fMemoryBlock;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral top VM node.
	 *
	 * @param parent the parent
	 * @param dmNode the dm node
	 * @param memoryBlock the memory block
	 */
	public PeripheralTopVMNode(PeripheralTreeVMNode parent, SvdDMNode dmNode,
	                           PeripheralMemoryBlockExtension memoryBlock) {

		super(parent, dmNode);

		
		GdbActivator.log("PeripheralTopVMNode() " + dmNode.getName()); //$NON-NLS-1$
		
		fMemoryBlock = memoryBlock;
	}

	@Override
	public void dispose() {

		fMemoryBlock = null;
		
		GdbActivator.log("PeripheralTopVMNode.dispose()"); //$NON-NLS-1$
		super.dispose();
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the memory block.
	 *
	 * @return the memory block
	 */
	public PeripheralMemoryBlockExtension getMemoryBlock() {
		return fMemoryBlock;
	}

	/**
	 * Register groups are peripherals or clusters, return the address of the
	 * peripheral.
	 *
	 * @return a big integer with the start address.
	 */
	@Override
	public BigInteger getBigAbsoluteAddress() {
		return fDMNode.getBigAbsoluteAddress();
	}

	@Override
	public String getDisplayNodeType() {
		return "Peripheral"; //$NON-NLS-1$
	}

	@Override
	public String getImageName() {
		return "peripheral"; //$NON-NLS-1$
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the display group name.
	 *
	 * @return the display group name
	 */
	public String getDisplayGroupName() {
		return ((SvdPeripheralDMNode) fDMNode).getGroupName();
	}

	/**
	 * Gets the display version.
	 *
	 * @return the display version
	 */
	public String getDisplayVersion() {
		return ((SvdPeripheralDMNode) fDMNode).getVersion();
	}

	// ------------------------------------------------------------------------
}
