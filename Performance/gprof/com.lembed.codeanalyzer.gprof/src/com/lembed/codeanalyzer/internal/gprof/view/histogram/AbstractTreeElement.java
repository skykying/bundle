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
package com.lembed.codeanalyzer.internal.gprof.view.histogram;


/**
 * Element of the hstogram
 *
 * @author Xavier Raynaud <xavier.raynaud@st.com>
 */
public abstract class AbstractTreeElement implements TreeElement {

    private final TreeElement parent;
    protected int samples = -1;
    protected int calls = -1;

    /**
     * Constructor
     * @param parent
     */
    public AbstractTreeElement(TreeElement parent) {
        this.parent = parent;
    }

    /**
     * Gets the parent of this tree node;
     * @return a tree node
     */
    @Override
    public TreeElement getParent() {
        return parent;
    }

    /**
     * Checks whether this tree node has children
     * @return <code>true</code> if this tree node has children,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public int getCalls() {
        if (calls == -1) {
            calls = 0;
            for (TreeElement elem : getChildren()) {
                int i = elem.getCalls();
                if (i != -1) {
                    calls += elem.getCalls();
                }
            }
        }
        return calls;
    }

    @Override
    public int getSamples() {
        if (samples == -1) {
            samples = 0;
            for (TreeElement elem : getChildren()) {
                samples += elem.getSamples();
            }
        }
        return samples;
    }

    @Override
    public int getSourceLine() {
        return 0;
    }

    @Override
    public String getSourcePath() {
        return null;
    }

    @Override
    public TreeElement getRoot() {
        if (parent == null) {
            return this;
        }
        return parent.getRoot();
    }
}
