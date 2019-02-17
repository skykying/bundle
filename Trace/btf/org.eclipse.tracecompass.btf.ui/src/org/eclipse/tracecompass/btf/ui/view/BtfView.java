/*******************************************************************************
 * Copyright (c) 2014, 2017 École Polytechnique de Montréal and others
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Geneviève Bastien - Initial API and implementation
 *******************************************************************************/

package org.eclipse.tracecompass.btf.ui.view;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceContext;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceManager;
import org.eclipse.tracecompass.tmf.ui.viewers.TmfViewer;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.TmfXYChartViewer;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfXYChartSettings;
import org.eclipse.tracecompass.tmf.ui.views.TmfChartView;

/**
 * CPU usage view. It contains 2 viewers: one tree viewer showing all the
 * threads who were on the CPU in the time range, and one XY chart viewer
 * plotting the total time spent on CPU and the time of the threads selected in
 * the tree viewer.
 *
 * @author Geneviève Bastien
 */
public class BtfView extends TmfChartView {

    /** ID string */
    public static final @NonNull String ID = "org.eclipse.tracecompass.btf.ui.view.BtfView"; //$NON-NLS-1$

    /** ID of the followed CPU in the map data in {@link TmfTraceContext} */
    public static final @NonNull String CPU_USAGE_FOLLOW_CPU = ID + ".FOLLOW_CPU"; //$NON-NLS-1$

    /*
     * To avoid up and downs CPU usage when process is in and out of CPU frequently,
     * use a smaller resolution to get better averages.
     */
    private static final double RESOLUTION = 0.4;

    /**
     * Constructor
     */
    public BtfView() {
        super(Messages.BtfView_Title);
    }

    @Override
    protected TmfXYChartViewer createChartViewer(Composite parent) {
        TmfXYChartSettings settings = new TmfXYChartSettings(Messages.BtfXYViewer_Title, Messages.BtfXYViewer_TimeXAxis, Messages.BtfXYViewer_CpuYAxis, RESOLUTION);
        return new BtfXYViewer(parent, settings);
    }

    @Override
    public TmfViewer createLeftChildViewer(Composite parent) {
        return new BtfTreeViewer(parent);
    }

    /**
     * Save a data in the data map of {@link TmfTraceContext}
     */
    private static void saveData(@NonNull ITmfTrace trace, @NonNull String key, @NonNull Object data) {
        TmfTraceManager.getInstance().updateTraceContext(trace,
                builder -> builder.setData(key, data));
    }

    private static Object getData(@NonNull ITmfTrace trace, @NonNull String key) {
        TmfTraceContext ctx = TmfTraceManager.getInstance().getTraceContext(trace);
        return ctx.getData(key);
    }

    @SuppressWarnings({ "unchecked", "javadoc" })
    protected static @NonNull Set<@NonNull Integer> getCpus(@NonNull ITmfTrace trace) {
        Set<@NonNull Integer> data = (Set<@NonNull Integer>) getData(trace, BtfView.CPU_USAGE_FOLLOW_CPU);
        return data != null ? data : Collections.emptySet();
    }

    @Override
    public void setFocus() {
        TmfXYChartViewer xyViewer = getChartViewer();
        if (xyViewer != null) {
            xyViewer.getControl().setFocus();
        }
    }

    public void traceOpened(ITmfTrace trace) {

        TmfXYChartViewer xyViewer = getChartViewer();
        TmfViewer viewer = getLeftChildViewer();
        if (xyViewer instanceof BtfXYViewer && viewer instanceof BtfTreeViewer) {
            Set<Integer> data = (Set<Integer>) getData(trace, CPU_USAGE_FOLLOW_CPU);
            if (data == null) {
                data = new TreeSet<>();
                saveData(trace, CPU_USAGE_FOLLOW_CPU, data);
            }

            xyViewer.refresh();
            ((BtfXYViewer) xyViewer).setTitle();

            BtfTreeViewer treeViewer = (BtfTreeViewer) viewer;
            treeViewer.updateContent(treeViewer.getWindowStartTime(), treeViewer.getWindowEndTime(), false);
        }
    }
}
