/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.rcp.lifecycle.license;

import com.lembed.lite.studio.rcp.licensing.base.LicensingUtils;
import com.lembed.lite.studio.rcp.licensing.core.ILicensedProduct;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.UUID;



public class LicensedProduct implements ILicensedProduct {

	public static final UUID PRODUCT_ID = UUID
			.fromString("cb4811fd-64a2-4e95-a758-ac9c716a6c31");

	public static byte[] PUBLIC_KEY = { 48, -126, 1, -73, 48, -126, 1, 44, 6,
			7, 42, -122, 72, -50, 56, 4, 1, 48, -126, 1, 31, 2, -127, -127, 0,
			-3, 127, 83, -127, 29, 117, 18, 41, 82, -33, 74, -100, 46, -20,
			-28, -25, -10, 17, -73, 82, 60, -17, 68, 0, -61, 30, 63, -128, -74,
			81, 38, 105, 69, 93, 64, 34, 81, -5, 89, 61, -115, 88, -6, -65,
			-59, -11, -70, 48, -10, -53, -101, 85, 108, -41, -127, 59, -128,
			29, 52, 111, -14, 102, 96, -73, 107, -103, 80, -91, -92, -97, -97,
			-24, 4, 123, 16, 34, -62, 79, -69, -87, -41, -2, -73, -58, 27, -8,
			59, 87, -25, -58, -88, -90, 21, 15, 4, -5, -125, -10, -45, -59, 30,
			-61, 2, 53, 84, 19, 90, 22, -111, 50, -10, 117, -13, -82, 43, 97,
			-41, 42, -17, -14, 34, 3, 25, -99, -47, 72, 1, -57, 2, 21, 0, -105,
			96, 80, -113, 21, 35, 11, -52, -78, -110, -71, -126, -94, -21,
			-124, 11, -16, 88, 28, -11, 2, -127, -127, 0, -9, -31, -96, -123,
			-42, -101, 61, -34, -53, -68, -85, 92, 54, -72, 87, -71, 121, -108,
			-81, -69, -6, 58, -22, -126, -7, 87, 76, 11, 61, 7, -126, 103, 81,
			89, 87, -114, -70, -44, 89, 79, -26, 113, 7, 16, -127, -128, -76,
			73, 22, 113, 35, -24, 76, 40, 22, 19, -73, -49, 9, 50, -116, -56,
			-90, -31, 60, 22, 122, -117, 84, 124, -115, 40, -32, -93, -82, 30,
			43, -77, -90, 117, -111, 110, -93, 127, 11, -6, 33, 53, 98, -15,
			-5, 98, 122, 1, 36, 59, -52, -92, -15, -66, -88, 81, -112, -119,
			-88, -125, -33, -31, 90, -27, -97, 6, -110, -117, 102, 94, -128,
			123, 85, 37, 100, 1, 76, 59, -2, -49, 73, 42, 3, -127, -124, 0, 2,
			-127, -128, 58, -114, -32, -48, 15, 95, 21, -103, -107, 51, 96, 9,
			-84, -27, 114, -81, 124, 79, -5, -18, -18, -62, -34, -33, -60, 69,
			-120, -108, -18, -1, 1, -127, -100, -52, 95, -28, -123, -106, -9,
			-49, 112, -55, 110, 66, 40, 68, 71, 59, -27, -57, 96, -41, -90, 45,
			-106, -106, -101, 116, 98, 12, -91, 127, 89, 14, 103, 113, -12, 80,
			-118, 118, -20, 71, -74, 74, -109, 1, -105, 126, 124, -90, 40, 110,
			64, -31, 60, 37, -6, -72, 124, -101, -25, -94, -122, -19, 21, 93,
			27, -54, -103, -74, 126, 17, -111, -59, -19, 63, 78, -71, -59, 78,
			114, -25, -86, 37, -125, -103, 76, -120, 115, -65, -119, -57, 34,
			98, -124, -93, -62, -70 };

	@Override
	public UUID getId() {
		return PRODUCT_ID;
	}

	@Override
	public String getName() {
		return "Licensed LiteSTUDIO";
	}

	@Override
	public String getVendor() {
		return "Lembed Electronic Co.,Ltd.";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public PublicKey getPublicKey() {
		try {
			return LicensingUtils.readPublicKeyFromBytes(PUBLIC_KEY);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
