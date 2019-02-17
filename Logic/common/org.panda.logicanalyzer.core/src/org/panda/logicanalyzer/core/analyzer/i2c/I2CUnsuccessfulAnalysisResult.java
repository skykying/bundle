package org.panda.logicanalyzer.core.analyzer.i2c;

import java.util.Collections;
import java.util.List;

import org.panda.logicanalyzer.core.analyzer.AnalysisResultProperty;
import org.panda.logicanalyzer.core.analyzer.Severity;

/**
 * This class describes an unsuccessful analysis result. See the {@link #getErrorMessage()}
 * why this analysis attempt failed.
 */
public class I2CUnsuccessfulAnalysisResult extends I2CAnalyzerResult {

	/**
	 * The reason of our failure
	 */
	private final String errorMessage;

	public I2CUnsuccessfulAnalysisResult(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the reason of our failure
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public List<AnalysisResultProperty> getProperties() {
		return Collections.singletonList(new AnalysisResultProperty(Severity.Error, "Reason", errorMessage));
	}

}
