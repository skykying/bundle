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
/**
 * (c) Copyright Mirasol Op'nWorks Inc. 2002, 2003. 
 * http://www.opnworks.com
 * Created on June 29, 2003 by lgauthier@opnworks.com
 * 
 */

package com.lembed.lite.studio.manager.analysis.editor.elf.ui.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

/**
 * This sample class demonstrates how to use the TableViewerExample 
 * inside a workbench view. The view is essentially a wrapper for
 * the TableViewerExample. It handles the Selection event for the close 
 * button.
 */

public class ElfTableView extends ViewPart {
	private ElfTableViewerEx viewer;

	/**
	 * The constructor.
	 */
	public ElfTableView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new ElfTableViewerEx(parent);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
