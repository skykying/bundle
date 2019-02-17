/*******************************************************************************
 * Copyright (c) 2009-2015 STMicroelectronics and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *    Red Hat Inc. - ongoing maintenance
 *******************************************************************************/
package com.lembed.codeanalyzer.cov.view;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.lembed.codeanalyzer.cov.CovPlugin;
import com.lembed.codeanalyzer.cov.action.SwitchContentProviderAction;
import com.lembed.codeanalyzer.cov.parser.CovManager;
import com.lembed.codeanalyzer.cov.parser.SourceFile;
import com.lembed.codeanalyzer.cov.view.annotatedsource.OpenSourceFileAction;
import com.lembed.codeanalyzer.ui.abstractview.AbstractSTDataView;
import com.lembed.codeanalyzer.ui.abstractviewers.AbstractSTViewer;
import com.lembed.codeanalyzer.ui.abstractviewers.TreeColumnViewerFilter;
import com.lembed.codeanalyzer.ui.actions.STExportToCSVAction;
import com.lembed.codeanalyzer.ui.charts.actions.ChartAction;


public class CovView extends AbstractSTDataView {

    private String defaultCSVPath = "coverage.csv"; //$NON-NLS-1$

    private final static String COV_VIEW_ID = "com.lembed.codeanalyzer.gcov.view"; //$NON-NLS-1$
    
    private Label label;
    private Text fFilterText;
    private TreeColumnViewerFilter fViewerFilter;

    private Action folderAction;
    private Action fileAction;
    private Action functionAction;

    @Override
    protected AbstractSTViewer createAbstractSTViewer(Composite parent) {
        return new CovViewer(parent);
    }

    @Override
    protected void contributeToToolbar(IToolBarManager manager) {
        manager.add(new Separator());
        manager.add(new Separator());
        manager.add(folderAction);
        manager.add(fileAction);
        manager.add(functionAction);
        manager.add(new Separator());
        manager.add(new ChartAction(getViewSite().getShell(), getSTViewer()));
    }

    @Override
    protected void createActions() {
        super.createActions();
        folderAction = new SwitchContentProviderAction(Messages.CovView_sort_coverage_per_folder,
                "icons/directory_obj.gif", //$NON-NLS-1$
                getSTViewer().getViewer(), CovFolderContentProvider.sharedInstance);

        fileAction = new SwitchContentProviderAction(Messages.CovView_sort_coverage_per_file, "icons/c_file_obj.gif", //$NON-NLS-1$
                getSTViewer().getViewer(), CovFileContentProvider.sharedInstance);
        fileAction.setChecked(true);

        functionAction = new SwitchContentProviderAction(Messages.CovView_sort_coverage_per_function,
                "icons/function_obj.gif", //$NON-NLS-1$
                getSTViewer().getViewer(), CovFunctionContentProvider.sharedInstance);
    }

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        GridLayout l = (GridLayout) parent.getLayout();
        l.horizontalSpacing = 0;
        l.verticalSpacing = 0;
        l.marginHeight = 0;
        l.marginWidth = 0;
        fViewerFilter = new TreeColumnViewerFilter((TreeViewer) getSTViewer().getViewer(),
                getSTViewer().getAllFields()[0], true);
        getSTViewer().getViewer().addFilter(fViewerFilter);
    }

    @Override
    protected void createTitle(Composite parent) {
        label = new Label(parent, SWT.WRAP);
        GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 1, 1);
        label.setLayoutData(data);

        fFilterText = new Text(parent, SWT.BORDER | SWT.SINGLE | SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
        fFilterText.setMessage(Messages.CovView_type_filter_text);
        fFilterText.setToolTipText(Messages.CovView_filter_by_name);
        fFilterText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        fFilterText.addModifyListener(e -> {
            String text = fFilterText.getText();
            fViewerFilter.setMatchingText(text);
        });
    }

    private static void setCovViewTitle(CovView view, String title, String binaryPath, String timestamp) {
        String viewText = NLS.bind(Messages.CovView_view_title, new Object[] { title, binaryPath, timestamp });
        view.label.setText(viewText);
        view.label.getParent().layout(true);
    }

    public static void displayCovDetailedResult(String binaryPath, String gcda) {
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IFile binary = root.getFileForLocation(new Path(binaryPath));
            IProject project = null;
            if (binary != null) {
                project = binary.getProject();
            }

            // parse and process coverage data
            CovManager cvrgeMnger = new CovManager(binaryPath, project);
            List<String> gcdaPaths = new LinkedList<>();
            gcdaPaths.add(gcda);
            cvrgeMnger.processCovFiles(gcdaPaths, gcda);
            // generate model for view
            cvrgeMnger.fillGcovView();

            for (SourceFile sf : cvrgeMnger.getSourceMap().values()) {
                OpenSourceFileAction.openAnnotatedSourceFile(project, binary, sf, 0);
            }
        } catch (CoreException | IOException e) {
            reportError(e);
        }
    }

    public static CovView displayCovResults(String binaryPath, String gcda) {
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IFile binary = root.getFileForLocation(new Path(binaryPath));
            IProject project = null;
            if (binary != null) {
                project = binary.getProject();
            }

            // parse and process coverage data
            CovManager cvrgeMnger = new CovManager(binaryPath, project);
            List<String> gcdaPaths = cvrgeMnger.getGCDALocations();
            cvrgeMnger.processCovFiles(gcdaPaths, gcda);
            // generate model for view
            cvrgeMnger.fillGcovView();
            // load an Eclipse view
            Date date = new Date(0);
            Date dateCandidate;
            for (String file : gcdaPaths) {
                dateCandidate = new Date(new File(file).lastModified());
                if (dateCandidate.after(date)) {
                    date = dateCandidate;
                }
            }
            String timestamp = DateFormat.getInstance().format(date);
            return displayCovResults(cvrgeMnger, timestamp);
        } catch (InterruptedException | IOException | CoreException e) {
            reportError(e);
        }

        return null;
    }

    private static void reportError(Exception ex) {
        final String message = NLS.bind(Messages.CovView_error_message, ex.getMessage());
        Status status = new Status(IStatus.ERROR, CovPlugin.PLUGIN_ID, IStatus.ERROR, message, ex);

        CovPlugin.getDefault().getLog().log(status);
        PlatformUI.getWorkbench().getDisplay().asyncExec(() -> {
            Shell s = PlatformUI.getWorkbench().getDisplay().getActiveShell();
            MessageDialog.openError(s, Messages.CovView_parsing_error, message);
        });
    }

    /**
     * Used by Test engine and OpenSerAction
     * @param cvrgeMnger
     * @throws PartInitException
     */
    private static CovView displayCovResults(CovManager cvrgeMnger, String timestamp) throws PartInitException {
        // load an Eclipse view
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        CovView cvrgeView = (CovView) page.showView(COV_VIEW_ID); //$NON-NLS-1$

        // view title
        CovView.setCovViewTitle(cvrgeView, Integer.toString((int) cvrgeMnger.getNbrPgmRuns()),
                                cvrgeMnger.getBinaryPath(), timestamp);

        // load the controller
        cvrgeView.setInput(cvrgeMnger);
        CovViewer stviewer = (CovViewer) cvrgeView.getSTViewer();
        stviewer.getViewer().expandToLevel(2);
        return cvrgeView;
    }

    @Override
    protected IAction createExportToCSVAction() {
        IAction action = new STExportToCSVAction(this.getSTViewer()) {
            @Override
            public void run() {
                Object o = getSTViewer().getInput();
                if (o instanceof CovManager) {
                    getExporter().setFilePath(defaultCSVPath);
                }
                super.run();
            }
        };
        return action;
    }
}
