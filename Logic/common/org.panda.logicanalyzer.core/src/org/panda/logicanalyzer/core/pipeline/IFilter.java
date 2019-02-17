package org.panda.logicanalyzer.core.pipeline;


/**
 * A filter may modify the data processed in a pipeline. Actually filter represent the processing
 * capability of such a pipeline.
 */
public interface IFilter {

	/**
	 * Processes the given packet in this filter. Implementors can be sure that the {@link #configure(IPipelineElementConfiguration)}
	 * method has been called before this method.
	 *
	 * @param packet the packet to process
	 */
	public IDataPacket process(IDataPacket packet);

}
