package org.panda.logicanalyzer.deviceintegration.core;

/**
 * A mutable device configuration is a {@link IDeviceConfiguration} that can be
 * modified.
 *
 *
 *
 */
public interface IMutableDeviceConfiguration extends IDeviceConfiguration {

	/**
	 * Sets a property to a certain value. If the value is null, the property is
	 * unset.
	 *
	 * @param name
	 *            the fully qualified property name in form of
	 *            <tt>group.property</tt>
	 * @param value the value to set
	 */
	public void setProperty(String name, Object value);

}
