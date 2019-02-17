/*******************************************************************************
 * Copyright (c) 2015 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package com.lembed.codeanalyzer.profiling.launch;

import org.eclipse.core.expressions.PropertyTester;

import com.lembed.codeanalyzer.internal.profiling.launch.provider.launch.ProviderFramework;


/**
 * @since 3.2
 */
public class ProfileCategoryEnablementTester extends PropertyTester {

    public ProfileCategoryEnablementTester() {
    }

    @Override
    public boolean test(Object receiver, String property, Object[] args,
            Object expectedValue) {
        ProfileLaunchShortcut x = null;

        if (args.length == 0)
            return true;

        // See if there is a profile provider for the given category
        x = ProviderFramework.getProfilingProvider((String)args[0]);

        if (x == null)
            return false;
        return true;
    }

}
