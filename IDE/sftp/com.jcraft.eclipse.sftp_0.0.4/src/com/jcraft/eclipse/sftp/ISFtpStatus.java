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

/**
 * This interface defines the status codes that are used in {@link SFtpExceptions} to
 * better describe the failure.
 * 
 * <p>
 * This interface is not intended to be implemented by clients.
 * </p>
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
public interface ISFtpStatus{

  /**
   * Status code indicating that a general I/O exception 
   * occurred. The payload of the
   * status is an instance of IOException.
   */
  int IO_FAILED=1001;

  /**
   * Status code indicating that the host is unknown.
   */
  int UNKNOWN_HOST=1002;

  /**
   * Status code indicating that the protocol
   * of a url is not sftp
   */
  int INVALID_URL_PROTOCOL=1003;

  /**
   * Status code indicating that the client could not
   * parse a response from the server.
   */
  int MALFORMED_SERVER_RESPONSE=1003;

  public static final int DIRECTORY_EXIST=521;

  public static final int DOES_NOT_EXIST=550;

  public static final int CONNECTION_LOST=601;

  public static final int BAD_RESPONSE_CODE=602;

}
