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
package com.lembed.lite.studio.debug.core.data;

import java.util.Map;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;

/**
 * The interface of the manager used to handle C project extra data.
 */
public interface ICProjectExtraDataManager {


	public static final String EXTENSION_POINT_NAME = "cprojectExtra";
	public static final String EXTENSION_POINT_ID = 
			com.lembed.lite.studio.debug.core.Activator.PLUGIN_ID + "." + EXTENSION_POINT_NAME;


	/**
	 * Get a map of additional properties associated to a configuration.
	 * <p>
	 * For the IDE MBS plug-in, these properties are assigned by the
	 * Devices tab, contributed by the packs feature.
	 * <p>
	 * For projects managed by other plug-ins, this should return null.
	 * 
	 * @param config a C/C++ configuration.
	 * @return the map of property/value pairs, or null.
	 */
	public Map<String, String> getExtraProperties(IConfiguration config);

}
