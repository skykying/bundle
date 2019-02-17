/*******************************************************************************
 * Copyright (c) 2011, 2012 Anton Gorenkov 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Anton Gorenkov - initial API and implementation
 *******************************************************************************/
package com.lembed.unit.test.runner.internal.ui.view.actions;


import org.eclipse.jface.action.Action;

import com.lembed.unit.test.runner.TestsRunnerPlugin;
import com.lembed.unit.test.runner.internal.ui.view.ResultsPanel;

/**
 * Toggles the filter for the passed test items.
 */
public class ShowFailedOnlyAction extends Action {

	private ResultsPanel resultsPanel;


	public ShowFailedOnlyAction(ResultsPanel resultsPanel) {
		super("", AS_CHECK_BOX); //$NON-NLS-1$
		this.resultsPanel = resultsPanel;
		setText(ActionsMessages.ShowFailedOnlyAction_text);
		setToolTipText(ActionsMessages.ShowFailedOnlyAction_tooltip);
		setImageDescriptor(TestsRunnerPlugin.getImageDescriptor("obj16/show_failed_only.gif")); //$NON-NLS-1$
		setChecked(resultsPanel.getShowFailedOnly());
	}

	@Override
	public void run() {
		resultsPanel.setShowFailedOnly(isChecked());
	}

}
