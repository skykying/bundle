package com.lembed.lite.studio.manager.analysis.editor.linker;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.lembed.lite.studio.manager.analysis.editor.linker.template.LinkerFileContentType;
import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.manager.analysis.editor.linker"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	private TemplateStore templateStore;
	public static final String TEMPLATE_STORE_ID = PLUGIN_ID + ".template";
	

	private static HashMap<String, Image> images = new HashMap<String, Image>();
	private static HashMap<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>();
	
		
		private ContributionContextTypeRegistry contextTypeRegistry;
	/**
	 * The constructor 
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		Collection<Image> values = images.values();
		for (Image image : values) {
			image.dispose();
		}
		images.clear();
		imageDescriptors.clear();
		
		plugin = null;
		super.stop(context);
	}

    
	/**
	 * @param file
	 *            name of the image
	 * @return an Image object
	 */
	public static Image getImage(String file) {
		String icons_file = null;
		if (!file.startsWith(ICONS_PATH)) {
			icons_file = ICONS_PATH + file;
		} else {
			icons_file = file;
		}

		Image image = null;
		if (images.containsKey(icons_file)) {
			image = images.get(icons_file);
		} else {
			image = createImage(icons_file);
			images.put(icons_file, image);
		}

		return image;
	}
	

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getIconDescriptor(String file) {
		String icons_file = null;
		if (!file.startsWith(ICONS_PATH)) {
			icons_file = ICONS_PATH + file;
		} else {
			icons_file = file;
		}

		if (imageDescriptors.containsKey(icons_file)) {
			return imageDescriptors.get(icons_file);
		}

		ImageDescriptor imageDescr = imageDescriptorFromPlugin(PLUGIN_ID, icons_file);
		if (imageDescr != null) {
			imageDescriptors.put(icons_file, imageDescr);
		}

		return imageDescr;
	}
	
	/**
	 * Helper method to load an image
	 * 
	 * @param file
	 *            name of the image
	 * @return Image object
	 */
	private static Image createImage(String file) {
		ImageDescriptor image = getIconDescriptor(file);
		if (image != null) {
			return image.createImage();
		}
		return null;

	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	public TemplateStore getTemplateStore() {
        
	    if (templateStore == null) {
            templateStore = new ContributionTemplateStore(getContextTypeRegistry(), getDefault().getPreferenceStore(), TEMPLATE_STORE_ID);
            try {
                templateStore.load();
            } catch (IOException e) {
            	EditorLog.logException(e);
            }
        }
        return templateStore;	    
	}
	
    public ContextTypeRegistry getContextTypeRegistry() {
        if (contextTypeRegistry == null) {
            contextTypeRegistry = new ContributionContextTypeRegistry();
            contextTypeRegistry.addContextType(LinkerFileContentType.LINKERFILE_CONTENT_TYPE);
        }
        return contextTypeRegistry;
    }	
    
    
    public static final String ICONS_PATH = "icons/"; //$NON-NLS-1$
	// icons

	public static final String ICON_ITEM = "item.png"; //$NON-NLS-1$
	public static final String ICON_BOOK = "book.png"; //$NON-NLS-1$
	public static final String ICON_FILE = "file.gif"; //$NON-NLS-1$
	public static final String ICON_FOLDER = "folder.gif"; //$NON-NLS-1$
	public static final String ICON_FOLDER_DEVICES = "folderDevices.gif"; //$NON-NLS-1$

	public static final String ICON_CHECKED = "checked.gif"; //$NON-NLS-1$
	public static final String ICON_UNCHECKED = "unchecked.gif"; //$NON-NLS-1$

	public static final String ICON_CHECKED_GREY = "checkedGrey.gif"; //$NON-NLS-1$
	public static final String ICON_UNCHECKED_GREY = "uncheckedGrey.gif"; //$NON-NLS-1$

	public static final String ICON_ROUND_CHECK = "roundCheck.png"; //$NON-NLS-1$
	public static final String ICON_ROUND_CHECK_GREY = "roundCheckGrey.png"; //$NON-NLS-1$

	public static final String ICON_RESOLVE_CHECK = "resolveCheck.png"; //$NON-NLS-1$
	public static final String ICON_RESOLVE_CHECK_GREY = "resolveCheckGrey.png"; //$NON-NLS-1$
	public static final String ICON_RESOLVE_CHECK_WARN = "resolveCheckWarn.png"; //$NON-NLS-1$

	public static final String ICON_DETAILS = "details.gif"; //$NON-NLS-1$
	public static final String ICON_HELP = "help_contents.gif"; //$NON-NLS-1$
	public static final String ICON_INFO = "info.gif"; //$NON-NLS-1$
	public static final String ICON_WARNING = "warning.gif"; //$NON-NLS-1$
	public static final String ICON_ERROR = "error.gif"; //$NON-NLS-1$

	public static final String ICON_RTEFILTER = "rteFilter.gif"; //$NON-NLS-1$
	public static final String ICON_LITE = "lite.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_GREY = "rteGrey.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_TRANSPARENT = "rteTransparent.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_INSTALL = "rteGrey.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_WARNING = "rteWarning.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_ERROR = "rteError.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_UNPACK = "rteUnpack.png"; //$NON-NLS-1$
	public static final String ICON_RTE_SUB_WARNING = "rteSubWarning.png"; //$NON-NLS-1$

	public static final String ICON_LITE_OVR = "lite_ovr.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_WARNING_OVR = "rteWarning_ovr.gif"; //$NON-NLS-1$
	public static final String ICON_RTE_ERROR_OVR = "rteError_ovr.gif"; //$NON-NLS-1$

	public static final String ICON_RTE_CONSOLE = "rteConsole.gif"; //$NON-NLS-1$

	public static final String ICON_NEW_CMSIS_TEMPLATE = "new_cmsis_templ.png"; //$NON-NLS-1$

	public static final String ICON_COMPONENT_CLASS = "componentClass.gif"; //$NON-NLS-1$
	public static final String ICON_COMPONENT_GROUP = "componentGroup.png"; //$NON-NLS-1$
	public static final String ICON_COMPONENT = "component.gif"; //$NON-NLS-1$
	public static final String ICON_COMPONENT_WARNING = "componentWarning.gif"; //$NON-NLS-1$
	public static final String ICON_COMPONENT_ERROR = "componentError.gif"; //$NON-NLS-1$
	public static final String ICON_MULTICOMPONENT = "multiComponent.gif"; //$NON-NLS-1$
	public static final String ICON_MULTICOMPONENT_WARNING = "multiComponentWarning.gif"; //$NON-NLS-1$
	public static final String ICON_MULTICOMPONENT_ERROR = "multiComponentError.gif"; //$NON-NLS-1$

	public static final String ICON_PACKINSTALLER = "packInstaller.gif"; //$NON-NLS-1$
	public static final String ICON_CHECK4UPDATE = "check4Update.gif"; //$NON-NLS-1$
	public static final String ICON_REFRESH = "refresh_nav.gif"; //$NON-NLS-1$
	public static final String ICON_IMPORT_FOLDER = "import_folder.gif"; //$NON-NLS-1$

	public static final String ICON_EXAMPLE = "cmsisExample.png"; //$NON-NLS-1$

	public static final String ICON_GEAR_WHEEL = "gear_wheel.gif"; //$NON-NLS-1$
	public static final String ICON_GEAR_WHEELS = "gear_wheels.gif"; //$NON-NLS-1$

	public static final String ICON_CHIP = "chip.png"; //$NON-NLS-1$
	public static final String ICON_CHIP_GREY = "chip_grey.png"; //$NON-NLS-1$
	public static final String ICON_CHIP_32 = "chip32.png"; //$NON-NLS-1$
	public static final String ICON_CHIP_48 = "chip48.png"; //$NON-NLS-1$

	public static final String ICON_DEVICE = ICON_CHIP;
	public static final String ICON_DEPRDEVICE = ICON_CHIP_GREY;

	public static final String ICON_DEVICE_32 = "device32.png"; //$NON-NLS-1$
	public static final String ICON_DEVICE_48 = "device48.png"; //$NON-NLS-1$

	public static final String ICON_BOARD = "board.png"; //$NON-NLS-1$
	public static final String ICON_BOARD_GREY = "boardGrey.png"; //$NON-NLS-1$

	public static final String ICON_RUN = "run.gif"; //$NON-NLS-1$
	public static final String ICON_RUN_GREY = "runGrey.gif"; //$NON-NLS-1$

	public static final String ICON_PACKAGE = "package.png"; //$NON-NLS-1$
	public static final String ICON_PACKAGE_EMPTY = "packageEmpty.png"; //$NON-NLS-1$
	public static final String ICON_PACKAGE_GREY = "packageGrey.png"; //$NON-NLS-1$
	public static final String ICON_PACKAGE_RED = "packageRed.png"; //$NON-NLS-1$

	public static final String ICON_PACKAGES = "packages.png"; //$NON-NLS-1$
	public static final String ICON_PACKAGES_EMPTY = "packagesEmpty.png"; //$NON-NLS-1$
	public static final String ICON_PACKAGES_GREY = "packagesGrey.png"; //$NON-NLS-1$
	public static final String ICON_PACKAGES_RED = "packagesRed.png"; //$NON-NLS-1$

	public static final String ICON_PACKAGES_FILTER = "packagesFilter.png"; //$NON-NLS-1$

	public static final String ICON_EXPAND_ALL = "expandall.gif"; //$NON-NLS-1$
	public static final String ICON_COLLAPSE_ALL = "collapseall.gif"; //$NON-NLS-1$
	public static final String ICON_REMOVE_ALL = "removeall.png"; //$NON-NLS-1$

	public static final String ICON_PIN = "pin.png"; //$NON-NLS-1$
	public static final String ICON_DOWNLOAD = "download.png"; //$NON-NLS-1$
	public static final String ERROR_OVR = "error_ovr.gif"; //$NON-NLS-1$
	public static final String WARN_OVR = "warn_ovr.gif"; //$NON-NLS-1$
	public static final String ASSUME_VALID_OVR = "assume_valid_ovr.gif"; //$NON-NLS-1$
	public static final String CHECKEDOUT_OVR = "checkedout_ovr.gif"; //$NON-NLS-1$
	public static final String MODIFIED_OVR = "modified_ovr.gif"; //$NON-NLS-1$

	public static final String NEWFILE_WIZARD = "newfile_wiz.png"; //$NON-NLS-1$

	public static final RGB GREEN = new RGB(189, 249, 181);
	public static final RGB YELLOW = new RGB(252, 200, 46);
}
