/*******************************************************************************
 * Copyright (c) 2015 Kaloyan Raev.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kaloyan Raev - initial API and implementation
 *******************************************************************************/
package com.lembed.lite.studio.rcp.licensing.base;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import com.lembed.lite.studio.rcp.licensing.core.ILicensedProduct;

public class LicensingUtils {

	public static boolean hasValidLicenseKey(ILicensedProduct product) {
		File[] licenseKeyFiles = getLicenseKeysFolder().listFiles();
		if (licenseKeyFiles != null) {
			for (File file : licenseKeyFiles) {
				if (file.isFile()) {
					LicenseKey licenseKey = new LicenseKey(file);
					if (licenseKey.isValidFor(product)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static PublicKey readPublicKeyFromBytes(byte[] bytes)
			throws GeneralSecurityException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
		PublicKey key = keyFactory.generatePublic(keySpec);

		return key;
	}

	public static LicenseKey[] getLicenseKeys() {
		List<LicenseKey> result = new ArrayList<LicenseKey>();

		File[] licenseKeyFiles = getLicenseKeysFolder().listFiles();
		if (licenseKeyFiles != null) {
			for (File file : licenseKeyFiles) {
				if (file.isFile()) {
					LicenseKey licenseKey = new LicenseKey(file);
					if (licenseKey.getProductId() != null) {
						result.add(licenseKey);
					}
				}
			}
		}

		return result.toArray(new LicenseKey[result.size()]);
	}

	public static File getLicenseKeysFolder() {
		return new File(System.getProperty("user.home"),
				".eclipse/com.lembed.lite.studio.rcp.licensing.base");
	}

}
