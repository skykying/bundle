package org.panda.logicanalyzer.core.analyzer;

/**
 * A result property is the atomic unit of information in the analysis result model.
 * It containts the actual data.
 *
 *
 *
 */
public class AnalysisResultProperty {

	private final Severity severity;
	private final String name;
	private final String value;



	public AnalysisResultProperty(Severity severity, String name, String value) {
		this.severity = severity;
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


}
