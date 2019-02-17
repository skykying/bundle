/**********************************************************************
 * Copyright (c) 2017, 2018 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **********************************************************************/

package org.eclipse.tracecompass.tmf.core.model.timegraph;

import java.util.List;

/**
 * Implementation of {@link ITimeGraphRowModel}.
 *
 * @author Simon Delisle
 * @since 4.0
 */
public class TimeGraphRowModel implements ITimeGraphRowModel {
    private final long fEntryID;
    private final List<ITimeGraphState> fStates;

    /**
     * Constructor
     *
     * @param entryID
     *            Entry model's ID for this row
     * @param states
     *            List of {@link TimeGraphState}
     */
    public TimeGraphRowModel(long entryID, List<ITimeGraphState> states) {
        fEntryID = entryID;
        fStates = states;
    }

    @Override
    public List<ITimeGraphState> getStates() {
        return fStates;
    }

    @Override
    public long getEntryID() {
        return fEntryID;
    }

}
