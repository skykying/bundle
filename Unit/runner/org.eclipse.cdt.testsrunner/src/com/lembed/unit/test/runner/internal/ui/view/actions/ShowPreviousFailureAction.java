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
import com.lembed.unit.test.runner.internal.ui.view.TestsHierarchyViewer;

/**
 * Looks for the previous failed test case in tests hierarchy (corresponding to the
 * currently selected one).
 */
public class ShowPreviousFailureAction extends Action {

	private TestsHierarchyViewer testsHierarchyViewer;

	public ShowPreviousFailureAction(TestsHierarchyViewer testsHierarchyViewer) {
		super(ActionsMessages.ShowPreviousFailureAction_text);
		this.testsHierarchyViewer = testsHierarchyViewer;
		setToolTipText(ActionsMessages.ShowPreviousFailureAction_tooltip);
		setDisabledImageDescriptor(TestsRunnerPlugin.getImageDescriptor("dlcl16/show_previous.gif")); //$NON-NLS-1$
		setHoverImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/show_previous.gif")); //$NON-NLS-1$
		setImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/show_previous.gif")); //$NON-NLS-1$
	}

	@Override
	public void run() {
		testsHierarchyViewer.showPreviousFailure();
	}
}

