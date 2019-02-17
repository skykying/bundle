/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.ui.console;

import org.eclipse.ui.console.IConsoleFactory;

/**
 * Console factory to open a console via user interface
 */
public class LiteConsoleFactory implements IConsoleFactory {

	@Override
	public void openConsole() {
		LiteConsole liteConsole = LiteConsole.openConsole(LiteConsole.BASE_NAME);
		LiteConsole.showConsole(liteConsole);
	}

}
