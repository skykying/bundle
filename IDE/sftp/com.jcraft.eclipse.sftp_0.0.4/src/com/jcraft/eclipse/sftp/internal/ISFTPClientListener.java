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

public interface ISFTPClientListener{
  /**
   * Called when a response is received from the server.
   * @param responseCode the 3 digit response
   * @param responseText the multi-line text string, lines are terminated by \n
   */
  public void responseReceived(int responseCode, String responseText);

  /**
   * Called when a request is sent to the server.
   * @param command the command sent to the server
   * @param argument the argument sent to the server
   */
  public void requestSent(String command, String argument);
}
