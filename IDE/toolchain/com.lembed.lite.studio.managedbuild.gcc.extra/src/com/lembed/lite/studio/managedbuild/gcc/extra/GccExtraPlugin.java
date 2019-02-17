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
package com.lembed.lite.studio.managedbuild.gcc.extra;

import com.lembed.lite.studio.core.AbstractUIActivator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GccExtraPlugin extends AbstractUIActivator {

	// ------------------------------------------------------------------------

	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "com.lembed.lite.studio.managedbuild.gcc.extra"; //$NON-NLS-1$

	@Override
	public String getBundleId() {
		return PLUGIN_ID;
	}
	
	/**
	 * Gets the id prefix.
	 *
	 * @return the id prefix
	 */
	public static String getIdPrefix() {

		// keep it explicitly defined, since it must not be changed, even if the
		// plug-in id is changed
		return PLUGIN_ID;
	}

	/** The Constant TOOLCHAIN_ID. */
	public static final String TOOLCHAIN_ID = getIdPrefix() + ".toolchain"; //$NON-NLS-1$
	
	// ------------------------------------------------------------------------

	// The shared instance
	private static GccExtraPlugin instance;

	public static GccExtraPlugin getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new tool chain plugin
	 */
	public GccExtraPlugin() {
		super();		
	}

	// ------------------------------------------------------------------------

	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		instance=null;
		super.stop(context);	
	}

    @Override
    public boolean isDebugging() {
        return true;
    }

    public static void log(IStatus status) {
        getInstance().getLog().log(status);
    }

    public static void log(Throwable e) {
        log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1,
                ">>> GccExtraPlugin " + "Internal Error", e)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static void log(String message) {
        if(getInstance().isDebugging()) {
        log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1, ">>> GccExtraPlugin " + message, //$NON-NLS-1$
                null));
        }
    }
}
