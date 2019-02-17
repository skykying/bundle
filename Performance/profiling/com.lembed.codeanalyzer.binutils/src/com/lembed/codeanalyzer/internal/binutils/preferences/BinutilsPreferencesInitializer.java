/*******************************************************************************
 * Copyright (c) 2013, 2016 Kalray.eu and others
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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.lembed.codeanalyzer.internal.BinUtilsPlugin;

/**
 * @author Xavier Raynaud <xavier.raynaud@kalray.eu>
 */
public class BinutilsPreferencesInitializer extends AbstractPreferenceInitializer {

    private static final String CPPFILT_CMD = "c++filt"; //$NON-NLS-1$
    private static final String ADDR2LINE_CMD = "addr2line"; //$NON-NLS-1$
    private static final String NM_CMD = "nm"; //$NON-NLS-1$
    private static final String STRINGS_CMD = "strings"; //$NON-NLS-1$

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = BinUtilsPlugin.getDefault().getPreferenceStore();
        store.setDefault(BinutilsPreferencePage.PREFKEY_ADDR2LINE_CMD, ADDR2LINE_CMD);
        store.setDefault(BinutilsPreferencePage.PREFKEY_ADDR2LINE_ARGS, ""); //$NON-NLS-1$
        store.setDefault(BinutilsPreferencePage.PREFKEY_CPPFILT_CMD, CPPFILT_CMD);
        store.setDefault(BinutilsPreferencePage.PREFKEY_CPPFILT_ARGS, ""); //$NON-NLS-1$
        store.setDefault(BinutilsPreferencePage.PREFKEY_NM_CMD, NM_CMD);
        store.setDefault(BinutilsPreferencePage.PREFKEY_NM_ARGS, ""); //$NON-NLS-1$
        store.setDefault(BinutilsPreferencePage.PREFKEY_STRINGS_CMD, STRINGS_CMD);
        store.setDefault(BinutilsPreferencePage.PREFKEY_STRINGS_ARGS, ""); //$NON-NLS-1$
    }

}
