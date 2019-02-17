package org.panda.logicanalyzer.ui;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IPipeline;

import org.eclipse.ui.IEditorInput;

/**
 * The data packet editor input provides data packets to editors.
 *
 *
 *
 */
public interface IDataPacketEditorInput extends IEditorInput {

	/**
	 * Implementors are allowed to return null if this information
	 * is not available.
	 *
	 * @return the pipeline that produced this packet.
	 */
	public IPipeline getPipeline();

	/**
	 * @return the data packet contained by this input
	 */
	public IDataPacket getPacket();

}
