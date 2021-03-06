/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.linker.preferences;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

import com.lembed.lite.studio.manager.analysis.editor.linker.Activator;
import com.lembed.lite.studio.manager.analysis.editor.linkerfile.LinkerFileEditorSourceViewerConfiguration;

public class TemplatePreferences extends TemplatePreferencePage implements IWorkbenchPreferencePage {

    protected static class LinkerFileEditorTemplateDialog extends TemplatePreferencePage.EditTemplateDialog {

        public LinkerFileEditorTemplateDialog(Shell arg0, Template arg1, boolean arg2, boolean arg3,
                ContextTypeRegistry arg4) {
            super(arg0, arg1, arg2, arg3, arg4);
        }
        
        protected SourceViewer createViewer(Composite parent) {

            SourceViewer viewer= new SourceViewer(parent, null, null, false, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
            SourceViewerConfiguration configuration = new LinkerFileEditorSourceViewerConfiguration();
            viewer.configure(configuration);         

            return viewer;
        }        
        
    }
    
    public TemplatePreferences(){
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setTemplateStore(Activator.getDefault().getTemplateStore());
        setContextTypeRegistry(Activator.getDefault().getContextTypeRegistry());
        
    } 

    protected Template editTemplate(Template template, boolean edit, boolean isNameModifiable) {
        LinkerFileEditorTemplateDialog dialog= new LinkerFileEditorTemplateDialog(getShell(), template, edit, isNameModifiable, getContextTypeRegistry());
        if (dialog.open() == Window.OK) {
            return dialog.getTemplate();
        }
        return null;
    }
    
    protected boolean isShowFormatterSetting() {
        return false;
    }
       
    
}
