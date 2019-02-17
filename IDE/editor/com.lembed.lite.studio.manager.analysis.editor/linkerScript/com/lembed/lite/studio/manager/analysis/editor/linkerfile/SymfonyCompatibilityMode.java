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
package com.lembed.lite.studio.manager.analysis.editor.linkerfile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import com.lembed.lite.studio.manager.analysis.editor.linker.scanner.LinkerFileScanner;
import com.lembed.lite.studio.manager.analysis.editor.linker.scanner.LinkerFileToken;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;


/**
 * Implementation of the Symfony compatibility mode.
 *
 * The configuration files for the PHP web-framework Symfony uses invalid YAML
 * causing problems for the SnakeYAML parser. The problem lies in the use of %VAR% and
 * %%VAR%% for special configuration variables. To get past this problem this class
 * implements a pre-processor that replaces all '%' with '_' in unqouted scalars before they are
 * sent to the SnakeYAML parser.
 *
 * Note that there are limitations to this mode since it depends on the parser made for the
 * syntax highlighter.
 * @author oysteto
 *
 */
public class SymfonyCompatibilityMode {

    private LinkerFileScanner scanner;

    private Pattern replaceCharPattern = Pattern.compile("% | \\{ | \\} | \\[ | \\] | \\?", Pattern.COMMENTS);

    public SymfonyCompatibilityMode( LinkerFileScanner linkerFileScanner ){
        this.scanner = linkerFileScanner;
    }

    /**
     * Replace all occurences of replaceCharPattern in unqouted scalars.
     * @param document The document with the content.
     * @return A string of the document content where all the replaceCharPattern characters have
     * been replaced with '_'.
     */
    public String fixScalars( IDocument document ){

        int rangeLength = document.getLength();
        scanner.setRange(document, 0, rangeLength);

        // Scan for tokens. It is crucial to update the range of the
        // scanner after each read token to prevent and infinite loop
        String replacedContent = "";
        IToken token = scanner.nextToken();
        while (token != Token.EOF) {

            String tokenString;
            try {
                tokenString = document.get(scanner.getTokenOffset(), scanner.getTokenLength());

                if( token instanceof LinkerFileToken ){
                	LinkerFileToken yt = (LinkerFileToken) token;
                    if( yt.getTokenType() == LinkerFileToken.SCALAR && !startsWithQuote( tokenString ) ){
                        EditorLog.logger.info( "Replacing characters in string " + tokenString );                        
                        Matcher m = replaceCharPattern.matcher(tokenString);
                        tokenString = m.replaceAll("_");
                    }
                }

                replacedContent += tokenString;

            } catch (BadLocationException e) {
                EditorLog.logException(e, "Replacing special chars in unquoted scalars failed" );

                //if quoting the scalars failed, just return the original text to prevent
                //more problems
                return document.get();
            }

            //update the range of the scanner
            int newOffset = scanner.getTokenOffset() + scanner.getTokenLength();
            rangeLength = rangeLength - scanner.getTokenLength();
            scanner.setRange(document, newOffset, rangeLength);

            token = scanner.nextToken();
        }

        return replacedContent;

    }

    /**
     * @param s The string to check
     * @return true if the string starts with a quote. false otherwise.
     */
    protected boolean startsWithQuote( String s ){

        if( s.charAt(0) == '\'' || s.charAt(0) == '"' ){
            return true;
        }
        return false;
    }


}
