/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Lembed Electronic - for LiteSTUDIO
 *******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag;

import org.eclipse.debug.core.ILaunchConfiguration;

@SuppressWarnings("javadoc")
public interface ILaunchConfigurationProvider {

	public ILaunchConfiguration getLaunchConfiguration();

}
