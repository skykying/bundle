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

package com.lembed.lite.studio.debug.gdbjtag.render.peripheral;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralGroupVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralRegisterVMNode;

/**
 * The Class PeripheralNameFilter.
 */
public class PeripheralNameFilter extends ViewerFilter {

	// ------------------------------------------------------------------------

	/**
	 * Upper case version of the filter text.
	 */
	private String fFilterText;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral name filter.
	 *
	 * @param filterText the filter text
	 */
	public PeripheralNameFilter(String filterText) {
		fFilterText = (filterText != null) ? filterText.toUpperCase() : null;
	}

	/**
	 * Gets the filter text.
	 *
	 * @return the filter text
	 */
	public String getFilterText() {
		return fFilterText;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if (element instanceof PeripheralGroupVMNode) {
			return true; // Groups are always visible
		}

		if (element instanceof PeripheralRegisterVMNode) {

			String qualifiedName;
			qualifiedName = ((PeripheralRegisterVMNode) element).getQualifiedName();

			if (qualifiedName == null) {
				return true;
			}

			// Comparison performed as upper text
			if (qualifiedName.toUpperCase().contains(fFilterText)) {
				// Registers & fields are visible only if they
				// match the filter
				return true;
			}
		}

		// All other things are not visible
		return false;
	}

	// ------------------------------------------------------------------------
}
