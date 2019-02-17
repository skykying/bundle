package com.lembed.lite.studio.device.core.enums;

import com.lembed.lite.studio.device.common.CmsisConstants;

/**
 * 	Enumeration value corresponding <code>"attr"</code> attribute in pdsc file
 *  @see ICpFile
 */
public enum EFileRole {
	NONE,
	COPY,
	CONFIG,
	TEMPLATE,
	INTERFACE;

	/**
	 * @param str value of <code>"attr"</code> attribute 
	 * @return corresponding enumeration value
	 */
	public static EFileRole fromString(final String str) {
		if(str == null)
			return NONE;
		switch(str) {
		case  CmsisConstants.COPY:
			return COPY;
		case  CmsisConstants.CONFIG:
			return CONFIG;
		case  CmsisConstants.TEMPLATE:
			return TEMPLATE;
		case  CmsisConstants.INTERFACE:
			return INTERFACE;
		default:
			return NONE;
		}
	}
}
