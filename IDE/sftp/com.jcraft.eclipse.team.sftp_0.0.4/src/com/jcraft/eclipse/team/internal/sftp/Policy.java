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

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;

public class Policy{
  protected static ResourceBundle bundle=null;

  //debug constants
  public static boolean DEBUG_REQUESTS=false;
  public static boolean DEBUG_RESPONSES=false;
  public static boolean DEBUG_LIST=false;

  static{
    //init debug options
    if(SFTPPlugin.getPlugin().isDebugging()){
      DEBUG_REQUESTS="true".equalsIgnoreCase(Platform.getDebugOption(SFTPPlugin.ID+"/requests"));//$NON-NLS-1$ //$NON-NLS-2$
      DEBUG_RESPONSES="true".equalsIgnoreCase(Platform.getDebugOption(SFTPPlugin.ID+"/responses"));//$NON-NLS-1$ //$NON-NLS-2$
      DEBUG_LIST="true".equalsIgnoreCase(Platform.getDebugOption(SFTPPlugin.ID+"/list"));//$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * Creates a NLS catalog for the given locale.
   */
  public static void localize(String bundleName){
    bundle=ResourceBundle.getBundle(bundleName);
  }

  /**
   * Lookup the message with the given ID in this catalog and bind its
   * substitution locations with the given string.
   */
  public static String bind(String id, String binding){
    return bind(id, new String[] {binding});
  }

  /**
   * Lookup the message with the given ID in this catalog and bind its
   * substitution locations with the given strings.
   */
  public static String bind(String id, String binding1, String binding2){
    return bind(id, new String[] {binding1, binding2});
  }

  /**
   * Gets a string from the resource bundle. We don't want to crash because of a missing String.
   * Returns the key if not found.
   */
  public static String bind(String key){
    try{
      return bundle.getString(key);
    }
    catch(MissingResourceException e){
      return key;
    }
    catch(NullPointerException e){
      return "!"+key+"!"; //$NON-NLS-1$  //$NON-NLS-2$
    }
  }

  /**
   * Gets a string from the resource bundle and binds it with the given arguments. If the key is 
   * not found, return the key.
   */
  public static String bind(String key, Object[] args){
    try{
      return MessageFormat.format(bind(key), args);
    }
    catch(MissingResourceException e){
      return key;
    }
    catch(NullPointerException e){
      return "!"+key+"!"; //$NON-NLS-1$  //$NON-NLS-2$
    }
  }

  /**
   * Progress monitor helpers
   */
  public static void checkCanceled(IProgressMonitor monitor){
    if(monitor.isCanceled())
      throw new OperationCanceledException();
  }

  public static IProgressMonitor monitorFor(IProgressMonitor monitor){
    if(monitor==null)
      return new NullProgressMonitor();
    return monitor;
  }

  public static IProgressMonitor subMonitorFor(IProgressMonitor monitor,
      int ticks){
    if(monitor==null)
      return new NullProgressMonitor();
    if(monitor instanceof NullProgressMonitor)
      return monitor;
    return new SubProgressMonitor(monitor, ticks);
  }

  public static IProgressMonitor subMonitorFor(IProgressMonitor monitor,
      int ticks, int style){
    if(monitor==null)
      return new NullProgressMonitor();
    if(monitor instanceof NullProgressMonitor)
      return monitor;
    return new SubProgressMonitor(monitor, ticks, style);
  }

  public static ResourceBundle getBundle(){
    return bundle;
  }

}
