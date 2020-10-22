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

import com.jcraft.eclipse.sftp.SFtpException;

/**
 * Exception class for non-fatal errors thrown as a result of the
 * receipt of certain server responses.
 */
public class SFtpServerException extends SFtpException{

  private static final long serialVersionUID=1L;

  public SFtpServerException(String message, int code){
    super(message, code);
  }

  public static String getTextOfMessage(String message, int code){
    String codeString=Integer.toString(code);
    if(message.startsWith(codeString)){
      message=message.substring(codeString.length()+1);
    }
    return message;
  }
}
