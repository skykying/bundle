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
import com.lembed.unit.test.runner.internal.ui.view.UIUpdater;

/**
 * Toggles the auto-scroll for tests hierarchy tree.
 */
public class ScrollLockAction extends Action {

	private UIUpdater modelSyncronizer;


	public ScrollLockAction(UIUpdater modelSyncronizer) {
		super(ActionsMessages.ScrollLockAction_name);
		this.modelSyncronizer = modelSyncronizer;
		setToolTipText(ActionsMessages.ScrollLockAction_tooltip);
		setDisabledImageDescriptor(TestsRunnerPlugin.getImageDescriptor("dlcl16/scroll_lock.gif")); //$NON-NLS-1$
		setHoverImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/scroll_lock.gif")); //$NON-NLS-1$
		setImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/scroll_lock.gif")); //$NON-NLS-1$
		setChecked(!this.modelSyncronizer.getAutoScroll());
	}

	@Override
	public void run() {
		modelSyncronizer.setAutoScroll(!isChecked());
	}
}

