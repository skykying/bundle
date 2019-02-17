package org.panda.logicanalyzer.core.util.byteInterpreter;

/**
 * The configuration of a byte interpreter.
 */
public class ByteInterpreterConfiguration {

	/**
	 * The default configuration of the byte interpreter
	 */
	public static final ByteInterpreterConfiguration DEFAULT = new ByteInterpreterConfiguration();

	/**
	 * The channel nr of the clock signal or -1 if there is no clock
	 */
	private int clockChannelNr = 0;

	/**
	 * The hard length of a bit or -1 if this length is not defined
	 */
	private long bitWidth = 0;

	/**
	 * If this flag is true, we'll interpret full bytes as char value
	 */
	private boolean showCharValue = false;

	/**
	 * If this flag is true, we'll interpret bytes as little endian, if false as
	 * big endian
	 */
	private boolean littleEndian = false;

	/**
	 * If we're in clock mode this field denotes whether to read on rising or falling egde
	 */
	private boolean onRisingEdge = true;

	/**
	 * The mode we're going to run in
	 */
	private ByteInterpreterMode mode = ByteInterpreterMode.Auto;


	public int getClockChannelNr() {
		return clockChannelNr;
	}

	public long getBitWidth() {
		return bitWidth;
	}

	public boolean isShowCharValue() {
		return showCharValue;
	}

	public boolean isLittleEndian() {
		return littleEndian;
	}

	public boolean isOnRisingEdge() {
		return onRisingEdge;
	}

	public ByteInterpreterMode getMode() {
		return mode;
	}


	/**
	 * @param clockChannelNr the clockChannelNr to set
	 */
	public void setClockChannelNr(int clockChannelNr) {
		this.clockChannelNr = clockChannelNr;
	}

	/**
	 * @param bitWidth the bitWidth to set
	 */
	public void setBitWidth(long bitWidth) {
		this.bitWidth = bitWidth;
	}

	/**
	 * @param showCharValue the showCharValue to set
	 */
	public void setShowCharValue(boolean showCharValue) {
		this.showCharValue = showCharValue;
	}

	/**
	 * @param littleEndian the littleEndian to set
	 */
	public void setLittleEndian(boolean littleEndian) {
		this.littleEndian = littleEndian;
	}

	/**
	 * @param onRisingEdge the onRisingEdge to set
	 */
	public void setOnRisingEdge(boolean onRisingEdge) {
		this.onRisingEdge = onRisingEdge;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(ByteInterpreterMode mode) {
		this.mode = mode;
	}

}
