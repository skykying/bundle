/**********************************************************************
 * Copyright (c) 2018 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **********************************************************************/

package org.eclipse.tracecompass.internal.analysis.timing.core.segmentstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.analysis.timing.core.segmentstore.SegmentStoreStatisticsModel;
import org.eclipse.tracecompass.analysis.timing.core.segmentstore.statistics.AbstractSegmentStatisticsAnalysis;
import org.eclipse.tracecompass.analysis.timing.core.statistics.IStatistics;
import org.eclipse.tracecompass.internal.tmf.core.model.AbstractTmfTraceDataProvider;
import org.eclipse.tracecompass.segmentstore.core.ISegment;
import org.eclipse.tracecompass.tmf.core.analysis.IAnalysisModule;
import org.eclipse.tracecompass.tmf.core.model.CommonStatusMessage;
import org.eclipse.tracecompass.tmf.core.model.filters.FilterTimeQueryFilter;
import org.eclipse.tracecompass.tmf.core.model.filters.TimeQueryFilter;
import org.eclipse.tracecompass.tmf.core.model.tree.ITmfTreeDataProvider;
import org.eclipse.tracecompass.tmf.core.response.TmfModelResponse;
import org.eclipse.tracecompass.tmf.core.response.ITmfResponse.Status;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * This data provider will supply a statistics tree for an
 * {@link AbstractSegmentStatisticsAnalysis}. Passing a
 * {@link FilterTimeQueryFilter} will also return the statistics from the
 * selected range.
 *
 * @author Loic Prieur-Drevon
 * @since 4.0
 */
public class SegmentStoreStatisticsDataProvider extends AbstractTmfTraceDataProvider
        implements ITmfTreeDataProvider<SegmentStoreStatisticsModel> {

    /**
     * Base {@link SegmentStoreStatisticsDataProvider} prefix
     */
    public static final String ID = "org.eclipse.tracecompass.analysis.timing.core.segmentstore.SegmentStoreStatisticsDataProvider"; //$NON-NLS-1$

    private static final String STATISTICS_SUFFIX = ".statistics"; //$NON-NLS-1$
    private static final String TOTAL_PREFIX = "Total_"; //$NON-NLS-1$
    private static final String SELECTION_PREFIX = "Selection_"; //$NON-NLS-1$
    private static final Map<AbstractSegmentStatisticsAnalysis, SegmentStoreStatisticsDataProvider> PROVIDER_MAP = new WeakHashMap<>();
    private static final AtomicLong ENTRY_ID = new AtomicLong();

    private final AbstractSegmentStatisticsAnalysis fProvider;
    private final String fId;

    private final Map<String, Long> fIdToType = new HashMap<>();
    private final long fTraceId = ENTRY_ID.getAndIncrement();

    /**
     * Get an instance of {@link SegmentStoreStatisticsDataProvider} for a trace and
     * provider. Returns a null instance if the ISegmentStoreProvider is null. If
     * the provider is an instance of {@link IAnalysisModule}, analysis is also
     * scheduled.
     *
     * @param trace
     *            A trace on which we are interested to fetch a model
     * @param module
     *            the ID of the analysis to generate this provider from.
     * @return An instance of SegmentStoreDataProvider. Returns a null if the
     *         ISegmentStoreProvider is null.
     */
    public static synchronized @Nullable SegmentStoreStatisticsDataProvider getOrCreate(ITmfTrace trace, AbstractSegmentStatisticsAnalysis module) {
        // TODO experiment support.
        return PROVIDER_MAP.computeIfAbsent(module, p -> new SegmentStoreStatisticsDataProvider(trace, p, module.getId() + STATISTICS_SUFFIX));
    }

    /**
     * Constructor
     *
     * @param trace
     *            the trace for which this provider will supply info
     * @param provider
     *            the segment statistics module from which to get data
     * @param id
     *            the extension point ID
     */
    public SegmentStoreStatisticsDataProvider(ITmfTrace trace, AbstractSegmentStatisticsAnalysis provider, String id) {
        super(trace);
        fId = id;
        fProvider = provider;
    }

    @Override
    public TmfModelResponse<List<SegmentStoreStatisticsModel>> fetchTree(TimeQueryFilter filter, @Nullable IProgressMonitor monitor) {
        if (monitor != null) {
            fProvider.waitForCompletion(monitor);
            if (monitor.isCanceled()) {
                return new TmfModelResponse<>(null, Status.CANCELLED, CommonStatusMessage.TASK_CANCELLED);
            }
        } else {
            fProvider.waitForCompletion();
        }

        IStatistics<ISegment> statsTotal = fProvider.getStatsTotal();
        if (statsTotal == null) {
            return new TmfModelResponse<>(null, Status.FAILED, CommonStatusMessage.ANALYSIS_INITIALIZATION_FAILED);
        }

        List<SegmentStoreStatisticsModel> list = new ArrayList<>();
        list.add(new SegmentStoreStatisticsModel(fTraceId, -1, Objects.requireNonNull(getTrace().getName()), statsTotal));

        /*
         * Add statistics for full duration.
         */
        long totalId = getUniqueId(TOTAL_PREFIX);
        list.add(new SegmentStoreStatisticsModel(totalId, fTraceId, Objects.requireNonNull(Messages.SegmentStoreStatisticsDataProvider_Total), statsTotal));
        Map<String, IStatistics<ISegment>> totalStats = fProvider.getStatsPerType();
        for (Entry<String, IStatistics<ISegment>> entry : totalStats.entrySet()) {
            list.add(new SegmentStoreStatisticsModel(getUniqueId(TOTAL_PREFIX + entry.getKey()), totalId, entry.getKey(), entry.getValue()));
        }

        /*
         * Add statistics for selection if any.
         */
        if (filter instanceof FilterTimeQueryFilter && ((FilterTimeQueryFilter) filter).isFiltered()) {
            long start = filter.getStart();
            long end = filter.getEnd();

            IProgressMonitor nonNullMonitor = monitor != null ? monitor : new NullProgressMonitor();
            IStatistics<ISegment> statsForRange = fProvider.getStatsForRange(start, end, nonNullMonitor);
            if (statsForRange == null) {
                return new TmfModelResponse<>(null, Status.CANCELLED, CommonStatusMessage.TASK_CANCELLED);
            }

            long selectionId = getUniqueId(SELECTION_PREFIX);
            list.add(new SegmentStoreStatisticsModel(selectionId, fTraceId, Objects.requireNonNull(Messages.SegmentStoreStatisticsDataProvider_Selection), statsForRange));
            Map<String, IStatistics<ISegment>> selectionStats = fProvider.getStatsPerTypeForRange(start, end, nonNullMonitor);
            for (Entry<String, IStatistics<ISegment>> entry : selectionStats.entrySet()) {
                list.add(new SegmentStoreStatisticsModel(getUniqueId(SELECTION_PREFIX + entry.getKey()), selectionId, entry.getKey(), entry.getValue()));
            }
        }
        return new TmfModelResponse<>(Collections.unmodifiableList(list), Status.COMPLETED, CommonStatusMessage.COMPLETED);
    }

    private long getUniqueId(String name) {
        return fIdToType.computeIfAbsent(name, n -> ENTRY_ID.getAndIncrement());
    }

    @Override
    public String getId() {
        return fId;
    }

    @Override
    public void dispose() {
        fIdToType.clear();
        fProvider.dispose();
    }
}
