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

import java.io.StringReader;
import java.util.*;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;
import com.lembed.lite.studio.manager.analysis.editor.linker.Activator;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;
import com.lembed.lite.studio.manager.analysis.editor.map.model.MapParser;
import com.lembed.lite.studio.manager.analysis.editor.map.model.GccMapElement;

public class MapContentOutlinePage extends ContentOutlinePage {

	private Object input;
	private IDocumentProvider documentProvider;
	private MapFileEditor mapEditor;

	public static final String MAP_SEGMENT = "_____MAP_Element";

	protected class ContentProvider implements ITreeContentProvider {

		
		protected List<MapOutlineElement> mapElementList = new ArrayList<MapOutlineElement>();
		protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(MAP_SEGMENT);

		public void parse(IDocument document) {
			MapParser mapParser = new MapParser(document);
			mapElementList.clear();
			
			List<GccMapElement> sections = mapParser.composeAll();
			if(sections != null){
				 for( GccMapElement section : sections) {
					 MapOutlineElement le = new MapOutlineElement(section, document);
					 mapElementList.add(le);
				 }
			}
		}

		@Override
		public Object[] getChildren(Object element) {

			if (element instanceof MapOutlineElement) {
				return ((MapOutlineElement) element).children.toArray();
			}

			return null;
		}

		@Override
		public Object getParent(Object element) {

			if (element instanceof MapOutlineElement) {
				return ((MapOutlineElement) element).parent;
			}

			return null;
		}

		@Override
		public boolean hasChildren(Object element) {

			if (element instanceof MapOutlineElement) {
				return (((MapOutlineElement) element).children.size() == 0) ? false : true;
			}

			return false;
		}

		@Override
		public Object[] getElements(Object arg0) {

			if (mapElementList != null) {
				return mapElementList.toArray();
			}
			return null;
		}

		@Override
		public void dispose() {

			if (mapElementList != null) {
				mapElementList.clear();
				mapElementList = null;
			}

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			EditorLog.logger.fine("Input to the Outline view was changed.");

			if (oldInput != null) {
				IDocument document = documentProvider.getDocument(oldInput);
				if (document != null) {
					try {
						document.removePositionCategory(MAP_SEGMENT);
					} catch (BadPositionCategoryException x) {
					}
					document.removePositionUpdater(positionUpdater);
				}
			}

			if (newInput != null) {
				IDocument document = documentProvider.getDocument(newInput);
				if (document != null) {
					document.addPositionCategory(MAP_SEGMENT);
					document.addPositionUpdater(positionUpdater);
					parse(document);
				}

			}
		}

	}

	public MapContentOutlinePage(IDocumentProvider provider, MapFileEditor editor) {
		super();
		documentProvider = provider;
		mapEditor = editor;
	}

	@Override
	public void createControl(Composite parent) {

		super.createControl(parent);

		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();

		int expandLevels = TreeViewer.ALL_LEVELS;
		if (!(prefs.getBoolean(PreferenceConstants.AUTO_EXPAND_OUTLINE))) {
			expandLevels = 0;
		}

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new MapEditorStyledLabelProvider(mapEditor.getColorManager()));
		viewer.addSelectionChangedListener(this);
		viewer.setAutoExpandLevel(expandLevels);

		if (input != null) {
			setInput(input);
		}

	}

	public void setInput(Object input) {
		this.input = input;
		update();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		ISelection selection = event.getSelection();
		if (selection.isEmpty()) {
			mapEditor.resetHighlightRange();
		} else {
			MapOutlineElement segment = (MapOutlineElement) ((IStructuredSelection) selection)
					.getFirstElement();
			int start = segment.position.getOffset();
			int length = segment.position.getLength();
			try {
				mapEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				mapEditor.resetHighlightRange();
			}
		}
	}

	public void update() {
		TreeViewer viewer = getTreeViewer();

		if (viewer != null && viewer.getControl() != null && !viewer.getControl().isDisposed()) {
			viewer.setInput(input);
		}
	}

	/**
	 * Toggle collapse/expand state of all element in the outline view. If no
	 * elements are expanded all will be expaned, otherwise all will be
	 * collapsed.
	 */
	public void toggleCollapse() {
		TreeViewer viewer = getTreeViewer();

		if (0 == viewer.getVisibleExpandedElements().length) {
			viewer.expandAll();
		} else {
			viewer.collapseAll();
		}
	}

}
