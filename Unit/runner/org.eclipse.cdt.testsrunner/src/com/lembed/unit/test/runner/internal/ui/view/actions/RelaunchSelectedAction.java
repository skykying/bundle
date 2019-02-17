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


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.lembed.unit.test.runner.TestsRunnerPlugin;
import com.lembed.unit.test.runner.internal.launcher.ITestsLaunchConfigurationConstants;
import com.lembed.unit.test.runner.internal.ui.view.TestPathUtils;
import com.lembed.unit.test.runner.model.ITestItem;
import com.lembed.unit.test.runner.model.ITestingSession;

/**
 * Launches the new run or debug session for the currently selected items of
 * test hierarchy.
 */
public abstract class RelaunchSelectedAction extends Action {

	private ITestingSession testingSession;
	private TreeViewer treeViewer;


	public RelaunchSelectedAction(ITestingSession testingSession, TreeViewer treeViewer) {
		this.testingSession = testingSession;
		this.treeViewer = treeViewer;
	}

	/**
	 * Returns the launch mode that should be use to run selected test item.
	 * 
	 * @return launch mode
	 */
	protected abstract String getLaunchMode();

	@Override
	public void run() {
		if (testingSession != null) {
			try {
				ILaunch launch = testingSession.getLaunch();
				ILaunchConfigurationWorkingCopy launchConf = launch.getLaunchConfiguration().getWorkingCopy();
				List<String> testsFilterAttr = Arrays.asList(TestPathUtils.packTestPaths(getSelectedTestItems()));
				launchConf.setAttribute(ITestsLaunchConfigurationConstants.ATTR_TESTS_FILTER, testsFilterAttr);
				DebugUITools.launch(launchConf, getLaunchMode());
				return;
			} catch (CoreException e) {
				TestsRunnerPlugin.log(e);
			}
		}
		setEnabled(false);
	}
	
	/**
	 * Returns the currently selected items of test hierarchy.
	 * 
	 * @return array of test items
	 */
	private ITestItem[] getSelectedTestItems() {
		IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
		ITestItem[] result = new ITestItem[selection.size()];
		int resultIndex = 0;
		for (Iterator<?> it = selection.iterator(); it.hasNext();) {
			result[resultIndex] = (ITestItem)it.next();
			++resultIndex;
		}
		return result;
	}

	/**
	 * Sets actual testing session.
	 * 
	 * @param testingSession testing session
	 */
	public void setTestingSession(ITestingSession testingSession) {
		this.testingSession = testingSession;
	}
	
}

