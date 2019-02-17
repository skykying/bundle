/*******************************************************************************
 * Copyright (c) 2017 LEMBED.
 * Copyright (c) 2015 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * LEMBED - adapter for LiteSTUDIO
 * ARM Ltd and ARM Germany GmbH - Initial API and implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.project.template;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.cdt.ui.templateengine.AbstractWizardDataPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.PlatformUI;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.generic.Attributes;
import com.lembed.lite.studio.device.project.IHelpContextIds;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.ui.IStatusMessageListener;

public class LiteTemplateTargetFolderPage extends AbstractWizardDataPage implements IStatusMessageListener {

	private Button chkMain;
	private boolean initialized = false;

	private Text includeFolderText;
	private Text sourceFolderText;
	private Text systemFolderText;
	private Text scriptFolderText;

	private Text miniStackSizeText;
	private Text stackSizeText;

	protected String TIncludeDir = "";//$NON-NLS-1$
	protected String TSourceDir = "";//$NON-NLS-1$
	protected String TSystemDir = "";//$NON-NLS-1$
	protected String TScriptDir = "";//$NON-NLS-1$

	protected String TStackMiniSize = "";//$NON-NLS-1$
	protected String TStackSize = "";//$NON-NLS-1$

	/**
	 * @param pageName
	 */
	public LiteTemplateTargetFolderPage(String pageName) {
		super(pageName);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param imageDescriptor
	 */
	public LiteTemplateTargetFolderPage(String pageName, String title, ImageDescriptor imageDescriptor) {
		super(pageName, title, imageDescriptor);
	}

	@Override
	public Map<String, String> getPageData() {

		Attributes a = new Attributes();
		a.setAttribute(CmsisConstants.FEATURE_INCLUDE_DIR_KEY, TIncludeDir);
		a.setAttribute(CmsisConstants.FEATURE_SOURCE_DIR_KEY, TSourceDir);
		a.setAttribute(CmsisConstants.FEATURE_SYSTEM_DIR_KEY, TSystemDir);
		a.setAttribute(CmsisConstants.FEATURE_LINKER_SCRIPT_DIR_KEY, TScriptDir);

		a.setAttribute(CmsisConstants.FEATURE_PROJECT_STACKMINSIZE, TStackMiniSize);
		a.setAttribute(CmsisConstants.FEATURE_PROJECT_STACKSIZE, TStackSize);

		if (initialized) {
			boolean createMain = chkMain == null ? false : chkMain.getSelection();
			a.setAttribute(CmsisConstants.FEATURE_PROJECT_TEMPLATE_CMAIN, createMain);
		}

		a.setAttribute(CmsisConstants.FEATURE_PROJECT_TEMPLATE_LAST, !(getWizard() instanceof IImportWizard));

		Map<String, String> fMaps = a.getAttributesAsMap();
		LiteProjectTemplateProvider.collectProjectFeatures(fMaps);

		return fMaps;
	}

	@Override
	public void createControl(Composite parent) {

		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 8;
		mainComposite.setLayout(gridLayout);

		Group targetGroup = new Group(mainComposite, SWT.NONE);
		targetGroup.setText(Messages.LiteTemplateFolder_FolderSetting);
		GridLayout layout = new GridLayout(2, false);
		targetGroup.setLayout(layout);
		targetGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			Composite composite = new Composite(targetGroup, SWT.NONE);
			GridLayout layoutComposite = new GridLayout(2, false);
			composite.setLayout(layoutComposite);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));

			Label includeFolderLabel = new Label(composite, SWT.NONE);
			includeFolderLabel.setText(Messages.LiteTemplateFolder_IncludeFolder);// $NON-NLS-1$

			includeFolderText = new Text(composite, SWT.BORDER);
			includeFolderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			includeFolderText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					// Get the widget whose text was modified
					Text text = (Text) event.widget;
					System.out.println(text.getText());
					TIncludeDir = StringFilter(text.getText());
				}
			});
			includeFolderText.setText(Messages.LiteTemplateFolder_IncludeFolderDefault);

			Label sourceFolderLabel = new Label(composite, SWT.NONE);
			sourceFolderLabel.setText(Messages.LiteTemplateFolder_SourceFolder);// $NON-NLS-1$

			sourceFolderText = new Text(composite, SWT.BORDER);
			sourceFolderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			sourceFolderText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					// Get the widget whose text was modified
					Text text = (Text) event.widget;
					System.out.println(text.getText());
					TSourceDir = StringFilter(text.getText());
				}
			});
			sourceFolderText.setText(Messages.LiteTemplateFolder_SourceFolderDefault);

			Label systemFolderLabel = new Label(composite, SWT.NONE);
			systemFolderLabel.setText(Messages.LiteTemplateFolder_SystemFolder);// $NON-NLS-1$

			systemFolderText = new Text(composite, SWT.BORDER);
			systemFolderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			systemFolderText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					// Get the widget whose text was modified
					Text text = (Text) event.widget;
					TSystemDir = StringFilter(text.getText());
					System.out.println(text.getText());
				}
			});
			systemFolderText.setText(Messages.LiteTemplateFolder_SystemFolderDefault);

			Label scriptFolderLabel = new Label(composite, SWT.NONE);
			scriptFolderLabel.setText(Messages.LiteTemplateFolder_LinkerScriptFolder);// $NON-NLS-1$

			scriptFolderText = new Text(composite, SWT.BORDER);
			scriptFolderText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			scriptFolderText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					// Get the widget whose text was modified
					Text text = (Text) event.widget;
					System.out.println(text.getText());
					TScriptDir = StringFilter(text.getText());
				}
			});
			scriptFolderText.setText(Messages.LiteTemplateFolder_LinkerScriptFolderDefault);
		}

		new Label(mainComposite, SWT.NONE);

		Group memGroup = new Group(mainComposite, SWT.NONE);
		memGroup.setText(Messages.LiteTemplateFolder_HeapStackSetting);// $NON-NLS-1$
		GridLayout mlayout = new GridLayout(2, false);
		memGroup.setLayout(mlayout);
		memGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		{
			Composite composite = new Composite(memGroup, SWT.NONE);
			GridLayout layoutComposite = new GridLayout(2, false);
			composite.setLayout(layoutComposite);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));

			Label includeFolderLabel = new Label(composite, SWT.NONE);
			includeFolderLabel.setText(Messages.LiteTemplateFolder_MiniStackSize);// $NON-NLS-1$

			miniStackSizeText = new Text(composite, SWT.BORDER);
			miniStackSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			miniStackSizeText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					// Get the widget whose text was modified
					Text text = (Text) event.widget;
					System.out.println(text.getText());
					TStackMiniSize = StringFilter(text.getText());
				}
			});
			miniStackSizeText.setText(Messages.LiteTemplateFolder_MiniStackSizeDefault);

			Label sourceFolderLabel = new Label(composite, SWT.NONE);
			sourceFolderLabel.setText(Messages.LiteTemplateFolder_StackSize);// $NON-NLS-1$

			stackSizeText = new Text(composite, SWT.BORDER);
			stackSizeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			stackSizeText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					// Get the widget whose text was modified
					Text text = (Text) event.widget;
					System.out.println(text.getText());
					TStackSize = StringFilter(text.getText());
				}
			});
		}
		stackSizeText.setText(Messages.LiteTemplateFolder_StackSizeDefault);

		new Label(mainComposite, SWT.NONE);

		chkMain = new Button(mainComposite, SWT.CHECK);
		chkMain.setText(Messages.LiteTemplateCmsisProjectPage_CreateDefaultMain);
		chkMain.setSelection(true);

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

	}

	@Override
	public void handle(String message) {
		updateStatus(message);
	}

	public void updateStatus(String message) {
		setErrorMessage(message);

		boolean complete = TIncludeDir.isEmpty() & TScriptDir.isEmpty() & TSystemDir.isEmpty() & TSourceDir.isEmpty();
		setPageComplete(!complete); // TODO : implement
	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~£¡@#£¤%¡­¡­&*£¨£©¡ª¡ª+|{}¡¾¡¿¡®£»£º¡±¡°¡¯¡££¬¡¢£¿]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
}
