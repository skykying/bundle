package org.panda.logicanalyzer.core.analyzer;


/**
 * A timed frame contains some data that was captured at a certain point in time.
 *
 *
 *
 */
public class TimedFrame {

	/**
	 * The severity of this frame
	 */
	private final Severity severity;

	/**
	 * The time of this frame
	 */
	private final long time;

	/**
	 * The value of this frame
	 */
	private final byte value;

	/**
	 * The description of this frame
	 */
	private final String description;

	public TimedFrame(Severity severity, long time, byte value, String description) {
		super();
		this.severity = severity;
		this.time = time;
		this.value = value;
		this.description = description;
	}

	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @return the value
	 */
	public byte getValue() {
		return value;
	}

	public String getDescription() {
		return description == null ? "" : description;
	}

}
