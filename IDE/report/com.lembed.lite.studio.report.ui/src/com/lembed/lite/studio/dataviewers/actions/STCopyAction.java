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

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.dataviewers.abstractviewers.AbstractSTViewer;
import com.lembed.lite.studio.dataviewers.abstractviewers.ISTDataViewersField;
import com.lembed.lite.studio.dataviewers.abstractviewers.STDataViewersMessages;

/**
 * The Class STCopyAction.
 */
public class STCopyAction extends Action {
    private final AbstractSTViewer stViewer;

    /**
     * Constructor
     *
     * @param stViewer
     *            the stViewer to expand
     */
    public STCopyAction(final AbstractSTViewer stViewer) {
        super(STDataViewersMessages.copyToAction_title, PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
        this.stViewer = stViewer;
    }

    @Override
    public void run() {
        Clipboard cb = new Clipboard(Display.getDefault());
        IStructuredSelection selections = (IStructuredSelection) stViewer.getViewer().getSelection();
        Iterator<?> iterator = selections.iterator();
        StringBuilder sb = new StringBuilder();

        while (iterator.hasNext()) {
            Object obj = iterator.next();
            boolean needTab = false;
            for (ISTDataViewersField field : stViewer.getAllFields()) {
                if (needTab) {
                    sb.append("\t"); //$NON-NLS-1$
                }
                needTab = true;
                if (field.getValue(obj) != null) {
                    sb.append(field.getValue(obj));
                }
            }
            sb.append("\n"); //$NON-NLS-1$
        }
        cb.setContents(new Object[] { sb.toString() }, new Transfer[] { TextTransfer.getInstance() });
    }
}
