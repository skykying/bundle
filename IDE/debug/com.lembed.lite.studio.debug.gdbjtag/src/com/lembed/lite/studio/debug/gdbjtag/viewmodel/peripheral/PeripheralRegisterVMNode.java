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
import org.eclipse.debug.core.model.IValue;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdRegisterDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdUtils;

/**
 * PeripheralRegister objects are used to represent peripheral registers or
 * fields in the rendering view. Since SVD hierarchies are simple, registers are
 * direct descendants of another register group and register children can be
 * only fields.
 */
public class PeripheralRegisterVMNode extends PeripheralTreeVMNode {

	// ------------------------------------------------------------------------

	/** The bit mask. */
	protected BigInteger fBitMask;
	
	/** The value. */
	protected PeripheralValue fValue;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral register VM node.
	 *
	 * @param parent the parent
	 * @param dmNode the dm node
	 */
	public PeripheralRegisterVMNode(PeripheralTreeVMNode parent, SvdDMNode dmNode) {

		super(parent, dmNode);

		fBitMask = null;

		fValue = new PeripheralValue();
		fValue.setDisplayFormatForBitWidth(getWidthBits());
	}

	/**
	 * Clear all internal references.
	 */
	@Override
	public void dispose() {

		fValue = null;
		super.dispose();
	}

	// ------------------------------------------------------------------------

	@Override
	public IValue getValue() throws DebugException {
		return fValue;
	}

	@Override
	public boolean supportsValueModification() {
		return super.supportsValueModification();
	}

	/**
	 * Gets the peripheral value.
	 *
	 * @return the peripheral value
	 */
	public PeripheralValue getPeripheralValue() {
		return fValue;
	}

	/**
	 * Gets the value string.
	 *
	 * @return the value string
	 */
	public String getValueString() {

		IValue value = null;
		try {
			value = getValue();
		} catch (DebugException e1) {
		}

		if (value instanceof PeripheralValue) {

			try {
				return value.getValueString();
			} catch (DebugException e) {
			}

		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public void setValue(String value) throws DebugException {

		
		GdbActivator.log("setValue(" + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void setValue(IValue value) throws DebugException {

		GdbActivator.log("setValue(" + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		if ((value instanceof PeripheralValue)) {
			fValue = ((PeripheralValue) value);
		} else {
			setValue(value.getValueString());
		}
		setChanged(true);
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {

		// GdbActivator.log("verifyValue(" + expression + ")");
		return PeripheralValue.isNumeric(expression);
	}

	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		return verifyValue(value.getValueString());
	}

	// ------------------------------------------------------------------------

	/**
	 * Sets the numeric value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean setNumericValue(String value) {

		setChanged(false);
		try {
			if (!verifyValue(value)) {
				return false;
			}

			BigInteger bigValue;
			bigValue = SvdUtils.parseScaledNonNegativeBigInteger(value);
			// Strip unused bits.
			bigValue = bigValue.and(getBitMask());

			// Update value and hasChanged flag
			update(bigValue);

		} catch (NumberFormatException e) {
			// Illegal values are ineffective, leave value unchanged.
		} catch (DebugException e) {
		}

		// Always refresh, to be sure fields are updated
		return true;
	}

	/**
	 * Update the current value and related children values.
	 * <p>
	 * If the new value is different from the previous value, the dirty flag is
	 * set.
	 *
	 * @param newValue
	 *            the new value.
	 */
	protected void update(BigInteger newValue) {

		boolean sameValue = getPeripheralValue().getBigValue().equals(newValue);

		if (!sameValue) {

			long offset = getPeripheralBigAddressOffset().longValue();
			// Write register.
			getPeripheral().getMemoryBlock().writePeripheralRegister(offset, getWidthBytes(), newValue);

			// Re-read and update the entire peripheral, since many registers
			// may be affected by one write.
			getPeripheral().getMemoryBlock().updatePeripheralRenderingValues();
		}
	}

	/**
	 * Sets the value.
	 *
	 * @param newValue the new value
	 */
	public void setValue(BigInteger newValue) {

		// Update with actual value.
		setChanged(getPeripheralValue().update(newValue));

		Object[] children = getChildren();
		for (int i = 0; i < children.length; ++i) {
			if (children[i] instanceof PeripheralRegisterFieldVMNode) {
				((PeripheralRegisterFieldVMNode) children[i]).updateFieldValueFromParent();
			}
		}
	}

	/**
	 * Get the bit mask corresponding to the current register or field width.
	 * <p>
	 * The bit mask is computed as ((1&lt;&lt;width)-1) and it looks like 0x1,
	 * 0x3, 0x7...
	 *
	 * @return a BigInteger with some right side bits set.
	 */
	protected BigInteger getBitMask() {

		if (fBitMask == null) {
			fBitMask = BigInteger.ONE;
			fBitMask = fBitMask.shiftLeft(getWidthBits());
			fBitMask = fBitMask.subtract(BigInteger.ONE);
		}
		return fBitMask;
	}

	@Override
	public String getDisplayNodeType() {
		return "Register"; //$NON-NLS-1$
	}

	@Override
	public String getImageName() {
		return "register_obj"; //$NON-NLS-1$
	}

	@Override
	public String getDisplaySize() {

		int width;
		width = getWidthBits();

		if (width == 1) {
			return "1 bit"; //$NON-NLS-1$
		} else if (width > 1) {
			return String.format("%d bits", width); //$NON-NLS-1$
		}

		return null;
	}

	@Override
	public String getDisplayValue() {

		String readAction = getReadAction();
		if (!readAction.isEmpty()) {
			return "(read " + readAction + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		String value = getValueString();

		if (!value.isEmpty()) {
			return value;
		}

		return null;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	/**
	 * Get the value to be displayed for the reset value, possibly with mask.
	 *
	 * @return a string or null if no value is available.
	 */
	public String getDisplayResetValue() {

		if (fDMNode instanceof SvdRegisterDMNode) {

			// Only registers have reset definitions
			String resetValue = ((SvdRegisterDMNode) fDMNode).getResetValue();
			if (resetValue.isEmpty()) {
				return null;
			}

			String resetMask = ((SvdRegisterDMNode) fDMNode).getResetMask();
			if (resetValue.isEmpty()) {
				return resetValue;
			}

			return resetValue + "/" + resetMask; //$NON-NLS-1$
		}

		return null;
	}

	// ------------------------------------------------------------------------

	/**
	 * Perform an explicit read of the register, usually when the register has
	 * read side effects and is not automatically fetched.
	 */
	public void forceReadRegister() {

		if (isField())
			return;

		// TODO: implement forceReadRegister()
		
		GdbActivator.log("unimplemented forceReadRegister()"); //$NON-NLS-1$
	}

	/**
	 * Gets the offset bits.
	 *
	 * @return the offset bits
	 */
	public int getOffsetBits() {
		return 0;
	}

	/**
	 * Gets the width bits.
	 *
	 * @return the width bits
	 */
	public int getWidthBits() {
		return ((SvdRegisterDMNode) fDMNode).getWidthBits();
	}

	/**
	 * Gets the width bytes.
	 *
	 * @return the width bytes
	 */
	public int getWidthBytes() {
		return (getWidthBits() + 7) / 8;
	}

	// ------------------------------------------------------------------------
}
