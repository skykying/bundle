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

import org.eclipse.team.internal.ui.synchronize.ScopableSubscriberParticipant;
import org.eclipse.team.ui.TeamUI;
import org.eclipse.team.ui.synchronize.ISynchronizePageConfiguration;
import org.eclipse.team.ui.synchronize.ISynchronizeParticipantDescriptor;
import org.eclipse.team.ui.synchronize.ISynchronizeScope;
import org.eclipse.team.ui.synchronize.SynchronizeModelAction;
import org.eclipse.team.ui.synchronize.SynchronizePageActionGroup;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;

import com.jcraft.eclipse.team.internal.sftp.Policy;
import com.jcraft.eclipse.team.internal.sftp.SFTPPlugin;

public class SFTPSynchronizeParticipant extends ScopableSubscriberParticipant{

  public static final String ID="com.jcraft.eclipse.team.sftp.synchronize-participant"; //$NON-NLS-1$

  public static final String ACTIONS_GROUP="target_actions"; //$NON-NLS-1$
  public static final String CONTEXT_MENU_CONTRIBUTION_GROUP_1="context_group_1"; //$NON-NLS-1$
  public static final String CONTEXT_MENU_CONTRIBUTION_GROUP_2="context_group_2"; //$NON-NLS-1$

  class SFTPActions extends SynchronizePageActionGroup{
    public void initialize(ISynchronizePageConfiguration configuration){
      super.initialize(configuration);
      SynchronizeModelAction putToolbar=new SFTPPutAction(configuration,
          getVisibleRootsSelectionProvider(), "action.sftp.put.", //$NON-NLS-1$
          Policy.getBundle());
      SynchronizeModelAction getToolbar=new SFTPGetAction(configuration,
          getVisibleRootsSelectionProvider(), "action.sftp.get.", //$NON-NLS-1$
          Policy.getBundle());
      appendToGroup(ISynchronizePageConfiguration.P_TOOLBAR_MENU,
          ACTIONS_GROUP, getToolbar);
      appendToGroup(ISynchronizePageConfiguration.P_TOOLBAR_MENU,
          ACTIONS_GROUP, putToolbar);

      appendToGroup(ISynchronizePageConfiguration.P_CONTEXT_MENU,
          CONTEXT_MENU_CONTRIBUTION_GROUP_1, new SFTPGetAction(configuration));
      appendToGroup(ISynchronizePageConfiguration.P_CONTEXT_MENU,
          CONTEXT_MENU_CONTRIBUTION_GROUP_1, new SFTPPutAction(configuration));
      appendToGroup(ISynchronizePageConfiguration.P_CONTEXT_MENU,
          CONTEXT_MENU_CONTRIBUTION_GROUP_2, new SFTPOverrideGetAction(
              configuration));
      appendToGroup(ISynchronizePageConfiguration.P_CONTEXT_MENU,
          CONTEXT_MENU_CONTRIBUTION_GROUP_2, new SFTPOverridePutAction(
              configuration));
    }
  }

  public SFTPSynchronizeParticipant(){
  }

  public SFTPSynchronizeParticipant(ISynchronizeScope scope){
    super(scope);
    setSubscriber(SFTPPlugin.getPlugin().getSubscriber());
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.ui.synchronize.ISynchronizeParticipant#init(org.eclipse.ui.IMemento)
   */
  public void init(String secondaryId, IMemento memento)
      throws PartInitException{
    super.init(secondaryId, memento);
    setSubscriber(SFTPPlugin.getPlugin().getSubscriber());
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.ui.synchronize.subscribers.SubscriberParticipant#initializeConfiguration(org.eclipse.team.ui.synchronize.ISynchronizePageConfiguration)
   */
  protected void initializeConfiguration(
      ISynchronizePageConfiguration configuration){
    super.initializeConfiguration(configuration);
    SFTPActions actions=new SFTPActions();
    configuration.setSupportedModes(ISynchronizePageConfiguration.ALL_MODES);
    configuration.addMenuGroup(ISynchronizePageConfiguration.P_TOOLBAR_MENU,
        ACTIONS_GROUP);
    configuration.addMenuGroup(ISynchronizePageConfiguration.P_CONTEXT_MENU,
        CONTEXT_MENU_CONTRIBUTION_GROUP_1);
    configuration.addMenuGroup(ISynchronizePageConfiguration.P_CONTEXT_MENU,
        CONTEXT_MENU_CONTRIBUTION_GROUP_2);
    configuration.addActionContribution(actions);
  }

  protected ISynchronizeParticipantDescriptor getDescriptor(){
    return TeamUI.getSynchronizeManager().getParticipantDescriptor(ID);
  }
}
