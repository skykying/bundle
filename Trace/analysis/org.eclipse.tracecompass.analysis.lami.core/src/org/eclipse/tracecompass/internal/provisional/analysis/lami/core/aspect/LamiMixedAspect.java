/*******************************************************************************
 * Copyright (c) 2016 EfficiOS Inc., Philippe Proulx
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.provisional.analysis.lami.core.aspect;

import java.util.Comparator;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.internal.provisional.analysis.lami.core.module.LamiTableEntry;
import org.eclipse.tracecompass.internal.provisional.analysis.lami.core.types.LamiData;
import org.eclipse.tracecompass.internal.provisional.analysis.lami.core.types.LamiData.DataType;

/**
 * Aspect for LAMI mixed types.
 *
 * The data in this column can contain any class of data.
 *
 * @author Philippe Proulx
 */
public class LamiMixedAspect extends LamiTableEntryAspect {

    private final int fColIndex;

    /**
     * Constructor
     *
     * @param colName
     *            Column name
     * @param colIndex
     *            Column index
     */
    public LamiMixedAspect(String colName, int colIndex) {
        super(colName, null);
        fColIndex = colIndex;
    }

    @Override
    public boolean isContinuous() {
        return false;
    }

    @Override
    public boolean isTimeStamp() {
        return false;
    }

    @Override
    public @Nullable String resolveString(LamiTableEntry entry) {
        LamiData data = entry.getValue(fColIndex);
        Class<? extends LamiData> cls = data.getClass();

        DataType dataType = DataType.fromClass(cls);

        if (dataType == null) {
            return data.toString();
        }

        String str = data.toString();

        if (dataType.getUnits() != null) {
            str += " " + dataType.getUnits(); //$NON-NLS-1$
        }

        return str;
    }

    @Override
    public @Nullable Number resolveNumber(LamiTableEntry entry) {
        return null;
    }

    @Override
    public Comparator<LamiTableEntry> getComparator() {
        return (o1, o2) -> {
            String s1 = resolveString(o1);
            String s2 = resolveString(o2);

            if (s1 == null && s2 == null) {
                return 0;
            }
            if (s1 == null) {
                return 1;
            }

            if (s2 == null) {
                return -1;
            }

            return s1.compareTo(s2);
        };
    }

}
