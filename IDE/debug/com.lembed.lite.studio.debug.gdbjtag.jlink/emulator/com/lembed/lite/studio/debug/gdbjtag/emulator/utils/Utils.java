package com.lembed.lite.studio.debug.gdbjtag.emulator.utils;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

/**
 * http://codeandme.blogspot.de/2012/10/integrating-custom-builder.html
 * 
 * This class is to look up the simulator project
 * 
 * The build docker can be on another PC, the builder can push the kernel source
 * to remote and listener the SSH event, when the message received, the builder
 * will display and parser the build process. at the end, the builder can pull
 * the remote source to local.
 * 
 * ESP the builder, we must implement the parser to parse the builder message
 * and the error.
 * 
 * 
 * https://github.com/stephenh/mirror --> the project do the sync between two PC.
 * or https://github.com/facebook/watchman
 */
public class Utils {
	public static List<IProject> getCProjects() {
		List<IProject> projectList = new LinkedList<IProject>();
		try {
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			IProject[] projects = workspaceRoot.getProjects();
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				if (project.isOpen() && project.hasNature(CProjectNature.C_NATURE_ID)) {
					projectList.add(project);
				}
			}
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
		return projectList;
	}
}