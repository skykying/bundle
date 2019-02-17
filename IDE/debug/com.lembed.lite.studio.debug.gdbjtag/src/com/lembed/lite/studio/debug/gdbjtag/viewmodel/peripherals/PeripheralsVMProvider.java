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

import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.ui.viewmodel.AbstractVMAdapter;
import org.eclipse.cdt.dsf.ui.viewmodel.IVMModelProxy;
import org.eclipse.cdt.dsf.ui.viewmodel.IVMNode;
import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.AbstractDMVMProvider;
import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.RootDMVMNode;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.IPropertiesUpdate;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IColumnPresentation;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.render.peripherals.PeripheralsColumnPresentation;

/**
 * Provide the column presentation and the model proxy strategy for the
 * peripherals view.
 */
@SuppressWarnings("restriction")
public class PeripheralsVMProvider extends AbstractDMVMProvider {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripherals VM provider.
	 *
	 * @param adapter the adapter
	 * @param presentationContext the presentation context
	 * @param session the session
	 */
	public PeripheralsVMProvider(AbstractVMAdapter adapter, IPresentationContext presentationContext,
	                             DsfSession session) {

		super(adapter, presentationContext, session);

		GdbActivator.log("PeripheralsVMProvider() " + presentationContext + " " + session); //$NON-NLS-1$ //$NON-NLS-2$

		RootDMVMNode rootDMVMNode = new RootDMVMNode(this);
		PeripheralsVMNode peripheralVMNode = new PeripheralsVMNode(this, getSession());
		addChildNodes(rootDMVMNode, new IVMNode[] { peripheralVMNode });
		setRootNode(rootDMVMNode);
	}

	// Contributed by IColumnPresentationFactory.

	@Override
	public String getColumnPresentationId(IPresentationContext context, Object element) {

		return PeripheralsColumnPresentation.ID;
	}

	// Contributed by IColumnPresentationFactory.

	// Factory for the column presentation.
	@Override
	public IColumnPresentation createColumnPresentation(IPresentationContext context, Object element) {

		return new PeripheralsColumnPresentation();
	}

	// Factory for the model proxy strategy.
	@Override
	protected IVMModelProxy createModelProxyStrategy(Object object) {

		return new PeripheralsVMModelProxyStrategy(this, object);
	}

	@Override
	public void refresh() {

		// GdbActivator.log("PeripheralsVMProvider.refresh() ");

		super.refresh();

		// TODO: perhaps clear caches, if any
	}

	@Override
	public void update(IPropertiesUpdate[] propertiesUpdates) {

		// GdbActivator.log("PeripheralsVMProvider.update() ");

		super.update(propertiesUpdates);
	}

	// ------------------------------------------------------------------------
}
