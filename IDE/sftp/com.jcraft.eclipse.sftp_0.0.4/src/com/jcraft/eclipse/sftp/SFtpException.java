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
package com.jcraft.eclipse.sftp;

import org.eclipse.core.runtime.*;

import com.jcraft.eclipse.sftp.internal.SFtpPlugin;

/**
 * SFTP specific exception.
 * 
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. 
 * It is being made available at this early stage to solicit feedback from pioneering 
 * adopters on the understanding that any code that uses this API will almost 
 * certainly be broken (repeatedly) as the API evolves.
 * </p>
 * 
 * @since 3.1
 */
public class SFtpException extends CoreException{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID=1L;

  /**
   * Create an SFTP exception with the given status
   * @param status the exception's status
   */
  public SFtpException(IStatus status){
    super(status);
  }

  public SFtpException(String message, int code){
    this(new Status(IStatus.ERROR, SFtpPlugin.ID, code, message, null));
  }

}
