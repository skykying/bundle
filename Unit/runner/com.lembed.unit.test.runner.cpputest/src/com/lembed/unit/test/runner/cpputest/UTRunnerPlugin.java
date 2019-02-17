package com.lembed.unit.test.runner.cpputest;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;


public class UTRunnerPlugin extends Plugin {

	public static final String PLUGIN_ID = "com.lembed.unit.test.runner.cpputest";
	private static UTRunnerPlugin plugin;

	public UTRunnerPlugin() {
		super();
		plugin = this;
	}

	public static UTRunnerPlugin getDefault() {
		return plugin;
	}

	public static String getUniqueIdentifier() {
		return PLUGIN_ID;
	}

	public static void log(IStatus status) { 
		getDefault().getLog().log(status);
	}
	
	public static void log(Throwable e) {
		log(new Status(IStatus.ERROR, getUniqueIdentifier(), IStatus.ERROR, e.getMessage(), e));
	}

}
