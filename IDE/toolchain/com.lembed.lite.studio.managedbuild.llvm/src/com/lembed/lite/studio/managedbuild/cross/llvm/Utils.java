/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.managedbuild.cross.llvm;

import com.lembed.lite.studio.core.EclipseUtils;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IBuildObject;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IFileInfo;
import org.eclipse.cdt.managedbuilder.core.IFolderInfo;
import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IResourceInfo;
import org.eclipse.cdt.managedbuilder.core.IToolChain;

/**
 * The Class Utils.
 */
public class Utils {

	// ------------------------------------------------------------------------

	/** The Constant BUILD_ARTEFACT_TYPE. */
	public static final String BUILD_ARTEFACT_TYPE = "org.eclipse.cdt.build.core.buildArtefactType"; //$NON-NLS-1$
	
	/** The Constant BUILD_ARTEFACT_TYPE_EXE. */
	public static final String BUILD_ARTEFACT_TYPE_EXE = BUILD_ARTEFACT_TYPE + ".exe"; //$NON-NLS-1$
	
	/** The Constant BUILD_ARTEFACT_TYPE_STATICLIB. */
	public static final String BUILD_ARTEFACT_TYPE_STATICLIB = BUILD_ARTEFACT_TYPE + ".staticLib"; //$NON-NLS-1$

	/**
	 * Extracts a resource info from a build object. If no resource info can be
	 * found, it returns null.
	 * 
	 * @param configuration IBuildObject
	 * @return IResourceInfo
	 */
	public static IResourceInfo getResourceInfo(IBuildObject configuration) {
		if (configuration instanceof IFolderInfo)
			return (IFolderInfo) configuration;
		if (configuration instanceof IFileInfo)
			return (IFileInfo) configuration;
		if (configuration instanceof IConfiguration)
			return ((IConfiguration) configuration).getRootFolderInfo();
		return null;
	}

	/**
	 * Gets the configuration.
	 *
	 * @param configuration the configuration
	 * @return the configuration
	 */
	public static IConfiguration getConfiguration(IBuildObject configuration) {
		if (configuration instanceof IFolderInfo)
			return ((IFolderInfo) configuration).getParent();
		if (configuration instanceof IFileInfo)
			return ((IFileInfo) configuration).getParent();
		if (configuration instanceof IConfiguration)
			return (IConfiguration) configuration;
		return null;
	}

	/**
	 * Gets the configuration.
	 *
	 * @param holder the holder
	 * @return the configuration
	 */
	public static IConfiguration getConfiguration(IHoldsOptions holder) {
		if (holder instanceof IToolChain)
			return ((IToolChain) holder).getParent();
		return null;
	}

	/**
	 * Sets the option forced.
	 *
	 * @param config the config
	 * @param toolchain the toolchain
	 * @param option the option
	 * @param value the value
	 * @return the i option
	 * @throws BuildException the build exception
	 */
	public static IOption setOptionForced(IConfiguration config, IToolChain toolchain, IOption option, String value)
			throws BuildException {

		// System.out.println("setOptionForced(" + config.getName() + ", "
		// + toolchain.getName() + ", " + option.getName() + ", " + value
		// + ") was " + option.getStringValue());
		// setOption() does nothing if the new value is identical to the
		// previous one. this is generally ok, except the initial settings
		// when we do not want to depend on defaults, so we do this in
		// two steps, we first set an impossible value, than the actual
		// one
		IOption newOption = config.setOption(toolchain, option, "?!"); //$NON-NLS-1$
		return config.setOption(toolchain, newOption, value);
	}

	/**
	 * Force option rewrite.
	 *
	 * @param config the config
	 * @param toolchain the toolchain
	 * @param option the option
	 * @return the i option
	 * @throws BuildException the build exception
	 */
	public static IOption forceOptionRewrite(IConfiguration config, IToolChain toolchain, IOption option)
			throws BuildException {

		String value = option.getStringValue();
		// System.out.println("setOptionForced(" + config.getName() + ", "
		// + toolchain.getName() + ", " + option.getName() + ") was "
		// + option.getStringValue());
		// setOption() does nothing if the new value is identical to the
		// previous one. this is generally ok, except the initial settings
		// when we do not want to depend on defaults, so we do this in
		// two steps, we first set an impossible value, than the actual
		// one
		IOption newOption = config.setOption(toolchain, option, "?!"); //$NON-NLS-1$
		return config.setOption(toolchain, newOption, value);
	}

	/**
	 * Escape whitespaces.
	 *
	 * @param path the path
	 * @return the string
	 */
	static public String escapeWhitespaces(String path) {
		String vpath = path.trim();
		// Escape the spaces in the path/filename if it has any
		String[] segments = vpath.split("\\s"); //$NON-NLS-1$
		if (segments.length > 1) {
			if (EclipseUtils.isWindows()) {
				if (vpath.startsWith("\"") || vpath.startsWith("'")) { //$NON-NLS-1$ //$NON-NLS-2$
					return vpath;
				}

				return "\"" + vpath + "\""; //$NON-NLS-1$ //$NON-NLS-2$
			}
            StringBuffer escapedPath = new StringBuffer();
            for (int index = 0; index < segments.length; ++index) {
            	escapedPath.append(segments[index]);
            	if (index + 1 < segments.length) {
            		escapedPath.append("\\ "); //$NON-NLS-1$
            	}
            }
            return escapedPath.toString().trim();
		}
        return vpath;
	}

	/**
	 * Quote whitespaces.
	 *
	 * @param path the path
	 * @return the string
	 */
	static public String quoteWhitespaces(String path) {
	    String vpath = path.trim();
		// Escape the spaces in the path/filename if it has any
		String[] segments = vpath.split("\\s"); //$NON-NLS-1$
		if (segments.length > 1) {
			if (vpath.startsWith("\"") || vpath.startsWith("'")) { //$NON-NLS-1$ //$NON-NLS-2$
				return vpath;
			}

			return "\"" + vpath + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}
        return vpath;
	}

}
