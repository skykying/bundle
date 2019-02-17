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
import java.util.LinkedList;
import java.util.List;

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.device.core.svd.AbstractTreePreOrderIterator;
import com.lembed.lite.studio.device.core.svd.ITreeIterator;
import com.lembed.lite.studio.device.core.svd.Leaf;
import com.lembed.lite.studio.device.core.svd.Node;

/**
 * Wrapper over the tree node parsed from the SVD file. All details of a
 * peripheral are available from this point. Some of them are cached, for faster
 * access.
 */
public class SvdPeripheralDMNode extends SvdDMNode {

	// ------------------------------------------------------------------------

	private String fHexAddress;
	private BigInteger fBigAbsoluteAddress;

	private String fGroupName;
	private String fVersion;

	private Boolean fIsSystem;

	/**
	 * The address where the Cortex-M system registers start.
	 */
	private static final BigInteger fSystemLimit = new BigInteger("E0000000", 16);//NON-NLS-1$  //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new SVD peripheral DM node.
	 *
	 * @param node the node
	 */
	public SvdPeripheralDMNode(Leaf node) {

		super(node);

		// GdbActivator.log("SvdPeripheralDMNode(" + node + ")");

		fBigAbsoluteAddress = null;
		fHexAddress = null;
		fIsSystem = null;
		fGroupName = null;
		fVersion = null;
	}

	/**
	 * Clear all internal references.
	 */
	@Override
	public void dispose() {


		fBigAbsoluteAddress = null;
		fHexAddress = null;
		fIsSystem = null;
		fGroupName = null;
		fVersion = null;

		super.dispose();
	}

	// ------------------------------------------------------------------------

	@Override
	protected SvdObjectDMNode[] prepareChildren(Leaf node) {

		if (node == null || !node.hasChildren()) {
			return null;
		}

		// GdbActivator.log("prepareChildren(" + node.getProperty("name") +
		// ")");

		Leaf group = ((Node) node).findChild("registers"); //$NON-NLS-1$
		if (!group.hasChildren()) {
			return null;
		}

		List<SvdObjectDMNode> list = new LinkedList<>();
		for (Leaf child : ((Node) group).getChildren()) {

			// Keep only <register> and <cluster> nodes
			if (child.isType("register")) { //$NON-NLS-1$
				list.add(new SvdRegisterDMNode(child));
			} else if (child.isType("cluster")) { //$NON-NLS-1$
				list.add(new SvdClusterDMNode(child));
			}
		}

		SvdObjectDMNode[] array = list.toArray(new SvdObjectDMNode[list.size()]);

		// Preserve apparition order.
		return array;
	}

	// ------------------------------------------------------------------------

	/**
	 * Enumerate all peripherals of the same device and find the derived from
	 * node. The name is taken from the derivedFrom attribute.
	 *
	 * @return a peripheral node, or null if not found.
	 */
	@Override
	protected Leaf findDerivedFromNode() {

		String derivedFromName = getNode().getPropertyOrNull("derivedFrom"); //$NON-NLS-1$
		final SvdDerivedFromPath path = SvdDerivedFromPath.createPeripheralPath(derivedFromName);

		if (path == null) {
			return null;
		}

		Node root = getNode().getParent();
		while (!root.isType("device")) { //$NON-NLS-1$
			root = root.getParent();
		}

		ITreeIterator peripheralNodes = new AbstractTreePreOrderIterator() {

			@Override
			public boolean isIterable(Leaf node) {

				if (node.isType("peripherals")) { //$NON-NLS-1$
					return true;
				} else if (node.isType("peripheral")) { //$NON-NLS-1$
					if (path.peripheralName.equals(node.getProperty("name"))) { //$NON-NLS-1$
						return true;
					}
					return false;
				}
				return false;
			}

			@Override
			public boolean isLeaf(Leaf node) {

				if (node.isType("peripheral")) { //$NON-NLS-1$
					return true;
				}
				return false;
			}
		};

		// Iterate only the current device children nodes
		peripheralNodes.setTreeNode(root);

		Leaf ret = null;
		for (Leaf node : peripheralNodes) {

			// GdbActivator.log(node);
			if (node.isType("peripheral")) { //$NON-NLS-1$
				// There should be only one, filtered by the iterator.
				if (ret == null) {
					ret = node;
				} else {
					GdbActivator.log("Non unique SVD path " + path); //$NON-NLS-1$
				}
			}
		}

		return ret;
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return getHexAddress();
	}

	/**
	 * Get peripheral block length.
	 *
	 * @return a big integer value with the length in bytes, or null if not
	 *         found.
	 */
	@Override
	public BigInteger getBigSizeBytes() {

		String size;

		size = getAddressBlockSizeElement(getNode());
		if ((size == null) && (getDerivedFromNode() != null)) {
			size = getAddressBlockSizeElement(getDerivedFromNode());
		}

		if (size != null) {
			return SvdUtils.parseScaledNonNegativeBigInteger(size);
		}

		return null;
	}

	/**
	 * Get the size element from inside the first addressBlock.
	 *
	 * @param node
	 *            a tree node with addressBlock.
	 * @return the size node or null if not found.
	 */
	private static String getAddressBlockSizeElement(Leaf node) {

		if (node.hasChildren()) {
			for (Leaf child : ((Node) node).getChildren()) {
				if (child.isType("addressBlock")) { //$NON-NLS-1$
					String usage = child.getProperty("usage", ""); //$NON-NLS-1$ //$NON-NLS-2$
					if ("registers".equals(usage)) { //$NON-NLS-1$
						return child.getProperty("size", null); //$NON-NLS-1$
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get the numeric value of the address. Decimal, hex and binary values are
	 * accepted.
	 * <p>
	 * To support 64-bit addresses, use BigInteger objects.
	 *
	 * @return a big integer.
	 */
	@Override
    public BigInteger getBigAbsoluteAddress() {

		if (fBigAbsoluteAddress == null) {
			fBigAbsoluteAddress = SvdUtils.parseScaledNonNegativeBigInteger(getBaseAddress());
		}
		return fBigAbsoluteAddress;
	}

	/**
	 * Get the address formatted as a fixed size hex string.
	 * <p>
	 * Also used as peripheral ID.
	 *
	 * @return a string with the "%08X" formatted value.
	 */
	public String getHexAddress() {

		if (fHexAddress == null) {
			fHexAddress = String.format("0x%08X", getBigAbsoluteAddress().longValue());//NON-NLS-1$ //$NON-NLS-1$
		}

		return fHexAddress;
	}

	/**
	 * Get the original address value from the SVD file. Mandatory, not derived.
	 *
	 * @return a string with the address, usually a hex number.
	 */
	public String getBaseAddress() {
		return getNode().getProperty("baseAddress", "0");//NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-1$
	}

	/**
	 * Check if the peripheral is a Cortex-M system peripheral, i.e. has an
	 * address higher then 0xE0000000.
	 *
	 * @return true if the peripheral is a system peripheral.
	 */
	public boolean isSystem() {

		if (fIsSystem == null) {

			BigInteger addr = getBigAbsoluteAddress();
			if (addr.compareTo(fSystemLimit) >= 0) {
				fIsSystem = new Boolean(true);
			} else {
				fIsSystem = new Boolean(false);
			}
		}
		return fIsSystem.booleanValue();
	}

	/**
	 * Get group name.
	 *
	 * @return a string, possibly empty.
	 */
	public String getGroupName() {

		if (fGroupName == null) {
			fGroupName = getPropertyWithDerived("groupName");//NON-NLS-1$ //$NON-NLS-1$
		}
		return fGroupName;
	}

	/**
	 * Get version string.
	 *
	 * @return a string, possibly empty.
	 */
	public String getVersion() {

		if (fVersion == null) {
			fVersion = getPropertyWithDerived("version");//NON-NLS-1$ //$NON-NLS-1$
		}
		return fVersion;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {

		return "[" + getClass().getSimpleName() + ": " + getDisplayName() + ", " + getBaseAddress() + ", " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		       + getBigSizeBytes() + ", " + getAccess() + ", \"" + getDescription() + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
