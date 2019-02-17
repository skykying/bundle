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
package com.lembed.lite.studio.managedbuild.cross.llvm.xpi;

import java.util.Map;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.runtime.CoreException;

import com.lembed.lite.studio.core.LiteProjectPacksStorage;
import com.lembed.lite.studio.debug.core.data.ICProjectExtraDataManager;
import com.lembed.lite.studio.managedbuild.cross.llvm.LlvmUIPlugin;

/**
 * The Class CProjectExtraDataManager.
 */
public class CProjectExtraDataManager implements ICProjectExtraDataManager {

	// ------------------------------------------------------------------------

	private static final CProjectExtraDataManager fgInstance;

	static {
		// Required via static, to simplify synchronizations
		fgInstance = new CProjectExtraDataManager();
	}

	/**
	 * Gets the single instance of CProjectExtraDataManager.
	 *
	 * @return single instance of CProjectExtraDataManager
	 */
	public static CProjectExtraDataManager getInstance() {
		return fgInstance;
	}

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new c project extra data manager.
	 */
	public CProjectExtraDataManager() {
		LlvmUIPlugin.log("CProjectExtraDataManager()"); //$NON-NLS-1$
	}

	// ------------------------------------------------------------------------

	@Override
	public Map<String, String> getExtraProperties(IConfiguration config) {

		try {
			LiteProjectPacksStorage storage = new LiteProjectPacksStorage(config);
			return storage.getOptions();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null; // No extra properties
	}

	// ------------------------------------------------------------------------
}
