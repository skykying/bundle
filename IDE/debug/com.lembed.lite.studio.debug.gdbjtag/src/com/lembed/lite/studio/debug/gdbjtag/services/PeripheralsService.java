/*******************************************************************************
 * Copyright (c) 2017 Lembed Electronic.
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

package com.lembed.lite.studio.debug.gdbjtag.services;

import com.lembed.lite.studio.core.EclipseUtils;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.dsf.concurrent.DataRequestMonitor;
import org.eclipse.cdt.dsf.concurrent.RequestMonitor;
import org.eclipse.cdt.dsf.datamodel.IDMContext;
import org.eclipse.cdt.dsf.debug.service.IRunControl.IContainerDMContext;
import org.eclipse.cdt.dsf.debug.service.command.ICommandControlService;
import org.eclipse.cdt.dsf.service.AbstractDsfService;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.osgi.framework.BundleContext;
import com.lembed.lite.studio.debug.gdbjtag.GdbActivator;
import com.lembed.lite.studio.debug.gdbjtag.ILaunchConfigurationProvider;
import com.lembed.lite.studio.debug.gdbjtag.data.LiteProjectAttributes;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.IPeripheralDMContext;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.PeripheralDMContext;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.PeripheralDMNode;
import com.lembed.lite.studio.debug.gdbjtag.datamodel.SvdUtils;
import com.lembed.lite.studio.device.core.svd.Leaf;

/**
 * The Class PeripheralsService.
 */
public class PeripheralsService extends AbstractDsfService implements IPeripheralsService {

	private ICommandControlService fCommandControl;
	private PeripheralDMContext[] fPeripheralsDMContexts = null;

	// ------------------------------------------------------------------------

	/**
	 * Instantiates a new peripherals service.
	 *
	 * @param session the session
	 */
	public PeripheralsService(DsfSession session) {
		super(session);

	}

	@Override
    public void initialize(final RequestMonitor rm) {

		GdbActivator.log("PeripheralsService.initialize()"); //$NON-NLS-1$

		super.initialize(new RequestMonitor(getExecutor(), rm) {

			@Override
            protected void handleSuccess() {
				doInitialize(rm);
			}
		});
	}

	@SuppressWarnings("rawtypes")
	private void doInitialize(RequestMonitor rm) {

		GdbActivator.log("PeripheralsService.doInitialize()"); //$NON-NLS-1$

		// Get and remember the command control service
		fCommandControl = (getServicesTracker().getService(ICommandControlService.class));

		// Register this service to DSF.
		// For completeness, use both the interface and the class name.
		// Used in PeripheralVMNode by interface name.
		register(new String[] { IPeripheralsService.class.getName(), PeripheralsService.class.getName() },
		         new Hashtable());

		GdbActivator.log("PeripheralsService registered " + this); //$NON-NLS-1$

		rm.done();
	}

	@Override
	public void shutdown(RequestMonitor rm) {

		GdbActivator.log("PeripheralsService.shutdown()"); //$NON-NLS-1$

		// Remove this service from DSF.
		unregister();

		super.shutdown(rm);
	}

	// ------------------------------------------------------------------------

	@Override
	protected BundleContext getBundleContext() {
		return GdbActivator.getInstance().getBundle().getBundleContext();
	}

	@Override
	public void getPeripherals(IContainerDMContext containerDMContext, DataRequestMonitor<IPeripheralDMContext[]> drm) {


		GdbActivator.log("PeripheralsService.getPeripherals()"); //$NON-NLS-1$

		if (fPeripheralsDMContexts != null) {

			// On subsequent calls, return existing array.
			drm.setData(fPeripheralsDMContexts);
			drm.done();
			return;
		}

		if (fCommandControl instanceof ILaunchConfigurationProvider) {

			// The launch configuration is obtained from the command control,
			// using a dedicated interface.
			ILaunchConfiguration launchConfiguration = ((ILaunchConfigurationProvider) fCommandControl)
			        .getLaunchConfiguration();

			// The second step is to get the build configuration description.
			ICConfigurationDescription cConfigDescription = EclipseUtils.getBuildConfigDescription(launchConfiguration);

			if (cConfigDescription != null) {
				// Activator.log(cConfigDescription);

				// The third step is to get the CDT configuration.
				IConfiguration config = EclipseUtils.getConfigurationFromDescription(cConfigDescription);
				GdbActivator.log(config.toString());
				
				ICProjectDescription projDesc = cConfigDescription.getProjectDescription();
				
				try {
					String vendorId = null; //vendorId="13";
					String deviceName = null; //deviceName="STM32F103VE";

					if(projDesc != null) {
						vendorId = LiteProjectAttributes.getCmsisVenderId(projDesc);
						deviceName = LiteProjectAttributes.getCmsisDeviceName(projDesc);
					}
					
					if (vendorId == null && deviceName == null) {
						vendorId = LiteProjectAttributes.getCmsisVenderId(config);
						deviceName = LiteProjectAttributes.getCmsisDeviceName(config);
					}

					if (vendorId != null && deviceName != null) {
						
						GdbActivator.log("get peripheral tree is begin !"); //$NON-NLS-1$
						
						Leaf tree = SvdUtils.getTree(vendorId, deviceName);
						if (tree == null) {
							GdbActivator.log("get peripheral tree is empty !"); //$NON-NLS-1$
							return;
						}

						List<Leaf> list = SvdUtils.getPeripherals(tree);
						if (list == null) {
							GdbActivator.log("get peripherals failure !"); //$NON-NLS-1$
							return;
						}

						fPeripheralsDMContexts = createPeripheralsContexts(containerDMContext, list);

						drm.setData(fPeripheralsDMContexts);
						drm.done();
						return;

					}
                    drm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID,
                                             "There are no peripheral descriptions available, assign a device to the project.")); //$NON-NLS-1$
                    drm.done();
                    return;
				} catch (CoreException e) {
					drm.setStatus(e.getStatus());
					drm.done();
					return;
				}
			}
		}

		drm.setStatus(new Status(IStatus.ERROR, GdbActivator.PLUGIN_ID, "No peripherals available.")); //$NON-NLS-1$
		drm.done();
	}

	private PeripheralDMContext[] createPeripheralsContexts(IDMContext parentIDMContext, List<Leaf> list) {

		PeripheralDMContext contexts[] = new PeripheralDMContext[list.size()];
		IDMContext[] parents;
		if (parentIDMContext != null) {
			parents = new IDMContext[] { parentIDMContext };
		} else {
			parents = new IDMContext[0];
		}

		int i = 0;
		for (Leaf child : list) {
			PeripheralDMNode node = new PeripheralDMNode(child);
			contexts[i] = new PeripheralDMContext(getSession(), parents, node);
			++i;
		}

		Arrays.sort(contexts);
		return contexts;
	}

}
