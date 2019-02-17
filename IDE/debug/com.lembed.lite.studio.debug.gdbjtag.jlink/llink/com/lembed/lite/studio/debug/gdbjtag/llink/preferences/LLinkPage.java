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
package com.lembed.lite.studio.debug.gdbjtag.llink.preferences;

import com.lembed.lite.studio.core.preferences.DirectoryNotStrictVariableFieldEditor;
import com.lembed.lite.studio.core.preferences.StringVariableFieldEditor;
import com.lembed.lite.studio.debug.gdbjtag.llink.DefaultPreferences;
import com.lembed.lite.studio.debug.gdbjtag.llink.PersistentPreferences;
import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;
import com.lembed.lite.studio.debug.gdbjtag.llink.VariableInitializer;
import com.lembed.lite.studio.debug.gdbjtag.llink.ui.Messages;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page uses special filed editors, that get the default values from the
 * preferences store, but the values are from the variables store.
 */
public class LLinkPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	// ------------------------------------------------------------------------

	/** The Constant ID. */
	public static final String ID = "com.lembed.lite.studio.debug.gdbjtag.llink.preferencePage"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new l link page.
	 */
	public LLinkPage() {
		super(GRID);

		// Not really used, the field editors directly access the variables
		// store.
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, LlinkPlugin.PLUGIN_ID));
		setDescription(Messages.L_LinkPreferencePage_description);
	}

	// ------------------------------------------------------------------------

	// Contributed by IWorkbenchPreferencePage
	@Override
	public void init(IWorkbench workbench) {
		
		LlinkPlugin.log("STlinkPage.init()"); //$NON-NLS-1$
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	protected void createFieldEditors() {

		FieldEditor executable;
		executable = new StringVariableFieldEditor(PersistentPreferences.EXECUTABLE_NAME,
				VariableInitializer.VARIABLE_L_LINK_EXECUTABLE, Messages.Variable_executable_description,
				Messages.L_LinkPreferencePage_executable_label, getFieldEditorParent());
		addField(executable);

		boolean isStrict;
		isStrict = DefaultPreferences.getBoolean(PersistentPreferences.FOLDER_STRICT, true);

		FieldEditor folder;
		folder = new DirectoryNotStrictVariableFieldEditor(PersistentPreferences.INSTALL_FOLDER,
				VariableInitializer.VARIABLE_L_LINK_PATH, Messages.Variable_path_description,
				Messages.L_LinkPropertyPage_executable_folder, getFieldEditorParent(), isStrict);
		addField(folder);
	}

	// ------------------------------------------------------------------------
}
