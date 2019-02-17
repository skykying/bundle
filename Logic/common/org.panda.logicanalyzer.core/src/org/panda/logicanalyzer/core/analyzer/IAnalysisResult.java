package org.panda.logicanalyzer.core.analyzer;

import java.util.List;

/**
 * An analysis result can transform itself to several different
 * formats. This interface is used to provide a generic access
 * and frontend to analysis results.
 */
public interface IAnalysisResult {

	/**
	 * @return the title of these results
	 */
	public String getTitle();

	/**
	 * This method computes the properties of the analysis result (such as
	 * length, errors, etc.)
	 *
	 * @return the properties of this result
	 */
	public List<AnalysisResultProperty> getProperties();

	/**
	 * @return the frames of this result (can be empty but never null)
	 */
	public List<TimedFrame> getFrames();

}
