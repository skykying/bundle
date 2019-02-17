/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.manager.analysis.editor.yaml.template;

import org.eclipse.jface.text.templates.Template;

/**
 * Implementation of templates in YEdit. One difference from normal Eclipse
 * templates is in the way that templates are matched against a prefix.
 */
public class YEditTemplate extends Template {

    /**
     * Copy a Template and create a YEditTemplate
     * 
     * @param template The template to copy
     */
    YEditTemplate(Template template) {
        super(template);
    }

    /**
     * @param prefix The prefix to match against.
     * @param contextTypeId The current context type id to match against.
     * @return Returns true if the the contextTypeId matches the templates
     *         context type and the prefix matches the template name.
     */
    public boolean matches(String prefix, String contextTypeId) {
        
        if (!super.matches(prefix, contextTypeId)) {
            return false;
        }

        String templateName = getName().toLowerCase();
        if (templateName.startsWith(prefix.toLowerCase())) {
            return true;
        }

        return false;

    }
}
