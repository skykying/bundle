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

package com.lembed.lite.studio.debug.gdbjtag.dsf;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.StringUtils;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.debug.core.CDebugUtils;
import org.eclipse.cdt.debug.gdbjtag.core.IGDBJtagConstants;
import org.eclipse.cdt.debug.gdbjtag.core.Messages;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.gdb.service.IGDBBackend;
import org.eclipse.cdt.dsf.service.AbstractDsfService;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.DebugUtils;
import com.lembed.lite.studio.debug.gdbjtag.services.IGnuArmDebuggerCommandsService;

/**
 * The Class GnuArmDebuggerCommandsService.
 */
public abstract class GnuArmDebuggerCommandsService extends AbstractDsfService
	implements IGnuArmDebuggerCommandsService {

	// ------------------------------------------------------------------------

	/** The session. */
	protected DsfSession fSession;
	
	/** The ILaunchConfiguration. */
	protected ILaunchConfiguration fConfig;
	
	/** The do double backslash. */
	protected boolean fDoDoubleBackslash;
	
	/** The DsfServices tracker. */
	protected DsfServicesTracker fTracker;
	
	/** The GDB backend. */
	protected IGDBBackend fGdbBackend;
	
	/** The attributes. */
	protected Map<String, Object> fAttributes;
	
	/** The mode. */
	protected String fMode;

	// protected static final String LINESEP = System
	// .getProperty("line.separator"); //$NON-NLS-1$

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new gnu arm debugger commands service.
	 *
	 * @param session the session
	 * @param lc the lc
	 * @param mode the mode
	 */
	public GnuArmDebuggerCommandsService(DsfSession session, ILaunchConfiguration lc, String mode) {
		this(session, lc, mode, false);
	}

	/**
	 * Instantiates a new gnu arm debugger commands service.
	 *
	 * @param session the session
	 * @param lc the lc
	 * @param mode the mode
	 * @param doubleBackslash the double backslash
	 */
	public GnuArmDebuggerCommandsService(DsfSession session, ILaunchConfiguration lc, String mode,
	                                     boolean doubleBackslash) {
		super(session);

		fSession = session;
		fConfig = lc;

		fMode = mode;
		fDoDoubleBackslash = doubleBackslash;
	}

	// ------------------------------------------------------------------------

	@Override
    public void initialize(final RequestMonitor rm) {

		GdbActivator.log("GnuArmDebuggerCommandsService.initialize()"); //$NON-NLS-1$
		super.initialize(new RequestMonitor(getExecutor(), rm) {

			@Override
            protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void doInitialize(RequestMonitor rm) {

		GdbActivator.log("GnuArmDebuggerCommandsService.doInitialize()"); //$NON-NLS-1$

		// Get and remember the command control service
		// fCommandControl = ((ICommandControlService) getServicesTracker()
		// .getService(ICommandControlService.class));

		// Register this service to DSF.
		// For completeness, use both the interface and the class name.
		register(new String[] { IGnuArmDebuggerCommandsService.class.getName(), this.getClass().getName() },
		         new Hashtable());

		GdbActivator.log(this.getClass().getName() + " registered "); //$NON-NLS-1$
		
		fTracker = new DsfServicesTracker(GdbActivator.getInstance().getBundle().getBundleContext(), fSession.getId());
		fGdbBackend = fTracker.getService(IGDBBackend.class);
		if (fGdbBackend == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1, "Cannot obtain GDBBackend service", null)); //$NON-NLS-1$
			rm.done();
			return;
		}

		rm.done();
	}

	@Override
	public void shutdown(RequestMonitor rm) {

		GdbActivator.log("GnuArmDebuggerCommandsService.shutdown()"); //$NON-NLS-1$

		// Remove this service from DSF.
		unregister();

		super.shutdown(rm);
	}

	// ------------------------------------------------------------------------

	@Override
	public void setAttributes(Map<String, Object> attributes) {
		fAttributes = attributes;
	}

	// ------------------------------------------------------------------------

	@Override
    public IStatus addGnuArmSelectRemoteCommands(List<String> commandsList) {

		String remoteTcpHost = CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_IP_ADDRESS,
		                       IGDBJtagConstants.DEFAULT_IP_ADDRESS);
		Integer remoteTcpPort = CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_PORT_NUMBER,
		                        IGDBJtagConstants.DEFAULT_PORT_NUMBER);

		commandsList.add("-target-select remote " + remoteTcpHost + ":" + remoteTcpPort + ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		return Status.OK_STATUS;
	}

	@Override
	public IStatus addGnuArmRestartCommands(List<String> commandsList) {

		return addStartRestartCommands(true, commandsList);
	}

	// ------------------------------------------------------------------------

	/**
	 * Escape spaces.
	 *
	 * @param file the file
	 * @return the string
	 */
	protected String escapeSpaces(String file) {
		if (file.indexOf(' ') >= 0) {
			return '"' + file + '"';
		}
		return file;
	}

	/**
	 * Add the "symbol-file <path>" command, using the jtag device definition.
	 * Always duplicate backslashes on Windows.
	 */
	@Override
	public IStatus addLoadSymbolsCommands(List<String> commandsList) {

		IPath programPath = fGdbBackend.getProgramPath();

		if (!CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_LOAD_SYMBOLS,
		                              IGDBJtagConstants.DEFAULT_LOAD_SYMBOLS)) {

			// Not required.
			return Status.OK_STATUS;
		}

		String symbolsFileName = null;

		// New setting in Helios. Default is true. Check for existence
		// in order to support older launch configs
		if (fAttributes.containsKey(IGDBJtagConstants.ATTR_USE_PROJ_BINARY_FOR_SYMBOLS)
		        && CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_USE_PROJ_BINARY_FOR_SYMBOLS,
		                                    IGDBJtagConstants.DEFAULT_USE_PROJ_BINARY_FOR_SYMBOLS)) {
			if (programPath != null) {
				symbolsFileName = programPath.toOSString();
			}
		} else {
			symbolsFileName = CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_SYMBOLS_FILE_NAME,
			                  IGDBJtagConstants.DEFAULT_SYMBOLS_FILE_NAME);
			if (!symbolsFileName.isEmpty()) {
				symbolsFileName = DebugUtils.resolveAll(symbolsFileName, fAttributes);
			} else {
				symbolsFileName = null;
			}
		}

		if (symbolsFileName == null) {
			return new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1,
			                  Messages.getString("GDBJtagDebugger.err_no_img_file"), null); //$NON-NLS-1$
		}

		if (EclipseUtils.isWindows()) {
			// Escape windows path separator characters TWICE, once for
			// Java and once for GDB.
			symbolsFileName = StringUtils.duplicateBackslashes(symbolsFileName);
		}

		String file = escapeSpaces(symbolsFileName);

		String symbolsOffset = CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_SYMBOLS_OFFSET,
		                       IGDBJtagConstants.DEFAULT_SYMBOLS_OFFSET);
		if (!symbolsOffset.isEmpty()) {
			symbolsOffset = "0x" + symbolsOffset; //$NON-NLS-1$
			// addCmd(commandsList, "add-symbol-file " + file + " "
			// + symbolsOffset);
			commandsList.add("add-symbol-file " + file + " " + symbolsOffset); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			// addCmd(commandsList, "symbol-file " + file);
			commandsList.add("symbol-file " + file); //$NON-NLS-1$
		}

		GdbActivator.log(commandsList.toString());
		return Status.OK_STATUS;
	}

	/**
	 * Add the "load <path>" command, using the jtag device definition. Always
	 * duplicate backslashes on Windows.
	 */
	@Override
	public IStatus addLoadImageCommands(List<String> commandsList) {

		IPath programPath = fGdbBackend.getProgramPath();

		String imageFileName = null;

		if (fAttributes.containsKey(IGDBJtagConstants.ATTR_USE_PROJ_BINARY_FOR_IMAGE)
		        && CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_USE_PROJ_BINARY_FOR_IMAGE,
		                                    IGDBJtagConstants.DEFAULT_USE_PROJ_BINARY_FOR_IMAGE)) {
			if (programPath != null) {
				imageFileName = programPath.toOSString();
			}
		} else {
			imageFileName = CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_IMAGE_FILE_NAME,
			                IGDBJtagConstants.DEFAULT_IMAGE_FILE_NAME);
			if (!imageFileName.isEmpty()) {
				imageFileName = DebugUtils.resolveAll(imageFileName, fAttributes);
			} else {
				imageFileName = null;
			}
		}

		if (imageFileName == null) {
			return new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, -1,
			                  Messages.getString("GDBJtagDebugger.err_no_img_file"), null); //$NON-NLS-1$
		}

		imageFileName = DebugUtils.resolveAll(imageFileName, fAttributes);

		if (EclipseUtils.isWindows()) {
			// Escape windows path separator characters TWICE, once
			// for Java and once for GDB.
			imageFileName = StringUtils.duplicateBackslashes(imageFileName);
		}

		String imageOffset = CDebugUtils
		                     .getAttribute(fAttributes, IGDBJtagConstants.ATTR_IMAGE_OFFSET, IGDBJtagConstants.DEFAULT_IMAGE_OFFSET)
		                     .trim();
		if (!imageOffset.isEmpty()) {
			imageOffset = (imageFileName.endsWith(".elf")) ? "" //$NON-NLS-1$ //$NON-NLS-2$
			              : "0x" + CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_IMAGE_OFFSET, //$NON-NLS-1$
			                      IGDBJtagConstants.DEFAULT_IMAGE_OFFSET); // $NON-NLS-2$
		}

		String file = escapeSpaces(imageFileName);
		// addCmd(commandsList, "load " + file + ' ' + imageOffset);
		commandsList.add("load " + file + ' ' + imageOffset); //$NON-NLS-1$
		GdbActivator.log(commandsList.toString());

		return Status.OK_STATUS;
	}

	@Override
    public IStatus addSetPcCommands(List<String> commandsList) {

		if (CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_SET_PC_REGISTER,
		                             IGDBJtagConstants.DEFAULT_SET_PC_REGISTER)) {
			String pcRegister = CDebugUtils
			                    .getAttribute(fAttributes, IGDBJtagConstants.ATTR_PC_REGISTER, CDebugUtils.getAttribute(fAttributes,
			                                  IGDBJtagConstants.ATTR_IMAGE_OFFSET, IGDBJtagConstants.DEFAULT_PC_REGISTER))
			                    .trim();
			if (!pcRegister.isEmpty()) {
				commandsList.add("set $pc=0x" + pcRegister); //$NON-NLS-1$
				GdbActivator.log(commandsList.toString());
			}
		}

		return Status.OK_STATUS;
	}

	@Override
    public IStatus addStopAtCommands(List<String> commandsList) {

		// This code is also used to start run configurations.
		// Set the breakpoint only for debug.
		if (fMode.equals(ILaunchManager.DEBUG_MODE)) {
			if (CDebugUtils.getAttribute(fAttributes, IGDBJtagConstants.ATTR_SET_STOP_AT,
			                             IGDBJtagConstants.DEFAULT_SET_STOP_AT)) {
				String stopAt = CDebugUtils
				                .getAttribute(fAttributes, IGDBJtagConstants.ATTR_STOP_AT, IGDBJtagConstants.DEFAULT_STOP_AT)
				                .trim();

				if (!stopAt.isEmpty()) {
					// doAtopAt replaced by a simple tbreak
					commandsList.add("tbreak " + stopAt); //$NON-NLS-1$
					GdbActivator.log(commandsList.toString());
				}
			}
		}

		return Status.OK_STATUS;
	}

	// ------------------------------------------------------------------------
}
