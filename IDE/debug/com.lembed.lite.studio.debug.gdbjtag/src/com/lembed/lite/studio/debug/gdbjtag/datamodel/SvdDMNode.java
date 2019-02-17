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

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.device.core.svd.Leaf;

/**
 * The Class SvdDMNode.
 */
public class SvdDMNode extends SvdObjectDMNode implements Comparable<SvdDMNode> {

	// ------------------------------------------------------------------------

	/**
	 * The content of the SVD <tt>&lt;access&gt;</tt> element. Defines the
	 * default access rights for all registers. Access rights can be redefined
	 * on any lower level of the description using the access element there.
	 * <p>
	 * <ul>
	 * <li><b>read-only</b>: read access is permitted. Write operations have an
	 * undefined result.
	 * <li><b>write-only</b>: write access is permitted. Read operations have an
	 * undefined result.
	 * <li><b>read-write</b>: both read and write accesses are permitted. Writes
	 * affect the state of the register and reads return a value related to the
	 * register.
	 * <li><b>writeOnce</b>: only the first write after reset has an effect on
	 * the register. Read operations deliver undefined results.
	 * <li><b>read-writeOnce</b>: Read operations deliver a result related to
	 * the register content. Only the first write access to this register after
	 * a reset will have an effect on the register content.
	 * </ul>
	 */
	private String fAccess;

	/**
	 * The content of the SVD <tt>&lt;readAction&gt;</tt> element.
	 * <p>
	 * If set, it specifies the side effect following a read operation. If not
	 * set, the register is not modified.
	 * <p>
	 * The defined side effects are:
	 * <ul>
	 * <li><b>clear</b>: The register is cleared (set to zero) following a read
	 * operation.
	 * <li><b>set</b>: The register is set (set to ones) following a read
	 * operation.
	 * <li><b>modify</b>: The register is modified in some way after a read
	 * operation.
	 * <li><b>modifyExternal</b>: One or more dependent resources other than the
	 * current register are immediately affected by a read operation (it is
	 * recommended that the register description specifies these dependencies).
	 * </ul>
	 * Debuggers are not expected to read this register location unless
	 * explicitly instructed by the user.
	 */
	private String fReadAction;


	/**
	 * Instantiates a new svd DM node.
	 *
	 * @param node the node
	 */
	public SvdDMNode(Leaf node) {

		super(node);

		fAccess = null;
		fReadAction = null;
	}

	/**
	 * Clear all internal references.
	 */
	@Override
    public void dispose() {

		fAccess = null;
		fReadAction = null;

		super.dispose();
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the big absolute address.
	 *
	 * @return the big absolute address
	 */
	public BigInteger getBigAbsoluteAddress() {
		return null;
	}

	/**
	 * Get the register access info.
	 * <p>
	 * The legal values are:
	 * <ul>
	 * <li>read-only
	 * <li>write-only
	 * <li>read-write
	 * <li>writeOnce
	 * <li>read-writeOnce
	 * </ul>
	 * 
	 * @return a string, possibly empty.
	 */
	public String getAccess() {

		if (fAccess == null) {
			fAccess = getPropertyWithDerivedWithParent("access"); //$NON-NLS-1$
		}
		return fAccess;
	}

	/**
	 * Checks if is read only.
	 *
	 * @return true, if is read only
	 */
	public boolean isReadOnly() {

		String access = getAccess();
		return "read-only".equals(access); //$NON-NLS-1$
	}

	/**
	 * Checks if is write only.
	 *
	 * @return true, if is write only
	 */
	public boolean isWriteOnly() {

		String access = getAccess();
		if ("write-only".equals(access) || "writeOnce".equals(access)) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		}
		return false;
	}

	/**
	 * Checks for read action.
	 *
	 * @return true, if successful
	 */
	public boolean hasReadAction() {
		return !getReadAction().isEmpty();
	}

	/**
	 * Checks if is read allowed.
	 *
	 * @return true, if is read allowed
	 */
	public boolean isReadAllowed() {
		return !hasReadAction();
	}

	/**
	 * Gets the width bits.
	 *
	 * @return the width bits
	 */
	public int getWidthBits() {
		return -1;
	}

	/**
	 * Gets the big size bytes.
	 *
	 * @return the big size bytes
	 */
	public BigInteger getBigSizeBytes() {
		return null;
	}

	/**
	 * Gets the big address offset.
	 *
	 * @return the big address offset
	 */
	public BigInteger getBigAddressOffset() {
		return BigInteger.ZERO;
	}

	/**
	 * Gets the read action.
	 *
	 * @return the read action
	 */
	public String getReadAction() {

		if (fReadAction == null) {
			fReadAction = getPropertyWithDerivedWithParent("readAction"); //$NON-NLS-1$
		}

		return fReadAction;
	}

	/**
	 * Checks if is array.
	 *
	 * @return true, if is array
	 */
	public boolean isArray() {
		return !(getNode().getProperty("dim").isEmpty()); //$NON-NLS-1$
	}

	/**
	 * Gets the array dim.
	 *
	 * @return the array dim
	 */
	public int getArrayDim() {

		String str = getNode().getProperty("dim"); //$NON-NLS-1$
		int dim;
		try {
			dim = (int) SvdUtils.parseScaledNonNegativeLong(str);
		} catch (NumberFormatException e) {
			GdbActivator.log("Node " + getNode().getName() + ", non integer <dim> " + str); //$NON-NLS-1$ //$NON-NLS-2$
			dim = 0;
		}

		return dim;
	}

	/**
	 * Gets the big integer array address increment.
	 *
	 * @return the big integer array address increment
	 */
	public BigInteger getBigIntegerArrayAddressIncrement() {

		String increment = getNode().getProperty("dimIncrement"); //$NON-NLS-1$

		try {
			return SvdUtils.parseScaledNonNegativeBigInteger(increment);
		} catch (NumberFormatException e) {
			GdbActivator.log("Node " + getNode().getName() + ", non number <dimIncrement> " + increment); //$NON-NLS-1$ //$NON-NLS-2$
			return BigInteger.ZERO;
		}
	}

	/**
	 * Gets the array indices.
	 *
	 * @return the array indices
	 */
	public String[] getArrayIndices() {

		int dim = getArrayDim();
		if (dim == 0) {
			return new String[] { "0" }; //$NON-NLS-1$
		}

		// Create the array of string indices
		String[] arr = new String[dim];

		// Init with increasing numbers
		for (int i = 0; i < dim; ++i) {
			arr[i] = String.valueOf(i);
		}

		String index = getNode().getProperty("dimIndex"); //$NON-NLS-1$
		if (!index.isEmpty()) {
			// "[0-9]+\-[0-9]+|[A-Z]-[A-Z]|[_0-9a-zA-Z]+(,\s*[_0-9a-zA-Z]+)+"
			if (index.contains("-")) { //$NON-NLS-1$
				// First two cases, range of numbers or of letters.
				String[] range = index.split("-"); //$NON-NLS-1$
				try {
					int from = Integer.parseInt(range[0]);
					int to = Integer.parseInt(range[1]);

					int i = 0;
					// Expand inclusive range of numbers.
					for (int j = from; (j <= to) && (i < arr.length); j++, i++) {
						arr[i] = String.valueOf(j);
					}
				} catch (NumberFormatException e) {
					// Range of characters.
					char from = range[0].charAt(0);
					char to = range[1].charAt(0);

					int i = 0;
					// Expand inclusive range of upper case letters.
					for (char j = from; (j <= to) && (i < arr.length); j++, i++) {
						arr[i] = String.valueOf(j);
					}
				}
			} else if (index.contains(",")) { //$NON-NLS-1$
				// Third case, comma separated list of strings.
				String[] indices = index.split(","); //$NON-NLS-1$
				// Trim and copy.
				for (int i = 0; (i < indices.length) && (i < arr.length); ++i) {
					arr[i] = indices[i].trim();
				}
			}

		}

		return arr;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + ": " + getDisplayName() + ", " + getAccess() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	}

	@Override
	public int compareTo(SvdDMNode comp) {
		return getDisplayName().compareTo(comp.getDisplayName());
	}

	// ------------------------------------------------------------------------
}
