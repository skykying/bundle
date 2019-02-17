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
package com.lembed.lite.studio.debug.gdbjtag.restart;

import com.lembed.lite.studio.core.AbstractUIActivator;

import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIActivator {

	// ------------------------------------------------------------------------

	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "com.lembed.lite.studio.debug.gdbjtag.restart"; //$NON-NLS-1$

	@Override
	public String getBundleId() {
		return PLUGIN_ID;
	}

	// ------------------------------------------------------------------------

	// The shared instance
	private static Activator fgInstance;

	public static Activator getInstance() {
		return fgInstance;
	}

	/**
	 * Instantiates a new activator.
	 */
	public Activator() {

		super();
		fgInstance = this;
	}

	// ------------------------------------------------------------------------

	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

}
