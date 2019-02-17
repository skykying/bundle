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
package com.lembed.codeanalyzer.cov.view;

import java.text.NumberFormat;

import com.lembed.codeanalyzer.cov.model.TreeElement;
import com.lembed.codeanalyzer.ui.abstractviewers.AbstractSTDataViewersField;
import com.lembed.codeanalyzer.ui.charts.provider.IChartField;

public class FieldExecutedLines extends AbstractSTDataViewersField implements IChartField {

    @Override
    public String getColumnHeaderText() {
        return Messages.FieldExecutedLines_column_header;
    }

    @Override
    public String getValue(Object obj) {
        int v = getExecutedLines(obj);
        return NumberFormat.getInstance().format(v);
    }

    @Override
    public String getToolTipText(Object element) {
        int v = getExecutedLines(element);
        String s = NumberFormat.getInstance().format(v);
        s += Messages.FieldExecutedLines_column_tooltip;
        if (v > 1) {
            s += "s"; //$NON-NLS-1$
        }
        return s;
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        int i1 = getExecutedLines(obj1);
        int i2 = getExecutedLines(obj2);
        if (i1 > i2) {
            return 1;
        }
        if (i1 < i2) {
            return -1;
        }
        return 0;
    }

    private int getExecutedLines(Object o) {
        if (o instanceof TreeElement) {
            return ((TreeElement) o).getExecutedLines();
        }
        return 0;
    }

    @Override
    public Integer getNumber(Object obj) {
        return getExecutedLines(obj);
    }
}
