/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.linkerfile;


import com.lembed.lite.studio.manager.analysis.editor.linker.Activator;

/**
 *
 */
public class ContentTypeIdForLinkerFile {
	
	public final static String CONTENT_TYPE_ID_LINKERFILE = getConstantString();

	/**
	 * Don't allow instantiation.
	 */
	private ContentTypeIdForLinkerFile() {
		super();
	}

	static String getConstantString() {
		return  Activator.PLUGIN_ID + ".linkerfile.linkerfilesource"; //$NON-NLS-1$
	}		

}
