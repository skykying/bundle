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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptElement;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;

/**
 * This class is used to represent the elements in the outline view.
 * https://www.ibm.com/developerworks/cn/opensource/os-cn-eclipse-outline/index.html
 */
public class LinkerFileOutlineElement {

	/** The element is part of a mapping */
	public static final int LINKER_ITEM = 1;

	/** The element is part of a sequence */
	public static final int GROUP_ITEM = 2;

	/** The element is a document element */
	public static final int SECTION_ITEM = 3;

	protected LinkerScriptElement section;
	protected String key = "key";
	protected String value = "value";

	protected LinkerFileOutlineElement parent;
	protected List<LinkerFileOutlineElement> children = new ArrayList<LinkerFileOutlineElement>();

	protected Position position;
	protected IDocument document;
	protected int type;

	protected List<Integer> sectionPath = new ArrayList<Integer>();

	protected LinkerFileOutlineElement(LinkerScriptElement section, IDocument document) {
		this(section, null, LINKER_ITEM, document);
	}

	protected LinkerFileOutlineElement(LinkerScriptElement section, LinkerFileOutlineElement parent, int type,
			IDocument document) {
		this.section = section;
		this.parent = parent;
		this.type = type;
		this.document = document;

		if (this.parent != null) {
			this.sectionPath = parent.sectionPath;
		}
		this.sectionPath.add(System.identityHashCode(this.section));

		// child element parser here
		parseSection(section);
		position = getPosition(section);
		
		if(position != null) {
			try {
				document.addPosition(LinkerFileContentOutlinePage.LINKERFILESEGMENT, position);
			} catch (BadLocationException e) {
				EditorLog.logger.warning(e.toString());
			} catch (BadPositionCategoryException e) {
				EditorLog.logger.warning(e.toString());
			}
		}
	}

	private Position getPosition(LinkerScriptElement section) {

		int startLine = section.getStartLine();
		int endLine = section.getStopLine();
		Position p = null;

		try {
			int offsetStart = document.getLineOffset(startLine);
			int offsetStop = document.getLineOffset(endLine);
			int length = offsetStop - offsetStart +1;
			offsetStart = offsetStart -1;
			
			p = new Position(offsetStart, length);
		} catch (BadLocationException e) {
			EditorLog.logger.warning(e.toString());
		}
		return p;
	}

	private void parseSection(LinkerScriptElement section) {

		LinkerScriptElement mSection = section;
		List<LinkerScriptElement> children = mSection.getChildren();
		if(children == null) {
			return;
		}
		
		for (LinkerScriptElement childSection : children) {

			// prevent infinite loops in case of recursive references
			if (this.sectionPath.contains(System.identityHashCode(childSection.getValue()))) {
				continue;
			}

			String keyNode = childSection.getKey();
			String key = keyNode.toString();

			LinkerFileOutlineElement child = new LinkerFileOutlineElement(childSection, this, SECTION_ITEM, document);

			child.key = key;
			this.children.add(child);
		}
	}

	@Override
	public String toString() {

		return section.toString();
	}

}
