/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Atsuhiko Yamanaka, JCraft, Inc. - adding sftp support.
 *******************************************************************************/
package com.jcraft.eclipse.sftp.internal;

import java.util.Arrays;
import java.util.TimeZone;

public class Constants{
  public static final TimeZone DEFAULT_TIMEZONE=TimeZone.getDefault();
  public static final int LF='\n';
  public static final int CR='\r';
  public static final int SPACE=' ';
  public static final int HYPHEN='-';
  public static final int ZERO='0';

  public static final int TRANSFER_PROGRESS_INCREMENT=32768;

  public static boolean IS_CRLF_PLATFORM=Arrays.equals(new byte[] {CR, LF},
      System.getProperty("line.separator").getBytes()); //$NON-NLS-1$

  private Constants(){
  }
}
