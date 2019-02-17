package org.panda.logicanalyzer.core.analyzer.i2c;

import org.panda.logicanalyzer.core.analyzer.Severity;
import org.panda.logicanalyzer.core.analyzer.TimedFrame;

/**
 * An I2C data packet containing real payload data.
 */
public class I2CDataPacket extends I2CPacket {

	/**
	 * The payload data
	 */
	private final byte data;

	I2CDataPacket(long time, byte data) {
		super(time);
		this.data = data;
	}

	/**
	 * @return the payload data
	 */
	public byte getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see org.panda.logicanalyzer.core.analyzer.i2c.I2CPacket#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		Object result;

		if (adapter == TimedFrame.class) {
			result = new TimedFrame(Severity.Info, getTime(), data, null);
		} else {
			result = super.getAdapter(adapter);
		}

		return result;
	}

}
