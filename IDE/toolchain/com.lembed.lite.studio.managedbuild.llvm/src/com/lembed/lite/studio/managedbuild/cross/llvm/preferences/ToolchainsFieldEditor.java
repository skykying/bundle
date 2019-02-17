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
package com.lembed.lite.studio.managedbuild.cross.llvm.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.swt.widgets.Composite;

import com.lembed.lite.studio.managedbuild.cross.llvm.ui.ToolchainDefinition;

/**
 * The Class ToolchainsFieldEditor.
 */
public class ToolchainsFieldEditor extends ComboFieldEditor {

	static String[][] populateCombo() {
		int len = ToolchainDefinition.getList().size();
		String[][] a = new String[len + 1][2];
		a[0][0] = Messages.USE_BUILD_IN_TOOLCHAIN_LABEL;
		a[0][1] = ""; //$NON-NLS-1$

		int i = 1;
		for (ToolchainDefinition t : ToolchainDefinition.getList()) {
			a[i][0] = t.getName();
			a[i][1] = t.getName();
			++i;
		}
		return a;
	}

	/**
	 * Instantiates a new toolchains field editor.
	 *
	 * @param name the name
	 * @param labelText the label text
	 * @param parent the parent
	 */
	public ToolchainsFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, populateCombo(), parent);
	}

}
