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
 * The Class PeripheralClusterVMNode.
 */
public class PeripheralClusterVMNode extends PeripheralGroupVMNode {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral cluster VM node.
	 *
	 * @param parent the parent
	 * @param dmNode the dm node
	 */
	public PeripheralClusterVMNode(PeripheralTreeVMNode parent, SvdDMNode dmNode) {

		super(parent, dmNode);

	}

	// ------------------------------------------------------------------------

	@Override
	public String getDisplayNodeType() {
		return "Cluster"; //$NON-NLS-1$
	}

	@Override
	public String getImageName() {
		return "registergroup_obj"; //$NON-NLS-1$
	}

	@Override
	public String getDisplaySize() {
		return null;
	}

	// ------------------------------------------------------------------------
}
