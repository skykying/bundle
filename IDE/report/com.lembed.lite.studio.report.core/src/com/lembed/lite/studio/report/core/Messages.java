/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.report.core;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings("javadoc")
public class Messages extends NLS {
	private static final String BUNDLE_NAME = Messages.class.getName();
	
	public static String CodanMetric_Export;
	/*
	 * 1. copy&paste content from MetricLabels.properties 2. search for
	 * ^(.*)=.*$ and replace all by public static String $1;
	 */
	public static String LSLOCMetric_name;
	public static String LSLOCMetric_description;
	public static String LSLOC_Maximum_Per_File;
	public static String LSLOC_Maximum_Per_Function;
	public static String NumberMembersMetric_name;
	public static String NumberLinesMetric_name;
	public static String NumberLinesMetric_description;
	public static String NumberMembersMetric_description;
	public static String NBMEMBERS_Maximum_Per_Type;
	public static String McCabeMetric_name;
	public static String McCabeMetric_description;
	public static String MCCABE_Maximum_Per_Function;
	public static String NumberParamsMetric_name;
	public static String NumberParamsMetric_description;
	public static String NBPARAMS_Maximum_Per_Function;
	public static String EfferentCouplingMetric_name;
	public static String EfferentCouplingMetric_description;
	public static String EFFERENTCOUPLING_Maximum_Per_Type;
	public static String REPORT_CHECKER_PROBLEMS;
	public static String NBLINES_Maximum_Per_File;
	public static String PREF_EFFERENTCOUPLING_MAXIMUM_PER_TYPE;

	public static String PREF_LSLOC_MAXIMUM_PER_FILE;
	public static String PREF_LSLOC_MAXIMUM_PER_FUNCTION;
	public static String PREF_MCCABE_MAXIMUM_PER_FUNCTION;
	public static String PREF_NBLINES_MAXIMUM_PER_TYPE;
	public static String PREF_NBMEMBERS_MAXIMUM_PER_TYPE;

	public static String PREF_NBPARAMS_MAXIMUM_PER_FUNCTION;
	public static String Job_TitleRunningAnalysis;
	
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
