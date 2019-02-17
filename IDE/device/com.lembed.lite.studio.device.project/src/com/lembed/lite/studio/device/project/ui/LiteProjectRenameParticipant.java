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

package com.lembed.lite.studio.device.project.ui;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameArguments;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.project.CpProjectPlugIn;
import com.lembed.lite.studio.device.project.ILiteProject;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.project.impl.LiteProjectManager;
import com.lembed.lite.studio.device.project.utils.ProjectUtils;

public class LiteProjectRenameParticipant extends RenameParticipant {

	IResource resource;
	IProject project;
	ILiteProject liteProject;
	int type;

	private static int lite_version = 1;


	@Override
	protected boolean initialize(Object element) {

		resource = ProjectUtils.getLiteResource(element);
		if (resource == null) {
			return false;
		}

		resource = (IResource) element;
		type = resource.getType();
		project = resource.getProject();
		LiteProjectManager liteProjectManager = CpProjectPlugIn.getLiteProjectManager();
		liteProject = liteProjectManager.getLiteProject(project);
		if (liteProject == null) {
			return false;
		}

		return true;
	}

	@Override
	public String getName() {
		return Messages.LiteProjectRenameParticipant_CMSIS_Lite_project_rename_handler;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context)
			throws OperationCanceledException {
		RefactoringStatus status = new RefactoringStatus();
		try {
			pm.beginTask(Messages.LiteProjectRenameParticipant_CheckingPreconditions, 1);
			IPath path = resource.getProjectRelativePath();
			log(path.toString());
			
			if (type != IResource.PROJECT && path.segment(0).equals(CmsisConstants.LITE)) {
				String msg = Messages.LiteProjectRenameParticipant_RenameOfLiteFolderIsNotAllowed;
				status.merge(RefactoringStatus.createFatalErrorStatus(msg));
			}
		} finally {
			pm.done();
		}
		return status; 
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		if (type != IResource.PROJECT) {
			return null;
		}
		try {
			pm.beginTask(Messages.LiteProjectRenameParticipant_CreatingChange, 1);
			String liteConfigName = project.getName() + CmsisConstants.DOT_RTECONFIG;
			IFile iFile = project.getFile(liteConfigName);
			RenameArguments args = getArguments();
			String newProjectName = args.getNewName();

			if(lite_version>2){
				String newLiteConfigName = newProjectName + CmsisConstants.DOT_RTECONFIG;
				Change change = new RenameResourceAfterProjectChange(iFile.getFullPath(), newProjectName, newLiteConfigName);
				return change;
			}else{
				return null;
			}
		} finally {
			pm.done();
		}
	}

	private static void log(String msg){
		System.out.println(msg);
	}
}
