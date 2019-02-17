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
package com.lembed.lite.studio.report.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import com.lembed.lite.studio.core.AbstractUIActivator;
import com.lembed.lite.studio.report.core.manager.BuildMonitor;


/**
 * 
 * http://www.vogella.com/tutorials/EclipseCommandsAdvanced/article.html
 * 
 * 
 * The activator class controls the plug-in life cycle
 */
public class BuildAnalyzerPlugin extends AbstractUIPlugin {

	/** The Constant PLUGIN_ID. */
	public static final String PLUGIN_ID = "com.lembed.lite.studio.report.core"; //$NON-NLS-1$

	// The shared instance
	private static BuildAnalyzerPlugin plugin;
	
	private BuildMonitor monitor = new BuildMonitor();
	
	   /**
   	 * Gets the bundle id.
   	 *
   	 * @return the bundle id
   	 */
   	public String getBundleId() {
	   return PLUGIN_ID;
	}
	   
	/**
	 * Instantiates a new builds the analyzer plugin.
	 */
	public BuildAnalyzerPlugin() {
		super();
		monitor.register();
		plugin = this;
	}
	
	/**
	 * Gets the active page.
	 *
	 * @return the active page
	 */
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
	
	/**
	 * Gets the current project.
	 *
	 * @return the current project
	 */
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
	
	/**
	 * Gets the selected project.
	 *
	 * @return the selected project
	 */
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
	
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		monitor.deRegister();
		plugin = null;		
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static BuildAnalyzerPlugin getDefault() {
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


    @Override
    public boolean isDebugging() {
        return true;
    }

    /**
     * Log.
     *
     * @param status the status
     */
    public static void log(IStatus status) {
        getInstance().getLog().log(status);
    }

    /**
     * Log.
     *
     * @param e the e
     */
    public static void log(Throwable e) {
        log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1,
                ">>> BuildAnalysis " + "Internal Error", e)); //$NON-NLS-1$ //$NON-NLS-2$
    }
 
    private static AbstractUIActivator getInstance() {
        
        return null;
    }

    /**
     * Log.
     *
     * @param message the message
     */
    public static void log(String message) {
        if(getInstance().isDebugging()) {
        log(new Status(IStatus.ERROR, getInstance().getBundleId(), 1, ">>> BuildAnalysis  " + message, //$NON-NLS-1$
                null));
        }
    }
}
