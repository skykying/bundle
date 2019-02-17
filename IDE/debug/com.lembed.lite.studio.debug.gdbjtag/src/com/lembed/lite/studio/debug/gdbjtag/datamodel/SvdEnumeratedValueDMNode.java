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

package com.lembed.lite.studio.debug.gdbjtag.datamodel;

import java.math.BigInteger;

import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralValue;
import com.lembed.lite.studio.device.core.svd.Leaf;

/**
 * The Class SvdEnumeratedValueDMNode.
 */
public class SvdEnumeratedValueDMNode extends SvdObjectDMNode {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new svd enumerated value DM node.
	 *
	 * @param node the node
	 */
	public SvdEnumeratedValueDMNode(Leaf node) {
		super(node);
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return getNode().getProperty("value"); //$NON-NLS-1$

	}

	/**
	 * Checks if is default.
	 *
	 * @return true, if is default
	 */
	public boolean isDefault() {

		String isDefault = getNode().getProperty("isDefault"); //$NON-NLS-1$
		if (isDefault.isEmpty()) {
			return false;
		}

		return "true".equalsIgnoreCase(isDefault); //$NON-NLS-1$
	}


	/**
	 * Checks if is match for value.
	 *
	 * @param value the value
	 * @return true, if is match for value
	 */
	public boolean isMatchForValue(PeripheralValue value) {

		BigInteger bigEnumerationValue = null;
		try {
			// TODO: The value tag in enumeratedValue accepts do not care bits
			// represented by "x".

			bigEnumerationValue = SvdUtils.parseScaledNonNegativeBigInteger(getValue());

			if (bigEnumerationValue.equals(value.getBigValue())) {
				return true;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		return false;
	}

	// ------------------------------------------------------------------------


	@Override
	public String toString() {

		String value = getValue();
		if (isDefault()) {
			value = "default"; //$NON-NLS-1$
		}

		return "[" + getClass().getSimpleName() + ": " + getName() + ", " + value + ", \"" + getDescription() + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	// ------------------------------------------------------------------------

}
