package org.panda.logicanalyzer.deviceintegration.ui.internal;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.panda.logicanalyzer.deviceintegration.core.IConfigurationInitializer;
import org.panda.logicanalyzer.deviceintegration.core.IConfigurationValidator;
import org.panda.logicanalyzer.deviceintegration.core.IMutableDeviceConfiguration;

/**
 * This is the default implementation of a device configuration.
 */
public class DeviceConfiguration implements IMutableDeviceConfiguration {

	/**
	 * Creates a new configuration based on the given extension point. If that
	 * particular extension point has a valid initializor and or validator set,
	 * those will be used.
	 *
	 * @param element the configuration element to base the configuration on
	 * @return the new device configuration
	 * @throws CoreException In case of an error
	 */
	public static DeviceConfiguration createFrom(IConfigurationElement element) throws CoreException {
		DeviceConfiguration result = new DeviceConfiguration();

		if (element.getAttribute("initializer") != null) {
			Object initializer = element.createExecutableExtension("initializer");

			if (!(initializer instanceof IConfigurationInitializer)) {
				IStatus status = new Status(
				    IStatus.ERROR,
				    Activator.PLUGIN_ID,
				    "Registered configuration initializer does not implement the IConfigurationInitializer interface");
				throw new CoreException(status);
			} else {
				((IConfigurationInitializer)initializer).initializeConfiguration(result);
			}
		}

		if (element.getAttribute("validator") != null) {
			Object validator = element.createExecutableExtension("validator");

			if (!(validator instanceof IConfigurationValidator)) {
				IStatus status = new Status(
				    IStatus.ERROR,
				    Activator.PLUGIN_ID,
				    "Registered configuration validator does not implement the IConfigurationValidator interface");
				throw new CoreException(status);
			} else {
				result.setValidator((IConfigurationValidator) validator);
			}
		}

		result.setElement(element);

		return result;
	}


	/**
	 * This configuration's validator. Can be null
	 */
	private IConfigurationValidator validator;

	/**
	 * The configuration backend.
	 */
	private Map<String, Object> configuration = new HashMap<String, Object>();

	/**
	 * If this device configuration was created from a configuration element,
	 * that element is referenced here for later use with the
	 * {@link #validate()} method.
	 */
	private IConfigurationElement element;



	/**
	 * Sets the {@link IConfigurationElement} this configuration was created from.
	 *
	 * @param element the element to set
	 */
	private void setElement(IConfigurationElement element) {
		this.element = element;
	}

	/**
	 * Sets a validator for this configuration
	 *
	 * @param validator the validator to set
	 */
	private void setValidator(IConfigurationValidator validator) {
		this.validator = validator;
	}

	@Override
	public void setProperty(String name, Object value) {
		if (value == null) {
			configuration.remove(name);
		} else {
			configuration.put(name, value);
		}
	}

	@Override
	public List<String> getKnownProperties() {
		return new LinkedList<String>(configuration.keySet());
	}

	@Override
	public Object getProperty(String name) {
		return configuration.get(name);
	}

	@Override
	public boolean isPropertyKnown(String name) {
		return configuration.get(name) != null;
	}

	/**
	 * Validates this configuration using a possibly previously set validator.
	 * If no validator is set, an {@link IStatus#isOK()} is returned. If a
	 * validator is set, the result of that validator is returned.
	 *
	 * @return the validitiy status of this configuration
	 */
	public IStatus validate() {
		IStatus result = Status.OK_STATUS;

		if (element != null) {
			IConfigurationElement[] groups = element.getChildren("group");

			if (groups != null) {
				for (IConfigurationElement group : groups) {
					IStatus groupStatus = validateConfigurationGroup(group);
					if (!groupStatus.isOK())
						result = groupStatus;
				}
			}
		}

		if (validator != null && result.isOK()) result = validator.validateConfiguration(this);

		return result;
	}

	/**
	 * Checks if all mandatory properties in the configuration group are set.
	 *
	 * @param group The group to check
	 * @return the groups validity status
	 */
	private IStatus validateConfigurationGroup(IConfigurationElement group) {
		String groupId = group.getAttribute("id");
		IConfigurationElement[] properties = group.getChildren();

		if (properties != null) {
			for (IConfigurationElement property : properties) {
				String propertyId = property.getAttribute("id");
				String name = "[" + group.getAttribute("name") + "] " + property.getAttribute("name");

				if (propertyId != null && ("true".equals(property.getAttribute("mandatory")) || property.getAttribute("mandatory") == null)) {
					String id = groupId + "." + propertyId;

					if (!isPropertyKnown(id)) {
						return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Property \"" + name + "\" is mandatory");
					}
				}
			}
		}

		return Status.OK_STATUS;
	}

}
