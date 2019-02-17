/*******************************************************************************
* Copyright (c) 2016 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
*******************************************************************************/

package com.lembed.lite.studio.device.pack.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.ICpEnvironmentProvider;
import com.lembed.lite.studio.device.core.data.ICpExample;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.data.ICpPack;
import com.lembed.lite.studio.device.core.events.LiteEvent;
import com.lembed.lite.studio.device.core.events.LiteEventProxy;
import com.lembed.lite.studio.device.core.lite.boards.ILiteBoardDeviceItem;
import com.lembed.lite.studio.device.core.lite.devices.ILiteDeviceItem;
import com.lembed.lite.studio.device.core.lite.examples.ILiteExampleItem;
import com.lembed.lite.studio.device.pack.ui.views.PackInstallerView;
import com.lembed.lite.studio.device.pack.ui.views.PackPropertyView;

/**
 *  Class responsible for synchronizing Pack Installer Views
 */
public class PackInstallerViewController extends LiteEventProxy implements ISelectionListener {

	public final static String INSTALLER_UI_FILTER_CHANGED = "pack.ui.filter.changed"; //$NON-NLS-1$
	public final static String INSTALLER_UI_PACK_CHANGED = "pack.ui.pack.changed"; //$NON-NLS-1$
	protected PackInstallerViewFilter fFilter = null;
	protected ICpPack fSelectedPack = null;

	public PackInstallerViewController() {
	}

	public void clear() {
		fSelectedPack = null;
		removeAllListeners();
		if(fFilter != null) {
			fFilter.clear();
			fFilter = null;
		}
	}


	@Override
	public void handle(LiteEvent event) {
		if(event.getTopic().equals(LiteEvent.PACKS_RELOADED)) {
			fSelectedPack = null;
			if (fFilter != null) {
				fFilter.clear();
			}
		}
		super.handle(event);
	}

	public ICpPack getSelectedPack() {
		return fSelectedPack;
	}

	public PackInstallerViewFilter getFilter() {
		if(fFilter == null) {
			fFilter = createFilter();
		}
		return fFilter;
	}

	public PackInstallerViewFilter createFilter() {
		return new PackInstallerViewFilter();
	}


	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(part instanceof PackInstallerView) {
			updateSelectedPack((IStructuredSelection)selection);
			PackInstallerView view = (PackInstallerView)part;
			if(view.isFilterSource()) {
				boolean changed = getFilter().setSelection(part, (IStructuredSelection) selection);
				if(changed) {
					emitLiteEvent(INSTALLER_UI_FILTER_CHANGED, null);
				}
			}
		}
	}

	static public ICpPack getPackFromSelection(ISelection selection) {
		if(selection == null || !(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection sel = (IStructuredSelection) selection;
		if(sel.size() == 1) {
			return getPackFromObject(sel.getFirstElement());
		}
		return null;
	}

	static public ICpPack getPackFromObject(Object o) {
		if(o == null) {
			return null;
		}
		if(o instanceof IStructuredSelection) {
			return getPackFromSelection((ISelection) o);
		}
		ICpItem item = null;
		if(o instanceof ICpItem) {
			item = (ICpItem)o;
		} else 	if(o instanceof ILiteBoardDeviceItem) {
			ILiteBoardDeviceItem board = (ILiteBoardDeviceItem)o;
			item = board.getBoard();
		} else if(o instanceof ILiteDeviceItem) {
			ILiteDeviceItem device = (ILiteDeviceItem)o;
			item = device.getDevice();
		} else if(o instanceof ILiteExampleItem) {
			ILiteExampleItem example = (ILiteExampleItem)o;
			item = example.getExample();
		}
		if(item != null) {
			return item.getPack();
		}
		return null;
	}



	protected void updateSelectedPack(IStructuredSelection selection) {
		ICpPack pack = getPackFromSelection(selection);
		if(fSelectedPack == pack) {
			return;
		}
		fSelectedPack = pack;
		emitLiteEvent(INSTALLER_UI_PACK_CHANGED, fSelectedPack);
	}


	public void showPackProperties(ISelection selection) {
		if(selection != null && selection instanceof IStructuredSelection) {
			updateSelectedPack((IStructuredSelection)selection);
		}
		IWorkbench wb = PlatformUI.getWorkbench();
		if(wb == null) {
			return;
		}
		IWorkbenchWindow wbw = wb.getActiveWorkbenchWindow();
		if(wbw == null || wbw.getActivePage() == null) {
			return;
		}
		try {
			wbw.getActivePage().showView(PackPropertyView.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	public void copyExample(ICpExample cpExample) {
		ICpEnvironmentProvider envProvider = CpPlugIn.getEnvironmentProvider();
		if(envProvider == null) {
			return;
		}
		IAdaptable copyResult = envProvider.copyExample(cpExample);
		if(copyResult == null) {
			return;
		}
		@SuppressWarnings("cast")
		IProject project = (IProject) copyResult.getAdapter(IProject.class);
		if (project == null) {
			return;
		}

		IWorkbench wb = PlatformUI.getWorkbench();
		if (wb != null) {
			IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
			if (window != null) {
				IPerspectiveDescriptor persDescription = wb.getPerspectiveRegistry().findPerspectiveWithId("org.eclipse.cdt.ui.CPerspective"); //$NON-NLS-1$
				IWorkbenchPage page = window.getActivePage();
				if (page != null && persDescription != null) {
					page.setPerspective(persDescription);
					try {
						String rteConf = project.getName()	+ '.' + CmsisConstants.RTECONFIG;
						IResource r = project.findMember(rteConf);
						if(r != null && r.exists() && r.getType() == IResource.FILE) {
							IDE.openEditor(page, project.getFile(rteConf));
						}
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


}
