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
package com.lembed.lite.studio.manager.analysis.editor.linker.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;
import com.lembed.lite.studio.manager.analysis.editor.linker.Activator;

import org.eclipse.ui.IWorkbench;

/**
 * The UI for setting the YEdit preferences
 * @author oysteto
 *
 */
public class LinkerFileEditorPreferences
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public LinkerFileEditorPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Linker File Preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {

		addField(new IntegerFieldEditor(PreferenceConstants.SPACES_PER_TAB, "Spaces per tab (Re-open editor to take effect.)", getFieldEditorParent(), 2 ) );
		addField(new IntegerFieldEditor(PreferenceConstants.SECONDS_TO_REEVALUATE, "Seconds between syntax reevaluation", getFieldEditorParent(), 3 ) );

		addField(new IntegerFieldEditor(PreferenceConstants.OUTLINE_SCALAR_MAX_LENGTH, "Maximum display length of scalar", getFieldEditorParent(), 4 ) );
		addField(new BooleanFieldEditor(PreferenceConstants.OUTLINE_SHOW_TAGS, "Show tags in outline view", getFieldEditorParent() ) );

		addField(new BooleanFieldEditor(PreferenceConstants.SYMFONY_COMPATIBILITY_MODE, "Symfony compatibility mode", getFieldEditorParent() ) );

		addField(new BooleanFieldEditor(PreferenceConstants.AUTO_EXPAND_OUTLINE, "Expand outline on open", getFieldEditorParent() ) );

		String[][] validationValues = new String[][] {
			{"Error", PreferenceConstants.SYNTAX_VALIDATION_ERROR},
			{"Warning", PreferenceConstants.SYNTAX_VALIDATION_WARNING},
			{"Ignore", PreferenceConstants.SYNTAX_VALIDATION_IGNORE}
		};
		addField(new ComboFieldEditor(PreferenceConstants.VALIDATION, "Syntax validation severity", validationValues, getFieldEditorParent()));

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

}
