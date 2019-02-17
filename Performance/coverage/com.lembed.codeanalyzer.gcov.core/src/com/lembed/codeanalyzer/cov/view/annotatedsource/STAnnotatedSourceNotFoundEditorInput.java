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

import com.lembed.codeanalyzer.binutils.link2source.STCSourceNotFoundEditorInput;
import com.lembed.codeanalyzer.cov.parser.SourceFile;


public class STAnnotatedSourceNotFoundEditorInput extends STCSourceNotFoundEditorInput {

    private final SourceFile sourceFile;

    public STAnnotatedSourceNotFoundEditorInput(IProject project,
            SourceFile sourceFile,
            IPath sourcePath, int lineNumber) {
        super(project, sourcePath, lineNumber);
        this.sourceFile = sourceFile;
    }

    /**
     * @return the sourceFile
     */
    public SourceFile getSourceFile() {
        return sourceFile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((sourceFile == null) ? 0 : sourceFile.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final STAnnotatedSourceNotFoundEditorInput other = (STAnnotatedSourceNotFoundEditorInput) obj;
        if (sourceFile == null) {
            if (other.sourceFile != null) {
                return false;
            }
        } else if (!sourceFile.equals(other.sourceFile))
            return false;
        return true;
    }

}
