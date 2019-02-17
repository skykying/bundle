/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Xavier Raynaud <xavier.raynaud@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.internal.gprof.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeColumn;

import com.lembed.codeanalyzer.internal.gprof.Activator;
import com.lembed.codeanalyzer.internal.gprof.Messages;
import com.lembed.codeanalyzer.internal.gprof.parser.GmonDecoder;
import com.lembed.codeanalyzer.internal.gprof.view.fields.SampleProfField;
import com.lembed.codeanalyzer.ui.abstractview.AbstractSTDataView;
import com.lembed.codeanalyzer.ui.abstractviewers.AbstractSTTreeViewer;


/**
 * This action changes the content provider of
 * the {@link com.lembed.codeanalyzer.internal.gprof.view.GmonView}
 *
 * @author Xavier Raynaud <xavier.raynaud@st.com>
 */
public class SwitchSampleTimeAction extends Action {

    private final AbstractSTDataView view;

    /**
     * Constructor
     * @param name name of the action
     * @param view the Gmon viewer
     */
    public SwitchSampleTimeAction(AbstractSTDataView view) {
        super(Messages.SwitchSampleTimeAction_SWITCH_SAMPLE_TIME, SWT.TOGGLE);
        this.setImageDescriptor(Activator.getImageDescriptor("icons/datetime_obj.gif")); //$NON-NLS-1$
        this.setToolTipText(Messages.SwitchSampleTimeAction_SWITCH_SAMPLE_TIME);
        this.view = view;
    }

    @Override
    public void run() {
        AbstractSTTreeViewer gmonViewer = (AbstractSTTreeViewer)view.getSTViewer();
        GmonDecoder decoder = (GmonDecoder) gmonViewer.getInput();
        if(decoder != null){
            int prof_rate = decoder.getHistogramDecoder().getProfRate();

            if (prof_rate == 0) {
                MessageDialog.openError(view.getSite().getShell(),
                        Messages.SwitchSampleTimeAction_GMON_PROF_RATE_IS_NULL,
                Messages.SwitchSampleTimeAction_GMON_PROF_RATE_IS_NULL_LONG_MSG);
                return;
            }

            TreeColumn tc = gmonViewer.getViewer().getTree().getColumn(1);
            SampleProfField spf = (SampleProfField) tc.getData();
            spf.toggle();
            tc.setText(spf.getColumnHeaderText());
            gmonViewer.getViewer().refresh();
        }
    }


}
