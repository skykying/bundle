package org.panda.logicanalyzer.core.util;

import java.util.Collection;
import java.util.List;

import org.panda.logicanalyzer.core.pipeline.IDataSink;
import org.panda.logicanalyzer.core.pipeline.IDataSource;
import org.panda.logicanalyzer.core.pipeline.IFilter;
import org.panda.logicanalyzer.core.pipeline.IPipeline;

/**
 * Simple default implementation of a pipeline.
 */
public class Pipeline implements IPipeline {

	private final IDataSource source;
	private final Collection<IFilter> filter;
	private final Collection<IDataSink> sinks;

	public Pipeline(IDataSource source, List<IFilter> filter, Collection<IDataSink> sinks) {
		this.source = source;
		this.filter = filter;
		this.sinks = sinks;

	}


	public Collection<IFilter> getFilter() {
		return filter;
	}


	public Collection<IDataSink> getSinks() {
		return sinks;
	}


	public IDataSource getSource() {
		return source;
	}

}
