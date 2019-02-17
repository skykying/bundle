package org.panda.logicanalyzer.ui.internal.pipeline;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.panda.logicanalyzer.core.IFactoryDescriptor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.panda.logicanalyzer.ui.pipeline.IPipelineElementConfigurationProvider;

/**
 * This class is capable of providing configuration provider factories
 * for all {@link IFactoryDescriptor}s which have a configuration provider registered.
 */
public class PipelineElementConfigurationProviderFactory<V> {

	/**
	 * Helper class to deal with pipeline elements which have no configuration
	 * provider registered.
	 */
	public class DummyConfigurationProvider implements IPipelineElementConfigurationProvider {

		public Map<String, Object> getConfiguration() {
			return new HashMap<String, Object>();
		}

		public WizardPage getPage() {
			return null;
		}

	}

	/**
	 * Returns a list of configuration providers for the given list of factor descriptors.
	 * Be aware that it's possible for a factory descriptor to have no configuration provider
	 * registered in which case a dummy configuration provider will be used.
	 *
	 * @param descriptors The list descriptors
	 * @return A list of configuration provider
	 */
	public Collection<ConfiguredPipelineElementFactoryDescriptor<V>> getProvider(Collection<IFactoryDescriptor<V>> descriptors) throws CoreException {
		List<ConfiguredPipelineElementFactoryDescriptor<V>> result = new LinkedList<ConfiguredPipelineElementFactoryDescriptor<V>>();

		for (IFactoryDescriptor<V> descriptor : descriptors) {
			IPipelineElementConfigurationProvider configurationProvider = findConfiurationProvider(descriptor.getId());
			if (configurationProvider == null) {
				configurationProvider = new DummyConfigurationProvider();
			}
			result.add(new ConfiguredPipelineElementFactoryDescriptor<V>(descriptor, configurationProvider));
		}

		return result;
	}

	/**
	 * Searches for a configuration provider for a pipeline element with the given ID.
	 *
	 * @param id The ID of the pipeline element
	 * @return A configuration provider or null if there's none registered.
	 */
	private IPipelineElementConfigurationProvider findConfiurationProvider(String id) throws CoreException {
		IPipelineElementConfigurationProvider result = null;

		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(org.panda.logicanalyzer.ui.Activator.PLUGIN_ID + ".pipelineElementConfigurationProvider");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension extension : extensions) {
			IConfigurationElement[] elements = extension.getConfigurationElements();
			for (IConfigurationElement element : elements) {
				if (id.equals(element.getAttribute("id"))) {
					result = (IPipelineElementConfigurationProvider) element.createExecutableExtension("class");
				}
			}
		}

		return result;
	}

}
