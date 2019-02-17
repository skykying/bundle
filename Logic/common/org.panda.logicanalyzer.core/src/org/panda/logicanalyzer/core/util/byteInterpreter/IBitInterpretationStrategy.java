package org.panda.logicanalyzer.core.util.byteInterpreter;

import java.util.Iterator;

/**
 * A byte interpretation strategy knows how to interpret bits from a
 * data packet.
 *
 * The {@link #next()} method provides the next bit. The value returned by that method is
 * masked with <code>0x01</code> before it's used.
 */
interface IBitInterpretationStrategy extends Iterator<Byte> {

	/**
	 * @return current point in time in respect to the packet we're operating upon (in nanoseconds)
	 */
	public long getCurrentTime();

	/**
	 * @return the length of a single bit in nanoseconds. If the bit length is varying, -1 should be be returned.
	 */
	public long getBitLength();

}
