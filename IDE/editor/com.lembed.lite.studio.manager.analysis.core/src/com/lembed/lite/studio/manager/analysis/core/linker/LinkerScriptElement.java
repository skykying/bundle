package com.lembed.lite.studio.manager.analysis.core.linker;

import java.util.LinkedList;
import java.util.List;

public class LinkerScriptElement {
	
	public static final String ENTRY_TYPE 		= "startUp";
	public static final String MEMORY_TYPE 		= "Memory Configure";
	public static final String GROUP_TYPE 		= "Group";
	public static final String SECTION_TYPE 	= "Memory Layout";
	public static final String DISCARD_TYPE 	= "Discard";
	public static final String NORMAL_TYPE 		= "Default";
	
	private int hash = 0;

	
	private List<LinkerScriptElement> children = new LinkedList<LinkerScriptElement>();
	LinkerScriptElement parent = null;
	
	private String sectionName = null;
	private String elementType = NORMAL_TYPE;
	private String elementValue = null;
	
	private int elementStartLine = 0;
	private int elementStopLine = 0;

	public List<LinkerScriptElement> getChildren() {
		return children;
	}
	
	public LinkerScriptElement(String name){
		sectionName = name;
		elementType = DISCARD_TYPE;
	}
	
	public LinkerScriptElement(String key, String value, String type, int startLine, int stopLine) {		
		sectionName = key;
		elementValue = value;
		elementType = type;
		elementStartLine =startLine;
		elementStopLine = stopLine;
		hash = System.identityHashCode(sectionName+elementValue);
	}


	public  int getStartLine() {
		return elementStartLine;
	}

	public String getValue() {
		
		return elementValue;
	}

	public String getKey() {

		return sectionName;
	}

	public String getType()
	{
		return elementType;
	}
	
	public String getName() {
		return sectionName ;
	}

	
	public String toString() {
		return sectionName;
	}


	public int getStopLine() {
		return elementStopLine;
	}

	public void addChild(LinkerScriptElement e) {
		if(e!= null) {
			children.add(e);
		}
	}
}
