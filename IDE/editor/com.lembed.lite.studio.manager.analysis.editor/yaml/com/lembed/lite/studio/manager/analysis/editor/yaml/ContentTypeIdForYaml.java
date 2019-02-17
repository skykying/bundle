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
package com.lembed.lite.studio.manager.analysis.editor.yaml;


/**
 *
 */
public class ContentTypeIdForYaml {

	public final static String CONTENT_TYPE_ID_YAML = getConstantString();

	/**
	 * Don't allow instantiation.
	 */
	private ContentTypeIdForYaml() {
		super();
	}

	static String getConstantString() {
		return  Activator.PLUGIN_ID + ".yaml.yamlsource"; //$NON-NLS-1$
	}

}
