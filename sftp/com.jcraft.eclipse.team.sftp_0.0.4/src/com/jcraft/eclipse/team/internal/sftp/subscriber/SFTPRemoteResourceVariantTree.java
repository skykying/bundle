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

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.team.core.IIgnoreInfo;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.core.variants.IResourceVariant;
import org.eclipse.team.core.variants.ThreeWayRemoteTree;
import org.eclipse.team.internal.core.StringMatcher;
import org.eclipse.team.target.DeploymentProvider;
import org.eclipse.team.target.TeamTarget;

import com.jcraft.eclipse.sftp.*;
import com.jcraft.eclipse.sftp.internal.SFTPClient;
import com.jcraft.eclipse.sftp.internal.SFTPDirectoryEntry;
import com.jcraft.eclipse.team.internal.sftp.*;

public class SFTPRemoteResourceVariantTree extends ThreeWayRemoteTree{

  private static final IDirectoryEntry IS_URL_ROOT=new SFTPDirectoryEntry(
      SFTPClient.PARENT_DIRECTORY_NAME, true, false, 0, new Date(0));

  public SFTPRemoteResourceVariantTree(){
    super(SFTPPlugin.getPlugin().getSubscriber());
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.subscribers.RefreshOperation#getRemoteChildren(org.eclipse.team.core.sync.IRemoteResource, org.eclipse.core.runtime.IProgressMonitor)
   */
  protected IResourceVariant[] fetchMembers(IResourceVariant remote,
      IProgressMonitor progress) throws TeamException{
    if(remote.isContainer()){
      IDirectoryEntry[] entries=getEntries(remote, progress);
      
      Vector tmp=new Vector();
      for(int i=0; i<entries.length; i++){
        IDirectoryEntry entry=entries[i];
        if(matchesEnabledIgnore(new Path(null, entry.getName()))){
          continue;
        }
        tmp.addElement(entry);
      }
      if(tmp.size()!=entries.length){
        IDirectoryEntry[] foo=new IDirectoryEntry[tmp.size()];
        if(tmp.size()>0)
        System.arraycopy(tmp.toArray(), 0, foo, 0, tmp.size());
        entries=foo;
      }

      IResourceVariant[] result=new IResourceVariant[entries.length];
      for(int i=0; i<entries.length; i++){
        IDirectoryEntry entry=entries[i];
        result[i]=SFTPSubscriberResource.create((SFTPSubscriberResource)remote,
            entry);
      }
      return result;
    }
    else{
      return new IResourceVariant[0];
    }
  }

  private IDirectoryEntry[] getEntries(IResourceVariant remote,
      IProgressMonitor monitor) throws TeamException{
    ISFTPRunnableContext context=((SFTPSubscriberResource)remote).getContext();
    try{
      return context.getOpenClient().listFiles(
          context.getRelativePath(((SFTPSubscriberResource)remote).getUrl())
              .toString(), false, monitor);
    }
    catch(SFtpException e){
      throw SFTPException.wrapException(e);
    }
  }

  private SFTPDeploymentProvider getMappedProvider(IResource resource)
      throws TeamException{
    DeploymentProvider[] providers=TeamTarget.getDeploymentManager()
        .getMappings(resource, SFTPPlugin.DEPLOYMENT_PROVIDER_ID);
    if(providers.length==0){
      throw new TeamException(Policy.bind(
          "SFTPRefreshOperation.2", resource.getFullPath().toString())); //$NON-NLS-1$
    }
    return (SFTPDeploymentProvider)providers[0];
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.subscribers.RefreshOperation#buildRemoteTree(org.eclipse.core.resources.IResource, int, boolean, org.eclipse.core.runtime.IProgressMonitor)
   */
  protected IResourceVariant fetchVariant(IResource resource, int depth,
      IProgressMonitor monitor) throws TeamException{

    final SFTPDeploymentProvider sftpProvider=getMappedProvider(resource);
    IDirectoryEntry entry=getEntry(sftpProvider, resource, monitor);
    if(entry==null){
      return null;
    }
    else{
      return SFTPSubscriberResource.create(sftpProvider, resource, entry);
    }
  }

  private IDirectoryEntry getEntry(SFTPDeploymentProvider provider,
      IResource resource, IProgressMonitor monitor) throws TeamException{
    if(resource.getType()==IResource.FILE){
      return fetchEntryForFile(provider, provider.getRelativePath(resource),
          monitor);
    }
    else{
      return fetchEntry(provider, provider.getRelativePath(resource), monitor);
    }
  }

  /*
   * This method probably throws an exception if the parent doesn't exist
   */
  protected IDirectoryEntry fetchEntry(SFTPDeploymentProvider provider,
      IPath providerRelativePath, IProgressMonitor progress)
      throws TeamException{
    try{
      IDirectoryEntry[] entries=provider.getOpenClient()
          .listFiles(providerRelativePath.removeLastSegments(1).toString(),
              true, progress);
      String name=providerRelativePath.lastSegment();
      if(providerRelativePath.isEmpty()){
        // We're at the root and it must exist since we got entries
        // Check for the . in the list
        name=SFTPClient.PARENT_DIRECTORY_NAME;
      }
      for(int i=0; i<entries.length; i++){
        IDirectoryEntry dirEntry=entries[i];
        if(dirEntry.getName().equals(name)){
          return dirEntry;
        }
      }
      if(name.equals(SFTPClient.PARENT_DIRECTORY_NAME)){
        // The list didn't have a dot so just use a dummy entry to indicate the folder exists
        return IS_URL_ROOT;
      }
      return null;
    }
    catch(SFtpException e){
      if(e.getStatus().getCode()==ISFtpStatus.DOES_NOT_EXIST){
        return null;
      }
      throw SFTPException.wrapException(e);
    }
  }

  /*
   * This method probably throws an exception if the parent doesn't exist
   */
  protected IDirectoryEntry fetchEntryForFile(SFTPDeploymentProvider provider,
      IPath providerRelativePath, IProgressMonitor progress)
      throws TeamException{
    try{
      IDirectoryEntry[] entries=provider.getOpenClient().listFiles(
          providerRelativePath.toString(), false, progress);
      if(entries.length==0)
        return null;
      if(entries.length>1){
        // Wrong number of entries. Is it a folder
        throw new SFTPException(Policy
            .bind("SFTPTargetProvider.remoteNotAFile1")); //$NON-NLS-1$
      }
      IDirectoryEntry dirEntry=entries[0];
      if(dirEntry.getName().equals(providerRelativePath.lastSegment())){
        return dirEntry;
      }
      if(new Path(dirEntry.getName()).equals(providerRelativePath)){
        return new SFTPDirectoryEntry(providerRelativePath.lastSegment(),
            dirEntry.hasDirectorySemantics(), dirEntry.hasFileSemantics(),
            dirEntry.getSize(), dirEntry.getModTime());
      }
      else{
        // Wrong entry. The remote must be a folder
        throw new SFTPException(Policy
            .bind("SFTPTargetProvider.remoteNotAFile2")); //$NON-NLS-1$
      }
    }
    catch(SFtpException e){
      if(e.getStatus().getCode()==ISFtpStatus.DOES_NOT_EXIST){
        return null;
      }
      throw SFTPException.wrapException(e);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.core.subscribers.helpers.RefreshOperation#refresh(org.eclipse.core.resources.IResource, int, boolean, org.eclipse.core.runtime.IProgressMonitor)
   */
  protected IResource[] refresh(final IResource resource, final int depth,
      IProgressMonitor monitor) throws TeamException{

    SFTPDeploymentProvider provider=getMappedProvider(resource);
    final IResource[][] result=new IResource[1][0];
    result[0]=null;
    final TeamException[] exception=new TeamException[] {null};
    provider.run(new ISFtpRunnable(){

      public void run(IProgressMonitor monitor) throws SFtpException{
        try{
          result[0]=SFTPRemoteResourceVariantTree.super.refresh(resource,
              depth, monitor);
        }
        catch(TeamException e){
          exception[0]=e;
        }
      }
    }, monitor);
    if(exception[0]!=null){
      throw exception[0];
    }
    return result[0];
  }

  private static StringMatcher[] ignoreMatchers=null;
  private static boolean matchesEnabledIgnore(IPath resource) {
    StringMatcher[] matchers = getStringMatchers();
    for (int i = 0; i < matchers.length; i++) {
      String resourceName = resource.lastSegment();
      //if(matchers[i].isPathPattern()) {
      //  resourceName = resource.getFullPath().toString();
      //}
      if (matchers[i].match(resourceName)) return true;
    }
    return false;
  }
  private synchronized static StringMatcher[] getStringMatchers() {
    if (ignoreMatchers==null) {
      IIgnoreInfo[] ignorePatterns = org.eclipse.team.core.Team.getAllIgnores();
      ArrayList matchers = new ArrayList(ignorePatterns.length);
      for (int i = 0; i < ignorePatterns.length; i++) {
        if (ignorePatterns[i].getEnabled()) {
          matchers.add(new StringMatcher(ignorePatterns[i].getPattern(), true, false));
        }
      }
      ignoreMatchers = new StringMatcher[matchers.size()];
      ignoreMatchers = (StringMatcher[]) matchers.toArray(ignoreMatchers);
    }
    return ignoreMatchers;
  }
}
