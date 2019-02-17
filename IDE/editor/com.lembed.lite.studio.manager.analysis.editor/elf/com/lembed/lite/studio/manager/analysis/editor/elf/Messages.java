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
package com.lembed.lite.studio.manager.analysis.editor.elf;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = Messages.class.getName(); //$NON-NLS-1$
	
	public static String CollapseAll;
	public static String ConfigEditor_ErrorInNestedTextEditor;
	public static String ElfEditor_OverviewPageText;
	public static String ElfEditor_ModulePageText;
	public static String ElfEditor_FilePageText;
	public static String ConfigEditor_InvalidEditorInput;

	public static String ElfEditor_InfoPageText;
	public static String ElfEditor_SymbolPageText;
	public static String ElfEditor_SectionPageText;
	

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
