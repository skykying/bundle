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
package com.lembed.lite.studio.managedbuild.cross.llvm;

import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler;
import org.eclipse.cdt.managedbuilder.internal.core.FolderInfo;
import org.eclipse.cdt.managedbuilder.internal.core.MultiFolderInfo;
import org.eclipse.cdt.managedbuilder.internal.core.ResourceConfiguration;

/**
 * The Class AdditionalToolsManagedOptionValueHandler.
 */
@SuppressWarnings("restriction")
public class AdditionalToolsManagedOptionValueHandler extends ManagedOptionValueHandler {

	// ------------------------------------------------------------------------


	@Override
    public boolean handleValue(IBuildObject configuration, IHoldsOptions holder, IOption option, String extraArgument,
			int event) {

		// ManagedOptionValueHandlerDebug.dump(configuration, holder, option,
		// extraArgument, event);

		if (event == EVENT_APPLY) {
			if (configuration instanceof FolderInfo) {
				//FolderInfo oFolderInfo;
				//oFolderInfo = (FolderInfo) configuration;
				// oFolderInfo.setDirty(true); //does not update interface :-(
				// TODO: find the proper sequence to update the interface
				// System.out.println("should update FolderInfo");
				// PlatformUI.getWorkbench().getModalDialogShellProvider().getShell().redraw();

				return false; // should we return true?

			} else if (configuration instanceof ResourceConfiguration) {
				// TODO: find the proper sequence to update the interface
				// System.out
				// .println("should update ResourceConfiguration");

				return false; // should we return true?

			} else if (configuration instanceof MultiFolderInfo) {
				// TODO: find the proper sequence to update the interface
				// System.out
				// .println("should update MultiFolderInfo");

				return false; // should we return true?

			} else {
				LlvmUIPlugin.log("unexpected instanceof configuration " + configuration.getClass().getCanonicalName()); //$NON-NLS-1$
			}

		}
		// The event was not handled, thus return false
		return false;
	}

}
