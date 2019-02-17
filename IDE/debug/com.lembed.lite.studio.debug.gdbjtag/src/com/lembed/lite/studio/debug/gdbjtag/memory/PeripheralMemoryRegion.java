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

package com.lembed.lite.studio.debug.gdbjtag.memory;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.debug.core.model.MemoryByte;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralRegisterVMNode;

/**
 * The Class PeripheralMemoryRegion.
 */
public class PeripheralMemoryRegion implements Comparable<PeripheralMemoryRegion> {

	// ------------------------------------------------------------------------

	private long fAddressOffset;
	private long fSizeBytes;
	private List<PeripheralRegisterVMNode> fVMNodes;

	private MemoryByte[] fBytes;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral memory region.
	 *
	 * @param offset the offset
	 * @param sizeBytes the size bytes
	 */
	public PeripheralMemoryRegion(long offset, long sizeBytes) {

		fAddressOffset = offset;
		fSizeBytes = sizeBytes;

		fVMNodes = new LinkedList<>();
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the address offset.
	 *
	 * @return the address offset
	 */
	public long getAddressOffset() {
		return fAddressOffset;
	}

	/**
	 * Gets the size bytes.
	 *
	 * @return the size bytes
	 */
	public long getSizeBytes() {
		return fSizeBytes;
	}

	/**
	 * Adds the node.
	 *
	 * @param node the node
	 */
	public void addNode(PeripheralRegisterVMNode node) {
		fVMNodes.add(node);
	}

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public List<PeripheralRegisterVMNode> getNodes() {
		return fVMNodes;
	}

	/**
	 * Check if the parameter region is contiguous or contained in the current
	 * region.
	 * 
	 * @param region
	 *            the region to be compared with the current region.
	 * @return true if contiguous or contained.
	 */
	public boolean isContiguous(PeripheralMemoryRegion region) {
		return (region.fAddressOffset <= (fAddressOffset + fSizeBytes));
	}

	/**
	 * Concatenate to contiguous regions by adjusting the current region size to
	 * fully include the given region and take of region nodes.
	 * 
	 * @param region
	 *            the region to be concatenated with the current region.
	 */
	public void concatenate(PeripheralMemoryRegion region) {

		// Assume the regions are contiguous or contained.
		assert (region.fAddressOffset <= (fAddressOffset + fSizeBytes));

		if ((region.fAddressOffset + region.fSizeBytes) > (fAddressOffset + fSizeBytes)) {
			// Increase the size
			fSizeBytes = (region.fAddressOffset + region.fSizeBytes) - fAddressOffset;
		}

		// Take over nodes from given region.
		fVMNodes.addAll(region.getNodes());
	}

	/**
	 * Sets the bytes.
	 *
	 * @param bytes the new bytes
	 */
	public void setBytes(MemoryByte[] bytes) {
		fBytes = bytes;
	}

	/**
	 * Gets the bytes.
	 *
	 * @return the bytes
	 */
	public MemoryByte[] getBytes() {
		return fBytes;
	}

	// ------------------------------------------------------------------------

	@Override
	public int compareTo(PeripheralMemoryRegion comp) {
		return Long.signum((getAddressOffset() - comp.getAddressOffset()));
	}

	@Override
	public String toString() {
		return String.format("[Region 0x%08X, 0x%X, %d nodes]", fAddressOffset, fSizeBytes, fVMNodes.size()); //$NON-NLS-1$
	}

	// ------------------------------------------------------------------------
}
