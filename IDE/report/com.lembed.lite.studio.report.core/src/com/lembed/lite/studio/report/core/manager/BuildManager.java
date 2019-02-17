package com.lembed.lite.studio.report.core.manager;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.cdt.codan.core.model.ICodanProblemMarker;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.report.core.views.BuildAnalyzerView;


/**
 * The Class BuildManager.
 */
public class BuildManager {

	/** The problem cache. */
	public volatile  HashMap<Long,ICodanProblemMarker> problemCache = new HashMap<>();

	private  static BuildManager manager;
	private Lock lock = new ReentrantLock();
	
	/**
	 * Gets the single instance of BuildManager.
	 *
	 * @return single instance of BuildManager
	 */
	public static BuildManager getInstance() {
		if(manager == null) {
			manager = new BuildManager();
		}
		
		return manager;
	}
	
	/**
	 * @return the problemCache
	 */
	public  HashMap<Long, ICodanProblemMarker> getProblemCache() {
		return problemCache;
	}
	
	/**
	 * Clear cache.
	 */
	public  void clearCache() {
		lock.lock();
		problemCache.clear();
		lock.unlock();
	}
	
	/**
	 * Adds the problem.
	 *
	 * @param millis the millis
	 * @param pm the pm
	 */
	public  void addProblem(long millis,ICodanProblemMarker pm) {
		lock.lock();
		problemCache.put(millis, pm);
//		log(problemCache.size()+"");
		lock.unlock();
	}
	
	/**
	 * Show report view.
	 */
	public static void showReportView() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
            public void run() {

				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					page.showView(BuildAnalyzerView.VIEW_ID);
				} catch (PartInitException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	
}
