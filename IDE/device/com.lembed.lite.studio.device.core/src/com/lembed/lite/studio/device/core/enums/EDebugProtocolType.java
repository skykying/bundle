package com.lembed.lite.studio.device.core.enums;

import com.lembed.lite.studio.device.common.CmsisConstants;

/**
 * 	Enumeration value corresponding "DataPatchAccessTypeEnum" in pdsc file schema
 *  @see ICpDataPatch
 */
public enum EDebugProtocolType {
	SWD,
	JTAG,
	CJTAG;

	/**
	 * @param str value of <code>"attr"</code> attribute 
	 * @return corresponding enumeration value
	 */
	public static EDebugProtocolType fromString(final String str) {
		if(str == null)
			return SWD;
		switch(str) {
		case  CmsisConstants.JTAG:
			return JTAG;
		case  CmsisConstants.CJTAG:
			return CJTAG;
		case  CmsisConstants.SWD:
		default:
			return SWD;
		}
	}
	
	public static String toString(EDebugProtocolType type) {
		switch( type) {
		case JTAG:
			return CmsisConstants.JTAG;
		case CJTAG:
			return CmsisConstants.CJTAG;
		default:
			return CmsisConstants.SWD;
		}
	}

	@Override
	public String toString() {
		return toString(this);
	}
		
}
