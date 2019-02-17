package com.lembed.unit.test.parser;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class UTParserPlugin extends AbstractUIPlugin {

	/** The plug-in Symbol - must match what is in plugin.xml */
	public static final String PLUGIN_ID = "com.lembed.unit.test.parser"; //$NON-NLS-1$

	/**
	 * The shared (singleton) instance of the plugin, initialized on construction
	 */
	/* @NonNull */
	private static UTParserPlugin plugin;

	/** The option to print out verbose diagnostic information for the plug-in */
	public static boolean verbose = false;

	/**
	 * The constructor
	 */
	public UTParserPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	/* @NonNull *//* @Pure */
	public static UTParserPlugin getDefault() {
		return plugin;
	}

}
