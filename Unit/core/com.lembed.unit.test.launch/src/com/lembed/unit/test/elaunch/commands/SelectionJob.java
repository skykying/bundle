package com.lembed.unit.test.elaunch.commands;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;

public abstract class SelectionJob extends Job {

	public SelectionJob(String name) {
		super(name);
	}

	static IResource getIResource(Object element) {
		// Adapt the first element to a file.
		if (!(element instanceof IAdaptable)){
			return null;
		}
		IResource res = (IResource) ((IAdaptable) element).getAdapter(IResource.class);
		
		return res;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IStructuredSelection selection = (IStructuredSelection) getProperty(AbstractCommand.SELECTION_PROPERTY);
		
		// progress monitor has as many steps as selected files
		int count = selection.size();
		monitor.beginTask(getName(), count);
		if (monitor.isCanceled()){
			return Status.CANCEL_STATUS;
		}
		
		for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
			IResource res = getIResource(iterator.next());
			if (res == null){
				continue;
			}
			
			runResource(res, new SubProgressMonitor(monitor, 1));
			
			if (monitor.isCanceled()){
				return Status.CANCEL_STATUS;
			}
			
		}
		return Status.OK_STATUS;
	}

	abstract protected void runResource(IResource resource, IProgressMonitor monitor);
}
