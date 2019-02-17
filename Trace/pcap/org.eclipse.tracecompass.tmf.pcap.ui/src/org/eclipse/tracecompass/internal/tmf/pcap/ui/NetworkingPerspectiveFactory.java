/*******************************************************************************
 * Copyright (c) 2014 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Vincent Perot - Initial API and implementation
 *******************************************************************************/

package org.eclipse.tracecompass.internal.tmf.pcap.ui;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.search.ui.NewSearchUI;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.internal.tmf.pcap.ui.stream.StreamListView;
import org.eclipse.tracecompass.tmf.ui.project.wizards.NewTmfProjectWizard;
import org.eclipse.tracecompass.tmf.ui.views.colors.ColorsView;
import org.eclipse.tracecompass.tmf.ui.views.filter.FilterView;
import org.eclipse.tracecompass.tmf.ui.views.histogram.HistogramView;
import org.eclipse.tracecompass.tmf.ui.views.statistics.TmfStatisticsView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/**
 * The networking perspective definition.
 *
 * @author Vincent Perot
 */
public class NetworkingPerspectiveFactory implements IPerspectiveFactory {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    /** The Perspective ID */
    public static final String ID = "org.eclipse.linuxtools.tmf.pcap.ui.perspective.network"; //$NON-NLS-1$

    // Views
    private static final String PROJECT_VIEW_ID = checkNotNull(IPageLayout.ID_PROJECT_EXPLORER);
    private static final String PROPERTIES_VIEW_ID = checkNotNull(IPageLayout.ID_PROP_SHEET);
    private static final String BOOKMARKS_VIEW_ID = checkNotNull(IPageLayout.ID_BOOKMARKS);
    private static final String FILTER_VIEW_ID = FilterView.ID;
    private static final String HISTOGRAM_VIEW_ID = HistogramView.ID;
    private static final String STATISTICS_VIEW_ID = TmfStatisticsView.ID;
    private static final String COLOR_VIEW_ID = ColorsView.ID;
    private static final String STREAM_LIST_VIEW_ID = StreamListView.ID;

    // ------------------------------------------------------------------------
    // IPerspectiveFactory
    // ------------------------------------------------------------------------

    @Override
    public void createInitialLayout(@Nullable IPageLayout layout) {

        if (layout == null) {
            return;
        }

        // Editor area
        layout.setEditorAreaVisible(true);

        // Create the top left folder
        IFolderLayout topLeftFolder = layout.createFolder("topLeftFolder", IPageLayout.LEFT, 0.15f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
        topLeftFolder.addView(PROJECT_VIEW_ID);

        // Create the top left folder
        IFolderLayout botLeftFolder = layout.createFolder(
                "botLeftFolder", IPageLayout.BOTTOM, 0.55f, "topLeftFolder"); //$NON-NLS-1$
        botLeftFolder.addView(PROPERTIES_VIEW_ID);

        // Create the middle right folder
        IFolderLayout middleTopFolder = layout.createFolder("middleTopFolder", IPageLayout.TOP, 0.35f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
        middleTopFolder.addView(FILTER_VIEW_ID);
        middleTopFolder.addView(STATISTICS_VIEW_ID);
        middleTopFolder.addView(COLOR_VIEW_ID);
        middleTopFolder.addView(STREAM_LIST_VIEW_ID);

        // Create the bottom right folder
        IFolderLayout bottomRightFolder = layout.createFolder("bottomRightFolder", IPageLayout.BOTTOM, 0.55f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$ //$NON-NLS-2$
        bottomRightFolder.addView(HISTOGRAM_VIEW_ID);
        bottomRightFolder.addView(BOOKMARKS_VIEW_ID);

        // Populate menus, etc
        layout.addPerspectiveShortcut(ID);
        layout.addNewWizardShortcut(NewTmfProjectWizard.ID);

        layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);

     // views - build console
        layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);

        // views - searching
        layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);

        // views - standard workbench
        layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
        layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);

    }

}
