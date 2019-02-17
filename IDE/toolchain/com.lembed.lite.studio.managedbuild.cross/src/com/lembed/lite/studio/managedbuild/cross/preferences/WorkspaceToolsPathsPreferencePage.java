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
package com.lembed.lite.studio.managedbuild.cross.preferences;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.ScopedPreferenceStoreWithoutDefaults;
import com.lembed.lite.studio.core.preferences.DirectoryNotStrictFieldEditor;
import com.lembed.lite.studio.core.preferences.LabelFakeFieldEditor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.lembed.lite.studio.managedbuild.cross.CrossGccPlugin;
import com.lembed.lite.studio.managedbuild.cross.Option;
import com.lembed.lite.studio.managedbuild.cross.ui.Messages;
import com.lembed.lite.studio.managedbuild.cross.ui.PersistentPreferences;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */
public class WorkspaceToolsPathsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	// ------------------------------------------------------------------------

	/** The Constant ID. */
	public static final String ID = "com.lembed.lite.studio.managedbuild.cross.preferencePage.workspaceToolsPaths"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new workspace tools paths preference page.
	 */
	public WorkspaceToolsPathsPreferencePage() {
		super(GRID);

		setPreferenceStore(new ScopedPreferenceStoreWithoutDefaults(InstanceScope.INSTANCE, CrossGccPlugin.PLUGIN_ID));

		setDescription(Messages.WorkspaceToolsPathsPropertyPage_description);
	}

	// ------------------------------------------------------------------------

	// Contributed by IWorkbenchPreferencePage
	@Override
	public void init(IWorkbench workbench) {
		if (CrossGccPlugin.getInstance().isDebugging()) {
			System.out.println("WorkspaceToolsPathsPage.init()"); //$NON-NLS-1$
		}
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	protected void createFieldEditors() {

		boolean isStrict;
		isStrict = DefaultPreferences.getBoolean(PersistentPreferences.WORKSPACE_BUILDTOOLS_PATH_STRICT, true);
		FieldEditor buildToolsPathField;
		buildToolsPathField = new DirectoryNotStrictFieldEditor(PersistentPreferences.BUILD_TOOLS_PATH_KEY,
				Messages.ToolsPaths_label, getFieldEditorParent(), isStrict);

		addField(buildToolsPathField);

		FieldEditor toolchainNameField = new ToolchainsFieldEditor(PersistentPreferences.TOOLCHAIN_NAME_KEY,
				Messages.ToolchainName_label, getFieldEditorParent());
		addField(toolchainNameField);

		Set<String> toolchainNames = new HashSet<>();

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i = 0; i < projects.length; ++i) {
			IConfiguration[] configs = EclipseUtils.getConfigurationsForProject(projects[i]);

			if (configs != null) {
				for (int j = 0; j < configs.length; ++j) {
					IToolChain toolchain = configs[j].getToolChain();
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

			isStrict = DefaultPreferences.getBoolean(PersistentPreferences.WORKSPACE_TOOLCHAIN_PATH_STRICT, true);

			FieldEditor toolchainPathField;
			toolchainPathField = new DirectoryNotStrictFieldEditor(key, Messages.ToolchainPaths_label,
					getFieldEditorParent(), isStrict);

			addField(toolchainPathField);
		}
	}

	// ------------------------------------------------------------------------
}
