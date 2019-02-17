package org.panda.logicanalyzer.core.pipeline;

import org.eclipse.core.runtime.CoreException;

/**
 * A data source provides data for the LA pipeline.
 */
public interface IDataSource {

	/**
	 * If something goes wrong during the acquisition,
	 * this method is expected to throw an exception. This method is expected to
	 * block until the acquisition is done.
	 *
	 * @throws CoreException In case something goes wrong during acquisition
	 */
	public IDataPacket run() throws CoreException;

}
