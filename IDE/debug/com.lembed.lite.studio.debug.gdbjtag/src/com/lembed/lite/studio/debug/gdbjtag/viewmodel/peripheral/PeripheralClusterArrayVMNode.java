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

import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdDMNode;

/**
 * The Class PeripheralClusterArrayVMNode.
 */
public class PeripheralClusterArrayVMNode extends PeripheralClusterVMNode {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral cluster array VM node.
	 *
	 * @param parent the parent
	 * @param dmNode the dm node
	 */
	public PeripheralClusterArrayVMNode(PeripheralTreeVMNode parent, SvdDMNode dmNode) {

		super(parent, dmNode);

	}

	// ------------------------------------------------------------------------

	@Override
	public String getDisplayNodeType() {
		return "Cluster array"; //$NON-NLS-1$
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

	// ------------------------------------------------------------------------
}
