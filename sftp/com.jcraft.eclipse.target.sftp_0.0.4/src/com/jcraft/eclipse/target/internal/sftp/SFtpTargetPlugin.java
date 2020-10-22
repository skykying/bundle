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
package com.jcraft.eclipse.target.internal.sftp;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class SFtpTargetPlugin extends AbstractUIPlugin{

  public static final String ID="com.jcraft.eclipse.target.sftp"; //$NON-NLS-1$
  public static final String SITE_TYPE_ID=ID+".site"; //$NON-NLS-1$

  //The shared instance.
  private static SFtpTargetPlugin plugin;

  /**
   * The constructor.
   */
  public SFtpTargetPlugin(){
    plugin=this;
  }

  /**
   * This method is called upon plug-in activation
   */
  public void start(BundleContext context) throws Exception{
    super.start(context);
    Policy.localize("com.jcraft.eclipse.target.internal.sftp.messages"); //$NON-NLS-1$
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
  public static SFtpTargetPlugin getDefault(){
    return plugin;
  }

  public static void log(CoreException e){
    log(e.getMessage(), e);
  }

  public static void log(String string, CoreException e){
    getDefault().getLog().log(
        new Status(e.getStatus().getCode(), ID, 0, string, e));
  }
}
