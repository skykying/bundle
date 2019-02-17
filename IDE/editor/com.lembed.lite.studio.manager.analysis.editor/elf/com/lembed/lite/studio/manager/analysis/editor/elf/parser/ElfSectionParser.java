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
package com.lembed.lite.studio.manager.analysis.editor.elf.parser;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import com.google.common.base.Strings;
import com.lembed.lite.studio.manager.analysis.editor.command.IEditorConsole;
import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;
import com.lembed.lite.studio.manager.analysis.editor.elf.model.ElfSectionParserResult;

public class ElfSectionParser extends ElfParserBase {
	
	private static String joinLines = "";
	private static Integer index=0;

	public ElfSectionParser(IEditorConsole editorConsole, String binaryPath) {
		super(editorConsole, binaryPath);
		addArguments(" -t ");
	}

	@Override
	public ParserResult parseResult(String line, IProject project) {
		ParserResult r=null;
		index++;
		joinLines = joinLines + line;

		if(index <= 3){
			joinLines=BLANK;
			return null;			
		}
		
		// 1.2.3 -> 4.5.6
		if(index == 6){
			index = 3;
			r = _parseResult();
			joinLines = BLANK;
		}
		
		return r;
	}
	
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("[0-9]*");  
	    return pattern.matcher(str).matches();     
	} 
	
	/*
	 *     0        NULL            00000000 000000 000000 00   0   0  0        00000000  
	 *     5  .bss       NOBITS          20000b28 020b28 001084 00   0   0  4       00000003  WRITE, ALLOC
	 */
	public ParserResult _parseResult() {

		String line = joinLines;
		joinLines = BLANK;
		
		//replace delimiter to blank
		line = line.replaceAll("\\]", BLANK);
		line = line.replaceAll("\\[", BLANK);
		
		//remove duplicate blank char and trim the blank
		line = line.replaceAll(" +", " ").trim()+" ";

		String[] lineParts = line.split(BLANK, 11);
		if (lineParts.length < 11) {
			return null;
		}

		/**
		 * line should have the following format
		 * <file>;<line>;<severity>;<id>;<message>
		 * 
		 * TODO: <file> and <line> might be empty!
		 */
		try {

			String num;
			if (Strings.isNullOrEmpty(lineParts[0])) {
				num = STRING_EMPTY;
			} else {
				num = lineParts[0];
			}
			
			if(!isNumeric(num)){
				return null;
			}
			
			// if line is empty set it to -1
			String  name;
			if (Strings.isNullOrEmpty(lineParts[1])) {
				name = STRING_EMPTY;
			} else {
				name = lineParts[1];
			}
			
			// if line is empty set it to -1
			String  type;
			if (Strings.isNullOrEmpty(lineParts[2])) {
				type = STRING_EMPTY;
			} else {
				type = lineParts[2];
			}
			
			String  addr;
			if (Strings.isNullOrEmpty(lineParts[3])) {
				addr = STRING_EMPTY;
			} else {
				addr = lineParts[3];
			}
			
			
			String  off;
			if (Strings.isNullOrEmpty(lineParts[4])) {
				off = STRING_EMPTY;
			} else {
				off = lineParts[4];
			}
			
			
			String  size;
			if (Strings.isNullOrEmpty(lineParts[5])) {
				size = STRING_EMPTY;
			} else {
				size = lineParts[5];
			}
			
			String  es;
			if (Strings.isNullOrEmpty(lineParts[6])) {
				es = STRING_EMPTY;
			} else {
				es = lineParts[6];
			}
			
			String  lk;
			if (Strings.isNullOrEmpty(lineParts[7])) {
				lk = STRING_EMPTY;
			} else {
				lk = lineParts[7];
			}
			
			String  inf;
			if (Strings.isNullOrEmpty(lineParts[8])) {
				inf = STRING_EMPTY;
			} else {
				inf = lineParts[8];
			}
			
			String  al;
			if (Strings.isNullOrEmpty(lineParts[9])) {
				al = STRING_EMPTY;
			} else {
				al = lineParts[9];
			}
			
			String  flags;
			if (Strings.isNullOrEmpty(lineParts[10])) {
				flags = STRING_EMPTY;
			} else {
				flags = lineParts[10];
			}
			
			return new ElfSectionParserResult(num, name, type, addr, off, size, es, lk, inf, al, flags);

		} catch (NumberFormatException e2) {
			throw new IllegalArgumentException(
					"Could not parse the second token in line: '" + line + "' into an Integer", e2);
		}
	}

}
