/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic & Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Liviu Ionescu - initial API and implementation
 *      LiteSTUDIO   -  document and bug fixed.
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.restart.action;

/**
 * The Class RestartCommandActionDelegate.
 */
public class RestartCommandActionDelegate extends DebugCommandActionDelegate {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new restart command action delegate.
	 */
	public RestartCommandActionDelegate() {
		super(new RestartCommandAction());
	}

}
