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
package com.lembed.lite.studio.managedbuild.cross.properties;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.ScopedPreferenceStoreWithoutDefaults;
import com.lembed.lite.studio.core.preferences.LabelFakeFieldEditor;
import com.lembed.lite.studio.core.ui.FieldEditorPropertyPage;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import com.lembed.lite.studio.managedbuild.cross.CrossGccPlugin;
import com.lembed.lite.studio.managedbuild.cross.Option;
import com.lembed.lite.studio.managedbuild.cross.ui.Messages;
import com.lembed.lite.studio.managedbuild.cross.ui.PersistentPreferences;

/**
 * The Class ProjectToolsPathPropertyPage.
 */
public class ProjectToolsPathPropertyPage extends FieldEditorPropertyPage {

	// ------------------------------------------------------------------------

	/** The Constant ID. */
	public static final String ID = "com.lembed.lite.studio.managedbuild.cross.properties.toolsPage"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new project tools path property page.
	 */
	public ProjectToolsPathPropertyPage() {
		super(GRID);

		setDescription(Messages.ProjectToolsPathsPropertyPage_description);
	}

	// ------------------------------------------------------------------------

	@Override
    protected IPreferenceStore doGetPreferenceStore() {

		Object element = getElement();
		if (element instanceof IProject) {
			return new ScopedPreferenceStoreWithoutDefaults(new ProjectScope((IProject) element), CrossGccPlugin.PLUGIN_ID);
		}
		return null;
	}

	@Override
	protected void createFieldEditors() {

		FieldEditor buildToolsPathField = new DirectoryFieldEditor(PersistentPreferences.BUILD_TOOLS_PATH_KEY,
				Messages.ToolsPaths_label, getFieldEditorParent());
		addField(buildToolsPathField);

		Set<String> toolchainNames = new HashSet<>();

		Object element = getElement();
		if (element instanceof IProject) {
			// TODO: get project toolchain name. How?
			IProject project = (IProject) element;
			IConfiguration[] configs = EclipseUtils.getConfigurationsForProject(project);
			if (configs != null) {
				for (int i = 0; i < configs.length; ++i) {
					IToolChain toolchain = configs[i].getToolChain();
					if (toolchain == null) {
						continue;
					}
					IOption option = toolchain.getOptionBySuperClassId(Option.OPTION_TOOLCHAIN_NAME);
					if (option == null) {
						continue;
					}
					try {
						String name = option.getStringValue();
						if (!name.isEmpty()) {
							toolchainNames.add(name);
						}
					} catch (BuildException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (toolchainNames.isEmpty()) {
			toolchainNames.add(PersistentPreferences.getToolchainName());
		}

		for (String toolchainName : toolchainNames) {

			FieldEditor labelField = new LabelFakeFieldEditor(toolchainName, Messages.ToolsPaths_ToolchainName_label,
					getFieldEditorParent());
			addField(labelField);

			String key = PersistentPreferences.getToolchainKey(toolchainName);
			FieldEditor toolchainPathField = new DirectoryFieldEditor(key, Messages.ToolchainPaths_label,
					getFieldEditorParent());
			addField(toolchainPathField);
		}
	}

	// ------------------------------------------------------------------------
}
