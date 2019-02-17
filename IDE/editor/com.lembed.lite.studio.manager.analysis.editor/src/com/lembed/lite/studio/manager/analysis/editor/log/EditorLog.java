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
package com.lembed.lite.studio.manager.analysis.editor.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;

import com.lembed.lite.studio.manager.analysis.editor.BaseEditorPlugin;

/**
 * Utility class for logging exceptions and for tracing plugin
 * execution.
 * 
 * @author oysteto
 *
 */
public class EditorLog {
	
	public static Logger logger;
	
	//the trace level that is used if tracing/debugging is enabled and trace level not specified
	public static String DEFAULT_TRACE_LEVEL = "INFO";
	
	public static String TRACE_LEVEL_OPTION = BaseEditorPlugin.PLUGIN_ID + "/debug/trace_level";
	
	public static HashMap<String,Level> VALID_LEVELS = new HashMap<String,Level>();
	
	static {
		VALID_LEVELS.put("FINEST", Level.FINEST );
		VALID_LEVELS.put("FINER", Level.FINER );
		VALID_LEVELS.put("FINE", Level.FINE );
		VALID_LEVELS.put("CONFIG", Level.CONFIG );
		VALID_LEVELS.put("INFO", Level.INFO );
		VALID_LEVELS.put("WARNING", Level.WARNING );
		VALID_LEVELS.put("SEVERE", Level.SEVERE );
	}
		
	/**
	 * Initialize the trace logger.
	 * 
	 * The trace logger can be configured by changing the tracing options
	 * when running Eclipse.
	 */
	public static void initializeTraceLogger() {

		logger = Logger.getLogger(BaseEditorPlugin.PLUGIN_ID);

		//when not running in debug mode do not perform any tracing. Errors will still
		//be logged with the standard Eclipse error logging mechanism.
		if( !BaseEditorPlugin.getDefault().isDebugging() ) {
			logger.setLevel(Level.OFF);
			return;
		}
			
		String traceLevelString = Platform.getDebugOption(TRACE_LEVEL_OPTION);
		if( null == traceLevelString ){
			traceLevelString = DEFAULT_TRACE_LEVEL;
		}
		traceLevelString = traceLevelString.toUpperCase();
		
		Level traceLevel;
		if( !VALID_LEVELS.containsKey(traceLevelString) ){
			logError( "The traceLevel '" + traceLevelString + "' is not valid. Trace level set to SEVERE" );
			traceLevel = Level.SEVERE;
		} else {
			traceLevel = VALID_LEVELS.get( traceLevelString );
            logger.info("Setting trace level to " + traceLevel);			
		}	
		
		logger.setLevel(traceLevel);
	}
	
	public static void logException( Throwable exception ){
		logException( exception, "Unexpected exception: " );
	}
	
	public static void logException( Throwable exception, String message ){
		
		IStatus status = new Status( IStatus.ERROR, BaseEditorPlugin.PLUGIN_ID, IStatus.OK, message, exception );		
		BaseEditorPlugin.getDefault().getLog().log(status);
		
	}
	
	public static void logError( String message ){
		
		IStatus status = new Status( IStatus.ERROR, BaseEditorPlugin.PLUGIN_ID, message );		
		BaseEditorPlugin.getDefault().getLog().log(status);
				
	}

	public static void logError(String string, BadLocationException e) {
		// TODO Auto-generated method stub
		
	}

	public static void logError(String string, Exception e) {
		// TODO Auto-generated method stub
		
	}
	

}
