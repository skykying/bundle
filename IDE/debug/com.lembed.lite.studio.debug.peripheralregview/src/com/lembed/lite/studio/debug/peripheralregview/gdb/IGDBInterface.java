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

// could currently be turned into an Interface
public abstract class IGDBInterface {
	public abstract boolean hasActiveDebugSession();

	public abstract long readMemory(long laddress, int iByteCount);

	public abstract int writeMemory(long laddress, long lvalue, int iByteCount);

	public abstract void dispose();

	public abstract void addSuspendListener(IGDBInterfaceSuspendListener listener);

	public abstract void addterminateListener(IGDBInterfaceTerminateListener listener);
}
