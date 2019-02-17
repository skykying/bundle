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
package com.lembed.lite.studio.manager.analysis.editor.map.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;
import com.lembed.lite.studio.manager.analysis.editor.map.MapFileEditor;


public class FormatDocumentHandler extends AbstractHandler {


    public Object execute(ExecutionEvent event) throws ExecutionException {
    	EditorLog.logger.fine("FormatDocumentHandler executed");
        
        IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
        if( editorPart instanceof MapFileEditor ){            
        	MapFileEditor mapEditor = (MapFileEditor) editorPart;
        	mapEditor.formatDocument();            
        } else {
        	EditorLog.logger.warning("Expected the active editor to be MapFileEditor, but it wasn't");
        }
        return null;
    }

}
