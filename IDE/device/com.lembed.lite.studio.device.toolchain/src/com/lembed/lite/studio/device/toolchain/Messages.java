/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.toolchain;

import org.eclipse.osgi.util.NLS;

/**
 * @author edriouk
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName(); //$NON-NLS-1$

	public static String LiteTemplateMemory_ScriptTemplate;
	public static String LiteTemplateMemory_ScriptTemplate_common; 
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
