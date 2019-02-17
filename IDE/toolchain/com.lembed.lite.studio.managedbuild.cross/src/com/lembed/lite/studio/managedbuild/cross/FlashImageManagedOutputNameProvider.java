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

import org.eclipse.cdt.managedbuilder.core.IManagedOutputNameProvider;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * The Class FlashImageManagedOutputNameProvider.
 */
public class FlashImageManagedOutputNameProvider implements IManagedOutputNameProvider {

	@Override
    public IPath[] getOutputNames(ITool tool, IPath[] primaryInputNames) {

		String value = null;

		IOption option = tool.getOptionBySuperClassId(Option.OPTION_CREATEFLASH_CHOICE);
		if (option != null) {
			value = (String) option.getValue();
			// System.out.println(value);
		}

		String ext = "unknown"; //$NON-NLS-1$
		if (value != null) {
			if (value.endsWith("." + Option.CHOICE_IHEX)) { //$NON-NLS-1$
				ext = "hex"; //$NON-NLS-1$
			} else if (value.endsWith("." + Option.CHOICE_SREC)) { //$NON-NLS-1$
				ext = "srec"; //$NON-NLS-1$
			} else if (value.endsWith("." + Option.CHOICE_SYMBOLSREC)) { //$NON-NLS-1$
				ext = "symbolsrec"; //$NON-NLS-1$
			} else if (value.endsWith("." + Option.CHOICE_BINARY)) { //$NON-NLS-1$
				ext = "bin"; //$NON-NLS-1$
			}
		}

		IPath[] iPath = new IPath[1];
		iPath[0] = new Path("${BuildArtifactFileBaseName}." + ext); //$NON-NLS-1$
		return iPath;
	}

}
