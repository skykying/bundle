/*******************************************************************************
 * Copyright (c) 2015, 2016 EfficiOS Inc., Alexandre Montplaisir
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.provisional.analysis.lami.core.aspect;

import java.util.Comparator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.internal.provisional.analysis.lami.core.module.LamiTableEntry;
import org.eclipse.tracecompass.internal.provisional.analysis.lami.core.types.LamiData;
import org.eclipse.tracecompass.internal.provisional.analysis.lami.core.types.LamiTimestamp;
import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimestampFormat;

/**
 * Aspect for timestamps
 *
 * @author Alexandre Montplaisir
 */
public class LamiTimestampAspect extends LamiTableEntryAspect {

    private final int fColIndex;

    /**
     * Constructor
     *
     * @param timestampName
     *            Name of the timestamp
     * @param colIndex
     *            Column index
     */
    public LamiTimestampAspect(String timestampName, int colIndex) {
        super(timestampName, null);
        fColIndex = colIndex;
    }

    @Override
    public boolean isContinuous() {
        return true;
    }

    @Override
    public boolean isTimeStamp() {
        return true;
    }

    @Override
    public @Nullable String resolveString(LamiTableEntry entry) {
        LamiData data = entry.getValue(fColIndex);
        if (data instanceof LamiTimestamp) {
            LamiTimestamp ts = (LamiTimestamp) data;

            // TODO: Consider low and high limits here.
            Number timestamp = ts.getValue();

            if (timestamp != null) {
                return TmfTimestampFormat.getDefaulTimeFormat().format(timestamp.longValue());
            }
        }
        return data.toString();
    }

    @Override
    public @Nullable Number resolveNumber(@NonNull LamiTableEntry entry) {
        LamiData data = entry.getValue(fColIndex);
        if (data instanceof LamiTimestamp) {
            LamiTimestamp ts = (LamiTimestamp) data;

            // TODO: Consider low and high limits here.
            return ts.getValue();
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
