/*******************************************************************************
 * Copyright (c) 2014, 2018 École Polytechnique de Montréal
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tracecompass.btf.core.analysis.BtfEntryModel;
import org.eclipse.tracecompass.btf.core.analysis.BtfExDataProvider;
import org.eclipse.tracecompass.tmf.core.model.filters.SelectedCpuQueryFilter;
import org.eclipse.tracecompass.tmf.core.model.filters.SelectionTimeQueryFilter;
import org.eclipse.tracecompass.tmf.core.model.filters.TimeQueryFilter;
import org.eclipse.tracecompass.tmf.core.model.tree.ITmfTreeDataModel;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.AbstractSelectTreeViewer;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeColumnDataProvider;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.ITmfTreeViewerEntry;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeColumnData;
import org.eclipse.tracecompass.tmf.ui.viewers.tree.TmfTreeViewerEntry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * Tree viewer to display CPU usage information in a specified time range. It
 * shows the process's TID, its name, the time spent on the CPU during that
 * range, in % and absolute value.
 *
 * @author Geneviève Bastien
 */
public class BtfTreeViewer extends AbstractSelectTreeViewer {

    /** Provides label for the CPU usage tree viewer cells */
    protected class CpuLabelProvider extends TreeLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof BtfEntry) {
                BtfEntry obj = (BtfEntry) element;
                if (columnIndex == 0) {
                    return obj.getName();
                } else if (columnIndex == 1) {
                    int tid = obj.getModel().getTid();
                    if (tid == BtfExDataProvider.TOTAL_SERIES_TID) {
                        return Messages.BtfXYViewer_Total;
                    }
                    return Integer.toString(tid);
                } else if (columnIndex == 2) {
                    return String.format(Messages.BtfComposite_TextPercent, 100 * obj.getPercent());
                }
//                else if (columnIndex == 3) {
//                    return NLS.bind(Messages.BtfComposite_TextTime, obj.getModel().getTime());
//                }
            }
            return null;
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            if (columnIndex == 3 && element instanceof BtfEntry) {
                BtfEntry btfEntry = (BtfEntry) element;
                BtfEntryModel model = btfEntry.getModel();
                int tid = model.getTid();
                if (tid < 0) {
                    return getLegendImage(BtfExDataProvider.TOTAL + model.getName());
                }
                if (isChecked(element)) {
                    ITmfTreeViewerEntry parent =  btfEntry.getParent();
                    if(parent instanceof BtfEntry) {
                        BtfEntry bentry = (BtfEntry) parent;
                        return getLegendImage(bentry.getModel().getName() + ':' + model.getTid());
                    }
                }
            }
            return null;
        }
    }

    /**
     * Constructor
     *
     * @param parent
     *            The parent composite that holds this viewer
     */
    public BtfTreeViewer(Composite parent) {
        super(parent, 3, BtfExDataProvider.ID);
        setLabelProvider(new CpuLabelProvider());
    }

    /**
     * Expose the {@link AbstractSelectTreeViewer#updateContent} method to the
     * {@link CpuUsageView}.
     */
    @Override
    protected void updateContent(long start, long end, boolean isSelection) {
        super.updateContent(start, end, isSelection);
    }

    // ------------------------------------------------------------------------
    // Operations
    // ------------------------------------------------------------------------

    @Override
    protected @Nullable TimeQueryFilter getFilter(long start, long end, boolean isSelection) {
        long newStart = Long.max(start, getStartTime());
        long newEnd = Long.min(end, getEndTime());

        if (isSelection || newEnd < newStart) {
            return null;
        }
        return new SelectionTimeQueryFilter(newStart, newEnd, 2, Collections.emptyList());
    }

    @Override
    protected ITmfTreeViewerEntry modelToTree(long start, long end, List<ITmfTreeDataModel> model) {
        double time = end - start;

        Map<Long, TmfTreeViewerEntry> map = new HashMap<>();
        TmfTreeViewerEntry root = new TmfTreeViewerEntry(""); //$NON-NLS-1$
        map.put(-1L, root);

        for (BtfEntryModel entryModel : Iterables.filter(model, BtfEntryModel.class)) {
            BtfEntry cpuUsageEntry = new BtfEntry(entryModel, entryModel.getTime() / time);
            map.put(entryModel.getId(), cpuUsageEntry);

            TmfTreeViewerEntry parent = map.get(entryModel.getParentId());
            if (parent != null) {
                parent.addChild(cpuUsageEntry);
            }
        }
        return root;
    }

    @Override
    protected ITmfTreeColumnDataProvider getColumnDataProvider() {
        return () -> {
            ImmutableList.Builder<TmfTreeColumnData> columns = ImmutableList.builder();

            columns.add(createColumn(Messages.BtfComposite_ColumnProcess, Comparator.comparing(BtfEntry::getName)));

//            Comparator<BtfEntry> tidCompare = Comparator.comparingInt(c -> c.getModel().getTid());
//            columns.add(createColumn(Messages.BtfComposite_ColumnTID, tidCompare));

            TmfTreeColumnData percentColumn = createColumn(Messages.BtfComposite_ColumnPercent, Comparator.comparingDouble(BtfEntry::getPercent));
            percentColumn.setPercentageProvider(data -> ((BtfEntry) data).getPercent());
            columns.add(percentColumn);

            Comparator<BtfEntry> timeCompare = Comparator.comparingLong(c -> c.getModel().getTime());
            columns.add(createColumn(Messages.BtfComposite_ColumnTime, timeCompare));

            columns.add(new TmfTreeColumnData(Messages.BtfComposite_ColumnLegend));

            return columns.build();
        };

    }
}
