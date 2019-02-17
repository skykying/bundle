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
package com.lembed.lite.studio.manager.analysis.editor.elf.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.lembed.lite.studio.manager.analysis.editor.command.ParserResult;

public class ElfSectionParserResult extends ParserResult{

	HashMap<String, String> provider = new HashMap<String, String>();
	
	String _Num;
	String _Name;
	String _Type;
	String _Addr;
	String _Offsize;
	String _Size;
	String _ES;
	String _LK;
	String _Inf;
	String _Al;
	String _Flags;

	private static final String[] titles = {"Num","Name","Type","Addr","Offsize","Size","ES","LK","Inf","Al","Flags"};
	
	public ElfSectionParserResult(String key, String value) {		
		_Num = key;
		_Name = value;
	}

	public ElfSectionParserResult(String num, String name, String type, String addr, String off, String size,
			String es, String lk,String inf, String al, String flags) {
		_Num = num;
		_Name = name;
		_Type = type;
		_Addr = addr;
		_Offsize = off;
		_Size = size;
		_ES = es;
		_LK = lk;
		_Inf=inf;
		_Al = al;
		_Flags =flags;
	}


	@Override
	public String[] getTitles() {
		// TODO Auto-generated method stub
		return titles;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 11;
	}

	@Override
	public List<Integer> bound() {
		 List<Integer> results = new LinkedList<Integer>();
		 results.add(50);
		 results.add(100);
		 results.add(100);
		 results.add(100);
		 results.add(100);
		 results.add(100);
		 results.add(50);
		 results.add(50);
		 results.add(50);
		 results.add(80);
		 results.add(200);
		return results;
	}

	@Override
	public List<String> get_values() {
		 List<String> results = new LinkedList<String>();
		 results.add(_Num);
		 results.add(_Name);
		 results.add(_Type);
		 results.add(_Addr);
		 results.add(_Offsize);
		 results.add(_Size);
		 results.add(_ES);
		 results.add(_LK);
		 results.add(_Inf);
		 results.add(_Al);
		 results.add(_Flags);
		return results;
	}

}
