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

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.cdt.core.IAddress;
import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.DsfExecutor;
import org.eclipse.cdt.dsf.concurrent.IDsfStatusConstants;
import org.eclipse.cdt.dsf.concurrent.ImmediateDataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.ImmediateExecutor;
import org.eclipse.cdt.dsf.concurrent.ImmediateRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.concurrent.Sequence;
import org.eclipse.cdt.dsf.datamodel.DMContexts;
import org.eclipse.cdt.dsf.debug.service.IExpressions;
import org.eclipse.cdt.dsf.debug.service.IExpressions.IExpressionDMContext;
import org.eclipse.cdt.dsf.debug.service.IMemory;
import org.eclipse.cdt.dsf.debug.service.IProcesses;
import org.eclipse.cdt.dsf.debug.service.IRunControl.IContainerDMContext;
import org.eclipse.cdt.dsf.debug.service.IRunControl.IExitedDMEvent;
import org.eclipse.cdt.dsf.debug.service.command.ICommandControlService;
import org.eclipse.cdt.dsf.gdb.service.command.IGDBControl;
import org.eclipse.cdt.dsf.mi.service.IMIProcesses;
import org.eclipse.cdt.dsf.mi.service.MIMemory;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.mi.service.command.output.CLIShowEndianInfo;
import org.eclipse.cdt.dsf.mi.service.command.output.MIDataEvaluateExpressionInfo;
import org.eclipse.cdt.dsf.mi.service.command.output.MIGDBShowLanguageInfo;
import org.eclipse.cdt.dsf.mi.service.command.output.MIInfo;
import org.eclipse.cdt.dsf.service.DsfServiceEventHandler;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.MemoryByte;
import org.osgi.framework.BundleContext;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;

/**
 * More or less a duplicate of GDBMemory from CDT 8.3, but without any cache.
 *
 */
public class PeripheralMemoryService extends MIMemory implements IPeripheralMemoryService {

	// ------------------------------------------------------------------------

	//@SuppressWarnings("unused")
	//private ILaunchConfiguration fConfig = null;
	private IGDBControl fCommandControl;
	private DsfSession fSession;
	private DsfServicesTracker fTracker;

	// private PeripheralMemoryBlockRetrieval fMemoryRetrieval;

	/**
	 * Cache of the address sizes for each memory context.
	 */
	// private Map<IMemoryDMContext, Integer> fAddressSizes = new
	// HashMap<IMemoryDMContext, Integer>();

	/**
	 * We assume the endianness is the same for all processes because GDB
	 * supports only one target.
	 */
	private Boolean fIsBigEndian;
	private Integer fAddressSize;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral memory service.
	 *
	 * @param session the session
	 * @param launchConfiguration the launch configuration
	 */
	public PeripheralMemoryService(DsfSession session, ILaunchConfiguration launchConfiguration) {
		super(session);

		fSession = session;
		//fConfig = launchConfiguration;
	}

	@Override
	public void initialize(final RequestMonitor rm) {

		GdbActivator.log("PeripheralMemoryService.initialize()"); //$NON-NLS-1$
	
		super.initialize(new ImmediateRequestMonitor(rm) {
			@Override
			protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	private void doInitialize(final RequestMonitor rm) {

		fCommandControl = getServicesTracker().getService(IGDBControl.class);

		// Register this service to DSF.
		// For completeness, use both the interface and the class name.
		// Used in PeripheralMemoryBlockExtension by interface name.
		register(new String[] { IPeripheralMemoryService.class.getName(), PeripheralMemoryService.class.getName(), },
		         new Hashtable<String, String>());

		
		GdbActivator.log("PeripheralMemoryService registered " + this); //$NON-NLS-1$
	
		fTracker = new DsfServicesTracker(GdbActivator.getInstance().getBundle().getBundleContext(), fSession.getId());

		ICommandControlService commandControlService = fTracker
		        .getService(ICommandControlService.class);
		IMIProcesses processes = fTracker.getService(IMIProcesses.class);
		if ((commandControlService != null) && (processes != null)) {

			// Create memory context from process context.
			IProcesses.IProcessDMContext processDMContext = processes
			        .createProcessContext(commandControlService.getContext(), ""); //$NON-NLS-1$
			IMemory.IMemoryDMContext memoryDMContext = (IMemory.IMemoryDMContext) processes
			        .createContainerContext(processDMContext, ""); //$NON-NLS-1$

			initializeMemoryData(memoryDMContext, rm);
			return;

		}
		rm.done();
	}

	@Override
	public void shutdown(RequestMonitor rm) {

		{
			GdbActivator.log("PeripheralMemoryService.shutdown()"); //$NON-NLS-1$
		}

		// Remove this service from DSF.
		unregister();

		// fAddressSizes.clear();

		super.shutdown(rm);
	}

	// ------------------------------------------------------------------------

	@Override
	protected BundleContext getBundleContext() {
		return GdbActivator.getInstance().getBundle().getBundleContext();
	}

	/**
	 * The sequence of steps used to start the peripherals service. It is called
	 * on each memory block extension, but only the first time is effective,
	 * later it just returns 0 steps.
	 */
	private class PeripheralSequence extends Sequence {

		private String fOriginalLanguage = MIGDBShowLanguageInfo.AUTO;
		private boolean fAbortLanguageSteps = false;
		IMemoryDMContext fMemContext;

		// Need a global here as getSteps() can be called more than
		// once.
		private Step[] fSteps = null;

		public PeripheralSequence(IMemoryDMContext memContext, DsfExecutor executor, RequestMonitor rm) {
			super(executor, rm);

			{
				GdbActivator.log("PeripheralSequence() " + memContext.getSessionId()); //$NON-NLS-1$
			}
			fMemContext = memContext;
		}

		@Override
		public Step[] getSteps() {
			if (fSteps == null) {
				fSteps = prepareSteps();
			}

			return fSteps;
		}

		private void stepShowLanguage(final RequestMonitor requestMonitor) {

			fCommandControl.queueCommand(fCommandControl.getCommandFactory().createMIGDBShowLanguage(fMemContext),
			new ImmediateDataRequestMonitor<MIGDBShowLanguageInfo>(requestMonitor) {
				@Override
				protected void handleCompleted() {
					if (isSuccess()) {
						fOriginalLanguage = getData().getLanguage();
					} else {
						fAbortLanguageSteps = true;
					}
					requestMonitor.done();
				}
			});
		}

		private void stepReadAddressSize(final RequestMonitor requestMonitor) {

			// Run this step even if the language
			// commands where aborted, but accept
			// failures.
			readAddressSize(fMemContext, new ImmediateDataRequestMonitor<Integer>(requestMonitor) {
				@Override
				protected void handleCompleted() {
					if (isSuccess()) {
						// fAddressSizes.put(fMemContext, getData());
						fAddressSize = getData();
					}
					// Accept failure
					requestMonitor.done();
				}
			});
		}

		private void stepSetLanguage(final RequestMonitor requestMonitor) {

			if (fAbortLanguageSteps) {
				requestMonitor.done();
				return;
			}

			fCommandControl.queueCommand(
			    fCommandControl.getCommandFactory().createMIGDBSetLanguage(fMemContext, fOriginalLanguage),
			new ImmediateDataRequestMonitor<MIInfo>(requestMonitor) {
				@Override
				protected void handleCompleted() {
					if (!isSuccess()) {
						// If we are unable to set
						// the original language back
						// things could be bad.
						// Let's try setting it to
						// "auto" as a fall back.
						// Log the situation as
						// info.
						GdbActivator.log(getStatus());

						// The following error could be bad because
						// we've changed the language to C but are
						// unable to switch it back.
						// Log the error. If the language happens
						// to be C anyway, everything will continue to
						// work, which is why we don't abort the
						// sequence (which would cause the entire
						// session to fail).
						fCommandControl.queueCommand(
						    fCommandControl.getCommandFactory().createMIGDBSetLanguage(fMemContext,
						            MIGDBShowLanguageInfo.AUTO),
						new ImmediateDataRequestMonitor<MIInfo>(requestMonitor) {
							@Override
							protected void handleCompleted() {
								if (!isSuccess()) {
									// See above
									GdbActivator.log(getStatus());
								}
								// Accept
								// failure
								requestMonitor.done();
							}
						});
					} else {
						requestMonitor.done();
					}
				}
			});
		}

		private void stepReadEndianess(final RequestMonitor requestMonitor) {

			readEndianness(fMemContext, new ImmediateDataRequestMonitor<Boolean>(requestMonitor) {
				@Override
				protected void handleCompleted() {
					if (isSuccess()) {
						fIsBigEndian = getData();
					}
					// Accept failure
					requestMonitor.done();
				}
			});
		}

		private Step[] prepareSteps() {
			ArrayList<Step> stepsList = new ArrayList<>();

			// if (fAddressSizes.get(fMemContext) == null) {
			if (fAddressSize == null) {
				stepsList.add(new Step() {
					// store original language
					@Override
					public void execute(final RequestMonitor requestMonitor) {
						stepShowLanguage(requestMonitor);
					}
				});

				stepsList.add(new Step() {
					// read address size
					@Override
					public void execute(final RequestMonitor requestMonitor) {
						stepReadAddressSize(requestMonitor);
					}
				});

				stepsList.add(new Step() {
					// restore original language
					@Override
					public void execute(final RequestMonitor requestMonitor) {
						stepSetLanguage(requestMonitor);
					}
				});
			}

			if (fIsBigEndian == null) {
				stepsList.add(new Step() {
					// read endianness
					@Override
					public void execute(final RequestMonitor requestMonitor) {
						stepReadEndianess(requestMonitor);
					}

				});
			}

			{
				GdbActivator.log("PeripheralSequence has " + stepsList.size() + " steps."); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return stepsList.toArray(new Step[stepsList.size()]);
		}
	}

	@Override
	public void initializeMemoryData(final IMemoryDMContext memContext, RequestMonitor rm) {

		// if (fAddressSizes == null || fIsBigEndian == null) {

		// The address size and endianness do not change during a debug
		// session, so avoid executing the special sequence if not
		// necessary.
		ImmediateExecutor.getInstance().execute(new PeripheralSequence(memContext, getExecutor(), rm));
		// } else {
		// rm.done();
		// }
	}

	/**
	 * Event dispatched.
	 *
	 * @param event the event
	 */
	@DsfServiceEventHandler
	public void eventDispatched(IExitedDMEvent event) {
		if (event.getDMContext() instanceof IContainerDMContext) {
			IMemoryDMContext context = DMContexts.getAncestorOfType(event.getDMContext(), IMemoryDMContext.class);
			if (context != null) {
				// fAddressSizes.remove(context);
				fAddressSize = null;
				fIsBigEndian = null;
			}
		}
	}

	@Override
	public int getAddressSize(IMemoryDMContext context) {
		// Integer addressSize = fAddressSizes.get(context);
		// return (addressSize != null) ? addressSize.intValue() : 8;
		return fAddressSize.intValue();
	}

	@Override
	public boolean isBigEndian(IMemoryDMContext context) {
		//assert fIsBigEndian != null;
		if (fIsBigEndian == null) {
			GdbActivator.log(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Endianness was never initialized!")); //$NON-NLS-1$
			return false;
		}
		return fIsBigEndian;
	}

	/**
	 * Read address size.
	 *
	 * @param memContext the mem context
	 * @param drm the drm
	 */
	protected void readAddressSize(IMemoryDMContext memContext, final DataRequestMonitor<Integer> drm) {
		IExpressions exprService = getServicesTracker().getService(IExpressions.class);
		if(exprService != null) {
		    IExpressionDMContext exprContext = exprService.createExpression(memContext, "sizeof (void*)"); //$NON-NLS-1$
		      CommandFactory commandFactory = fCommandControl.getCommandFactory();
		        fCommandControl.queueCommand(commandFactory.createMIDataEvaluateExpression(exprContext),
		        new DataRequestMonitor<MIDataEvaluateExpressionInfo>(ImmediateExecutor.getInstance(), drm) {
		            @Override
		            protected void handleSuccess() {
		                try {
		                    Integer data = Integer.decode(getData().getValue());
		                    {
		                        GdbActivator.log("readAddressSize() " + data); //$NON-NLS-1$
		                    }
		                    drm.setData(data);
		                } catch (NumberFormatException e) {
		                    drm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID,
		                                             String.format("Invalid address size: %s", getData().getValue()))); //$NON-NLS-1$
		                }
		                drm.done();
		            }
		        });
		}
		return;
	}

	/**
	 * Read endianness.
	 *
	 * @param memContext the mem context
	 * @param drm the drm
	 */
	protected void readEndianness(IMemoryDMContext memContext, final DataRequestMonitor<Boolean> drm) {
		CommandFactory commandFactory = fCommandControl.getCommandFactory();
		fCommandControl.queueCommand(commandFactory.createCLIShowEndian(memContext),
		new DataRequestMonitor<CLIShowEndianInfo>(ImmediateExecutor.getInstance(), drm) {
			@Override
			protected void handleSuccess() {
				Boolean data = Boolean.valueOf(getData().isBigEndian());
				{
					GdbActivator.log("readEndianness() " + data); //$NON-NLS-1$
				}
				drm.setData(data);
				drm.done();
			}
		});
	}

	// ------------------------------------------------------------------------

	@Override
	public void getMemory(IMemoryDMContext memoryDMC, IAddress address, long offset, int word_size, int word_count,
	                      DataRequestMonitor<MemoryByte[]> drm) {

		if (memoryDMC == null) {
			drm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, INTERNAL_ERROR, "Unknown context type", null)); //$NON-NLS-1$ );
			drm.done();
			return;
		}

		if (word_size < 1) {
			drm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, NOT_SUPPORTED, "Word size not supported (< 1)", //$NON-NLS-1$
			                         null));
			drm.done();
			return;
		}

		if (word_count < 0) {
			drm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, IDsfStatusConstants.INTERNAL_ERROR,
			                         "Invalid word count (< 0)", null)); //$NON-NLS-1$
			drm.done();
			return;
		}

		{
			GdbActivator.log(String.format("readMemoryBlock 0x%s+0x%X 0x%X", address.toString(16), offset, //$NON-NLS-1$
			                               word_count * word_size));
		}

		flushCache(memoryDMC);
		readMemoryBlock(memoryDMC, address, offset, 1, word_count * word_size, drm);
	}

	@Override
	public void setMemory(IMemoryDMContext memoryDMC, IAddress address, long offset, int word_size, int word_count,
	                      byte[] buffer, RequestMonitor rm) {

		if (memoryDMC == null) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, INTERNAL_ERROR, "Unknown context type", null)); //$NON-NLS-1$ );
			rm.done();
			return;
		}

		if (word_size < 1) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, NOT_SUPPORTED, "Word size not supported (< 1)", //$NON-NLS-1$
			                        null));
			rm.done();
			return;
		}

		if (word_count < 0) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, IDsfStatusConstants.INTERNAL_ERROR,
			                        "Invalid word count (< 0)", null)); //$NON-NLS-1$
			rm.done();
			return;
		}

		if (buffer.length < word_count * word_size) {
			rm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, IDsfStatusConstants.INTERNAL_ERROR,
			                        "Buffer too short", null)); //$NON-NLS-1$
			rm.done();
			return;
		}

		{
			// System.out.print("writeMemoryBlock 0x");
			// for (int i=0; i < buffer.length; ++i){
			// System.out.print(String.format(" %02X", buffer[i]));
			// }
			// System.out.print(" 0x");
			GdbActivator.log(String.format("writeMemoryBlock 0x%s+0x%X 0x%X", address.toString(16), offset, //$NON-NLS-1$
			                               word_count * word_size));
		}

		flushCache(memoryDMC);
		writeMemoryBlock(memoryDMC, address, offset, 1, word_count * word_size, buffer, rm);

		// TODO: maybe notify MemoryChangedEvent
	}

	// ------------------------------------------------------------------------
}
