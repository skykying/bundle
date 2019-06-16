/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Liviu Ionescu - initial version
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.services;

import org.eclipse.cdt.dsf.concurrent.Immutable;
import org.eclipse.cdt.dsf.mi.service.IMIBackend.State;
import org.eclipse.cdt.dsf.service.IDsfService;
import org.eclipse.core.runtime.IStatus;

/**
 * The Interface IGdbServerBackendService.
 */
public interface IGdbServerBackendService extends IDsfService {
 
	// ========================================================================

	/**
	 * Event indicating that the server back end process has started or
	 * terminated.
	 */
	@Immutable
	public static class ServerBackendStateChangedEvent {

		// --------------------------------------------------------------------

		final private String fSessionId;
		final private String fBackendId;
		final private State fState;

		// --------------------------------------------------------------------

		/**
		 * Instantiates a new server backend state changed event.
		 *
		 * @param sessionId the session id
		 * @param backendId the backend id
		 * @param state the state
		 */
		public ServerBackendStateChangedEvent(String sessionId, String backendId, State state) {
			fSessionId = sessionId;
			fBackendId = backendId;
			fState = state;
		}

		// --------------------------------------------------------------------

		/**
		 * Gets the session id.
		 *
		 * @return the session id
		 */
		public String getSessionId() {
			return fSessionId;
		}

		/**
		 * Gets the backend id.
		 *
		 * @return the backend id
		 */
		public String getBackendId() {
			return fBackendId;
		}

		/**
		 * Gets the state.
		 *
		 * @return the state
		 */
		public State getState() {
			return fState;
		}

		// --------------------------------------------------------------------
	}

	// ------------------------------------------------------------------------

	/**
	 * Returns the identifier of this backend service. It can be used to
	 * distinguish between multiple instances of this service in a single
	 * session.
	 * @return String
	 */
	public String getId();

	/**
	 * Gets the server process.
	 *
	 * @return the server process
	 */
	public Process getServerProcess();

	/**
	 * Gets the server state.
	 *
	 * @return the server state
	 */
	public State getServerState();

	/**
	 * Gets the server exit code.
	 *
	 * @return the server exit code
	 */
	public int getServerExitCode();

	/**
	 * Gets the server exit status.
	 *
	 * @return the server exit status
	 */
	public IStatus getServerExitStatus();

	/**
	 * Destroy.
	 */
	public void destroy();

	/**
	 * Gets the server command name.
	 *
	 * @return the server command name
	 */
	public String getServerCommandName();

	// ------------------------------------------------------------------------
}
