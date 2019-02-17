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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "com.lembed.codeanalyzer.internal.binutils.preferences.messages"; //$NON-NLS-1$
    public static String BinutilsPreferencePage_addr2line;
    public static String BinutilsPreferencePage_addr2line_flags;
    public static String BinutilsPreferencePage_cppfilt;
    public static String BinutilsPreferencePage_cppfilt_flags;
    public static String BinutilsPreferencePage_description;
    public static String BinutilsPreferencePage_nm;
    public static String BinutilsPreferencePage_nm_flags;
    public static String BinutilsPreferencePage_title;
    public static String BinutilsPreferencePage_strings;
    public static String BinutilsPreferencePage_strings_flags;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
