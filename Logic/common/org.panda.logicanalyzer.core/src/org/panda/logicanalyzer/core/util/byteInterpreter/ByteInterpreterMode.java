package org.panda.logicanalyzer.core.util.byteInterpreter;

/**
 * This enumeration describes the modes a byte interpreter can run in.
 */
public enum ByteInterpreterMode {

	/**
	 * The interpreter tries to guess the bit length by searching for the shortest low/high
	 */
	Auto,

	/**
	 * The user provides the bit length
	 */
	FixedLength,

	/**
	 * A second channel is used as clock signal
	 */
	ClockSignal;

}
