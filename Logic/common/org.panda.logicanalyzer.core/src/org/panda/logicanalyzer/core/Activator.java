package org.panda.logicanalyzer.core;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.panda.logicanalyzer.core.internal.FactoryProvider;
import org.panda.logicanalyzer.core.pipeline.IDataSinkFactory;
import org.panda.logicanalyzer.core.pipeline.IDataSourceFactory;
import org.panda.logicanalyzer.core.pipeline.IFilterFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.panda.logicanalyzer.core";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	/**
	 * Computes a list of all available data source factories in the system.
	 * 
	 * @return the list of available data source factories.
	 * @throws CoreException In case something goes wrong during list computation
	 */
	public Collection<IFactoryDescriptor<IDataSourceFactory>> getDataSourceFactories() throws CoreException {
		return new FactoryProvider<IDataSourceFactory>(PLUGIN_ID + ".dataSourceFactory").getFactories();
	}
	
	/**
	 * Computes a list of all available data source factories in the system.
	 * 
	 * @return the list of available data source factories.
	 * @throws CoreException In case something goes wrong during list computation
	 */
	public Collection<IFactoryDescriptor<IFilterFactory>> getFilterFactories() throws CoreException {
		return new FactoryProvider<IFilterFactory>(PLUGIN_ID + ".filterFactory").getFactories();
	}
	
	/**
	 * Computes a list of all available data source factories in the system.
	 * 
	 * @return the list of available data source factories.
	 * @throws CoreException In case something goes wrong during list computation
	 */
	public Collection<IFactoryDescriptor<IDataSinkFactory>> getDataSinkFactories() throws CoreException {
		return new FactoryProvider<IDataSinkFactory>(PLUGIN_ID + ".dataSinkFactory").getFactories();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
