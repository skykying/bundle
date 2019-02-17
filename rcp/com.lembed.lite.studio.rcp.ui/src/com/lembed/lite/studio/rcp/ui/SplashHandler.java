/*******************************************************************************
* Copyright (c) 2017 LEMBED
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* LEMBED - setup for LiteSTUDIO
*******************************************************************************/

package com.lembed.lite.studio.rcp.ui;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.branding.IProductConstants;
import org.eclipse.ui.splash.BasicSplashHandler;

import com.lembed.lite.studio.rcp.ui.messages.Messages;

/**
 * Custom splash handler
 *
 */
public class SplashHandler extends BasicSplashHandler {

    private static final Point VERSION_LOCATION = new Point(400, 280);
    private static final Rectangle PROCESS_BAR_RECTANGLE = new Rectangle(0, 320, 500, 5);
    private static final RGB FOREGROUND_COLOR = new RGB(255, 255, 255);

    @Override
    public void init(Shell splash) {
        super.init(splash);
 
        String progressString = null;

        // Try to get the progress bar and message updater.
        IProduct product = Platform.getProduct();
        if (product != null) {
            progressString = product.getProperty(IProductConstants.STARTUP_PROGRESS_RECT);
        }
        Rectangle progressRect = StringConverter.asRectangle(progressString, PROCESS_BAR_RECTANGLE);
        setProgressRect(progressRect);

        // Set font color.
        setForeground(FOREGROUND_COLOR);
        
        @SuppressWarnings("unused")
		String copyright = "     Copyright © 2016-2017 Lembed Electronic Co., Ltd"; //$NON-NLS-1$
        String ver = LiteRcpUiPlugin.getDefault().getBundle().getVersion().toString().substring(0, 5).toUpperCase();
      
         
        // Set the software version.
        getContent().addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                e.gc.setForeground(getForeground());
                e.gc.drawText(
                        NLS.bind(Messages.SplahScreen_VersionString, ver),
                        VERSION_LOCATION.x, VERSION_LOCATION.y, true);
            }
        });
        
        
    }

	@Override
	public void dispose() {
		super.dispose();
	}
    
    
}
