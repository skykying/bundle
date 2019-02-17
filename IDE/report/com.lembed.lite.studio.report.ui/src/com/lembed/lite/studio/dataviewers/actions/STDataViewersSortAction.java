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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.lembed.lite.studio.dataviewers.STDataViewersActivator;
import com.lembed.lite.studio.dataviewers.abstractviewers.AbstractSTViewer;
import com.lembed.lite.studio.dataviewers.abstractviewers.STDataViewersMessages;
import com.lembed.lite.studio.dataviewers.dialogs.STDataViewersSortDialog;

/**
 * This action allows the user to sort the data in the viewer
 */
public class STDataViewersSortAction extends Action {

    private final AbstractSTViewer stViewer;

    private final STDataViewersSortDialog dialog;

    /**
     * Creates the action for the given viewer.
     *
     * @param stViewer The AbstractSTViewer to create the action for.
     */
    public STDataViewersSortAction(AbstractSTViewer stViewer) {
        super(STDataViewersMessages.sortAction_title,
                AbstractUIPlugin.imageDescriptorFromPlugin(STDataViewersActivator.PLUGIN_ID,
                        "icons/sort.gif")); //$NON-NLS-1$
        super.setToolTipText(STDataViewersMessages.sortAction_tooltip);
        this.stViewer = stViewer;

        // building a sort dialog
        dialog = new STDataViewersSortDialog(stViewer.getViewer().getControl().getShell(), stViewer.getTableSorter());

        setEnabled(true);
    }

    @Override
    public void run() {
        if (dialog.open() == Window.OK && dialog.isDirty()) {
            BusyIndicator.showWhile(null, () -> stViewer.setComparator(dialog.getSorter()));

        }
    }
}