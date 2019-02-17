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
package com.lembed.codeanalyzer.internal.gprof.view.fields;

import org.eclipse.swt.graphics.Color;

import com.lembed.codeanalyzer.internal.gprof.Messages;
import com.lembed.codeanalyzer.internal.gprof.view.GmonView;
import com.lembed.codeanalyzer.internal.gprof.view.histogram.TreeElement;
import com.lembed.codeanalyzer.ui.abstractviewers.AbstractSTTreeViewer;


/**
 * Column "Samples" of displayed elements
 *
 * @author Xavier Raynaud <xavier.raynaud@st.com>
 */
public class SamplePerCallField extends SampleProfField {

    /**
     * Constructor
     * @param viewer the gmon viewer
     */
    public SamplePerCallField(AbstractSTTreeViewer viewer) {
        super(viewer);
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        TreeElement e1 = (TreeElement) obj1;
        TreeElement e2 = (TreeElement) obj2;
        int c1 = e1.getCalls();
        int c2 = e2.getCalls();
        if ((c1 == 0 || c1 == -1) && (c2 == 0 || c2 == -1)) {
            return 0;
        }
        if (c1 == 0 || c1 == -1) {
            return -1;
        }
        if (c2 == 0 || c2 == -1) {
            return 1;
        }
        float f1 = (float)e1.getSamples()/(float)c1;
        float f2 = (float)e2.getSamples()/(float)c2;
        return Float.compare(f1, f2);
    }

    @Override
    public String getColumnHeaderText() {
        return Messages.SamplePerCallField_TIME_CALL;
    }

    @Override
    public String getColumnHeaderTooltip() {
        return Messages.SamplePerCallField_TIME_CALL_TOOLTIP;
    }

    @Override
    public String getValue(Object obj) {
        TreeElement e = (TreeElement) obj;
        int i = e.getSamples();
        int j = e.getCalls();
        if (i == -1 || j <= 0) {
            return ""; //$NON-NLS-1$
        }
        float k = (float)i/(float)j;

        double prof_rate = getProfRate();
        if(prof_rate != 0){
            return getValue(k, prof_rate);
        }else {
            return ""; //$NON-NLS-1$
        }
    }

    @Override
    public Color getBackground(Object element) {
        return GmonView.getBackground(element);
    }

    @Override
    public Number getNumber(Object obj) {
        TreeElement e = (TreeElement) obj;
        int i = e.getSamples();
        int j = e.getCalls();
        if (i == -1 || j <= 0) {
            return 0L;
        }
        float k = (float)i/(float)j;

        double prof_rate = getProfRate();
        if(prof_rate != 0){
            return k/prof_rate;
        } else {
            return 0L;
        }
    }


}
