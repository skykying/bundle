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
import java.util.regex.Pattern;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * Peripheral register values usually are BigInteger but for enumerations can be
 * String.
 */
public class PeripheralValue implements IValue {

	// ------------------------------------------------------------------------

	/** The Constant FORMAT_NONE. */
	public static final int FORMAT_NONE = 0;
	
	/** The Constant FORMAT_HEX. */
	public static final int FORMAT_HEX = 1;
	
	/** The Constant FORMAT_HEX8. */
	public static final int FORMAT_HEX8 = 2;
	
	/** The Constant FORMAT_HEX16. */
	public static final int FORMAT_HEX16 = 3;
	
	/** The Constant FORMAT_HEX32. */
	public static final int FORMAT_HEX32 = 4;
	
	/** The Constant FORMAT_HEX64. */
	public static final int FORMAT_HEX64 = 5;

	/**
	 * Checks if is numeric.
	 *
	 * @param str the str
	 * @return true, if is numeric
	 */
	public static boolean isNumeric(String str) {
		String regExp = "(0[xX]|\\+|\\-)?[0-9A-Fa-f]+"; //$NON-NLS-1$
		return Pattern.matches(regExp, str);
	}

	// ------------------------------------------------------------------------

	private Object fValue;
	private int fDisplayFormat;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral value.
	 */
	public PeripheralValue() {
		fValue = null;
		fDisplayFormat = FORMAT_NONE;
	}

	// ------------------------------------------------------------------------

	/**
	 * Set the format used when converting the value to string.
	 * 
	 * @param displayFormat
	 *            an integer (FORMAT_NONE, FORMAT_HEX, ...)
	 */
	public void setDisplayFormat(int displayFormat) {
		fDisplayFormat = displayFormat;
	}

	/**
	 * Sets the display format for bit width.
	 *
	 * @param bitWidth the new display format for bit width
	 */
	public void setDisplayFormatForBitWidth(int bitWidth) {

		if (bitWidth <= 8) {
			setDisplayFormat(PeripheralValue.FORMAT_HEX8);
		} else if (bitWidth <= 16) {
			setDisplayFormat(PeripheralValue.FORMAT_HEX16);
		} else if (bitWidth <= 32) {
			setDisplayFormat(PeripheralValue.FORMAT_HEX32);
		} else if (bitWidth <= 64) {
			setDisplayFormat(PeripheralValue.FORMAT_HEX64);
		} else {
			setDisplayFormat(PeripheralValue.FORMAT_HEX);
		}

	}

	/**
	 * Update the object value field with possibly a different node.
	 * 
	 * @param value
	 *            the new node.
	 * @return true if the new node is different.
	 */
	public boolean update(Object value) {

		Object previousValue = fValue;
		fValue = value;
		boolean hasChanged;
		if (previousValue != null) {
			hasChanged = !previousValue.equals(fValue);
		} else {
			hasChanged = false; // First time set, not a value change
		}
		return hasChanged;
	}

	/**
	 * Checks for value.
	 *
	 * @return true, if successful
	 */
	public boolean hasValue() {
		return fValue != null;
	}

	/**
	 * Get the numeric value.
	 * 
	 * @return a BigInteger value.
	 */
	public BigInteger getBigValue() {

		if (fValue instanceof BigInteger) {
			return (BigInteger) fValue;
		}
        return null;
	}

	/**
	 * Checks if is numeric.
	 *
	 * @return true, if is numeric
	 */
	public boolean isNumeric() {
		return (fValue instanceof BigInteger);
	}

	// ------------------------------------------------------------------------

	@Override
	public String getModelIdentifier() {
		return null;
	}

	@Override
	public IDebugTarget getDebugTarget() {
		return null;
	}

	@Override
	public ILaunch getLaunch() {
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return null;
	}

	@Override
	public String getValueString() throws DebugException {

		if (isNumeric()) {

			// Numeric values are formatted as upper case hex strings, with
			// various length.
			BigInteger value = getBigValue();

			if (fDisplayFormat == FORMAT_HEX8) {
				return String.format("0x%02X", value); //$NON-NLS-1$
			} else if (fDisplayFormat == FORMAT_HEX16) {
				return String.format("0x%04X", value); //$NON-NLS-1$
			} else if (fDisplayFormat == FORMAT_HEX32) {
				return String.format("0x%08X", value); //$NON-NLS-1$
			} else if (fDisplayFormat == FORMAT_HEX64) {
				return String.format("0x%016X", value); //$NON-NLS-1$
			} else if (fDisplayFormat == FORMAT_HEX) {
				return String.format("0x%X", value); //$NON-NLS-1$
			} else {
				// Default is hex 32/64
				if (value.bitCount() <= 32) {
					// 32-bit register value
					return String.format("0x%08X", value); //$NON-NLS-1$
				}
                // 64-bit register value
                return String.format("0x%016X", value); //$NON-NLS-1$
			}
		} else if (fValue != null) {
			return fValue.toString();
		} else {
			return ""; //$NON-NLS-1$
		}
	}

	@Override
	public boolean isAllocated() throws DebugException {
		return false;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		return null;
	}

	@Override
	public boolean hasVariables() throws DebugException {
		return false;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		try {
			return getValueString();
		} catch (DebugException e) {
			return super.toString();
		}
	}

	// ------------------------------------------------------------------------
}
