/*******************************************************************************
 * Copyright (c) 2009, 2016 STMicroelectronics and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *   Ingenico  - Vincent Guignot <vincent.guignot@ingenico.com> - Add binutils strings
 *******************************************************************************/
package com.lembed.codeanalyzer.binutils.utils;

import java.io.IOException;

import org.eclipse.cdt.utils.Addr2line;
import org.eclipse.cdt.utils.CPPFilt;
import org.eclipse.cdt.utils.CommandLineUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;

import com.lembed.codeanalyzer.internal.BinUtilsPlugin;
import com.lembed.codeanalyzer.internal.binutils.preferences.BinutilsPreferencePage;
import com.lembed.codeanalyzer.tools.launch.core.factory.RuntimeProcessFactory;

/**
 * Default binutils factory.
 */
public class DefaultBinutilsFactory implements ISTBinutilsFactory {

    @Override
    public Addr2line getAddr2line(String path, IProject project) throws IOException {
        IPreferenceStore prefsStore = BinUtilsPlugin.getDefault().getPreferenceStore();
        String addr2lineCmd = prefsStore.getString(BinutilsPreferencePage.PREFKEY_ADDR2LINE_CMD);
        String addr2lineArgs = prefsStore.getString(BinutilsPreferencePage.PREFKEY_ADDR2LINE_ARGS);
        String[] args = CommandLineUtil.argumentsToArray(addr2lineArgs);
        String addr2line = RuntimeProcessFactory.getFactory().whichCommand(addr2lineCmd, project);
        return new Addr2line(addr2line, args, path);
    }

    @Override
    public CPPFilt getCPPFilt(IProject project) throws IOException {
        IPreferenceStore prefsStore = BinUtilsPlugin.getDefault().getPreferenceStore();
        String cppfiltCmd = prefsStore.getString(BinutilsPreferencePage.PREFKEY_CPPFILT_CMD);
        String cppfiltArgs = prefsStore.getString(BinutilsPreferencePage.PREFKEY_CPPFILT_ARGS);
        String[] args = CommandLineUtil.argumentsToArray(cppfiltArgs);
        String cppfilt = RuntimeProcessFactory.getFactory().whichCommand(cppfiltCmd, project);
        return new CPPFilt(cppfilt, args);
    }

    @Override
    public STNM getNM(String path, STNMSymbolsHandler handler, IProject project) throws IOException {
        IPreferenceStore prefsStore = BinUtilsPlugin.getDefault().getPreferenceStore();
        String nmCmd = prefsStore.getString(BinutilsPreferencePage.PREFKEY_NM_CMD);
        String nmArgs = prefsStore.getString(BinutilsPreferencePage.PREFKEY_NM_ARGS);
        String[] args = CommandLineUtil.argumentsToArray(nmArgs);
        String nm = RuntimeProcessFactory.getFactory().whichCommand(nmCmd, project);
        return new STNM(nm, args, path, handler, project);
    }

    /**
	 * @since 6.0
	 */
    @Override
    public STStrings getSTRINGS(IProject project) throws IOException {
        IPreferenceStore prefsStore = BinUtilsPlugin.getDefault().getPreferenceStore();
        String stringsCmd = prefsStore.getString(BinutilsPreferencePage.PREFKEY_STRINGS_CMD);
        String stringsArgs = prefsStore.getString(BinutilsPreferencePage.PREFKEY_STRINGS_ARGS);
        String[] args = CommandLineUtil.argumentsToArray(stringsArgs);
        String strings = RuntimeProcessFactory.getFactory().whichCommand(stringsCmd, project);
        return new STStrings(strings, args);
    }

    /**
     * No availability test for default binutils.
     */
    @Override
    public boolean testAvailability() {
        return true;
    }
    
}
