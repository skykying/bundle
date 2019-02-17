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

package com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripherals;

import org.eclipse.cdt.dsf.ui.viewmodel.AbstractVMProvider;
import org.eclipse.cdt.dsf.ui.viewmodel.DefaultVMModelProxyStrategy;
import org.eclipse.debug.internal.ui.viewers.model.provisional.ICheckboxModelProxy;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.jface.viewers.TreePath;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.PeripheralDMContext;

/**
 * The Class PeripheralsVMModelProxyStrategy.
 */
@SuppressWarnings("restriction")
public class PeripheralsVMModelProxyStrategy extends DefaultVMModelProxyStrategy implements ICheckboxModelProxy {

	//private Object fRootElement;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripherals VM model proxy strategy.
	 *
	 * @param provider the provider
	 * @param rootElement the root element
	 */
	public PeripheralsVMModelProxyStrategy(AbstractVMProvider provider, Object rootElement) {
		super(provider, rootElement);

		GdbActivator.log("PeripheralsVMModelProxyStrategy() " + this + " " + provider + " " + rootElement); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		//fRootElement = rootElement;
	}

	// Contributed by ICheckboxModelProxy

	/**
	 * This is called when the radio button is checked in the interface.
	 */
	@Override
	public boolean setChecked(IPresentationContext context, Object viewerInput, TreePath path, boolean checked) {

		GdbActivator.log("PeripheralsVMModelProxyStrategy.setChecked(" + checked + ")"); //$NON-NLS-1$ //$NON-NLS-2$

		Object segment = path.getLastSegment();
		if ((segment instanceof PeripheralsVMNode.PeripheralsVMContext)) {

			PeripheralsVMNode.PeripheralsVMContext peripheralVMContext = (PeripheralsVMNode.PeripheralsVMContext) segment;
			PeripheralDMContext peripheralDMContext = (PeripheralDMContext) peripheralVMContext.getDMContext();
			peripheralDMContext.setChecked(checked);
			peripheralDMContext.displayPeripheralMonitor(context.getWindow());
			return checked;
		}
		return false;
	}

	// ------------------------------------------------------------------------
}
