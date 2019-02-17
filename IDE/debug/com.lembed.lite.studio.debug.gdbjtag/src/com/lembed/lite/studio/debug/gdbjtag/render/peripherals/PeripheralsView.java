
package com.lembed.lite.studio.debug.gdbjtag.render.peripherals;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.core.SystemUIJob;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.IMemoryBlockListener;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.internal.ui.viewers.model.provisional.TreeModelViewer;
import org.eclipse.debug.internal.ui.views.variables.VariablesView;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.progress.UIJob;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.memory.MemoryBlockMonitor;
import com.lembed.lite.studio.debug.gdbjtag.memory.PeripheralMemoryBlockExtension;

/**
 * The Class PeripheralsView.
 */
@SuppressWarnings("restriction")
public class PeripheralsView extends VariablesView implements IMemoryBlockListener, IDebugEventSetListener {

	// ------------------------------------------------------------------------

	/** The Constant PRESENTATION_CONTEXT_ID. */
	public final static String PRESENTATION_CONTEXT_ID = "PeripheralsView"; //$NON-NLS-1$

	private UIJob fRefreshUIjob = new SystemUIJob(PeripheralsView.class.getSimpleName() + "#refreshUIjob") { //$NON-NLS-1$

		@Override
        public IStatus runInUIThread(IProgressMonitor pm) {

			Viewer viewer = getViewer();
			if (viewer != null) {
				viewer.refresh();
			}
			return Status.OK_STATUS;
		}
	};

	// ------------------------------------------------------------------------

	Set<PeripheralMemoryBlockExtension> fMemoryBlocks;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripherals view.
	 */
	public PeripheralsView() {

		GdbActivator.log("PeripheralsView()"); //$NON-NLS-1$
		fMemoryBlocks = new HashSet<>();
	}

	@Override
    protected String getPresentationContextId() {
		return PRESENTATION_CONTEXT_ID;
	}

	@Override
    protected int getViewerStyle() {
		return SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.TITLE | SWT.VIRTUAL | SWT.FULL_SELECTION;
	}

	private void addDebugEventListener() {
		DebugPlugin.getDefault().addDebugEventListener(this);
	}

	private void removeDebugEventListener() {
		DebugPlugin.getDefault().removeDebugEventListener(this);
	}

	// Contributed by IDebugEventSetListener

	@Override
	public void handleDebugEvents(DebugEvent[] events) {

		GdbActivator.log("PeripheralsView.handleDebugEvents() " + events.length); //$NON-NLS-1$
		for (int i = 0; i < events.length; ++i) {
			GdbActivator.log(" " + events[i]); //$NON-NLS-1$
		}

		for (int i = 0; i < events.length; ++i) {

			// Currently there are no special events to be processed here, only
			// cleanup actions are handled, the MODEL_SPECIFIC is no yet used.
			if (events[i].getKind() == DebugEvent.TERMINATE) {

				// Clear possible messages.
				EclipseUtils.clearStatusMessage();

			} else if (events[i].getKind() == DebugEvent.MODEL_SPECIFIC) {
				// Currently no longer fired
				Object object = events[i].getSource();
				if ((object instanceof PeripheralMemoryBlockExtension)) {
					refresh();
				}
			}
		}
	}

	private void addMemoryBlockListener() {
		DebugPlugin.getDefault().getMemoryBlockManager().addListener(this);
	}

	private void removeMemoryBlockListener() {
		DebugPlugin.getDefault().getMemoryBlockManager().removeListener(this);
	}

	// Contributed by IMemoryBlockListener

	@Override
	public void memoryBlocksAdded(IMemoryBlock[] memoryBlocks) {

		// These are notifications that the memory blocks were already added
		for (int i = 0; i < memoryBlocks.length; i++) {
			if ((memoryBlocks[i] instanceof PeripheralMemoryBlockExtension)) {

				PeripheralMemoryBlockExtension memoryBlockExtension = (PeripheralMemoryBlockExtension) memoryBlocks[i];
				
				GdbActivator.log("PeripheralsView.memoryBlocksAdded() [] " + memoryBlockExtension); //$NON-NLS-1$
		
				fMemoryBlocks.add(memoryBlockExtension);
			}
		}
	}

	// Contributed by IMemoryBlockListener

	@Override
	public void memoryBlocksRemoved(IMemoryBlock[] memoryBlocks) {

		// These are notifications that the memory blocks were already removed
		for (int i = 0; i < memoryBlocks.length; i++) {
			if ((memoryBlocks[i] instanceof PeripheralMemoryBlockExtension)) {

				PeripheralMemoryBlockExtension memoryBlockExtension = (PeripheralMemoryBlockExtension) memoryBlocks[i];
				
				GdbActivator.log("PeripheralsView.memoryBlocksRemoved() [] " + memoryBlockExtension); //$NON-NLS-1$
		
				fMemoryBlocks.remove(memoryBlockExtension);
			}
		}

		// Update the check box status when the memory block is removed from the
		// monitor view.
		refresh();
	}


	@Override
	public Viewer createViewer(Composite composite) {

		GdbActivator.log("PeripheralsView.createViewer()"); //$NON-NLS-1$

		TreeModelViewer viewer = (TreeModelViewer) super.createViewer(composite);

		addMemoryBlockListener();
		addDebugEventListener();

		initStates(getMemento());

		return viewer;
	}

	@Override
	public void dispose() {

		GdbActivator.log("PeripheralsView.dispose()"); //$NON-NLS-1$

		if (!fMemoryBlocks.isEmpty()) {

			IMemoryBlock[] memoryBlocks = fMemoryBlocks.toArray(new IMemoryBlock[fMemoryBlocks.size()]);

			// Save open monitors
			MemoryBlockMonitor.getInstance().savePeripheralNames(memoryBlocks);
			// Remove all peripheral monitors
			MemoryBlockMonitor.getInstance().removeMemoryBlocks(memoryBlocks);
		}

		// Confirm that all peripheral monitors were removed
		assert (fMemoryBlocks.isEmpty());

		removeDebugEventListener();
		removeMemoryBlockListener();

		super.dispose();
	}

	private void refresh() {
		GdbActivator.log("PeripheralsView.refresh()"); //$NON-NLS-1$
		fRefreshUIjob.schedule();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object getAdapter(Class required) {
		return super.getAdapter(required);
	}


	/**
	 * Inits the states.
	 *
	 * @param memento the memento
	 */
	private void initStates(IMemento memento) {
		
	}

	@Override
    public void saveState(IMemento memento) {
		super.saveState(memento);
	}

	@Override
    public void saveViewerState(IMemento memento) {
		super.saveViewerState(memento);
	}

}
