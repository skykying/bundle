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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Word detector used in the scanner rule for tags. 
 */
public class TagWordDetector implements IWordDetector {

    private Pattern characterPattern = Pattern.compile( "[\\w \\d ! < > \\. , :]", Pattern.COMMENTS );

    public boolean isWordPart(char c) {
        
        Matcher m = characterPattern.matcher( new Character(c).toString() );
        if( m.matches() ){
            return true;
        }
        
        return false;
    }

    public boolean isWordStart(char c) {

        Matcher m = characterPattern.matcher( new Character(c).toString() );
        if( m.matches() ){
            return true;
        }
        
        return false;

    }
}
