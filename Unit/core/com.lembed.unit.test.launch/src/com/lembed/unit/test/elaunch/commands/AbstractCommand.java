package com.lembed.unit.test.elaunch.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;


public abstract class AbstractCommand extends AbstractHandler {

	static final QualifiedName SELECTION_PROPERTY = new QualifiedName(
			"com.lembed.lite.studio.unit.ui", "JobSelection"); //$NON-NLS-1$ //$NON-NLS-2$
	

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// get selection
		ISelection selection = HandlerUtil.getCurrentSelection(event);		
		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}

		// get the origin project and the unit project from this
		// find the origin and unit project and resync them
		//@2017.3.17
		IStructuredSelection sel = (IStructuredSelection) selection;
		for (Object obj : sel.toArray()) {
			IProject project = (IProject) obj;
			if (project == null) {
				continue;
			}
			
			SelectionJob job = getJob();
			job.setProperty(SELECTION_PROPERTY, sel);
			job.setUser(true);
			job.schedule();
		}

//		//example code port from pack project
//		//@2017.3.17
//		IFile iFile = ProjectUtils.createFile(project, rteConfigName, monitor);
//		iFile.refreshLocal(IResource.DEPTH_ONE, null);
//		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		
		
		//https://www.ibm.com/developerworks/cn/opensource/os-ast/
		//http://www.shenyanchao.cn/blog/2013/06/19/use-eclipse-ast-to-parser-java/
		
		return null;
	}
	
	abstract SelectionJob getJob();
}
