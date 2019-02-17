/*******************************************************************************
 * Copyright (c) 2013, 2016 Kalray.eu and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Xavier Raynaud <xavier.raynaud@kalray.eu> - initial API and implementation
 *    Ingenico - Vincent Guignot <vincent.guignot@ingenico.com> - Add binutils strings    
 *******************************************************************************/
package com.lembed.codeanalyzer.internal.binutils.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.lembed.codeanalyzer.binutils.utils.STSymbolManager;
import com.lembed.codeanalyzer.internal.BinUtilsPlugin;

/**
 * @author Xavier Raynaud <xavier.raynaud@kalray.eu>
 */
public class BinutilsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String PREFKEY_ADDR2LINE_CMD = "PREFKEY_ADDR2LINE_CMD"; //$NON-NLS-1$
    public static final String PREFKEY_ADDR2LINE_ARGS = "PREFKEY_ADDR2LINE_ARGS"; //$NON-NLS-1$

    public static final String PREFKEY_CPPFILT_CMD = "PREFKEY_CPPFILT_CMD"; //$NON-NLS-1$
    public static final String PREFKEY_CPPFILT_ARGS = "PREFKEY_CPPFILT_ARGS"; //$NON-NLS-1$

    public static final String PREFKEY_NM_CMD = "PREFKEY_NM_CMD"; //$NON-NLS-1$
    public static final String PREFKEY_NM_ARGS = "PREFKEY_NM_ARGS"; //$NON-NLS-1$

    public static final String PREFKEY_STRINGS_CMD = "PREFKEY_STRINGS_CMD"; //$NON-NLS-1$
    public static final String PREFKEY_STRINGS_ARGS = "PREFKEY_STRINGS_ARGS"; //$NON-NLS-1$

    public BinutilsPreferencePage() {
        super(Messages.BinutilsPreferencePage_title, FieldEditorPreferencePage.GRID);
        this.setPreferenceStore(BinUtilsPlugin.getDefault().getPreferenceStore());
        this.setDescription(Messages.BinutilsPreferencePage_description);
    }

    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    protected void createFieldEditors() {
        StringFieldEditor faddr2line = new StringFieldEditor(PREFKEY_ADDR2LINE_CMD, Messages.BinutilsPreferencePage_addr2line, this.getFieldEditorParent());
        this.addField(faddr2line);
        StringFieldEditor faddr2lineArgs = new StringFieldEditor(PREFKEY_ADDR2LINE_ARGS, Messages.BinutilsPreferencePage_addr2line_flags, this.getFieldEditorParent());
        this.addField(faddr2lineArgs);

        StringFieldEditor fcppfilt = new StringFieldEditor(PREFKEY_CPPFILT_CMD, Messages.BinutilsPreferencePage_cppfilt, this.getFieldEditorParent());
        this.addField(fcppfilt);
        StringFieldEditor fcppfiltArgs = new StringFieldEditor(PREFKEY_CPPFILT_ARGS, Messages.BinutilsPreferencePage_cppfilt_flags, this.getFieldEditorParent());
        this.addField(fcppfiltArgs);

        StringFieldEditor fnm = new StringFieldEditor(PREFKEY_NM_CMD, Messages.BinutilsPreferencePage_nm, this.getFieldEditorParent());
        this.addField(fnm);
        StringFieldEditor fnmArgs = new StringFieldEditor(PREFKEY_NM_ARGS, Messages.BinutilsPreferencePage_nm_flags, this.getFieldEditorParent());
        this.addField(fnmArgs);

        StringFieldEditor fstrings = new StringFieldEditor(PREFKEY_STRINGS_CMD, Messages.BinutilsPreferencePage_strings, this.getFieldEditorParent());
        this.addField(fstrings);
        StringFieldEditor fstringsArgs = new StringFieldEditor(PREFKEY_STRINGS_ARGS, Messages.BinutilsPreferencePage_strings_flags, this.getFieldEditorParent());
        this.addField(fstringsArgs);
    }

    @Override
    public boolean performOk() {
        STSymbolManager.sharedInstance.reset();
        return super.performOk();
    }

}
