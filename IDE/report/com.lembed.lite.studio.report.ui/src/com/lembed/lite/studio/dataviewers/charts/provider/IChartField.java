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
package com.lembed.lite.studio.dataviewers.charts.provider;

import com.lembed.lite.studio.dataviewers.abstractviewers.ISTDataViewersField;

/**
 * The interface used by the charts to get a <code>Number</code> data from a field.
 */
public interface IChartField extends ISTDataViewersField {

    /**
     * Returns the number to display in the chart for this object.
     *
     * @param obj The field to retrieve from.
     * @return the corresponding number value
     */
    Number getNumber(Object obj);
}
