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

package com.lembed.lite.studio.device.project;

import java.io.File;
import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.variableresolvers.PathVariableResolver;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpPackManager;
import com.lembed.lite.studio.device.core.lite.configuration.ILiteConfiguration;
import com.lembed.lite.studio.device.project.impl.LiteProjectManager;
import com.lembed.lite.studio.device.project.utils.ProjectUtils;

/**
 * The class is responsible for resolving cmsis_pack_root variable.<br>
 * It is contributed to org.eclipse.core.variables.dynamicVariables and
 * org.eclipse.core.resources.variableResolvers extension points
 */
public class CpVariableResolver extends PathVariableResolver implements IDynamicVariableResolver {

	public static final String[] supportedPathVariables = new String[] { CmsisConstants.CMSIS_PACK_ROOT_VAR,
			CmsisConstants.CMSIS_PACK_ROOT, CmsisConstants.CMSIS_LITE, CmsisConstants.CMSIS_LITE_VAR };

	@Override
	public String getValue(String variable, IResource resource) {
		if (variable.equals(CmsisConstants.CMSIS_PACK_ROOT_VAR) || variable.equals(CmsisConstants.CMSIS_PACK_ROOT)) {
			URI packRootURI = getCmsisPackRootURI();
			if (packRootURI != null) {
				String packRoot = packRootURI.toString();
				return packRoot; // a string like "file:/" + CMSIS_PACK_ROOT;
			}
		} else if (resource != null && variable.equals(CmsisConstants.CMSIS_DFP)) {
			IProject project = resource.getProject();
			return getDfpPath(project);
		} else if (variable.equals(CmsisConstants.CMSIS_LITE_VAR) || variable.equals(CmsisConstants.CMSIS_LITE)) {
			return CmsisConstants.EMPTY_STRING;
		}
		return null;
	}

	@Override
	public String[] getVariableNames(String variable, IResource resource) {
		return supportedPathVariables;
	}

	@Override
	public String resolveValue(IDynamicVariable variable, String argument) throws CoreException {
		String varName = variable.getName();
		if (varName.equals(CmsisConstants.CMSIS_LITE))
			return CmsisConstants.EMPTY_STRING; // always resolves to an empty
												// string

		if (argument == null || argument.isEmpty())
			return getCmsisPackRoot();
		if (varName.equals(CmsisConstants.CMSIS_DFP)) {
			// the argument must represent project name
			IProject project = ProjectUtils.getProject(argument);
			return getDfpPath(project);
		}
		return getCmsisPackRoot() + argument;
	}

	static public String getDfpPath(IProject project) {
		if (project == null)
			return null;
		LiteProjectManager liteProjectManager = CpProjectPlugIn.getLiteProjectManager();
		ILiteProject liteProject = liteProjectManager.getLiteProject(project);
		if (liteProject == null)
			return null;
		ILiteConfiguration rteConf = liteProject.getLiteConfiguration();
		if (rteConf == null)
			return null;
		return rteConf.getDfpPath();
	}

	/**
	 * Returns CMSIS pack root folder (the value of
	 * <code>${cmsis_pack_root}</code> variable)
	 * 
	 * @return CMSIS pack root folder as string
	 */
	static public String getCmsisPackRoot() {
		ICpPackManager pm = CpPlugIn.getPackManager();
		if (pm != null) {
			return pm.getCmsisPackRootDirectory();
		}
		return null;
	}

	/**
	 * Returns CMSIS pack root folder (the value of
	 * <code>${cmsis_pack_root}</code> variable)
	 * 
	 * @return CMSIS pack root folder as string with OS directory separators
	 */
	static public String getCmsisPackRootOS() {
		String root = getCmsisPackRoot();
		if (root != null && File.separatorChar != '/') {
			return root.replace('/', File.separatorChar);
		}
		return root;
	}

	/**
	 * Returns CMSIS pack root folder (the value of
	 * <code>${cmsis_pack_root}</code> variable) as URI
	 * 
	 * @return CMSIS pack root folder as URI
	 */
	static public URI getCmsisPackRootURI() {
		ICpPackManager pm = CpPlugIn.getPackManager();
		if (pm != null) {
			return pm.getCmsisPackRootURI();
		}
		return null;
	}

	/**
	 * Replaces <code>${cmsis_pack_root}</code> prefix with actual value
	 * 
	 * @param path
	 *            source path to substitute
	 * @return the resulting string
	 */
	static public String expandCmsisRootVariable(String path) {
		if (path == null || path.isEmpty())
			return path;

		if (path.startsWith(CmsisConstants.CMSIS_PACK_ROOT_VAR)) {
			String root = getCmsisPackRoot();
			if (root != null && !root.isEmpty()) {
				String result = root + path.substring(CmsisConstants.CMSIS_PACK_ROOT_VAR.length());
				return result;
			}
		}
		return path;
	}

	/**
	 * Replaces path prefix with <code>${cmsis_pack_root}</code> if it equals to
	 * the prefix with actual value
	 * 
	 * @param path
	 *            source path to substitute
	 * @return the resulting string
	 */
	static public String insertCmsisRootVariable(String path) {
		if (path == null || path.isEmpty())
			return path;
		String root = getCmsisPackRoot();
		if (root != null && !root.isEmpty()) {
			boolean ignoreCase = File.separatorChar == '\\';
			if (path.regionMatches(ignoreCase, 0, root, 0, root.length())) {
				String result = CmsisConstants.CMSIS_PACK_ROOT_VAR + path.substring(root.length());
				return result;
			}

		}
		return CmsisConstants.CMSIS_LITE_VAR + path;
	}
	
	static public Boolean checkCmsisRootVar(String path) {
		if(path.contains(CmsisConstants.CMSIS_PACK_ROOT_VAR)) {
			return true;
		}
		if(path.contains(CmsisConstants.CMSIS_PACK_ROOT)) {
			return true;
		}
		
		return false;
	}
	
	static public String dynamicTransferCmsisRootVarToOsPath(String varPath) {
		String osPath = varPath;
		String root  = getCmsisPackRoot();
		
		if(osPath.contains(CmsisConstants.CMSIS_PACK_ROOT_VAR)) {
			osPath = varPath.replace(CmsisConstants.CMSIS_PACK_ROOT_VAR, root);
		}
		
		if(osPath.contains(CmsisConstants.CMSIS_PACK_ROOT)) {
			osPath = varPath.replace(CmsisConstants.CMSIS_PACK_ROOT, root);
		}
		
		return osPath;
	}
}
