package org.panda.logicanalyzer.core;

/**
 * A factory descriptor describes a factory in terms of name and descriptional text.
 *
 *
 *
 * @param <V>
 */
public interface IFactoryDescriptor<V> {

	/**
	 * @return  the unique ID of this factory
	 */
	public String getId();

	/**
	 * @return The name of this factory
	 */
	public String getName();

	/**
	 * @return the descriptional text of this factory
	 */
	public String getDescription();

	/**
	 * @return the factory itself
	 */
	public V getFactory();

}
