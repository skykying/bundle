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
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.variants.ThreeWayRemoteTree;
import org.eclipse.team.internal.target.subscriber.PutOperation;
import org.eclipse.team.internal.target.subscriber.TargetSubscriber;

import com.jcraft.eclipse.team.internal.sftp.Policy;
import com.jcraft.eclipse.team.internal.sftp.SFTPPlugin;

/**
 * This is the SFTP Subscriber
 */
public class SFTPSubscriber extends TargetSubscriber{

  public SFTPSubscriber(){
    super(new SFTPSynchronizer());
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.subscribers.TeamSubscriber#getDescription()
   */
  public String getName(){
    return Policy.bind("SFTPSubscriber.0"); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.core.target.TargetSubscriber#getDeploymentProviderId()
   */
  protected String getDeploymentProviderId(){
    return SFTPPlugin.DEPLOYMENT_PROVIDER_ID;
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.core.target.TargetSubscriber#getPutOperation(org.eclipse.core.resources.IResource[], int, boolean)
   */
  protected PutOperation getPutOperation(IResource[] resources, int depth,
      boolean overwriteRemoteChanges){
    return new SFTPPutOperation(resources, depth, overwriteRemoteChanges);
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.variants.ThreeWaySubscriber#createRemoteTree()
   */
  protected ThreeWayRemoteTree createRemoteTree(){
    return new SFTPRemoteResourceVariantTree();
  }

  public boolean isSupervised(IResource resource) throws TeamException {
    if(org.eclipse.team.core.Team.isIgnoredHint(resource)){
      return false;
    }
    return super.isSupervised(resource);
  }
}
