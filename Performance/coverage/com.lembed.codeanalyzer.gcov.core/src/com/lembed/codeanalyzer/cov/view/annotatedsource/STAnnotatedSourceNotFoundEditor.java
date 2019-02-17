/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *******************************************************************************/

package com.lembed.codeanalyzer.cov.view.annotatedsource;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;

import com.lembed.codeanalyzer.binutils.link2source.STCSourceNotFoundEditor;
import com.lembed.codeanalyzer.cov.parser.SourceFile;

/**
 * @author Xavier Raynaud <xavier.raynaud@st.com>
 */
public class STAnnotatedSourceNotFoundEditor extends STCSourceNotFoundEditor {

    public static final String ID = "com.lembed.codeanalyzer.gcov.view.annotatedsource.STAnnotatedSourceNotFoundEditor"; //$NON-NLS-1$

    @Override
    protected void openSourceFileAtLocation(IProject project, IPath sourceLoc, int lineNumber) {
        IEditorInput input = this.getEditorInput();
        if (input instanceof STAnnotatedSourceNotFoundEditorInput) {
            STAnnotatedSourceNotFoundEditorInput editorInput = (STAnnotatedSourceNotFoundEditorInput) input;
            SourceFile sf = editorInput.getSourceFile();
            OpenSourceFileAction.openAnnotatedSourceFile(project, null, sf, sourceLoc, lineNumber);
        } else {
            super.openSourceFileAtLocation(project, sourceLoc, lineNumber);
        }
    }
}
