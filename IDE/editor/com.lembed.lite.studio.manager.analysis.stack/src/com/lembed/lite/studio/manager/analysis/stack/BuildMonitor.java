package com.lembed.lite.studio.manager.analysis.stack;

import java.time.LocalTime;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;


@SuppressWarnings("javadoc")
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
				log(event.getDelay() + " done >>>>>>>>>>>> "); //$NON-NLS-1$
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
				System.gc();
				log(event.getDelay() + " aboutToRun"); //$NON-NLS-1$
			}

		}
	};

	
	/**
	 * 
	 */
	public void register() {
		Job.getJobManager().addJobChangeListener(listener);
	}

	/**
	 * 
	 */
	public void deRegister() {
		Job.getJobManager().removeJobChangeListener(listener);
	}
	
	private static Boolean checkJobCodeAnalysis(IJobChangeEvent event) {
		String name = event.getJob().getName();
		//log(name);
		
		if(name.equals("Building workspace")) {//$NON-NLS-1$
			return true;
		}
		
		return false;
	}

	/**
	 * @param msg String
	 */
	public static void log(String msg) {
		LocalTime now = LocalTime.now();
		System.out.println(now.getSecond() +" | "+msg); //$NON-NLS-1$
	}
}
