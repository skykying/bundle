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
package com.lembed.lite.studio.manager.analysis.editor.yaml.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.lembed.lite.studio.manager.analysis.editor.yaml.Activator;
import com.lembed.lite.studio.manager.analysis.editor.yaml.preferences.tasktag.IStatusChangeListener;
import com.lembed.lite.studio.manager.analysis.editor.yaml.preferences.tasktag.TaskTagConfigurationBlock;

public class TaskTagPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    public TaskTagPreferencePage(){
        
    }
    
    public void init(IWorkbench workbench) {

        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("YEdit Task tag preferences");
        
    }

    @Override
    protected Control createContents(Composite parent) {

        Composite composite= new Composite(parent, SWT.NONE);
        GridLayout layout= new GridLayout();
        layout.marginHeight= 0;
        layout.marginWidth= 0;
        composite.setLayout(layout);
        composite.setFont(parent.getFont());        
        
        TaskTagConfigurationBlock configBlock = new TaskTagConfigurationBlock(getNewStatusChangedListener(), getPreferenceStore());

        configBlock.createContents(composite);
        return composite;
        
    }
    

    /**
     * Returns a new status change listener that calls {@link #setPreferenceContentStatus(IStatus)}
     * when the status has changed
     * @return The new listener
     */
    protected IStatusChangeListener getNewStatusChangedListener() {
        return new IStatusChangeListener() {
            public void statusChanged(IStatus status) {
                return;
            }
        };
    }    




}
