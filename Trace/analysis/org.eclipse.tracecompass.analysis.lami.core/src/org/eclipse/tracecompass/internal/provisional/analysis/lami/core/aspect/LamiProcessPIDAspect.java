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
import org.eclipse.tracecompass.internal.provisional.analysis.lami.core.types.LamiProcess;

/**
 * Aspect for process PID
 *
 * @author Philippe Proulx
 */
public class LamiProcessPIDAspect extends LamiTableEntryAspect {

    private final int fColIndex;

    /**
     * Constructor
     *
     * @param colName
     *            Column name
     * @param colIndex
     *            Column index
     */
    public LamiProcessPIDAspect(String colName, int colIndex) {
        super(colName + " (PID)", null); //$NON-NLS-1$
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
        if (data instanceof LamiProcess) {
            Long pid = ((LamiProcess) data).getPID();

            if (pid == null) {
                return null;
            }

            return pid.toString();
        }
        /* Could be null, unknown, etc. */
        return data.toString();
    }

    @Override
    public @Nullable Number resolveNumber(LamiTableEntry entry) {
        LamiData data = entry.getValue(fColIndex);
        if (data instanceof LamiProcess) {
            Long pid = ((LamiProcess) data).getPID();
            return pid;
        }

        return null;
    }

    @Override
    public Comparator<LamiTableEntry> getComparator() {
        return (o1, o2) -> {
            Number d1 = resolveNumber(o1);
            Number d2 = resolveNumber(o2);

            if (d1 == null && d2 == null) {
                return 0;
            }
            if (d1 == null) {
                return 1;
            }

            if (d2 == null) {
                return -1;
            }

            return Long.compare(d1.longValue(), d2.longValue());
        };
    }
}
