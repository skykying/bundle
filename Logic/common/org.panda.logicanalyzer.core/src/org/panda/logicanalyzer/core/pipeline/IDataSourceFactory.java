package org.panda.logicanalyzer.core.pipeline;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;

/**
 * Creates a new {@link IDataSource}.
 */
public interface IDataSourceFactory {

	/**
	 * Builds a new data source using the supplied configuration. If anything is wrong
	 * with the configuration this method is expected to throw an exception.
	 *
	 * @return the data source (never null)
	 * @throws CoreException In case something's wrong with the configuration
	 */
	public IDataSource createSource(Map<String, Object> configuration) throws CoreException;

}
