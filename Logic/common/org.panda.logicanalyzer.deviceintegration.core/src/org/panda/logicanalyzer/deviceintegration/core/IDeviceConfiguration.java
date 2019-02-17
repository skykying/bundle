package org.panda.logicanalyzer.deviceintegration.core;

import java.util.List;

/**
 * A device configuration describes all properties necessary to use a certain
 * capturing device.
 */
public interface IDeviceConfiguration {

	/**
	 * Returns the value of the given property or null if the property is
	 * unknown.
	 *
	 * @param name
	 *            the fully qualified property name in form of
	 *            <tt>group.property</tt>
	 * @return the properties value or null
	 */
	public Object getProperty(String name);

	/**
	 * @return a list of the names of all known properties
	 */
	public List<String> getKnownProperties();

	/**
	 * Returns true if the property is known by this configuration, false
	 * otherwise.
	 *
	 * @param name
	 *            the fully qualified property name in form of
	 *            <tt>group.property</tt>
	 * @return true if the property's know, false if not
	 */
	public boolean isPropertyKnown(String name);

}
