/*******************************************************************************
 * Copyright (c) 2015 ARM Ltd. and others
 * Copyright (c) 2017 LEMBED
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eclipse Project - generation from template
 * ARM Ltd and ARM Germany GmbH - application-specific implementation
 * LEMBED - adapter for LiteSTUDIO
 *******************************************************************************/
package com.lembed.lite.studio.device.ui.editors;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import org.eclipse.cdt.core.settings.model.ICStorageElement;
import org.eclipse.cdt.internal.ui.editor.asm.AsmTextEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.INavigationLocationProvider;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.TextSelectionNavigationLocation;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.config.Messages;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.events.ILiteEventListener;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.events.LitePairEvent;
import com.lembed.lite.studio.device.core.info.ICpConfigurationInfo;
import com.lembed.lite.studio.device.core.lite.ILiteModelController;
import com.lembed.lite.studio.device.core.lite.LiteModel;
import com.lembed.lite.studio.device.core.parser.CpConfigParser;
import com.lembed.lite.studio.device.core.storage.CStorageConfigParser;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
import com.lembed.lite.studio.device.ui.CpStringsUI;
import com.lembed.lite.studio.device.utils.StorageUtils;

/**
 * An example showing how to create an LITE configuration multi-page editor.
 * This example has 4 pages:
 * <ul>
 * <li>page 0 contains an LiteManagerWidget
 * <li>page 1 contains an LiteDesviceInfo
 * <li>page 1 contains an LitePackSelectorWidget
 * </ul>
 */
@SuppressWarnings("restriction")
public class LiteEditor extends MultiPageEditorPart implements IResourceChangeListener, ILiteEventListener, INavigationLocationProvider{

	private LiteComponentPage liteComponentPage;
	private LiteDevicePage liteDevicePage;
	private LitePackPage litePackPage;

	private int componentPageIndex = 0;
	private int devicePageIndex = 1;
	private int packPageIndex = 2;
	private int activePageIndex = 0; // initially the page with index 0 is
	// activated

	ILiteModelController fModelController = null;
	CpConfigParser parser = null;
	IFile iFile;

	TextEditor editor = new LiteCSourceEditor();;

	private IDocument fDocument;

	public LiteEditor() {
		super();
		parser = new CpConfigParser();
		CpPlugIn.addLiteListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	void createLiteManagerPage() {
		liteComponentPage = new LiteComponentPage();
		Composite composite = liteComponentPage.createControl(getContainer());
		componentPageIndex = addPage(composite);
		setPageText(componentPageIndex, CpStringsUI.LiteConfigurationEditor_ComponentsTab);
	}

	void createPackSelectorPage() {
		litePackPage = new LitePackPage();
		Composite composite = litePackPage.createControl(getContainer());

		packPageIndex = addPage(composite);
		setPageText(packPageIndex, CpStringsUI.LiteConfigurationEditor_PacksTab);
	}

	void createDeviceSelectorPage() {
		liteDevicePage = new LiteDevicePage();
		Composite composite = liteDevicePage.createControl(getContainer());

		devicePageIndex = addPage(composite);
		setPageText(devicePageIndex, CpStringsUI.LiteDevicePage_Device);
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createDefaultPage() {

		try {
			IEditorInput editorInput = getEditorInput();
			if (editorInput.getName().toLowerCase().endsWith(".s")) { //$NON-NLS-1$
				editor = new AsmTextEditor();
			} else {
				editor = new LiteCSourceEditor();				
			}
			int index = addPage(editor, getEditorInput());
			setPageText(index, Messages.ConfigEditor_FirstPageText);
			setPartName(editor.getTitle());
			setTitleImage(CpPlugInUI.getImage(CpPlugInUI.ICON_FILE));

			fDocument = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.ConfigEditor_ErrorInNestedTextEditor, null,
			                      e.getStatus());
		}
	}
	

	
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		iFile = ResourceUtil.getFile(input);
		String title = input.getName();
		setPartName(title);
	}

	public static boolean hasLiteNature(IProject project) {
		try {
			if (project != null && project.isOpen() && project.hasNature(CmsisConstants.LITE_NATURE_ID)) {
				return true;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void createPages() {
		createDefaultPage();

		IProject project = iFile.getProject();
		if (project == null) {
			return;
		}

		if (hasLiteNature(project)) {
			createLiteManagerPage();
			createDeviceSelectorPage();
			createPackSelectorPage();
			ICpConfigurationInfo info = loadConfigurationInfo();
			if (info != null) {
				createConfiguration(info);
			}
			setActivePage(1);
			setTitleImage(CpPlugInUI.getImage(CpPlugInUI.ICON_FILE));
		}
	}

	protected void createConfiguration(ICpConfigurationInfo info) {
		fModelController = new LiteEditorController(new LiteModel());
		if (info != null) {
			fModelController.setConfigurationInfo(info);
			liteComponentPage.setModelController(fModelController);
			liteDevicePage.setModelController(fModelController);
			litePackPage.setModelController(fModelController);
			fModelController.addListener(this);
		}
	}

	protected ICpConfigurationInfo loadConfigurationInfo() {
		ICpConfigurationInfo root = null;
		String xmlConf = null;

		CStorageConfigParser cparser = new CStorageConfigParser();
		ICStorageElement storage = StorageUtils.getConfigurationStorage(iFile);
		if (storage == null) {
			return null;
		}
		ICStorageElement[] child = storage.getChildren();
		for (ICStorageElement e : child) {
			String name = e.getName();
			switch (name) {
			case CmsisConstants.CONFIGURATION_TAG:
				xmlConf = cparser.writeToXmlString(e);
				break;
			default:
				break;
			}
		}

		if (xmlConf != null) {
			root = (ICpConfigurationInfo) parser.parseXmlString(xmlConf);
			//log(xmlConf);
		}

//		ICpDeviceInfo deviceInfo = root.getDeviceInfo();
//		if(deviceInfo == null){
//			return null;
//		}
//
//		if(deviceInfo.attributes() == null){
//			return null;
//		}
//
		return root;
	}
	
	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		CpPlugIn.removeLiteListener(this);
		parser = null;
		fModelController = null;
		liteComponentPage = null;
		litePackPage = null;
		liteDevicePage = null;
		super.dispose();
	}

	protected String getXmlString() {
		if (fModelController != null) {
			ICpConfigurationInfo info = fModelController.getConfigurationInfo();
			return parser.writeToXmlString(info);
		}
		return CmsisConstants.EMPTY_STRING;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (fModelController != null) {
			fModelController.commit();
		}
		saveString(fDocument.get());
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {
		if (fModelController != null) {
			fModelController.commit();
		}
		saveString(fDocument.get());
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput)) {
			throw new PartInitException(CpStringsUI.LiteConfigurationEditor_InvalidInput);
		}
		super.init(site, editorInput);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (activePageIndex == newPageIndex) {
			return;
		}
		activePageIndex = newPageIndex;
		if (fModelController != null) {
			fModelController.updateConfigurationInfo();
		}
	}

	@Override
	public void handle(LiteEvent event) {
		if (fModelController == null) {
			return;
		}

		switch (event.getTopic()) {
		case LiteEvent.CONFIGURATION_MODIFIED:
		case LiteEvent.COMPONENT_SELECTION_MODIFIED:
		case LiteEvent.FILTER_MODIFIED:
			handConfigurationEvent();
			break;
		case LiteEvent.PACKS_RELOADED:
		case LiteEvent.PACKS_UPDATED:
			if (fModelController != null) {
				fModelController.reloadPacks();
			}
			break;
		case LiteEvent.GPDSC_CHANGED:
			if (fModelController != null) {
				if (fModelController.isGeneratedPackUsed((String) event.getData())) {
					fModelController.update();
				}
			}
			break;
		case LiteEvent.CONFIG_INFO_RESPONSE:
			headerStringDisplay((StringBuffer)event.getData());
			break;
		default:
			break;
		}
	}

	private void handConfigurationEvent() {
		IProject project = iFile.getProject();
		if (project == null) {
			return;
		}

		ICpConfigurationInfo info = fModelController.getConfigurationInfo();
		if (info == null) {
			return;
		}
		LitePairEvent pair = new LitePairEvent(project, info);
		if (pair != null) {
			CpPlugIn.getDefault().emitLiteEvent(LiteEvent.CONFIG_INFO_REQUEST, pair);
		}

		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	private void headerStringDisplay(StringBuffer header) {
		String headers = header.toString();
		fDocument.set(headers);
	}

	private void saveString(String msg) {
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			iFile.setContents(new ByteArrayInputStream(msg.getBytes(Charset.defaultCharset())), true, true, monitor);
			iFile.refreshLocal(IResource.DEPTH_ZERO, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isDirty() {
		if (fModelController != null) {
			return fModelController.isModified();
		}
		return false;
	}

	public ILiteModelController getModelController() {
		return fModelController;
	}

	/**
	 * Closes all project files on project close.
	 */
	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			doSave(null);
			Display.getDefault().asyncExec(() -> {
				IProject project = iFile.getProject();
				IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
				for (int i = 0; i < pages.length; i++) {
					if (project.equals(event.getResource())) {
						IEditorPart editorPart = ResourceUtil.findEditor(pages[i], iFile);
						pages[i].closeEditor(editorPart, true);
					}
				}
			});
		}
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			IResourceDelta resourseDelta = event.getDelta();
			IResourceDeltaVisitor deltaVisitor = new IResourceDeltaVisitor() {
				@Override
				public boolean visit(IResourceDelta delta) {
					IResource resource = delta.getResource();
					int type = resource.getType();
					if (type == IResource.ROOT || type == IResource.PROJECT) {
						return true; // workspace or project => visit children
					}

					int kind = delta.getKind();
					int flags = delta.getFlags();

					//log(resource.getFullPath().toOSString());

					if (type == IResource.FILE && kind == IResourceDelta.REMOVED && resource.equals(iFile)) {
						if ((flags & IResourceDelta.MOVED_TO) == IResourceDelta.MOVED_TO) {
							// renamed
							IPath newPath = delta.getMovedToPath();
							IFile r = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(newPath);
							final FileEditorInput fileEditorInput = new FileEditorInput(r);
							Display.getDefault().asyncExec(() -> setInput(fileEditorInput));
							return false;
						} else if (flags == 0) { // project deleted
							Display.getDefault().asyncExec(() -> {
								LiteEditor.this.getEditorSite().getPage().closeEditor(LiteEditor.this, true);
							});
							return false;
						}
						return false;
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
	}

	// bind to framework
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class required) {
		if (IContentOutlinePage.class.equals(required)) {
			// two outline views for Components and xml views.
			// These views must implements IContentOutlinePage or extends
			// ContentOutlinePage
			// OutlineView ov = new OutlineView();
			// return ov;
			if (getActivePage() == 1) {
				// return new XMLContentOutlinePage(this);
			}
		}

		return super.getAdapter(required);
	}
	
	@Override
	public INavigationLocation createEmptyNavigationLocation() {
		return new TextSelectionNavigationLocation(editor, false);
	}

	@Override
	public INavigationLocation createNavigationLocation() {
		return new TextSelectionNavigationLocation(editor, true);
	}


//	private void log(String msg) {
//		System.out.println(msg);
//	}

}
