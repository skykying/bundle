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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.debug.core.DebugException;

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdEnumeratedValueDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdEnumerationDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdFieldDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdObjectDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdUtils;

/**
 * The Class PeripheralRegisterFieldVMNode.
 */
public class PeripheralRegisterFieldVMNode extends PeripheralRegisterVMNode {

	// ------------------------------------------------------------------------
	private SvdEnumeratedValueDMNode fEnumeratedValueDMNode;

	/**
	 * Instantiates a new peripheral register field VM node.
	 *
	 * @param parent the parent
	 * @param dmNode the dm node
	 */
	// ------------------------------------------------------------------------
	public PeripheralRegisterFieldVMNode(PeripheralTreeVMNode parent, SvdDMNode dmNode) {

		super(parent, dmNode);

		fValue.setDisplayFormat(PeripheralValue.FORMAT_HEX);
		fEnumeratedValueDMNode = null;
	}

	/**
	 * Gets the enumerated value DM node.
	 *
	 * @return the enumerated value DM node
	 */
	// ------------------------------------------------------------------------
	public SvdEnumeratedValueDMNode getEnumeratedValueDMNode() {
		if (fEnumeratedValueDMNode == null) {
			try {
				fEnumeratedValueDMNode = ((SvdFieldDMNode) fDMNode).findEnumeratedValue((PeripheralValue) getValue());
			} catch (DebugException e) {
				GdbActivator.log(e);
			}
		}
		return fEnumeratedValueDMNode;
	}

	/**
	 * Clear enumerated value DM node.
	 */
	public void clearEnumeratedValueDMNode() {
		fEnumeratedValueDMNode = null;
	}

	/**
	 * Gets the enumeration combo items.
	 *
	 * @return the enumeration combo items
	 */
	public String[] getEnumerationComboItems() {
		SvdEnumerationDMNode enumeration = ((SvdFieldDMNode) fDMNode).getWriteEnumerationDMNode();

		List<String> list = new LinkedList<>();
		if (enumeration != null) {
			SvdObjectDMNode children[] = enumeration.getChildren();
			for (int i = 0; i < children.length; ++i) {
				SvdEnumeratedValueDMNode child = (SvdEnumeratedValueDMNode) children[i];
				String name = child.getName();
				BigInteger bigValue = SvdUtils.parseScaledNonNegativeBigInteger(child.getValue());
				// Use the same format as for displaying, "value: name"
				list.add(String.format("0x%X: %s", bigValue, name)); //$NON-NLS-1$
			}
		}
		// GdbActivator.log("Combo has " + list.size());
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Try to find the current field value in the list of possible enumeration
	 * combo selections.
	 *
	 * @return an Integer with the index, or null if not found.
	 */
	public Integer getEnumerationComboIndex() {

		try {
			return ((SvdFieldDMNode) fDMNode).findEnumeratedComboIndex((PeripheralValue) getValue());

		} catch (DebugException e) {
			e.printStackTrace();
		}

		// Match not found.
		return null;
	}

	@Override
	public String getDisplayValue() {

		String readAction = getReadAction();
		if (!readAction.isEmpty()) {
			return "(read " + readAction + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		String value = getValueString();

		if (value.isEmpty()) {
			return null;
		}

		if (!isEnumeration()) {
			return value;
		}

		// Process enumerations, if any
		SvdEnumeratedValueDMNode node = getEnumeratedValueDMNode();
		if (node == null) {
			return value;
		}

		// Append the enumeration name to the existing value.
		String enumerationName = node.getName();
		if (!enumerationName.isEmpty()) {
			value += ": " + enumerationName; //$NON-NLS-1$
		}
		return value;
	}

	@Override
	public boolean supportsValueModification() {

		// Prevent write only fields to be modified
		return !isReadOnly() && isReadAllowed();
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {

		// GdbActivator.log("verifyValue(" + expression + ")");

		boolean isValid = false;
		if (isEnumeration()) {
			// Enumerations come as normal values.
			isValid = PeripheralValue.isNumeric(expression);
		} else {
			isValid = PeripheralValue.isNumeric(expression);
		}
		return isValid;
	}

	// ------------------------------------------------------------------------

	@Override
	protected void update(BigInteger newValue) {

		boolean sameValue = getPeripheralValue().getBigValue().equals(newValue);

		if (!sameValue) {

			clearEnumeratedValueDMNode();

			// Propagate change to parent.
			PeripheralRegisterVMNode parent = (PeripheralRegisterVMNode) getParent();

			PeripheralValue parentValue = null;
			parentValue = parent.getPeripheralValue();

			BigInteger bigValue;
			if (parentValue.isNumeric()) {
				bigValue = parentValue.getBigValue();
			} else {
				bigValue = BigInteger.ZERO;
			}
			// Mask out existing value, using andNot().
			bigValue = bigValue.andNot(getBitMask().shiftLeft(getOffsetBits()));

			// Mask in new value, using or().
			bigValue = bigValue.or(newValue.and(getBitMask()).shiftLeft(getOffsetBits()));

			// Update the parent with the newly computed value.
			// This in turn will use updateFieldValueFromParent() to update
			// all fields, including this one.
			parent.update(bigValue);
		}
	}

	/**
	 * Update the current field after the parent changed.
	 */
	public void updateFieldValueFromParent() {

		PeripheralRegisterVMNode parent = (PeripheralRegisterVMNode) getParent();
		PeripheralValue parentValue = parent.getPeripheralValue();

		BigInteger bigValue;
		if (parentValue.isNumeric()) {
			bigValue = parentValue.getBigValue();
			bigValue = bigValue.shiftRight(getOffsetBits()).and(getBitMask());
		} else {
			bigValue = BigInteger.ZERO;
		}

		// Update the current field with the newly computed value.
		setChanged(getPeripheralValue().update(bigValue));
	}

	@Override
	public String getDisplayNodeType() {
		return "Field"; //$NON-NLS-1$
	}

	@Override
	public String getImageName() {
		return "field"; //$NON-NLS-1$
	}

	@Override
	public String getDisplayAddress() {
		return convertFieldToString();
	}

	@Override
	public String getDisplayOffset() {
		return convertFieldToString();
	}

	private String convertFieldToString() {

		int lsb = getOffsetBits();
		int msb = lsb + getWidthBits() - 1;

		if (lsb == msb) {
			return String.format("[%d]", lsb); //$NON-NLS-1$
		}
        return String.format("[%d:%d]", msb, lsb); //$NON-NLS-1$
	}

	@Override
	public int getOffsetBits() {
		return ((SvdFieldDMNode) fDMNode).getOffset();
	}

	@Override
	public int getWidthBits() {
		return ((SvdFieldDMNode) fDMNode).getWidthBits();

	}

	/**
	 * Checks if is enumeration.
	 *
	 * @return true, if is enumeration
	 */
	public boolean isEnumeration() {
		return ((SvdFieldDMNode) fDMNode).isEnumeration();
	}

	@Override
	public BigInteger getThisBigAddressOffset() {
		return BigInteger.ZERO;
	}

	// ------------------------------------------------------------------------
}
