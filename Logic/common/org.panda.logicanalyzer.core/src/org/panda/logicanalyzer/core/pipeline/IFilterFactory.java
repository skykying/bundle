package org.panda.logicanalyzer.core.pipeline;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;

/**
 * Creates a new filter.
 */
public interface IFilterFactory {

	/**
	 * Builds a new filter using the supplied configuration. If anything is wrong
	 * with the configuration this method is expected to throw an exception.
	 *
	 * @return the filter (never null)
	 * @throws CoreException In case something's wrong with the configuration
	 */
	public IFilter createFilter(Map<String, Object> configuration) throws CoreException;

}
