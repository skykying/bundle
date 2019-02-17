/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.cov.view;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.lembed.codeanalyzer.cov.model.CovFileTreeElement;
import com.lembed.codeanalyzer.cov.model.CovFunctionTreeElement;
import com.lembed.codeanalyzer.cov.model.TreeElement;
import com.lembed.codeanalyzer.cov.parser.CovManager;
import com.lembed.codeanalyzer.cov.parser.SourceFile;
import com.lembed.codeanalyzer.cov.view.annotatedsource.OpenSourceFileAction;
import com.lembed.codeanalyzer.ui.abstractviewers.AbstractSTTreeViewer;
import com.lembed.codeanalyzer.ui.abstractviewers.ISTDataViewersField;



public class CovViewer extends AbstractSTTreeViewer {

    private ISTDataViewersField[] fields;

    /**
     * Constructor
     * @param parent
     */
    public CovViewer(Composite parent) {
        super(parent, SWT.BORDER | SWT.H_SCROLL| SWT.V_SCROLL | SWT.MULTI |
                SWT.FULL_SELECTION);
    }

    @Override
    protected IContentProvider createContentProvider() {
        return CovFileContentProvider.sharedInstance;

    }

    @Override
    public ISTDataViewersField[] getAllFields() {
        if (fields == null) {
            fields = new ISTDataViewersField[] {
                    new FieldName(),
                    new FieldTotalLines(),
                    new FieldInstrumentedLines(),
                    new FieldExecutedLines(),
                    new FieldCoveragePercentage() };
        }
        return fields;
    }

    @Override
    public IDialogSettings getDialogSettings() {
        return com.lembed.codeanalyzer.cov.CovPlugin.getDefault().getDialogSettings();
    }

    @Override
    protected void handleOpenEvent(OpenEvent event) {

        IStructuredSelection selection = (IStructuredSelection) event
        .getSelection();
        TreeElement element = (TreeElement) selection.getFirstElement();

        if (element != null) {
            if (element.getParent() != null) {
                String sourceLoc = ""; //$NON-NLS-1$
                long lineNumber = 0;

                if (element.getClass() == CovFileTreeElement.class) {
                    sourceLoc = element.getName();
                } else if (element.getClass() == CovFunctionTreeElement.class) {
                    sourceLoc = ((CovFunctionTreeElement) element).getSourceFilePath();
                    lineNumber  =((CovFunctionTreeElement)element).getFirstLnNmbr();
                }
                CovManager cvm = (CovManager) this.getInput();
                SourceFile sourceFile = cvm.getSourceFile(sourceLoc);
                if (sourceFile != null) {
                    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                    String binaryLoc = cvm.getBinaryPath();
                    IPath binaryPath = new Path(binaryLoc);
                    IFile binary = root.getFileForLocation(binaryPath);
                    IProject project = null;
                    if (binary != null) {
                        project = binary.getProject();
                    }

                    OpenSourceFileAction.openAnnotatedSourceFile(project,
                            binary, sourceFile, (int)lineNumber);
                }
            }
        }
    }
}
