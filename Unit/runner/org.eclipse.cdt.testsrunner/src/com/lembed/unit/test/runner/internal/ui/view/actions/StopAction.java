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
import com.lembed.unit.test.runner.internal.model.TestingSessionsManager;
import com.lembed.unit.test.runner.model.ITestingSession;

/**
 * Stops running of the active testing session.
 */
public class StopAction extends Action {

	private TestingSessionsManager testingSessionsManager;


	public StopAction(TestingSessionsManager testingSessionsManager) {
		super(ActionsMessages.StopAction_text);
		setToolTipText(ActionsMessages.StopAction_tooltip);
		setDisabledImageDescriptor(TestsRunnerPlugin.getImageDescriptor("dlcl16/stop.gif")); //$NON-NLS-1$
		setHoverImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/stop.gif")); //$NON-NLS-1$
		setImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/stop.gif")); //$NON-NLS-1$
		this.testingSessionsManager = testingSessionsManager;
	}

	@Override
	public void run() {
		ITestingSession activeSession = testingSessionsManager.getActiveSession();
		if (activeSession != null) {
			activeSession.stop();
		}
		setEnabled(false);
	}
	
}

