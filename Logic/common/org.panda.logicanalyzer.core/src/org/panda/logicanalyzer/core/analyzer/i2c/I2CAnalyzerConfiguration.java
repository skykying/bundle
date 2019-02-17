package org.panda.logicanalyzer.core.analyzer.i2c;

/**
 * The configuration of a byte interpreter.
 */
public class I2CAnalyzerConfiguration {

	/**
	 * The default configuration of the byte interpreter
	 */
	public static final I2CAnalyzerConfiguration DEFAULT = new I2CAnalyzerConfiguration();

	/**
	 * Classes outside of this package are not supposed to create
	 * instances directly.
	 */
	I2CAnalyzerConfiguration() {}


	/**
	 * The first line (SDA/SCL descision is done automatically)
	 */
	private int lineA;

	/**
	 * The second line (SDA/SCL descision is done automatically)
	 */
	private int lineB;


	/**
	 * @return The first line (SDA/SCL descision is done automatically)
	 */
	public int getLineA() {
		return lineA;
	}

	/**
	 * @return The second line (SDA/SCL descision is done automatically)
	 */
	public int getLineB() {
		return lineB;
	}

	/**
	 * @param lineA the lineA to set
	 */
	void setLineA(int lineA) {
		this.lineA = lineA;
	}

	/**
	 * @param lineB the lineB to set
	 */
	void setLineB(int lineB) {
		this.lineB = lineB;
	}

}
