/*******************************************************************************
 * Copyright (c) 2012 Anton Gorenkov 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Anton Gorenkov - initial API and implementation
 *******************************************************************************/
package com.lembed.unit.test.runner.launcher;

/**
 * Constants used for attributes in CDT Tests Runner launch configurations.
 *
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ITestsRunnerConstants {

	/**
	 * Tests Runner launch configuration type.
	 */
	public static final String LAUNCH_CONFIGURATION_TYPE_ID = "com.lembed.unit.test.runner.launch.CTestsRunner"; //$NON-NLS-1$

	/**
	 * Specifies the default launch delegate for a Tests Run session.
	 */
	public static final String PREFERRED_RUN_TESTS_LAUNCH_DELEGATE = "com.lembed.unit.test.runner.launch.runTests"; //$NON-NLS-1$
	
    /**
	 * Specifies the default launch delegate for a Tests Debug session.
	 */
    public static final String PREFERRED_DEBUG_TESTS_LAUNCH_DELEGATE = "com.lembed.unit.test.runner.launch.dsf.runTests"; //$NON-NLS-1$

    /**
	 * Specifies the ID of the Tests Runner main view for browsing results.
	 */
    public static final String TESTS_RUNNER_RESULTS_VIEW_ID = "com.lembed.unit.test.runner.resultsview"; //$NON-NLS-1$
}
