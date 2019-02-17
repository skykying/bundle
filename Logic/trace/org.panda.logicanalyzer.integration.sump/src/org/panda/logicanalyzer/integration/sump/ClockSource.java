package org.panda.logicanalyzer.integration.sump;

/**
 * This enumeration describes the clock source.
 *
 *
 *
 */
public enum ClockSource {

	/**
	 * Use the internal clock source
	 */
	Internal(false),

	/**
	 * Use an external clock on rising edge
	 */
	ExternalRisingEdge(true),

	/**
	 * Use an external clock on falling edge
	 */
	ExternalFallingEdge(true);


	/**
	 * If true, this clock source is an external source
	 */
	private final boolean external;

	private ClockSource(boolean external) {
		this.external = external;

	}

	/**
	 * @return true if this source is an external one
	 */
	public boolean isExternal() {
		return external;
	}

}
