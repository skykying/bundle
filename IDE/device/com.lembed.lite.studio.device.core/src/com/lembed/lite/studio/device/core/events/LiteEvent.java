/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.core.events;

/**
 * RTE event: component selection change, active bundle change, etc.
 */
public class LiteEvent {

	// some predefined topics
	public static final String CONFIGURATION_MODIFIED  = "com.lembed.lite.studio.device.lite.config.modified"; //$NON-NLS-1$
	public static final String CONFIGURATION_COMMITED  = "com.lembed.lite.studio.device.lite.config.commited"; //$NON-NLS-1$
	
	public static final String CONFIG_INFO_REQUEST  = "com.lembed.lite.studio.device.lite.config.request"; //$NON-NLS-1$
	public static final String CONFIG_INFO_RESPONSE  = "com.lembed.lite.studio.device.lite.config.response"; //$NON-NLS-1$

	public static final String FILTER_MODIFIED 	= "com.lembed.lite.studio.device.lite.filter.modified"; //$NON-NLS-1$

	public static final String COMPONENT_SELECTION_MODIFIED = "com.lembed.lite.studio.device.lite.component.modified"; //$NON-NLS-1$
	public static final String COMPONENT_SHOW 	= "com.lembed.lite.studio.device.lite.component.show"; //$NON-NLS-1$

	public static final String PACKS_RELOADED 	= "com.lembed.lite.studio.device.reloaded"; //$NON-NLS-1$ - all packs reloaded
	public static final String PACKS_UPDATED  	= "com.lembed.lite.studio.device.updated"; //$NON-NLS-1$  - some packs changed (added, removed)
	public static final String PACK_JOB 		= "com.lembed.lite.studio.device.job"; //$NON-NLS-1$ - prefix for job finished
	public static final String PACK_JOB_RELOAD		 			= PACK_JOB + ".reload"; //$NON-NLS-1$ // job finished and requests reload
	public static final String PACK_INSTALL_JOB_FINISHED 		= PACK_JOB + ".installed"; //$NON-NLS-1$
	public static final String PACK_IMPORT_FOLDER_JOB_FINISHED	= PACK_JOB + ".impolited"; //$NON-NLS-1$
	public static final String PACK_REMOVE_JOB_FINISHED 		= PACK_JOB + ".removed"; //$NON-NLS-1$
	public static final String PACK_DELETE_JOB_FINISHED 		= PACK_JOB + ".deleted"; //$NON-NLS-1$

	public static final String PRINT		= "com.lembed.lite.studio.device.print"; //$NON-NLS-1$
	public static final String PRINT_OUTPUT	= PRINT + ".output"; //$NON-NLS-1$
	public static final String PRINT_INFO	= PRINT + ".info"; //$NON-NLS-1$
	public static final String PRINT_WARNING= PRINT + ".warning"; //$NON-NLS-1$
	public static final String PRINT_ERROR	= PRINT + ".error"; //$NON-NLS-1$

	public static final String PACK_OLNLINE_STATE_CHANGED 	= "com.lembed.lite.studio.device.online"; //$NON-NLS-1$

	public static final String DEVICE_TRIGGER_SELECT  = "com.lembed.lite.studio.device.lite.device.select"; //$NON-NLS-1$

	public static final String PROJECT_ADDED  	= "com.lembed.lite.studio.device.lite.project.added"; //$NON-NLS-1$
	public static final String PROJECT_REMOVED  = "com.lembed.lite.studio.device.lite.project.removed"; //$NON-NLS-1$
	public static final String PROJECT_UPDATED  = "com.lembed.lite.studio.device.lite.project.updated"; //$NON-NLS-1$

	public static final String PRE_IMPORT   = "com.lembed.lite.studio.device.lite.project.pre_import"; //$NON-NLS-1$
	public static final String POST_IMPORT  = "com.lembed.lite.studio.device.lite.project.post_import"; //$NON-NLS-1$

	// gpdsc file with given name is changed : created, deleted, modified
	public static final String GPDSC_CHANGED = "com.lembed.lite.studio.device.gpdsc.changed"; //$NON-NLS-1$
	public static final String GPDSC_LAUNCH_ERROR = "com.lembed.lite.studio.device.gpdsc.launch.error"; //$NON-NLS-1$


	protected String topic = null;
	protected Object data  = null;


	/**
	 * Constructor with topic only
	 * @param topic event topic
	 */
	public LiteEvent(final String topic) {
		this.topic = topic;
	}

	/**
	 * Constructor with topic and data
	 * @param topic event topic
	 * @param data event data
	 */
	public LiteEvent(final String topic, final Object data) {
		this.topic = topic;
		this.data = data;
	}

	/**
	 * Returns event topic
	 * @return event topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Returns event data
	 * @return event data
	 */
	public Object getData() {
		return data;
	}

}
