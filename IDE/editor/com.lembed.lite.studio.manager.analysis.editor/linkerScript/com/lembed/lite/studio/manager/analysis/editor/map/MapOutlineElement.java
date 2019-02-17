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
package com.lembed.lite.studio.manager.analysis.editor.map;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptElement;
import com.lembed.lite.studio.manager.analysis.editor.linkerfile.LinkerFileOutlineElement;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;
import com.lembed.lite.studio.manager.analysis.editor.map.model.GccMapElement;

/**
 * This class is used to represent the elements in the outline view.
 *
 */
public class MapOutlineElement {


	public static final int ARCHIVE_ITEM = 1;
	public static final int ALLOCATING_SYMBOL_ITEM = 2;	
	public static final int LINK_MAP_ITEM = 3;	
	public static final int DISCARD_ITEM = 4;	
	public static final int MEMORY_CONFIGURE_ITEM = 5;
	public static final int DOCUMENT = 6;

	protected GccMapElement element;

	protected MapOutlineElement parent;
	protected List<MapOutlineElement> children = new ArrayList<MapOutlineElement>();
	protected Position position;
	protected IDocument document;
	protected int type;

	protected List<Integer> sectionPath = new ArrayList<Integer>();

	/**
	 * Create an DOCUMENT element. The document elements are the roots in the
	 * outline tree
	 * 
	 * @param section
	 *            A node as returned by SnakeYAML.
	 * @param document
	 *            The document containing the YAML text
	 */
	protected MapOutlineElement(GccMapElement section, IDocument document) {
		this(section, null, ALLOCATING_SYMBOL_ITEM, document);
	}

	/**
	 * @param section
	 *            A node as returned by SnakeYAML
	 * @param parent
	 *            The parent element
	 * @param type
	 *            The type of YAML element that this is. The different element
	 *            types are defined as constants in this class.
	 * @param document
	 *            The document containing the YAML text.
	 */
	protected MapOutlineElement(GccMapElement section, MapOutlineElement parent, int type, IDocument document) {
		this.element = section;
		this.parent = parent;
		this.type = type;
		this.document = document;

		if (this.parent != null) {
			this.sectionPath = parent.sectionPath;
		}
		this.sectionPath.add(System.identityHashCode(this.element));

		parseNode(section);
		position = getPosition(section);

		try {
			document.addPosition(MapContentOutlinePage.MAP_SEGMENT, position);
		} catch (BadLocationException e) {
			EditorLog.logger.warning(e.toString());
		} catch (BadPositionCategoryException e) {
			EditorLog.logger.warning(e.toString());
		}
	}

	/**
	 * Get the position of the node within the document containing the YAML
	 * text. This position is necessary to associate clicking on the element in
	 * the outline view with moving the caret to the right position in the
	 * document.
	 * 
	 * @param section
	 *            The SnakeYAML node of the current element.
	 * @return
	 */
	private Position getPosition(GccMapElement section) {

		Position p = null;
		p=section.getPosition();		
		return p;
	}

	/**
	 * Parse the node to determine what type of element it is and if it has any
	 * children. This method recursively constructs any children of the element
	 * as well.
	 * 
	 * @param node
	 *            The SnakeYAML of the current element.
	 */
	private void parseNode(GccMapElement node) {
		GccMapElement mSection = node;
		List<GccMapElement> children = mSection.getChildren();
		if(children == null) {
			return;
		}
		
		for (GccMapElement childSection : children) {

			// prevent infinite loops in case of recursive references
			if (this.sectionPath.contains(System.identityHashCode(childSection.getValue()))) {
				continue;
			}

			MapOutlineElement child = new MapOutlineElement(childSection, document);

			this.children.add(child);
		}
	}

	@Override
	public String toString() {
		return element.getTag();
	}

}
