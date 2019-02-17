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

import java.util.Map;

import com.lembed.lite.studio.debug.core.data.ICProjectExtraDataManager;
import com.lembed.lite.studio.debug.core.data.ICProjectExtraDataManagerFactory;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;

/**
 * @author Lembed
 *
 */
public class LiteProjectExtraDataManagerProxy implements ICProjectExtraDataManager {


	private static final String FACTORY_ELEMENT = "factory"; //$NON-NLS-1$
	private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	private static LiteProjectExtraDataManagerProxy fgInstance;

	/**
	 * get LiteProjectExtraDataManagerProxy Instance
	 * @return LiteProjectExtraDataManagerProxy
	 */
	public static LiteProjectExtraDataManagerProxy getInstance() {

		if (fgInstance == null) {
			fgInstance = new LiteProjectExtraDataManagerProxy();
		}
		return fgInstance;
	}

	private ICProjectExtraDataManager fDataManagers[];

	/**
	 * LiteProjectExtraDataManagerProxy
	 */
	public LiteProjectExtraDataManagerProxy() {

		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_POINT_ID).getExtensions();

		if (extensions.length == 0) {
			GdbActivator.log("no cprojectExtra extension point"); //$NON-NLS-1$
			return;
		}

		fDataManagers = new ICProjectExtraDataManager[extensions.length];

		for (int i = 0; i < extensions.length; ++i) {

			fDataManagers[i] = null;

			IExtension extension = extensions[i];
			IConfigurationElement[] configElements = extension.getConfigurationElements();
			IConfigurationElement configElement = configElements[0];

			if (FACTORY_ELEMENT.equals(configElement.getName())) {

				ICProjectExtraDataManagerFactory factory;
				try {
					Object obj = configElement.createExecutableExtension(CLASS_ATTRIBUTE);

					if (obj instanceof ICProjectExtraDataManagerFactory) {
						factory = (ICProjectExtraDataManagerFactory) obj;

						// Create the extension point data manager.
						fDataManagers[i] = factory.create();
					} else {
						GdbActivator.log("no ICProjectExtraDataManagerFactory"); //$NON-NLS-1$
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
	public Map<String, String> getExtraProperties(IConfiguration config) {

		if (fDataManagers == null) {
			return null;
		}

		// Iterate all managers and return the first value available.
		for (int i = 0; i < fDataManagers.length; ++i) {
			Map<String, String> map = null;
			if (fDataManagers[i] != null) {
				map = fDataManagers[i].getExtraProperties(config);
				if (map != null) {
					return map;
				}
			}
		}

		return null;
	}
}
