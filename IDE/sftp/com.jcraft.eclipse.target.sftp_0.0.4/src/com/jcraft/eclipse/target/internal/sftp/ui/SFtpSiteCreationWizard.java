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
package com.jcraft.eclipse.target.internal.sftp.ui;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.target.ui.SiteCreationWizard;
import org.eclipse.target.ui.SiteCreationWizardPage;

import com.jcraft.eclipse.target.internal.sftp.SFtpTargetPlugin;

public class SFtpSiteCreationWizard extends SiteCreationWizard{

  public SFtpSiteCreationWizard(){
    // initializes the dialogs settings store
    IDialogSettings workbenchSettings=SFtpTargetPlugin.getDefault()
        .getDialogSettings();
    IDialogSettings section=workbenchSettings
        .getSection("SFTPSiteCreationWizard");//$NON-NLS-1$
    if(section==null){
      section=workbenchSettings.addNewSection("SFTPSiteCreationWizard");//$NON-NLS-1$
    }
    setDialogSettings(section);
  }

  protected String getMainPageTitle(){
    return "SFTP";
  }

  protected String getMainPageDescription(){
    return "Create a connection to an SFTP site";
  }

  /*
   * @see org.eclipse.jface.wizard.IWizard#getWindowTitle()
   */
  public String getWindowTitle(){
    return "New SFTP Site";
  }

  protected SiteCreationWizardPage createSitePage(){
    return new SFtpSiteCreationPage("sftp-site-creation", getMainPageTitle(),
        getMainPageDescription(), null);
  }
}
