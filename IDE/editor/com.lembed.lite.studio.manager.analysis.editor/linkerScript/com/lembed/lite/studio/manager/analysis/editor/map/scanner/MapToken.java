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

import java.util.HashMap;

import org.eclipse.jface.text.rules.Token;

public class MapToken extends Token {
	
	public static int KEY = 1;
	public static int SCALAR = 2;
	public static int DOCUMENT_START = 3;
	public static int DOCUMENT_END = 4;
	public static int COMMENT = 5;
	public static int ANCHOR = 6;
	public static int OPERATORS = 7;
	public static int TAG_PROPERTY = 8;
	public static int INDICATOR_CHARACTER = 9;
	public static int DIRECTIVE = 10;
	public static int CONSTANT = 11;
	public static int WHITESPACE = 12;

	//helper mapping used by toString(). Should really be easier way to do this.
	private static final HashMap<Integer, String> TOKEN_NAMES = new HashMap<Integer, String>();
	static {
		TOKEN_NAMES.put( MapToken.DOCUMENT_START, "DOCUMENT_START" );
		TOKEN_NAMES.put( MapToken.DOCUMENT_END, "DOCUMENT_END" );
		TOKEN_NAMES.put( MapToken.COMMENT, "COMMENT" );
		TOKEN_NAMES.put( MapToken.KEY, "KEY" );
		TOKEN_NAMES.put( MapToken.SCALAR, "SCALAR" );
		TOKEN_NAMES.put( MapToken.ANCHOR,"ANCHOR" );
		TOKEN_NAMES.put( MapToken.OPERATORS, "ALIAS" );
		TOKEN_NAMES.put( MapToken.TAG_PROPERTY, "TAG_PROPERTY" );
		TOKEN_NAMES.put( MapToken.INDICATOR_CHARACTER, "INDICATOR_CHARACTER" );
		TOKEN_NAMES.put( MapToken.DIRECTIVE, "DIRECTIVE" );
		TOKEN_NAMES.put( MapToken.CONSTANT, "CONSTANT" );
        TOKEN_NAMES.put( MapToken.WHITESPACE, "WHITESPACE" );		
	}	
	
	//helper mapping used for the scanner tests
	public static final HashMap<String,Integer> VALID_TOKENS = new HashMap<String, Integer>();
	static {
        VALID_TOKENS.put( "DOCUMENT_START", MapToken.DOCUMENT_START );
        VALID_TOKENS.put( "DOCUMENT_END", MapToken.DOCUMENT_END );
        VALID_TOKENS.put( "COMMENT", MapToken.COMMENT );
        VALID_TOKENS.put( "KEY", MapToken.KEY );
        VALID_TOKENS.put( "SCALAR", MapToken.SCALAR );
        VALID_TOKENS.put( "ANCHOR", MapToken.ANCHOR );
        VALID_TOKENS.put( "ALIAS", MapToken.OPERATORS );
        VALID_TOKENS.put( "TAG_PROPERTY", MapToken.TAG_PROPERTY );
        VALID_TOKENS.put( "INDICATOR_CHARACTER", MapToken.INDICATOR_CHARACTER );
        VALID_TOKENS.put( "DIRECTIVE", MapToken.DIRECTIVE );
        VALID_TOKENS.put( "CONSTANT", MapToken.CONSTANT );	   
        VALID_TOKENS.put( "WHITESPACE", MapToken.WHITESPACE );        
	}
	
	
	protected int tokenType;
	
	public MapToken( Object data, int type ){
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
		
		if( !( o instanceof MapToken ) ){
			return false;
		}
		
		MapToken t = ( MapToken ) o;
		if( t.tokenType == this.tokenType ){
			return true;
		}
		
		return false;
		
	}

}
