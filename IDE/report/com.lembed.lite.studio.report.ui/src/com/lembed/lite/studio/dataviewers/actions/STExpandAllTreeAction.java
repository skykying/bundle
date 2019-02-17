/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marzia Maugeri <marzia.maugeri@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.lite.studio.dataviewers.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.lembed.lite.studio.dataviewers.STDataViewersActivator;
import com.lembed.lite.studio.dataviewers.abstractviewers.AbstractSTTreeViewer;
import com.lembed.lite.studio.dataviewers.abstractviewers.STDataViewersMessages;

/**
 * This action expands all the tree
 *
 */
public class STExpandAllTreeAction extends Action {

    private final AbstractSTTreeViewer stViewer;

    /**
     * Constructor
     *
     * @param stViewer
     *            the stViewer to expand
     */
    public STExpandAllTreeAction(AbstractSTTreeViewer stViewer) {
        super(STDataViewersMessages.expandAllAction_title,
                AbstractUIPlugin.imageDescriptorFromPlugin(STDataViewersActivator.PLUGIN_ID,
                        "icons/expand_all.gif")); //$NON-NLS-1$
        this.stViewer = stViewer;
    }

    @Override
    public void run() {
        Object input = stViewer.getViewer().getInput();
        if (input != null) {
            stViewer.getViewer().expandAll();
        }
    }
}
