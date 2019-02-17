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

import org.eclipse.debug.core.DebugException;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdDMNode;

/**
 * The Class PeripheralRegisterArrayVMNode.
 */
public class PeripheralRegisterArrayVMNode extends PeripheralGroupVMNode {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral register array VM node.
	 *
	 * @param parent the parent
	 * @param dmNode the dm node
	 */
	public PeripheralRegisterArrayVMNode(PeripheralTreeVMNode parent, SvdDMNode dmNode) {

		super(parent, dmNode);

	}

	// ------------------------------------------------------------------------

	@Override
	public String getDisplayNodeType() {
		return "Register array"; //$NON-NLS-1$
	}

	@Override
	public String getImageName() {
		return "registergroup_obj"; //$NON-NLS-1$
	}

	@Override
	public String getDisplaySize() {
		int dim = fDMNode.getArrayDim();
		if (dim != 0) {
			return dim + " elements"; //$NON-NLS-1$
		}

		return null;
	}

	@Override
	public BigInteger getBigAbsoluteAddress() {

		BigInteger base;
		try {
			base = ((PeripheralGroupVMNode) getRegisterGroup()).getBigAbsoluteAddress();
		} catch (DebugException e) {
			base = BigInteger.ZERO;
		}
		BigInteger offset;
		offset = fDMNode.getBigAddressOffset();

		return base.add(offset);
	}

	// ------------------------------------------------------------------------
}
