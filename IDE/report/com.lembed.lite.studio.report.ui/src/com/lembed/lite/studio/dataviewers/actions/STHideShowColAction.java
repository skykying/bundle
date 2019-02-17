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
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.lembed.lite.studio.dataviewers.STDataViewersActivator;
import com.lembed.lite.studio.dataviewers.abstractviewers.AbstractSTViewer;
import com.lembed.lite.studio.dataviewers.abstractviewers.STDataViewersMessages;
import com.lembed.lite.studio.dataviewers.dialogs.STDataViewersHideShowColumnsDialog;

/**
 * This action allows the user to hide/show some columns.
 */
public class STHideShowColAction extends Action {

    private final AbstractSTViewer stViewer;

    /**
     * Creates the action for the given viewer.
     *
     * @param stViewer The AbstractSTViewer to create the action for.
     */
    public STHideShowColAction(AbstractSTViewer stViewer) {
        super(STDataViewersMessages.hideshowAction_title,
                AbstractUIPlugin.imageDescriptorFromPlugin(STDataViewersActivator.PLUGIN_ID,
                        "icons/prop_edt.gif")); //$NON-NLS-1$
        this.stViewer = stViewer;
        setEnabled(true);
    }

    @Override
    public void run() {
        STDataViewersHideShowColumnsDialog dialog = new STDataViewersHideShowColumnsDialog(stViewer);

        if (dialog.open() == Window.OK && dialog.isDirty()) {
            if (dialog.getManager() != null) {
                stViewer.setHideShowManager(dialog.getManager());
            }
        }
    }
}
