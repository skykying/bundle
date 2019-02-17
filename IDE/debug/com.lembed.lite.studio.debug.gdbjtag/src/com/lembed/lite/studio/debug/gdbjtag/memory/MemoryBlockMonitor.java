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

import com.lembed.lite.studio.core.EclipseUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IMemoryBlockRetrieval;
import org.eclipse.debug.core.model.IMemoryBlockRetrievalExtension;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.memory.IMemoryRendering;
import org.eclipse.debug.ui.memory.IMemoryRenderingContainer;
import org.eclipse.debug.ui.memory.IMemoryRenderingSite;
import org.eclipse.debug.ui.memory.IMemoryRenderingType;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.PeripheralDMContext;
import com.lembed.lite.studio.debug.gdbjtag.render.peripheral.PeripheralRendering;
import com.lembed.lite.studio.debug.gdbjtag.ui.Messages;

/**
 * This class manages adding/removing memory monitors. The memory monitors are
 * used to display the content of a memory area. In our case the memory areas
 * are peripheral blocks.
 * <p>
 * The PeripheralsView listens for changes, to update the view when monitors are
 * removed.
 */
public class MemoryBlockMonitor {

	// ------------------------------------------------------------------------

	// The shared instance
	private static final MemoryBlockMonitor fgInstance;

	static {
		// Use the class loader to avoid using synchronization on getInstance().
		fgInstance = new MemoryBlockMonitor();
	}

	/**
	 * Gets the single instance of MemoryBlockMonitor.
	 *
	 * @return single instance of MemoryBlockMonitor
	 */
	public static MemoryBlockMonitor getInstance() {
		return fgInstance;
	}

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new memory block monitor.
	 */
	public MemoryBlockMonitor() {
		
		GdbActivator.log("MemoryBlockMonitor()"); //$NON-NLS-1$
	}

	// ------------------------------------------------------------------------

	/**
	 * Called from UI Thread.
	 *
	 * @param workbenchWindow IWorkbenchWindow
	 * @param peripheralDMContext PeripheralDMContext
	 * @param isChecked boolean
	 */
	public void displayPeripheralMonitor(final IWorkbenchWindow workbenchWindow,
	                                     final PeripheralDMContext peripheralDMContext, final boolean isChecked) {

		
		GdbActivator.log("MemoryBlockMonitor.displayPeripheralMonitor(" + isChecked + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		
		Object object;
		object = peripheralDMContext.getAdapter(PeripheralMemoryBlockRetrieval.class);

		if ((object instanceof IMemoryBlockRetrieval)) {

			final IMemoryBlockRetrieval memoryBlockRetrieval = (IMemoryBlockRetrieval) object;

			if (isChecked) {
				addMemoryBlock(workbenchWindow, peripheralDMContext, memoryBlockRetrieval);

				// In case the Memory view was not visible, make it
				// visible now.
				showMemoryView(workbenchWindow);
			} else {
				removeMemoryBlock(workbenchWindow, peripheralDMContext);
			}
		}
	}

	/**
	 * Find memory block retrieval and save names.
	 *
	 * @param memoryBlocks IMemoryBlock Array
	 */
	public void savePeripheralNames(IMemoryBlock[] memoryBlocks) {

		
		GdbActivator.log("MemoryBlockMonitor.savePeripheralNames()"); //$NON-NLS-1$

		for (int i = 0; i < memoryBlocks.length; ++i) {
			if (memoryBlocks[i] instanceof PeripheralMemoryBlockExtension) {

				PeripheralMemoryBlockExtension memBlock = (PeripheralMemoryBlockExtension) memoryBlocks[i];

				IMemoryBlockRetrieval memRetrieval;
				memRetrieval = memBlock.getMemoryBlockRetrieval();

				if (memRetrieval instanceof PeripheralMemoryBlockRetrieval) {
					((PeripheralMemoryBlockRetrieval) memRetrieval).saveMemoryBlocks();
					break;
				}
			}
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Called by displayPeripheralMonitor() and addPersistentPeripherals() from
	 * the UI thread.
	 *
	 * @param workbenchWindow IWorkbenchWindow
	 * @param peripheralDMContext PeripheralDMContext
	 * @param memoryBlockRetrieval IMemoryBlockRetrieval
	 */
	// @SuppressWarnings("restriction")
	public void addMemoryBlock(IWorkbenchWindow workbenchWindow, PeripheralDMContext peripheralDMContext,
	                           IMemoryBlockRetrieval memoryBlockRetrieval) {

		
		GdbActivator.log("MemoryBlockMonitor.addMemoryBlock() " + peripheralDMContext.getName()); //$NON-NLS-1$
		
		String addr = peripheralDMContext.getPeripheralInstance().getHexAddress();
		try {
			if ((memoryBlockRetrieval instanceof IMemoryBlockRetrievalExtension)) {

				IMemoryBlock memoryBlockToAdd = ((IMemoryBlockRetrievalExtension) memoryBlockRetrieval)
				                                .getExtendedMemoryBlock(addr, peripheralDMContext);
				if (memoryBlockToAdd != null) {

					// Instruct the memory block manager to add the new memory
					// block.
					DebugPlugin.getDefault().getMemoryBlockManager()
					.addMemoryBlocks(new IMemoryBlock[] { memoryBlockToAdd });

					// Add a custom rendering for the memory block.
					addDefaultRenderings(workbenchWindow, memoryBlockToAdd, PeripheralRendering.ID);

				} else {
					EclipseUtils.openError(Messages.AddMemoryBlockAction_title,
					                       Messages.AddMemoryBlockAction_noMemoryBlock, null);
				}
			} else {
				GdbActivator.log("Cannot process memory block retrieval " + memoryBlockRetrieval); //$NON-NLS-1$
			}
		} catch (DebugException e) {
			EclipseUtils.openError(Messages.AddMemoryBlockAction_title, Messages.AddMemoryBlockAction_failed, e);
		} catch (NumberFormatException e) {
			String msg = Messages.AddMemoryBlockAction_failed + "\n" + Messages.AddMemoryBlockAction_input_invalid; //$NON-NLS-1$
			EclipseUtils.openError(Messages.AddMemoryBlockAction_title, msg, null);
		}
	}

	/**
	 * Removes the memory block.
	 *
	 * @param workbenchWindow the workbench window
	 * @param peripheralDMContext the peripheral DM context
	 */
	private static void removeMemoryBlock(IWorkbenchWindow workbenchWindow, PeripheralDMContext peripheralDMContext) {

		GdbActivator.log("MemoryBlockMonitor.removeMemoryBlock() " + peripheralDMContext.getName()); //$NON-NLS-1$

		IMemoryBlock[] memoryBlocks = DebugPlugin.getDefault().getMemoryBlockManager().getMemoryBlocks();
		for (IMemoryBlock memoryBlock : memoryBlocks) {

			if ((memoryBlock instanceof PeripheralMemoryBlockExtension)) {

				// The expression identifies the block (the display name).
				String expression = ((PeripheralMemoryBlockExtension) memoryBlock).getExpression();
				if (expression.equals(peripheralDMContext.getPeripheralInstance().getDisplayName())) {

					DebugPlugin.getDefault().getMemoryBlockManager()
					.removeMemoryBlocks(new IMemoryBlock[] { memoryBlock });
					// Continue, to allow it clean all possible duplicates
				}
			} else {
				GdbActivator.log("Cannot process memory block " + memoryBlock); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Removes the memory blocks.
	 *
	 * @param memoryBlocks the memory blocks
	 */
	public void removeMemoryBlocks(IMemoryBlock[] memoryBlocks) {

		DebugPlugin.getDefault().getMemoryBlockManager().removeMemoryBlocks(memoryBlocks);
	}

	/**
	 * Called from UI thread.
	 *
	 * @param workbenchWindow
	 * @param memoryBlock
	 * @param renderingId
	 */
	private static void addDefaultRenderings(IWorkbenchWindow workbenchWindow, IMemoryBlock memoryBlock, String renderingId) {
	    
	    String id = renderingId;
		if (id == null) {
		    id = ""; //$NON-NLS-1$
		}

		Object type = null;
		IMemoryRenderingType primaryType = DebugUITools.getMemoryRenderingManager()
		                                   .getPrimaryRenderingType(memoryBlock);
		if ((primaryType != null) && (id.equals(primaryType.getId()))) {
			type = primaryType;
		}
		if (type == null) {
			IMemoryRenderingType[] defaultTypes = DebugUITools.getMemoryRenderingManager()
			                                      .getDefaultRenderingTypes(memoryBlock);
			for (IMemoryRenderingType defaultType : defaultTypes) {
				
				GdbActivator.log("addDefaultRenderings() " + (defaultType.getId())); //$NON-NLS-1$
				type = defaultType;
				if (id.equals(defaultType.getId())) {
					break;
				}
			}
		}
		try {
			if (type != null) {
				createRenderingInContainer(workbenchWindow, memoryBlock, (IMemoryRenderingType) type,
				                           IDebugUIConstants.ID_RENDERING_VIEW_PANE_1);
			}
		} catch (CoreException e) {
			GdbActivator.log(e);
		}
	}

	/**
	 * Create a new rendering in the Memory view, and initialise it with the
	 * memory block.
	 *
	 * @param workbenchWindow
	 * @param memoryBlock
	 * @param memoryRenderingType
	 * @param paneId
	 * @throws CoreException
	 */
	private static void createRenderingInContainer(IWorkbenchWindow workbenchWindow, IMemoryBlock memoryBlock,
	                                        IMemoryRenderingType memoryRenderingType, String paneId) throws CoreException {

		
		GdbActivator.log(String.format("MemoryBlockMonitor.createRenderingInContainer() 0x%X", //$NON-NLS-1$
			                               memoryBlock.getStartAddress()));

		IMemoryRenderingSite site = getRenderingSite(workbenchWindow);
		if (site != null) {

			IMemoryRenderingContainer container = site.getContainer(paneId);
			IMemoryRendering rendering = memoryRenderingType.createRendering();
			if (rendering != null) {
				rendering.init(container, memoryBlock);
				container.addMemoryRendering(rendering);
			}
		}
	}

	/**
	 * Identify the rendering site of the "Memory" view.
	 *
	 * @param workbenchWindow
	 * @return the rendering site, or null if not found.
	 */
	private static IMemoryRenderingSite getRenderingSite(IWorkbenchWindow workbenchWindow) {

		if (workbenchWindow != null) {
			IViewPart viewPart = workbenchWindow.getActivePage().findView(IDebugUIConstants.ID_MEMORY_VIEW);
			return (IMemoryRenderingSite) viewPart;
		}
		return null;
	}

	/**
	 * Make the "Memory" view visible, by calling showView(). Called from the UI
	 * thread, no need to start a separate job.
	 *
	 * @param workbenchWindow IWorkbenchWindow
	 */
	public void showMemoryView(final IWorkbenchWindow workbenchWindow) {

		
		GdbActivator.log("showView(MemoryView)"); //$NON-NLS-1$
		
		Display.getDefault().asyncExec(new Runnable() {
			@Override
            public void run() {
				try {
					workbenchWindow.getActivePage().showView(IDebugUIConstants.ID_MEMORY_VIEW);
					
					GdbActivator.log("showView(MemoryView) done"); //$NON-NLS-1$
					
				} catch (PartInitException e) {
					GdbActivator.log(e);
				}
			}
		});
	}

	// ------------------------------------------------------------------------
}
