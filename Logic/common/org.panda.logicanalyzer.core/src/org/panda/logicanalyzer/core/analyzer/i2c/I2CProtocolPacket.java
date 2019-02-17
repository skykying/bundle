package org.panda.logicanalyzer.core.analyzer.i2c;

import org.panda.logicanalyzer.core.analyzer.Severity;
import org.panda.logicanalyzer.core.analyzer.TimedFrame;

/**
 * An I2C protocol packet denotes protocol messages (such as START condition, NACK, ACK).
 */
public class I2CProtocolPacket extends I2CPacket {

	/**
	 * The possible messages of a protocol packet
	 */
	public static enum Message {
		START, STOP, ACK, NACK, BUS_ERROR;
	}


	/**
	 * The message of this protocol packet
	 */
	private final Message message;

	I2CProtocolPacket(long time, Message message) {
		super(time);
		this.message = message;
	}

	/**
	 * @return the message of this protocol packet
	 */
	public Message getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see org.panda.logicanalyzer.core.analyzer.i2c.I2CPacket#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		Object result;

		if (adapter == TimedFrame.class) {
			result = new TimedFrame(message == Message.BUS_ERROR ? Severity.Error : Severity.Info, getTime(), (byte) 0x00, message.name());
		} else {
			result = super.getAdapter(adapter);
		}

		return result;
	}

}
