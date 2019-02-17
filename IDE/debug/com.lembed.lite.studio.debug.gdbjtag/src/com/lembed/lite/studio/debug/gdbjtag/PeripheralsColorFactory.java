package com.lembed.lite.studio.debug.gdbjtag;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.themes.IColorFactory;

/**
 * @author Administrator
 *
 */
public class PeripheralsColorFactory implements IColorFactory {

	private String fThemeColorName;
	private String fPreferenceName;

	/**
	 * @param themeColorName String
	 * @param preferenceName String
	 */
	protected PeripheralsColorFactory(String themeColorName, String preferenceName) {

		fThemeColorName = themeColorName;
		fPreferenceName = preferenceName;
	}

	@Override
	public RGB createColor() {

		String value;
		value = InstanceScope.INSTANCE.getNode("org.eclipse.ui.workbench").get(fThemeColorName, null); //$NON-NLS-1$
		if (value == null) {
			value = DefaultScope.INSTANCE.getNode(GdbActivator.PLUGIN_ID).get(fPreferenceName, "0,0,0"); //$NON-NLS-1$
		}

		String a[] = value.split(","); //$NON-NLS-1$
		RGB rgb;
		try {
			rgb = new RGB(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2]));
		} catch (Exception e) {
			rgb = new RGB(0, 0, 0);
		}
		return rgb;
	}
	// ------------------------------------------------------------------------
}
