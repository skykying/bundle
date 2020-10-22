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

import org.eclipse.team.core.synchronize.FastSyncInfoFilter;
import org.eclipse.team.core.synchronize.SyncInfo;
import org.eclipse.team.core.synchronize.FastSyncInfoFilter.SyncInfoDirectionFilter;
import org.eclipse.team.ui.synchronize.ISynchronizePageConfiguration;

import com.jcraft.eclipse.team.internal.sftp.Policy;

/**
 * Perform a get which will also affect outgoing and conflicting changes
 */
public class SFTPOverrideGetAction extends SFTPGetAction{

  protected SFTPOverrideGetAction(ISynchronizePageConfiguration configuration){
    super(configuration, configuration.getSite().getSelectionProvider(),
        "SFTPOverrideGetAction.", Policy.getBundle()); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.ui.sync.SubscriberAction#getSyncInfoFilter()
   */
  protected FastSyncInfoFilter getSyncInfoFilter(){
    return new SyncInfoDirectionFilter(new int[] {SyncInfo.CONFLICTING,
        SyncInfo.OUTGOING});
  }

  protected boolean getLocalOverwrite(){
    return true;
  }
}
