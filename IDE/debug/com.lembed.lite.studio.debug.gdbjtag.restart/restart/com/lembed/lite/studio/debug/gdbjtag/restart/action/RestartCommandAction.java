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
package com.lembed.lite.studio.debug.gdbjtag.restart.action;

import com.lembed.lite.studio.debug.gdbjtag.restart.Activator;

import org.eclipse.debug.core.commands.IRestartHandler;
import org.eclipse.debug.ui.actions.DebugCommandAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The Class RestartCommandAction.
 */
public class RestartCommandAction extends DebugCommandAction {

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new restart command action.
	 */
	public RestartCommandAction() {
		setActionDefinitionId(Activator.PLUGIN_ID + ".commands.Restart"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return "Restart"; //$NON-NLS-1$
	}

	@Override
	public String getHelpContextId() {
		return "org.eclipse.debug.ui.disconnect_action_context"; //$NON-NLS-1$
	}

	@Override
	public String getId() {
		return "org.eclipse.debug.ui.debugview.toolbar.disconnect"; //$NON-NLS-1$
	}

	@Override
	public String getToolTipText() {
		return "Reset target and restart debugging"; //$NON-NLS-1$
	}

	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"icons/dlcl16/restart_co.gif"); //$NON-NLS-1$
	}

	@Override
	public ImageDescriptor getHoverImageDescriptor() {
		return null;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"icons/elcl16/restart_co.gif"); //$NON-NLS-1$
	}

	@Override
	protected Class<IRestartHandler> getCommandType() {
		return IRestartHandler.class;
	}

	// ------------------------------------------------------------------------
}
