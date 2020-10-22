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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.target.AuthenticatedSite;
import org.eclipse.target.ITargetResource;
import org.eclipse.ui.IMemento;

import com.jcraft.eclipse.sftp.*;

public class SFtpSite extends AuthenticatedSite{

  private static final String TIMEOUT_CTX="connectionTimeout"; //$NON-NLS-1$
  public static final int DEFAULT_TIMEOUT=60;

  private int timeout=DEFAULT_TIMEOUT;

  private Map openClients=new HashMap();
  
  public SFtpSite(){
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.Site#save(org.eclipse.ui.IMemento)
   */
  public void save(IMemento memento){
    memento.putInteger(TIMEOUT_CTX, timeout);
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.Site#init(org.eclipse.ui.IMemento)
   */
  protected void init(IMemento memento){
    Assert.isNotNull(memento);

    /*
     // restoreAuthenticationInformation() will throw NPE
     // if password is not given.
    restoreAuthenticationInformation();
    */
    Map authInfo=Platform.getAuthorizationInfo(toUrl(), "", ""); //$NON-NLS-1$ //$NON-NLS-2$
    if (authInfo != null) {
        if(authInfo.get("name")!=null)
          try{
          setUsername((String)authInfo.get("name"));
          }
          catch(CoreException e){
            e.printStackTrace();
          }
        if(authInfo.get("password")!=null)
          try{
            setPassword((String)authInfo.get("password"));
          }
          catch(CoreException e){
            e.printStackTrace();
          }
    }

    if(getUsername()==null){
      URL url=toUrl();
      if(url.getUserInfo()!=null){
        try{
          setUsername(url.getUserInfo());
        }
        catch(CoreException e){
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

    // Timeouts.
    Integer to=memento.getInteger(TIMEOUT_CTX);
    if(to!=null){
      timeout=to.intValue();
    }

  }

  /* (non-Javadoc)
   * @see org.eclipse.target.Site#getRootResource()
   */
  public ITargetResource getRootResource(){
    /*
     URL url=toUrl();
     String root=url.getPath();
     root=root.substring(1);
     if(!root.equals("")){ // $NON-NLS-1$
     try {
     getOpenClient(Thread.currentThread()).changeDirectory(root, null);
     } catch (SFtpException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     }
     }
     */
    return new SFtpTargetResource(this, "", true); //$NON-NLS-1$
    //return new SFtpTargetResource(this, root, true); //$NON-NLS-1$
  }

  public int getTimeout(){
    return timeout;
  }

  public void setTimeout(int timeout){
    this.timeout=timeout;
  }

  public IClient createClient() throws SFtpException{
    IClient client=SFtp.createClient(toUrl());
    client.setAuthentication(getUsername(), getPassword());
    client.setTimeout(getTimeout());
    return client;
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.Site#canBeReached(org.eclipse.core.runtime.IProgressMonitor)
   */
  public boolean canBeReached(IProgressMonitor monitor) throws CoreException{
    IClient client=createClient();
    try{
      client.open(monitor);
      
        try{
          setPassword(client.getPassword());
        }
        catch(CoreException e){
          e.printStackTrace();
        }
      
    }
    catch(SFtpException e){
      throw e;
    }
    finally{
      client.close(monitor);
    }
    return true;
  }

  /**
   * Use this method to ensure that the same connection is used for multiple commands
   * issued from the same thread.
   * @throws SFtpException
   */
  private String home="";
  
  public void run(ISFtpRunnable runnable, IProgressMonitor monitor)
      throws SFtpException{
    Thread thread=Thread.currentThread();
    IClient openClient=getOpenClient(thread);
    boolean outerRun=false;
    try{
      monitor.beginTask(null, 100);
      if(openClient==null){
        openClient=getOpenedClient(monitor);
        outerRun=true;
      }

      goHome(openClient, monitor);

      runnable.run(Policy.subMonitorFor(monitor, 90));
    }
    finally{
      if(outerRun&&openClient!=null){
        openClient.close(Policy.subMonitorFor(monitor, 5));
        removeOpenClient(thread);
      }
    }
  }

  public IClient getOpenedClient(IProgressMonitor monitor) throws SFtpException{
    Thread thread=Thread.currentThread();
    IClient openClient=getOpenClient(thread);
    
    if(openClient!=null){
      goHome(openClient, monitor);
      return openClient;
    }
    
    openClient=createClient();
    openClient.open(Policy.subMonitorFor(monitor, 5));

      try{
        setPassword(openClient.getPassword());
      }
      catch(CoreException e){
        e.printStackTrace();
      }
    
    
    addOpenClient(thread, openClient);
    
    URL url=toUrl();
    String root=url.getPath();
    if(root.length()>0){
      root=root.substring(1);
    }
    if(!root.equals("")){ // $NON-NLS-1$
      openClient.changeDirectory(root, monitor);
    }
    home=openClient.pwd();
    return openClient;
  }
  
  public void goHome(IClient openClient, IProgressMonitor monitor) throws SFtpException{
    if(!openClient.pwd().equals(home)){
      openClient.changeDirectory(home, monitor);
    }
  }
  
  public boolean isConnected(){
    return getOpenClient(Thread.currentThread())!=null;
  }

  IClient getOpenClient(Thread thread){
    return (IClient)openClients.get(thread);
  }

  private void addOpenClient(Thread thread, IClient openClient){
    openClients.put(thread, openClient);
  }

  void removeOpenClient(Thread thread){
    openClients.remove(thread);
  }

  /* (non-Javadoc)
   * @see org.eclipse.target.Site#dispose()
   */
  public void dispose() throws CoreException{
    super.dispose();

    for(Iterator iter=openClients.values().iterator(); iter.hasNext();){
      IClient element=(IClient)iter.next();
      element.close(new NullProgressMonitor());
    }
  }

  public URL toUrl(){
    try{
      String location=getLocation();
      location=SFtp.hackedURL(location);
      return new URL(location);
    }
    catch(MalformedURLException e){
      Assert.isTrue(false, "The location of this site is not avalid URL"); //$NON-NLS-1$
      // This return statement is never reached
      return null;
    }
  }
}
