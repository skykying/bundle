package com.lembed.lite.studio.rcp.lifecycle.license;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class BundleCheck {

	public final static String UI_EXTENSION_POINT = "org.eclipse.ui.views";
	public final static String STACK_ANALYZER_ID = "com.lembed.lite.studio.analysis.stack";
	public final static String BUILD_ANALYZER_ID = "com.lembed.lite.studio.report.core.views.BuildAnalyzerView";
	
	public void checkPlugin() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(BundleCheck.UI_EXTENSION_POINT);
		if (point == null) return;
		IExtension[] extensions = point.getExtensions();
			for (IExtension ex: extensions) {
				ex.isValid();
			}
		}
}
