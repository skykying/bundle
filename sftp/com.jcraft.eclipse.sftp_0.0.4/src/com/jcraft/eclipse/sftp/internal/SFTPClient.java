/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;

import com.jcraft.eclipse.jsch.core.IJSchLocation;
import com.jcraft.eclipse.jsch.core.JSchCoreException;
import com.jcraft.eclipse.jsch.core.JSchLocation;
import com.jcraft.eclipse.jsch.core.JSchLocationAdapter;
import com.jcraft.eclipse.jsch.core.JSchSession;

import com.jcraft.eclipse.sftp.*;
import com.jcraft.eclipse.sftp.internal.streams.*;
import com.jcraft.jsch.*;

/**
 * Simple SFTP client class.
 * Based on ChannelSftp of JSch.
 * 
 * The public methods of this class all throw SFTPExceptions to indicate errors.
 * Recoverable errors: (subsequent operations will be processed normally)
 *   SFTPServerException and subclasses
 *     SFTPAuthenticationException
 *     SFTPFileNotAvailableException
 *     SFTPServiceNotAvailableException
 * 
 * Non-recoverable errors: (subsequent operations will probably fail)
 *   SFTPCommunicationException and subclasses
 *   ... anything else ...
 * 
 */
public class SFTPClient implements IClient{

  public static final String PARENT_DIRECTORY_NAME="."; //$NON-NLS-1$

  private static java.util.Hashtable pool=new java.util.Hashtable();

  private SFTPServerLocation location;
  private ISFTPClientListener listener;
  
  private String password=null;
  
  public static final String INFO_PASSWORD="com.jcraft.eclipse.sftp.password";//$NON-NLS-1$
  public static final String AUTH_SCHEME=""; //$NON-NLS-1$ 
  public static final URL FAKE_URL;

  static{
    URL temp=null;
    try{
      temp=new URL("http://com.jcraft.eclipse.sftp");//$NON-NLS-1$ 
    }
    catch(MalformedURLException e){
      // Should never fail
    }
    FAKE_URL=temp;
  }

  private int timeout;

  boolean expectDataTransferCompletion;

  public static final int USE_DEFAULT_TIMEOUT=-1;

  //private Session session;
  private JSchSession jschsession=null;
  
  private ChannelSftp channel;

  /**
   * Creates an SFTPClient for the specified location.
   * @param location the SFTP server location
   * @param listener the SFTP listener
   */
  public SFTPClient(SFTPServerLocation location, ISFTPClientListener listener){
    this.location=location;
    this.listener=listener;
    this.timeout=SFtpPlugin.getDefault().getTimeout();
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.sftp.IClient#getUrl()
   */
  public URL getUrl(){
    return location.getUrl();
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.sftp.IClient#setAuthentication(java.lang.String, java.lang.String)
   */
  public void setAuthentication(String username, String password){
    location.setAuthentication(username, password);
    this.password=password;
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.sftp.IClient#run(com.jcraft.eclipse.sftp.internal.ISFtpRunnable, org.eclipse.core.runtime.IProgressMonitor)
   */
  public void run(ISFtpRunnable runnable, IProgressMonitor monitor)
      throws SFtpException{

    boolean isOuterRun=false;

    try{
      monitor=SFtpPlugin.monitorFor(monitor);
      monitor.beginTask(null, 100+(channel==null ? 20 : 0));
      if(channel==null){
        open(SFtpPlugin.subMonitorFor(monitor, 10));
        isOuterRun=true;
      }
      runnable.run(SFtpPlugin.subMonitorFor(monitor, 100));

    }
    finally{
      if(isOuterRun){
        close(SFtpPlugin.subMonitorFor(monitor, 10));
      }
      monitor.done();
    }
  }

  /**
   * Opens and authenticates a connection to the server, if not already open.
   * @param monitor the progress monitor, or null
   */
  public void open(IProgressMonitor monitor) throws SFtpException{
    if(channel!=null)
      return;
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    monitor.subTask(SFtpPlugin.getResourceString(
        "SFTPClient.openConnection", location.getHostname())); //$NON-NLS-1$
    try{
      String hostname;
      int port;

      hostname=location.getHostname();
      port=location.getPort();

      monitor.worked(10);

      try{
        IJSchLocation jschlocation=getJSchLocation(location.getUsername()+"@"+hostname+":"+port);
        if(location.getPassword()!=null)
          jschlocation.setPassword(location.getPassword());
        jschsession=JSchSession.getSession(jschlocation, monitor);
        Session session=jschsession.getSession();
        if(channel==null){
          synchronized(pool){
            //channel=(ChannelSftp)pool.get(session);
          }
        }
        if(channel==null){
          channel=(ChannelSftp)session.openChannel("sftp"); //$NON-NLS-1$
          channel.connect();
          //pool.put(session, channel);
        }
      }
      catch(JSchException e){
        throw new SFtpException(e.toString(), 0);
      }
    }
    catch(SFtpException e){
      throw e;
    }
    finally{
      monitor.done();
    }
  }

  /**
   * Closes a connection to the server, if not already closed.
   * @param monitor the progress monitor, or null
   */
  public void close(IProgressMonitor monitor) throws SFtpException{
    if(channel==null)
      return;
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    monitor.subTask(SFtpPlugin.getResourceString("SFTPClient.closeConnection")); //$NON-NLS-1$
    try{
      channel.quit();
      channel.disconnect();
    }
    finally{
      channel=null;
      synchronized(pool){
        pool.remove(jschsession.getSession());
      }
    }
  }

  public boolean isOpen(){
    return channel!=null;
  }

  /**
   * Changes the current remote working directory.
   * @param directoryPath the absolute or relative path of the directory
   * @param monitor the progress monitor, or null
   */
  public void changeDirectory(String directoryPath, IProgressMonitor monitor)
      throws SFtpException{
    //Assert.isNotNull(directoryPath);
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    monitor.subTask(SFtpPlugin.getResourceString(
        "SFTPClient.changeDirectory", directoryPath)); //$NON-NLS-1$
    try{
      monitor.worked(50);
      try{
        channel.cd(directoryPath);
      }
      catch(SftpException e){
        throw new SFtpException(e.toString()+": changeDirectory "+directoryPath, 0);
      }
      monitor.worked(50);
    }
    finally{
      monitor.done();
    }
  }

  /**
   * Deletes a remote directory.
   * @param directoryPath the absolute or relative path of the directory
   * @param monitor the progress monitor, or null
   */
  public void deleteDirectory(String directoryPath, IProgressMonitor monitor)
      throws SFtpException{
    //Assert.isNotNull(directoryPath);
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    monitor.subTask(SFtpPlugin.getResourceString(
        "SFTPClient.deleteDirectory", directoryPath)); //$NON-NLS-1$
    try{
      monitor.worked(50);
      try{
        channel.rmdir(directoryPath);
      }
      catch(SftpException e){
        throw new SFtpException(e.toString()+": deleteDirectory "+directoryPath, 0);
      }
      monitor.worked(50);
    }
    finally{
      monitor.done();
    }
  }

  /**
   * Creates a remote directory.
   * @param directoryPath the absolute or relative path of the directory
   * @param monitor the progress monitor, or null
   */
  public void createDirectory(String directoryPath, IProgressMonitor monitor)
      throws SFtpException{
    //Assert.isNotNull(directoryPath);
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    monitor.subTask(SFtpPlugin.getResourceString(
        "SFTPClient.createDirectory", directoryPath)); //$NON-NLS-1$
    try{
      monitor.worked(50);
      try{
        channel.mkdir(directoryPath);
      }
      catch(SftpException e){
        throw new SFtpException(e.toString()+": createDirectory "+directoryPath, 0);
      }
      monitor.worked(50);
    }
    finally{
      monitor.done();
    }
  }

  /**
   * Deletes a remote file.
   * @param filePath the absolute or relative path of the file
   * @param monitor the progress monitor, or null
   */
  public void deleteFile(String filePath, IProgressMonitor monitor)
      throws SFtpException{
    //Assert.isNotNull(filePath);
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    monitor.subTask(SFtpPlugin.getResourceString(
        "SFTPClient.deleteFile", filePath)); //$NON-NLS-1$
    try{
      monitor.worked(50);
      try{
        channel.rm(filePath);
      }
      catch(SftpException e){
        throw new SFtpException(e.toString()+": deleteFile ", 0);
      }
      monitor.worked(50);
    }
    finally{
      monitor.done();
    }
  }

  /**
   * Retrieves the contents of a remote file.
   * 
   * The return input stream has a direct connection to the server. Therefore,
   * the contents should be read before the sftp connection is closed.
   * 
   * @param filePath the absolute or relative path of the file
   * @param binary if true, uses binary transfer type
   * @param resumeAt the position in the remote file to start at (or 0 for the whole file)
   * @param monitor the progress monitor, or null
   */
  private InputStream internalGetContents(String filePath, boolean binary,
      long resumeAt, IProgressMonitor monitor) throws SFtpException{
    InputStream in=null;
    try{
      if(resumeAt!=0){
        throw new SFtpException("get: resume is not supported.", 0);
      }
      SftpProgressMonitor smonitor=null;
      if(monitor!=null){
        final IProgressMonitor _monitor=monitor;
        smonitor=new SftpProgressMonitor(){
          public void init(int op, String src, String dest, long max){
          }

          public boolean count(long count){
            return !_monitor.isCanceled();
          }

          public void end(){
          }
        };
      }
      in=channel.get(filePath, smonitor);
    }
    catch(SftpException e){
      throw new SFtpException(e.toString(), 0);
    }
    // setup line terminator conversion
    if(!binary&&!Constants.IS_CRLF_PLATFORM)
      in=new CRLFtoLFInputStream(in);
    return in;
  }

  public InputStream getFile(String filePath, boolean binary, long resumeAt,
      IProgressMonitor monitor) throws SFtpException{

    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    final String title=SFtpPlugin.getResourceString(
        "SFTPClient.getFile", filePath); //$NON-NLS-1$
    monitor.subTask(title);
    try{
      InputStream in=internalGetContents(filePath, binary, resumeAt, SFtpPlugin
          .subMonitorFor(monitor, 10));
      // setup progress monitoring
      monitor.subTask(SFtpPlugin.getResourceString(
          "SFTPClient.transferNoSize", title)); //$NON-NLS-1$
      in=new ProgressMonitorInputStream(in, 0,
          Constants.TRANSFER_PROGRESS_INCREMENT, monitor){
        protected void updateMonitor(long bytesRead, long bytesTotal,
            IProgressMonitor monitor){
          if(bytesRead==0)
            return;
          monitor.subTask(SFtpPlugin.getResourceString(
              "SFTPClient.transferUnknownSize", //$NON-NLS-1$
              new Object[] {title, Long.toString(bytesRead>>10)}));
        }
      };
      return in;
    }
    finally{
      monitor.done();
    }
  }

  public void putFile(String filePath, InputStream in, long fileSize,
      boolean binary, IProgressMonitor monitor) throws SFtpException{

    //Assert.isNotNull(filePath);
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    final String title=SFtpPlugin.getResourceString(
        "SFTPClient.putFile", filePath); //$NON-NLS-1$
    monitor.subTask(title);

    try{
      try{
        OutputStream out=null;
        try{
          SftpProgressMonitor smonitor=null;
          if(monitor!=null){
            final IProgressMonitor _monitor=monitor;
            smonitor=new SftpProgressMonitor(){
              public void init(int op, String src, String dest, long max){
              }

              public boolean count(long count){
                return !_monitor.isCanceled();
              }

              public void end(){
              }
            };
          }

          out=channel.put(filePath, smonitor, ChannelSftp.OVERWRITE);
        }
        catch(SftpException e){
          throw new SFtpException(e.toString(), 0);
        }

        if(!binary&&!Constants.IS_CRLF_PLATFORM)
          in=new LFtoCRLFInputStream(in);
        
        monitor.subTask(SFtpPlugin.getResourceString(
            "SFTPClient.transferNoSize", title)); //$NON-NLS-1$

        in=new ProgressMonitorInputStream(in, fileSize,
            Constants.TRANSFER_PROGRESS_INCREMENT, monitor){
          protected void updateMonitor(long bytesRead, long bytesTotal,
              IProgressMonitor monitor){
            if(bytesRead==0)
              return;
            monitor.subTask(SFtpPlugin.getResourceString(
                "SFTPClient.transferSize", //$NON-NLS-1$
                new Object[] {title, Long.toString(bytesRead>>10),
                    Long.toString(bytesTotal>>10)}));
          }
        };

        // copy file to output stream		
        byte[] buffer=new byte[SFtpPlugin.getDefault().getSendBufferSize()];
        for(int count; (count=in.read(buffer))!=-1;){
          out.write(buffer, 0, count);
        }
        out.close();
      }
      finally{
        try{
          if(in!=null)
            in.close();
        }
        finally{
        }
        monitor.done();
      }
    }
    catch(IOException e){
      //e.printStackTrace();
      throw new SFtpCommunicationException(SFtpPlugin
          .getResourceString("SFTPClient.ErrorSendingFile"), e); //$NON-NLS-1$
    }
  }

  /**
   * Lists the files stored in a remote directory.
   * @param directoryPath the absolute or relative path of the directory, or null (or empty string) for current
   * @param includeParents indicates whether the parent directory entries should be included. Parent directories 
   * will have names with a single or double dot.
   * @param monitor the progress monitor, or null
   * @return an array of directory entries, or an empty array if none
   */
  public IDirectoryEntry[] listFiles(String directoryPath,
      boolean includeParents, IProgressMonitor monitor) throws SFtpException{
    monitor=SFtpPlugin.monitorFor(monitor);
    monitor.beginTask(null, 100);
    final String title=SFtpPlugin.getResourceString(
        "SFTPClient.listFiles", directoryPath==null ? "." : directoryPath); //$NON-NLS-1$ //$NON-NLS-2$
    monitor.subTask(title);

    String pwd=null;
    try{
      // Read the list entries from the data connection
      Map dirlist=new HashMap();

      if(directoryPath.length()==0)
        directoryPath=".";

      try{
        java.util.Vector files=channel.ls(directoryPath);

        for(int i=0; i<files.size(); i++){
          ChannelSftp.LsEntry le=(ChannelSftp.LsEntry)files.elementAt(i);
          String name=le.getFilename();
          boolean hasDirectorySemantics=true;
          boolean hasFileSemantics=true;
          IDirectoryEntry entry=null;
          SftpATTRS attrs=le.getAttrs();
          Date mtime=new Date(attrs.getMTime()*(long)1000);
          if(attrs.isDir()){
            if(!includeParents&&(name.equals(".")||name.equals(".."))){
              continue;
            }
            hasFileSemantics=false;
            entry=new SFTPDirectoryEntry(name, hasDirectorySemantics,
                hasFileSemantics, attrs.getSize(), mtime);
          }
          else{
            if(attrs.isLink()){
              try{
                
                if(pwd==null&&!directoryPath.equals(".")){
                  SftpATTRS _attrs=channel.stat(directoryPath);
                  if(_attrs.isDir()){
                    pwd=channel.pwd();
                    try{
                      channel.cd(directoryPath);
                    }
                    catch(SftpException e){
                      pwd=null;
                    }
                  }
                }
                
                SftpATTRS _attrs=channel.stat(name);
                if(!_attrs.isDir()){
                  hasDirectorySemantics=false;
                }
                attrs=_attrs;
                mtime=new Date(attrs.getMTime()*(long)1000);
              }
              catch(SftpException ee){
                continue;
              }
            }
            else{
              hasDirectorySemantics=false;
            }
            entry=new SFTPDirectoryEntry(name, hasDirectorySemantics,
                hasFileSemantics, attrs.getSize(), mtime);
          }
          if(entry!=null){
            if(dirlist.containsKey(name))
              continue;
            // ignore Unix . and .. that are sometimes included in listing
            if(!includeParents&&(".".equals(name)||"..".equals(name)))continue; //$NON-NLS-1$ //$NON-NLS-2$
            // A colon in a file or folder name is not supported in Eclipse
            dirlist.put(name, entry);
          }
        }

      }
      catch(SftpException e){
        throw new SFtpException(e.toString()+": "+directoryPath, 0); //$NON-NLS-1$

      }
      return (IDirectoryEntry[])dirlist.values().toArray(
          new SFTPDirectoryEntry[dirlist.size()]);
    }
    finally{
      try{
        if(pwd!=null)
          channel.cd(pwd);
      }
      catch(SftpException e){

      }
      monitor.done();
    }
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.sftp.IClient#getTimeout()
   */
  public int getTimeout(){
    return timeout;
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.sftp.IClient#setTimeout(int)
   */
  public void setTimeout(int timeout){
    this.timeout=timeout;
  }
  
  private IJSchLocation getJSchLocation(final String location){
    IJSchLocation _location=null;
    try{
      _location=JSchLocation.fromString(location);
    }
    catch(JSchCoreException e1){
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    _location=new JSchLocationAdapter(_location){
      public void flushUserInfo(){
        flushCache();
      }

      private void flushCache(){
        try{
          Platform.flushAuthorizationInfo(FAKE_URL, location, AUTH_SCHEME);
          SFTPClient.this.password=null;
        }
        catch(CoreException e){
        }
      }

      public void setAllowCaching(boolean value){
        if(value)
          updateCache();
        else
          flushCache();
      }

      private boolean updateCache(){
        // put the password into the Platform map
        Map map=Platform.getAuthorizationInfo(FAKE_URL, location, AUTH_SCHEME);
        if(map==null){
          map=new java.util.HashMap(10);
        }
        if(getPassword()!=null){
          map.put(INFO_PASSWORD, getPassword());
          SFTPClient.this.password=getPassword();
        }
        try{
          Platform.addAuthorizationInfo(FAKE_URL, location, AUTH_SCHEME, map);
        }
        catch(CoreException e){
          // We should probably wrap the CoreException here!
          return false;
        }
        return true;
      }
    };

    String password=null;
    Map map=Platform.getAuthorizationInfo(FAKE_URL, location, AUTH_SCHEME);
    if(map!=null){
      password=(String)map.get(INFO_PASSWORD);
    }
    if(password!=null)
      _location.setPassword(password);
    return _location;
  }
  
  public String pwd(){
//    try{
      return channel.pwd();
//    }
//    catch(SftpException e){
//      return null;
//    }
  }
  
  public String getPassword(){
    return password;
  }
}
