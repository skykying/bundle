package com.lembed.lite.studio.manager.analysis.editor.map.model;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.Position;

public class GccMapElement {
	
	private Position position = null;
	private String tag = null;
	private String value = null;
	private String type = "Array";
	
	public GccMapElement parent = null;
	private List<GccMapElement> children = new LinkedList<GccMapElement>();
	
	public GccMapElement(Position pos, String name) {
		position = pos;
		tag = name;
	}

	
	public GccMapElement(Position pos, String name,String v) {
		position = pos;
		tag = name;
		value = v;
	}
	
	public Position getPosition() {
		return position;
	}

	public String getTag() {
		return tag;
	}
	
	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	
	public void addChild(GccMapElement e) {
		if(e!= null) {
			children.add(e);
		}
	}


	public List<GccMapElement> getChildren() {
		return children;
	}
}
