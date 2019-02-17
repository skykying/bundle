package org.panda.logicanalyzer.ui.internal.pipeline;

import org.panda.logicanalyzer.ui.pipeline.IPipelineElementConfigurationProvider;

import org.panda.logicanalyzer.core.IFactoryDescriptor;

/**
 * A pipeline element factory can create pipeline elements using a UI based
 * configuration.
 */
public class ConfiguredPipelineElementFactoryDescriptor<V> {

	private final IFactoryDescriptor<V> factoryDescriptor;
	private final IPipelineElementConfigurationProvider configurationProvider;

	public ConfiguredPipelineElementFactoryDescriptor(IFactoryDescriptor<V> factoryDescriptor,
	        IPipelineElementConfigurationProvider configurationProvider) {

		this.factoryDescriptor = factoryDescriptor;
		this.configurationProvider = configurationProvider;
	}

	public IPipelineElementConfigurationProvider getConfigurationProvider() {
		return configurationProvider;
	}

	public IFactoryDescriptor<V> getFactoryDescriptor() {
		return factoryDescriptor;
	}

	public String toString() {
		return factoryDescriptor.getName();
	}

}
