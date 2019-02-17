/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.llink.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;

// toolbar:org.eclipse.debug.ui.main.toolbar?after=additions

/**
 * The Class ButtonResetHandler.
 */
public class ButtonResetHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		LlinkPlugin.log("ButtonResetHandler.execute(" + event + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}

}
