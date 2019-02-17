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

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching one of the indicator characters.
 * @author oysteto
 *
 */
public class IndicatorCharacterRule implements IRule {

    private IToken token;
    private char[] indicatorCharacters = { '{', '}', '[', ']', '(', ')' ,'.'};
    
    public IndicatorCharacterRule( IToken token ){
        this.token = token;
    }
    

    public IToken evaluate(ICharacterScanner scanner) {
       
        int c = scanner.read();

        for( char indicatorCharacter : indicatorCharacters ){
            if( indicatorCharacter == (char) c ){
                return token;
            }
        }
        
        scanner.unread(); //no match so must rewind the scanner
        return Token.UNDEFINED;

    }

}
