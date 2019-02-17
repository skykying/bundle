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
package com.lembed.unit.test.runner.internal.ui.view;

import org.eclipse.debug.core.ILaunch;

import com.lembed.unit.test.runner.launcher.ITestsRunnerProviderInfo;
import com.lembed.unit.test.runner.model.ITestItem;
import com.lembed.unit.test.runner.model.ITestModelAccessor;
import com.lembed.unit.test.runner.model.ITestingSession;

/**
 * Represents a simple testing session which is used for UI when there is no
 * "real" testing sessions to show (e.g. when there was no launched testing
 * session or when all of them were cleared).
 */
public class DummyUISession implements ITestingSession {

	@Override
	public int getCurrentCounter() {
		return 0;
	}

	@Override
	public int getTotalCounter() {
		return 0;
	}
	
	@Override
	public int getCount(ITestItem.Status status) {
		return 0;
	}

	@Override
	public boolean hasErrors() {
		return false;
	}

	@Override
	public boolean wasStopped() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public ITestModelAccessor getModelAccessor() {
		return null;
	}

	@Override
	public ILaunch getLaunch() {
		return null;
	}

	@Override
	public ITestsRunnerProviderInfo getTestsRunnerProviderInfo() {
		return null;
	}

	@Override
	public String getStatusMessage() {
		return ""; //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return "<dummy>"; //$NON-NLS-1$
	}

	@Override
	public void stop() {
	}

}
