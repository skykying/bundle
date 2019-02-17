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
package com.lembed.lite.studio.manager.analysis.editor.yaml;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class YEditMessages {

	private static final String RESOURCE_BUNDLE = "com.lembed.lite.studio.managedbuild.cross.editor.yaml.YEditMessages";//$NON-NLS-1$

	private static ResourceBundle fgResourceBundle = ResourceBundle
			.getBundle(RESOURCE_BUNDLE);


	public static String getString(String key) {
		try {
			return fgResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}

	public static ResourceBundle getResourceBundle() {
		return fgResourceBundle;
	}
}
