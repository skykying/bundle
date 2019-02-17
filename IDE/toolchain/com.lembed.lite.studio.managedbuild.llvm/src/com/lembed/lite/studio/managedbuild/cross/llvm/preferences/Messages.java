/*******************************************************************************
 * Copyright (c) 2010-2013 Nokia Siemens Networks Oyj, Finland.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Nokia Siemens Networks - initial implementation
 *      Petri Tuononen - Initial implementation
 *******************************************************************************/
package com.lembed.lite.studio.managedbuild.cross.llvm.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = Messages.class.getName(); //$NON-NLS-1$
	public static String IncludePathListEditor_0;
	public static String IncludePathListEditor_1;
	public static String LibraryListEditor_0;
	public static String LibraryPathListEditor_0;
	public static String LibraryPathListEditor_1;
	public static String LlvmPreferencePage_0;
	public static String LlvmPreferencePage_1;
	public static String LlvmPreferencePage_2;
	public static String LlvmPreferencePage_3;
	public static String LlvmPreferencePage_4;
	
    public static String ToolChainSettingsTab_name;
    public static String ToolChainSettingsTab_architecture;
    public static String ToolChainSettingsTab_prefix;
    public static String ToolChainSettingsTab_cCmd;
    public static String ToolChainSettingsTab_cppCmd;
    public static String ToolChainSettingsTab_suffix;
    public static String ToolChainSettingsTab_arCmd;
    public static String ToolChainSettingsTab_objcopyCmd;
    public static String ToolChainSettingsTab_objdumpCmd;
    public static String ToolChainSettingsTab_sizeCmd;
    public static String ToolChainSettingsTab_makeCmd;
    public static String ToolChainSettingsTab_rmCmd;
    public static String ToolChainSettingsTab_warning_link;
    public static String ToolChainSettingsTab_flash;
    public static String ToolChainSettingsTab_size;
    public static String ToolChainSettingsTab_listing;
    public static String ToolChainSettingsTab_path_label;
    public static String GlobalToolsPathsPropertyPage_description;
    public static String ToolsPaths_label;
    public static String ToolchainName_label;
    public static String ToolsPaths_ToolchainName_label;
    public static String ToolchainPaths_label;
    public static String WorkspaceToolsPathsPropertyPage_description;
    
    public static String USE_BUILD_IN_TOOLCHAIN_LABEL;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
