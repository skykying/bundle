/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Liviu Ionescu - initial version
 *     		(many thanks to Code Red for providing the inspiration)
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.datamodel;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.model.IMemoryBlockExtension;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.device.core.svd.Leaf;

/**
 * Peripheral data model definition. It is based on the PeripheralDetails class
 * that maps over the SVD tree node.
 */
public class PeripheralDMNode extends SvdPeripheralDMNode implements IAdaptable {

	// ------------------------------------------------------------------------

	private IMemoryBlockExtension fMemoryBlock;
	private boolean fIsChecked;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral DM node.
	 *
	 * @param node the node
	 */
	public PeripheralDMNode(Leaf node) {

		super(node);

		fMemoryBlock = null;
		fIsChecked = true;
	}

	@Override
    public void dispose() {

		GdbActivator.log("dispose() " + this); //$NON-NLS-1$
		
		if (fMemoryBlock != null) {
			fMemoryBlock = null;
		}

		// Peripheral nodes are special, and cannot be disposed, so DO NOT call
		// super.dispose() here, dispose only their children.
		disposeChildren();
	}

	@Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class clazz) {
		return null;
	}

	// ------------------------------------------------------------------------

	/**
	 * Sets the memory block.
	 *
	 * @param memoryBlockExtension the new memory block
	 */
	public void setMemoryBlock(IMemoryBlockExtension memoryBlockExtension) {
		fMemoryBlock = memoryBlockExtension;
	}

	/**
	 * Gets the memory block.
	 *
	 * @return the memory block
	 */
	public IMemoryBlockExtension getMemoryBlock() {
		return fMemoryBlock;
	}

	/**
	 * Checks if is shown.
	 *
	 * @return true, if is shown
	 */
	public boolean isShown() {
		return fMemoryBlock != null;
	}

	/**
	 * Checks if is checked.
	 *
	 * @return true, if is checked
	 */
	public boolean isChecked() {
		return fIsChecked;
	}

	/**
	 * Sets the checked.
	 *
	 * @param flag the new checked
	 */
	public void setChecked(boolean flag) {
		fIsChecked = flag;
	}

	// ------------------------------------------------------------------------

	@Override
	public String toString() {

		return "[" + getClass().getSimpleName() + ": " + fMemoryBlock + ", " + super.toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	// ------------------------------------------------------------------------
}
