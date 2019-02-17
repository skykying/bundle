/*******************************************************************************
 * Copyright (c) 2014 Liviu Ionescu.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Liviu Ionescu - initial version
 *     		(many thanks to Code Red for providing the inspiration)
 *******************************************************************************/

package com.lembed.lite.studio.debug.gdbjtag.memory;

import com.lembed.lite.studio.core.SystemJob;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import org.eclipse.cdt.core.IAddress;
import org.eclipse.cdt.core.IAddressFactory;
import org.eclipse.cdt.dsf.concurrent.CountingRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.DsfExecutor;
import org.eclipse.cdt.dsf.concurrent.Query;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.debug.internal.provisional.model.IMemoryBlockUpdatePolicyProvider;
import org.eclipse.cdt.dsf.debug.model.DsfMemoryBlockRetrieval;
import org.eclipse.cdt.dsf.debug.service.IMemory;
import org.eclipse.cdt.dsf.debug.service.IMemory.IMemoryDMContext;
import org.eclipse.cdt.dsf.debug.service.IRunControl;
import org.eclipse.cdt.dsf.mi.service.IMICommandControl;
import org.eclipse.cdt.dsf.mi.service.command.CommandFactory;
import org.eclipse.cdt.dsf.service.DsfServiceEventHandler;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.utils.Addr32Factory;
import org.eclipse.cdt.utils.Addr64Factory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlockExtension;
import org.eclipse.debug.core.model.IMemoryBlockRetrieval;
import org.eclipse.debug.core.model.MemoryByte;
import org.osgi.framework.Filter;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.PeripheralDMContext;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.PeripheralDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdPeripheralDMNode;
import com.lembed.lite.studio.debug.gdbjtag.dsf.GnuArmCommandFactory;
import com.lembed.lite.studio.debug.gdbjtag.services.IPeripheralMemoryService;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralGroupVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralRegisterFieldVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralRegisterVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralTopVMNode;
import com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripheral.PeripheralTreeVMNode;

/**
 * The Class PeripheralMemoryBlockExtension.
 */
@SuppressWarnings("restriction")
public class PeripheralMemoryBlockExtension extends PlatformObject
		implements IMemoryBlockExtension, IMemoryBlockUpdatePolicyProvider, IDebugEventSetListener {

	// ------------------------------------------------------------------------

	private PeripheralDMNode fPeripheralDMNode;
	
	/** The command factory. */
	//private GnuArmCommandFactory fCommandFactory;
	
	/** The command control. */
	//private ICommandControl fCommandControl;
	private DsfMemoryBlockRetrieval fRetrieval;
	private IMemory.IMemoryDMContext fMemoryDMContext;
	private String fBlockDisplayName;
	private String fModelId;
	private PeripheralTopVMNode fPeripheralTop;
	private ArrayList<Object> fConnections;
	private SystemJob fUpdatePeripheralRenderingJob;
	private PeripheralDMContext fPeripheralDMContext;
	private IPeripheralMemoryService fMemoryService;
	private boolean fIsBigEndian;

	private int fAddressSize = 4;
	private IAddressFactory fAddressFactory;

	private List<PeripheralMemoryRegion> fReadableMemoryRegions;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripheral memory block extension.
	 *
	 * @param memoryBlockRetrieval the memory block retrieval
	 * @param memoryDMContext the memory DM context
	 * @param modelId the model id
	 * @param peripheralDMContext the peripheral DM context
	 */
	public PeripheralMemoryBlockExtension(DsfMemoryBlockRetrieval memoryBlockRetrieval,
			IMemoryDMContext memoryDMContext, String modelId, final PeripheralDMContext peripheralDMContext) {

		
		GdbActivator.log("PeripheralMemoryBlockExtension()"); //$NON-NLS-1$
		
		// Used in scheduleUpdatePeripheralRendering()
		fUpdatePeripheralRenderingJob = new SystemJob("Update peripheral rendering") { //$NON-NLS-1$

			@Override
            protected IStatus run(IProgressMonitor pm) {
				if (fPeripheralTop == null) {
					return Status.OK_STATUS;
				}
				
				if (fReadableMemoryRegions == null) {
					return Status.OK_STATUS;
				}

				try {
					// Read all register values from the device and update
					// rendering
					updatePeripheralRenderingValues();

					// Notify world that this block possibly changed
					
					GdbActivator.log("PeripheralMemoryBlockExtension " + fBlockDisplayName + " fireDebugEventSet(changed)"); //$NON-NLS-1$ //$NON-NLS-2$
					
					DebugPlugin.getDefault().fireDebugEventSet(
							new DebugEvent[] { new DebugEvent(PeripheralMemoryBlockExtension.this,
									DebugEvent.CHANGE, DebugEvent.CONTENT) });
				} catch (NullPointerException e) {
					// Added because of an error report, but obvious no cause
					// was identified yet.
					GdbActivator.log(e);
					return new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "Update peripheral rendering failed", e); //$NON-NLS-1$
				}
				return Status.OK_STATUS;
			}
		};

		fPeripheralDMContext = peripheralDMContext;
		fConnections = new ArrayList<>();

		fRetrieval = memoryBlockRetrieval;
		fMemoryDMContext = memoryDMContext;

		// The expression is the peripheral name, visible in the Monitors view
		fBlockDisplayName = peripheralDMContext.getName();

		fModelId = modelId;

		String sessionId = memoryBlockRetrieval.getSession().getId();
		final DsfServicesTracker tracker = new DsfServicesTracker(
				GdbActivator.getInstance().getBundle().getBundleContext(), sessionId);

		@SuppressWarnings("rawtypes")
		Query query = new Query() {

			@Override
			protected void execute(DataRequestMonitor rm) {

				//fCommandControl = tracker.getService(ICommandControl.class);
				IMICommandControl miCmdCtl = tracker.getService(IMICommandControl.class);
				if(miCmdCtl != null) {
				    CommandFactory commandFactory = miCmdCtl.getCommandFactory();
    				if(commandFactory != null) {
    	                if (commandFactory instanceof GnuArmCommandFactory) {
    	                    //fCommandFactory = (GnuArmCommandFactory) commandFactory;
    	                    fPeripheralDMNode = peripheralDMContext.getPeripheralInstance();
    	                    fPeripheralDMNode.setMemoryBlock(PeripheralMemoryBlockExtension.this);
    	                } else {
    	                    GdbActivator.log("Error: unknown command factory:" + commandFactory.getClass().getSimpleName()); //$NON-NLS-1$
    	                }
    				}
				}

				// Get the memory service.
				fMemoryService = tracker.getService(IPeripheralMemoryService.class);

				if (fMemoryService == null) {
					GdbActivator.log("Error: cannot get IPeripheralMemoryService"); //$NON-NLS-1$
				}

				rm.done();
			}

		};
		((PeripheralMemoryBlockRetrieval) getMemoryBlockRetrieval()).getExecutor().execute(query);
		try {
			query.get();
			GdbActivator.log("memory service data initialised"); //$NON-NLS-1$
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 

		// Decide what size is the address
		fIsBigEndian = fMemoryService.isBigEndian(fMemoryDMContext);
		fAddressSize = fMemoryService.getAddressSize(fMemoryDMContext);
		if (fAddressSize <= 4) {
			fAddressFactory = new Addr32Factory();
		} else {
			fAddressFactory = new Addr64Factory();
		}

		fPeripheralTop = createPeripheralGroupNode();
		if (fPeripheralTop == null) {
			GdbActivator.log("Cannot create peripheral group " + fPeripheralDMNode.getName()); //$NON-NLS-1$
		}

		// Parse all registers and create a map of readable registers.
		fReadableMemoryRegions = createRegionsList();

		scheduleUpdatePeripheralRendering();

		addDebugEventListeners();
		tracker.dispose();
	}

	@Override
	public void dispose() throws DebugException {

		
		GdbActivator.log("PeripheralMemoryBlockExtension.dispose()"); //$NON-NLS-1$
		removeDebugEventListeners();
		//fCommandControl = null;
		//fCommandFactory = null;
		fMemoryDMContext = null;
		fBlockDisplayName = null;
		if (fPeripheralTop != null) {
			// Warning: since it is the same as the node in the Peripherals
			// view, the peripheral itself is not disposed, only its children.
			fPeripheralTop.dispose();

			fPeripheralTop = null;
		}

		fPeripheralDMContext.getPeripheralInstance().setMemoryBlock(null);
		fReadableMemoryRegions = null;
		fAddressFactory = null;
	}

	// ------------------------------------------------------------------------

	private void addDebugEventListeners() {

		try {
			fRetrieval.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// Also add this memory block to the session notifier
					fRetrieval.getSession().addServiceEventListener(PeripheralMemoryBlockExtension.this,
							(Filter) null);
				}
			});
		} catch (RejectedExecutionException e) {
			GdbActivator.log(e);
		}

		// Add this memory block to the global notifier
		DebugPlugin.getDefault().addDebugEventListener(this);
	}

	private void removeDebugEventListeners() {

		// Remove this memory block from the global notifier
		DebugPlugin.getDefault().removeDebugEventListener(this);
		try {
			fRetrieval.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// Also remove this memory block from the session notifier
					fRetrieval.getSession().removeServiceEventListener(PeripheralMemoryBlockExtension.this);
				}
			});
		} catch (RejectedExecutionException e) {
			GdbActivator.log(e);
		}
	}

	/**
	 * DSF events are directed here.
	 *
	 * @param event IMemoryChangedEvent
	 */
	@DsfServiceEventHandler
	public void eventDispatched(IMemory.IMemoryChangedEvent event) {

		
		GdbActivator.log("PeripheralMemoryBlockExtension.eventDispatched(IMemoryChangedEvent) " + fBlockDisplayName //$NON-NLS-1$
					+ " " + event); //$NON-NLS-1$
		
		if (event.getDMContext().equals(fMemoryDMContext)) {
			IAddress[] addresses = event.getAddresses();
			for (int i = 0; i < addresses.length; ++i) {
				handleMemoryChange(addresses[i].getValue());
			}
		}
	}

	/**
	 * Event dispatched.
	 *
	 * @param event the event
	 */
	@DsfServiceEventHandler
	public void eventDispatched(IRunControl.ISuspendedDMEvent event) {

		GdbActivator.log("PeripheralMemoryBlockExtension.eventDispatched(ISuspendedDMEvent) " + fBlockDisplayName //$NON-NLS-1$
					+ " " + event); //$NON-NLS-1$

		// Each time execution is suspended, the peripheral monitors are
		// updated.
		scheduleUpdatePeripheralRendering();

		// handleMemoryChange(BigInteger.ZERO);
	}

	/**
	 * Handle memory change.
	 *
	 * @param bigInteger the big integer
	 */
	public void handleMemoryChange(BigInteger bigInteger) {

		GdbActivator.log("PeripheralMemoryBlockExtension.handleMemoryChange() 0x" + bigInteger.toString(16) + " not used"); //$NON-NLS-1$ //$NON-NLS-2$
	
		// updatePeripheralRegisters();
	}

	private void scheduleUpdatePeripheralRendering() {

		if (fUpdatePeripheralRenderingJob != null) {
			fUpdatePeripheralRenderingJob.schedule();
		}
	}

	// ------------------------------------------------------------------------
	// Contributed by IDebugEventSetListener

	@Override
	public void handleDebugEvents(DebugEvent[] events) {

		GdbActivator.log("PeripheralMemoryBlockExtension.handleDebugEvents() " + fBlockDisplayName + " " + events.length); //$NON-NLS-1$ //$NON-NLS-2$
		for (int i = 0; i < events.length; ++i) {
			GdbActivator.log(" " + events[i]); //$NON-NLS-1$
		}

		for (int i = 0; i < events.length; ++i) {
			Object object = events[i].getSource();
			if (events[i].getKind() == DebugEvent.TERMINATE) {

				// Do not remove monitors here, since they are saved by the
				// monitor
			} else if ((events[i].getKind() == DebugEvent.MODEL_SPECIFIC)
					|| !(object instanceof PeripheralMemoryBlockExtension)) {
				// Currently no longer fired
			}
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Create a peripheral group node for the current peripheral. Children nodes
	 * will be added later when getChildren() is called.
	 */
	private PeripheralTopVMNode createPeripheralGroupNode() {

		SvdPeripheralDMNode svdNode = fPeripheralDMContext.getPeripheralInstance();

		PeripheralTopVMNode node;
		node = new PeripheralTopVMNode(null, svdNode, this); // Root node

		return node;
	}

	/**
	 * Parse all registers and create a sorted list of readable memory regions. For
	 * unions, the registers may overlap.
	 * <p>
	 * Each region has a list of registers stored in the region.
	 *
	 * @return the list.
	 */
	private List<PeripheralMemoryRegion> createRegionsList() {

		List<PeripheralMemoryRegion> list = new LinkedList<>();

		// Get all registers of the peripheral, in svd definition order.
		collectRegistersRecursive(fPeripheralTop, list);

		PeripheralMemoryRegion[] array = list.toArray(new PeripheralMemoryRegion[list.size()]);
		// Sort by offset. Beware of duplicates (in case of unions).
		Arrays.sort(array);

		// Create an empty list of regions.
		list = new LinkedList<>();

		for (int i = 0; i < array.length;) {

			// Add register as region begin.
			list.add(array[i]);

			int j;
			// Iterate next registers until a gap is found.
			for (j = i + 1; j < array.length; ++j) {
				// If possible, concatenate contiguous or contained register
				// regions to a larger region.
				if (array[i].isContiguous(array[j])) {
					array[i].concatenate(array[j]);
				} else {
					i = j;
					break;
				}
			}

			// Check if end of peripheral address space reached.
			if (j == array.length) {
				break;
			}
		}

		return list;
	}

	/**
	 * Contribute to the peripheral memory map the registers that can be read.
	 * <p>
	 * For peripheral and cluster node, descend the hierarchy.
	 *
	 * @param node
	 *            the current PeripheralTreeVMNode.
	 * @param list
	 *            the list of regions.
	 */
	private void collectRegistersRecursive(PeripheralTreeVMNode node, List<PeripheralMemoryRegion> list) {

		// This should match both simple registers and register array elements.
		if ((node instanceof PeripheralRegisterVMNode) && node.isReadAllowed()) {

			// Register found, create a small region to cover only the register.
			PeripheralMemoryRegion region = new PeripheralMemoryRegion(node.getPeripheralBigAddressOffset().longValue(),
					node.getBigSize().longValue());
			region.addNode((PeripheralRegisterVMNode) node);

			// Contribute to the output list; SVD order, to be ordered later.
			list.add(region);

			return;

		} else if (node instanceof PeripheralRegisterFieldVMNode) {

			// For just in case, the above return should prevent recursion to
			// reach here.
			return;
		}

		if (node.hasChildren()) {

			// Mainly for cluster nodes, to reach inner registers.
			for (int i = 0; i < node.getChildren().length; ++i) {
				PeripheralTreeVMNode child = (PeripheralTreeVMNode) node.getChildren()[i];
				collectRegistersRecursive(child, list);
			}
		}
	}

	/**
	 * Read the memory content and store the byte arrays in the regions nodes.
	 */
	private void readPeripheralMemoryRegions(RequestMonitor rm) {

		GdbActivator.log("PeripheralMemoryBlockExtension.readPeripheralMemoryRegions() " + fBlockDisplayName); //$NON-NLS-1$
		
		IAddress address = getAddressFactory().createAddress(fPeripheralTop.getBigAbsoluteAddress());

		DsfExecutor executor = ((PeripheralMemoryBlockRetrieval) getMemoryBlockRetrieval()).getExecutor();
		final CountingRequestMonitor countingRm = new CountingRequestMonitor(executor, rm);

		if (fReadableMemoryRegions == null) {
			return;
		}

		// Initialise the counting monitor with the number of memory regions.
		countingRm.setDoneCount(fReadableMemoryRegions.size());

		for (final PeripheralMemoryRegion region : fReadableMemoryRegions) {

			DataRequestMonitor<MemoryByte[]> drm = new DataRequestMonitor<MemoryByte[]>(executor, countingRm) {

				@Override
				protected void handleCompleted() {

					// Transfer the result to the region object.
					region.setBytes(getData());

					// Among other things, call parent done(), which will
					// decrement the counting monitor.
					super.handleCompleted();
				}
			};

			// Read memory region in
			fMemoryService.getMemory(fMemoryDMContext, address, region.getAddressOffset(), 1,
					(int) region.getSizeBytes(), drm);
		}
	}

	/**
	 * Update peripheral rendering values.
	 */
	public void updatePeripheralRenderingValues() {

		@SuppressWarnings("rawtypes")
		Query query = new Query() {

			@Override
			protected void execute(DataRequestMonitor rm) {

				readPeripheralMemoryRegions(rm);
			}
		};

		((PeripheralMemoryBlockRetrieval) getMemoryBlockRetrieval()).getExecutor().execute(query);

		try {
			query.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 

		if (fReadableMemoryRegions == null) {
			return;
		}

		// Iterate all regions.
		for (PeripheralMemoryRegion region : fReadableMemoryRegions) {

			MemoryByte[] regionBytes = region.getBytes();
			long regionOffset = region.getAddressOffset();

			// Iterate all registers in a region.
			for (PeripheralRegisterVMNode node : region.getNodes()) {

				long nodeOffset = node.getPeripheralBigAddressOffset().longValue();
				int byteOffset = (int) (nodeOffset - regionOffset);

				int widthBytes = node.getWidthBytes();
				MemoryByte[] bytes = new MemoryByte[widthBytes];
				for (int i = 0; i < widthBytes; ++i) {
					bytes[i] = regionBytes[byteOffset + i];
				}

				BigInteger value = prepareBigIntegerFromByteArray(bytes);
				// Works without problems for unions
				node.setValue(value);
			}
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Convert the big value to a fixed size byte array, using the device
	 * endianness.
	 *
	 * @param value
	 *            the BigInteger value.
	 * @param sizeBytes
	 *            the new byte array size.
	 * @return a byte array with the value bytes.
	 */
	private byte[] prepareByteArrayFromBigInteger(BigInteger value, int sizeBytes) {

		byte buf[] = new byte[sizeBytes];
		byte valueBuf[] = value.toByteArray();

		// Pad (if too short), truncate (if too long)
		if (fIsBigEndian) {
			for (int i = 0; i < buf.length; ++i) {
				buf[buf.length - i - 1] = (i < valueBuf.length) ? valueBuf[valueBuf.length - i - 1] : 0;
			}
		} else {
			// Reverse order
			for (int i = 0; i < buf.length; ++i) {
				buf[i] = (i < valueBuf.length) ? valueBuf[valueBuf.length - i - 1] : 0;
			}
		}
		return buf;
	}

	/**
	 * Convert the special byte array to a BigInteger, using the device endianness.
	 *
	 * @param bytes
	 * @return
	 */
	private BigInteger prepareBigIntegerFromByteArray(MemoryByte[] bytes) {

		if (bytes == null || bytes.length == 0) {
			return BigInteger.ZERO;
		}

		byte buf[] = new byte[bytes.length];

		if (fIsBigEndian) {
			for (int i = 0; i < buf.length; ++i) {
				buf[i] = bytes[i].getValue();
			}
		} else {
			// Reverse order
			for (int i = 0; i < buf.length; ++i) {
				buf[buf.length - i - 1] = bytes[i].getValue();
			}
		}

		// Force always positive
		BigInteger value = new BigInteger(1, buf);
		return value;
	}

	/**
	 * Write a peripheral register to the device. The peripheral address is
	 * retrieved from the peripheral group node.
	 *
	 * @param offset
	 *            the register address offset from the peripheral absolute address.
	 * @param sizeBytes
	 *            the register size.
	 * @param value
	 *            the new register value.
	 */
	public void writePeripheralRegister(final long offset, final int sizeBytes, final BigInteger value) {

		@SuppressWarnings("rawtypes")
		Query query = new Query() {

			@Override
			protected void execute(DataRequestMonitor rm) {

				IAddress address = getAddressFactory().createAddress(fPeripheralTop.getBigAbsoluteAddress());

				byte buf[] = prepareByteArrayFromBigInteger(value, sizeBytes);

				fMemoryService.setMemory(fMemoryDMContext, address, offset, sizeBytes, 1, buf, rm);
			}
		};

		((PeripheralMemoryBlockRetrieval) getMemoryBlockRetrieval()).getExecutor().execute(query);
		try {
			query.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Read a peripheral register from the device. The peripheral address is
	 * retrieved from the peripheral group node.
	 *
	 * @param offset
	 *            the register address offset from the peripheral absolute address.
	 * @param sizeBytes
	 *            the register size.
	 * @return a BigInteger with the register value.
	 */
	public BigInteger readPeripheralRegister(final long offset, final int sizeBytes) {

		Query<MemoryByte[]> query = new Query<MemoryByte[]>() {

			@Override
			protected void execute(DataRequestMonitor<MemoryByte[]> drm) {

				IAddress address = getAddressFactory().createAddress(fPeripheralTop.getBigAbsoluteAddress());

				fMemoryService.getMemory(fMemoryDMContext, address, offset, sizeBytes, 1, drm);
			}
		};

		((PeripheralMemoryBlockRetrieval) getMemoryBlockRetrieval()).getExecutor().execute(query);

		BigInteger value = BigInteger.ZERO;
		try {
			MemoryByte bytes[];
			bytes = query.get();
			// System.out.print("Read 0x");
			// for (int i = 0; i < bytes.length; ++i) {
			// System.out.print(String.format(" %02X", bytes[i].getValue()));
			// }
			// GdbActivator.log(" @ 0x" + String.format("%X", offset));
			value = prepareBigIntegerFromByteArray(bytes);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} 

		return value;
	}

	// ------------------------------------------------------------------------

	/**
	 * Gets the address factory.
	 *
	 * @return the address factory
	 */
	public IAddressFactory getAddressFactory() {
		return fAddressFactory;
	}

	/**
	 * Gets the peripheral register group.
	 *
	 * @return the peripheral register group
	 */
	public PeripheralGroupVMNode getPeripheralRegisterGroup() {
		return fPeripheralTop;
	}

	/**
	 * Gets the peripheral instance.
	 *
	 * @return the peripheral instance
	 */
	public PeripheralDMNode getPeripheralInstance() {
		return fPeripheralDMNode;
	}

	// ------------------------------------------------------------------------

	// Contributed by IMemoryBlock

	@Override
	public long getStartAddress() {
		return fPeripheralDMNode.getBigAbsoluteAddress().longValue();
	}

	@Override
	public long getLength() {
		try {
			return getBigLength().longValue();
		} catch (DebugException e) {
			return 0;
		}
	}

	@Override
	public byte[] getBytes() throws DebugException {
		return null;
	}

	@Override
	public boolean supportsValueModification() {
		return false;
	}

	@Override
	public void setValue(long offset, byte[] bytes) throws DebugException {
		
	}

	// Contributed by IDebugElement

	@Override
	public String getModelIdentifier() {
		return fModelId;
	}

	@Override
	public IDebugTarget getDebugTarget() {
		return fRetrieval.getDebugTarget();
	}

	@Override
	public ILaunch getLaunch() {
		return fRetrieval.getLaunch();
	}

	// Contributed by IMemoryBlockExtension

	@Override
	public String getExpression() {
		return fBlockDisplayName;
	}

	@Override
	public BigInteger getBigBaseAddress() throws DebugException {
		return fPeripheralDMNode.getBigAbsoluteAddress();
	}

	@Override
	public BigInteger getMemoryBlockStartAddress() throws DebugException {
		return fPeripheralDMNode.getBigAbsoluteAddress();
	}

	@Override
	public BigInteger getMemoryBlockEndAddress() throws DebugException {
		return (fPeripheralDMNode.getBigAbsoluteAddress()).add(getBigLength()).subtract(BigInteger.ONE);
	}

	@Override
	public BigInteger getBigLength() throws DebugException {
		return fPeripheralDMNode.getBigSizeBytes();
	}

	@Override
	public int getAddressSize() throws DebugException {
		return fAddressSize;
	}

	@Override
	public boolean supportBaseAddressModification() throws DebugException {
		return false;
	}

	@Override
	public boolean supportsChangeManagement() {
		return false;
	}

	@Override
	public void setBaseAddress(BigInteger address) throws DebugException {
		
	}

	@Override
	public MemoryByte[] getBytesFromOffset(BigInteger unitOffset, long addressableUnits) throws DebugException {

		// Fake content
		MemoryByte[] bytes = new MemoryByte[(int) addressableUnits];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = new MemoryByte((byte) 0, (byte) 0);
		}
		return bytes;
	}

	@Override
	public MemoryByte[] getBytesFromAddress(BigInteger address, long units) throws DebugException {

		// Fake content
		MemoryByte[] bytes = new MemoryByte[(int) units];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = new MemoryByte((byte) 0, (byte) 0);
		}
		return bytes;
	}

	@Override
	public void setValue(BigInteger offset, byte[] bytes) throws DebugException {
		
	}

	@Override
	public void connect(Object client) {
		
	}

	@Override
	public void disconnect(Object client) {
		
	}

	@Override
	public Object[] getConnections() {
		return fConnections.toArray();
	}

	@Override
	public IMemoryBlockRetrieval getMemoryBlockRetrieval() {
		return fRetrieval;
	}

	@Override
	public int getAddressableSize() throws DebugException {
		return 1;
	}

	// ------------------------------------------------------------------------
	// Contributed by IMemoryBlockUpdatePolicyProvider

	@Override
	public String[] getUpdatePolicies() {
		return null;
	}

	@Override
	public String getUpdatePolicyDescription(String id) {
		return null;
	}

	@Override
	public String getUpdatePolicy() {
		return null;
	}

	@Override
	public void setUpdatePolicy(String id) {
		
	}

	@Override
	public void clearCache() {
	}
	

	// ------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
    @Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {

		if (adapter.equals(IMemoryBlockRetrieval.class))
			return getMemoryBlockRetrieval();

		return super.getAdapter(adapter);
	}

	// ------------------------------------------------------------------------

	@Override
    public String toString() {
		return "[" + getClass().getSimpleName() + " " + getExpression() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	// ------------------------------------------------------------------------
}
