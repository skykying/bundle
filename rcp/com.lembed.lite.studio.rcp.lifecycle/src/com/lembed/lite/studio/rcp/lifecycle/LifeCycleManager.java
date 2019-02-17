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
package com.lembed.lite.studio.rcp.lifecycle;

import java.util.List;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.util.tracker.ServiceTracker;


/**
 * lifeCycle:
 * http://www.vogella.com/tutorials/Eclipse4LifeCycle/article.html
 * 
 * trace:
 * http://www.eclipse.org/eclipse/platform-core/documents/3.1/perf_stats.html
 * https://www.winklerweb.net/index.php/blog/12-eclipse/10-finding-the-almost-new-eclipse-tracing-api
 * http://www.vogella.com/tutorials/EclipsePerformance/article.html#tracing_runonatruntime
 *
 * log:
 * http://blog.csdn.net/wander754085/article/details/7766767
 */
@SuppressWarnings("restriction")
public class LifeCycleManager {

	@PostContextCreate
    void postContextCreate(IApplicationContext appContext, Display display)    {
		String[] args = (String[]) appContext.getArguments().get(IApplicationContext.APPLICATION_ARGS);

		for(String arg : args) {
			log(arg);
		}
		log("@PostContextCreate");	
		
		BundleContext bundleContext = appContext.getBrandingBundle().getBundleContext();
		ServiceTracker debugTracker;
		Filter filter;
		debugTracker = new ServiceTracker(bundleContext, LifeCyclePlugin.class.getName(), null);
		debugTracker.open();
    }
	
	@ProcessAdditions
	public void processAdditions(MApplication app, EModelService modelService, Display display) {
		log("@ProcessAdditions");	
		
		List<MPerspective> perspectives = modelService.findElements(app,
				null, MPerspective.class, null);
		for(MPerspective ps : perspectives) {
			log(ps.getLabel());
			
		}
		
		
		ILog log = LifeCyclePlugin.getDefault().getLog();
		log.addLogListener(new ILogListener() {

			@Override
			public void logging(IStatus status, String plugin) {
			
			}
			
		});
	}
	
	@ProcessRemovals
    void processRemovals(IApplicationContext appContext, Display display)    {
		log("@ProcessRemovals");		
    }
	
	@PreSave
	public void preSave(MApplication application, EModelService modelService,
			EPartService partService) {
		log("@PreSave");	
	}
	
	
	private void log(String msg) {
		System.out.println(LifeCycleManager.class.getSimpleName()+"  " +msg);
	}
}
