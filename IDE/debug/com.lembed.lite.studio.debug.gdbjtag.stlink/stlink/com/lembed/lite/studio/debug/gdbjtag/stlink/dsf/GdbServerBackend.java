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
package com.lembed.lite.studio.debug.gdbjtag.stlink.dsf;

import com.lembed.lite.studio.core.StringUtils;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmGdbServerBackend;
import com.lembed.lite.studio.debug.gdbjtag.stlink.STlinkPlugin;
import com.lembed.lite.studio.debug.gdbjtag.stlink.SemihostingProcess;
import com.lembed.lite.studio.debug.gdbjtag.stlink.Configuration;
import com.lembed.lite.studio.debug.gdbjtag.stlink.ConfigurationAttributes;
import com.lembed.lite.studio.debug.gdbjtag.stlink.DefaultPreferences;
import com.lembed.lite.studio.debug.gdbjtag.stlink.SemihostingProcess;

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

public class GdbServerBackend extends GnuArmGdbServerBackend {

	// ------------------------------------------------------------------------

	// Allow derived classes to use these variables.

	protected boolean fDoStartSemihostingConsole = false;
	protected Process fSemihostingProcess;
	protected SemihostingMonitorJob fSemihostingMonitorJob;

	// For synchronisation reasons, set/check this only on the DSF thread.
	protected State fSemihostingBackendState = State.NOT_INITIALIZED;
	protected int fSemihostingExitValue = 0;
	protected int fGdbServerLaunchTimeout = 30;

	// ------------------------------------------------------------------------

	public GdbServerBackend(DsfSession session, ILaunchConfiguration lc) {
		super(session, lc);
		STlinkPlugin.log("GdbServerBackend(" + session + "," + lc.getName() + ")");
	}

	// ------------------------------------------------------------------------

	@Override
	public void initialize(final RequestMonitor rm) {

		STlinkPlugin.log("GdbServerBackend.initialize()");
		try {
			// Update parent data member before calling initialise.
			fDoStartGdbServer = Configuration.getDoStartGdbServer(fLaunchConfiguration);
			fDoStartSemihostingConsole = Configuration.getDoAddSemihostingConsole(fLaunchConfiguration);
		} catch (CoreException e) {
			rm.setStatus(new Status(IStatus.ERROR, STlinkPlugin.PLUGIN_ID, -1, "Cannot get configuration", e)); //$NON-NLS-1$
			rm.done();
			return;
		}

		// Initialise the super class, and, when ready, perform the local
		// initialisations.
		super.initialize(new RequestMonitor(getExecutor(), rm) {
			protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	private void doInitialize(RequestMonitor rm) {

		STlinkPlugin.log("GdbServerBackend.doInitialize()");

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

		STlinkPlugin.log("GdbServerBackend.shutdown()");

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

		STlinkPlugin.log("GdbServerBackend.destroy() " + Thread.currentThread());

		// Destroy the semihosting process
		if (fSemihostingProcess != null && fSemihostingBackendState == State.STARTED) {
			fSemihostingProcess.destroy();
		}

		// Destroy the parent (the GDB server; the client is also destroyed
		// there).
		super.destroy();
	}

	// ------------------------------------------------------------------------

	@Override
	protected BundleContext getBundleContext() {
		return STlinkPlugin.getInstance().getBundle().getBundleContext();
	}

	@Override
	public String[] getServerCommandLineArray() {
		String[] commandLineArray = Configuration.getGdbServerCommandLineArray(fLaunchConfiguration);
		return commandLineArray;
	}

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

	public String getServerName() {
		return "ST Link GDB Server";
	}

	public String getSemihostingName() {
		return "ST Link GDB";
	}

	public String getStartingSemihostingJobName() {
		return "Starting " + getSemihostingName() + " Semihosting Process";
	}

	public String getTerminatingSemihostingJobName() {
		return "Terminating " + getSemihostingName() + " Semihosting Process";
	}

	public boolean matchStdOutExpectedPattern(String line) {
		if (line.indexOf("Listening at") >= 0) {
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
	 * @param message
	 *            a string with the captured stderr text.
	 * @return a string with the text to be displayed.
	 */
	@Override
	public String prepareMessageBoxText(int exitCode) {

		String body = "";

		if (exitCode == -1) {
			body = "Unknown error. Please use ST Link software v4.96f or later.";
		} else if (exitCode == -2) {
			body = "Could not listen on tcp port. Please check if another version of the server is running.";
		} else if (exitCode == -3) {
			body = "Could not connect to target. Please check if target is powered and if ribbon cable is plugged properly.";
		} else if (exitCode == -4) {
			body = "Failed to accept a connection from GDB client.";
		} else if (exitCode == -5) {
			body = "Failed to parse the command line. Please check the command line parameters.";
		} else if (exitCode == -6) {
			try {
				String name = Configuration.getGdbServerDeviceName(fLaunchConfiguration);
				body = "Device name '" + name + "' not recognised.";
			} catch (CoreException e) {
				STlinkPlugin.log(e);
			}
		} else if (exitCode == -7) {
			// TODO: check if TCP and adjust message accordingly
			body = "Could not connect to ST Link. Please check if plugged into USB port or Ethernet switch.";
		} else if (exitCode == 0 ) {
			body = "Could not connect to ST Link. Please check if plugged into USB port or Ethernet switch.";
		}

		String name = getServerCommandName();
		if (name == null) {
			name = "GDB Server";
		}
		String tail = "\n\nFor more details, see the " + name + " console.";

		if (body.isEmpty()) {
			return getServerName() + " failed with code (" + exitCode + ")." + tail;
		} else {
			return getServerName() + " failed: \n" + body + tail;
		}
	}

	// ------------------------------------------------------------------------

	public Process getSemihostingProcess() {
		return fSemihostingProcess;
	}

	// ========================================================================

	// Start/Stop the J-Link semihosting console
	protected class SemihostingStep extends InitializationShutdownStep {

		SemihostingStep(Direction direction) {
			super(direction);
		}

		@Override
		public void initialize(final RequestMonitor rm) {

			STlinkPlugin.log("SemihostingStep.initialise()");
			if (fServerBackendState != State.STARTED) {

				STlinkPlugin.log("SemihostingStep.initialise() skipped");
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

					STlinkPlugin.log("SemihostingStep.initialise() handleCompleted()");

					if (!fSemihostingLaunchMonitor.fTimedOut) {
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

						STlinkPlugin.log("startSemihostingJob run cancel");
						fTmpLaunchRequestMonitor.setStatus(new Status(IStatus.CANCEL, STlinkPlugin.PLUGIN_ID, -1,
						                                   getStartingSemihostingJobName() + " cancelled.", null)); //$NON-NLS-1$
						fTmpLaunchRequestMonitor.done();
						return Status.OK_STATUS;
					}

					try {
						String host = "localhost";

						int port = fLaunchConfiguration.getAttribute(
						               ConfigurationAttributes.GDB_SERVER_TELNET_PORT_NUMBER,
						               DefaultPreferences.GDB_SERVER_TELNET_PORT_NUMBER_DEFAULT);

						fSemihostingProcess = launchSemihostingProcess(host, port);

						// Need to do this on the executor for thread-safety
						getExecutor().submit(new DsfRunnable() {
							@Override
							public void run() {

								STlinkPlugin.log("startSemihostingJob run State.STARTED");
								fSemihostingBackendState = State.STARTED;
							}
						});
					} catch (CoreException e) {
						fTmpLaunchRequestMonitor.setStatus(new Status(IStatus.ERROR, STlinkPlugin.PLUGIN_ID, -1, e.getMessage(), e));
						fTmpLaunchRequestMonitor.done();
						return Status.OK_STATUS;
					}

					// TODO: check if the process started properly
					// (parse input and check greeting).

					fTmpLaunchRequestMonitor.done();

					STlinkPlugin.log("startSemihostingJob run completed");
					return Status.OK_STATUS;
				}
			};
			startSemihostingJob.schedule();

			STlinkPlugin.log("SemihostingStep.initialise() after job schedule");

			getExecutor().schedule(new Runnable() {

				@Override
				public void run() {

					// Only process the event if we have not finished yet (hit
					// the breakpoint).
					if (!fSemihostingLaunchMonitor.fLaunched) {
						fSemihostingLaunchMonitor.fTimedOut = true;
						Thread jobThread = startSemihostingJob.getThread();
						if (jobThread != null) {

							STlinkPlugin.log("interrupt thread " + jobThread);

							jobThread.interrupt();
						}
						rm.setStatus(
						    new Status(IStatus.ERROR, STlinkPlugin.PLUGIN_ID, DebugException.TARGET_REQUEST_FAILED,
						               getStartingSemihostingJobName() + " timed out.", null)); //$NON-NLS-1$
						rm.done();
					}
				}
			}, getServerLaunchTimeoutSeconds(), TimeUnit.SECONDS);

			STlinkPlugin.log("SemihostingStep.initialise() return");
		}

		@Override
		protected void shutdown(final RequestMonitor requestMonitor) {

			STlinkPlugin.log("SemihostingStep.shutdown()");

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

						STlinkPlugin.log("SemihostingStep.shutdown() run()");

						getExecutor().submit(new DsfRunnable() {
							@Override
							public void run() {

								STlinkPlugin.log("SemihostingStep.shutdown() run() run()");

								destroy();

								if (fSemihostingMonitorJob.fMonitorExited) {
									// Now that we have destroyed the process,
									// and that the monitoring thread was
									// killed,
									// we need to set our state and send the
									// event

									STlinkPlugin.log("SemihostingStep.shutdown() run() run() State.TERMINATED");

									fSemihostingBackendState = State.TERMINATED;

									// If necessary, send an event like
									// BackendStateChangedEvent(getSession().getId(),
									// getId(), State.TERMINATED),
									// getProperties()
								}
							}
						}).get();
					} catch (InterruptedException e1) {
					} catch (ExecutionException e1) {
					}

					STlinkPlugin.log("SemihostingStep shutdown() run() before getting exitValue");

					int attempts = 0;
					while (attempts < 10) {
						try {
							// Don't know if we really need the exit value...
							// but what the heck.
							// throws exception if process not exited
							fSemihostingExitValue = fSemihostingProcess.exitValue();

							STlinkPlugin.log("SemihostingStep shutdown() run() return");

							requestMonitor.done();
							return Status.OK_STATUS;
						} catch (IllegalThreadStateException ie) {
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
						attempts++;
					}

					STlinkPlugin.log("SemihostingStep shutdown() run() REQUEST_FAILED");

					requestMonitor.setStatus(new Status(IStatus.ERROR, STlinkPlugin.PLUGIN_ID,
					                                    IDsfStatusConstants.REQUEST_FAILED, "GDB semihosting terminate failed", null)); //$NON-NLS-1$
					requestMonitor.done();
					return Status.OK_STATUS;
				}
			} .schedule();

			STlinkPlugin.log("SemihostingStep shutdown() return");

		}

		protected Process launchSemihostingProcess(String host, int port) throws CoreException {

			SemihostingProcess proc = new SemihostingProcess(host, port);

			// proc.submit(getExecutor());
			proc.submit();

			STlinkPlugin.log("launchSemihostingProcess() return " + proc);

			return proc;
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

				STlinkPlugin.log("SemihostingMonitorJob.run() submit " + fMonitorStarted + " thread " + getThread());

				getExecutor().submit(fMonitorStarted);
				try {
					fProcess.waitFor();
					fSemihostingExitValue = fProcess.exitValue();

					// Need to do this on the executor for thread-safety
					getExecutor().submit(new DsfRunnable() {
						@Override
						public void run() {

							STlinkPlugin.log("SemihostingMonitorJob.run() run() thread " + getThread());

							// Destroy the entire backend
							destroy();

							STlinkPlugin.log("SemihostingMonitorJob.run() run() State.TERMINATED");

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

				STlinkPlugin.log("SemihostingMonitorJob.run() fMonitorExited = true thread " + getThread());

				fMonitorExited = true;
			}
			return Status.OK_STATUS;
		}

		void kill() {
			synchronized (fProcess) {
				if (!fMonitorExited) {
					Thread thread = getThread();
					if (thread != null) {

						STlinkPlugin.log("SemihostingMonitorJob.kill() interrupt " + thread.toString());

						thread.interrupt();
					} else {
						STlinkPlugin.log("SemihostingMonitorJob.kill() null thread");
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

			STlinkPlugin.log("SemihostingMonitorStep.initialize()");

			if (fServerBackendState != State.STARTED) {

				STlinkPlugin.log("SemihostingMonitorStep.initialise() skipped");

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

			STlinkPlugin.log("SemihostingMonitorStep.shutdown()");

			if (fSemihostingMonitorJob != null) {
				fSemihostingMonitorJob.kill();
			}
			requestMonitor.done();

			STlinkPlugin.log("SemihostingMonitorStep.shutdown() return");
		}
	}

	@Override
	public State getGDBServerBackendState() {
		// TODO Auto-generated method stub
		return null;
	}

	// ------------------------------------------------------------------------
}
