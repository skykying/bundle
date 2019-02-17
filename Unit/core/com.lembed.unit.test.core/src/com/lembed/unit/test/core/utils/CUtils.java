package com.lembed.unit.test.core.utils;

import org.eclipse.cdt.core.CCProjectNature;
import org.eclipse.cdt.core.CProjectNature;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.CoreModelUtil;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.core.internal.resources.ProjectDescription;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

/**
 * Utility methods for working with c/cpp files / projects.
 * 
 * @author Eric Van Dewoestine
 */
public class CUtils {

	public static final String NATURE_C = CProjectNature.C_NATURE_ID;
	public static final String NATURE_CPP = CCProjectNature.CC_NATURE_ID;

	/**
	 * Gets a c project by name.
	 * 
	 * @param project
	 *            The name of the project.
	 * @return The project.
	 */
	public static ICProject getCProject(String project) throws Exception {
		return getCProject(ProjectUtils.getProject(project, true));
	}

	/**
	 * Gets a c project from the supplied IProject.
	 * 
	 * @param project
	 *            The IProject.
	 * @return The c project.
	 */
	public static ICProject getCProject(IProject project) throws Exception {
		if (ProjectUtils.getPath(project) == null) {

		}

		if (!project.hasNature(NATURE_C) && !project.hasNature(NATURE_CPP)) {

		}

		ICProject cproject = CoreModel.getDefault().create(project);
		if (cproject == null || !cproject.exists()) {
			return null;
		}
		
		return cproject;
	}


	/**
	 * Finds a compilation unit by looking in all the java project of the supplied
	 * name.
	 * 
	 * @param project
	 *            The name of the project to locate the file in.
	 * @param file
	 *            The file to find.
	 * @return The compilation unit. or null
	 */
	public static ITranslationUnit getTranslationUnit(String project, String file) throws Exception {
		ICProject cproject = getCProject(project);
		ITranslationUnit src = getTranslationUnit(cproject, file);
		if (src == null || !src.exists()) {

		}
		return src;
	}
	
	/**
	 * get ITranslationUnit from file
	 * 
	 * @param file
	 * @return ITranslationUnit or null
	 */
	public static ITranslationUnit getTranslationUnit(IFile file) {
		ITranslationUnit tu= (ITranslationUnit) CoreModel.getDefault().create(file);		
		return tu;
	}
	
	/**
	 * get IASTTranslationUnit from a file
	 * 
	 * @param file
	 * @return IASTTranslationUnit or null.
	 * @throws CoreException
	 */
	public static IASTTranslationUnit getASTTranslationUnit(IFile file) throws CoreException {
		return getTranslationUnit(file).getAST();
	}

	/**
	 * Gets the compilation unit from the supplied project.
	 * 
	 * @param project
	 *            The project.
	 * @param file
	 *            The absolute path to the file.
	 * @return The compilation unit or null if not found.
	 */
	public static ITranslationUnit getTranslationUnit(ICProject project, String file) throws Exception {
		Path path = new Path(ProjectUtils.getFilePath(project.getProject(), file));
		ITranslationUnit src = CoreModelUtil.findTranslationUnitForLocation(path, project);
		if (src == null || !src.exists()) {
			return null;
		}
		return src;
	}
	
	public static ITranslationUnit getTranslationUnit(ICProject project, IResource resource) throws Exception {
		Path path = new Path(resource.getLocationURI().getPath());
		ITranslationUnit src = CoreModelUtil.findTranslationUnitForLocation(path, project);
		if (src == null || !src.exists()) {
			return null;
		}
		return src;
	}
}