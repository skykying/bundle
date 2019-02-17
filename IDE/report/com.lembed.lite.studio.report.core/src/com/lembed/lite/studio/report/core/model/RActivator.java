package com.lembed.lite.studio.report.core.model;

import java.util.List;

import org.eclipse.ui.plugin.AbstractUIPlugin;

@SuppressWarnings("javadoc")
public class RActivator extends AbstractUIPlugin {
	
	private static  RActivator plugin;

	/**
	 * Show message.
	 *
	 * @param string the string
	 */
	public static void showMessage(String string) {
		// TODO Auto-generated method stub
		
	}


	public static RActivator getDefault() {
		return plugin;
	}


	public List<AbstractMetric> getMetrics() {
		// TODO Auto-generated method stub
		return null;
	}
}
