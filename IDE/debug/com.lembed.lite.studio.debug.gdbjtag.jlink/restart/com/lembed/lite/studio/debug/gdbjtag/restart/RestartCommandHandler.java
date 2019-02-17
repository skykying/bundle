/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic & Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Liviu Ionescu - initial API and implementation
 *      LiteSTUDIO   -  document and bug fixed.
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.restart;

import org.eclipse.debug.core.commands.IRestartHandler;
import org.eclipse.debug.ui.actions.DebugCommandHandler;

/**
 * Command candler for the restart command (to enable key-binding activation).
 * 
 * @since 3.6
 */
public class RestartCommandHandler extends DebugCommandHandler {

	@Override
	protected Class<IRestartHandler> getCommandType() {
		return IRestartHandler.class;
	}
}
