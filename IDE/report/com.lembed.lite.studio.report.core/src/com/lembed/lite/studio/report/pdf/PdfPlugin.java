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
package com.lembed.lite.studio.report.pdf;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * 
 * http://www.vogella.com/tutorials/EclipseCommandsAdvanced/article.html
 *  https://www.tutorialspoint.com/pdfbox/pdfbox_adding_rectangles.htm
 * 
 * The activator class controls the plug-in life cycle
 */
public class PdfPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.report.pdf"; //$NON-NLS-1$

	// The shared instance
	private static PdfPlugin plugin;

	
	public PdfPlugin() {
		super();		
	}
	
	public static IWorkbenchPage getActivePage() {
		IWorkbench wb = PlatformUI.getWorkbench();		
		if(wb != null) {
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();			
			if(win != null) {
				IWorkbenchPage page = win.getActivePage();				
				if(page != null) {
					return page;
				}
			}
		}
		return null;
	}
	
	public static IProject getCurrentProject() {
		IProject activeProject = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IEditorPart editorPart = window.getActivePage().getActiveEditor();
		if (editorPart != null) {
			IEditorInput input = editorPart.getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				activeProject = file.getProject();
			}
		}
		return activeProject;
	}
	
	public static IProject getSelectedProject() {
		IProject activeProject = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelectionService selectionService = window.getSelectionService();
		ISelection selection = selectionService.getSelection(IPageLayout.ID_PROJECT_EXPLORER);
		if (selection instanceof StructuredSelection) {
			IResource resource = (IResource) ((StructuredSelection) selection).getFirstElement();
			activeProject = resource.getProject();
		}
		
		return activeProject;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext )
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext )
	 */
	@Override
    public void stop(BundleContext context) throws Exception {		
		plugin = null;		
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static PdfPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}



	public static void log(String msg) {
		System.out.println(msg);
	}
	
	
	public static final String FILE = "icons/obj16/file_obj.gif";
	public static final String NAMESPACE = "icons/obj16/namespace_obj.gif";
	public static final String FUNCTION = "icons/obj16/function_obj.gif";
	public static final String CLASS = "icons/obj16/class_obj.gif";
	public static final String VIEWMODE_LOGICAL = "icons/view16/class_hi.gif";
	public static final String FILESYSTEM = "icons/obj16/filesyst.gif";
	public static final String COLLAPSE_ALL = "icons/elcl16/collapseall.gif";
	public static final String EXPAND_ALL = "icons/elcl16/expandall.gif";
	public static final String STRUCT = "icons/obj16/struct_obj.gif";
	public static final String H_FILE = "icons/obj16/h_file_obj.gif";
	public static final String C_FILE = "icons/obj16/c_file_obj.gif";
	public static final String FOLDER = "icons/obj16/fldr_obj.gif";
	public static final String METHOD_PUBLIC = "icons/obj16/method_public_obj.gif";
	public static final String C_PROJECT = "icons/obj16/cprojects.gif";
	public static final String WORKSPACE = "icons/obj16/workspace.gif";
	public static final String TAGCLOUD = "icons/obj16/tagcloud.gif";
	public static final String UNION = "icons/obj16/union_obj.gif";
	public static final String EXPORT = "icons/view16/templates.gif";
	public static final String TEXT = "icons/elcl16/segment_edit.gif";
	public static final String HTML = "icons/elcl16/th_single.gif";
	public static final String DBG	="icons/obj16/exec_dbg_obj.gif";
	public static final String METRIC	="icons/obj16/flask.gif";
	public static final String CONFIGURE	="icons/obj16/import_settings_wiz.gif";
	public static final String SUMMARY	="icons/obj16/variable_obj.gif";
}
