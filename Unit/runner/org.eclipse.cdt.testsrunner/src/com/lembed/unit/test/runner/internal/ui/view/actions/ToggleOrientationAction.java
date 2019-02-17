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
import com.lembed.unit.test.runner.internal.ui.view.ResultsView;

/**
 * Toggles the orientation of the view.
 */
public class ToggleOrientationAction extends Action {

	private ResultsView resultsView;
	private ResultsView.Orientation orientation;


	public ToggleOrientationAction(ResultsView resultsView, ResultsView.Orientation orientation) {
		super("", AS_RADIO_BUTTON); //$NON-NLS-1$
		this.resultsView = resultsView;
		if (orientation == ResultsView.Orientation.Horizontal) {
			setText(ActionsMessages.ToggleOrientationAction_horizontal_text);
			setImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/orientation_horizontal.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(TestsRunnerPlugin.getImageDescriptor("dlcl16/orientation_horizontal.gif")); //$NON-NLS-1$
			setHoverImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/orientation_horizontal.gif")); //$NON-NLS-1$
		} else if (orientation == ResultsView.Orientation.Vertical) {
			setText(ActionsMessages.ToggleOrientationAction_vertical_text);
			setImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/orientation_vertical.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(TestsRunnerPlugin.getImageDescriptor("dlcl16/orientation_vertical.gif")); //$NON-NLS-1$
			setHoverImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/orientation_vertical.gif")); //$NON-NLS-1$
		} else if (orientation == ResultsView.Orientation.Auto) {
			setText(ActionsMessages.ToggleOrientationAction_automatic_text);
			setImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/orientation_auto.gif")); //$NON-NLS-1$
			setDisabledImageDescriptor(TestsRunnerPlugin.getImageDescriptor("dlcl16/orientation_auto.gif")); //$NON-NLS-1$
			setHoverImageDescriptor(TestsRunnerPlugin.getImageDescriptor("elcl16/orientation_auto.gif")); //$NON-NLS-1$
		}
		this.orientation = orientation;
	}

	public ResultsView.Orientation getOrientation() {
		return orientation;
	}

	@Override
	public void run() {
		if (isChecked()) {
			resultsView.setOrientation(orientation);
		}
	}

}
