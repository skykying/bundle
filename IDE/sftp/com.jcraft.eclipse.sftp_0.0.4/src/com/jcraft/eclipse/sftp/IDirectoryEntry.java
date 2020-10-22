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

import java.util.Date;

/**
 * Represents a file stored remotely.
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
public interface IDirectoryEntry{

  /**
   * Value returned by <code>getSize</code> if the size
   * of the file could not be determined.
   */
  public static final long UNKNOWN_SIZE=-1;

  /**
   * Constant that is the name used in directory entries
   * that represent the current directory being listed.
   */
  public static final String CURRENT_FOLDER="."; //$NON-NLS-1$

  /**
   * Returns the name of this directory entry.
   * 
   * @return the entry name
   */
  public abstract String getName();

  /**
   * Returns <code>true</code> only if <code>changeDirectory()</code> 
   * <em>might</em> succeed for this directory entry.
   * Does not imply <code>hasFileSemantics() == false</code>.
   * 
   * @return <code>true</code> if the entry might represent a remote directory
   */
  public abstract boolean hasDirectorySemantics();

  /**
   * Returns <code>true</code> only if <code>getFile()</code> <em>might</em> succeed for this directory entry.
   * Does not imply <code>hasDirectorySemantics() == false</code>.
   * 
   * @return <code>true</code> if the entry might represent a remote file
   */
  public abstract boolean hasFileSemantics();

  /**
   * Returns the size of the file in bytes, or <code>UNKNOWN_SIZE</code> if 
   * the size is unknown.
   * 
   * @return the file size, or <code>UNKNOWN_SIZE</code>
   */
  public abstract long getSize();

  /**
   * Returns the modification stamp of this file, or <code>null</code> if unknown.
   * <p>
   * Mod time is unreliable for SFTP files since some servers do not send
   * send results back in GMT, or provide poor granularity for files that
   * were modified over 6 months ago.
   * </p>
   * 
   * @return the date, or null
   */
  public abstract Date getModTime();
}
