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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.variants.ThreeWaySynchronizer;

import com.jcraft.eclipse.team.internal.sftp.SFTPPlugin;

/**
 * This class provides workspace synchronization for SFTP
 */
public class SFTPSynchronizer extends ThreeWaySynchronizer{

  protected SFTPSynchronizer(){
    super(new QualifiedName(SFTPPlugin.ID, "workspace-sync")); //$NON-NLS-1$
  }

  public void setBaseBytes(IResource resource, byte[] remoteBaseIdentifier)
      throws TeamException{
    if(resource.getType()!=IResource.FILE){
      byte[] _remoteBaseIdentifier=SFTPSubscriberResource.FOLDER_BYTES;
      super.setBaseBytes(resource, _remoteBaseIdentifier);
      return;
    }
    super.setBaseBytes(resource, remoteBaseIdentifier);
  }

  public boolean setRemoteBytes(IResource resource, byte[] remoteIdentifier)
      throws TeamException{
    if(resource.getType()!=IResource.FILE){
      byte[] _remoteIdentifier=SFTPSubscriberResource.FOLDER_BYTES;
      return super.setRemoteBytes(resource, _remoteIdentifier);
    }
    return super.setRemoteBytes(resource, remoteIdentifier);
  }

}
