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
package com.lembed.lite.studio.manager.analysis.editor.elf.ui;

import java.io.IOException;
import java.util.*;

import org.eclipse.core.resources.IFile;
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
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;
import com.lembed.lite.studio.manager.analysis.editor.elf.Activator;
import com.lembed.lite.studio.manager.analysis.editor.elf.ElfTextEditor;
import com.lembed.lite.studio.manager.analysis.editor.elf.cdt.elf;
import com.lembed.lite.studio.manager.analysis.editor.elf.cdt.elfHelper;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;


public class ElfContentOutlinePage extends ContentOutlinePage {

	private Object input;
	private IDocumentProvider documentProvider;
	private ElfTextEditor linkerFileEditor;
	IFile fFile;

	public static final String LINKERFILESEGMENT = "_____LINKER_Element";

	protected class ContentProvider implements ITreeContentProvider {

		
		protected List<ElfOutlineElement> elfElementList = new ArrayList<ElfOutlineElement>();
		protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(LINKERFILESEGMENT);

		public void parse(IDocument document) throws IOException {

			String content = document.get();
			elfElementList.clear();
			
			elfHelper elfParser = new elfHelper(fFile.getFullPath().toOSString());
			
			int index = 0;
			elf.Symbol[] symbols = elfParser.getExternalFunctions();
			if(symbols != null){
				 for( elf.Symbol symbol : symbols) {
					 ElfOutlineElement le = new ElfOutlineElement(symbol, document);
					 
					 index++;
					 log("add element " + index);
					 // root element add here 
					 elfElementList.add(le);
				 }
			}
		}

		@Override
		public Object[] getChildren(Object element) {

			if (element instanceof ElfOutlineElement) {
				return ((ElfOutlineElement) element).children.toArray();
			}

			return null;
		}

		@Override
		public Object getParent(Object element) {

			if (element instanceof ElfOutlineElement) {
				return ((ElfOutlineElement) element).parent;
			}

			return null;
		}

		@Override
		public boolean hasChildren(Object element) {

			if (element instanceof ElfOutlineElement) {
				return (((ElfOutlineElement) element).children.size() == 0) ? false : true;
			}

			return false;
		}

		@Override
		public Object[] getElements(Object arg0) {

			if (elfElementList != null) {
				return elfElementList.toArray();
			}
			return null;
		}

		@Override
		public void dispose() {

			if (elfElementList != null) {
				elfElementList.clear();
				elfElementList = null;
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
					try {
						parse(document);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	public ElfContentOutlinePage(IDocumentProvider provider, ElfTextEditor editor) {
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
		viewer.setLabelProvider(new ElfEditorStyledLabelProvider(linkerFileEditor.getColorManager()));
		viewer.addSelectionChangedListener(this);
		viewer.setAutoExpandLevel(expandLevels);

		if (input != null) {
			setInput(input);
		}

	}

	public void setInput(Object input) {
		this.input = input;
		fFile = ResourceUtil.getFile(input);
		update();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		ISelection selection = event.getSelection();
		if (selection.isEmpty()) {
			linkerFileEditor.resetHighlightRange();
		} else {
			ElfOutlineElement segment = (ElfOutlineElement) ((IStructuredSelection) selection)
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
