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
package com.lembed.lite.studio.manager.analysis.editor.linker.formatter;

import org.eclipse.jface.preference.IPreferenceStore;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;

import com.lembed.lite.studio.manager.analysis.editor.PreferenceConstants;


public class FormatterUtils {
    
    public static LinkerFileFormatter preferencesToFormatter(IPreferenceStore prefs){
        
        LinkerFileFormatter.Builder builder = new LinkerFileFormatter.Builder();
        
        builder.indent(prefs.getInt(PreferenceConstants.SPACES_PER_TAB));
        builder.lineLength(prefs.getInt(PreferenceConstants.FORMATTER_LINE_WIDTH));
        
        String flowStyle = prefs.getString(PreferenceConstants.FORMATTER_FLOW_STYLE);
        builder.flowStyle(FlowStyle.valueOf(flowStyle));
        builder.prettyFlow(prefs.getBoolean(PreferenceConstants.FORMATTER_PRETTY_FLOW));
        
        String scalarStyle = prefs.getString(PreferenceConstants.FORMATTER_SCALAR_STYLE);
        builder.scalarStyle(ScalarStyle.valueOf(scalarStyle));
        
        builder.explicitStart(prefs.getBoolean(PreferenceConstants.FORMATTER_EXPLICIT_START));
        builder.explicitEnd(prefs.getBoolean(PreferenceConstants.FORMATTER_EXPLICIT_END));
        
        return builder.build();
    }
    

}
