package com.lembed.lite.studio.qemu.view;


public interface IemultorStore {

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
	//public List<AnalysisResultProperty> getProperties();

	/**
	 * @return the frames of this result (can be empty but never null)
	 */
	//public List<TimedFrame> getFrames();

}
