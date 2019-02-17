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


import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.viewers.TreeViewer;

import com.lembed.unit.test.runner.model.ITestingSession;

/**
 * Launches the new run session for the currently selected items of test
 * hierarchy.
 */
public class RerunSelectedAction extends RelaunchSelectedAction {

	public RerunSelectedAction(ITestingSession testingSession, TreeViewer treeViewer) {
		super(testingSession, treeViewer);
		setText(ActionsMessages.RerunSelectedAction_text);
		setToolTipText(ActionsMessages.RerunSelectedAction_tooltip);
	}

	@Override
	protected String getLaunchMode() {
		return ILaunchManager.RUN_MODE;
	}
	
}

