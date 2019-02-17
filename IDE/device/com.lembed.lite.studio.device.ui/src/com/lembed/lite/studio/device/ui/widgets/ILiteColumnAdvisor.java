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

package com.lembed.lite.studio.device.ui.widgets;

import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.ui.tree.IColumnAdvisor;

/**
 * Extends IRteColumnAdvisor with IRteModelController awareness
 */
public interface ILiteColumnAdvisor extends IColumnAdvisor {

	/**
	 * Sets an RTE model controller to be used by the advisor
	 * 
	 * @param ILiteModelController
	 *            controller to use
	 */
	public void setModelController(ILiteModelController modelController);

	/**
	 * Returns RTE model controller used by the widget
	 * 
	 * @return IRteModelController
	 */
	public ILiteModelController getModelController();

}
