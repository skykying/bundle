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
package com.lembed.unit.test.runner.launcher;

import java.io.InputStream;

import com.lembed.unit.test.runner.model.ITestModelUpdater;
import com.lembed.unit.test.runner.model.TestingException;


/**
 * The interface for a Tests Runner provider plug-in.
 * 
 * <p>
 * Clients may implement this interface.
 * </p>
 */
public interface ITestsRunnerProvider {

	/**
	 * Returns the list of automatically generated parameters that should be
	 * passed to test module for launching. Usually there are parameters to
	 * configure test module output format and content. If testPaths is not
	 * <code>null</code>, it contains the paths to the test suites and test
	 * cases that should be run during the testing process, so the proper
	 * filters should be generated too.
	 * 
	 * @param testPaths the array of test paths
	 * @return parameters list
	 * @throws TestingException if test parameters cannot be generated
	 */
	public String[] getAdditionalLaunchParameters(String[][] testPaths) throws TestingException;

	/**
	 * Starts the processing of testing results by Tests Runner Plug-in. The
	 * input data are provided via the stream. They should be converted to the
	 * commands for the Model Updater.
	 * 
	 * @param modelUpdater model updater instance
	 * @param inputStream test model output or error data stream
	 * @throws TestingException if there are errors during the testing process
	 * or in data stream
	 */
	public void run(ITestModelUpdater modelUpdater, InputStream inputStream) throws TestingException;
	
}