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

import org.eclipse.cdt.core.IBinaryParser.IBinaryObject;
import org.eclipse.cdt.core.IBinaryParser.ISymbol;
import org.eclipse.core.runtime.Path;

import com.lembed.codeanalyzer.internal.gprof.symbolManager.Bucket;
import com.lembed.codeanalyzer.internal.gprof.symbolManager.CallGraphNode;


/**
 * Tree node corresponding to a file
 *
 * @author Xavier Raynaud <xavier.raynaud@st.com>
 */
public class HistFile extends AbstractTreeElement {

    /** The source path to display */
    public final String sourcePath;
    private final LinkedList<HistFunction> children = new LinkedList<>();

    /**
     * Constructor
     * @param parent
     * @param path
     */
    public HistFile(HistRoot parent, String path) {
        super(parent);
        this.sourcePath = path;
    }

    /**
     * Gets the tree item corresponding to the given function.
     * Lazily create it if needed.
     * @param s
     * @return a {@link HistFunction}
     */
    private HistFunction getChild(ISymbol s) {
        for (HistFunction f : this.children) {
            if (f.symbol == s) {
                return f;
            }
        }
        HistFunction f = new HistFunction(this, s);
        this.children.add(f);
        return f;
    }

    void addBucket(Bucket b, ISymbol s, IBinaryObject program) {
        HistFunction hf = getChild(s);
        hf.addBucket(b, program);
    }

    void addCallGraphNode(CallGraphNode node) {
        ISymbol s = node.getSymbol();
        HistFunction hf = getChild(s);
        hf.addCallGraphNode(node);
    }

    @Override
    public LinkedList<? extends TreeElement> getChildren() {
        return this.children;
    }

    @Override
    public String getName() {
        Path f = new Path(sourcePath);
        return f.lastSegment();
    }

    @Override
    public int getSourceLine() {
        return 0;
    }

    @Override
    public String getSourcePath() {
        return this.sourcePath;
    }

    @Override
    public int getCalls() {
        return -1;
    }

}
