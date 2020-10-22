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

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.*;
import org.eclipse.team.core.Team;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.variants.CachedResourceVariant;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.team.internal.target.UrlUtil;

import com.jcraft.eclipse.sftp.*;
import com.jcraft.eclipse.sftp.internal.SFTPDirectoryEntry;
import com.jcraft.eclipse.target.internal.sftp.SFtpTargetResource;
import com.jcraft.eclipse.team.internal.sftp.*;

public class SFTPSubscriberResource extends CachedResourceVariant{

  public static final byte[] FOLDER_BYTES="D0:0".getBytes(); //$NON-NLS-1$

  private ISFTPRunnableContext context;
  private URL url;
  private boolean isContainer;
  private IDirectoryEntry entry;

  private static final int MAX_TRANSFER_SIZE=32788;

  public static byte[] getSyncBytes(IDirectoryEntry entry){
    // Return enough to rebuild the handle
    long modTime=entry.getModTime().getTime();
    long size=entry.getSize();
    String type=entry.hasFileSemantics() ? "F" : "D"; //$NON-NLS-1$ //$NON-NLS-2$
    return (type+new Long(modTime).toString()+":"+new Long(size).toString()).getBytes(); //$NON-NLS-1$
  }

  /**
   * Create a resource from the given parent and entry
   * @param remote
   * @param entry2
   * @return
   */
  public static SFTPSubscriberResource create(SFTPSubscriberResource parent,
      IDirectoryEntry entry) throws SFTPException{
    try{
      return new SFTPSubscriberResource(parent.getContext(), UrlUtil.concat(
          parent.getUrl().toExternalForm(), new Path(entry.getName())), entry
          .hasDirectorySemantics(), entry);
    }
    catch(MalformedURLException e){
      throw new SFTPException(
          Policy
              .bind(
                  "SFTPSubscriberResource.8", parent.getUrl().toExternalForm(), entry.getName()), e); //$NON-NLS-1$
    }
  }

  /**
   * @param sftpProvider
   * @param resource
   * @param entry2
   * @return
   */
  public static IResourceVariant create(SFTPDeploymentProvider sftpProvider,
      IResource resource, IDirectoryEntry entry) throws SFTPException{
    try{
      return new SFTPSubscriberResource(sftpProvider, sftpProvider
          .getUrl(resource), resource.getType()!=IResource.FILE, entry);
    }
    catch(MalformedURLException e){
      throw new SFTPException(Policy.bind(
          "SFTPSubscriberResource.9", resource.getFullPath().toString()), e); //$NON-NLS-1$
    }
  }

  public static SFTPSubscriberResource create(ISFTPRunnableContext context,
      URL url, byte[] syncBytes){
    boolean isContainer=(syncBytes[0]=='D');
    String id=new String(syncBytes);
    int colon=id.indexOf(":"); //$NON-NLS-1$
    long modTime=Long.parseLong(id.substring(1, colon));
    long size=Long.parseLong(id.substring(colon+1));
    IPath path=new Path(url.getPath());
    String name;
    if(path.isEmpty()){
      name="."; //$NON-NLS-1$
    }
    else{
      name=new Path(url.getPath()).lastSegment();
    }
    SFTPDirectoryEntry entry=new SFTPDirectoryEntry(name, isContainer,
        !isContainer, size, new Date(modTime));
    return new SFTPSubscriberResource(context, url, isContainer, entry);
  }

  public SFTPSubscriberResource(ISFTPRunnableContext context, URL url,
      boolean isContainer, IDirectoryEntry entry){
    this.entry=entry;
    this.isContainer=isContainer;
    this.url=url;
    this.context=context;
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.subscribers.ISubscriberResource#getName()
   */
  public String getName(){
    return getEntry().getName();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.subscribers.ISubscriberResource#isContainer()
   */
  public boolean isContainer(){
    return isContainer;
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.sync.IRemoteResource#getContents(org.eclipse.core.runtime.IProgressMonitor)
   */
  private InputStream getContents(IProgressMonitor progress)
      throws TeamException{
    final InputStream result[]=new InputStream[] {null};
    context.run(new ISFtpRunnable(){
      public void run(IProgressMonitor monitor) throws SFtpException{
        monitor.beginTask(null, 100);
        try{
          // get the size and only transfer if it is under the max transfer size
          long size=getEntry().getSize();
          if(size>MAX_TRANSFER_SIZE){
            result[0]=new ByteArrayInputStream(Policy.bind(
                "SFTPRemoteTargetResource.remoteFileTooBig").getBytes()); //$NON-NLS-1$
            return;
          }
          final String title=Policy
              .bind(
                  "SFTPClient.getFile", context.getRelativePath(getUrl()).toString()); //$NON-NLS-1$
          monitor.subTask(title);
          InputStream in=SFtpTargetResource.getFile(getClient(), context
              .getRelativePath(getUrl()).toString(), isBinary(), 0L, Policy
              .subMonitorFor(monitor, 10));
          // setup progress monitoring
          IProgressMonitor subMonitor=Policy.subMonitorFor(monitor, 80);
          try{
            // Transfer the contents
            byte[] buffer=new byte[1024];
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            int read;
            try{
              try{
                while((read=in.read(buffer))>=0){
                  out.write(buffer, 0, read);
                }
              }
              finally{
                out.close();
              }
            }
            catch(IOException e){
              throw new SFtpException(new Status(IStatus.ERROR, SFTPPlugin.ID,
                  ISFtpStatus.IO_FAILED, e.getMessage(), e));
            }
            finally{
              try{
                in.close();
              }
              catch(IOException e){
                SFTPPlugin.log(SFTPPlugin.wrapException(e));
              }
            }
            result[0]=new ByteArrayInputStream(out.toByteArray());
          }
          finally{
            subMonitor.done();
          }
        }
        finally{
          monitor.done();
        }
      }
    }, Policy.monitorFor(progress));
    return result[0];
  }

  /* private */boolean isBinary(){
    return Team.getFileContentManager().getType(new IStorage(){
      public InputStream getContents() throws CoreException{
        // use empty contents when trying to determine the file type
        return new ByteArrayInputStream(new byte[0]);
      }

      public IPath getFullPath(){
        return new Path(getUrl().getPath());
      }

      public String getName(){
        return SFTPSubscriberResource.this.getName();
      }

      public boolean isReadOnly(){
        return true;
      }

      public Object getAdapter(Class adapter){
        return null;
      }
    })!=Team.TEXT;
  }

  /* private */IClient getClient(){
    return getContext().getOpenClient();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.subscribers.ISubscriberResource#getContentIdentifier()
   */
  public String getContentIdentifier(){
    return getEntry().getModTime().toString();
  }

  public ISFTPRunnableContext getContext(){
    return context;
  }

  public IDirectoryEntry getEntry(){
    return entry;
  }

  public URL getUrl(){
    return url;
  }

  public byte[] asBytes(){
    if(isContainer){
      return FOLDER_BYTES;
    }
    return getSyncBytes(getEntry());
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.synchronize.ResourceVariant#fetchContents(org.eclipse.core.runtime.IProgressMonitor)
   */
  protected void fetchContents(IProgressMonitor monitor) throws TeamException{
    setContents(getContents(Policy.subMonitorFor(monitor, 75)), Policy
        .subMonitorFor(monitor, 25));
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.synchronize.ResourceVariant#getUniquePath()
   */
  public String getCachePath(){
    return getUrl().toExternalForm()+' '+getContentIdentifier();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.synchronize.ResourceVariant#getCacheId()
   */
  protected String getCacheId(){
    return SFTPPlugin.ID;
  }

}
