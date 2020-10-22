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

import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A handle to a simple SFTP client.
 * 
 * The public methods of this interface all throw SFtpExceptions to indicate errors.
 * Recoverable errors: (subsequent operations will be processed normally)
 *   SFtpServerException and subclasses
 *     SFtpAuthenticationException
 *     SFtpFileNotAvailableException
 *     SFtpServiceNotAvailableException
 * 
 * Non-recoverable errors: (subsequent operations will probably fail)
 *   SFtpCommunicationException and subclasses
 *   ... anything else ...
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
 * 
 */
public interface IClient{

  /**
   * Constant that indicates that data transfers should
   * be done passively. This means that the client will
   * connect to the server when a data transfer is requested.
   * 
   * @see #setDataTransferMode(int)
   */
  public static final int PASSIVE_DATA_TRANSFER=1;

  /**
   * Constant that indicates that data transfers should
   * be done using a port on the client. This means that 
   * the client will listen for a connetion from the server
   * when performing a data transfer.
   * 
   * @see #setDataTransferMode(int)
   */
  public static final int PORT_DATA_TRANSFER=2;

  /**
   * Return the URL to which this client is associated.
   */
  public abstract URL getUrl();

  /**
   * Sets the username and password that ae to be used to authenticate when
   * a connection is opened. This method does not need to be invoked if the client
   * was created using a URL that contained the username and pasword. Otherwise,
   * the authentication information must be provided.
   * @param username the username
   * @param password the password
   */
  public abstract void setAuthentication(String username, String password);

  /**
   * Sets the perferred data transfer mode that will be used by the client.
   * If the mode is <code>PASSIVE_DATA_TRANSFER</code> then the client will
   * connect to the server when data transfers occur. If the mode is 
   * <code>PORT_DATA_TRANSFER</code>, then the client will listen on the
   * server provided port for a connection from the server.
   * @param mode the transfer mode (one of <code>PASSIVE_DATA_TRANSFER</code> 
   * or <code>PORT_DATA_TRANSFER</code>)
   * @param fallbackOnFailure whether a data transfer with other modes should be 
   * attempted if the requested mode fails.
   */
  /*
   public abstract void setDataTransferMode(int mode, boolean fallbackOnFailure);
   */

  /**
   * Opens and authenticates a connection to the server, if not already open.
   * This operation is potentially long running so it requires a progress monitor.
   * @param monitor a progress monitor
   */
  public abstract void open(IProgressMonitor monitor) throws SFtpException;

  /**
   * Closes a connection to the server, if not already closed.
   * @param monitor a progress monitor
   */
  public abstract void close(IProgressMonitor monitor) throws SFtpException;

  /**
   * Returns whether the client has an open connection to the server.
   * @return whether the client has an open connection to the server
   */
  public abstract boolean isOpen();

  /**
   * Execute the given runnable in a context that has access to an open connection.
   * This method supports nested invocations and is helpful when performing
   * multiple somewhat independant operations within a single connection.
   * @param runnable a runnable containing calls to this client and possibly nested runs.
   * @param monitor a progress monitor
   */
  public abstract void run(ISFtpRunnable runnable, IProgressMonitor monitor)
      throws SFtpException;

  /**
   * Changes the current remote working directory on the server. Any
   * paths provided to other methods of the client can be stated relative
   * to the current directory
   * @param directoryPath the absolute or relative path of the directory
   * @param monitor a progress monitor
   */
  public abstract void changeDirectory(String directoryPath,
      IProgressMonitor monitor) throws SFtpException;

  /**
   * Deletes the remote directory at the specified path. Deletion
   * will fail if the directory is not empty.
   * @param directoryPath the absolute or relative path of the directory
   * @param monitor the progress monitor
   */
  public abstract void deleteDirectory(String directoryPath,
      IProgressMonitor monitor) throws SFtpException;

  /**
   * Creates a remote directory at the specified path.
   * @param directoryPath the absolute or relative path of the directory
   * @param monitor the progress monitor
   */
  public abstract void createDirectory(String directoryPath,
      IProgressMonitor monitor) throws SFtpException;

  /**
   * Deletes the remote file at the specified path.
   * @param filePath the absolute or relative path of the file
   * @param monitor the progress monitor
   */
  public abstract void deleteFile(String filePath, IProgressMonitor monitor)
      throws SFtpException;

  /**
   * Get the contents of the file at the given path. If binary is <code>false</code>
   * then any line delimeters in the file will be translated to the platform line delimeter.
   * @param filePath the absolute or relative path of the file
   * @param binary whether the file is binary or text
   * @param resumeAt the position of the file to begin at when retieving contents
   * @param monitor the progress monitor
   */
  public abstract InputStream getFile(String filePath, boolean binary,
      long resumeAt, IProgressMonitor monitor) throws SFtpException;

  /**
   * Set the contents of the remote file to the given contents, creating the
   * file if it doesn't exist. The parent directory must exist for this 
   * to succeed.
   * @param filePath the absolute or relative path of the file
   * @param in the contents to be written to the file
   * @param fileSize the size of the file contents
   * @param binary whether the file is binary or text
   * @param monitor the progress monitor
   */
  public abstract void putFile(String filePath, InputStream in, long fileSize,
      boolean binary, IProgressMonitor monitor) throws SFtpException;

  /**
   * Lists the files stored in a remote directory.
   * @param directoryPath the absolute or relative path of the directory, or null (or empty string) for current
   * @param includeParents indicates whether the parent directory entries should be included. Parent directories 
   * will have names with a single or double dot.
   * @param monitor the progress monitor, or null
   * @return an array of directory entries, or an empty array if the directory is empty
   */
  public abstract IDirectoryEntry[] listFiles(String directoryPath,
      boolean includeParents, IProgressMonitor monitor) throws SFtpException;

  /**
   * Return the connection timeout in seconds for communications performed by this client.
   * @return the connection timeout in seconds for communications performed by this client
   */
  public abstract int getTimeout();

  /**
   * Set the connection timeout in seconds for communications performed by this client.
   * @param timeout the connection timeout in seconds for communications performed by this client.
   */
  public abstract void setTimeout(int timeout);
  
  public abstract String getPassword();
  
  public abstract String pwd();
  
}
