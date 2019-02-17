package com.lembed.lite.studio.report.core.model;

import org.eclipse.jface.preference.IPreferenceStore;

import com.lembed.lite.studio.report.core.BuildAnalyzerPlugin;

@SuppressWarnings("javadoc")
public class ReportStore {

	public static IPreferenceStore getStore() {
		return BuildAnalyzerPlugin.getDefault().getPreferenceStore();
	}
}
