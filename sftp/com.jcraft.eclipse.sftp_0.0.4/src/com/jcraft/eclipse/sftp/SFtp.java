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

import java.net.URL;

import com.jcraft.eclipse.sftp.internal.*;

/**
 * From this class, you can create SFTP clients
 * for transferring data to and from an SFTP server.
 * 
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. 
 * It is being made available at this early stage to solicit feedback from pioneering 
 * adopters on the understanding that any code that uses this API will almost 
 * certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public class SFtp{

  /**
   * Create a client that can be used to transfer data to and from an SFTP server.
   * @param url a url of the form sftp://host:port/path
   * @return an SFTP client
   * @throws SFtpException
   */
  public static IClient createClient(URL url) throws SFtpException{
    validateUrl(url);
    SFTPServerLocation location=new SFTPServerLocation(url);
    return new SFTPClient(location, new ISFTPClientListener(){
      public void responseReceived(int responseCode, String responseText){
        if(SFtpPlugin.DEBUG_RESPONSES){
          System.out.println(responseCode+" "+responseText); //$NON-NLS-1$
        }
      }

      public void requestSent(String command, String argument){
        if(SFtpPlugin.DEBUG_REQUESTS){
          System.out.println(command+" "+argument); //$NON-NLS-1$
        }
      }
    });
  }

  /**
   * Validate that the given URL identifies an sftp server.
   * @param url a url that is potentially being used to connect to an sftp server
   */
  public static void validateUrl(URL url) throws SFtpException{
    if(!url.getProtocol().equals("sftp")&& //$NON-NLS-1$
        !url.getProtocol().equals("https")){ //$NON-NLS-1$
      throw new SFtpException(SFtpPlugin.getResourceString(
          "SFtp.0", url.toExternalForm()), ISFtpStatus.INVALID_URL_PROTOCOL); //$NON-NLS-1$
    }
  }

  public static String hackedURL(String location){
    if(location.toLowerCase().indexOf("sftp://")==0){ //$NON-NLS-1$
      location="https://"+location.substring(7, location.length());
    }
    return location;
  }

  public static String unhackedURL(String location){
    if(location.toLowerCase().indexOf("https://")==0){ //$NON-NLS-1$
      location="sftp://"+location.substring(8, location.length());
    }
    return location;
  }
}
