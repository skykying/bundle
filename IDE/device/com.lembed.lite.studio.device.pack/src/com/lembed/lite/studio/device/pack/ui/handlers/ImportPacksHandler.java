/*******************************************************************************
 * Copyright (c) 2016 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.pack.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackInstaller;
import com.lembed.lite.studio.device.pack.Messages;
import com.lembed.lite.studio.device.utils.DeviceUIUtils;


/**
 * Handler of importing pack from a .pack or .zip file
 */
public class ImportPacksHandler extends AbstractHandler {
	
	private static final String[] FILTER_NAMES = {
		      "Pack Files (*.pack)", //$NON-NLS-1$
		      "Zip Files (*.zip)"}; //$NON-NLS-1$
	
	private static final String[] FILTER_EXTS = {"*.pack", "*.zip"}; //$NON-NLS-1$ //$NON-NLS-2$


	public ImportPacksHandler() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ICpPackInstaller packInstaller = CpPlugIn.getPackManager().getPackInstaller();
		if(packInstaller == null)
			return null;
		IWorkbenchWindow  window = HandlerUtil.getActiveWorkbenchWindow(event);
		FileDialog dialog = new FileDialog(window.getShell(), SWT.MULTI);
		dialog.setText(Messages.ImportPacksHandler_DialogText); 
		dialog.setFilterNames(FILTER_NAMES);
		dialog.setFilterExtensions(FILTER_EXTS);
		dialog.setFilterPath("C:/"); //$NON-NLS-1$
		if (dialog.open() != null) {
			String[] files = dialog.getFileNames();
			for (String file : files) {
				String fullFileName = DeviceUIUtils.addTrailingSlash(dialog.getFilterPath()) + file;
				packInstaller.importPack(fullFileName);
			}
		}
		
		return null;
	}

}
