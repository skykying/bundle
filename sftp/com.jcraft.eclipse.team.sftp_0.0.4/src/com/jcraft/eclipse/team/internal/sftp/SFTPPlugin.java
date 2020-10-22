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
package com.jcraft.eclipse.team.internal.sftp;

import org.eclipse.core.runtime.*;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.jcraft.eclipse.team.internal.sftp.subscriber.SFTPSubscriber;

public class SFTPPlugin extends AbstractUIPlugin{
  public static final String ID="com.jcraft.eclipse.team.sftp"; //$NON-NLS-1$
  private static SFTPPlugin instance;

  // boolean indicating
  private boolean fetchRemoteTimestampImmediately=false;
  public static String DEPLOYMENT_PROVIDER_ID=ID+".SFTPDeploymentProvider"; //$NON-NLS-1$
  private SFTPSubscriber subscriber;

  /**
   * Constructor for SFTPProviderPlugin.
   * @param descriptor
   */
  public SFTPPlugin(IPluginDescriptor descriptor){
    super(descriptor);
    instance=this;
  }

  /**
   * Returns the singleton plug-in instance.
   * 
   * @return the plugin instance
   */
  public static SFTPPlugin getPlugin(){
    return instance;
  }

  /**
   * @see Plugin#startup()
   */
  public void startup() throws CoreException{
    super.startup();
    Policy.localize("com.jcraft.eclipse.team.internal.sftp.messages"); //$NON-NLS-1$
  }

  /**
   * @see Plugin#shutdown()
   */
  public void shutdown() throws CoreException{
    super.shutdown();
  }

  /**
   * Returns the time (in seconds) to wait for establishing a PORT connection.
   */
  public int getPortConnectionTimeout(){
    return 30;
  }

  public int getTimeout(){
    return 30;
  }

  public int getReceiveBufferSize(){
    return 32768;
  }

  public int getSendBufferSize(){
    return 32768;
  }

  /**
   * Convenience method for logging TeamExceptions to the plugin log
   */
  public static void log(CoreException e){
    log(e.getStatus().getSeverity(), e.getMessage(), e);
  }

  /**
   * Convenience method for logging a status to the plugin log
   */
  public static void log(IStatus status){
    instance.getLog().log(status);
  }

  /**
   * Convenience method for logging an internal error
   */
  public static void logError(String message, Throwable e){
    log(new Status(IStatus.ERROR, ID, 0, message, e));
  }

  /**
   * Convenience method for logging an internal error
   */
  public static void log(int severity, String message, Throwable e){
    log(new Status(severity, ID, 0, message, e));
  }

  public static SFTPException wrapException(CoreException e){
    return new SFTPException(e.getStatus());
  }

  public static SFTPException wrapException(Exception e){
    return new SFTPException(new Status(IStatus.ERROR, SFTPPlugin.ID, 0, e
        .getMessage(), e));
  }

  /**
   * Returns the fetchRemoteTimestampImmediately.
   * @return boolean
   */
  public boolean isFetchRemoteTimestampImmediately(){
    return fetchRemoteTimestampImmediately;
  }

  /**
   * Sets the fetchRemoteTimestampImmediately.
   * @param fetchRemoteTimestampImmediately The fetchRemoteTimestampImmediately to set
   */
  public void setFetchRemoteTimestampImmediately(
      boolean fetchRemoteTimestampImmediately){
    this.fetchRemoteTimestampImmediately=fetchRemoteTimestampImmediately;
  }

  public synchronized SFTPSubscriber getSubscriber(){
    if(subscriber==null){
      subscriber=new SFTPSubscriber();
    }
    return subscriber;
  }
}
