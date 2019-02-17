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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;
import com.lembed.lite.studio.manager.analysis.editor.linker.Activator;


/**
 * Preference page for the Yaml formatter
 */
public class FormatterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public FormatterPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Linker File Formatter Preferences");
    }

    @Override
    protected void createFieldEditors() {

        String[][] flowStyles = {{"Block", "BLOCK"}, {"Flow", "FLOW"} };
        addField(new ComboFieldEditor(PreferenceConstants.FORMATTER_FLOW_STYLE, "Flow style", flowStyles, getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.FORMATTER_PRETTY_FLOW, "Pretty print in when in Flow style", BooleanFieldEditor.SEPARATE_LABEL, getFieldEditorParent()));

        String[][] scalarStyles = {{"Plain", "PLAIN"}, {"Single quoted", "SINGLE_QUOTED"}, {"Double quoted", "DOUBLE_QUOTED"} };
        addField(new ComboFieldEditor(PreferenceConstants.FORMATTER_SCALAR_STYLE, "Scalar style", scalarStyles, getFieldEditorParent()));

        addField(new BooleanFieldEditor(PreferenceConstants.FORMATTER_EXPLICIT_START, "Explicit document start", BooleanFieldEditor.SEPARATE_LABEL, getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.FORMATTER_EXPLICIT_END, "Explicit document end", BooleanFieldEditor.SEPARATE_LABEL, getFieldEditorParent()));

        addField(new IntegerFieldEditor(PreferenceConstants.FORMATTER_LINE_WIDTH, "The line width to use", getFieldEditorParent(), 4));

    }

    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub

    }

}