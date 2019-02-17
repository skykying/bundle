/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Lembed Electronic - for LiteSTUDIO
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.data;

import com.lembed.lite.studio.debug.core.data.ISVDPathManager;
import com.lembed.lite.studio.debug.core.data.ISVDPathManagerFactory;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.data.ICpDebugConfiguration;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.item.CmsisMapItem;
import com.lembed.lite.studio.device.item.ICmsisMapItem;


/**
 * The Class SVDPathManagerProxy.
 */
public class SVDPathManagerProxy implements ISVDPathManager {

	// ------------------------------------------------------------------------

	/** The Constant FACTORY_ELEMENT. */
	private static final String FACTORY_ELEMENT = "factory"; //$NON-NLS-1$
	
	/** The Constant CLASS_ATTRIBUTE. */
	private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/** The fg instance. */
	private static SVDPathManagerProxy fgInstance;

	/**
	 * @return SVDPathManagerProxy
	 */
	public static SVDPathManagerProxy getInstance() {

		if (fgInstance == null) {
			fgInstance = new SVDPathManagerProxy();
		}
		return fgInstance;
	}

	// ------------------------------------------------------------------------

	/** The path managers. */
	private ISVDPathManager fPathManagers[];

	/**
	 * SVDPathManagerProxy construct
	 */
	public SVDPathManagerProxy() {

		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT_ID).getExtensions();

		if (extensions.length == 0) {
			// GdbActivator.log("no svdPath xp");
			return;
		}

		fPathManagers = new ISVDPathManager[extensions.length];

		for (int i = 0; i < extensions.length; ++i) {

			fPathManagers[i] = null;

			IExtension extension = extensions[i];
			IConfigurationElement[] configElements = extension.getConfigurationElements();
			IConfigurationElement configElement = configElements[0];

			if (FACTORY_ELEMENT.equals(configElement.getName())) {

				ISVDPathManagerFactory factory;
				try {
					Object obj = configElement.createExecutableExtension(CLASS_ATTRIBUTE);

					if (obj instanceof ISVDPathManagerFactory) {
						factory = (ISVDPathManagerFactory) obj;

						// Create the extension point data manager.
						fPathManagers[i] = factory.create();
					} else {
						GdbActivator.log("no ISVDPathManagerFactory"); //$NON-NLS-1$
					}
				} catch (CoreException e) {
					GdbActivator.log("cannot get factory for " + EXTENSION_POINT_ID); //$NON-NLS-1$
				}
			} else {
				GdbActivator.log("no <factory> element"); //$NON-NLS-1$
			}

		}
	}


	@Override
	public IPath getSVDAbsolutePathById(String deviceVendorId, String deviceName) {

		if (fPathManagers == null) {
			return null;
		}

		CpPlugIn.getPackManager().getCmsisPackRootDirectory();
		ICpPackManager manager = CpPlugIn.getPackManager();
		ILiteDeviceItem devices = manager.getDevices();
		ICmsisMapItem<ILiteDeviceItem> root = new CmsisMapItem<>();
		root.addChild(devices);
		Collection<? extends ILiteDeviceItem> children = root.getChildren();
		for (ILiteDeviceItem device : children) {
			device.getName();
			device.getVendorName();
			String name = device.getDevice().getProcessorName();
			ICpDebugConfiguration debugConf = device.getDevice().getDebugConfiguration(name);
			debugConf.getSvdFile();
		}

		// Iterate all managers and return the first value available.
		for (int i = 0; i < fPathManagers.length; ++i) {
			IPath path = null;
			if (fPathManagers[i] != null) {
				path = fPathManagers[i].getSVDAbsolutePathById(deviceVendorId, deviceName);
				if (path != null) {
					return path;
				}
			}
		}

		return null;
	}

}
