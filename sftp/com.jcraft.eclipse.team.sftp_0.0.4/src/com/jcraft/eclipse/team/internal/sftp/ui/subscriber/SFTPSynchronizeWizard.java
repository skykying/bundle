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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.team.ui.synchronize.*;

import com.jcraft.eclipse.team.internal.sftp.Policy;
import com.jcraft.eclipse.team.internal.sftp.SFTPPlugin;

public class SFTPSynchronizeWizard extends SubscriberParticipantWizard{

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.ui.synchronize.SubscriberParticipantWizard#getRootResources()
   */
  protected IResource[] getRootResources(){
    return SFTPPlugin.getPlugin().getSubscriber().roots();
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.ui.synchronize.SubscriberParticipantWizard#createParticipant(org.eclipse.core.resources.IResource[])
   */
  protected SubscriberParticipant createParticipant(ISynchronizeScope scope){
    return new SFTPSynchronizeParticipant(scope);
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.ui.synchronize.SubscriberParticipantWizard#getName()
   */
  protected String getPageTitle(){
    return Policy.bind("SFTPSynchronizeWizard.0"); //$NON-NLS-1$
  }

  /* (non-Javadoc)
   * @see org.eclipse.team.internal.ui.synchronize.SubscriberParticipantWizard#getImportWizard()
   */
  protected IWizard getImportWizard(){
    return new SFTPImportWizard();
  }
}
