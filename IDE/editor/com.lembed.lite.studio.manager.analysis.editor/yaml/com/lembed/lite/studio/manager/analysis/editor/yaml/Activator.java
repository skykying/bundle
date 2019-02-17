package com.lembed.lite.studio.manager.analysis.editor.yaml;

import java.io.IOException;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.persistence.TemplateStore;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.lembed.lite.studio.manager.analysis.editor.log.EditorLog;
import com.lembed.lite.studio.manager.analysis.editor.yaml.template.YAMLContentType;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.lembed.lite.studio.manager.analysis.editor.elf"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
private TemplateStore templateStore;
public static final String TEMPLATE_STORE_ID = PLUGIN_ID + ".template";
	
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
		plugin = null;
		super.stop(context);
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
            contextTypeRegistry.addContextType(YAMLContentType.YAML_CONTENT_TYPE);
        }
        return contextTypeRegistry;
    }	
}
