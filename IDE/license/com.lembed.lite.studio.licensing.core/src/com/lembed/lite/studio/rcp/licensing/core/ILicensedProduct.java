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
package com.lembed.lite.studio.rcp.licensing.core;

import java.security.PublicKey;
import java.util.UUID;

public interface ILicensedProduct {

	public UUID getId();

	public String getName();

	public String getVendor();

	public String getVersion();

	public PublicKey getPublicKey();

}
