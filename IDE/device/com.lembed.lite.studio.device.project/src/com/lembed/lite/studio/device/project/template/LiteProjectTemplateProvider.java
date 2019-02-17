/*******************************************************************************
* Copyright (c) 2015 ARM Ltd. and others
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.project.template;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.ui.wizards.MBSCustomPageManager;
import org.eclipse.cdt.ui.templateengine.IPagesAfterTemplateSelectionProvider;
import org.eclipse.cdt.ui.templateengine.IWizardDataPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.data.CpItem;
import com.lembed.lite.studio.device.core.data.ICpItem;
import com.lembed.lite.studio.device.core.info.ICpDeviceInfo;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.ui.CpPlugInUI;
//import com.lembed.lite.studio.managedbuild.cross.ToolchainPlugin;

/**
 * This class provides extra pages for new Project wizard and static functions
 * helping in project creation
 */
public class LiteProjectTemplateProvider implements IPagesAfterTemplateSelectionProvider {

	protected List<IWizardDataPage> fPages = null;

	static protected ICpDeviceInfo selectedDeviceInfo = null;
	
	static protected Map<String, String> features = new HashMap<String, String>();

	@Override
	public IWizardDataPage[] createAdditionalPages(IWorkbenchWizard wizard, IWorkbench workbench,
			IStructuredSelection selection) {

		fPages = new ArrayList<IWizardDataPage>();
		getSelectedToolChain();		
		TemplateDeviceSelectorPage devicePage = new TemplateDeviceSelectorPage();

		LiteTemplateMiddlePage middlePage = new LiteTemplateMiddlePage(
				Messages.LiteProjectTemplate_SelectLibrary, 
				Messages.LiteProjectTemplate_SelectLibrary, 
				CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_CHIP_48));
		
		LiteTemplateTargetFolderPage folderPage = new LiteTemplateTargetFolderPage(
				Messages.LiteProjectTemplate_SetTargetFolder, 
				Messages.LiteProjectTemplate_SetTargetFolder, 
				CpPlugInUI.getImageDescriptor(CpPlugInUI.ICON_CHIP_48));
		

		fPages.add(devicePage);
		
		// disable the toolchain adapter page @2017.3.15
		fPages.add(middlePage);
		
		
		fPages.add(folderPage);
		

		return getCreatedPages(wizard);
	}

	@Override
	public IWizardDataPage[] getCreatedPages(IWorkbenchWizard wizard) {
		return fPages.toArray(new IWizardDataPage[0]);
	}
	
	
	public static Map<String, String> getProjectFeatures() {
		return features;
	}

	public static void collectProjectFeatures(Map<String, String> feature) {
		LiteProjectTemplateProvider.features.putAll(feature);
	}
	
	/**
	 * Returns device info selected in LiteTemplateDeviceSelectorPage
	 *
	 * @return ICpDeviceInfo
	 */
	public static ICpDeviceInfo getSelectedDeviceInfo() {
		return selectedDeviceInfo;
	}

	public static void setSelectedDeviceInfo(ICpDeviceInfo selectedDeviceInfo) {
		LiteProjectTemplateProvider.selectedDeviceInfo = selectedDeviceInfo;
	}

	/**
	 * Helper method that returns selected in the main page of CDT new project
	 * wizard
	 *
	 * @return selected IToolChain object
	 */
	public static IToolChain getSelectedToolChain() {
		Object tsProperty = MBSCustomPageManager.getPageProperty(MBSCustomPageManager.PAGE_ID,
				MBSCustomPageManager.TOOLCHAIN);
		if (tsProperty instanceof List<?>) {
			List<?> l = (List<?>) tsProperty;
			for (Object o : l) {
				if (o instanceof IToolChain) {
					return (IToolChain) o;
				}
			}
		}
		return null;
	}

	/**
	 * Creates toolchain info for given compiler and output type
	 *
	 * @param Tcompiler
	 *            compiler family
	 * @param Toutput
	 *            output type : " exe" or "lib"
	 * @return toolchain info as ICpItem
	 */
	public static ICpItem createToolChainInfo(String Tcompiler, String Toutput) {
		ICpItem toolchainInfo = new CpItem(null, CmsisConstants.TOOLCHAIN_TAG);
		toolchainInfo.attributes().setAttribute(CmsisConstants.TCOMPILER, Tcompiler);
		toolchainInfo.attributes().setAttribute(CmsisConstants.TOUTPUT, Toutput);
		return toolchainInfo;

	}

}
