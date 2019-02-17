/*******************************************************************************
 * Copyright (c) 2015, 2016 EfficiOS Inc., Alexandre Montplaisir and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.analysis.os.linux.core.latency;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.datastore.core.interval.IHTIntervalReader;
import org.eclipse.tracecompass.datastore.core.serialization.ISafeByteBufferWriter;
import org.eclipse.tracecompass.datastore.core.serialization.SafeByteBufferFactory;
import org.eclipse.tracecompass.segmentstore.core.ISegment;
import org.eclipse.tracecompass.segmentstore.core.segment.interfaces.INamedSegment;

/**
 * A linux kernel system call, represented as an {@link ISegment}.
 *
 * @author Alexandre Montplaisir
 * @since 2.0
 */
public final class SystemCall implements INamedSegment {

    private static final long serialVersionUID = 1554494342105208730L;

    /**
     * The reader for this segment class
     */
    public static final IHTIntervalReader<ISegment> READER = buffer -> new SystemCall(buffer.getLong(), buffer.getLong(), buffer.getString());

    /**
     * The subset of information that is available from the syscall entry event.
     */
    public static class InitialInfo {

        private long fStartTime;
        private String fName;

        /**
         * @param startTime
         *            Start time of the system call
         * @param name
         *            Name of the system call
         */
        public InitialInfo(
                long startTime,
                String name) {
            fStartTime = startTime;
            fName = name.intern();
        }
    }

    private final long fStartTime;
    private final long fEndTime;
    private final String fName;

    /**
     * @param info
     *            Initial information of the system call
     * @param endTime
     *            End time of the system call
     */
    public SystemCall(
            InitialInfo info,
            long endTime) {
        fStartTime = info.fStartTime;
        fName = info.fName;
        fEndTime = endTime;
    }

    private SystemCall(long startTime, long endTime, String name) {
        fStartTime = startTime;
        fEndTime = endTime;
        fName = name;
    }

    @Override
    public long getStart() {
        return fStartTime;
    }

    @Override
    public long getEnd() {
        return fEndTime;
    }

    /**
     * Get the name of the system call
     *
     * @return Name
     */
    @Override
    public String getName() {
        return fName;
    }

    @Override
    public int getSizeOnDisk() {
        return 2 * Long.BYTES + SafeByteBufferFactory.getStringSizeInBuffer(fName);
    }

    @Override
    public void writeSegment(@NonNull ISafeByteBufferWriter buffer) {
        buffer.putLong(fStartTime);
        buffer.putLong(fEndTime);
        buffer.putString(fName);
    }

    @Override
    public int compareTo(@NonNull ISegment o) {
        int ret = INamedSegment.super.compareTo(o);
        if (ret != 0) {
            return ret;
        }
        return toString().compareTo(o.toString());
    }

    @Override
    public String toString() {
        return "Start Time = " + getStart() + //$NON-NLS-1$
                "; End Time = " + getEnd() + //$NON-NLS-1$
                "; Duration = " + getLength() + //$NON-NLS-1$
                "; Name = " + getName(); //$NON-NLS-1$
    }

}
