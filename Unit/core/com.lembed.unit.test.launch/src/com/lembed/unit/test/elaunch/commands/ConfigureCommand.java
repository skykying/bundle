package com.lembed.unit.test.elaunch.commands;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.lembed.unit.test.elaunch.UnitBuilder;


public class ConfigureCommand extends AbstractCommand {
	
	private static class RunSyncJob extends SelectionJob {

		private final UnitBuilder unitBuilder;
		private Boolean flag = false;

		public RunSyncJob(String name) {
			super(name);
			unitBuilder = new UnitBuilder();
		}

		@Override
		protected void runResource(IResource resource, IProgressMonitor monitor) {
			
			if(flag) {
				return;
			}
			
			//unitBuilder.processResource(resource, monitor);
			IProject project  =  resource.getProject();
			ICProject cproject = CoreModel.getDefault().create(project);
//			IFolder folder = project.getFolder("Folder1");
			
			try {
				project.accept(new IResourceVisitor() {
					@Override
					public boolean visit(IResource resource) throws CoreException {
						log(resource.getFullPath().toString());
						return true;						
					}
				});
			} catch (CoreException e) {
				e.printStackTrace();
			}
			
			//flag = true;
		}
	}
	

	/**
	 * CoreModelUtil class is very important !
	 */
	@Override
	SelectionJob getJob() {
		return new RunSyncJob("make");
	}
	
	private static void log(String msg) {
		System.out.println(msg);
	}

}
