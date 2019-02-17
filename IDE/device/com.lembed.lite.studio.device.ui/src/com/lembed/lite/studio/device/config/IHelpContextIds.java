/*******************************************************************************
* Copyright (c) 2016 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.config;

import com.lembed.lite.studio.device.ui.CpPlugInUI;

/**
 * Interface for help context IDs in this plug-in
 */
public interface IHelpContextIds {
	public static final String PREFIX = CpPlugInUI.PLUGIN_ID + "."; //$NON-NLS-1$

	public static final String CONFIG_WIZARD = PREFIX + "config_wizard"; //$NON-NLS-1$
}
