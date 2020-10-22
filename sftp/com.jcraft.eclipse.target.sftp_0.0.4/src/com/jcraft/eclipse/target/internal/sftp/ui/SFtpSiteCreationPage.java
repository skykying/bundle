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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.target.Site;
import org.eclipse.target.Target;
import org.eclipse.target.ui.SiteCreationWizardPage;

import com.jcraft.eclipse.target.internal.sftp.*;

/*
 * Wizard page for configuring a project with a SFTP location.
 */
public class SFtpSiteCreationPage extends SiteCreationWizardPage{

  boolean isCheckout;

  // Widgets
  private Combo usernameCombo;
  private Combo hostnameUrlCombo;
  private Text password;
  private Combo timeoutCombo;

  // Dialog store id constants
  private static final String STORE_USERNAME_ID="SFTPWizardMainPage.STORE_USERNAME_ID";//$NON-NLS-1$
  private static final String STORE_URL_ID="SFTPWizardMainPage.STORE_HOSTNAME_ID";//$NON-NLS-1$
  private static final String STORE_TIMEOUT_ID="SFTPWizardMainPage.STORE_MODULE_ID";//$NON-NLS-1$

  /*
   * ConfigurationWizardMainPage constructor.
   */
  public SFtpSiteCreationPage(String pageName, String title,
      String description, ImageDescriptor titleImage){
    super(pageName, title, titleImage);
    setDescription(description);
    setTitle(title);
  }

  /*
   * Creates the UI part of the page.
   * 
   * @param parent  the parent of the created widgets
   */
  public void createControl(Composite parent){
    Composite composite=createComposite(parent, 2);
    setControl(composite);

    createLabel(composite, Policy.bind("SFTPWizardMainPage.url")); //$NON-NLS-1$
    hostnameUrlCombo=createEditableCombo(composite);
    hostnameUrlCombo.addListener(SWT.Modify, new Listener(){
      public void handleEvent(Event e){
        SFtpSiteCreationPage.this.validateFields();
      }
    });

    Group loginDetailsTabGroup=new Group(composite, SWT.NONE);
    loginDetailsTabGroup.setText(Policy.bind("SFTPWizardMainPage.loginFrame")); //$NON-NLS-1$
    GridData loginDetails=new GridData(GridData.FILL_HORIZONTAL);
    loginDetails.horizontalSpan=2;
    loginDetailsTabGroup.setLayoutData(loginDetails);
    loginDetailsTabGroup.setLayout(new GridLayout(2, false));

    createLabel(loginDetailsTabGroup, Policy
        .bind("SFTPWizardMainPage.userName")); //$NON-NLS-1$
    usernameCombo=createEditableCombo(loginDetailsTabGroup);
    usernameCombo.addListener(SWT.Modify, new Listener(){
      public void handleEvent(Event e){
        SFtpSiteCreationPage.this.validateFields();
      }
    });

    createLabel(loginDetailsTabGroup, Policy
        .bind("SFTPWizardMainPage.password")); //$NON-NLS-1$
    password=createTextField(loginDetailsTabGroup);
    password.setEchoChar('*');

    createLabel(composite, Policy.bind("SFTPWizardMainPage.timeout")); //$NON-NLS-1$
    timeoutCombo=createEditableCombo(composite);
    timeoutCombo.addListener(SWT.Modify, new Listener(){
      public void handleEvent(Event e){
        SFtpSiteCreationPage.this.validateFields();
      }
    });

    initializeValues();
    validateFields();
    hostnameUrlCombo.setFocus();
    setControl(composite);
  }

  public Site getSite(){
    if(!isPageComplete()){
      return null;
    }

    SFtpSite site;
    try{
      String url=getHostAsURL();
      if(url.indexOf("@")==-1){
        url=getHostAsURL(usernameCombo.getText());

      }
 
      site=(SFtpSite)Site.createSite(SFtpTargetPlugin.SITE_TYPE_ID, url);

      site.setUsername(usernameCombo.getText());
      site.setPassword(password.getText());
      try{
        String text=timeoutCombo.getText();
        int timeout=Integer.parseInt(text);
        site.setTimeout(timeout);
      }
      catch(NumberFormatException e){
        // Just skip the timeout
      }
    }
    catch(CoreException e){
      SFtpTargetPlugin.log(e);
      return null;
    }
    return site;
  }

  /*
   * @see WizardPage#finish
   */
  public boolean finish(IProgressMonitor monitor){
    // Set the result to be the current values
    saveWidgetValues();
    return true;
  }

  /*
   * Initializes states of the controls.
   */
  private void initializeValues(){
    IDialogSettings settings=getDialogSettings();
    if(settings!=null){
      String[] hostNames=settings.getArray(STORE_URL_ID);
      if(hostNames!=null){
        for(int i=0; i<hostNames.length; i++){
          hostnameUrlCombo.add(hostNames[i]);
        }
      }
      String[] userNames=settings.getArray(STORE_USERNAME_ID);
      if(userNames!=null){
        for(int i=0; i<userNames.length; i++){
          usernameCombo.add(userNames[i]);
        }
      }
      String[] timeouts=settings.getArray(STORE_TIMEOUT_ID);
      if(timeouts!=null){
        for(int i=0; i<timeouts.length; i++){
          timeoutCombo.add(timeouts[i]);
        }
      }
      timeoutCombo.setText(String.valueOf(SFtpSite.DEFAULT_TIMEOUT));
    }
  }

  /*
   * Saves the widget values
   */
  private void saveWidgetValues(){
    // Update history
    IDialogSettings settings=getDialogSettings();
    if(settings!=null){
      String[] userNames=settings.getArray(STORE_USERNAME_ID);
      if(userNames==null)
        userNames=new String[0];
      userNames=addToHistory(userNames, usernameCombo.getText());
      settings.put(STORE_USERNAME_ID, userNames);

      String[] hostNames=settings.getArray(STORE_URL_ID);
      if(hostNames==null)
        hostNames=new String[0];
      hostNames=addToHistory(hostNames, hostnameUrlCombo.getText());
      settings.put(STORE_URL_ID, hostNames);

      String[] modules=settings.getArray(STORE_TIMEOUT_ID);
      if(modules==null)
        modules=new String[0];
      modules=addToHistory(modules, timeoutCombo.getText());
      settings.put(STORE_TIMEOUT_ID, modules);
    }
  }

  private String getHostAsURL(){
    return getHostAsURL("");
  }

  private String getHostAsURL(String usr){
    if(usr.length()>0){
      usr=usr+"@";  //$NON-NLS-1$
    }
    String host=hostnameUrlCombo.getText();
    if(host.length()!=0){
      if(host.toLowerCase().indexOf("sftp://")!=0&& //$NON-NLS-1$
          host.indexOf("://")==-1){ //$NON-NLS-1$
        host="sftp://"+host; //$NON-NLS-1$
      }
      if(host.toLowerCase().indexOf("sftp://")==0&& //$NON-NLS-1$
          host.indexOf("@")==-1&& //$NON-NLS-1$
          usr.length()>0){
        host="sftp://"+usr+host.substring("sftp://".length()); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    return host;
  }

  /*
   * Validates the contents of the editable fields and set page completion and
   * error messages appropriately.
   */
  private void validateFields(){
    // Validate the server URL
    String host=getHostAsURL();
    if(host.length()==0){
      setPageComplete(false);
      return;
    }

    if(host.indexOf("://")!=0&&!host.startsWith("sftp://")){
      setMessage(Policy.bind("SFTPWizardMainPage.invalidServerURL"), WARNING); //$NON-NLS-1$
      setPageComplete(false);
      return;

    }

    try{
      String _host=com.jcraft.eclipse.sftp.SFtp.hackedURL(host);
      URL url=new URL(_host);
      String user=url.getUserInfo();
      if(user!=null && !usernameCombo.getText().equals(user)){
        usernameCombo.setText(user);
      }
    }
    catch(MalformedURLException e){
      setMessage(Policy.bind("SFTPWizardMainPage.invalidServerURL"), WARNING); //$NON-NLS-1$
      setPageComplete(false);
      return;
    }

    Site site=Target.getSiteManager().getSite(host);
    if(site!=null){
      setMessage(Policy.bind("SFTPWizardMainPage.locationExists"), WARNING); //$NON-NLS-1$
      setPageComplete(false);
      return;
    }

    String username=usernameCombo.getText();
    if(username.length()==0){
      setPageComplete(false);
      setMessage(
          Policy.bind("SFTPWizardMainPage.usernameMustBeSpecified"), WARNING); //$NON-NLS-1$
      return;
    }

    // validate the timeout
    String timeout=timeoutCombo.getText();
    try{
      int timeoutInt=Integer.parseInt(timeout);
      if(timeoutInt<=0){
        setMessage(Policy.bind("SFTPWizardMainPage.invalidTimeout"), WARNING); //$NON-NLS-1$
        setPageComplete(false);
        return;
      }
    }
    catch(NumberFormatException e){
      setMessage(Policy.bind("SFTPWizardMainPage.invalidTimeout"), WARNING); //$NON-NLS-1$
      setPageComplete(false);
      return;
    }

    setMessage(null);
    if(!isPageComplete()){
      setPageComplete(true);
    }
  }

  public void setVisible(boolean visible){
    super.setVisible(visible);
    if(visible){
      hostnameUrlCombo.setFocus();
    }
  }

}
