/*******************************************************************************
 * Copyright (c) 2010, 2014 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Francois Chouinard - Initial API and implementation
 *******************************************************************************/

package org.eclipse.tracecompass.tmf.ui.views;

import org.eclipse.tracecompass.tmf.ui.project.wizards.NewTmfProjectWizard;
import org.eclipse.tracecompass.tmf.ui.views.histogram.HistogramView;
import org.eclipse.tracecompass.tmf.ui.views.statistics.TmfStatisticsView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The tracing perspective definition.
 *
 * @version 1.0
 * @author Francois Chouinard
 */
public class TracingPerspectiveFactory implements IPerspectiveFactory {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    /** The Perspective ID */
    public static final String ID = "org.eclipse.linuxtools.tmf.ui.HypervisorPerspective"; //$NON-NLS-1$

    // Standard TMF views
    private static final String HISTOGRAM_VIEW_ID = HistogramView.ID;

    // Standard Eclipse views
    private static final String PROJECT_VIEW_ID = IPageLayout.ID_PROJECT_EXPLORER;
    private static final String STATISTICS_VIEW_ID = TmfStatisticsView.ID;
    private static final String PROPERTIES_VIEW_ID = IPageLayout.ID_PROP_SHEET;
    private static final String BOOKMARKS_VIEW_ID = IPageLayout.ID_BOOKMARKS;


    // ------------------------------------------------------------------------
    // IPerspectiveFactory
    // ------------------------------------------------------------------------

    @Override
    public void createInitialLayout(IPageLayout layout) {

        // Editor area
        layout.setEditorAreaVisible(true);

        // Create the top left folder
        IFolderLayout topLeftFolder = layout.createFolder("topLeftFolder", IPageLayout.LEFT, 0.15f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
        topLeftFolder.addView(PROJECT_VIEW_ID);

        // Create the top left folder
        IFolderLayout botLeftFolder = layout.createFolder("botLeftFolder", IPageLayout.BOTTOM, 0.55f, "topLeftFolder"); //$NON-NLS-1$
        botLeftFolder.addView(PROPERTIES_VIEW_ID);

        IFolderLayout middleTopFolder = layout.createFolder("middleTopFolder", IPageLayout.TOP, 0.35f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
        middleTopFolder.addView(STATISTICS_VIEW_ID);


        // Create the bottom right folder
        IFolderLayout bottomRightFolder = layout.createFolder(
                "bottomRightFolder", IPageLayout.BOTTOM, 0.55f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$ //$NON-NLS-2$
        bottomRightFolder.addView(HISTOGRAM_VIEW_ID);
        bottomRightFolder.addView(BOOKMARKS_VIEW_ID);

        // Populate menus, etc
        layout.addPerspectiveShortcut(ID);
        layout.addNewWizardShortcut(NewTmfProjectWizard.ID);
    }

}
