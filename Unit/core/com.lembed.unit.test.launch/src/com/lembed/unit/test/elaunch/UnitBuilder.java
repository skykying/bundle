package com.lembed.unit.test.elaunch;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

public class UnitBuilder extends IncrementalProjectBuilder {
	public static final String BUILDER_ID = "com.lembed.lite.studio.unit.ui.Builder"; //$NON-NLS-1$

	public static final String[] VALID_EXTENSIONS = { ".cpp", ".cxx", ".c++", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			".cc", ".c", ".txx" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	
	/**
	 * This builder is only instanciated once, no parallel builds are possible
	 * with the same builder!
	 */
	public UnitBuilder() {

	}

	public class DeltaVisitor implements IResourceDeltaVisitor {

		private final IProgressMonitor monitor;

		public DeltaVisitor(IProgressMonitor monitor, IResourceDelta delta) {
			this.monitor = monitor;
		} 

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse
		 * .core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			// ignore all parent elements (projects, folders)
			if (resource instanceof IFile) {
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					// handle added resource
					// processResource(resource, new SubProgressMonitor(monitor,
					// 1));
					break;
				case IResourceDelta.REMOVED:
					// resources are not available any more and therefore the
					// markers were automatically removed
					break;
				case IResourceDelta.CHANGED:
					// handle changed resource
					// processResource(resource, new SubProgressMonitor(monitor,
					// 1));
					break;
				}
			}
			// return true to continue visiting children.
			return true;
		}
	}

	private class ResourceVisitor implements IResourceVisitor {
		private final IProgressMonitor monitor;

		/**
		 * 
		 * @param monitor
		 *            which is already initialized the number of items, this
		 *            visitor gets called for
		 */
		public ResourceVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;

		}

		public boolean visit(IResource resource) throws CoreException {
			if (resource instanceof IFile) {
				IFile file = (IFile) resource;

				// create translation unit and access index
				String fileName = file.getLocation().makeAbsolute().toOSString();

			}
			// return true to continue visiting children.
			return !monitor.isCanceled();
		}

		protected void processFile(IFile file) throws CoreException {

		}

	}

	/**
	 * Counts the relevant resources.
	 * 
	 */
	private class ResourceVisitorCounter extends ResourceVisitor {

		private int count;

		public ResourceVisitorCounter() {
			super(new NullProgressMonitor());
			count = 0;
		}

		@Override
		protected void processFile(IFile file) throws CoreException {
			count++;
		}
		
		

		@Override
		public boolean visit(IResource resource) throws CoreException {
			return super.visit(resource);
		}

		public int getCount() {
			return count;
		}
	}

	/**
	 * 
	 * @param resource
	 * @param monitor
	 *            , new monitor, not yet initialized with beginTask
	 * @throws CoreException
	 */
	public void processResource(IResource resource, IProgressMonitor monitor) {
		IProject project = resource.getProject();
		if (project == null) {
			return;
		}

		// open project if necessary
		if (!project.isOpen()) {
			try {
				project.open(new NullProgressMonitor());

			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (project.exists()) {
				IProjectDescription desc = project.getDescription();
				desc.setName(desc.getName() + ".Unit");
				project.move(desc, true, monitor);
				project.build(project.getActiveBuildConfig(),IncrementalProjectBuilder.FULL_BUILD, monitor);
			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
			// first count all relevant resources including and below resource
			ResourceVisitorCounter visitorCounter = new ResourceVisitorCounter();
			resource.accept(visitorCounter);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}
}