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

package com.lembed.lite.studio.debug.gdbjtag.emulator;

import com.lembed.lite.studio.core.AbstractUIActivator;

import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EmulatorPlugin extends AbstractUIActivator {

	// ------------------------------------------------------------------------

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.debug.gdbjtag.emulator"; //$NON-NLS-1$

	@Override
	public String getBundleId() {
		return PLUGIN_ID;
	}

	
	public static String icon_refresh="dlcl16/restart_co.gif";
	
	public static final String ICONS_PATH = "icons/"; //$NON-NLS-1$

	public static final String ICON_FILE = "dlcl16/restart_co.gif"; //$NON-NLS-1$
	
	private static HashMap<String, Image> images = new HashMap<String, Image>();
	private static HashMap<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>();
	
	// ------------------------------------------------------------------------

	// The shared instance
	private static EmulatorPlugin fgInstance;


	public static EmulatorPlugin getInstance() {
		return fgInstance;
	}

	public EmulatorPlugin() {

		super();
		fgInstance = this;
	}

	// ------------------------------------------------------------------------

	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}



	// ------------------------------------------------------------------------
}
