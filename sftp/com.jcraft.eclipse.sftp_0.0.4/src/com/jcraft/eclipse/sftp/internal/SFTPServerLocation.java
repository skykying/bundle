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
package com.jcraft.eclipse.sftp.internal;

import java.net.URL;

public class SFTPServerLocation{
  public static final int DEFAULT_PORT=22;
  private URL url;
  private String username;
  private String password;

  /**
   * Creates a new SFTP location.
   * 
   * @param url URL which provides hostname and port
   */
  public SFTPServerLocation(URL url){
    this.url=url;
    String userInfo=url.getUserInfo();
    if(userInfo!=null&&userInfo.length()>0){
      int colon=userInfo.indexOf(':');
      if(colon==-1){
        username=userInfo;
      }
      else{
        username=userInfo.substring(0, colon);
        password=userInfo.substring(colon+1);
      }
    }
  }

  public String getHostname(){
    String hostname=url.getHost();
    return hostname;
  }

  public int getPort(){
    return url.getPort()==-1 ? DEFAULT_PORT : url.getPort();
  }

  public String getUsername(){
    return username;
  }

  public String getPassword(){
    return password;
  }

  /*
   * Returns a human-readable string for debugging purposes.
   */
  public String toString(){
    StringBuffer buf=new StringBuffer("sftp://"); //$NON-NLS-1$
    buf.append(username);
    buf.append(':');
    buf.append(password);
    buf.append('@');
    buf.append(getHostname());
    buf.append(':');
    buf.append(Integer.toString(getPort()));
    return buf.toString();
  }

  public URL getUrl(){
    return url;
  }

  public void setAuthentication(String username, String password){
    this.username=username;
    this.password=password;
  }
}
