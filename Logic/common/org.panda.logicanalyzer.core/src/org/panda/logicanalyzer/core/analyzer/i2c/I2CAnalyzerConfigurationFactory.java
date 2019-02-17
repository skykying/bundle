package org.panda.logicanalyzer.core.analyzer.i2c;


/**
 * This class creates new byte interpreter configurations.
 */
public class I2CAnalyzerConfigurationFactory {

	/**
	 * @return a factory initialized with the {@link I2CAnalyzerConfiguration#DEFAULT} values.
	 */
	public static I2CAnalyzerConfigurationFactory applyDefaults() {
		return new I2CAnalyzerConfigurationFactory();
	}

	/**
	 * The configuration we're building
	 */
	private I2CAnalyzerConfiguration configuration = new I2CAnalyzerConfiguration();

	/**
	 * Instanciation should only be possible using {@link #applyDefaults()}.
	 */
	private I2CAnalyzerConfigurationFactory() { }

	/**
	 * Be aware that after a call to this method, all setters will throw an {@link UnsupportedOperationException}.
	 *
	 * @return the constructed configuration
	 */
	public I2CAnalyzerConfiguration apply() {
		I2CAnalyzerConfiguration result = configuration;
		configuration = null;
		return result;
	}

	/**
	 * @param lineA the lineA to set
	 */
	public I2CAnalyzerConfigurationFactory setLineA(int lineA) {
		if (configuration == null) throw new UnsupportedOperationException();
		configuration.setLineA(lineA);
		return this;
	}

	/**
	 * @param lineB the lineB to set
	 */
	public I2CAnalyzerConfigurationFactory setLineB(int lineB) {
		if (configuration == null) throw new UnsupportedOperationException();
		configuration.setLineB(lineB);
		return this;
	}

}
