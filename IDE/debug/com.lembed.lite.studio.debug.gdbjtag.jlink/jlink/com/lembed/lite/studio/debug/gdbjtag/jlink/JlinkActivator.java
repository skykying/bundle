/*******************************************************************************
 * Copyright (c) 2013 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.jlink;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JlinkActivator extends AbstractUIPlugin {

	// ------------------------------------------------------------------------

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.debug.gdbjtag.jlink"; //$NON-NLS-1$

	public String getBundleId() {
		return PLUGIN_ID;
	}

	// ------------------------------------------------------------------------

	// The shared instance
	private static JlinkActivator activator;

	public static JlinkActivator getInstance() {
		return activator;
	}

	public JlinkActivator() {

		super();
		activator = this;
	}

	// ------------------------------------------------------------------------

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	public static void log(IStatus status) {
		getInstance().getLog().log(status);
	}

	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1,
				">>> JLINK " + "Internal Error", e)); //$NON-NLS-1$
	}

	public static void log(String message) {
		log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1, ">>> JLINK " + message,
				null)); //$NON-NLS-1$
	}
}
