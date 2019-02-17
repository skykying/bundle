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

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexManager;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;
import com.lembed.lite.studio.manager.analysis.editor.color.ColorManager;
import com.lembed.lite.studio.manager.analysis.editor.linker.Activator;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;


public class MapFileEditor extends TextEditor {

	private ColorManager colorManager;
	private IdleTimer idleTimer;
	MapEditorSourceViewerConfiguration sourceViewerConfig;
	private MapContentOutlinePage contentOutline;

	private final IPropertyChangeListener propertyChangeListener = new PreferenceChangeListener(this);
	private final String SCOPE_ID = "com.lembed.lite.studio.manager.analysis.editor.map.mapEditScope";//$NON-NLS-1$
	private IFile file = null;

	private IProject project = null;
	private IIndex index = null;

	public static Boolean enableChecker = false;

	public MapFileEditor() {
		super();
		colorManager = new ColorManager();

		String[] array = new String[] { SCOPE_ID};
		//setKeyBindingScopes(array);
	}

	public void dispose() {

		try {
			colorManager.dispose();

			if (idleTimer != null) {
				idleTimer.dispose();
			}
			Activator.getDefault().getPreferenceStore().removePropertyChangeListener(propertyChangeListener);

			super.dispose();
		} catch (InterruptedException e) {
			EditorLog.logException(e);
		}
	}

	protected void addDocumentIdleListener(IDocumentIdleListener listener) {
		if (idleTimer != null) {
			idleTimer.addListener(listener);
		} else {
			EditorLog.logError( "Failed adding listener for idle document since listener is null" );
			EditorLog.logger.severe( "listener is null" );
		}
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();

		MapEditorSourceViewerConfiguration jsvc = createSourceViewerConfiguration();
		setSourceViewerConfiguration(jsvc);

		sourceViewerConfig = jsvc;

		IIndexManager indexManager = CCorePlugin.getIndexManager();
		if (project != null) {
			ICProject cproject = CoreModel.getDefault().create(project);
			if (cproject != null) {
				try {
					index = indexManager.getIndex(cproject);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}


	protected MapEditorSourceViewerConfiguration createSourceViewerConfiguration() {
		MapEditorSourceViewerConfiguration jsvc = null;

		/*
		 * Check for custom LinkerFileEditorSourceViewerConfiguration contributed via
		 * extension point
		 */
		boolean contribFound = false;

		// otherwise default to the base LinkerFileEditorSourceViewerConfiguration
		if ( !contribFound ) {
			jsvc = new MapEditorSourceViewerConfiguration();
		}
		return jsvc;
	}

	public void createPartControl(Composite parent) {

		super.createPartControl(parent);

		idleTimer = new IdleTimer(getSourceViewer(), Display.getCurrent());
		idleTimer.start();

		IDocumentIdleListener listener = new IDocumentIdleListener() {
			public void editorIdle( ISourceViewer sourceViewer ) {
				sourceViewer.invalidateTextPresentation();
			}
		};
		addDocumentIdleListener(listener);

		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(propertyChangeListener);

		IEditorInput input  = getEditorInput();
		file = ResourceUtil.getFile(input);

		if (file != null) {
			project = file.getProject();
		}
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class required) {

		if (IContentOutlinePage.class.equals(required)) {
			if (contentOutline == null) {
				contentOutline = new MapContentOutlinePage(getDocumentProvider(), this );
				if (getEditorInput() != null)
					contentOutline.setInput(getEditorInput());
			}
			return contentOutline;
		}

		return super.getAdapter(required);
	}

	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		if (enableChecker) {
			markErrors();
		}

		updateContentOutline();
	}

	public void doSaveAs() {
		super.doSaveAs();
		if (enableChecker) {
			markErrors();
		}

		updateContentOutline();
	}

	void updateContentOutline() {
		if (contentOutline != null) {
			contentOutline.update();
		}
	}

	/**
	 * Re-initialize the editor after changes have been done to the preferences.
	 */
	void reinitialize() {

		if ( getSourceViewer() instanceof SourceViewer) {
			((SourceViewer) getSourceViewer()).unconfigure();
			initializeEditor();
			getSourceViewer().configure(sourceViewerConfig);
		} else {
			String msg = "Expected source viewer to be of type SourceViewer, but is wasn't. ";
			msg += "Might cause problems with preferences.";
			EditorLog.logger.warning(msg);
		}
	}

	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	/**
	 * Parses the file and adds error markers for any syntax errors found in the document.
	 * Old error markers are removed before any new markers are added.
	 */
	void markErrors() {

		IEditorInput editorInput = this.getEditorInput();

		//if the file is not part of a workspace it does not seems that it is a IFileEditorInput
		//but instead a FileStoreEditorInput. Unclear if markers are valid for such files.
		if ( !( editorInput instanceof IFileEditorInput ) ) {
			EditorLog.logError("Marking errors not supported for files outside of a project." );
			EditorLog.logger.info("editorInput is not a part of a project." );
			return;
		}

		IFile file = ( (IFileEditorInput) editorInput ).getFile();

		//start by clearing all the old markers.
		int depth = IResource.DEPTH_INFINITE;
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, depth);
		} catch (CoreException e) {
			EditorLog.logException(e);
			EditorLog.logger.warning("Failed to delete markers:\n" + e.toString() );
		}

		IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();

		String severity = prefs.getString(PreferenceConstants.VALIDATION);
		if ( PreferenceConstants.SYNTAX_VALIDATION_IGNORE.equals(severity) ) {
			EditorLog.logger.info("Possible syntax errors ignored due to preference settings");
			return;
		}

		int markerSeverity = IMarker.SEVERITY_ERROR;
		if (PreferenceConstants.SYNTAX_VALIDATION_WARNING.equals(severity)) {
			markerSeverity = IMarker.SEVERITY_WARNING;
		}

	}



	public boolean formatDocument() {

		return true;
	}

	/**
	 * This method overrides the corresponding method in AbstractDecoratedTextEditor.
	 * It is used to force tab to spaces conversion even if the the preference
	 * EDITOR_SPACES_FOR_TABS is not set to true. If this method is not present
	 * pressing the tab key will cause the focus to jump out of the editor and on to button
	 * row if the EDITOR_SPACE_FOR_TABS is not true.
	 * @return Always returns true
	 */
	protected boolean isTabsToSpacesConversionEnabled() {
		return true;
	}

	protected ColorManager getColorManager() {
		return colorManager;
	}
	
	public MapContentOutlinePage getContentOutlinePage() {
		return contentOutline;
	}

	private void log(String msg) {
		System.out.println(msg);
	}

}
