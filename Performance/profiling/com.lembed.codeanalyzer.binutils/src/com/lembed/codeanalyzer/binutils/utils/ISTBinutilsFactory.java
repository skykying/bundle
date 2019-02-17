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
import org.eclipse.core.resources.IProject;

/**
 * Binutils factory (especially used for cross-compile tools)
 *
 */
public interface ISTBinutilsFactory {

    Addr2line getAddr2line(String path, IProject project) throws IOException;

    CPPFilt getCPPFilt(IProject project) throws IOException;

    STNM getNM(String path, STNMSymbolsHandler handler, IProject project) throws IOException;

    /**
	 * @param project
     * @return an instance of strings for the given program
     * @throws IOException
     * @since 6.0
	 */
    STStrings getSTRINGS(IProject project) throws IOException;

    boolean testAvailability();
}
