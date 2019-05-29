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

package com.lembed.lite.studio.debug.gdbjtag.device;

import com.lembed.lite.studio.core.AbstractUIActivator;

import java.util.HashMap;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DevicePlugin extends AbstractUIPlugin {

	// ------------------------------------------------------------------------

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.debug.gdbjtag.device"; //$NON-NLS-1$

	public String getBundleId() {
		return PLUGIN_ID;
	}

	
	public static final String ICONS_PATH = "icons/"; //$NON-NLS-1$
	private static HashMap<String, Image> images = new HashMap<String, Image>();
	private static HashMap<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>();

	// The shared instance
	private static DevicePlugin fgInstance;
	public static String icon_refresh="dlcl16/restart_co.gif";

	public static DevicePlugin getInstance() {
		return fgInstance;
	}

	public DevicePlugin() {

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

	public static void log(IStatus status) {
		getInstance().getLog().log(status);
	}

	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1,
				">>> PLUGIN_ID " + "Internal Error", e)); //$NON-NLS-1$
	}

	public static void log(String message) {
		log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1, ">>> PLUGIN_ID " + message,
				null)); //$NON-NLS-1$
	}
	
	
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String file) {
		String icons_file = null;
		if (!file.startsWith(ICONS_PATH)) {
			icons_file = ICONS_PATH + file;
		} else {
			icons_file = file;
		}

		if (imageDescriptors.containsKey(icons_file)) {
			return imageDescriptors.get(icons_file);
		}

		ImageDescriptor imageDescr = imageDescriptorFromPlugin(PLUGIN_ID, icons_file);
		if (imageDescr != null) {
			imageDescriptors.put(icons_file, imageDescr);
		}

		return imageDescr;
	}
}
