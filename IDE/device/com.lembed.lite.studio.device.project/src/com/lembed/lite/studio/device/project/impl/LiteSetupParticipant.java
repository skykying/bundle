/*******************************************************************************
* Copyright (c) 2017 Lembed
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
* LEMBED - adapter for LiteSTUDIO
*******************************************************************************/

package com.lembed.lite.studio.device.project.impl;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexManager;
import org.eclipse.cdt.core.index.IndexerSetupParticipant;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.settings.model.CProjectDescriptionEvent;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionListener;
import org.eclipse.cdt.core.settings.model.ICProjectDescriptionManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.lembed.lite.studio.device.project.CpProjectPlugIn;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.project.utils.ProjectUtils;

/**
 * This class handles project creation event in order to apply LITE configuration
 * data to loaded project.<br>
 * It also postpones project indexing until LITE data applied to the project.
 */
public class LiteSetupParticipant extends IndexerSetupParticipant implements ICProjectDescriptionListener {

	/**
	 * Default constructor that registers this as IndexerSetupParticipant and
	 * ICProjectDescriptionListener
	 */
	public LiteSetupParticipant() {
		IIndexManager indexManager = CCorePlugin.getIndexManager();
		ICProjectDescriptionManager descManager = CCorePlugin.getDefault().getProjectDescriptionManager();
		if (indexManager != null && descManager != null) {
			indexManager.addIndexerSetupParticipant(this);
			descManager.addCProjectDescriptionListener(this, CProjectDescriptionEvent.ALL);
		}
		
	}

	@Override
	public void handleEvent(CProjectDescriptionEvent event) {
		if (event.getEventType() == CProjectDescriptionEvent.LOADED) {
			IProject project = event.getProject();
			if (!LiteProjectNature.hasLiteNature(project)){
				return;
			}

			LiteProjectManager liteProjectManager = CpProjectPlugIn.getLiteProjectManager();
			ILiteProject liteProject = liteProjectManager.createLiteProject(project);
			try {
				liteProject.load();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			
			updateIndex(project);		
		}
	}

	// IndexerSetupParticipant overridden methods
	@Override
	public boolean postponeIndexerSetup(ICProject cproject) {		
		IProject project = cproject.getProject();
		if (!LiteProjectNature.hasLiteNature(project)){
			return false;
		}
		
		LiteProjectManager liteProjectManager = CpProjectPlugIn.getLiteProjectManager();
		ILiteProject liteProject = liteProjectManager.getLiteProject(project);
		if (liteProject == null || !liteProject.isUpdateCompleted()) {
			return true; 
			// postpone indexer until lite data is loaded and updated
		}
		return false;
	}

	public void updateIndex(IProject project) {
		ICProject cproject = ProjectUtils.getCProject(project);
		if (cproject != null) {
			notifyIndexerSetup(cproject);
		}
	}
	
	@SuppressWarnings("unused")
	private static void log(String msg){
		System.out.println(msg);
	}
}
