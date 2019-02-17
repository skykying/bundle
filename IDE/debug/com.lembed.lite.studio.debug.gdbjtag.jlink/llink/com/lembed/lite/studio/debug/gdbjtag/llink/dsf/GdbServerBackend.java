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

import com.lembed.lite.studio.core.StringUtils;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmGdbServerBackend;
import com.lembed.lite.studio.debug.gdbjtag.llink.Configuration;
import com.lembed.lite.studio.debug.gdbjtag.llink.ConfigurationAttributes;
import com.lembed.lite.studio.debug.gdbjtag.llink.DefaultPreferences;
import com.lembed.lite.studio.debug.gdbjtag.llink.IProcessListener;
import com.lembed.lite.studio.debug.gdbjtag.llink.LlinkPlugin;
import com.lembed.lite.studio.debug.gdbjtag.llink.dsf.process.SemihostingProcess;

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
import org.osgi.framework.BundleContext;

/**
 * The Class GdbServerBackend.
 */
public class GdbServerBackend extends GnuArmGdbServerBackend {

	// ------------------------------------------------------------------------

	// Allow derived classes to use these variables.

	/** The do start semihosting console. */
	protected boolean fDoStartSemihostingConsole = false;
	
	/** The semihosting process. */
	protected Process fSemihostingProcess;
	
	/** The semihosting monitor job. */
	protected SemihostingMonitorJob fSemihostingMonitorJob;

	/** For synchronisation reasons, set/check this only on the DSF thread. */
	protected State fSemihostingBackendState = State.NOT_INITIALIZED;
	
	/** The semihosting exit value. */
	protected int fSemihostingExitValue = 0;
	
	/** The gdb server launch timeout. */
	protected int fGdbServerLaunchTimeout = 30;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gdb server backend.
	 *
	 * @param session the session
	 * @param lc the lc
	 */
	public GdbServerBackend(DsfSession session, ILaunchConfiguration lc) {
		super(session, lc);
		LlinkPlugin.log("GdbServerBackend(" + session + "," + lc.getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	// ------------------------------------------------------------------------

	@Override
	public void initialize(final RequestMonitor rm) {

		LlinkPlugin.log("GdbServerBackend.initialize()"); //$NON-NLS-1$
		try {
			// Update parent data member before calling initialize.
			fDoStartGdbServer = Configuration.getDoStartGdbServer(fLaunchConfiguration);
			fDoStartSemihostingConsole = Configuration.getDoAddSemihostingConsole(fLaunchConfiguration);
		} catch (CoreException e) {
			rm.setStatus(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, -1, "Cannot get configuration", e)); //$NON-NLS-1$
			rm.done();
			return;
		}

		// Initialize the super class, and, when ready, perform the local
		// Initializations.
		super.initialize(new RequestMonitor(getExecutor(), rm) {
			@Override
            protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	private void doInitialize(RequestMonitor rm) {

		LlinkPlugin.log("GdbServerBackend.doInitialize()"); //$NON-NLS-1$

		if (fDoStartGdbServer && fDoStartSemihostingConsole) {

			final Sequence.Step[] initializeSteps = new Sequence.Step[] {
			    new SemihostingStep(InitializationShutdownStep.Direction.INITIALIZING),
			    new SemihostingMonitorStep(InitializationShutdownStep.Direction.INITIALIZING),
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

		LlinkPlugin.log("GdbServerBackend.shutdown()"); //$NON-NLS-1$

		if (fDoStartGdbServer && fDoStartSemihostingConsole) {
			final Sequence.Step[] shutdownSteps = new Sequence.Step[] {
			    new SemihostingMonitorStep(InitializationShutdownStep.Direction.SHUTTING_DOWN),
			    new SemihostingStep(InitializationShutdownStep.Direction.SHUTTING_DOWN),
			};
			Sequence startupSequence = new Sequence(getExecutor(), new ImmediateRequestMonitor(rm) {
				@Override
				protected void handleSuccess() {
					// We're done here, shutdown parent.
					GdbServerBackend.super.shutdown(rm);
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

		LlinkPlugin.log("GdbServerBackend.destroy() " + Thread.currentThread()); //$NON-NLS-1$

		destroySemihosting();

		// Destroy the parent (the GDB server; the client is also destroyed
		// there).
		super.destroy();
	}

	private void destroySemihosting() {
		// Destroy the semihosting process
		if (fSemihostingProcess != null && fSemihostingBackendState == State.STARTED) {
			fSemihostingProcess.destroy();
		}
	}
	// ------------------------------------------------------------------------

	@Override
	protected BundleContext getBundleContext() {
		return LlinkPlugin.getInstance().getBundle().getBundleContext();
	}

	@Override
	public String[] getServerCommandLineArray() {
		String[] commandLineArray = Configuration.getGdbServerCommandLineArray(fLaunchConfiguration);
		return commandLineArray;
	}

	@Override
    public String getServerCommandName() {

		String[] commandLineArray = getServerCommandLineArray();
		if (commandLineArray == null) {
			return null;
		}

		String fullCommand = commandLineArray[0];
		return StringUtils.extractNameFromPath(fullCommand);
	}

	@Override
	public int getServerLaunchTimeoutSeconds() {
		return fGdbServerLaunchTimeout;
	}

	@Override
    public String getServerName() {
		return "L Link GDB Server"; //$NON-NLS-1$
	}

	/**
	 * Gets the semihosting name.
	 *
	 * @return the semihosting name
	 */
	public String getSemihostingName() {
		return "L Link GDB"; //$NON-NLS-1$
	}
	
	/**
	 * Gets the trace name.
	 *
	 * @return the trace name
	 */
	public String getTraceName() {
		return "L Link GDB"; //$NON-NLS-1$
	}

	/**
	 * Gets the starting semihosting job name.
	 *
	 * @return the starting semihosting job name
	 */
	public String getStartingSemihostingJobName() {
		return "Starting " + getSemihostingName() + " Semihosting Process"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Gets the terminating semihosting job name.
	 *
	 * @return the terminating semihosting job name
	 */
	public String getTerminatingSemihostingJobName() {
		return "Terminating " + getSemihostingName() + " Semihosting Process"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
    public boolean matchStdOutExpectedPattern(String line) {
		if (line.indexOf("Listening at") >= 0) { //$NON-NLS-1$
			return true;
		}

		return false;
	}

	/**
	 * Since the J-Link stderr messages are not final, this function makes the
	 * best use of the available information (the exit code and the captured
	 * string) to compose the text displayed in case of error.
	 *
	 * @param exitCode
	 *            an integer with the process exit code.
	 * @return a string with the text to be displayed.
	 */
	@Override
	public String prepareMessageBoxText(int exitCode) {

		String body = ""; //$NON-NLS-1$

		if (exitCode == -1) {
			body = "Unknown error. Please use ST Link software v4.96f or later."; //$NON-NLS-1$
		} else if (exitCode == -2) {
			body = "Could not listen on tcp port. Please check if another version of the server is running."; //$NON-NLS-1$
		} else if (exitCode == -3) {
			body = "Could not connect to target. Please check if target is powered and if ribbon cable is plugged properly."; //$NON-NLS-1$
		} else if (exitCode == -4) {
			body = "Failed to accept a connection from GDB client."; //$NON-NLS-1$
		} else if (exitCode == -5) {
			body = "Failed to parse the command line. Please check the command line parameters."; //$NON-NLS-1$
		} else if (exitCode == -6) {
			try {
				String name = Configuration.getGdbServerDeviceName(fLaunchConfiguration);
				body = "Device name '" + name + "' not recognised."; //$NON-NLS-1$ //$NON-NLS-2$
			} catch (CoreException e) {
				LlinkPlugin.log(e);
			}
		} else if (exitCode == -7) {
			// TODO: check if TCP and adjust message accordingly
			body = "Could not connect to ST Link. Please check if plugged into USB port or Ethernet switch."; //$NON-NLS-1$
		} else if (exitCode == 0 ) {
			body = "Could not connect to ST Link. Please check if plugged into USB port or Ethernet switch."; //$NON-NLS-1$
		}

		String name = getServerCommandName();
		if (name == null) {
			name = "GDB Server"; //$NON-NLS-1$
		}
		String tail = "\n\nFor more details, see the " + name + " console."; //$NON-NLS-1$ //$NON-NLS-2$

		if (body.isEmpty()) {
			return getServerName() + " failed with code (" + exitCode + ")." + tail; //$NON-NLS-1$ //$NON-NLS-2$
		}
        return getServerName() + " failed: \n" + body + tail; //$NON-NLS-1$
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the semihosting process.
	 *
	 * @return the semihosting process
	 */
	public Process getSemihostingProcess() {
		return fSemihostingProcess;
	}

	// ========================================================================


	/** Start/Stop the J-Link semihosting console */
	protected class SemihostingStep extends InitializationShutdownStep implements IProcessListener{

		SemihostingStep(Direction direction) {
			super(direction);
		}

		@Override
		public void initialize(final RequestMonitor rm) {

			LlinkPlugin.log("SemihostingStep.initialise()"); //$NON-NLS-1$
			if (getGDBServerBackendState() != State.STARTED) {

				LlinkPlugin.log("SemihostingStep.initialise() skipped"); //$NON-NLS-1$
				// rm.cancel();
				rm.done();
				return;
			}

			class SemihostingLaunchMonitor {
				boolean fLaunched = false;
				boolean fTimedOut = false;
			}

			final SemihostingLaunchMonitor fSemihostingLaunchMonitor = new SemihostingLaunchMonitor();

			final RequestMonitor fTmpLaunchRequestMonitor = new RequestMonitor(getExecutor(), rm) {

				@Override
				protected void handleCompleted() {

					LlinkPlugin.log("SemihostingStep.initialise() handleCompleted()"); //$NON-NLS-1$

					if (!fSemihostingLaunchMonitor.fTimedOut) {
						LlinkPlugin.log("SemihostingStep.initialise() handleCompleted not time out"); //$NON-NLS-1$
						fSemihostingLaunchMonitor.fLaunched = true;
						if (!isSuccess()) {
							rm.setStatus(getStatus());
						}
						rm.done();
					}
				}
			};

			final Job startSemihostingJob = new Job(getStartingSemihostingJobName()) {
				{
					setSystem(true);
				}

				@Override
				protected IStatus run(IProgressMonitor monitor) {

					if (fTmpLaunchRequestMonitor.isCanceled()) {

						LlinkPlugin.log("startSemihostingJob run cancel"); //$NON-NLS-1$
						fTmpLaunchRequestMonitor.setStatus(new Status(IStatus.CANCEL, LlinkPlugin.PLUGIN_ID, -1,
						                                   getStartingSemihostingJobName() + " cancelled.", null)); //$NON-NLS-1$
						fTmpLaunchRequestMonitor.done();
						return Status.OK_STATUS;
					}

					try {
						String host = "localhost"; //$NON-NLS-1$

						int port = fLaunchConfiguration.getAttribute(
						               ConfigurationAttributes.GDB_SERVER_TELNET_PORT_NUMBER,
						               DefaultPreferences.GDB_SERVER_TELNET_PORT_NUMBER_DEFAULT);

						fSemihostingProcess = launchSemihostingProcess(host, port);

						// Need to do this on the executor for thread-safety
						getExecutor().submit(new DsfRunnable() {
							@Override
							public void run() {

								LlinkPlugin.log("startSemihostingJob run State.STARTED"); //$NON-NLS-1$
								fSemihostingBackendState = State.STARTED;
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

					LlinkPlugin.log("startSemihostingJob run completed"); //$NON-NLS-1$
					return Status.OK_STATUS;
				}
			};
			startSemihostingJob.schedule();

			LlinkPlugin.log("SemihostingStep.initialise() after job schedule"); //$NON-NLS-1$

			getExecutor().schedule(new Runnable() {

				@Override
				public void run() {

					// Only process the event if we have not finished yet (hit
					// the breakpoint).
					if (!fSemihostingLaunchMonitor.fLaunched) {
						fSemihostingLaunchMonitor.fTimedOut = true;
						
						Thread jobThread = startSemihostingJob.getThread();
						if (jobThread != null) {

							LlinkPlugin.log("interrupt thread " + jobThread); //$NON-NLS-1$

							jobThread.interrupt();
						}
						rm.setStatus(
						    new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID, DebugException.TARGET_REQUEST_FAILED,
						               getStartingSemihostingJobName() + " timed out.", null)); //$NON-NLS-1$
						rm.done();
					}
				}
			}, getServerLaunchTimeoutSeconds(), TimeUnit.SECONDS);

			LlinkPlugin.log("SemihostingStep.initialise() return"); //$NON-NLS-1$
		}

		@Override
		protected void shutdown(final RequestMonitor requestMonitor) {

			LlinkPlugin.log("SemihostingStep.shutdown()"); //$NON-NLS-1$

			if (fSemihostingBackendState != State.STARTED) {
				// Not started yet or already killed, don't bother starting
				// a job to kill it
				requestMonitor.done();
				return;
			}

			new Job(getTerminatingSemihostingJobName()) {
				{
					setSystem(true);
				}

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						// Need to do this on the executor for thread-safety
						// And we should wait for it to complete since we then
						// check if the killing of GDB worked.

						LlinkPlugin.log("SemihostingStep.shutdown() run()"); //$NON-NLS-1$

						getExecutor().submit(new DsfRunnable() {
							@Override
							public void run() {

								LlinkPlugin.log("SemihostingStep.shutdown() run() run()"); //$NON-NLS-1$

								destroySemihosting();

								if (fSemihostingMonitorJob.fMonitorExited) {
									// Now that we have destroyed the process,
									// and that the monitoring thread was
									// killed,
									// we need to set our state and send the
									// event

									LlinkPlugin.log("SemihostingStep.shutdown() run() run() State.TERMINATED"); //$NON-NLS-1$

									fSemihostingBackendState = State.TERMINATED;

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

					LlinkPlugin.log("SemihostingStep shutdown() run() before getting exitValue"); //$NON-NLS-1$

					int attempts = 0;
					while (attempts < 100) {
						try {
							// Don't know if we really need the exit value...
							// but what the heck.
							// throws exception if process not exited
							LlinkPlugin.log("SemihostingStep shutdown() exitValue ~~~~~"); //$NON-NLS-1$
							fSemihostingExitValue = fSemihostingProcess.exitValue();

							LlinkPlugin.log("SemihostingStep shutdown() run() return"); //$NON-NLS-1$

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

					LlinkPlugin.log("SemihostingStep shutdown() run() REQUEST_FAILED"); //$NON-NLS-1$

					requestMonitor.setStatus(new Status(IStatus.ERROR, LlinkPlugin.PLUGIN_ID,
					                                    IDsfStatusConstants.REQUEST_FAILED, "GDB semihosting terminate failed", null)); //$NON-NLS-1$
					requestMonitor.done();
					
					return Status.OK_STATUS;
				}
			} .schedule();

			LlinkPlugin.log("SemihostingStep shutdown() return"); //$NON-NLS-1$

		}


		/**
		 * Launch semihosting process.
		 *
		 * @param host the host
		 * @param port the port
		 * @return the process
		 */
		protected Process launchSemihostingProcess(String host, int port) {

			SemihostingProcess proc = new SemihostingProcess(host, port);

			// proc.submit(getExecutor());
			proc.submit();

			LlinkPlugin.log("launchSemihostingProcess() return " + proc); //$NON-NLS-1$

			return proc;
		}

		@Override
		public Object callback(Object ey) {
			//if((Boolean)ey) {
				LlinkPlugin.log("launchSemihostingProcess() &&&&&&&&&&&&&&"); //$NON-NLS-1$
			//}
				synchronized(fSemihostingProcess) {
					notify();
				}
			return null;
		}
		
	}



	// ========================================================================

	/**
	 * Monitors the semihosting process, waiting for it to terminate, and then
	 * notifies the associated runtime process.
	 */
	private class SemihostingMonitorJob extends Job {

		boolean fMonitorExited = false;
		DsfRunnable fMonitorStarted;
		Process fProcess;

		SemihostingMonitorJob(Process process, DsfRunnable monitorStarted) {
			super("Semihosting process monitor job."); //$NON-NLS-1$
			fProcess = process;
			fMonitorStarted = monitorStarted;
			setSystem(true);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			synchronized (fProcess) {

				LlinkPlugin.log("SemihostingMonitorJob.run() submit " + fMonitorStarted + " thread " + getThread()); //$NON-NLS-1$ //$NON-NLS-2$

				getExecutor().submit(fMonitorStarted);
				try {
					fProcess.waitFor();
					fSemihostingExitValue = fProcess.exitValue();

					// Need to do this on the executor for thread-safety
					getExecutor().submit(new DsfRunnable() {
						@Override
						public void run() {

							LlinkPlugin.log("SemihostingMonitorJob.run() run() thread " + getThread()); //$NON-NLS-1$

							// Destroy the entire backend
							destroySemihosting();

							LlinkPlugin.log("SemihostingMonitorJob.run() run() State.TERMINATED"); //$NON-NLS-1$

							fSemihostingBackendState = State.TERMINATED;

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

				LlinkPlugin.log("SemihostingMonitorJob.run() fMonitorExited = true thread " + getThread()); //$NON-NLS-1$

				fMonitorExited = true;
			}
			return Status.OK_STATUS;
		}

		void kill() {
			synchronized (fProcess) {
				if (!fMonitorExited) {
					Thread thread = getThread();
					if (thread != null) {

						LlinkPlugin.log("SemihostingMonitorJob.kill() interrupt " + thread.toString()); //$NON-NLS-1$

						thread.interrupt();
					} else {
						LlinkPlugin.log("SemihostingMonitorJob.kill() null thread"); //$NON-NLS-1$
					}
				}
			}
		}
	}

	// ========================================================================

	/**
	 * Start/stop the SemihostingMonitorJob.
	 */
	protected class SemihostingMonitorStep extends InitializationShutdownStep {

		SemihostingMonitorStep(Direction direction) {
			super(direction);
		}

		@Override
		public void initialize(final RequestMonitor rm) {

			LlinkPlugin.log("SemihostingMonitorStep.initialize()"); //$NON-NLS-1$

			if (getGDBServerBackendState() != State.STARTED) {

				LlinkPlugin.log("SemihostingMonitorStep.initialise() skipped"); //$NON-NLS-1$

				// rm.cancel();
				rm.done();
				return;
			}

			fSemihostingMonitorJob = new SemihostingMonitorJob(fSemihostingProcess, new DsfRunnable() {
				@Override
				public void run() {
					rm.done();
				}
			});
			fSemihostingMonitorJob.schedule();
		}

		@Override
		protected void shutdown(RequestMonitor requestMonitor) {

			LlinkPlugin.log("SemihostingMonitorStep.shutdown()"); //$NON-NLS-1$

			if (fSemihostingMonitorJob != null) {
				fSemihostingMonitorJob.kill();
			}
			requestMonitor.done();

			LlinkPlugin.log("SemihostingMonitorStep.shutdown() return"); //$NON-NLS-1$
		}
	}


	@Override
	public State getGDBServerBackendState() {
		return fServerBackendState;
	}

}
