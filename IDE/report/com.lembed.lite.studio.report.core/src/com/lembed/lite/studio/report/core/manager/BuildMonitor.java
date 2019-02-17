package com.lembed.lite.studio.report.core.manager;

import java.time.LocalTime;
import java.util.HashMap;

import org.eclipse.cdt.codan.core.model.ICodanProblemMarker;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;

/**
 * The Class BuildMonitor.
 */
public class BuildMonitor {
	
	
	private IJobChangeListener listener =  new IJobChangeListener() {

		@Override
		public void sleeping(IJobChangeEvent event) {
			if (checkJobCodeAnalysis(event)) {

				log(event.getDelay() + " sleeping"); //$NON-NLS-1$
			}
		}

		@Override
		public void scheduled(IJobChangeEvent event) {
			if (checkJobCodeAnalysis(event)) {				
				log(event.getDelay() + " scheduled"); //$NON-NLS-1$
			}
		}

		@Override
		public void running(IJobChangeEvent event) {
			if (checkJobCodeAnalysis(event)) {
				log(event.getDelay() + " running"); //$NON-NLS-1$
			}
		}

		@Override
		public void done(IJobChangeEvent event) {
			if (checkJobCodeAnalysis(event)) {
				BuildManager manager = BuildManager.getInstance();
				HashMap<Long, ICodanProblemMarker> cache = manager.getProblemCache();
				log(event.getDelay() + " done >>>>>>>>>>>> " + cache.size()); //$NON-NLS-1$
			}
		}

		@Override
		public void awake(IJobChangeEvent event) {
			if (checkJobCodeAnalysis(event)) {
				log(event.getDelay() + " awake"); //$NON-NLS-1$
			}
		}

		@Override
        public void aboutToRun(IJobChangeEvent event) {
			if (checkJobCodeAnalysis(event)) {
				BuildManager manager = BuildManager.getInstance();
				manager.clearCache();
				System.gc();
				log(event.getDelay() + " aboutToRun"); //$NON-NLS-1$
			}

		}
	};

	
	@SuppressWarnings("javadoc")
    public void register() {
		Job.getJobManager().addJobChangeListener(listener);
	}

	@SuppressWarnings("javadoc")
    public void deRegister() {
		Job.getJobManager().removeJobChangeListener(listener);
	}
	
	private static Boolean checkJobCodeAnalysis(IJobChangeEvent event) {
		String name = event.getJob().getName();
	
		if(name.equals("Building workspace")) {//NON-NLS-1$ //$NON-NLS-1$
			return true;
		}
        return false;
	}

	@SuppressWarnings("javadoc")
    public static void log(String msg) {
		LocalTime now = LocalTime.now();
		System.out.println(now.getSecond() +" | "+msg); //$NON-NLS-1$
	}
}
