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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.target.Site;
import org.eclipse.team.core.Team;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.team.core.variants.ThreeWaySynchronizer;
import org.eclipse.team.internal.target.*;
import org.eclipse.team.internal.target.subscriber.TargetDeploymentProvider;
import org.eclipse.team.internal.target.subscriber.TargetSubscriber;

import com.jcraft.eclipse.sftp.*;
import com.jcraft.eclipse.target.internal.sftp.SFtpSite;
import com.jcraft.eclipse.target.internal.sftp.SFtpTargetResource;
import com.jcraft.eclipse.team.internal.sftp.*;
import com.jcraft.eclipse.team.internal.sftp.Policy;

/**
 * SFTP Deployment Provider
 */
public class SFTPDeploymentProvider extends TargetDeploymentProvider implements
    ISFTPRunnableContext{

  private final String CTX_URL_KEY="URL"; //$NON-NLS-1$

  private static final Map connectedThreads=new HashMap();

  /*
   * Constructor required for creation by the Team deployment provider manager
   */
  public SFTPDeploymentProvider(){
    // Url will be set in restoreState
  }

  public SFTPDeploymentProvider(URL url){
    super(url);
  }

  public String getID(){
    return SFTPPlugin.DEPLOYMENT_PROVIDER_ID;
  }

  protected TargetSubscriber getSubscriber(){
    TargetSubscriber subscriber=SFTPPlugin.getPlugin().getSubscriber();
    return subscriber;
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.DeploymentProvider#saveState(org.eclipse.team.core.IMemento)
   */
  public void saveState(IMemento memento){
    String urlString=getUrl().toExternalForm();
    urlString=SFtp.unhackedURL(urlString);
    memento.putString(CTX_URL_KEY, urlString);
    // TODO: Need to persist site (or enough info to recreate the site)
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.DeploymentProvider#restoreState(org.eclipse.team.core.IMemento)
   */
  public void restoreState(IMemento memento){
    String urlString=memento.getString(CTX_URL_KEY);
    if(urlString==null){
      handleStateError(null);
      return;
    }
    try{
      urlString=SFtp.hackedURL(urlString);
      setUrl(new URL(urlString));
    }
    catch(MalformedURLException e){
      handleStateError(e);
    }
  }

  private void handleStateError(Throwable e){
    // invalid state. 
    // Log it and remove the provider
    SFTPPlugin
        .logError(
            Policy
                .bind(
                    "SFTPDeploymentProvider.3", getMappedContainer().getFullPath().toString()), e); //$NON-NLS-1$
    //		try {
    //			Team.getDeploymentManager().unmap(getMappedContainer(), this);
    //		} catch (TeamException e1) {
    //			SFTPPlugin.log(e1);
    //		}
  }

  /**
   * Return the URL of the SFTP site that the container of this provider is mapped to.
   * @return Returns the URL.
   */
  public URL getRemoteURL(){
    return getUrl();
  }

  /**
   * Return the host relative path to which this deployment provider is mapped.
   * @return
   */
  public String getHostRelativePath(){
    return getRemoteURL().getPath();
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.team.internal.sftp.client.ISFTPRunnableContext#getRelativePath(java.net.URL)
   */
  public IPath getRelativePath(URL url){
    IPath path=UrlUtil.getTrailingPath(url, getUrl());
    return path;
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.team.internal.sftp.client.ISFTPRunnableContext#run(com.jcraft.eclipse.team.internal.sftp.client.ISFtpRunnable, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void run(ISFtpRunnable runnable, IProgressMonitor monitor)
      throws TeamException{
    // Determine if we are nested or not
    boolean isOuterRun=false;
    URL baseUrl=getUrl();
    IClient client=internalGetClient(baseUrl);

    IProgressMonitor _monitor=null;
    try{
      _monitor=Policy.monitorFor(monitor);
      _monitor.beginTask(null, 110+(client.isOpen() ? 0 : 20));
      if(!client.isOpen()){
        // The path always has a leading / to remove
        String path=baseUrl.getPath();
        if(path.length()>0&&path.charAt(0)=='/'){
          path=path.substring(1);
        }
        client.open(Policy.subMonitorFor(_monitor, 10));
        if(path.length()>0){
          client.changeDirectory(path, Policy.subMonitorFor(_monitor, 10));
        }
        registerClient(client);
        isOuterRun=true;
      }
      client.run(runnable, Policy.subMonitorFor(_monitor, 100));

    }
    catch(SFtpException e){
      throw SFTPException.wrapException(e.getMessage(), e);
    }
    finally{
      if(isOuterRun){
        try{
          client.close(Policy.subMonitorFor(_monitor, 10));
        }
        catch(SFtpException e){
          SFTPPlugin.log(e);
        }
        finally{
          deregisterClient();
        }
      }
      if(_monitor!=null)
        _monitor.done();
    }
  }

  /**
   * Return the client that is in use by the current thread
   * or <code>null</code> if no client is available.
   * @return a connected client
   */
  public IClient getConnectedClient(){
    return (IClient)connectedThreads.get(Thread.currentThread());
  }

  public IClient registerClient(IClient client){
    return (IClient)connectedThreads.put(Thread.currentThread(), client);
  }

  public IClient deregisterClient(){
    return (IClient)connectedThreads.remove(Thread.currentThread());
  }

  private IClient internalGetClient(URL url) throws TeamException{
    IClient client=getConnectedClient();
    if(client==null){
      // Create client
      try{
        client=SFtp.createClient(url);
        client.setAuthentication(getUsername(url), getPassword(url));
        client.setTimeout(getTimeout(url));
      }
      catch(SFtpException e){
        SFTPException.wrapException(e.getMessage(), e);
      }
    }
    return client;
  }

  private String getPassword(URL url) throws TeamException{
    // First, attempt to get the password from the url
    String userInfo=url.getUserInfo();
    if(userInfo!=null){
      int colon=userInfo.indexOf(':');
      if(colon!=-1){
        return userInfo.substring(colon+1);
      }
    }
    // If the above fails, try to get the password from the site
    return getSFTPSite(url).getPassword();
  }

  private String getUsername(URL url) throws TeamException{
    // First, attempt to get the username from the url
    String userInfo=url.getUserInfo();
    if(userInfo!=null){
      int colon=userInfo.indexOf(':');
      if(colon==-1){
        return userInfo;
      }
      return userInfo.substring(0, colon);
    }
    // If the above fails, try to get the username from the site
    return getSFTPSite(url).getUsername();
  }

  /*
   private boolean getUsePassive(URL url) throws TeamException {
   return getSFTPSite(url).isPassive();
   }
   */
  protected SFtpSite getSFTPSite(URL url) throws TeamException{
    Site site=getSite(url);
    if(site instanceof SFtpSite){
      return (SFtpSite)site;
    }
    throw new TeamException(Policy.bind(
        "SFTPDeploymentProvider.4", url.toExternalForm()), null); //$NON-NLS-1$
  }

  private int getTimeout(URL url) throws TeamException{
    return getSFTPSite(url).getTimeout();
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.team.internal.sftp.client.ISFTPRunnableContext#getOpenClient()
   */
  public IClient getOpenClient(){
    return getConnectedClient();
  }

  public IResourceVariant getResourceVariant(IResource resource,
      byte[] syncBytes) throws TeamException{
    try{
      if(syncBytes==null)
        return null;
      return SFTPSubscriberResource.create(this, getUrl(resource), syncBytes);
    }
    catch(MalformedURLException e){
      SFTPPlugin.logError(Policy.bind(
          "SFTPDeploymentProvider.5", resource.getFullPath().toString()), e); //$NON-NLS-1$
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.target.TargetDeploymentProvider#run(org.eclipse.team.internal.core.target.ITargetRunnable, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void run(final ITargetRunnable runnable, IProgressMonitor monitor)
      throws TeamException{
    final TeamException[] exception=new TeamException[] {null};
    run(new ISFtpRunnable(){
      public void run(IProgressMonitor monitor) throws SFtpException{
        try{
          SFTPDeploymentProvider.super.run(runnable, monitor);
        }
        catch(TeamException e){
          exception[0]=e;
        }
      }
    }, monitor);
    if(exception[0]!=null){
      throw exception[0];
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.target.TargetDeploymentProvider#putFile(org.eclipse.core.resources.IFile, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void putFile(final IFile localFile, IProgressMonitor monitor)
      throws TeamException{
    final TeamException[] exception=new TeamException[] {null};
    run(new ISFtpRunnable(){
      public void run(IProgressMonitor monitor) throws SFtpException{
        try{
          putFile(getOpenClient(), getRelativePath(localFile).toString(),
              localFile, Team.getType(localFile)!=Team.TEXT, monitor);
        }
        catch(SFTPException e){
          exception[0]=e;
        }

      }
    }, monitor);
    if(exception[0]!=null){
      throw exception[0];
    }
  }

  /**
   * Stores a local file on the remote system.
   * @param filePath the absolute or relative path of the file
   * @param localFile the local file to send
   * @param binary if true, uses binary transfer type
   * @param monitor the progress monitor, or null
   */
  public void putFile(IClient client, String filePath, IFile localFile,
      boolean binary, IProgressMonitor monitor) throws SFTPException{

    InputStream in=null;
    try{
      in=new BufferedInputStream(localFile.getContents());
      SFtpTargetResource.putFile(client, filePath, in, getFileSize(localFile),
          binary, monitor);
    }
    catch(CoreException e){
      throw SFTPException.wrapException(Policy
          .bind("SFTPClient.ErrorSendingFile"), e); //$NON-NLS-1$
    }
    finally{
      if(in!=null)
        try{
          in.close();
        }
        catch(IOException e1){
          // Ignore exceptions on close;
        }
    }

  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.target.TargetDeploymentProvider#deleteRemote(org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void deleteRemote(IResource resource, IProgressMonitor monitor)
      throws TeamException{
    try{
      if(resource.getType()==IResource.FILE){
        getOpenClient().deleteFile(getRelativePath(resource).toString(),
            monitor);
      }
      else{
        getOpenClient().deleteDirectory(getRelativePath(resource).toString(),
            monitor);
      }
    }
    catch(SFtpException e){
      throw SFTPException.wrapException(e);
    }
  }

  public void createRemoteDirectory(IResource resource, IProgressMonitor monitor)
      throws TeamException{
    try{
      try{
        getOpenClient().createDirectory(getRelativePath(resource).toString(),
            monitor);
      }
      catch(SFtpException e){
        if(e.getStatus().getCode()==ISFtpStatus.DOES_NOT_EXIST){
          // A parent must not exist, try to create it
          createRemoteDirectory(resource.getParent(), monitor);
          getOpenClient().createDirectory(getRelativePath(resource).toString(),
              monitor);
        }
        else{
          throw e;
        }
      }
    }
    catch(SFtpException e){
      // Some servers use 550 (DOES_NOT_EXIST) when the directory already exists.
      // Therefore, don't report an error in either case. If the directory wasn't created
      // another operation will fail anyway so we don't need to report it here
      // P.S. The repercusions of this are that server that return 550 cannot create multiple
      // ancesters for a file (i.e. upload of f1/f2/file will fail if f1 doesn't exist.
      if(e.getStatus().getCode()==ISFtpStatus.DIRECTORY_EXIST
          ||e.getStatus().getCode()==ISFtpStatus.DOES_NOT_EXIST)
        return;
      throw SFTPException.wrapException(e);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.target.TargetDeploymentProvider#getFile(org.eclipse.core.resources.IFile, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void getFile(IFile localFile, IProgressMonitor monitor)
      throws TeamException{
    getFile(getOpenClient(), getRelativePath(localFile).toString(), localFile,
        Team.getType(localFile)!=Team.TEXT, false /* resume */, monitor);
  }

  /**
   * Retrieves a remote file.
   * @param filePath the absolute or relative path of the file
   * @param localFile the local file to create
   * @param binary if true, uses binary transfer type
   * @param resume if true, attempts to resume a partial transfer, else overwrites
   * @param monitor the progress monitor, or null
   */
  public void getFile(IClient client, String filePath, IFile localFile,
      boolean binary, boolean resume, IProgressMonitor monitor)
      throws SFTPException{

    long resumeAt=0;
    if(resume){
      resumeAt=getFileSize(localFile);
    }

    InputStream in=null;

    // transfer the file
    try{
      in=SFtpTargetResource
          .getFile(client, filePath, binary, resumeAt, monitor);
      if(localFile.exists()){
        if(resumeAt!=0){
          // don't bother remembering the previous (incomplete) generation of the file
          localFile.appendContents(in, false /*force*/,
              false /*keepHistory*/, null);
        }
        else{
          localFile.setContents(in, false /*force*/, true /*keepHistory*/,
              null);
        }
      }
      else{
        localFile.create(in, false /*force*/, null);
      }
    }
    catch(CoreException e){
      throw SFTPPlugin.wrapException(e);
    }
    finally{
      if(in!=null){
        try{
          in.close();
        }
        catch(IOException e1){
          // Ignore close exception
        }
      }
    }
  }

  private long getFileSize(IFile file){
    if(!file.exists())
      return 0;
    return file.getLocation().toFile().length();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.target.TargetDeploymentProvider#getSynchronizer()
   */
  public ThreeWaySynchronizer getSynchronizer(){
    return SFTPPlugin.getPlugin().getSubscriber().getSynchronizer();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.target.TargetDeploymentProvider#fetchRemoteSyncBytes(org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)
   */
  public byte[] fetchRemoteSyncBytes(IResource localResource,
      IProgressMonitor monitor) throws TeamException{
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.target.subscriber.TargetDeploymentProvider#getSite()
   */
  public Site getSite(){
    try{
      return getSFTPSite(getUrl());
    }
    catch(TeamException e){
      SFTPPlugin.log(e);
      return null;
    }
  }

}
