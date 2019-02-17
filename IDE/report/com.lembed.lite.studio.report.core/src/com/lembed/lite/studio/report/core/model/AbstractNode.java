package com.lembed.lite.studio.report.core.model;

@SuppressWarnings("javadoc")
public class AbstractNode {

	public interface EditorInfo {

		String getFilePath();

		int getNodeOffset();

		int getNodeLength();

	}

	public Object getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the value of.
	 *
	 * @param metric the metric
	 * @return the value of
	 */
	public Object getValueOf(AbstractMetric metric) {
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractNode.EditorInfo getEditorInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
