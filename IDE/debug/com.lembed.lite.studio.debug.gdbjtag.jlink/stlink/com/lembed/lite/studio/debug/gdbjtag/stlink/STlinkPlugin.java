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
package com.lembed.lite.studio.debug.gdbjtag.stlink;

import com.lembed.lite.studio.core.AbstractUIActivator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class STlinkPlugin extends AbstractUIActivator {

	// ------------------------------------------------------------------------

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.debug.gdbjtag.stlink"; //$NON-NLS-1$

	@Override
	public String getBundleId() {
		return PLUGIN_ID;
	}

	// ------------------------------------------------------------------------

	// The shared instance
	private static STlinkPlugin sTlinkPlugin;

	public static STlinkPlugin getInstance() {
		return sTlinkPlugin;
	}

	public STlinkPlugin() {

		super();
		sTlinkPlugin = this;
	}

	// ------------------------------------------------------------------------

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	public boolean isDebugging() {
		return true;
	}

	public static void log(IStatus status) {
		getInstance().getLog().log(status);
	}

	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1, ">>> STLINK " + "Internal Error", e)); //$NON-NLS-1$
	}

	public static void log(String message) {
		if (getInstance().isDebugging()) {
			log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1, ">>> STLINK  " + message, null)); // $NON-NLS-1$
		}
	}
}
