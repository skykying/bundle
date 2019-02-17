package org.panda.logicanalyzer.core.analyzer.i2c;

import java.util.Collections;
import java.util.List;

import org.panda.logicanalyzer.core.analyzer.IAnalysisResult;
import org.panda.logicanalyzer.core.analyzer.TimedFrame;

/**
 * This class describes the result of an I2C analyzer session. Check the subclasses
 * for further information.
 *
 * @see I2CUnsuccessfulAnalysisResult
 * @see I2CSuccessfulAnalysisResult
 */
public abstract class I2CAnalyzerResult implements IAnalysisResult {

	/**
	 * We want noone outside this package to create subclasses. That's also the
	 * reason why we're not using an interface here.
	 */
	I2CAnalyzerResult() { }

	/* (non-Javadoc)
	 * @see org.panda.logicanalyzer.core.analyzer.IAnalysisResult#getTitle()
	 */
	public String getTitle() {
		return "I2C analysis result";
	}

	/* (non-Javadoc)
	 * @see org.panda.logicanalyzer.core.analyzer.IAnalysisResult#getFrames()
	 */
	public List<TimedFrame> getFrames() {
		return Collections.emptyList();
	}

}
