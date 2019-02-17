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

package com.lembed.lite.studio.device.ui.editors;

import com.lembed.lite.studio.device.core.lite.ILiteModel;
import com.lembed.lite.studio.device.core.lite.LiteModelController;
import com.lembed.lite.studio.device.ui.OpenURL;

/**
 * RteEditor-specific controller for RTE model
 */
public class LiteEditorController extends LiteModelController {

	/**
	 * @param model
	 */
	public LiteEditorController(ILiteModel model) {
		super(model);
	}

	@Override
	public String openUrl(String url) {
		return OpenURL.open(url);
	}

}
