/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/

package com.lembed.lite.studio.debug.core.data;

/**
 * Factory to create a manager to handle the SVD path.
 */
public interface ISVDPathManagerFactory {

	// ------------------------------------------------------------------------

	/**
	 * Create the path manager object.
	 * 
	 * @return the data manager object.
	 */
	public ISVDPathManager create();

	/**
	 * Free any resources used by the data manager. (currently not used).
	 */
	public void dispose();

	// ------------------------------------------------------------------------
}
