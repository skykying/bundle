package org.panda.logicanalyzer.core.analyzer.i2c;

import org.eclipse.core.runtime.IAdaptable;

/**
 * An I2C packet is a piece of information discovered during I2C protocol analysis.
 */
public abstract class I2CPacket implements IAdaptable {

	/**
	 * The time this packet was discovered completely
	 */
	private final long time;

	I2CPacket(long time) {
		this.time = time;
	}

	/**
	 * @return the time this packet was discovered completely
	 */
	protected long getTime() {
		return time;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}

}
