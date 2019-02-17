package com.lembed.lite.studio.device.project.ui;

import org.eclipse.core.expressions.PropertyTester;

import com.lembed.lite.studio.device.project.utils.ProjectUtils;

public class LitePropertyTester extends PropertyTester {

	public LitePropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (property.equalsIgnoreCase("liteFile")) { //$NON-NLS-1$
			return ProjectUtils.getLiteFileResource(receiver) != null;
		}
		return false;
	}
}
