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

import java.io.File;
import java.net.URI;
import java.text.MessageFormat;



import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.lembed.lite.studio.manager.analysis.editor.hex.HexFileManager;
import com.lembed.lite.studio.manager.analysis.editor.hex.HexManager;
import com.lembed.lite.studio.manager.analysis.editor.hex.Messages;
import com.lembed.lite.studio.manager.analysis.editor.hex.ui.HexEditorControl;

//import org.eclipse.ui.texteditor.DocumentProviderRegistry;

/**
 * The Class HexEditor.
 */
public class HexEditor extends EditorPart {
	private HexEditorControl hexEditorControl = null;
	private HexFileManager hexFileManager = null;
	private IFile iFile = null;
	private File ioFile = null;
	private boolean isDirty = false;
	private HexContentOutlinePage contentOutline;

	/**
	 * Instantiates a new hex editor.
	 */
	public HexEditor() {
		super();
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#doSave(IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		IEditorInput input = getEditorInput();
		IFile iFileTmp = null;
		File ioFileTmp = null;
		HexManager.log("Saving input type: " + input.getClass().getName()); //$NON-NLS-1$

		if (input instanceof IFileEditorInput) {
			//
			// Input file found in Eclipse Workspace - good
			//
			iFileTmp = ((IFileEditorInput) input).getFile();
			ioFileTmp = iFileTmp.getRawLocation().toFile();
			HexManager.log("Saving project file: " + ioFileTmp.getAbsolutePath()); //$NON-NLS-1$
		} else if (input instanceof IPathEditorInput) {
			//
			// Input file is outside the Eclipse Workspace
			//
			IPathEditorInput pathEditorInput = (IPathEditorInput) input;
			IPath path = pathEditorInput.getPath();
			ioFileTmp = path.toFile();
			HexManager.log("Saving exernal file: " + ioFileTmp.getAbsolutePath()); //$NON-NLS-1$
		} else if (input instanceof IURIEditorInput) {
			//
			// Input file is outside the Eclipse Workspace
			//
			IURIEditorInput uriEditorInput = (IURIEditorInput) input;
			URI uri = uriEditorInput.getURI();
			ioFileTmp = new File(uri);
			HexManager.log("Saving exernal file: " + ioFileTmp.getAbsolutePath()); //$NON-NLS-1$
		} else {
			//
			// ERROR - Unhandled input type
			//
			HexManager.log("Unhandled input type!"); //$NON-NLS-1$
		} // else

		hexFileManager.saveFile(this, hexEditorControl.getHexTable(), ioFileTmp, monitor, false);
		if (iFileTmp != null) {
			try {
				iFileTmp.refreshLocal(IResource.DEPTH_ONE, monitor);
			} catch (CoreException e) {
				HexManager.log(e);
			}
		}
		
		updateContentOutline();
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		performSaveAs(getProgressMonitor());
		updateContentOutline();
	}

	/**
	 * @param progressMonitor the progress monitor to be used
	 * @see org.eclipse.ui.editors.text.TextEditor#performSaveAs()
	 */
	protected void performSaveAs(IProgressMonitor progressMonitor) {
		Shell shell = getSite().getShell();
		IEditorInput input = getEditorInput();

		SaveAsDialog dialog = new SaveAsDialog(shell);

		IFile iFileOriginal = null;
		String stringOriginal = null;
		if (input instanceof IFileEditorInput) {
			iFileOriginal = ((IFileEditorInput) input).getFile();
		} else if (input instanceof IPathEditorInput) {
			stringOriginal = ((IPathEditorInput) input).getPath().toFile().getName();
		} else if (input instanceof IURIEditorInput) {
			stringOriginal = new File(((IURIEditorInput) input).getURI()).getName();
		}

		if (iFileOriginal != null) {
			//
			// File belongs to Eclipse Workspace
			//
			dialog.setOriginalFile(iFileOriginal);
		} else if (stringOriginal != null) {
			//
			// External file
			//
			dialog.setOriginalName(stringOriginal);
		} else {
			//
			// Shouldn't occur - ERROR
			//
			HexManager.log("ERROR in performSaveAs()!"); //$NON-NLS-1$
			String message = MessageFormat.format(Messages.HexEditor_6, (Object[]) null);
			dialog.setErrorMessage(null);
			dialog.setMessage(message, IMessageProvider.WARNING);
			return;
		} // else

		dialog.create();

		IDocumentProvider provider = DocumentProviderRegistry.getDefault().getDocumentProvider(input);

		if (provider == null) {
			//
			// Editor has programatically been closed while the dialog was open
			//
			return;
		} // if

		if (provider.isDeleted(input) && (iFileOriginal != null || stringOriginal != null)) {
			String message = MessageFormat.format("The original file ''{0}'' has been deleted.", new Object[] { ((iFileOriginal != null) ? iFileOriginal.getName() : stringOriginal) }); //$NON-NLS-1$
			dialog.setErrorMessage(null);
			dialog.setMessage(message, IMessageProvider.WARNING);
		} // if

		if (dialog.open() == Window.CANCEL) {
			if (progressMonitor != null) {
				progressMonitor.setCanceled(true);
			}
			return;
		}
		
		Hashing.sha256().hashString("abc", Charsets.UTF_8).toString();
		
		IPath filePath = dialog.getResult();
		if (filePath == null) {
			if (progressMonitor != null) {
				progressMonitor.setCanceled(true);
			}
			return;
		}
		IWorkspace workspace = iFile.getWorkspace();
		IFile file = workspace.getRoot().getFile(filePath);
		final IEditorInput newInput = new FileEditorInput(file);
		boolean success = false;

		provider.aboutToChange(newInput);

		hexFileManager.saveFile(this, hexEditorControl.getHexTable(), file.getRawLocation().toFile(), progressMonitor, true);
		try {
			file.refreshLocal(IResource.DEPTH_ONE, progressMonitor);
		} catch (CoreException e) {
			HexManager.log(e);
		}

		success = true;
		provider.changed(newInput);
		if (success) {
			setInput(newInput);
		}

		if (progressMonitor != null) {
			progressMonitor.setCanceled(!success);
		}
	}

	/**
	 * Returns the progress monitor related to this editor.
	 * @return the progress monitor related to this editor
	 */
	protected IProgressMonitor getProgressMonitor() {
		IProgressMonitor pm = null;

		IStatusLineManager manager = getStatusLineManager();
		if (manager != null) {
			pm = manager.getProgressMonitor();
		}

		return pm != null ? pm : new NullProgressMonitor();
	}

	/**
	 * Returns the status line manager of this editor.
	 * @return the status line manager of this editor
	 */
	private IStatusLineManager getStatusLineManager() {
		IEditorActionBarContributor contributor = getEditorSite().getActionBarContributor();
		if (!(contributor instanceof EditorActionBarContributor)) {
			return null;
		}

		IActionBars actionBars = ((EditorActionBarContributor) contributor).getActionBars();
		if (actionBars == null) {
			return null;
		}

		return actionBars.getStatusLineManager();
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#init(IEditorSite, IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)	throws PartInitException {
		setSite(site);
		setInput(input);
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Sets the "dirty" flag.
	 * @param isDirty true if the file has been modified otherwise false
	 */
	public void setDirty(boolean isDirty) {
		//
		// Set internal "dirty" flag
		//
		this.isDirty = isDirty;
		//
		// Fire the "property change" event to change file's status within Eclipse IDE
		//
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	/**
	 * Gets the file read-only flag value
	 * @return true if the file is read-only otherwise false
	 */
	public boolean isReadOnly() {
		return (ioFile != null) ? !ioFile.canWrite() : true;
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		//
		// Make an instance of the main plugin GUI control
		//
		hexEditorControl = new HexEditorControl(this, parent);

		//
		// Make an instance of the file manager
		//
		hexFileManager = new HexFileManager(parent, ioFile);

		//
		// Load file into the table
		//
		if (!hexFileManager.loadFile(hexEditorControl.getHexTable())) {
			ioFile = null;
		}

		//
		// Adjust the panel size with data table
		//
		hexEditorControl.getHexTable().pack();

		//
		// Refresh GUI, set cursor position
		//
		hexEditorControl.getHexTable().setSelection(0);
		if (hexEditorControl.getHexTable().getItemCount() > 0) {
			hexEditorControl.getCursor().setSelection(0, 1);
		}
		hexEditorControl.updateStatusPanel();
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		hexEditorControl.setFocus();
	}

	/**
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		String fileName = null;
		boolean isReadOnly = false;

		HexManager.log("Input type: " + input.getClass().getName()); //$NON-NLS-1$

		if (input instanceof IFileEditorInput) {
			//
			// Input file found in Eclipse Workspace - good
			//
			iFile = ((IFileEditorInput) input).getFile();
			ioFile = iFile.getRawLocation().toFile();
			fileName = iFile.getName();
			isReadOnly = iFile.isReadOnly();
			HexManager.log("Opening project file: " + ioFile.getAbsolutePath()); //$NON-NLS-1$
		} else if (input instanceof IPathEditorInput) {
			//
			// Input file is outside the Eclipse Workspace
			//
			IPathEditorInput pathEditorInput = (IPathEditorInput) input;
			IPath path = pathEditorInput.getPath();
			ioFile = path.toFile();
			fileName = ioFile.getName();
			isReadOnly = !ioFile.canWrite();
			HexManager.log("Opening exernal file: " + ioFile.getAbsolutePath()); //$NON-NLS-1$
		} else if (input instanceof IURIEditorInput) {
			//
			// Input file is outside the Eclipse Workspace
			//
			IURIEditorInput uriEditorInput = (IURIEditorInput) input;
			ioFile = new File(uriEditorInput.getURI());
			fileName = ioFile.getName();
			isReadOnly = !ioFile.canWrite();
			HexManager.log("Opening exernal file: " + ioFile.getAbsolutePath()); //$NON-NLS-1$
		} else {
			//
			// ERROR - Unhandled input type
			//
			HexManager.log("Unhandled input type!"); //$NON-NLS-1$
		} // else

		if (fileName != null) {
			setPartName(fileName + ((isReadOnly) ? Messages.HexEditor_12 : "")); //$NON-NLS-2$
		} // if
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class required) {
		
		IEditorInput input = getEditorInput();
		IDocumentProvider provider = DocumentProviderRegistry.getDefault().getDocumentProvider(input);
		
		if (IContentOutlinePage.class.equals(required)) {
			if (contentOutline == null) {
				contentOutline = new HexContentOutlinePage(provider, this );
				if (getEditorInput() != null){
					contentOutline.setInput(getEditorInput());
					this.addListenerObject(contentOutline); 
				}
			}
			return contentOutline;
		}
		
		return super.getAdapter(required);
	}

	void updateContentOutline(){
		if (contentOutline != null) {
			contentOutline.update();
		}
	}
	
	/**
	 * Called when the editor is to be disposed
	 */
	@Override
	public void dispose() {
		if (hexEditorControl != null) {
			hexEditorControl.dispose();
			hexEditorControl = null;
		}
		if (contentOutline != null) {
			this.removeListenerObject(contentOutline);
		}
		super.dispose();
		System.gc();
	}

	/**
	 *
	 */
	public void undo() {
		hexEditorControl.undo();
		setDirty(hexEditorControl.canUndo());
	}

	/**
	 *
	 */
	public void redo() {
		hexEditorControl.redo();
		setDirty(hexEditorControl.canUndo());
	}

	public HexEditorControl getControl() {
		return hexEditorControl;
	}

	/**
	 * Sets the highlight range.
	 *
	 * @param start the start
	 * @param length the length
	 * @param b the b
	 */
	public void setHighlightRange(int start, int length, boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void resetHighlightRange() {
		// TODO Auto-generated method stub
		
	}
}
