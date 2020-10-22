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

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.jcraft.eclipse.sftp.ISFtpStatus;
import com.jcraft.eclipse.sftp.SFtpException;

/**
 * Exception class for fatal communications or protocol errors thrown
 * as a result of physical I/O errors or deviations from the known protocol.
 */
public class SFtpCommunicationException extends SFtpException{
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID=1L;

  public SFtpCommunicationException(String message, IOException e){
    super(new Status(IStatus.ERROR, SFtpPlugin.ID, ISFtpStatus.IO_FAILED,
        message, e));
  }

  public SFtpCommunicationException(String message, int status){
    super(new Status(IStatus.ERROR, SFtpPlugin.ID, status, message, null));
  }
}
