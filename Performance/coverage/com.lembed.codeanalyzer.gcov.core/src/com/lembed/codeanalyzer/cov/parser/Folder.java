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
package com.lembed.codeanalyzer.cov.parser;

import java.io.Serializable;
import java.util.ArrayList;

public class Folder implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5155033391199109661L;
    private final String path;
    private final ArrayList<SourceFile> srcFiles = new ArrayList<>();
    private int numLines = 0;
    private int linesInstrumented = 0;
    private int linesExecuted = 0;

    /**
     * Constructor
     */
    public Folder(String path) {
        this.path = path;
    }


    public void accumulateSourcesCounts(){
        for (SourceFile srcFile: srcFiles) {
            numLines += srcFile.getNumLines();
            linesInstrumented += srcFile.getLinesInstrumented();
            linesExecuted += srcFile.getLinesExecuted();
        }
    }

    public String getPath() {
        return path;
    }

    public ArrayList<SourceFile> getSrcFiles() {
        return srcFiles;
    }

    public void addSrcFiles(SourceFile srcFile) {
        this.srcFiles.add(srcFile);
    }

    public int getNumLines() {
        return numLines;
    }
    public int getLinesExecuted() {
        return linesExecuted;
    }
    public int getLinesInstrumented() {
        return linesInstrumented;
    }
}
