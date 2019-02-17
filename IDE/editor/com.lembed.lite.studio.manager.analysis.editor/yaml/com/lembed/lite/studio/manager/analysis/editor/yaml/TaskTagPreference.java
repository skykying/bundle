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
package com.lembed.lite.studio.manager.analysis.editor.yaml;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;

import com.lembed.lite.studio.manager.analysis.editor.yaml.preferences.PreferenceConstants;

public class TaskTagPreference {
    public String tag;
    public String severity;
    
    public TaskTagPreference(){
    	
    }
    
    public TaskTagPreference(String tag, String severity){
        this.tag = tag;
        this.severity = severity;
    }
    
    
    public static List<TaskTagPreference> getTaskTagPreferences(IPreferenceStore prefStore){
    	
        String currTags = prefStore.getString(PreferenceConstants.TODO_TASK_TAGS);    
        String currPrios = prefStore.getString(PreferenceConstants.TODO_TASK_PRIORITIES);
        String[] tags = getTokens(currTags, ","); //$NON-NLS-1$
        String[] prios = getTokens(currPrios, ","); //$NON-NLS-1$
        List<TaskTagPreference> elements = new ArrayList<>(tags.length);
        for (int i = 0; i < tags.length; i++) {
            String tag = tags[i].trim();
            String severity =(i < prios.length) ? prios[i] : PreferenceConstants.TASK_PRIORITY_NORMAL;
            elements.add(new TaskTagPreference(tag, severity));
        }  
        
        return elements;
    	
    }
    
    private static String[] getTokens(String text, String separator) {
        StringTokenizer tok= new StringTokenizer(text, separator); 
        int nTokens= tok.countTokens();
        String[] res= new String[nTokens];
        for (int i= 0; i < res.length; i++) {
            res[i]= tok.nextToken().trim();
        }
        return res;
    }    
    
}
