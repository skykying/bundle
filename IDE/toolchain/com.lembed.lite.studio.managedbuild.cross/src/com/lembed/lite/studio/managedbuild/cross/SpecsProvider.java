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

import org.eclipse.cdt.core.language.settings.providers.ILanguageSettingsProvider;
import org.eclipse.cdt.core.language.settings.providers.LanguageSettingsManager;

/**
 * The Class SpecsProvider.
 */
public class SpecsProvider {

	// ------------------------------------------------------------------------

	private static String PROVIDER_ID = CrossGccPlugin.getIdPrefix() + ".GCCBuiltinSpecsDetector"; //$NON-NLS-1$

	// ------------------------------------------------------------------------

	// private static String PROVIDER_ID =
	// "org.eclipse.cdt.managedbuilder.core.GCCBuiltinSpecsDetector";

	/**
	 * Gets the provider.
	 *
	 * @return the provider
	 */
	public static ILanguageSettingsProvider getProvider() {
		ILanguageSettingsProvider p = LanguageSettingsManager.getWorkspaceProvider(PROVIDER_ID);

		return LanguageSettingsManager.getRawProvider(p);// ;
	}

	// private static void clear() {
	// ILanguageSettingsProvider provider = getProvider();
	// ((AbstractBuiltinSpecsDetector) provider).clear();
	// // System.out.println("clear " + provider.getName());
	// }

	// ------------------------------------------------------------------------
}
