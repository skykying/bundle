package com.lembed.lite.studio.device.project.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.enums.EFileRole;
import com.lembed.lite.studio.device.core.info.ICpFileInfo;
import com.lembed.lite.studio.device.project.utils.ProjectUtils;

public class LiteUpdateConfigFileTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (property.equalsIgnoreCase("canupdate") || //$NON-NLS-1$
				property.equalsIgnoreCase("canmerge")) { //$NON-NLS-1$
			IFile file = ProjectUtils.getLiteFileResource(receiver);
			if (file == null || file.isLinked()) {
				return false;
			}
			if (CmsisConstants.RTECONFIG.equals(file.getFileExtension())
					|| CmsisConstants.LITE_LITE_Components_h.equals(file.getProjectRelativePath().toString())) {
				return false;
			}
			ICpFileInfo fi = ProjectUtils.getCpFileInfo(file);
			if (fi == null) {
				return false;
			}
			if (property.equalsIgnoreCase("canmerge") && fi.getRole() != EFileRole.CONFIG) { //$NON-NLS-1$
				return false;
			}
			int versionDiff = fi.getVersionDiff();
			if (versionDiff > 2 || versionDiff < 0) {
				return true;
			}
		}
		return false;
	}

}
