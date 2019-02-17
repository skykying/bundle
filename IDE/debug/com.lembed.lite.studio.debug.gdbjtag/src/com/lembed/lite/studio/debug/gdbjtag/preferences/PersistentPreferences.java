package com.lembed.lite.studio.debug.gdbjtag.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;

/**
 * Manage a workspace preference file stored in:
 *
 * <pre>
 * workspace/.metadata/.plugins/org.eclipse.core.runtime/.settings/<plug-in-id>.prefs
 * </pre>
 *
 * Some of the values may be retrieved from the EclipseDefaults.
 */

public class PersistentPreferences {

	// ------------------------------------------------------------------------

	/** The Constant PERIPHERALS_COLOR_READONLY. */
	public static final String PERIPHERALS_COLOR_READONLY = "peripherals.color.readonly"; //$NON-NLS-1$
	
	/** The Constant PERIPHERALS_COLOR_WRITEONLY. */
	public static final String PERIPHERALS_COLOR_WRITEONLY = "peripherals.color.writeonly"; //$NON-NLS-1$
	
	/** The Constant PERIPHERALS_COLOR_CHANGED. */
	public static final String PERIPHERALS_COLOR_CHANGED = "peripherals.color.changed"; //$NON-NLS-1$
	
	/** The Constant PERIPHERALS_COLOR_CHANGED_MEDIUM. */
	public static final String PERIPHERALS_COLOR_CHANGED_MEDIUM = "peripherals.color.changed.medium"; //$NON-NLS-1$
	
	/** The Constant PERIPHERALS_COLOR_CHANGED_LIGHT. */
	public static final String PERIPHERALS_COLOR_CHANGED_LIGHT = "peripherals.color.changed.light"; //$NON-NLS-1$

	/** The Constant PERIPHERALS_CHANGED_USE_FADING_BACKGROUND. */
	public static final String PERIPHERALS_CHANGED_USE_FADING_BACKGROUND = "peripherals.changed.useFadingBackground"; //$NON-NLS-1$
	
	/** The Constant PERIPHERALS_CHANGED_USE_FADING_BACKGROUND_DEFAULT. */
	public static final boolean PERIPHERALS_CHANGED_USE_FADING_BACKGROUND_DEFAULT = true;

	// ----- Getters ----------------------------------------------------------

	/**
	 * Gets the string.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the string
	 */
	public static String getString(String key, String defaultValue) {

		String value;
		value = Platform.getPreferencesService().getString(GdbActivator.PLUGIN_ID, key, null, null);
		// GdbActivator.log("Value of " + id + " is " + value);
		if (value != null) {
			return value;
		}

		return defaultValue;
	}

	/**
	 * Gets the boolean.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the boolean
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {

		boolean value;
		value = Platform.getPreferencesService().getBoolean(GdbActivator.PLUGIN_ID, key, defaultValue, null);
		// GdbActivator.log("Value of " + id + " is " + value);
		return value;
	}

	// ----- Setters ----------------------------------------------------------

	/**
	 * Put workspace string.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public static void putWorkspaceString(String key, String value) {
		String pvalue = value.trim();

		// Access the instanceScope
		Preferences preferences = InstanceScope.INSTANCE.getNode(GdbActivator.PLUGIN_ID);
		preferences.put(key, pvalue);
	}

	/**
	 * Flush.
	 */
	public static void flush() {

		try {
			InstanceScope.INSTANCE.getNode(GdbActivator.PLUGIN_ID).flush();
		} catch (BackingStoreException e) {
			GdbActivator.log(e);
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the peripherals changed use fading background.
	 *
	 * @return the peripherals changed use fading background
	 */
	public static boolean getPeripheralsChangedUseFadingBackground() {
		return getBoolean(PERIPHERALS_CHANGED_USE_FADING_BACKGROUND, PERIPHERALS_CHANGED_USE_FADING_BACKGROUND_DEFAULT);
	}

	// ------------------------------------------------------------------------
}
