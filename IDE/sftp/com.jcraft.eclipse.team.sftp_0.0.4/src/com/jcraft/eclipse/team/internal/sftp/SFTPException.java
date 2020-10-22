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
package com.jcraft.eclipse.team.internal.sftp;

import java.net.MalformedURLException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.team.core.TeamException;

import com.jcraft.eclipse.sftp.SFtpException;

/**
 * Wraps FtpExceptions in a TeamException
 */
public class SFTPException extends TeamException{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID=1L;

  public SFTPException(IStatus status){
    super(status);
  }

  public SFTPException(String message){
    super(new Status(IStatus.ERROR, SFTPPlugin.ID, UNABLE, message, null));
  }

  public SFTPException(String message, int code){
    this(new Status(IStatus.ERROR, SFTPPlugin.ID, code, message, null));
  }

  public SFTPException(String message, MalformedURLException e){
    this(new Status(IStatus.ERROR, SFTPPlugin.ID, 0, message, e));
  }

  /*
   * Static helper methods for creating exceptions
   */
  public static SFTPException wrapException(String message, CoreException e){
    return new SFTPException(new Status(IStatus.ERROR, SFTPPlugin.ID, e
        .getStatus().getCode(), message, e));
  }

  public static SFTPException wrapException(SFtpException e){
    // TODO: may bury exceptions
    return new SFTPException(new Status(IStatus.ERROR, SFTPPlugin.ID, e
        .getStatus().getCode(), e.getMessage(), e));
  }

}
