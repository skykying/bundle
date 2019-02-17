package com.lembed.lite.studio.manager.analysis.stack;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
@SuppressWarnings("javadoc")
public class StackAnalyzerPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.manager.analysis.stack"; //$NON-NLS-1$

	// The shared instance
	private static StackAnalyzerPlugin plugin;

	/**
	 * The constructor
	 */
	public StackAnalyzerPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static StackAnalyzerPlugin getDefault() {
		return plugin;
	}

	
	
    public static final String FILE = "icons/obj16/file_obj.gif"; //$NON-NLS-1$
	public static final String NAMESPACE = "icons/obj16/namespace_obj.gif"; //$NON-NLS-1$
	public static final String FUNCTION = "icons/obj16/function_obj.gif"; //$NON-NLS-1$
	public static final String CLASS = "icons/obj16/class_obj.gif"; //$NON-NLS-1$
	public static final String VIEWMODE_LOGICAL = "icons/view16/class_hi.gif"; //$NON-NLS-1$
	public static final String FILESYSTEM = "icons/obj16/filesyst.gif"; //$NON-NLS-1$
	public static final String COLLAPSE_ALL = "icons/elcl16/collapseall.gif"; //$NON-NLS-1$
	public static final String EXPAND_ALL = "icons/elcl16/expandall.gif"; //$NON-NLS-1$
	public static final String STRUCT = "icons/obj16/struct_obj.gif"; //$NON-NLS-1$
	public static final String H_FILE = "icons/obj16/h_file_obj.gif"; //$NON-NLS-1$
	public static final String C_FILE = "icons/obj16/c_file_obj.gif"; //$NON-NLS-1$
	public static final String FOLDER = "icons/obj16/fldr_obj.gif"; //$NON-NLS-1$
	public static final String METHOD_PUBLIC = "icons/obj16/method_public_obj.gif"; //$NON-NLS-1$
	public static final String C_PROJECT = "icons/obj16/cprojects.gif"; //$NON-NLS-1$
	public static final String WORKSPACE = "icons/obj16/workspace.gif"; //$NON-NLS-1$
	public static final String TAGCLOUD = "icons/obj16/tagcloud.gif"; //$NON-NLS-1$
	public static final String UNION = "icons/obj16/union_obj.gif"; //$NON-NLS-1$
	public static final String EXPORT = "icons/view16/templates.gif"; //$NON-NLS-1$
	public static final String TEXT = "icons/elcl16/segment_edit.gif"; //$NON-NLS-1$
	public static final String HTML = "icons/elcl16/th_single.gif"; //$NON-NLS-1$
	public static final String DBG	="icons/obj16/exec_dbg_obj.gif"; //$NON-NLS-1$
	public static final String METRIC	="icons/obj16/flask.gif"; //$NON-NLS-1$
	public static final String CONFIGURE	="icons/obj16/import_settings_wiz.gif"; //$NON-NLS-1$
	public static final String SUMMARY	="icons/obj16/variable_obj.gif"; //$NON-NLS-1$
	public static final String REFRESH ="icons/elcl16/refresh_nav.gif"; //$NON-NLS-1$
	
	
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

}
