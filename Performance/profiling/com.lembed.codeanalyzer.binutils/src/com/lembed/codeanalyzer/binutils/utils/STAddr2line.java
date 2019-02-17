/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.binutils.utils;

import java.io.IOException;

import org.eclipse.cdt.core.IAddress;
import org.eclipse.cdt.utils.Addr2line;

/**
 * Addr2line implementation, compatible with "hostname:file:linenumber" format of addr2line output.
 */
public class STAddr2line extends Addr2line {

    /**
     * Constructor
     *
     * @param cmd The command to run.
     * @param file The binary file.
     * @throws IOException If any IOException happened.
     */
    public STAddr2line(String cmd, String file) throws IOException {
        super(cmd, file);
    }

    @Override
    public String getLine(IAddress address) throws IOException {
        String s = super.getLine(address);
        int index1 = s.lastIndexOf(':');
        int index2 = s.indexOf(':');
        if (index1 != index2 && index2 != 2) {
            s = s.substring(index2 + 1);
        }
        return s;
    }

}
