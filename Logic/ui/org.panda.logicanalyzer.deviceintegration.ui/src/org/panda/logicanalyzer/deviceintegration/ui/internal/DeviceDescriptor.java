package org.panda.logicanalyzer.deviceintegration.ui.internal;

import org.panda.logicanalyzer.deviceintegration.core.IDevice;
import org.panda.logicanalyzer.deviceintegration.core.IMutableDeviceConfiguration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

/**
 * The device descriptor combines the device port, baud rate, configuration,
 * configuration initializer, configuration validator and the device itself.
 */
public class DeviceDescriptor {

	/**
	 * Computes a list of all registered devices.
	 *
	 * @return the list of devices
	 * @throws CoreException In case of an error
	 */
	public static DeviceDescriptor[] getRegisteredDevices() throws CoreException {
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint("org.panda.logicanalyzer.deviceintegration.core.device");
		IConfigurationElement[] elements = point.getConfigurationElements();

		DeviceDescriptor[] result = new DeviceDescriptor[elements.length];
		for (int i = 0; i < result.length; i++)
			result[i] = newDescriptor(elements[i]);

		return result;
	}

	/**
	 * Creates a new device descriptor based on a configuration element.
	 *
	 * @param element The element to use to build the descriptor
	 * @return the new device descriptor
	 */
	public static DeviceDescriptor newDescriptor(IConfigurationElement element) throws CoreException {
		Object device = element.createExecutableExtension("class");
		if (!(device instanceof IDevice)) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Registered device class does not implement IDevice");
			throw new CoreException(status);
		}

		IConfigurationElement[] configurationChildElement = element.getChildren("configuration");
		DeviceConfiguration configuration = null;
		if (configurationChildElement.length != 0) {
			configuration = DeviceConfiguration.createFrom(configurationChildElement[0]);
		}

		return new DeviceDescriptor(element, (IDevice) device, configuration);
	}

	/**
	 * The device itself
	 */
	private final IDevice device;

	/**
	 * The devices configuration (possibly null)
	 */
	private final DeviceConfiguration configuration;

	/**
	 * The configuration element this descriptor is based on
	 */
	private final IConfigurationElement element;


	private DeviceDescriptor(IConfigurationElement element, IDevice device, DeviceConfiguration configuration) {
		this.element = element;
		this.device = device;
		this.configuration = configuration;
	}

	public IMutableDeviceConfiguration getConfiguration() {
		return configuration;
	}

	public String getName() {
		return getElement().getAttribute("name");
	}

	public String getDescription() {
		return getElement().getAttribute("description");
	}

	public IDevice getDevice() {
		return device;
	}

	/**
	 * Validates the {@link #getConfiguration()}. If the configuration is valid,
	 * an {@link IStatus#isOK()} is returned.
	 *
	 * @return the status describing the configuration validity
	 */
	public IStatus validateConfiguration() {
		return configuration.validate();
	}

	@Override
	public String toString() {
		return getName();
	}

	public IConfigurationElement getElement() {
		return element;
	}

}
