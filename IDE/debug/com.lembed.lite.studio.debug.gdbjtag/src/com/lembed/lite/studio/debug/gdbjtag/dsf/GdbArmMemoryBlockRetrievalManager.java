/*******************************************************************************
 * Copyright (c) 2015 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.dsf;

import org.eclipse.cdt.dsf.debug.model.DsfMemoryBlockRetrieval;
import org.eclipse.cdt.dsf.debug.service.IMemory.IMemoryDMContext;
import org.eclipse.cdt.dsf.debug.service.IRunControl.IStartedDMEvent;
import org.eclipse.cdt.dsf.gdb.internal.memory.GdbMemoryBlockRetrievalManager;
import org.eclipse.cdt.dsf.service.DsfServiceEventHandler;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IMemoryBlockRetrieval;

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.memory.PeripheralMemoryBlockRetrieval;

/**
 * The Class GdbArmMemoryBlockRetrievalManager.
 */
@SuppressWarnings("restriction")
public class GdbArmMemoryBlockRetrievalManager extends GdbMemoryBlockRetrievalManager {

	/**
	 * Instantiates a new gdb arm memory block retrieval manager.
	 *
	 * @param modelId the String model id
	 * @param config the ILaunchConfiguration
	 * @param session the DsfSession
	 */
	public GdbArmMemoryBlockRetrievalManager(String modelId, ILaunchConfiguration config, DsfSession session) {
		super(modelId, config, session);
	}


	@Override
	protected IMemoryBlockRetrieval createMemoryBlockRetrieval(String model, ILaunchConfiguration config,
			DsfSession session) {
		DsfMemoryBlockRetrieval memRetrieval = null;

		try {
			memRetrieval = new PeripheralMemoryBlockRetrieval(model, config, session);
		} catch (DebugException e) {
			GdbActivator.log(e.getStatus());
		}

		return memRetrieval;
	}

	@Override
    @DsfServiceEventHandler
	public void eventDispatched(IStartedDMEvent event) {

		super.eventDispatched(event);

		// If a new memory context is starting, create its memory retrieval
		// instance
		if (event.getDMContext() instanceof IMemoryDMContext) {
			IMemoryDMContext memDmc = (IMemoryDMContext) event.getDMContext();
			IMemoryBlockRetrieval memRetrieval = getMemoryBlockRetrieval(memDmc);
			if (memRetrieval != null && memRetrieval instanceof PeripheralMemoryBlockRetrieval) {

				DsfSession.getSession(memDmc.getSessionId()).registerModelAdapter(PeripheralMemoryBlockRetrieval.class,
						memRetrieval);
			}
		}
	}

}
