/*******************************************************************************
 * Copyright (c) 2016 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/
package com.lembed.lite.studio.device.project.editors;

import org.eclipse.cdt.internal.ui.editor.asm.AsmTextEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import com.lembed.lite.studio.device.config.Messages;
import com.lembed.lite.studio.device.config.model.IConfigWizardItem;
import com.lembed.lite.studio.device.parser.ConfigWizardParser;
import com.lembed.lite.studio.device.parser.ConfigWizardScanner;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to use configuration wizard
 * </ul>
 */
@SuppressWarnings("restriction")
public class LiteConfigEditorCS extends LiteCSConfigEditor implements IResourceChangeListener {

	public final static String ID = "com.lembed.lite.studio.device.config.editors.LiteConfigEditorCS";

	/** The text editor used in page 0. */
	TextEditor editor;

	/** The document in the editor. */
	private IDocument fDocument;

	/** The resource file in the workspace */
	IFile fFile;

	/** parser job for the config wizard. */
	Job fParserJob;

	/** parser for the config wizard. */
	ConfigWizardParser fParser;

	/** True if user has changed the text before switch pages */
	boolean fNeedReparse;

	/** The tree viewer */
	TreeViewer fViewer;

	private ConfigWizardScanner scanner = null;

	IConfigWizardItem getConfigWizardItem(Object obj) {
		if (obj instanceof IConfigWizardItem) {
			return (IConfigWizardItem) obj;
		}
		return null;
	}

	/**
	 * Creates a multi-page editor example.
	 * http://www.ai2100.net/show/29122549583564598066.html
	 */
	public LiteConfigEditorCS() {
		super();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace != null) {
			workspace.addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
		}

		fParserJob = new Job(Messages.ConfigEditor_ParsingConfigWizard) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.ConfigEditor_ParsingConfigWizard, 100);
				monitor.worked(98);
				if (fParser != null) {
					fParser.parse();
				}
				fNeedReparse = false;
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		if (fParserJob == null) {
			return;
		}

		fParserJob.setUser(true);
		fParserJob.setPriority(Job.INTERACTIVE);
		fParserJob.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						IConfigWizardItem root = fParser.getConfigWizardRoot();
						if (root == null) { // parsing error
							editor.selectAndReveal(fParser.getParsingErrorOffset(), 0);
						} else if (fViewer.getControl() != null && !fViewer.getControl().isDisposed()) {
							if (fViewer != null) {
								fViewer.setInput(root);
							}
						}
					}
				});
			}
		});

	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		fDocument = getDocumentProvider().getDocument(getEditorInput());
		scanner = new ConfigWizardScanner(editor instanceof AsmTextEditor);
		if (scanner == null) {
			return;
		}

		fParser = new ConfigWizardParser(scanner, fDocument);
		if (fParser == null) {
			return;
		}

		if (fParser.containWizard()) {
			fParserJob.schedule();
			hookDocumentChangeListener();
		}
	}

	protected void hookDocumentChangeListener() {

		if (fDocument == null) {
			return;
		}

		fDocument.addDocumentListener(new IDocumentListener() {
			@Override
			public void documentChanged(DocumentEvent event) {
				fNeedReparse = true;
			}

			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
				// do nothing
			}
		});
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors. Subclasses
	 * may extend.
	 */
	@Override
	public void dispose() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		if (workspace != null) {
			workspace.removeResourceChangeListener(this);
		}
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the text
	 * for page 0's tab, and updates this multi-page editor's input to correspond to
	 * the nested editor's.
	 */
	@Override
	public void doSaveAs() {

		doSaveAs();
		setPartName(getTitle());
		setInput(getEditorInput());

	}

	public void gotoMarker(IMarker marker) {
		IDE.gotoMarker(this, marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method checks
	 * that the input is an instance of <code>IFileEditorInput</code>.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		// @2017.6.11, if enable below, it can not open GCC header file in GCC path.
		// if (!(editorInput instanceof IFileEditorInput)) {
		// throw new PartInitException(Messages.ConfigEditor_InvalidEditorInput);
		// }
		//
		super.init(site, editorInput);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * Closes all project files on project close.
	 */
	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		// consider only POST_CHANGE events
		if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
			return;
		}
		IResourceDelta resourseDelta = event.getDelta();
		IResourceDeltaVisitor deltaVisitor = new IResourceDeltaVisitor() {
			@Override
			public boolean visit(IResourceDelta delta) {
				IResource resource = delta.getResource();
				int type = resource.getType();
				if (type == IResource.ROOT) {
					return true; // workspace => visit children
				}

				int kind = delta.getKind();
				int flags = delta.getFlags();

				if (type == IResource.FILE && kind == IResourceDelta.REMOVED && resource.equals(fFile)) {

					if ((flags & IResourceDelta.MOVED_TO) == IResourceDelta.MOVED_TO) {
						// renamed
						IPath newPath = delta.getMovedToPath();
						IFile r = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(newPath);
						final FileEditorInput fileEditorInput = new FileEditorInput(r);
						Display.getDefault().asyncExec(() -> setInput(fileEditorInput));
						return false;
						// project deleted
					} else if (flags == 0 || (flags & IResourceDelta.MARKERS) != 0) { // markers have changed
						Display.getDefault().asyncExec(() -> {
							LiteConfigEditorCS.this.getEditorSite().getPage().closeEditor(LiteConfigEditorCS.this,
									true);
						});
						return false;
					}
				}
				return true;
			}
		};

		try {
			resourseDelta.accept(deltaVisitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		super.setFocus();
	}

}
