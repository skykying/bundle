package org.panda.logicanalyzer.core.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.panda.logicanalyzer.core.Activator;
import org.panda.logicanalyzer.core.IFactoryDescriptor;


/**
 * The factory provider is able to provide a list of all factories defined at a certain extension
 * point assuming that the extension point is defined according to the factory extension point pattern.
 */
public class FactoryProvider<V> {

	/**
	 * Default implementation of the {@link IFactoryDescriptor} interface.
	 */
	private class FactoryDescriptor implements IFactoryDescriptor<V> {

		private final String name;
		private final String description;
		private final String id;
		private final V factory;

		public FactoryDescriptor(String name, String description, String id, V factory) {
			this.name = name;
			this.description = description;
			this.id = id;
			this.factory = factory;
		}

		public String getDescription() {
			return description;
		}

		public V getFactory() {
			return factory;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

	}

	/**
	 * The ID of the extension point we're dealing with
	 */
	private final String extensionPointId;

	public FactoryProvider(String extensionPointId) {
		this.extensionPointId = extensionPointId;
	}

	/**
	 * @throws CoreException If the {@link #extensionPointId} is either unknown, or its definition does not follow the factory extension point definition pattern
	 * @return a (possibly empty) collection of all factories defined
	 */
	public Collection<IFactoryDescriptor<V>> getFactories() throws CoreException {
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(extensionPointId);
		IExtension[] extensions = extensionPoint.getExtensions();
		List<IFactoryDescriptor<V>> result = new ArrayList<IFactoryDescriptor<V>>(extensions.length);

		for (IExtension extension : extensions) {
			IConfigurationElement[] configurationElements = extension.getConfigurationElements();
			for (IConfigurationElement element : configurationElements) {
				result.add(buildFactoryDescriptor(element));
			}
		}

		return result;
	}

	/**
	 * Creates a new factory descriptor based on a configuration element. The element is supposed
	 * to be defined as
	 * <pre>
	 * &lt;element name="factory"&gt;
	 * &lt;complexType&gt;
	 *    &lt;sequence&gt;
	 *       &lt;element ref="description"/&gt;
	 *    &lt;/sequence&gt;
	 *    &lt;attribute name="class" type="string" use="required"&gt;
	 *       &lt;annotation&gt;
	 *          &lt;documentation&gt;
	 *
	 *          &lt;/documentation&gt;
	 *          &lt;appinfo&gt;
	 *             &lt;meta.attribute kind="java" basedOn=":org.panda.logicanalyzer.core.pipeline.IDataSourceFactory"/&gt;
	 *          &lt;/appinfo&gt;
	 *       &lt;/annotation&gt;
	 *    &lt;/attribute&gt;
	 *    &lt;attribute name="name" type="string" use="required"&gt;
	 *       &lt;annotation&gt;
	 *          &lt;documentation&gt;
	 *
	 *          &lt;/documentation&gt;
	 *          &lt;appinfo&gt;
	 *             &lt;meta.attribute translatable="true"/&gt;
	 *          &lt;/appinfo&gt;
	 *       &lt;/annotation&gt;
	 *    &lt;/attribute&gt;
	 *    &lt;attribute name="id" type="string" use="required"&gt;
	 *       &lt;annotation&gt;
	 *          &lt;documentation&gt;
	 *             The id of this factory. The value given here will be appended to the defining plugin&apos;s id.
	 *          &lt;/documentation&gt;
	 *       &lt;/annotation&gt;
	 *    &lt;/attribute&gt;
	 * &lt;/complexType&gt;
	 * &lt;/element&gt;
	 *
	 * &lt;element name="description" type="string"&gt;
	 * &lt;annotation&gt;
	 *    &lt;appinfo&gt;
	 *       &lt;meta.element translatable="true"/&gt;
	 *    &lt;/appinfo&gt;
	 * &lt;/annotation&gt;
	 * &lt;/element&gt;
	 * </pre>
	 *
	 * @param element The element describing the factory
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private IFactoryDescriptor<V> buildFactoryDescriptor(IConfigurationElement element) throws CoreException {
		String name = element.getAttribute("name");

		IConfigurationElement[] description = element.getChildren("description");
		if (description.length == 0) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "No factory description available. Please check your factory definitions");
			throw new CoreException(status);
		}

		String id = element.getAttribute("id");

		V factory;
		try {
			factory = (V) element.createExecutableExtension("class");
		} catch (ClassCastException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Factory is not of expected type.", e);
			throw new CoreException(status);
		}

		return new FactoryDescriptor(name, description[0].getValue(), id, factory);
	}


}
