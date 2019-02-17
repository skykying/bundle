package org.panda.logicanalyzer.core.pipeline;

import java.util.Collection;

/**
 * A pipeline is the main data processing element in LA. It consists of
 * a data source, priorised filters and possibly multiple data sinks.
 */
public interface IPipeline {

	/**
	 * @return the data source of this pipeline or null if it's not yet available
	 */
	public IDataSource getSource();

	/**
	 * @return a sorted list of filters of this pipeline
	 */
	public Collection<IFilter> getFilter();

	/**
	 * @return an unsorted list of sinks of this pipeline
	 */
	public Collection<IDataSink> getSinks();

}
