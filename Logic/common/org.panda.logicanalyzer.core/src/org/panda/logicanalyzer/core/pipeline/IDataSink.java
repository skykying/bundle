package org.panda.logicanalyzer.core.pipeline;

import org.eclipse.core.runtime.CoreException;

/**
 * A data sink is the end of a pipeline. It accepts data and visualizes it somehow.
 */
public interface IDataSink {

	/**
	 * Accepts a data packet and visualizes it somehow.
	 *
	 * @param packet The packet to visualize
	 * @throws CoreException Thrown if something goes wrong during data processing
	 */
	public void accept(IDataPacket packet) throws CoreException;

}
