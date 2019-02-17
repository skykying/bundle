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

import java.util.Map;
import org.eclipse.cdt.ui.templateengine.AbstractWizardDataPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.generic.Attributes;
import com.lembed.lite.studio.device.project.IHelpContextIds;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.ui.IStatusMessageListener;

public class LiteTemplateMiddlePage extends AbstractWizardDataPage implements IStatusMessageListener {

	private Label lblOutput;

	private LiteMiddleSelector middleSelector = null;

	protected boolean updatingControls;
	protected boolean initialized = false;

	protected String Tcompiler = "GCC";//$NON-NLS-1$
	protected String Toutput = "exe";//$NON-NLS-1$
	protected String adapterId = "com.lembed.lite.studio.device.toolchain.default.toolChainAdapter";//$NON-NLS-1$

	/**
	 * @param pageName
	 */
	public LiteTemplateMiddlePage(String pageName) {
		super(pageName);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param imageDescriptor
	 */
	public LiteTemplateMiddlePage(String pageName, String title, ImageDescriptor imageDescriptor) {
		super(pageName, title, imageDescriptor);
	}

	@Override
	public Map<String, String> getPageData() {

		Attributes a = new Attributes();
		a.setAttribute(CmsisConstants.TCOMPILER, Tcompiler);
		a.setAttribute(CmsisConstants.TOUTPUT, Toutput);
		a.setAttribute(CmsisConstants.TADAPTER_ID, adapterId);

		String ext = CmsisConstants.EMPTY_STRING;
		if (Toutput.equals(CmsisConstants.TOUTPUT_EXE)) {
			ext = "elf"; //$NON-NLS-1$			
		} else {
			ext = "a"; //$NON-NLS-1$
		}
		a.setAttribute(CmsisConstants.FEATURE_PROJECT_EXTENSION, ext); 

		// FIXME: move this to another place
		a.setAttribute(CmsisConstants.FEATURE_PROJECT_TEMPLATE_LAST, !(getWizard() instanceof IImportWizard)); 
		
		
		Map<String, String> features = a.getAttributesAsMap();
		Map<String, String> ms = LiteProjectTemplateProvider.getProjectFeatures();
		features.putAll(ms);
		
		return features;
	}

	@Override
	public void createControl(Composite parent) {

		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
//		gridLayout.horizontalSpacing = 8;
		mainComposite.setLayout(gridLayout);

		Label lblOutputLabel = new Label(mainComposite, SWT.NONE);
		lblOutputLabel.setText(Messages.LiteTemplateCmsisProjectPage_Output);

		lblOutput = new Label(mainComposite, SWT.NONE);
		lblOutput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		middleSelector = new LiteMiddleSelector(mainComposite, SWT.NONE);
		GridData gdm = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		middleSelector.setLayoutData(gdm);
		
		setControl(mainComposite);
		initialized = true;
		update();

		// add context-sensitive help
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IHelpContextIds.CMSIS_PROJECT_WIZARD);

	}

	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			update();
		}
	}

	protected void update() {
		if (!initialized) {
			return;
		}

		lblOutput.setText(Toutput);
	}

	@Override
	public void handle(String message) {
		updateStatus(message);
	}

	public void updateStatus(String message) {
		setErrorMessage(message);
		
		boolean complete = Tcompiler != null && !Tcompiler.isEmpty() && adapterId != null && !adapterId.isEmpty();
		setPageComplete(complete); // TODO : implement
	}
	
}
