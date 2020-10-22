/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class SFtpPlugin extends Plugin{

  public static final String ID="com.jcraft.eclipse.sftp"; //$NON-NLS-1$

  //The shared instance.
  private static SFtpPlugin plugin;
  //Resource bundle.
  private ResourceBundle resourceBundle;

  public static final boolean DEBUG_STREAMS=false;

  public static boolean DEBUG_LIST;

  public static final boolean DEBUG_RESPONSES=false;

  public static final boolean DEBUG_REQUESTS=false;

  /**
   * The constructor.
   */
  public SFtpPlugin(){
    super();
    plugin=this;
    try{
      resourceBundle=ResourceBundle
          .getBundle("com.jcraft.eclipse.sftp.internal.messages"); //$NON-NLS-1$
    }
    catch(MissingResourceException x){
      resourceBundle=null;
    }
  }

  /**
   * This method is called upon plug-in activation
   */
  public void start(BundleContext context) throws Exception{
    super.start(context);
  }

  /**
   * This method is called when the plug-in is stopped
   */
  public void stop(BundleContext context) throws Exception{
    super.stop(context);
  }

  /**
   * Returns the shared instance.
   */
  public static SFtpPlugin getDefault(){
    if(plugin==null)
      plugin=new SFtpPlugin();
    return plugin;
  }

  /**
   * Returns the string from the plugin's resource bundle,
   * or 'key' if not found.
   */
  public static String getResourceString(String key){
    ResourceBundle bundle=SFtpPlugin.getDefault().getResourceBundle();
    try{
      return (bundle!=null) ? bundle.getString(key) : key;
    }
    catch(MissingResourceException e){
      return key;
    }
  }

  public static String getResourceString(String key, String param){
    return getResourceString(key, new Object[] {param});
  }

  public static String getResourceString(String key, String param1,
      String param2){
    return getResourceString(key, new Object[] {param1, param2});
  }

  public static String getResourceString(String key, Object[] params){
    String message=null;
    try{
      ResourceBundle bundle=SFtpPlugin.getDefault().getResourceBundle();
      message=bundle.getString(key);
    }
    catch(MissingResourceException e){
      return key;
    }
    return MessageFormat.format(message, params);
  }

  /**
   * Returns the plugin's resource bundle,
   */
  public ResourceBundle getResourceBundle(){
    return resourceBundle;
  }

  public static IProgressMonitor monitorFor(IProgressMonitor monitor){
    if(monitor==null)
      monitor=new NullProgressMonitor();
    return monitor;
  }

  public static IProgressMonitor subMonitorFor(IProgressMonitor monitor, int i){
    return new SubProgressMonitor(monitor, i);
  }

  public static void log(IOException e){
    getDefault().getLog().log(
        new Status(IStatus.ERROR, ID, 0, e.getMessage(), e));
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
    return 32768
    -1024
    ;
  }
}
