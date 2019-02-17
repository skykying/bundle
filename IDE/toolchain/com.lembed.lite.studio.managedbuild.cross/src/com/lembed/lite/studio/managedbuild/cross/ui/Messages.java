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
package com.lembed.lite.studio.managedbuild.cross.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;
import com.lembed.lite.studio.managedbuild.cross.CrossGccPlugin;

@SuppressWarnings("javadoc")
public class Messages extends NLS {

	// ------------------------------------------------------------------------

	private static final String MESSAGES = CrossGccPlugin.PLUGIN_ID + ".ui.messages"; //$NON-NLS-1$

    public static String SetCrossCommandWizardPage_browse;
	public static String SetCrossCommandWizardPage_description;
	public static String SetCrossCommandWizardPage_name;
	public static String SetCrossCommandWizardPage_toolchain;
	public static String SetCrossCommandWizardPage_path;
	public static String SetCrossCommandWizardPage_prefix;
	public static String SetCrossCommandWizardPage_title;

	public static String ToolChainSettingsTab_name;
	public static String ToolChainSettingsTab_architecture;
	public static String ToolChainSettingsTab_prefix;
	public static String ToolChainSettingsTab_suffix;
	public static String ToolChainSettingsTab_cCmd;
	public static String ToolChainSettingsTab_cppCmd;
	public static String ToolChainSettingsTab_arCmd;
	public static String ToolChainSettingsTab_objcopyCmd;
	public static String ToolChainSettingsTab_objdumpCmd;
	public static String ToolChainSettingsTab_sizeCmd;
	public static String ToolChainSettingsTab_makeCmd;
	public static String ToolChainSettingsTab_rmCmd;

	public static String ToolChainSettingsTab_flash;
	public static String ToolChainSettingsTab_listing;
	public static String ToolChainSettingsTab_size;

	public static String ToolChainSettingsTab_path_label;
	public static String ToolChainSettingsTab_path_link;
	public static String ToolChainSettingsTab_warning_link;

	public static String ToolsPaths_label;
	public static String ToolsPaths_ToolchainName_label;

	public static String ToolchainPaths_label;
	public static String ToolchainName_label;

	public static String ProjectToolsPathsPropertyPage_description;
	public static String WorkspaceToolsPathsPropertyPage_description;
	public static String GlobalToolsPathsPropertyPage_description;

	// ------------------------------------------------------------------------

	static {
		// Initialize resource bundle
		NLS.initializeMessages(MESSAGES, Messages.class);
	}

	private static ResourceBundle RESOURCE_BUNDLE;
	static {
		try {
			RESOURCE_BUNDLE = ResourceBundle.getBundle(MESSAGES);
		} catch (MissingResourceException e) {
			CrossGccPlugin.log(e);
		}
	}

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
