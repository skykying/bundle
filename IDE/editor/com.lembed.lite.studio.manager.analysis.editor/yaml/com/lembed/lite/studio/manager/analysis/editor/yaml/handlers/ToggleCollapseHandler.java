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
package com.lembed.lite.studio.manager.analysis.editor.yaml.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;
import com.lembed.lite.studio.manager.analysis.editor.yaml.YEditor;

/**
 * Handler for toggling the collapse/expand of the outline view elements.
 */
public class ToggleCollapseHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
		if( editorPart instanceof YEditor ){
			YEditor yedit = (YEditor) editorPart;
			yedit.getContentOutlinePage().toggleCollapse();
		} else {
			EditorLog.logger.warning("Expected the active editor to be YEdit, but it wasn't");
		}
		return null;
	}

}
