/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.analysis.os.linux.ui.perspectives;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.tracecompass.internal.analysis.os.linux.ui.views.cpuusage.CpuUsageView;
import org.eclipse.tracecompass.internal.analysis.os.linux.ui.views.io.diskioactivity.DiskIOActivityView;
import org.eclipse.tracecompass.internal.analysis.os.linux.ui.views.kernelmemoryusage.KernelMemoryUsageView;
import org.eclipse.tracecompass.tmf.ui.project.wizards.NewTmfProjectWizard;
import org.eclipse.tracecompass.tmf.ui.views.histogram.HistogramView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The overview perspective. This perspective is useful for locating problem
 * areas in a kernel trace.
 *
 * @author Matthew Khouzam
 */
public class KernelOverviewPerspectiveFactory implements IPerspectiveFactory {

    /** Perspective ID */
    public static final String ID = "org.eclipse.tracecompass.analysis.os.linux.ui.perspective"; //$NON-NLS-1$

    // LTTng views
    private static final String HISTOGRAM_VIEW_ID = HistogramView.ID;
    private static final String CPU_VIEW_ID = CpuUsageView.ID;
    private static final String MEMORY_VIEW_ID = KernelMemoryUsageView.ID;
    private static final String DISK_VIEW_ID = DiskIOActivityView.ID;

    private static final String PROPERTIES_VIEW_ID = checkNotNull(IPageLayout.ID_PROP_SHEET);

    // Standard Eclipse views
    private static final String PROJECT_VIEW_ID = IPageLayout.ID_PROJECT_EXPLORER;
    private static final String BOOKMARKS_VIEW_ID = IPageLayout.ID_BOOKMARKS;

    @Override
    public void createInitialLayout(IPageLayout layout) {

        layout.setEditorAreaVisible(true);

        // Create the top left folder
        IFolderLayout topLeftFolder = layout.createFolder(
                "topLeftFolder", IPageLayout.LEFT, 0.15f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
        topLeftFolder.addView(PROJECT_VIEW_ID);

        IFolderLayout leftBotFolder = layout.createFolder(
                "leftBotFolder", IPageLayout.BOTTOM, 0.65f, "topLeftFolder"); //$NON-NLS-1$
        leftBotFolder.addView(PROPERTIES_VIEW_ID);

        IFolderLayout topRightFolder = layout.createFolder(
                "topRightFolder", IPageLayout.TOP, 0.35f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
        topRightFolder.addView(CPU_VIEW_ID);
        topRightFolder.addView(DISK_VIEW_ID);
        topRightFolder.addView(MEMORY_VIEW_ID);

        // Create the bottom right folder
        IFolderLayout bottomRightFolder = layout.createFolder(
                "bottomRightFolder", IPageLayout.BOTTOM, 0.50f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
        bottomRightFolder.addView(HISTOGRAM_VIEW_ID);
        bottomRightFolder.addView(BOOKMARKS_VIEW_ID);

        layout.addNewWizardShortcut(NewTmfProjectWizard.ID);
    }

}
