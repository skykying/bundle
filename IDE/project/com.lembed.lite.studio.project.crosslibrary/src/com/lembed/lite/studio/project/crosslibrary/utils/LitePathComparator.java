/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.project.crosslibrary.utils;

import java.util.Comparator;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.utils.AlnumComparator;

/**
 * Comparator that sorts collection of include or library paths<br>
 * Project - local includes are preceding those from CMSIS pack root folder on
 * the top<br>
 * Newer versions are preceding the older ones
 */
public class LitePathComparator implements Comparator<String> {

	public LitePathComparator() {
	}

	@Override
	public int compare(String arg0, String arg1) {
		if (arg0 == null || arg1 == null) {
			return AlnumComparator.alnumCompare(arg0, arg1); // should actually
																// never happen
		}

		if (arg0.startsWith(CmsisConstants.LITE)) {
			if (arg1.startsWith(CmsisConstants.LITE)) {
				return AlnumComparator.alnumCompare(arg0, arg1);
			}
			return -1;
		} else if (arg1.startsWith(CmsisConstants.LITE)) {
			return 1;
		}

		// for non-local paths use descending order (puts newer version above
		// older)
		return AlnumComparator.alnumCompare(arg1, arg0); //
	}
}
