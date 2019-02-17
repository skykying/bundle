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
package com.lembed.lite.studio.manager.analysis.editor.map.scanner;

import org.eclipse.jface.text.rules.IToken;

public class DoubleQuotedKeyRule extends KeyRule {

    public DoubleQuotedKeyRule( IToken token ){
        super(token);
    }    

    protected String getKeyRegex(){
        return "( \" [ \\[ \\] ' \\w \\s + - / \\\\ \\. \\( \\) \\? \\@ \\$ _ < = > \\| \\{ \\} \\* ]*\" \\s* : ) \\s.*";
    }
    
}
