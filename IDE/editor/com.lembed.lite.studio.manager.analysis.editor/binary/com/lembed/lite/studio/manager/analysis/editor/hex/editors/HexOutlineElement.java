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
package com.lembed.lite.studio.manager.analysis.editor.hex.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

import com.lembed.lite.studio.manager.analysis.editor.hex.parser.Page;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;

/**
 * http://marketplace.eclipse.org/content/bytecode-outline#group-screenshots
 * @author Administrator
 *
 */
@SuppressWarnings("javadoc")
public class HexOutlineElement {

	/** The element is part of a mapping */
	public static final int MAPPINGITEM = 1;

	/** The element is part of a sequence */
	public static final int SEQUENCEITEM = 2;

	/** The element is a document element */
	public static final int DOCUMENT = 3;

	
    protected Page page;
	protected String key;
	protected String value;
	protected HexOutlineElement parent;
	protected List<HexOutlineElement> children = new ArrayList<>();
	protected Position position;
	protected IDocument document;
	protected int type;

	protected List<Integer> pagePath = new ArrayList<>();

	/**
	 * Create an DOCUMENT element. The document elements are the roots in the
	 * outline tree
	 * 
	 * @param page
	 *            A node as returned by SnakeYAML.
	 * @param document
	 *            The document containing the YAML text
	 */
	protected HexOutlineElement(Page page, IDocument document) {
		this(page, null, DOCUMENT, document);
	}

	/**
	 * @param page
	 *            A node as returned by SnakeYAML
	 * @param parent
	 *            The parent element
	 * @param type
	 *            The type of YAML element that this is. The different element
	 *            types are defined as constants in this class.
	 * @param document
	 *            The document containing the YAML text.
	 */
	protected HexOutlineElement(Page page, HexOutlineElement parent, int type, IDocument document) {
		this.page = page;
		this.parent = parent;
		this.type = type;
		this.document = document;

		if (this.parent != null) {
			this.pagePath = parent.pagePath;
		}
		this.pagePath.add(System.identityHashCode(this.page));

		parsePage(page);
		position = getPosition(page);
		try {
			document.addPosition(HexContentOutlinePage.HEXSEGMENT, position);
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
	 * @param hexPage
	 *            The SnakeYAML node of the current element.
	 * @return
	 */
	private Position getPosition(Page hexPage) {

		int startLine = hexPage.getStartMark().getLine();
		int startColumn = hexPage.getStartMark().getColumn();
		int endLine = hexPage.getEndMark().getLine();
		int endColumn = hexPage.getEndMark().getColumn();
		Position p = null;
		try {
			int offset = document.getLineOffset(startLine);
			offset += startColumn;

			int length;
			if (startLine < endLine) {
				length = document.getLineLength(startLine) - startColumn;
			} else {
				length = endColumn - startColumn;
			}
			p = new Position(offset, length);
		} catch (BadLocationException e) {
			EditorLog.logger.warning(e.toString());
		}
		return p;
	}

	/**
	 * Parse the node to determine what type of element it is and if it has any
	 * children. This method recursively constructs any children of the element
	 * as well.
	 * 
	 * @param pPage
	 *            The SnakeYAML of the current element.
	 */
	private void parsePage(Page pPage) {

		Page sNode = pPage;
		List<Page> ccpage = sNode.getValue();
		for (Page childNode : ccpage) {

			// prevent infinite loops in case of recursive references
			if (this.pagePath.contains(System.identityHashCode(childNode))) {
				continue;
			}

			HexOutlineElement child = new HexOutlineElement(childNode, this, SEQUENCEITEM, document);
			this.children.add(child);
		}

	}

	@Override
    public String toString() {
		if (type == HexOutlineElement.DOCUMENT) {
			return ""; //$NON-NLS-1$
		} else if (type == HexOutlineElement.MAPPINGITEM) {
			if (0 == children.size()) {
				return key + ": " + page.toString(); //$NON-NLS-1$
			}
            return key;
		} else if (type == HexOutlineElement.SEQUENCEITEM) {
			if (0 == children.size()) {
				return page.toString();
			}
            return ""; //$NON-NLS-1$
		}

		return super.toString();
	}

}
