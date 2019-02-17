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

import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptElement;
import com.lembed.lite.studio.manager.analysis.core.linker.LinkerScriptHelper;
import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;
import com.lembed.lite.studio.manager.analysis.editor.linker.Activator;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;


public class LinkerFileContentOutlinePage extends ContentOutlinePage {

	private Object input;
	private IDocumentProvider documentProvider;
	private LinkerFileEditor linkerFileEditor;

	public static final String LINKERFILESEGMENT = "_____LINKER_Element";

	protected class ContentProvider implements ITreeContentProvider {

		protected LinkerScriptHelper lHelper = new LinkerScriptHelper();
		protected List<LinkerFileOutlineElement> linkerFileDocuments = new ArrayList<LinkerFileOutlineElement>();
		protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(LINKERFILESEGMENT);

		public void parse(IDocument document) {

			String content = document.get();
			linkerFileDocuments.clear();
			
			//int index = 0;
			List<LinkerScriptElement> sections = lHelper.composeAll(content);
			if(sections != null){
				 for( LinkerScriptElement section : sections) {
					 LinkerFileOutlineElement le = new LinkerFileOutlineElement(section, document);
					 
					 //index++;
					 // root element add here 
					 linkerFileDocuments.add(le);
				 }
			}
		}

		@Override
		public Object[] getChildren(Object element) {

			if (element instanceof LinkerFileOutlineElement) {
				return ((LinkerFileOutlineElement) element).children.toArray();
			}

			return null;
		}

		@Override
		public Object getParent(Object element) {

			if (element instanceof LinkerFileOutlineElement) {
				return ((LinkerFileOutlineElement) element).parent;
			}

			return null;
		}

		@Override
		public boolean hasChildren(Object element) {

			if (element instanceof LinkerFileOutlineElement) {
				return (((LinkerFileOutlineElement) element).children.size() == 0) ? false : true;
			}

			return false;
		}

		@Override
		public Object[] getElements(Object arg0) {

			if (linkerFileDocuments != null) {
				return linkerFileDocuments.toArray();
			}
			return null;
		}

		@Override
		public void dispose() {

			if (linkerFileDocuments != null) {
				linkerFileDocuments.clear();
				linkerFileDocuments = null;
			}
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			EditorLog.logger.fine("Input to the Outline view was changed.");

			if (oldInput != null) {
				IDocument document = documentProvider.getDocument(oldInput);
				if (document != null) {
					try {
						document.removePositionCategory(LINKERFILESEGMENT);
					} catch (BadPositionCategoryException x) {
						x.printStackTrace();
					}
					document.removePositionUpdater(positionUpdater);
				}
			}

			if (newInput != null) {
				IDocument document = documentProvider.getDocument(newInput);
				if (document != null) {
					document.addPositionCategory(LINKERFILESEGMENT);
					document.addPositionUpdater(positionUpdater);
					parse(document);
				}
			}
		}

	}

	public LinkerFileContentOutlinePage(IDocumentProvider provider, LinkerFileEditor editor) {
		super();
		documentProvider = provider;
		linkerFileEditor = editor;
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
		viewer.setLabelProvider(new LinkerFileEditorStyledLabelProvider(linkerFileEditor.getColorManager()));
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
			linkerFileEditor.resetHighlightRange();
		} else {
			LinkerFileOutlineElement segment = (LinkerFileOutlineElement) ((IStructuredSelection) selection)
					.getFirstElement();
			int start = segment.position.getOffset();
			int length = segment.position.getLength();
			try {
				linkerFileEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				linkerFileEditor.resetHighlightRange();
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

	private void log(String msg){
		System.out.println(msg);
	}
}
