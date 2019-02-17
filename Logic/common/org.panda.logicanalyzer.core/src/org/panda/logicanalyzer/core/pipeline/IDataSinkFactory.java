package org.panda.logicanalyzer.core.pipeline;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;

/**
 * Creates a new data sink.
 */
public interface IDataSinkFactory {

	/**
	 * Builds a new data sink using the supplied configuration. If anything is wrong
	 * with the configuration this method is expected to throw an exception.
	 *
	 * @return the data sink (never null)
	 * @throws CoreException In case something's wrong with the configuration
	 */
	public IDataSink createSink(Map<String, Object> configuration) throws CoreException;

}
