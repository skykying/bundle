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
package com.lembed.codeanalyzer.ui.listeners;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.lembed.codeanalyzer.ui.abstractviewers.AbstractSTViewer;

public class STDisposeListener implements DisposeListener {
    AbstractSTViewer stViewer;

    public STDisposeListener(AbstractSTViewer stViewer) {
        this.stViewer = stViewer;
    }

    @Override
    public void widgetDisposed(DisposeEvent e) {
        stViewer.saveState();
    }

}
