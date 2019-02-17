/*******************************************************************************
 * Copyright (c) 2017 LEMBED 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.lembed.lite.studio.device.utils;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.core.settings.model.ICStorageElement;
import org.eclipse.cdt.core.settings.model.XmlStorageUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.w3c.dom.Document;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.parser.CpConfigParser;

public class StorageUtils {

	public static ICStorageElement getConfigurationStorage(IFile iFile) {
		ICStorageElement elements = null;

		IProject project = iFile.getProject();
		if (project == null) {
			return null;
		}

		CoreModel model = CoreModel.getDefault();
		ICProjectDescription projDes = model.getProjectDescription(project);
		if (projDes != null) {
			try {
				ICStorageElement storage = projDes.getStorage(CmsisConstants.LITE_STORAGE, true);
				elements = storage;
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		IProgressMonitor monitor = new NullProgressMonitor();
		try {
			project.refreshLocal(IResource.DEPTH_ZERO, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return elements;
	}
	
	public static void saveStorage(ICpConfigurationInfo info, CpConfigParser parser, IFile iFile) {
		Document document = parser.writeToXmlDocument(info);

		IProject project = iFile.getProject();
		if (project == null) {
			return;
		}

		CoreModel model = CoreModel.getDefault();
		ICProjectDescription projDes = model.getProjectDescription(project);
		if (projDes != null) {
			try {
				ICStorageElement storage = projDes.getStorage(CmsisConstants.LITE_STORAGE, true);
				ICStorageElement[] child = storage.getChildren();
				for (ICStorageElement e : child) {
					String name = e.getName();
					switch (name) {
					case CmsisConstants.CONFIGURATION_TAG:
						storage.removeChild(e);
						break;
					default:
						break;
					}
				}
				if (document != null) {
					ICStorageElement nse = XmlStorageUtil.createCStorageTree(document);
					storage.importChild(nse);
				}

				model.setProjectDescription(project, projDes);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
}
