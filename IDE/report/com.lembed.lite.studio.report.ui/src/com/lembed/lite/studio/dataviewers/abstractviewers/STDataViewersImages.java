/*******************************************************************************
 * Copyright (c) 2009 STMicroelectronics.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marzia Maugeri <marzia.maugeri@st.com> - initial API and implementation
 *******************************************************************************/
package com.lembed.lite.studio.dataviewers.abstractviewers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.dataviewers.STDataViewersActivator;

/**
 * The Class STDataViewersImages.
 */
/*
 * This class handle the registration of all images used into org.eclipse.linuxtools.dataviewers
 */
public class STDataViewersImages {
    private static ImageRegistry imageRegistry;

    /**
     * A table of all the <code>ImageDescriptor</code>s.
     */
    private static HashMap<String, ImageDescriptor> imageDescriptors;

    /* Declare Common paths */
    private static URL ICON_BASE_URL = null;

    static {
        String pathSuffix = "icons/"; //$NON-NLS-1$

        STDataViewersActivator activator = STDataViewersActivator.getDefault();
        ICON_BASE_URL = activator.getBundle().getEntry(pathSuffix);
        imageRegistry = activator.getImageRegistry();
        if (imageRegistry == null) {
            imageRegistry = new ImageRegistry(PlatformUI.getWorkbench().getDisplay());
        }
    }

    /** The Constant IMG_EXPORT. */
    public static final String IMG_EXPORT = "export.gif"; //$NON-NLS-1$
    
    /** The Constant IMG_EDIT_PROPERTIES. */
    public static final String IMG_EDIT_PROPERTIES = "prop_edt.gif"; //$NON-NLS-1$

    /** The Constant IMG_EXPANDALL. */
    public static final String IMG_EXPANDALL = "expand_all.gif"; //$NON-NLS-1$
    
    /** The Constant IMG_COLLAPSEALL. */
    public static final String IMG_COLLAPSEALL = "collapse_all.gif"; //$NON-NLS-1$

    /** The Constant IMG_SORT. */
    public static final String IMG_SORT = "sort.gif"; //$NON-NLS-1$

    /**
     * Declare all images
     */
    private static void declareImages() {
        declareRegistryImage(IMG_EXPORT, "export.gif"); //$NON-NLS-1$
        declareRegistryImage(IMG_EDIT_PROPERTIES, "prop_edt.gif"); //$NON-NLS-1$

        declareRegistryImage(ISharedImages.IMG_OBJS_INFO_TSK, "info_obj.gif"); //$NON-NLS-1$
        declareRegistryImage(ISharedImages.IMG_TOOL_FORWARD, "forward_nav.gif"); //$NON-NLS-1$
        declareRegistryImage(ISharedImages.IMG_TOOL_BACK, "backward_nav.gif"); //$NON-NLS-1$

        declareRegistryImage(IMG_EXPANDALL, "expand_all.gif"); //$NON-NLS-1$
        declareRegistryImage(IMG_COLLAPSEALL, "collapse_all.gif"); //$NON-NLS-1$

        declareRegistryImage(IMG_SORT, "sort.gif"); //$NON-NLS-1$
    }

    /**
     * Declare an Image in the registry table.
     *
     * @param key
     *            The key to use when registering the image
     * @param path
     *            The path where the image can be found. This path is relative to where this plugin class is found (i.e.
     *            typically the packages directory)
     */
    private static void declareRegistryImage(String key, URL path) {
        ImageDescriptor desc = ImageDescriptor.getMissingImageDescriptor();
        desc = ImageDescriptor.createFromURL(path);
        imageRegistry.put(key, desc);
        imageDescriptors.put(key, desc);
    }

    /**
     * Declare an Image in the registry table.
     *
     * @param key
     *            The key to use when registering the image
     * @param path
     *            The path where the image can be found. This path is relative to where this plugin class is found (i.e.
     *            typically the packages directory)
     */
    private static void declareRegistryImage(String key, String path) {
        try {
            URL url = makeIconFileURL(path);
            declareRegistryImage(key, url);
        } catch (MalformedURLException me) {
        }

    }

    /**
     * Initialize the image registry by declaring all of the required graphics. This involves creating JFace image
     * descriptors describing how to create/find the image should it be needed. The image is not actually allocated
     * until requested.
     *
     * Prefix conventions Wizard Banners WIZBAN_ Preference Banners PREF_BAN_ Property Page Banners PROPBAN_ Color
     * toolbar CTOOL_ Enable toolbar ETOOL_ Disable toolbar DTOOL_ Local enabled toolbar ELCL_ Local Disable toolbar
     * DLCL_ Object large OBJL_ Object small OBJS_ View VIEW_ Product images PROD_ Misc images MISC_
     *
     * Where are the images? The images (typically gifs) are found in the same location as this plugin class. This may
     * mean the same package directory as the package holding this class. The images are declared using this.getClass()
     * to ensure they are looked up via this plugin class.
     *
     * @return The newly initialized ImageRegistry.
     *
     * @see org.eclipse.jface.resource.ImageRegistry
     */
    public static ImageRegistry initializeImageRegistry() {
        imageDescriptors = new HashMap<>(30);
        declareImages();
        return imageRegistry;
    }

    /**
     * Returns the <code>ImageDescriptor</code> identified by the given key, or <code>null</code> if it does not exist.
     *
     * @param key The name of the image looked.
     * @return The ImageDescriptor if found, null otherwise.
     */
    public static ImageDescriptor getImageDescriptor(String key) {
        if (imageDescriptors == null) {
            initializeImageRegistry();
        }
        return imageDescriptors.get(key);
    }

    private static URL makeIconFileURL(String iconPath) throws MalformedURLException {
        if (ICON_BASE_URL == null) {
            throw new MalformedURLException();
        }

        return new URL(ICON_BASE_URL, iconPath);
    }
}
