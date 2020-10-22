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
package com.jcraft.eclipse.target.internal.sftp;

import java.io.*;
import java.net.URL;

import org.eclipse.core.runtime.*;
import org.eclipse.target.*;
import org.eclipse.target.internal.Utils;

import com.jcraft.eclipse.sftp.*;
import com.jcraft.eclipse.sftp.internal.SFTPDirectoryEntry;

public class SFtpTargetResource extends TargetResource{

  private final String siteRelativePath;
  private final boolean isDirectory;
  private IDirectoryEntry entry; /* chache of remote information */

  /*
   * Helper method that makes fetching file contents responsive to cancellation
   */
  public static InputStream getFile(IClient client, String filePath,
      boolean binary, long resumeAt, IProgressMonitor monitor)
      throws SFtpException{
    return client.getFile(filePath, binary, resumeAt, Policy.subMonitorFor(
        monitor, 10));
  }

  /*
   * Helper method that makes sending file contents responsive to cancellation
   */
  public static void putFile(IClient client, String filePath, InputStream in,
      long fileSize, boolean binary, IProgressMonitor monitor)
      throws SFtpException{
    client.putFile(filePath, in, fileSize, binary, Policy.subMonitorFor(
        monitor, 100));
  }

  public SFtpTargetResource(Site site, String siteRelativePath,
      boolean isDirectory){
    super(site);
    this.siteRelativePath=siteRelativePath;
    this.isDirectory=isDirectory;
  }

  public SFtpTargetResource(Site site, String siteRelativePath,
      IDirectoryEntry entry){
    this(site, siteRelativePath, entry.hasDirectorySemantics());
    this.entry=entry;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#getRelativePath()
   */
  public String getRelativePath(){
    return siteRelativePath;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#isDirectory()
   */
  public boolean isDirectory(){
    return isDirectory;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#exists(org.eclipse.core.runtime.IProgressMonitor)
   */
  public boolean exists(IProgressMonitor monitor) throws CoreException{
    entry=null;
    getEntry(monitor);
    return entry!=null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#members(org.eclipse.core.runtime.IProgressMonitor)
   */
  public ITargetResource[] members(IProgressMonitor monitor)
      throws CoreException{
    if(isDirectory()){
      final IDirectoryEntry[] entries=listFiles(getRelativePath(), false,
          monitor);
      ITargetResource[] members=new ITargetResource[entries.length];
      for(int i=0; i<entries.length; i++){
        IDirectoryEntry entry=entries[i];
        members[i]=new SFtpTargetResource(getSite(), Utils.appendPath(
            getRelativePath(), entry.getName()), entry);
      }
      return members;
    }
    else{
      return new ITargetResource[0];
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#getContents(org.eclipse.core.runtime.IProgressMonitor)
   */
  public InputStream getContents(final IProgressMonitor progress)
      throws CoreException{
    
    if(isConnected()){
      ((SFtpSite)getSite()).goHome(getClient(), progress);
      return getFile(getClient(), getRelativePath(), isBinary(), 0, progress);
    }
    progress.beginTask(null, 100);
 
    final IClient openClient=((SFtpSite)getSite()).getOpenedClient(progress);
    
    InputStream in=getFile(openClient, getRelativePath(), isBinary(), 0, Policy
        .subMonitorFor(progress, 90));
    return new FilterInputStream(in){
      public void close() throws IOException{
        try{
          super.close();
        }
        finally{
          try{
            openClient.close(Policy.subMonitorFor(progress, 5));
            ((SFtpSite)getSite()).removeOpenClient(Thread.currentThread());
          }
          catch(SFtpException e){
            // Ignore
          }
        }
      }
    };
  }

  private boolean isConnected(){
    return ((SFtpSite)getSite()).isConnected();
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#setContents(java.io.InputStream, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void setContents(final InputStream stream, final long size,
      IProgressMonitor progress) throws CoreException{
    run(new ISFtpRunnable(){
      public void run(IProgressMonitor monitor) throws SFtpException{
        putFile(getClient(), getRelativePath(), stream, size, isBinary(),
            monitor);
      }
    }, progress);
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#delete(org.eclipse.core.runtime.IProgressMonitor)
   */
  public void delete(IProgressMonitor monitor) throws CoreException{
    run(new ISFtpRunnable(){
      public void run(IProgressMonitor monitor) throws SFtpException{
        if(isDirectory()){
          getClient().deleteDirectory(getRelativePath(), monitor);
        }
        else{
          getClient().deleteFile(getRelativePath(), monitor);
        }
      }
    }, monitor);
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#fetchProperties(org.eclipse.core.runtime.QualifiedName[], boolean, org.eclipse.core.runtime.IProgressMonitor)
   */
  public QualifiedName[] fetchProperties(QualifiedName[] properties,
      boolean refresh, IProgressMonitor monitor) throws CoreException{
    if(refresh||entry==null){
      entry=null;
      getEntry(monitor);
    }
    return getSupportedProperties();
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#isAvailable(org.eclipse.core.runtime.QualifiedName)
   */
  public boolean isAvailable(QualifiedName property){
    return (entry!=null&&isSupported(property));
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#getProperty(org.eclipse.core.runtime.QualifiedName)
   */
  public String getProperty(QualifiedName property) throws CoreException{
    Assert.isNotNull(entry);
    if(property==LAST_MODIFIED_TIMESTAMP_PROPERTY){
      return entry.getModTime().toString();
    }
    if(property==SIZE_PROPERTY){
      return Long.toString(entry.getSize());
    }
    if(property==CONTENT_IDENTIFIER_PROPERTY){
      return getProperty(LAST_MODIFIED_TIMESTAMP_PROPERTY)+getSize();
    }
    return null;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#getSize()
   */
  public long getSize(){
    if(entry!=null)
      return entry.getSize();
    return 0;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#mkdir(org.eclipse.core.runtime.IProgressMonitor)
   */
  public void mkdir(IProgressMonitor monitor) throws CoreException{
    createDirectory(getRelativePath(), true, monitor);
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.ITargetResource#mkdirs(org.eclipse.core.runtime.IProgressMonitor)
   */
  public void mkdirs(IProgressMonitor monitor) throws CoreException{
    createDirectory(getRelativePath(), true, monitor);
  }

  /**
   * @param monitor
   * @throws SFtpException
   */
  private void createDirectory(final String relativePath,
      final boolean createParents, IProgressMonitor monitor)
      throws SFtpException{
    if(relativePath.length()==0)
      return;
    run(new ISFtpRunnable(){
      public void run(IProgressMonitor monitor) throws SFtpException{
        try{
          getClient().createDirectory(relativePath, monitor);
        }
        catch(SFtpException e){
          if(e.getStatus().getCode()==ISFtpStatus.DOES_NOT_EXIST&&createParents){
            // A parent must not exist, try to create it
            createDirectory(Utils.removeLastSegment(getRelativePath()),
                createParents, monitor);
            getClient().createDirectory(relativePath, monitor);
          }
          else{
            throw e;
          }
        }
      }
    }, monitor);
  }

  /* private */IClient getClient(){
    return ((SFtpSite)getSite()).getOpenClient(Thread.currentThread());
  }

  private void run(ISFtpRunnable runnable, IProgressMonitor monitor)
      throws SFtpException{
    ((SFtpSite)getSite()).run(runnable, monitor);
  }

  private IDirectoryEntry getEntry(IProgressMonitor monitor)
      throws CoreException{
    if(entry==null){
      if(isDirectory()){
        entry=fetchDirectoryEntry(monitor);
      }
      else{
        entry=fetchFileEntry(monitor);
      }
    }
    return entry;
  }

  private IDirectoryEntry[] listFiles(final String directory,
      final boolean includeParents, IProgressMonitor monitor)
      throws CoreException{
    final IDirectoryEntry[][] entries=new SFTPDirectoryEntry[1][0];
    run(new ISFtpRunnable(){
      public void run(IProgressMonitor monitor) throws SFtpException{
        entries[0]=getClient().listFiles(directory, includeParents, monitor);
      }
    }, monitor);
    return entries[0];
  }

  private IDirectoryEntry fetchDirectoryEntry(IProgressMonitor progress)
      throws CoreException{
    try{
      IDirectoryEntry[] entries=listFiles(getRelativePath(), true, progress);
      for(int i=0; i<entries.length; i++){
        IDirectoryEntry dirEntry=entries[i];
        if(dirEntry.getName().equals(IDirectoryEntry.CURRENT_FOLDER)){
          return dirEntry;
        }
      }
      return null;
    }
    catch(CoreException e){
      if(e.getStatus().getCode()==ISFtpStatus.DOES_NOT_EXIST){
        return null;
      }
      throw e;
    }
  }

  private IDirectoryEntry fetchFileEntry(IProgressMonitor progress)
      throws CoreException{
    try{
      IDirectoryEntry[] entries=listFiles(getRelativePath(), false, progress);
      if(entries.length==0)
        return null;
      if(entries.length>1){
        // Wrong number of entries. Is it a folder
        throw new CoreException(new Status(IStatus.ERROR, SFtpTargetPlugin.ID,
            ITargetStatus.RESOURCE_WRONG_TYPE,
            "SFTP resource {0} may not be a file"+getFullPath(), null)); //$NON-NLS-1$
      }
      IDirectoryEntry dirEntry=entries[0];
      if(dirEntry.getName().equals(getName())){
        return dirEntry;
      }
      if(dirEntry.getName().equals(getRelativePath())){
        return new SFTPDirectoryEntry(getName(), dirEntry
            .hasDirectorySemantics(), dirEntry.hasFileSemantics(), dirEntry
            .getSize(), dirEntry.getModTime());
      }
      else{
        // Wrong entry. The remote must be a folder
        throw new CoreException(new Status(IStatus.ERROR, SFtpTargetPlugin.ID,
            ITargetStatus.RESOURCE_WRONG_TYPE,
            "SFTP resource {0} may not be a file"+getFullPath(), null)); //$NON-NLS-1$
      }
    }
    catch(CoreException e){
      if(e.getStatus().getCode()==ISFtpStatus.DOES_NOT_EXIST){
        return null;
      }
      throw e;
    }
  }

  private boolean isBinary(){
    // TODO: Just say true for now
    return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.TargetResource#createResource(java.lang.String, boolean)
   */
  protected ITargetResource createResource(String relativePath,
      boolean isDirectory){
    String resourcePath;
    if(relativePath.equals(PARENT_RELATIVE_PATH)){
      resourcePath=Utils.removeLastSegment(siteRelativePath);
    }
    else{
      resourcePath=Utils.appendPath(getRelativePath(), relativePath);
    }
    return new SFtpTargetResource(getSite(), resourcePath, isDirectory);
  }

  public URL toUrl(){
    String location=getFullPath();
    location=SFtp.hackedURL(location);
    try{
      return new URL(location);
    }
    catch(java.net.MalformedURLException e){
      return null;
    }
  }

}
