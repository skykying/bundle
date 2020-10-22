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
package com.jcraft.eclipse.sftp;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Allows nesting of SFTP operations.
 * 
 * Each runnable must leave the connection in the same directory as it started
 */
public interface ISFtpRunnable{

  public void run(IProgressMonitor monitor) throws SFtpException;

}
