package com.lembed.lite.studio.device.core.enums;

import com.lembed.lite.studio.device.common.CmsisConstants;

/**
 * 	Enumeration value corresponding "DataPatchAccessTypeEnum" in pdsc file schema
 *  @see ICpDataPatch
 */
public enum ESequenceControlType {
	IF,
	WHILE;

	/**
	 * @param str value of <code>"attr"</code> attribute 
	 * @return corresponding enumeration value
	 */
	public static ESequenceControlType fromString(final String str) {
		if(CmsisConstants.WHILE.equals(str))
			return WHILE;
		return IF;
	}
	
	public static String toString(ESequenceControlType type) {
		if(type == WHILE)
			return CmsisConstants.WHILE;
		return CmsisConstants.IF;
	}

	@Override
	public String toString() {
		return toString(this);
	}
		
}
