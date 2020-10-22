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

import java.util.*;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.core.NullSubProgressMonitor;
import org.eclipse.team.internal.target.subscriber.PutOperation;
import org.eclipse.team.internal.target.subscriber.TargetDeploymentProvider;

import com.jcraft.eclipse.sftp.*;
import com.jcraft.eclipse.team.internal.sftp.*;

/**
 * SFTP specific put operation
 */
public class SFTPPutOperation extends PutOperation{

  private List changed=new ArrayList();

  public SFTPPutOperation(IResource[] resources, int depth,
      boolean overwriteRemoteChanges){
    super(SFTPPlugin.DEPLOYMENT_PROVIDER_ID, resources, depth,
        overwriteRemoteChanges);
  }

  protected void needsRemoteIdentifier(TargetDeploymentProvider provider,
      IResource localResource, IProgressMonitor monitor) throws TeamException{
    if(localResource.getType()==IResource.FILE){
      changed.add(localResource);
    }
    else{
      ((SFTPSynchronizer)SFTPPlugin.getPlugin().getSubscriber()
          .getSynchronizer()).setBaseBytes(localResource,
          SFTPSubscriberResource.FOLDER_BYTES);
    }
  }

  private void provideRemoteIdentifiers(TargetDeploymentProvider provider,
      IProgressMonitor progress) throws TeamException{
    // Get the remote identifiers for any resources that require it
    if(!changed.isEmpty()){
      try{
        // group the resources by parent directory
        final Map resourcesByParent=new HashMap();
        for(Iterator iter=changed.iterator(); iter.hasNext();){
          IResource resource=(IResource)iter.next();
          List children=(List)resourcesByParent.get(resource.getParent());
          if(children==null){
            children=new ArrayList();
            resourcesByParent.put(resource.getParent(), children);
          }
          children.add(resource);
        }
        // Update the base bytes for all the resources
        IProgressMonitor noProgress=new NullSubProgressMonitor(progress);
        for(Iterator iter=resourcesByParent.keySet().iterator(); iter.hasNext();){
          IContainer parent=(IContainer)iter.next();
          List children=(List)resourcesByParent.get(parent);
          IDirectoryEntry entries[]=getEntries(provider, parent, noProgress);
          for(Iterator iterator=children.iterator(); iterator.hasNext();){
            IResource resource=(IResource)iterator.next();
            IDirectoryEntry entry=findEntry(resource.getName(), entries);
            if(entry==null){
              throw new SFTPException(Policy.bind(
                  "SFTPPutOperation.0", resource.getFullPath().toString())); //$NON-NLS-1$
            }
            getSynchronizer(provider).setBaseBytes(resource,
                SFTPSubscriberResource.getSyncBytes(entry));
          }
        }
      }
      finally{
        // remove the processed nodes from the list
        changed.clear();
      }
    }
  }

  private IDirectoryEntry[] getEntries(TargetDeploymentProvider provider,
      IContainer parent, IProgressMonitor monitor) throws SFTPException,
      TeamException{
    SFTPDeploymentProvider sftp=(SFTPDeploymentProvider)provider;
    try{
      return getClient(sftp).listFiles(sftp.getRelativePath(parent).toString(),
          false, monitor);
    }
    catch(SFtpException e){
      throw SFTPException.wrapException(e);
    }
  }

  private IDirectoryEntry findEntry(String name, IDirectoryEntry[] entries){
    for(int i=0; i<entries.length; i++){
      IDirectoryEntry entry=entries[i];
      if(entry.getName().equals(name)){
        return entry;
      }
    }
    return null;
  }

  protected String getRemoteRoot(SFTPDeploymentProvider provider){
    return provider.getHostRelativePath();
  }

  protected String getProviderRelativePath(SFTPDeploymentProvider provider,
      IResource resource) throws TeamException{
    return provider.getRelativePath(resource).toString();
  }

  public IClient getClient(SFTPDeploymentProvider provider){
    return provider.getOpenClient();
  }

  /* (non-Javadoc)
   * @see com.jcraft.eclipse.team.internal.sftp.deployment.SFTPOperation#execute(org.eclipse.team.core.target.TargetDeploymentProvider, org.eclipse.core.resources.IResource[], org.eclipse.core.runtime.IProgressMonitor)
   */
  protected void execute(TargetDeploymentProvider provider,
      IResource[] resources, IProgressMonitor monitor) throws TeamException{

    monitor.beginTask(null, 100);
    super.execute(provider, resources, Policy.subMonitorFor(monitor, 80));
    provideRemoteIdentifiers(provider, Policy.subMonitorFor(monitor, 20));
    monitor.done();
  }

}
