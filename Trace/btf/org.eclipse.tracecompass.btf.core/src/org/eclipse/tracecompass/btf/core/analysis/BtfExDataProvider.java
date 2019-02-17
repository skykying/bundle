package org.eclipse.tracecompass.btf.core.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.internal.tmf.core.model.xy.AbstractTreeCommonXDataProvider;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateSystemDisposedException;
import org.eclipse.tracecompass.tmf.core.model.YModel;
import org.eclipse.tracecompass.tmf.core.model.filters.SelectionTimeQueryFilter;
import org.eclipse.tracecompass.tmf.core.model.filters.TimeQueryFilter;
import org.eclipse.tracecompass.tmf.core.model.tree.ITmfTreeDataModel;
import org.eclipse.tracecompass.tmf.core.model.tree.ITmfTreeDataProvider;
import org.eclipse.tracecompass.tmf.core.model.xy.IYModel;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;

@SuppressWarnings({ "restriction", "javadoc" })
public class BtfExDataProvider extends AbstractTreeCommonXDataProvider<@NonNull BtfAnalysisModule, @NonNull BtfEntryModel> {

    public static final String TOTAL = "total:"; //$NON-NLS-1$
    public static final int TOTAL_SERIES_TID = -2;

    private BtfAnalysisModule btfAlgModule = null;
    /**
     * This provider's extension point ID.
     *
     * @since 2.4
     */
    public static final String ID = "org.eclipse.tracecompass.btf.core.analysis.BtfProviderFactory"; //$NON-NLS-1$

    @SuppressWarnings("null")
    public BtfExDataProvider(ITmfTrace trace, BtfAnalysisModule analysisModule) {
        super(trace, analysisModule);
        btfAlgModule = analysisModule;
    }

    @Override
    public @NonNull String getId() {
        return ID;
    }

    @Override
    protected @Nullable Map<@NonNull String, @NonNull IYModel> getYModels(@NonNull ITmfStateSystem ss, @NonNull SelectionTimeQueryFilter filter, @Nullable IProgressMonitor monitor) throws StateSystemDisposedException {

        long[] xValues = filter.getTimesRequested();

        try {
            btfAlgModule.queryAll(filter);
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        }

        for (long v : xValues) {
            ITmfStateSystem bs = btfAlgModule.getStateSystem();

        }

        Map<String, IYModel> sEntryValues = new HashMap<>();
        Set<@NonNull Entry<@NonNull Long, @NonNull Integer>> selected = getSelectedEntries(filter).entrySet();
        System.out.println(xValues.length + ">>>>>>>@@@@@@@@@@#@@@@@@@@" + selected.size()); //$NON-NLS-1$
        for (Entry<Long, Integer> entry : selected) {
            Long id = entry.getKey();
            Integer quark = entry.getValue();
            System.out.println(id + "@@@@@@@@@@!@@@@@@@@" + quark); //$NON-NLS-1$
            String name = Integer.toString(entry.getValue());
            double[] xd = getDouble(xValues, id);
            sEntryValues.put(name, new YModel(entry.getKey(), ss.getAttributeName(quark) + ':' + name, xd));
        }

        double[] xd = getDouble(xValues,2L);
        sEntryValues.put("ttt", new YModel(-1, getTrace().getName(), xd)); //$NON-NLS-1$
        return sEntryValues;
    }

    private static double[] getDouble(long[] src, Long x) {
        int len = src.length;
        Random r = new Random();
        double[] dst = new double[len];
        for (int i = 0; i < len; i++) {
            dst[i] = r.nextDouble() * x;
        }
        return dst;
    }

    @Override
    protected @NonNull String getTitle() {
        return "BBB"; //$NON-NLS-1$
    }

    @Override
    protected boolean isCacheable() {
        return false;
    }

    @Override
    protected @NonNull List<@NonNull BtfEntryModel> getTree(@NonNull ITmfStateSystem ss, @NonNull TimeQueryFilter filter, @Nullable IProgressMonitor monitor) throws StateSystemDisposedException {
        if (!(filter instanceof SelectionTimeQueryFilter)) {
            return Collections.emptyList();
        }

        SelectionTimeQueryFilter cFilter = (SelectionTimeQueryFilter) filter;

        try {
            btfAlgModule.queryAll(cFilter);
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        }

        String ATTRIBUTE_TASKS = "Tasks"; //$NON-NLS-1$

        int taskQuark;
        List<BtfEntryModel> entryList = new ArrayList<>();



        try {
            taskQuark = ss.getQuarkAbsolute(ATTRIBUTE_TASKS);
            entryList.add(new BtfEntryModel(getId(taskQuark), -1, ss.getAttributeName(taskQuark), 0, filter.getTimesRequested().length));

            List<@NonNull Integer> subList = ss.getSubAttributes(taskQuark, true);
            for (Integer id : subList) {
                entryList.add(new BtfEntryModel(getId(id), getId(taskQuark), ss.getAttributeName(id), 0, filter.getTimesRequested().length));
            }

        } catch (AttributeNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return entryList;
    }

    public static @Nullable ITmfTreeDataProvider<? extends ITmfTreeDataModel> create(@NonNull ITmfTrace trace) {
        BtfAnalysisModule module = TmfTraceUtils.getAnalysisModuleOfClass(trace, BtfAnalysisModule.class, BtfAnalysisModule.ID);
        if (module != null) {
            module.schedule();
            return new BtfExDataProvider(trace, module);
        }
        return null;
    }

}
