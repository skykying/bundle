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

package com.lembed.lite.studio.debug.gdbjtag.viewmodel.peripherals;

import com.lembed.lite.studio.core.EclipseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.eclipse.cdt.dsf.concurrent.ConfinedToDsfExecutor;
import org.eclipse.cdt.dsf.concurrent.DsfRunnable;
import org.eclipse.cdt.dsf.concurrent.ImmediateExecutor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.datamodel.IDMContext;
import org.eclipse.cdt.dsf.debug.service.IRunControl;
import org.eclipse.cdt.dsf.service.DsfServicesTracker;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.dsf.ui.concurrent.ViewerDataRequestMonitor;
import org.eclipse.cdt.dsf.ui.viewmodel.VMDelta;
import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.AbstractDMVMNode;
import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.AbstractDMVMProvider;
import org.eclipse.cdt.dsf.ui.viewmodel.datamodel.IDMVMContext;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.IElementPropertiesProvider;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.IPropertiesUpdate;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.LabelAttribute;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.LabelColumnInfo;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.LabelImage;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.LabelText;
import org.eclipse.cdt.dsf.ui.viewmodel.properties.PropertiesBasedLabelProvider;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.model.IMemoryBlockRetrieval;
import org.eclipse.debug.internal.ui.viewers.model.provisional.ICheckUpdate;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IChildrenUpdate;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IElementEditor;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IElementLabelProvider;
import org.eclipse.debug.internal.ui.viewers.model.provisional.ILabelUpdate;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IModelDelta;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IPresentationContext;
import org.eclipse.debug.internal.ui.viewers.model.provisional.IViewerUpdate;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.IPeripheralDMContext;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.PeripheralDMContext;
import com.lembed.lite.studio.debug.gdbjtag.memory.MemoryBlockMonitor;
import com.lembed.lite.studio.debug.gdbjtag.memory.PeripheralMemoryBlockRetrieval;
import com.lembed.lite.studio.debug.gdbjtag.render.peripherals.PeripheralsColumnPresentation;
import com.lembed.lite.studio.debug.gdbjtag.services.IPeripheralsService;

/**
 * The Class PeripheralsVMNode.
 */
@SuppressWarnings("restriction")
public class PeripheralsVMNode extends AbstractDMVMNode	implements IElementLabelProvider, IElementPropertiesProvider, IElementEditor {

	// ------------------------------------------------------------------------
	// This node uses properties to store content, and these are the names.

	/** The Constant PROPERTY_NAME. */
	public static final String PROPERTY_NAME = "prop.name"; //$NON-NLS-1$
	
	/** The Constant PROPERTY_ADDRESS. */
	public static final String PROPERTY_ADDRESS = "prop.address"; //$NON-NLS-1$
	
	/** The Constant PROPERTY_DESCRIPTION. */
	public static final String PROPERTY_DESCRIPTION = "prop.description"; //$NON-NLS-1$

	/** The Constant PROPERTY_ISSYSTEM. */
	public static final String PROPERTY_ISSYSTEM = "prop.isSystem"; //$NON-NLS-1$

	private IElementLabelProvider fLabelProvider = createLabelProvider();

	private IDMContext[] fPeripherals;

	// ------------------------------------------------------------------------

	/**
	 * Peripherals view model context. Contains a reference to the data model.
	 */
	public class PeripheralsVMContext extends AbstractDMVMNode.DMVMContext {

		/**
		 * Instantiates a new peripherals VM context.
		 *
		 * @param context the context
		 */
		protected PeripheralsVMContext(IDMContext context) {
			super(context);
			// Activator.log("PeripheralsVMContext(" + context + ")");
		}
	}

	@Override
    protected IDMVMContext createVMContext(IDMContext context) {
		return new PeripheralsVMContext(context);
	}

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripherals VM node.
	 *
	 * @param provider the provider
	 * @param session the session
	 * @param dmcClassType the dmc class type
	 */
	public PeripheralsVMNode(AbstractDMVMProvider provider, DsfSession session,
	                         Class<? extends IDMContext> dmcClassType) {
		super(provider, session, dmcClassType);
	}

	/**
	 * Instantiates a new peripherals VM node.
	 *
	 * @param provider the provider
	 * @param session the session
	 */
	public PeripheralsVMNode(AbstractDMVMProvider provider, DsfSession session) {
		super(provider, session, IPeripheralDMContext.class);
	}

	@Override
	public int getDeltaFlags(Object event) {

		if ((event instanceof IRunControl.ISuspendedDMEvent)) {
			return IModelDelta.CONTENT;
		}

		return 0;
	}

	@Override
	public void buildDelta(Object event, VMDelta parent, int nodeOffset, RequestMonitor requestMonitor) {

		if ((event instanceof IRunControl.ISuspendedDMEvent)) {
			parent.setFlags(parent.getFlags() | IModelDelta.CONTENT);
		}
		requestMonitor.done();
	}

	@Override
	public CellEditor getCellEditor(IPresentationContext context, String columnId, Object element, Composite parent) {

		return null; // No cell editor.
	}

	@Override
	public ICellModifier getCellModifier(IPresentationContext context, Object element) {

		return null; // No cell modifier.
	}

	@Override
	public void update(final IPropertiesUpdate[] updates) {

		GdbActivator.log("PeripheralsVMNode.update() properties " + this + ", " + updates.length + " objs"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		try {
			getSession().getExecutor().execute(new DsfRunnable() {

				@Override
                public void run() {
					updatePropertiesInSessionThread(updates);
				}
			});
		} catch (RejectedExecutionException e) {
			for (IPropertiesUpdate update : updates)
				handleFailedUpdate(update);
		}
	}

	@Override
	public void update(ILabelUpdate[] updates) {

		GdbActivator.log("PeripheralsVMNode.update() labels " + this + ", " + updates.length + " objs"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		{
			for (int i = 0; i < updates.length; i++) {

				ILabelUpdate update = updates[i];
				IPresentationContext presentationContext = update.getPresentationContext();
				TreePath path = update.getElementPath();
				if (((update instanceof ICheckUpdate))
				        && (Boolean.TRUE.equals(presentationContext.getProperty("org.eclipse.debug.ui.check")))) { //$NON-NLS-1$
                    boolean checked = getChecked(path, presentationContext);
                    boolean grayed = getGrayed(path, presentationContext);

                    // Update the check button
                    ((ICheckUpdate) update).setChecked(checked, grayed);
                }
			}
		}
		// Update the tree content, using the updates
		fLabelProvider.update(updates);
	}

	@Override
	protected boolean checkUpdate(IViewerUpdate update) {

		// Activator.log("PeripheralsVMNode.checkUpdate() " + this + " "
		// + update);

		// As recommended, first check parent.
		if (!super.checkUpdate(update))
			return false;

		// Currently always update, although the view content is static and
		// does not depend on debug state or other views.
		return true; // Conservative;
	}

	@Override
	protected void updateElementsInSessionThread(final IChildrenUpdate update) {

		GdbActivator.log("PeripheralsVMNode.updateElementsInSessionThread() " + this + " " + update); //$NON-NLS-1$ //$NON-NLS-2$

		DsfServicesTracker tracker = getServicesTracker();
		IPeripheralsService peripheralsService = tracker.getService(IPeripheralsService.class);
		GdbActivator.log("got service " + peripheralsService); //$NON-NLS-1$

		final IRunControl.IContainerDMContext containerDMContext = findDmcInPath(
		            update.getViewerInput(), update.getElementPath(), IRunControl.IContainerDMContext.class);

		if ((peripheralsService == null) || (containerDMContext == null)) {
			// Leave the view empty. This also happens after closing the
			// session, since service is no longer there.
			handleFailedUpdate(update);
			return;
		}

		if (fPeripherals != null) {
			// On subsequent calls, use cached values.

			GdbActivator.log("PeripheralsVMNode.updateElementsInSessionThread() use cached values"); //$NON-NLS-1$

			fillUpdateWithVMCs(update, fPeripherals);
			update.done();
			return;
		}

		Executor executor;
		executor = ImmediateExecutor.getInstance();
		// executor = getSession().getExecutor();

		// Get peripherals only on first call.
		peripheralsService.getPeripherals(containerDMContext,
		new ViewerDataRequestMonitor<IPeripheralDMContext[]>(executor, update) {

			@Override
			public void handleCompleted() {

				if (isSuccess()) {
					fPeripherals = getData();
					fillUpdateWithVMCs(update, fPeripherals);

					addPersistentPeripherals(containerDMContext);

					update.done();
				} else {
					EclipseUtils.showStatusErrorMessage(getStatus().getMessage());
					handleFailedUpdate(update);
				}
			}
		});
	}

	/**
	 * Add memory monitors for persistent peripherals.
	 *
	 * @param containerDMContext
	 */
	private void addPersistentPeripherals(final IRunControl.IContainerDMContext containerDMContext) {

		GdbActivator.log("PeripheralsVMNode.addPersistentPeripherals()"); //$NON-NLS-1$

		final List<String> persistentPeripherals = new ArrayList<>();

		Object object = containerDMContext.getAdapter(PeripheralMemoryBlockRetrieval.class);
		if (object instanceof PeripheralMemoryBlockRetrieval) {
			persistentPeripherals.addAll(((PeripheralMemoryBlockRetrieval) object).getPersistentPeripherals());
		}

		if (persistentPeripherals.isEmpty()) {
			return;
		}

		// Call addMemoryBlock() on the UI thread to avoid refresh problems.
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {

				IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IMemoryBlockRetrieval retrieval = containerDMContext
				                                  .getAdapter(PeripheralMemoryBlockRetrieval.class);

				for (String peripheralName : persistentPeripherals) {
					for (int i = 0; i < fPeripherals.length; ++i) {
						PeripheralDMContext peripheralDMContext = (PeripheralDMContext) fPeripherals[i];
						if (peripheralName.equals(peripheralDMContext.getName())) {

							GdbActivator.log(fPeripherals[i] + " must be rendered"); //$NON-NLS-1$

							MemoryBlockMonitor.getInstance().addMemoryBlock(workbenchWindow, peripheralDMContext,
							        retrieval);
						}
					}
				}

				MemoryBlockMonitor.getInstance().showMemoryView(workbenchWindow);
			}
		});

	}

	/**
	 * Update properties in session thread.
	 *
	 * @param updates the updates
	 */
	@ConfinedToDsfExecutor("getSession().getExecutor()")
	protected void updatePropertiesInSessionThread(IPropertiesUpdate[] updates) {

		GdbActivator.log("PeripheralsVMNode.updatePropertiesInSessionThread() " + this + ", " + updates.length + " objs"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		for (final IPropertiesUpdate update : updates) {

			IPeripheralDMContext peripheralDMContext = findDmcInPath(update.getViewerInput(),
			        update.getElementPath(), IPeripheralDMContext.class);

			if (peripheralDMContext == null) {
				handleFailedUpdate(update);
				return;
			}

			setProperties(update, peripheralDMContext);
			// Activator.log("updatePropertiesInSessionThread() "
			// + propertiesUpdate);
			update.done();
		}
	}

	// ------------------------------------------------------------------------

	/**
	 * Create the label provider, that will assign content to the table columns
	 * from the properties of the node.
	 *
	 * @return the label provider.
	 */
	protected IElementLabelProvider createLabelProvider() {

		PropertiesBasedLabelProvider labelProvider = new PropertiesBasedLabelProvider();

		LabelAttribute labelAttributes[];
		LabelColumnInfo labelColumnInfo;

		LabelImage labelImage = new LabelImage() {

			@Override
			public void updateAttribute(ILabelUpdate update, int columnIndex, IStatus status,
			                            Map<String, Object> properties) {

				ImageDescriptor descriptor = null;
				Boolean isSystem = (Boolean) properties.get(PROPERTY_ISSYSTEM);
				if (isSystem != null && isSystem.booleanValue()) {
					descriptor = GdbActivator.getInstance().getImageDescriptor("system_peripheral"); //$NON-NLS-1$
				} else {
					descriptor = GdbActivator.getInstance().getImageDescriptor("peripheral"); //$NON-NLS-1$
				}

				if (descriptor != null) {
					update.setImageDescriptor(descriptor, columnIndex);
				}
			}
		};

		// The PROPERTY_ISSYSTEM was added, although not used here, because
		// it needs to be referred somewhere to be available for tests in the
		// above updateAttribute().
		labelAttributes = new LabelAttribute[] {
		    new LabelText("{0}", new String[] { PROPERTY_NAME, PROPERTY_ISSYSTEM }), labelImage //$NON-NLS-1$
		};
		labelColumnInfo = new LabelColumnInfo(labelAttributes);

		// Define content for "Peripheral" column
		labelProvider.setColumnInfo(PeripheralsColumnPresentation.COLUMN_PERIPHERAL_ID, labelColumnInfo);

		labelAttributes = new LabelAttribute[] { new LabelText("{0}", new String[] { PROPERTY_ADDRESS }) }; //$NON-NLS-1$
		labelColumnInfo = new LabelColumnInfo(labelAttributes);

		// Define content for "Address" column
		labelProvider.setColumnInfo(PeripheralsColumnPresentation.COLUMN_ADDRESS_ID, labelColumnInfo);

		labelAttributes = new LabelAttribute[] { new LabelText("{0}", new String[] { PROPERTY_DESCRIPTION }) }; //$NON-NLS-1$
		labelColumnInfo = new LabelColumnInfo(labelAttributes);

		// Define content for "Description" column
		labelProvider.setColumnInfo(PeripheralsColumnPresentation.COLUMN_DESCRIPTION_ID, labelColumnInfo);

		return labelProvider;
	}

	// ------------------------------------------------------------------------

	/**
	 * Fill-in the view node properties from a data view context.
	 *
	 * @param update
	 *            the properties object.
	 * @param context
	 *            the data model context.
	 */
	protected void setProperties(IPropertiesUpdate update, IPeripheralDMContext context) {

		assert (context != null);

		update.setProperty(PROPERTY_NAME, context.getName());
		update.setProperty(PROPERTY_ADDRESS, context.getHexAddress());
		update.setProperty(PROPERTY_DESCRIPTION, context.getDescription());

		update.setProperty(PROPERTY_ISSYSTEM, new Boolean(context.isSystem()));
	}

	/**
	 * Tell if the peripheral should be displayed as checked, by testing if the
	 * peripheral is shown in the memory monitor window.
	 *
	 * @param treePath
	 *            the peripheral path.
	 * @param presentationContext
	 *            the presentation context (unused).
	 * @return true if the peripheral should be checked.
	 */
	protected boolean getChecked(TreePath treePath, IPresentationContext presentationContext) {

		Object pathSegment = treePath.getLastSegment();
		if ((pathSegment instanceof PeripheralsVMContext)) {
			PeripheralsVMContext peripheralVMContext = (PeripheralsVMContext) pathSegment;
			PeripheralDMContext peripheralDMContext = (PeripheralDMContext) peripheralVMContext.getDMContext();

			// Activator.log("PeripheralsVMNode.getChecked()="
			// + peripheralDMContext.hasMemoryMonitor() + " "
			// + peripheralDMContext);
			return peripheralDMContext.hasMemoryMonitor();
		}
		return false;
	}

	/**
	 * Gets the grayed.
	 *
	 * @param treePath the tree path
	 * @param presentationContext the presentation context
	 * @return the grayed
	 */
	protected boolean getGrayed(TreePath treePath, IPresentationContext presentationContext) {
		return false;
	}

	// ------------------------------------------------------------------------

	@Override
    public String toString() {
		return "PeripheralsVMNode(" + getSession().getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	// ------------------------------------------------------------------------
}
