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

import java.util.Date;

import com.jcraft.eclipse.sftp.IDirectoryEntry;

/**
 * Represents a file stored remotely.  Due to the imprecise nature of
 * decoding remote directory listings, the information contained herein
 * is of a mostly advisory nature and might be wrong on occasion.  This
 * a common phenomenon among SFTP clients, unfortunately.
 */
public class SFTPDirectoryEntry implements IDirectoryEntry{
  public static final IDirectoryEntry[] EMPTY_ARRAY=new IDirectoryEntry[0];
  private String name;
  private boolean hasDirectorySemantics;
  private boolean hasFileSemantics;
  private long size;
  private Date modTime;

  /**
   * Creates a new directory entry.
   * @param name the file or directory name (not its full path)
   * @param hasDirectorySemantics set to true if CWD might succeed
   * @param hasFileSemantics set to true if RETR might succeed
   * @param size the file size in bytes, or UNKNOWN_SIZE if unknown
   * @param modTime the modification stamp or null if unknown
   */
  public SFTPDirectoryEntry(String name, boolean hasDirectorySemantics,
      boolean hasFileSemantics, long size, Date modTime){
    this.name=name;
    this.hasDirectorySemantics=hasDirectorySemantics;
    this.hasFileSemantics=hasFileSemantics;
    this.size=size;
    this.modTime=modTime;
  }

  /**
   * Returns the name of this directory entry.
   * 
   * @return the entry name
   */
  public String getName(){
    return name;
  }

  /**
   * Returns true only if changeDirectory() _might_ succeed for this directory entry.
   * Does not imply hasFileSemantics() == false.
   * 
   * @return true if the entry might represent a remote directory
   */
  public boolean hasDirectorySemantics(){
    return hasDirectorySemantics;
  }

  /**
   * Returns true only if getFile() _might_ succeed for this directory entry.
   * Does not imply hasDirectorySemantics() == false.
   * 
   * @return true if the entry might represent a remote file
   */
  public boolean hasFileSemantics(){
    return hasFileSemantics;
  }

  /**
   * Returns the size of the file in bytes, or <code>UNKNOWN_SIZE</code> if unknown.
   * 
   * @return the file size, or UNKNOWN_SIZE
   */
  public long getSize(){
    return size;
  }

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
  public Date getModTime(){
    return modTime;
  }
}
