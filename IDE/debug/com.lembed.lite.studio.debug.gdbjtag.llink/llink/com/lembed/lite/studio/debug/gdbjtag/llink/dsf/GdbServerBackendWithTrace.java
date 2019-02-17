/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.debug.gdbjtag.llink.dsf;

import com.lembed.lite.studio.debug.gdbjtag.llink.Configuration;
import com.lembed.lite.studio.debug.gdbjtag.llink.ConfigurationAttributes;
import com.lembed.lite.studio.debug.gdbjtag.llink.DefaultPreferences;
import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;
import com.lembed.lite.studio.debug.gdbjtag.llink.dsf.process.TraceProcess;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.eclipse.cdt.dsf.concurrent.DsfRunnable;
import org.eclipse.cdt.dsf.concurrent.IDsfStatusConstants;
import org.eclipse.cdt.dsf.concurrent.ImmediateRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.Sequence;
import org.eclipse.cdt.dsf.gdb.service.command.GDBControl.InitializationShutdownStep;
import org.eclipse.cdt.dsf.mi.service.IMIBackend.State;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunchConfiguration;

/**
 * The Class GdbServerBackendWithTrace.
 */
public class GdbServerBackendWithTrace extends GdbServerBackend {

	// ------------------------------------------------------------------------

	// Allow derived classes to use these variables.

	/** The do start trace console. */
	protected boolean fDoStartTraceConsole = false;
	
	/** The trace process. */
	protected Process fTraceProcess;
	
	/** The trace monitor job. */
	protected TraceMonitorJob fTraceMonitorJob;

	/** For synchronisation reasons, set/check this only on the DSF thread. */
	protected State fTraceBackendState = State.NOT_INITIALIZED;
	
	/** The trace exit value. */
	protected int fTraceExitValue = 0;
	

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gdb server backend with trace.
	 *
	 * @param session the session
	 * @param lc the lc
	 */
	public GdbServerBackendWithTrace(DsfSession session, ILaunchConfiguration lc) {
		super(session, lc);
		LlinkPlugin.log("GdbServerBackendWithTrace(" + session + "," + lc.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	// ------------------------------------------------------------------------

	@Override
	public void initialize(final RequestMonitor rm) {

		LlinkPlugin.log("GdbServerBackendWithTrace.initialize()"); //$NON-NLS-1$
		try {
			// Update parent data member before calling initialize.
			fDoStartGdbServer = Configuration.getDoStartGdbServer(fLaunchConfiguration);
			fDoStartTraceConsole = Configuration.getDoAddSemihostingConsole(fLaunchConfiguration);
		} catch (CoreException e) {
			rm.setStatus(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, -1, "Cannot get configuration", e)); //$NON-NLS-1$
			rm.done();
			return;
		}

		// Initialise the super class, and, when ready, perform the local
		// initialisations.
		super.initialize(new RequestMonitor(getExecutor(), rm) {
			@Override
            protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	private void doInitialize(RequestMonitor rm) {

		LlinkPlugin.log("GdbServerBackendWithTrace.doInitialize()"); //$NON-NLS-1$

		if (fDoStartGdbServer && fDoStartTraceConsole) {

			final Sequence.Step[] initializeSteps = new Sequence.Step[] {
			    new TraceStep(InitializationShutdownStep.Direction.INITIALIZING),
			    new TraceMonitorStep(InitializationShutdownStep.Direction.INITIALIZING),
			};

			Sequence startupSequence = new Sequence(getExecutor(), rm) {
				@Override
				public Step[] getSteps() {
					return initializeSteps;
				}
			};
			getExecutor().execute(startupSequence);

		} else {
			rm.done();
		}
	}

	@Override
	public void shutdown(final RequestMonitor rm) {

		LlinkPlugin.log("GdbServerBackendWithTrace.shutdown()"); //$NON-NLS-1$

		if (fDoStartGdbServer && fDoStartTraceConsole) {
			final Sequence.Step[] shutdownSteps = new Sequence.Step[] {
			    new TraceMonitorStep(InitializationShutdownStep.Direction.SHUTTING_DOWN),
			    new TraceStep(InitializationShutdownStep.Direction.SHUTTING_DOWN),
			};
			Sequence startupSequence = new Sequence(getExecutor(), new ImmediateRequestMonitor(rm) {
				@Override
				protected void handleSuccess() {
					// We're done here, shutdown parent.
					GdbServerBackendWithTrace.super.shutdown(rm);
				}
			}) {
				@Override
				public Step[] getSteps() {
					return shutdownSteps;
				}
			};
			getExecutor().execute(startupSequence);

		} else {
			super.shutdown(rm);
		}
	}

	@Override
	public void destroy() {

		LlinkPlugin.log("GdbServerBackendWithTrace.destroy() " + Thread.currentThread()); //$NON-NLS-1$

		destroyTrace();

		// Destroy the parent (the GDB server; the client is also destroyed
		// there).
		super.destroy();
	}

	private void destroyTrace() {
		// Destroy the semihosting process
		if (fTraceProcess != null && fTraceBackendState == State.STARTED) {
			fTraceProcess.destroy();
		}
	}
	// ------------------------------------------------------------------------
	
	@Override
    public String getTraceName() {
		return "L Link GDB"; //$NON-NLS-1$
	}

	/**
	 * Gets the starting trace job name.
	 *
	 * @return the starting trace job name
	 */
	public String getStartingTraceJobName() {
		return "Starting " + getTraceName() + " Trace Process"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Gets the terminating trace job name.
	 *
	 * @return the terminating trace job name
	 */
	public String getTerminatingTraceJobName() {
		return "Terminating " + getTraceName() + " Trace Process"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * Gets the trace process.
	 *
	 * @return the trace process
	 */
	public Process getTraceProcess() {
		return fTraceProcess;
	}

	// ========================================================================

	/** Start/Stop the J-Link trace console */
	protected class TraceStep extends InitializationShutdownStep  {

		TraceStep(Direction direction) {
			super(direction);
		}

		@Override
		public void initialize(final RequestMonitor rm) {

			LlinkPlugin.log("TraceStep.initialise()"); //$NON-NLS-1$
			if (getGDBServerBackendState() != State.STARTED) {

				LlinkPlugin.log("TraceStep.initialise() skipped"); //$NON-NLS-1$
				// rm.cancel();
				rm.done();
				return;
			}

			class TraceLaunchMonitor {
				boolean fLaunched = false;
				boolean fTimedOut = false;
			}

			final TraceLaunchMonitor fTraceLaunchMonitor = new TraceLaunchMonitor();

			final RequestMonitor fTmpLaunchRequestMonitor = new RequestMonitor(getExecutor(), rm) {

				@Override
				protected void handleCompleted() {

					LlinkPlugin.log("TraceStep.initialise() handleCompleted()"); //$NON-NLS-1$

					if (!fTraceLaunchMonitor.fTimedOut) {
						LlinkPlugin.log("TraceStep.initialise() handleCompleted not time out"); //$NON-NLS-1$
						fTraceLaunchMonitor.fLaunched = true;
						if (!isSuccess()) {
							rm.setStatus(getStatus());
						}
						rm.done();
					}
				}
			};

			final Job startTraceJob = new Job(getStartingTraceJobName()) {
				{
					setSystem(true);
				}

				@Override
				protected IStatus run(IProgressMonitor monitor) {

					if (fTmpLaunchRequestMonitor.isCanceled()) {

						LlinkPlugin.log("startSemihostingJob run cancel"); //$NON-NLS-1$
						fTmpLaunchRequestMonitor.setStatus(new Status(IStatus.CANCEL, LlinkPlugin.PLUGIN_ID, -1,
						                                   getStartingTraceJobName() + " cancelled.", null)); //$NON-NLS-1$
						fTmpLaunchRequestMonitor.done();
						return Status.OK_STATUS;
					}

					try {
						String host = "localhost"; //$NON-NLS-1$

						int port = fLaunchConfiguration.getAttribute(
						               ConfigurationAttributes.GDB_SERVER_SWO_PORT_NUMBER,
						               DefaultPreferences.GDB_SERVER_SWO_PORT_NUMBER_DEFAULT);

						fTraceProcess = launchTraceProcess(host, port);

						// Need to do this on the executor for thread-safety
						getExecutor().submit(new DsfRunnable() {
							@Override
							public void run() {

								LlinkPlugin.log("startTraceJob run State.STARTED"); //$NON-NLS-1$
								fTraceBackendState = State.STARTED;
							}
						});
					} catch (CoreException e) {
						fTmpLaunchRequestMonitor.setStatus(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, -1, e.getMessage(), e));
						fTmpLaunchRequestMonitor.done();
						return Status.OK_STATUS;
					}

					// TODO: check if the process started properly
					// (parse input and check greeting).

					fTmpLaunchRequestMonitor.done();

					LlinkPlugin.log("startTraceJob run completed"); //$NON-NLS-1$
					return Status.OK_STATUS;
				}
			};
			startTraceJob.schedule();

			LlinkPlugin.log("TraceStep.initialise() after job schedule"); //$NON-NLS-1$

			getExecutor().schedule(new Runnable() {

				@Override
				public void run() {

					// Only process the event if we have not finished yet (hit
					// the breakpoint).
					if (!fTraceLaunchMonitor.fLaunched) {
						fTraceLaunchMonitor.fTimedOut = true;
						
						Thread jobThread = startTraceJob.getThread();
						if (jobThread != null) {

							LlinkPlugin.log("interrupt thread " + jobThread); //$NON-NLS-1$

							jobThread.interrupt();
						}
						rm.setStatus(
						    new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, DebugException.TARGET_REQUEST_FAILED,
						               getStartingTraceJobName() + " timed out.", null)); //$NON-NLS-1$
						rm.done();
					}
				}
			}, getServerLaunchTimeoutSeconds(), TimeUnit.SECONDS);

			LlinkPlugin.log("TraceStep.initialise() return"); //$NON-NLS-1$
		}

		@Override
		protected void shutdown(final RequestMonitor requestMonitor) {

			LlinkPlugin.log("TraceStep.shutdown()"); //$NON-NLS-1$

			if (fTraceBackendState != State.STARTED) {
				// Not started yet or already killed, don't bother starting
				// a job to kill it
				requestMonitor.done();
				return;
			}

			new Job(getTerminatingTraceJobName()) {
				{
					setSystem(true);
				}

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						// Need to do this on the executor for thread-safety
						// And we should wait for it to complete since we then
						// check if the killing of GDB worked.

						LlinkPlugin.log("TraceStep.shutdown() run()"); //$NON-NLS-1$

						getExecutor().submit(new DsfRunnable() {
							@Override
							public void run() {

								LlinkPlugin.log("TraceStep.shutdown() run() run()"); //$NON-NLS-1$

								destroyTrace();

								if (fTraceMonitorJob.fMonitorExited) {
									// Now that we have destroyed the process,
									// and that the monitoring thread was
									// killed,
									// we need to set our state and send the
									// event

									LlinkPlugin.log("TraceStep.shutdown() run() run() State.TERMINATED"); //$NON-NLS-1$

									fTraceBackendState = State.TERMINATED;

									// If necessary, send an event like
									// BackendStateChangedEvent(getSession().getId(),
									// getId(), State.TERMINATED),
									// getProperties()
								}
							}
						}).get();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					} catch (ExecutionException e1) {
					}

					LlinkPlugin.log("TraceStep shutdown() run() before getting exitValue"); //$NON-NLS-1$

					int attempts = 0;
					while (attempts < 100) {
						try {
							// Don't know if we really need the exit value...
							// but what the heck.
							// throws exception if process not exited
							LlinkPlugin.log("TraceStep shutdown() exitValue ~~~~~"); //$NON-NLS-1$
							fTraceExitValue = fTraceProcess.exitValue();

							LlinkPlugin.log("TraceStep shutdown() run() return"); //$NON-NLS-1$

							requestMonitor.done();
							return Status.OK_STATUS;
						} catch (IllegalThreadStateException ie) {
							ie.printStackTrace();
						}
						
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						attempts++;
					}

					LlinkPlugin.log("TraceStep shutdown() run() REQUEST_FAILED"); //$NON-NLS-1$

					requestMonitor.setStatus(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID,
					                                    IDsfStatusConstants.REQUEST_FAILED, "GDB semihosting terminate failed", null)); //$NON-NLS-1$
					requestMonitor.done();
					
					return Status.OK_STATUS;
				}
			} .schedule();

			LlinkPlugin.log("TraceStep shutdown() return"); //$NON-NLS-1$

		}

		/**
		 * Launch trace process.
		 *
		 * @param host the host
		 * @param port the port
		 * @return the process
		 */
		protected Process launchTraceProcess(String host, int port) {

			TraceProcess proc = new TraceProcess(host, port);

			// proc.submit(getExecutor());
			proc.submit();

			LlinkPlugin.log("launchTraceProcess() return " + proc); //$NON-NLS-1$

			return proc;
		}
		
	}



	// ========================================================================

	/**
	 * Monitors the trace process, waiting for it to terminate, and then
	 * notifies the associated runtime process.
	 */
	private class TraceMonitorJob extends Job {

		boolean fMonitorExited = false;
		DsfRunnable fMonitorStarted;
		Process fProcess;

		TraceMonitorJob(Process process, DsfRunnable monitorStarted) {
			super("Trace process monitor job."); //$NON-NLS-1$
			fProcess = process;
			fMonitorStarted = monitorStarted;
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			synchronized (fProcess) {

				LlinkPlugin.log("TraceMonitorJob.run() submit " + fMonitorStarted + " thread " + getThread()); //$NON-NLS-1$ //$NON-NLS-2$

				getExecutor().submit(fMonitorStarted);
				try {
					fProcess.waitFor();
					fTraceExitValue = fProcess.exitValue();

					// Need to do this on the executor for thread-safety
					getExecutor().submit(new DsfRunnable() {
						@Override
						public void run() {

							LlinkPlugin.log("TraceMonitorJob.run() run() thread " + getThread()); //$NON-NLS-1$

							// Destroy the entire backend
							destroyTrace();

							LlinkPlugin.log("TraceMonitorJob.run() run() State.TERMINATED"); //$NON-NLS-1$

							fTraceBackendState = State.TERMINATED;

							// If necessary, send an event like
							// BackendStateChangedEvent(getSession().getId(),
							// getId(), State.TERMINATED),
							// getProperties()
						}
					});
				} catch (InterruptedException ie) {
					// clear interrupted state
					Thread.interrupted();
				}

				LlinkPlugin.log("TraceMonitorJob.run() fMonitorExited = true thread " + getThread()); //$NON-NLS-1$

				fMonitorExited = true;
			}
			return Status.OK_STATUS;
		}

		void kill() {
			synchronized (fProcess) {
				if (!fMonitorExited) {
					Thread thread = getThread();
					if (thread != null) {

						LlinkPlugin.log("TraceMonitorJob.kill() interrupt " + thread.toString()); //$NON-NLS-1$

						thread.interrupt();
					} else {
						LlinkPlugin.log("TraceMonitorJob.kill() null thread"); //$NON-NLS-1$
					}
				}
			}
		}
	}

	// ========================================================================

	/**
	 * Start/stop the SemihostingMonitorJob.
	 */
	protected class TraceMonitorStep extends InitializationShutdownStep {

		TraceMonitorStep(Direction direction) {
			super(direction);
		}

		@Override
		public void initialize(final RequestMonitor rm) {

			LlinkPlugin.log("TraceMonitorStep.initialize()"); //$NON-NLS-1$

			if (getGDBServerBackendState() != State.STARTED) {

				LlinkPlugin.log("TraceMonitorStep.initialise() skipped"); //$NON-NLS-1$

				// rm.cancel();
				rm.done();
				return;
			}

			fTraceMonitorJob = new TraceMonitorJob(fTraceProcess, new DsfRunnable() {
				@Override
				public void run() {
					rm.done();
				}
			});
			fTraceMonitorJob.schedule();
		}

		@Override
		protected void shutdown(RequestMonitor requestMonitor) {

			LlinkPlugin.log("TraceMonitorStep.shutdown()"); //$NON-NLS-1$

			if (fTraceMonitorJob != null) {
				fTraceMonitorJob.kill();
			}
			requestMonitor.done();

			LlinkPlugin.log("TraceMonitorStep.shutdown() return"); //$NON-NLS-1$
		}
	}

}
