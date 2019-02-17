/**********************************************************************
 * Copyright (c) 2018 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **********************************************************************/

package org.eclipse.tracecompass.internal.analysis.profiling.core.callgraph;

import java.util.Iterator;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.analysis.profiling.core.callgraph.ICallGraphProvider;
import org.eclipse.tracecompass.analysis.profiling.core.callstack.CallStackAnalysis;
import org.eclipse.tracecompass.internal.analysis.timing.core.segmentstore.SegmentStoreStatisticsDataProvider;
import org.eclipse.tracecompass.internal.tmf.core.model.tree.TmfTreeCompositeDataProvider;
import org.eclipse.tracecompass.tmf.core.dataprovider.IDataProviderFactory;
import org.eclipse.tracecompass.tmf.core.exceptions.TmfAnalysisException;
import org.eclipse.tracecompass.tmf.core.model.tree.ITmfTreeDataModel;
import org.eclipse.tracecompass.tmf.core.model.tree.ITmfTreeDataProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceManager;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
import org.eclipse.tracecompass.tmf.core.trace.experiment.TmfExperiment;

/**
 * {@link SegmentStoreStatisticsDataProvider} factory for the
 * {@link CallGraphStatisticsAnalysis}.
 *
 * @author Loic Prieur-Drevon
 */
public class CallGraphStatisticsDataProviderFactory implements IDataProviderFactory {

    private static final String ID = "org.eclipse.tracecompass.internal.analysis.profiling.core.callgraph.callgraphanalysis.statistics"; //$NON-NLS-1$

    @Override
    public @Nullable ITmfTreeDataProvider<? extends ITmfTreeDataModel> createProvider(ITmfTrace trace) {
        if (trace instanceof TmfExperiment) {
            return TmfTreeCompositeDataProvider.create(TmfTraceManager.getTraceSet(trace), ID);
        }
        Iterator<CallStackAnalysis> csModules = TmfTraceUtils.getAnalysisModulesOfClass(trace, CallStackAnalysis.class).iterator();
        if (!csModules.hasNext()) {
            return null;
        }
        CallStackAnalysis csModule = csModules.next();
        csModule.schedule();
        ICallGraphProvider cgModule = csModule.getCallGraph();
        if (!(cgModule instanceof CallGraphAnalysis)) {
            return null;
        }
        CallGraphStatisticsAnalysis statisticsAnalysis = new CallGraphStatisticsAnalysis();
        try {
            statisticsAnalysis.setTrace(trace);
        } catch (TmfAnalysisException e) {
            statisticsAnalysis.dispose();
            return null;
        }
        statisticsAnalysis.schedule();
        return new SegmentStoreStatisticsDataProvider(trace, statisticsAnalysis, CallGraphStatisticsAnalysis.ID);
    }

}
