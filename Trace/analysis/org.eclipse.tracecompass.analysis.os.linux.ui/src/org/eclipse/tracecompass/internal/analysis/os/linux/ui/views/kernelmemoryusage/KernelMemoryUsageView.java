/**********************************************************************
 * Copyright (c) 2016, 2017 École Polytechnique de Montréal and others
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 **********************************************************************/
package org.eclipse.tracecompass.internal.analysis.os.linux.ui.views.kernelmemoryusage;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.internal.analysis.os.linux.core.kernelmemoryusage.KernelMemoryUsageDataProvider;
import org.eclipse.tracecompass.internal.analysis.os.linux.ui.views.memory.MemoryUsageView;
import org.eclipse.tracecompass.tmf.ui.viewers.xycharts.linecharts.TmfXYChartSettings;

/**
 * Memory usage view
 *
 * @author Samuel Gagnon
 * @author Mahdi Zolnouri
 * @author Wassim Nasrallah
 */
public class KernelMemoryUsageView extends MemoryUsageView {

    /** ID string */
    public static final @NonNull String ID = "org.eclipse.tracecompass.analysis.os.linux.ui.kernelmemoryusageview"; //$NON-NLS-1$

    /**
     * Constructor
     */
    public KernelMemoryUsageView() {
        super(Messages.MemoryUsageView_title, KernelMemoryUsageDataProvider.ID,
                new TmfXYChartSettings(Messages.MemoryUsageViewer_title, Messages.MemoryUsageViewer_xAxis, Messages.MemoryUsageViewer_yAxis, 1));
    }
}
