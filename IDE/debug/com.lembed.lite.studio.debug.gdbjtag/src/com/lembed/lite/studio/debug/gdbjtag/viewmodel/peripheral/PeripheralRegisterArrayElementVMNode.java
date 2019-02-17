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

import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdDMNode;

/**
 * The Class PeripheralRegisterArrayElementVMNode.
 */
public class PeripheralRegisterArrayElementVMNode extends PeripheralRegisterVMNode {

	// ------------------------------------------------------------------------

	/** The big array address offset. */
	protected BigInteger fBigArrayAddressOffset;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral register array element VM node.
	 *
	 * @param parent the parent
	 * @param dmNode the dm node
	 * @param index the index
	 * @param offset the offset
	 */
	public PeripheralRegisterArrayElementVMNode(PeripheralTreeVMNode parent, SvdDMNode dmNode, String index,
			BigInteger offset) {

		super(parent, dmNode);

		// The node name must have a %s, substitute it with the actual index
		substituteIndex(index);
		fBigArrayAddressOffset = offset;
	}

	/**
	 * Get the local offset to the array base.
	 */
	@Override
	public BigInteger getThisBigAddressOffset() {
		return fBigArrayAddressOffset;
	}

	// ------------------------------------------------------------------------
}
