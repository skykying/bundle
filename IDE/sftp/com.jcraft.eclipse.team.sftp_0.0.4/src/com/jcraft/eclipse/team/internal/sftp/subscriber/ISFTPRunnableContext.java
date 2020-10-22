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
package com.jcraft.eclipse.team.internal.sftp.subscriber;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;

import com.jcraft.eclipse.sftp.IClient;
import com.jcraft.eclipse.sftp.ISFtpRunnable;

/**
 * This interface defines the API necessary for running multiple SFTP commands
 * over a single connection
 */
public interface ISFTPRunnableContext{

  /**
   * Get the URL for this context. This defines where the context is connected
   * to.
   * @return a URL
   */
  URL getUrl();

  /**
   * Return the relative path of the given URL to the URL of this context
   * @param url
   * @return
   */
  IPath getRelativePath(URL url);

  /**
   * Execute the given runnable in this context. The context sets up the connection
   * to the SFTP server. Any paths used should be relative to the context.
   * @param runnable an ISFtpRunnable
   * @param monitor a progress monitor
   */
  void run(ISFtpRunnable runnable, IProgressMonitor monitor)
      throws TeamException;

  /**
   * Return a handle to the client that has been opened by this context. This will
   * return <code>null</code> unless nested inside an invokation or <code>run</code>.
   * @return
   */
  IClient getOpenClient();

}
