package org.panda.logicanalyzer.core.util.byteInterpreter;

/**
 * Describes the result of a byte interpretation attempt
 */
public class ByteInterpreterResult {

	/**
	 * The interpreted bytes
	 */
	private final byte[] resultingBytes;

	/**
	 * The length of a single bit
	 */
	private final long bitLength;

	/**
	 * The bit start positions in nanoseconds
	 */
	private final long[] bitStarts;

	private final boolean[] bitsArray;


	/**
	 * We want noone outside of this package to create instances of this class
	 * @param bitsArray
	 */
	ByteInterpreterResult(byte[] resultingBytes, long bitLength, long[] bitStarts, boolean[] bitsArray) {
		this.resultingBytes = resultingBytes;
		this.bitLength = bitLength;
		this.bitStarts = bitStarts;
		this.bitsArray = bitsArray;
	}

	/**
	 * @return the interpreted bytes
	 */
	public byte[] getResultingBytes() {
		return resultingBytes;
	}

	/**
	 * @return the length of a single bit. This method will return -1 if that information is not available.
	 */
	public long getBitLength() {
		return bitLength;
	}

	/**
	 * @return the bit start positions in nanoseconds
	 */
	public long[] getBitStarts() {
		return bitStarts;
	}

	public boolean[] getBitsArray() {
		return bitsArray;
	}



}
