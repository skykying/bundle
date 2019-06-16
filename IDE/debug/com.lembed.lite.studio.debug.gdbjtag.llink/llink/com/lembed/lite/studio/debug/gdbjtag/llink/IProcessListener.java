/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.llink;

/**
 * The listener interface for receiving IProcess events.
 * The class that is interested in processing a IProcess
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addIProcessListener<code> method. When
 * the IProcess event occurs, that object's appropriate
 * method is invoked.
 *
 */
public interface IProcessListener {
 
	
	/**
	 * Callback.
	 *
	 * @param ey the ey
	 * @return the object
	 */
	Object callback(Object ey);
}
