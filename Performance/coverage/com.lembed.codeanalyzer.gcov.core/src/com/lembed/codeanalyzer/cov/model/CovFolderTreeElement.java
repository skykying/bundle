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
package com.lembed.codeanalyzer.cov.model;

public class CovFolderTreeElement extends AbstractTreeElement {

    /**
     *
     */
    private static final long serialVersionUID = 583359546284716138L;

    public CovFolderTreeElement(TreeElement parent,
                    String name, int totalLines, int executedLines, int instrumentedLines) {
        super(parent, name, totalLines, executedLines, instrumentedLines);
    }
}
