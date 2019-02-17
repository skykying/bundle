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

import java.io.StringReader;
import java.util.*;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;
import com.lembed.lite.studio.manager.analysis.editor.binary.Activator;
import com.lembed.lite.studio.manager.analysis.editor.hex.parser.HexParser;
import com.lembed.lite.studio.manager.analysis.editor.hex.parser.Page;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;

/**
 * The Class HexContentOutlinePage.
 */
public class HexContentOutlinePage extends ContentOutlinePage implements IPropertyListener {

	private Object input;
	private IDocumentProvider documentProvider;
	public static final String HEXSEGMENT = "_____HEX_Element"; //$NON-NLS-1$
	private IActionBars bar;
	private HexEditor _hexEditor;

	/**
	 * The Class HexOutlineLabelProvider.
	 */
	public class HexOutlineLabelProvider implements ILabelProvider {

		@Override
		public Image getImage(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(Object element) {
			return "name"; //$NON-NLS-1$
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {

		}

	}

	protected class HexContentProvider implements ITreeContentProvider {

		protected HexParser hexParser = new HexParser();
		protected List<HexOutlineElement> hexDocuments = new ArrayList<>();
		protected IPositionUpdater positionUpdater = new DefaultPositionUpdater(HEXSEGMENT);

		public void parse(IDocument document) {

			String content = document.get();
			hexDocuments.clear();

			for (Page page : hexParser.composeAll(new StringReader(content))) {
				HexOutlineElement ye = new HexOutlineElement(page, document);
				hexDocuments.add(ye);
			}

		}

		@Override
        public Object[] getChildren(Object element) {

			if (element instanceof HexOutlineElement) {
				return ((HexOutlineElement) element).children.toArray();
			}

			return null;
		}

		@Override
        public Object getParent(Object element) {

			if (element instanceof HexOutlineElement) {
				return ((HexOutlineElement) element).parent;
			}
			return null;
		}

		@Override
        public boolean hasChildren(Object element) {

			if (element instanceof HexOutlineElement) {
				return (((HexOutlineElement) element).children.size() == 0) ? false : true;
			}

			return false;
		}

		@Override
        public Object[] getElements(Object arg0) {

			if (hexDocuments != null) {
				return hexDocuments.toArray();
			}
			return null;
		}

		@Override
        public void dispose() {
			if (hexDocuments != null) {
				hexDocuments.clear();
				hexDocuments = null;
			}
		}

		@Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			EditorLog.logger.fine("Input to the Outline view was changed."); //$NON-NLS-1$

			if (oldInput != null) {
				IDocument document = documentProvider.getDocument(oldInput);
				if (document != null) {
					try {
						document.removePositionCategory(HEXSEGMENT);
					} catch (BadPositionCategoryException x) {
						x.printStackTrace();
					}
					document.removePositionUpdater(positionUpdater);
				}
			}

			if (newInput != null) {
				IDocument document = documentProvider.getDocument(newInput);
				if (document != null) {
					document.addPositionCategory(HEXSEGMENT);
					document.addPositionUpdater(positionUpdater);
					parse(document);
				}

			}
		}

	}

	public HexContentOutlinePage(IDocumentProvider provider, HexEditor editor) {
		super();
		documentProvider = provider;
		_hexEditor = editor;
	}

	@Override
    public void createControl(Composite parent) {

		super.createControl(parent);

		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();

		int expandLevels = AbstractTreeViewer.ALL_LEVELS;
		if (!(prefs.getBoolean(PreferenceConstants.AUTO_EXPAND_OUTLINE))) {
			expandLevels = 0;
		}

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new HexContentProvider());
		viewer.setLabelProvider(new HexOutlineLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setAutoExpandLevel(expandLevels);

		if (input != null) {
			setInput(input);
		}

		bar = this.getSite().getActionBars();
		// bar.getToolBarManager().add(new SortAction());
		bar.updateActionBars();
	}

	public void setInput(Object input) {
		this.input = input;
		update();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		EditorLog.logger.info("Select in the outline view changed"); //$NON-NLS-1$

		super.selectionChanged(event);

		ISelection selection = event.getSelection();
		if (selection.isEmpty()) {
			// hexEditor.resetHighlightRange();
		} else {
			HexOutlineElement segment = (HexOutlineElement) ((IStructuredSelection) selection).getFirstElement();
			int start = segment.position.getOffset();
			int length = segment.position.getLength();
			try {
				_hexEditor.setHighlightRange(start, length, true);
			} catch (IllegalArgumentException x) {
				_hexEditor.resetHighlightRange();
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

	@Override
	public void propertyChanged(Object source, int propId) {
		if (propId == IEditorPart.PROP_DIRTY) {
			// this.getTreeViewer().setInput(((XMLEditor)source).getRootElement());
		}
	}

}
