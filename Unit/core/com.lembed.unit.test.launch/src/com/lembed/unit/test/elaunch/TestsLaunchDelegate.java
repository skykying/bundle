/*******************************************************************************
 * Copyright (c) 2011, 2015 Anton Gorenkov.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Anton Gorenkov - initial API and implementation
 *******************************************************************************/
package com.lembed.unit.test.elaunch;

import com.lembed.unit.test.runner.launcher.BaseTestsLaunchDelegate;


/**
 * Launch delegate implementation that redirects its queries to the preferred
 * launch delegate, correcting the arguments attribute (to take into account
 * auto generated test module parameters) and setting up the custom process
 * factory (to handle testing process IO streams).
 */
public abstract class TestsLaunchDelegate extends BaseTestsLaunchDelegate {
	
	
}
