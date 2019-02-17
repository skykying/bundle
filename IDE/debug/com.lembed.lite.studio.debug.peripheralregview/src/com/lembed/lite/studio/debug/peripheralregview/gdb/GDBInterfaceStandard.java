/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.peripheralregview.gdb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

//import org.eclipse.cdt.debug.core.cdi.ICDISession;
//import org.eclipse.cdt.debug.core.cdi.model.ICDITarget;
//import org.eclipse.cdt.debug.internal.core.model.CDebugTarget;
//import org.eclipse.cdt.debug.mi.core.MIException;
//import org.eclipse.cdt.debug.mi.core.MISession;
//import org.eclipse.cdt.debug.mi.core.cdi.model.Target;
//import org.eclipse.cdt.debug.mi.core.output.MIMemory;

import org.eclipse.cdt.dsf.gdb.service.command.IGDBControl;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;

public class GDBInterfaceStandard extends IGDBInterface implements IDebugEventSetListener {
	private DsfSession miSession = null;
	
	@SuppressWarnings("unused")
	private IGDBControl fCommandControl = null;
	
	private List<IGDBInterfaceSuspendListener> suspendListener = new ArrayList<IGDBInterfaceSuspendListener>();
	private List<IGDBInterfaceTerminateListener> terminateListener = new ArrayList<IGDBInterfaceTerminateListener>();

	public GDBInterfaceStandard() {
		if (!hasActiveDebugSession())
			init();
	}
	

	public void addSuspendListener(IGDBInterfaceSuspendListener listener) {
		suspendListener.add(listener);
	}

	public void addterminateListener(IGDBInterfaceTerminateListener listener) {
		terminateListener.add(listener);
	}

	public void dispose() {
		miSession = null;
		DebugPlugin.getDefault().removeDebugEventListener(this);
	}

	private void init() {
		DebugPlugin.getDefault().addDebugEventListener(this);
	}

	public boolean hasActiveDebugSession() {
		if (miSession == null)
			return false;
		else
			return true;
	}



	/**
	 * Handle DebugEvents while an active Debug Session TODO: find a non
	 * Discouraged method to get miSession
	 */
	@Override
	public void handleDebugEvents(DebugEvent[] events) {
	
		
		for (DebugEvent event : events) {
			
			@SuppressWarnings("unused")
			Object source = event.getSource();					

			if (event.getKind() == DebugEvent.SUSPEND) {
				System.out.println("DebugEvent " + event.toString());

					miSession = (DsfSession) GDBSessionTranslator.getSession();

				for (IGDBInterfaceSuspendListener listener : suspendListener)
					listener.gdbSuspendListener();
			}
			if (event.getKind() == DebugEvent.TERMINATE) {
				for (IGDBInterfaceTerminateListener listener : terminateListener)
					listener.gdbTerminateListener();
			}
		}
	}


	@Override
	public long readMemory(long laddress, int iByteCount) {
		// TODO Auto-generated method stub
		try {
			return GDBSessionTranslator.readMemory(laddress, iByteCount);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}


	@Override
	public int writeMemory(long laddress, long lvalue, int iByteCount) {
		// TODO Auto-generated method stub
		try {
			return GDBSessionTranslator.writeMemory(laddress, lvalue,iByteCount);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
