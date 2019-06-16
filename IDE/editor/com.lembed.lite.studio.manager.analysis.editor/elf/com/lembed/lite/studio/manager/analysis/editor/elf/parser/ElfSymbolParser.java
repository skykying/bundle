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

import java.util.HashMap;
import org.eclipse.core.resources.IProject;
import com.google.common.base.Strings;
import com.lembed.lite.studio.manager.analysis.editor.command.IEditorConsole;
import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;
import com.lembed.lite.studio.manager.analysis.editor.elf.model.ElfSymbolParserResult;

public class ElfSymbolParser extends ElfParserBase {

	public ElfSymbolParser(IEditorConsole editorConsole, String binaryPath) {
		super(editorConsole, binaryPath);
		addArguments(" -s "); 
	}

	String removeDuplicates(String str)
    {

        HashMap<Integer,Character> lhs = new HashMap<Integer,Character>();
        for(int i=0;i<str.length();i++){
        	lhs.put(i,str.charAt(i));
        }
            
        for(Integer i : lhs.keySet()){
        	Character cs = lhs.get(i);
        	Character csc = lhs.get(++i);
        	if(csc != null){
        		if(cs.equals(csc) && csc.equals(BLANK)){
        			lhs.remove(i);
        		}
        	}
        }
        String res="";
        for(Integer i : lhs.keySet()){
        	res = res + lhs.get(i);
        }
        return res;
    }
	
	@Override
	public ParserResult parseResult(String line, IProject project) {
		if(!line.contains(DELIMITER)){
			return null;
		}
		//replace delimiter to blank
		line = line.replaceAll(DELIMITER, BLANK);
		
		//remove duplicate blank char and trim the blank
		line = line.replaceAll(" +", " ").trim()+" ";
		
		String[] lineParts = line.split(BLANK, 8);
		if (lineParts.length < 8) {
			return null;
		}

		/**
		 * line should have the following format
		 * <file>;<line>;<severity>;<id>;<message>
		 * 
		 * TODO: <file> and <line> might be empty!
		 */
		try {
			String sindex = lineParts[0];
			if(sindex.toLowerCase().contains("n") ){
				return null;
			}

			String index;
			if (Strings.isNullOrEmpty(lineParts[0])) {
				index = STRING_EMPTY;
			} else {
				index = lineParts[0];
			}
			// if line is empty set it to -1
			String  value;
			if (Strings.isNullOrEmpty(lineParts[1])) {
				value = STRING_EMPTY;
			} else {
				value = lineParts[1];
			}
			
			// if line is empty set it to -1
			String  size;
			if (Strings.isNullOrEmpty(lineParts[2])) {
				size = STRING_EMPTY;
			} else {
				size = lineParts[2];
			}
			
			String  type;
			if (Strings.isNullOrEmpty(lineParts[3])) {
				type = STRING_EMPTY;
			} else {
				type = lineParts[3];
			}
			
			
			String  bind;
			if (Strings.isNullOrEmpty(lineParts[4])) {
				bind = STRING_EMPTY;
			} else {
				bind = lineParts[4];
			}
			
			
			String  vis;
			if (Strings.isNullOrEmpty(lineParts[5])) {
				vis = STRING_EMPTY;
			} else {
				vis = lineParts[5];
			}
			
			String  ndx;
			if (Strings.isNullOrEmpty(lineParts[6])) {
				ndx = STRING_EMPTY;
			} else {
				ndx = lineParts[6];
			}
			
			String  name;
			if (Strings.isNullOrEmpty(lineParts[7])) {
				name = STRING_EMPTY;
			} else {
				name = lineParts[7];
			}
			
			return new ElfSymbolParserResult(index, value,size,type,bind,vis,ndx,name);

		} catch (NumberFormatException e2) {
			throw new IllegalArgumentException(
					"Could not parse the second token in line: '" + line + "' into an Integer", e2);
		}
	}

}
