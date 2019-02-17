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
package com.lembed.lite.studio.manager.analysis.editor.utils;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

public class EditorUtils {
    
    // ensure no objects of this type
    private EditorUtils() {}
    
    /**
     * @param doc The document to query
     * @param offset The offset to query about
     * @return The text at the line where the offset is located
     * @throws BadLocationException Thrown on illegal offset values
     */
    public static String getLine(IDocument doc, int offset) throws BadLocationException{
        
        int line = doc.getLineOfOffset(offset);
        int lineLength = doc.getLineLength(line);
        int lineStart = doc.getLineOffset(line);
        return doc.get(lineStart, lineLength);        
        
    }

}
