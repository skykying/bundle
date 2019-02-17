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

package com.lembed.lite.studio.device.refclient;

import com.lembed.lite.studio.device.core.CpEnvironmentProvider;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.data.ICpExample;
import com.lembed.lite.studio.device.toolchain.LiteToolChainAdapterFactory;
import com.lembed.lite.studio.device.toolchain.litestudio.LiteStudioToolChainAdapter;

/**
 *  A sample environment provider 
 */
public class RefClientEnvironmentProvider extends CpEnvironmentProvider {


	static public final String REF_CLIENT = "RefClient"; //$NON-NLS-1$
	static private int liteStduioInstalled = -1; // not initialized
	
	public RefClientEnvironmentProvider() {
	}

	@Override
	public String getName() {
		return REF_CLIENT;
	}
	
	static public boolean isLiteStduioToolchainInstalled() {
		if(liteStduioInstalled < 0) {
			String prefix = LiteStudioToolChainAdapter.LITE_STUDIO_TOOLCHAIN_PREFIX;
			liteStduioInstalled = LiteToolChainAdapterFactory.isToolchainInstalled(prefix) ? 1 : 0;
		}
		return liteStduioInstalled > 0;
	}

	@Override
	public void init() {
		// install custom pack installer
		RefClientPackInstaller packInstaller = new RefClientPackInstaller();
		CpPlugIn.getDefault().setPackInstaller(packInstaller);
	}

	@Override
	public boolean isExampleSupported(ICpExample example) {
		// return true to see all examples, even those that are not supported
		return super.isExampleSupported(example);
	}

	
}
