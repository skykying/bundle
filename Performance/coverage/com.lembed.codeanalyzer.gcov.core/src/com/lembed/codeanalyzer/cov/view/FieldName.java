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

import com.lembed.codeanalyzer.cov.model.TreeElement;
import com.lembed.codeanalyzer.ui.abstractviewers.AbstractSTDataViewersField;

public class FieldName extends AbstractSTDataViewersField {

    @Override
    public String getColumnHeaderText() {
        return Messages.FieldName_column_header;
    }

    @Override
    public String getValue(Object obj) {
        if (obj instanceof TreeElement) {
            TreeElement e = (TreeElement) obj;
            String nm = e.getName();
            nm = nm.substring(nm.lastIndexOf('/') + 1);
            nm = nm.substring(nm.lastIndexOf('\\') + 1);
            return nm;
        }
        return ""; //$NON-NLS-1$
    }

    @Override
    public String getToolTipText(Object element) {
        if (element instanceof TreeElement) {
            TreeElement elem = (TreeElement) element;
            return elem.getName();
        }
        return ""; //$NON-NLS-1$
    }

    @Override
    public int compare(Object obj1, Object obj2) {
        String s1 = getValue(obj1);
        String s2 = getValue(obj2);
        if (s1 == null) {
            if (s2 == null) {
                return 0;
            }
            return -1;
        }
        if (s2 == null) {
            return 1;
        }
        return s1.compareTo(s2);
    }
}
