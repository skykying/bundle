/*******************************************************************************
* Copyright (c) 2017 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
* Lembed Electronic - modife for liteSTUDIO
*******************************************************************************/

package com.lembed.lite.studio.device.project.impl;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.lembed.lite.studio.device.common.CmsisConstants;

/**
 *
 */
public class LiteProjectNature implements IProjectNature {

	private IProject project = null;

	public LiteProjectNature() {
	}

	@Override
	public void configure() throws CoreException {
		// does nothing
	}

	@Override
	public void deconfigure() throws CoreException {
		// does nothing
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;

	}

	/**
	 * Checks if supplied project has RteNature
	 * 
	 * @param project
	 *            IProject to test
	 * @return true if RteNature is installed for this project
	 */
	public static boolean hasLiteNature(IProject project) {
		try {
			if (project != null && project.isOpen() && project.hasNature(CmsisConstants.LITE_NATURE_ID))
				return true;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

}
