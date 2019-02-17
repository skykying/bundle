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

import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Scanner rule for matching one of the predefined values/constants.
 * @author oysteto
 *
 */
public class PredefinedValueRule implements IRule {

    private IToken token;
    
    public PredefinedValueRule( IToken token ){
        this.token = token;
    }
    
    public IToken evaluate(ICharacterScanner scanner) {

        int c = scanner.read();
        
        //first possibility is a single char predefined value
//        if( '.' == (char) c){
//            return token;
//        }
        
        //read the next three character for testing the 4 character predefined values
        String s = "" + (char) c;
        s += (char) scanner.read();
        String twoCharacterRegex = "AT"; //$NON-NLS-1$
        if( Pattern.matches(twoCharacterRegex, s) ){
            return token;
        }
        
        s += (char) scanner.read();
        String thirdCharacterRegex = "|END|";
        if( Pattern.matches(thirdCharacterRegex, s) ){
            return token;
        }
        
        s += (char) scanner.read();
        String fourCharacterRegex = "|true|True|TRUE|"; //$NON-NLS-1$
        fourCharacterRegex += "|\\.inf|\\.Inf|\\.INF"; //$NON-NLS-1$
        fourCharacterRegex += "|\\.nan|\\.NaN|\\.NAN"; //$NON-NLS-1$
        fourCharacterRegex += "|null|Null|NULL|"; //$NON-NLS-1$
        fourCharacterRegex += "|KEEP|LONG|ADDR|FILL|LOAD|"; //$NON-NLS-1$
        fourCharacterRegex += "|Name|"; //$NON-NLS-1$
        
        if( Pattern.matches(fourCharacterRegex, s) ){
            return token;
        }
        
        //read the next character for testing 4 character predefined values
        s += (char) scanner.read();
        
        String fiveCharacterRegex = "|false|False|FALSE|ENTRY|ALIGN|GROUP|START|"; //$NON-NLS-1$
        if( Pattern.matches(fiveCharacterRegex, s) ){
            return token;
        }
        
        s += (char) scanner.read();
        String sixCharacterRegex = "|MEMORY|ORIGIN|LENGTH|symbol|"; //$NON-NLS-1$
        sixCharacterRegex += "|Origin|Length|";
        
        if( Pattern.matches(sixCharacterRegex, s) ){
            return token;
        }
        
        s += (char) scanner.read();
        String sevCharacterRegex = "|PROVIDE|DISCARD|"; //$NON-NLS-1$
        if( Pattern.matches(sevCharacterRegex, s) ){
            return token;
        }
        
      
        s += (char) scanner.read();
        String eigCharacterRegex = "|SECTIONS|LOADADDR|"; //$NON-NLS-1$
        if( Pattern.matches(eigCharacterRegex, s) ){
            return token;
        }
        
        
        s += (char) scanner.read();
        s += (char) scanner.read();
        String tenCharacterRegex = "|Attributes|"; //$NON-NLS-1$
        if( Pattern.matches(tenCharacterRegex, s) ){
            return token;
        }
        
        s += (char) scanner.read();
        s += (char) scanner.read();
        s += (char) scanner.read();
        s += (char) scanner.read();
        String fourThenCharacterRegex = "|PROVIDE_HIDDEN"; //$NON-NLS-1$
        if( Pattern.matches(fourThenCharacterRegex, s) ){
            return token;
        }
        
        //no one of them matched so rewind the scanner
        for( int i = 0; i < 14; i++ ){
            scanner.unread();
        }
        
        return Token.UNDEFINED;
        
    }

}
