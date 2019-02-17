package com.lembed.lite.studio.device.core.enums;

import com.lembed.lite.studio.device.common.CmsisConstants;

/**
 * 	Enumeration value corresponding "DataPatchAccessTypeEnum" in pdsc file schema
 *  @see ICpDataPatch
 */
public enum EDataPatchAccessType {
	Mem,
	DP,
	AP;

	/**
	 * @param str value of <code>"attr"</code> attribute 
	 * @return corresponding enumeration value
	 */
	public static EDataPatchAccessType fromString(final String str) {
		if(str == null)
			return Mem;
		switch(str) {
		case  CmsisConstants.AP:
			return AP;
		case  CmsisConstants.DP:
			return DP;
		case  CmsisConstants.MEM:
		default:
			return Mem;
		}
	}
	
	public static String toString(EDataPatchAccessType type) {
		switch( type) {
		case DP:
			return CmsisConstants.DP;
		case AP:
			return CmsisConstants.AP;
		default:
			return CmsisConstants.MEM;
		}
	}

	@Override
	public String toString() {
		return toString(this);
	}
		
}
