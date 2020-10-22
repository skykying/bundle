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

import java.net.URL;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.target.ui.SiteCreationWizardPage;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.target.subscriber.TargetDeploymentProvider;
import org.eclipse.team.internal.target.subscriber.TargetSubscriber;
import org.eclipse.team.internal.target.ui.ImportWizard;
import org.eclipse.team.internal.ui.ITeamUIImages;
import org.eclipse.team.internal.ui.TeamUIPlugin;
import org.eclipse.team.ui.synchronize.ISynchronizeScope;
import org.eclipse.team.ui.synchronize.SubscriberParticipant;
import org.eclipse.ui.IWorkbench;

import com.jcraft.eclipse.target.internal.sftp.SFtpTargetPlugin;
import com.jcraft.eclipse.target.internal.sftp.ui.SFtpSiteCreationPage;
import com.jcraft.eclipse.team.internal.sftp.Policy;
import com.jcraft.eclipse.team.internal.sftp.SFTPPlugin;
import com.jcraft.eclipse.team.internal.sftp.subscriber.*;

public class SFTPImportWizard extends ImportWizard{
  protected void initializeDialogSettings(){
    // initializes the dialogs settings store
    IDialogSettings workbenchSettings=SFTPPlugin.getPlugin()
        .getDialogSettings();
    IDialogSettings section=workbenchSettings.getSection("ImportWizard");//$NON-NLS-1$
    if(section==null){
      section=workbenchSettings.addNewSection("ImportWizard");//$NON-NLS-1$
    }
    setDialogSettings(section);
  }

  public String getDeploymentProviderId(){
    return SFTPPlugin.DEPLOYMENT_PROVIDER_ID;
  }

  protected String getSiteType(){
    return SFtpTargetPlugin.SITE_TYPE_ID;
  }

  /* (non-Javadoc)
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
   */
  public void init(IWorkbench workbench, IStructuredSelection currentSelection){
    super.init(workbench, currentSelection);
  }

  /* (non-Javadoc)
   * @see org.eclipse.jface.wizard.IWizard#getWindowTitle()
   */
  public String getWindowTitle(){
    return Policy.bind("SFTPImportWizard.5"); //$NON-NLS-1$
  }

  protected TargetDeploymentProvider createDeploymentProvider(URL url){
    return new SFTPDeploymentProvider(url);
  }

  protected void setBaseBytes(IContainer container,
      TargetDeploymentProvider provider) throws TeamException{
    // We know the remote directory exists so update the base in the synchronizer
    ((SFTPSynchronizer)provider.getSynchronizer()).setBaseBytes(container,
        SFTPSubscriberResource.FOLDER_BYTES);
  }

  protected SiteCreationWizardPage createSiteCreationPage(){
    return new SFtpSiteCreationPage("sftp-site-creation", //$NON-NLS-1$
        Policy.bind("SFTPMainPage.name"), //$NON-NLS-1$
        Policy.bind("SFTPMainPage.description"), //$NON-NLS-1$
        TeamUIPlugin.getImageDescriptor(ITeamUIImages.IMG_WIZBAN_SHARE));
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.ui.target.subscriber.ExportWizard#getSubscriber()
   */
  public TargetSubscriber getSubscriber(){
    return SFTPPlugin.getPlugin().getSubscriber();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.ui.target.subscriber.DeploymentWizard#createParticipant()
   */
  protected SubscriberParticipant createParticipant(ISynchronizeScope scope){
    return new SFTPSynchronizeParticipant(scope);
  }
}
