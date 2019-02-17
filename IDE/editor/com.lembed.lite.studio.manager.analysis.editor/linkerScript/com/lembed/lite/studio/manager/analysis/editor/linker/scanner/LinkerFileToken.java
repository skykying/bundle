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
package com.lembed.lite.studio.manager.analysis.editor.linker.scanner;

import java.util.HashMap;

import org.eclipse.jface.text.rules.Token;

public class LinkerFileToken extends Token {
	
	public static int KEY = 1;
	public static int SCALAR = 2;
	public static int DOCUMENT_START = 3;
	public static int DOCUMENT_END = 4;
	public static int COMMENT = 5;
	public static int ANCHOR = 6;
	public static int ALIAS = 7;
	public static int TAG_PROPERTY = 8;
	public static int INDICATOR_CHARACTER = 9;
	public static int DIRECTIVE = 10;
	public static int CONSTANT = 11;
	public static int WHITESPACE = 12;

	//helper mapping used by toString(). Should really be easier way to do this.
	private static final HashMap<Integer, String> TOKEN_NAMES = new HashMap<Integer, String>();
	static {
		TOKEN_NAMES.put( LinkerFileToken.DOCUMENT_START, "DOCUMENT_START" );
		TOKEN_NAMES.put( LinkerFileToken.DOCUMENT_END, "DOCUMENT_END" );
		TOKEN_NAMES.put( LinkerFileToken.COMMENT, "COMMENT" );
		TOKEN_NAMES.put( LinkerFileToken.KEY, "KEY" );
		TOKEN_NAMES.put( LinkerFileToken.SCALAR, "SCALAR" );
		TOKEN_NAMES.put( LinkerFileToken.ANCHOR,"ANCHOR" );
		TOKEN_NAMES.put( LinkerFileToken.ALIAS, "ALIAS" );
		TOKEN_NAMES.put( LinkerFileToken.TAG_PROPERTY, "TAG_PROPERTY" );
		TOKEN_NAMES.put( LinkerFileToken.INDICATOR_CHARACTER, "INDICATOR_CHARACTER" );
		TOKEN_NAMES.put( LinkerFileToken.DIRECTIVE, "DIRECTIVE" );
		TOKEN_NAMES.put( LinkerFileToken.CONSTANT, "CONSTANT" );
        TOKEN_NAMES.put( LinkerFileToken.WHITESPACE, "WHITESPACE" );		
	}	
	
	//helper mapping used for the scanner tests
	public static final HashMap<String,Integer> VALID_TOKENS = new HashMap<String, Integer>();
	static {
        VALID_TOKENS.put( "DOCUMENT_START", LinkerFileToken.DOCUMENT_START );
        VALID_TOKENS.put( "DOCUMENT_END", LinkerFileToken.DOCUMENT_END );
        VALID_TOKENS.put( "COMMENT", LinkerFileToken.COMMENT );
        VALID_TOKENS.put( "KEY", LinkerFileToken.KEY );
        VALID_TOKENS.put( "SCALAR", LinkerFileToken.SCALAR );
        VALID_TOKENS.put( "ANCHOR", LinkerFileToken.ANCHOR );
        VALID_TOKENS.put( "ALIAS", LinkerFileToken.ALIAS );
        VALID_TOKENS.put( "TAG_PROPERTY", LinkerFileToken.TAG_PROPERTY );
        VALID_TOKENS.put( "INDICATOR_CHARACTER", LinkerFileToken.INDICATOR_CHARACTER );
        VALID_TOKENS.put( "DIRECTIVE", LinkerFileToken.DIRECTIVE );
        VALID_TOKENS.put( "CONSTANT", LinkerFileToken.CONSTANT );	   
        VALID_TOKENS.put( "WHITESPACE", LinkerFileToken.WHITESPACE );        
	}
	
	
	protected int tokenType;
	
	public LinkerFileToken( Object data, int type ){
		super( data );
		this.tokenType = type;		
	}
	
	public int getTokenType(){
	    return this.tokenType;
	}
		
	public String toString(){	
		return TOKEN_NAMES.get( tokenType );	
	}
	
	public boolean equals( Object o ){
		
		if( !( o instanceof LinkerFileToken ) ){
			return false;
		}
		
		LinkerFileToken t = ( LinkerFileToken ) o;
		if( t.tokenType == this.tokenType ){
			return true;
		}
		
		return false;
		
	}

}
