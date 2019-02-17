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

import java.util.LinkedList;

import com.lembed.codeanalyzer.internal.gprof.symbolManager.Bucket;


/**
 * Tree Item displaying a bucket.
 *
 * @author Xavier Raynaud <xavier.raynaud@st.com>
 */
public class HistBucket extends AbstractTreeElement {

    public final Bucket bucket;

    /**
     * Constructor
     * @param parent the parent of this tree node
     * @param b the object to display in the tree
     */
    public HistBucket(HistLine parent, Bucket b) {
        super(parent);
        this.bucket = b;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public LinkedList<? extends TreeElement> getChildren() {
        return null;
    }

    @Override
    public int getCalls() {
        return -1;
    }

    @Override
    public String getName() {
        return "0x" + Long.toHexString(bucket.startAddr); //$NON-NLS-1$
    }

    @Override
    public int getSamples() {
        return bucket.time;
    }

}
