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
package com.jcraft.eclipse.team.internal.sftp.ui.subscriber;

import java.util.ResourceBundle;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.team.core.synchronize.SyncInfoSet;
import org.eclipse.team.internal.target.subscriber.ITargetOperations;
import org.eclipse.team.internal.target.ui.GetAction;
import org.eclipse.team.ui.synchronize.ISynchronizePageConfiguration;

import com.jcraft.eclipse.team.internal.sftp.Policy;
import com.jcraft.eclipse.team.internal.sftp.SFTPPlugin;

public class SFTPGetAction extends GetAction{

  protected SFTPGetAction(ISynchronizePageConfiguration configuration){
    super(configuration, configuration.getSite().getSelectionProvider(),
        "SFTPGetAction.", Policy.getBundle()); //$NON-NLS-1$
  }

  public SFTPGetAction(ISynchronizePageConfiguration configuration,
      ISelectionProvider provider, String string, ResourceBundle bundle){
    super(configuration, provider, string, bundle);
  }

  protected String getJobName(SyncInfoSet syncSet){
    return Policy.bind(
        "SFTPGetAction.0", new Integer(syncSet.size()).toString()); //$NON-NLS-1$
  }

  protected ITargetOperations getTargetOperations(){
    return SFTPPlugin.getPlugin().getSubscriber().getTeamOperations();
  }

}
