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

import com.lembed.lite.studio.debug.core.data.ICProjectExtraDataManager;
import com.lembed.lite.studio.debug.core.data.ICProjectExtraDataManagerFactory;

/**
 * A factory for creating CProjectExtraDataManager objects.
 */
public class CProjectExtraDataManagerFactory implements ICProjectExtraDataManagerFactory {

	// Create a DataManager object
	@Override
	public ICProjectExtraDataManager create() {
		return CProjectExtraDataManager.getInstance();
	}

	@Override
	public void dispose() {
		
	}

}
