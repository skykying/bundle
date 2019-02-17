/*******************************************************************************
 * Copyright (c) 2014 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Alexandre Montplaisir - Initial API and implementation
 *******************************************************************************/

package org.eclipse.tracecompass.btf.core.analysis;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.btf.core.trace.BtfTrace;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateSystemDisposedException;
import org.eclipse.tracecompass.statesystem.core.interval.ITmfStateInterval;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue.Type;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfAnalysisException;
import org.eclipse.tracecompass.tmf.core.model.filters.SelectionTimeQueryFilter;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * Analysis module for the BTF base analysis
 *
 * <pre>
 * {root}
 *   +- Cores
 *   |   +- Core 1            (not running/Name of the running Task)
 *   |   +- Core 2            (not running/Name of the running Task)
 *   |
 *   +- Tasks
 *       +- Task A
 *       |   +- Core 1             (not running/running/suspended)
 *       |   |    +- Runnable A1   (not running/running/suspended)
 *       |   |    +- Runnable A2   (not running/running/suspended)
 *       |   +- Core 2
 *       |   |    +- Runnable A1
 *       |   |    +- Runnable A2
 *       |   +- ActiveCore
 *       |
 *       +- Task B
 *           +- Core 1
 *           |    +- Runnable B1
 *           |    +- Runnable B2
 *           +- Core 2
 *           |    +- Runnable B1
 *           |    +- Runnable B2
 *           +- ActiveCore
 * </pre>
 *
 * @author Alexandre Montplaisir
 */
public class BtfAnalysisModule extends TmfStateSystemAnalysisModule {

    private static final String ATTRIBUTE_TASKS = "Tasks"; //$NON-NLS-1$

    private static final String ATTRIBUTE_CORES = "Cores"; //$NON-NLS-1$
    private static final String ATTRIBUTE_ACTIVE_CORE = "ActiveCore"; //$NON-NLS-1$

    @SuppressWarnings("javadoc")
    public static final @NonNull String ID = "org.eclipse.linuxtools.btf.core.analysis"; //$NON-NLS-1$

    @Override
    public boolean setTrace(ITmfTrace trace) throws TmfAnalysisException {
        if (!(trace instanceof BtfTrace)) {
            return false;
        }
        return super.setTrace(trace);
    }

    @SuppressWarnings({ "javadoc", "unused" })
    public void queryAll(@NonNull SelectionTimeQueryFilter filter) throws AttributeNotFoundException, StateSystemDisposedException {
        ITmfStateSystem ss = this.getStateSystem();

        long[] xValue = filter.getTimesRequested();
        for (int i = 0; i < xValue.length; i++) {
            // System.out.println(xValue.length+"@@@%%%%%%%%"+xValue[i]); //$NON-NLS-1$
        }

        List<Long> tsQust = new LinkedList<>();

        for (long x : xValue) {
            tsQust.add(new Long(x));
        }

        Collection<@NonNull Long> sls = filter.getSelectedItems();
        Iterator<@NonNull Long> its = sls.iterator();
        while (its.hasNext()) {
            Long vs = its.next();
            System.out.println(sls.size() + "*****%%%%%%%%" + vs); //$NON-NLS-1$
        }

        if (ss != null) {
            int taskQuark = ss.getQuarkAbsolute(ATTRIBUTE_CORES);
            List<@NonNull Integer> qlist = ss.getSubAttributes(taskQuark, false);
            Iterable<@NonNull ITmfStateInterval> d2s = ss.query2D(qlist, tsQust);
            d2s.forEach(d -> {
                ITmfStateInterval si = d;
                int aat = si.getAttribute();
                String an = ss.getAttributeName(aat);

                System.out.println(si.getStateValue().unboxInt() + " x " + an); //$NON-NLS-1$
            });

            taskQuark = ss.getQuarkAbsolute(ATTRIBUTE_TASKS);
            List<@NonNull Integer> subList = ss.getSubAttributes(taskQuark, true);

            Iterable<@NonNull ITmfStateInterval> d2d = ss.query2D(subList, tsQust);
            Iterator<@NonNull ITmfStateInterval> dlists = d2d.iterator();
            while (dlists.hasNext()) {
                ITmfStateInterval v = dlists.next();
                String vs = ""; //$NON-NLS-1$
                long is = v.getStartTime();
                ITmfStateValue sv = v.getStateValue();
                if (sv.isNull()) {
                    vs = "null"; //$NON-NLS-1$
                } else {
                    Type ty = sv.getType();

                    if(ty == Type.INTEGER) {
                        vs = "" + sv.unboxInt(); //$NON-NLS-1$
                    }

                    if(ty == Type.STRING) {
                        vs = sv.unboxStr();
                    }
                }
                long es = v.getEndTime();
                System.out.println(tsQust.size() + "@" +ss.getAttributeName(v.getAttribute()) + "!!!!!!" + vs + "@@@@" + is + "###" + es); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

            // for (Integer s : subList) {
            // long endTime = ss.getCurrentEndTime();
            // System.out.println(ss.getAttributeName(s)+"^^^^^");
            // int qu = ss.optQuarkAbsolute(ss.getAttributeName(s));
            // if (qu != ITmfStateSystem.INVALID_ATTRIBUTE) {
            //
            //
            // ITmfStateInterval v = ss.querySingleState(endTime, qu);
            //
            // int i = v.getStateValue().unboxInt();
            // long is = v.getStartTime();
            // long es = v.getEndTime();
            // System.out.println(ss.getAttributeName(s) + "!!!!!!" + i + "@@@@" + is +
            // "###" + es); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            // }
            // }

            long endTime = ss.getCurrentEndTime();
            long startTime = ss.getStartTime();
            List<@NonNull ITmfStateInterval> sts = ss.queryFullState(startTime);
            List<@NonNull ITmfStateInterval> eds = ss.queryFullState(endTime);

            for (ITmfStateInterval st : sts) {
                int attr = st.getAttribute();
                String attrName = ss.getAttributeName(attr);
                // System.out.println(attrName+"!"+st.getValue()); //$NON-NLS-1$
            }

            System.out.println("@@@@@@@@@@@@@@@@@@"); //$NON-NLS-1$
        }

    }

    /**
     * @since 2.1
     */
    @Override
    public BtfTrace getTrace() {
        return (BtfTrace) super.getTrace();
    }

    @Override
    protected ITmfStateProvider createStateProvider() {
        return new BtfStateProvider(checkNotNull(getTrace()));
    }
}
