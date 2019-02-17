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
package com.lembed.lite.studio.managedbuild.cross;

import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IOptionApplicability;

/**
 * The Class CommonOptionApplicability.
 */
public class CommonOptionApplicability implements IOptionApplicability {

	// ------------------------------------------------------------------------

	@Override
	public boolean isOptionUsedInCommandLine(IBuildObject configuration, IHoldsOptions holder, IOption option) {

		return isOption(configuration, holder, option);
	}

	@Override
	public boolean isOptionVisible(IBuildObject configuration, IHoldsOptions holder, IOption option) {

		return isOption(configuration, holder, option);
	}

	@Override
	public boolean isOptionEnabled(IBuildObject configuration, IHoldsOptions holder, IOption option) {

		return isOption(configuration, holder, option);
	}

	
	/**
	 * Checks if is option.
	 *
	 * @param configuration the configuration
	 * @param holder the holder
	 * @param option the option
	 * @return true, if is option
	 */
	private static boolean isOption(IBuildObject configuration, IHoldsOptions holder, IOption option) {

		// IToolChain toolchain = (IToolChain) holder;
		// not yet used

		return true;
	}

	// ------------------------------------------------------------------------
}
