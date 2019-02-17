/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Keven - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.managedbuild.cross.llvm.ui;

import org.eclipse.cdt.core.settings.model.ICResourceDescription;
import org.eclipse.cdt.managedbuilder.buildproperties.IBuildPropertyValue;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IProjectType;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.eclipse.cdt.managedbuilder.internal.core.MultiConfiguration;
import org.eclipse.cdt.managedbuilder.makegen.IManagedBuilderMakefileGenerator;
import org.eclipse.cdt.managedbuilder.makegen.gnu.GnuMakefileGenerator;
import org.eclipse.cdt.managedbuilder.ui.properties.AbstractCBuildPropertyTab;
import org.eclipse.cdt.managedbuilder.ui.properties.Page_BuildSettings;
import org.eclipse.cdt.ui.newui.ICPropertyProvider2;
import org.eclipse.cdt.ui.newui.ICPropertyTab;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.lembed.lite.studio.core.EclipseUtils;
import com.lembed.lite.studio.managedbuild.cross.llvm.ClangOption;
import com.lembed.lite.studio.managedbuild.cross.llvm.LlvmUIPlugin;
import com.lembed.lite.studio.managedbuild.cross.llvm.Utils;
import com.lembed.lite.studio.managedbuild.cross.llvm.preferences.Messages;
import com.lembed.lite.studio.managedbuild.cross.llvm.preferences.PersistentPreferences;

/**
 * @noextend This class is not intended to be subclassed by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
@SuppressWarnings("restriction")
public class LLVMTabToolchains extends AbstractCBuildPropertyTab {

    // ------------------------------------------------------------------------

    // private Composite fComposite;
    private IConfiguration fConfig;
    private IConfiguration fLastUpdatedConfig = null;

    // ---

    private Combo fToolchainCombo;
    private int fSelectedToolchainIndex = 0;
    private String fSelectedToolchainName;

    private Combo fArchitectureCombo;

    private Text fPrefixText;
    private Text fSuffixText;
    private Text fCommandCText;
    private Text fCommandCppText;
    private Text fCommandArText;
    private Text fCommandObjcopyText;
    private Text fCommandObjdumpText;
    private Text fCommandSizeText;
    private Text fCommandMakeText;
    private Text fCommandRmText;
    private Text fPathLabel;

    private Button fFlashButton;
    private Button fListingButton;
    private Button fSizeButton;

    // private boolean fIsExecutable;
    // private boolean fIsStaticLibrary;

    private static int WIDTH_HINT = 120;

    // ------------------------------------------------------------------------

    /**
     * Gets the project.
     *
     * @return the project
     */
    protected IProject getProject() {
        assert (fConfig != null);
        return (IProject) fConfig.getManagedProject().getOwner();
    }

    /**
     * Gets the selected toolchain name.
     *
     * @return the selected toolchain name
     */ 
    protected String getSelectedToolchainName() {

        assert (fToolchainCombo != null);

        int index;
        try {
            String sSelectedCombo = fToolchainCombo.getText();
            index = ToolchainDefinition.findToolchainByFullName(sSelectedCombo);
        } catch (NullPointerException e) {
            index = 0;
        }
        ToolchainDefinition td = ToolchainDefinition.getToolchain(index);

        return td.getName();
    }

    @Override
    public void createControls(final Composite parent) {

        if (EclipseUtils.isLinux()) {
            WIDTH_HINT = 150;
        }

        // fComposite = parent;
        // Disabled, otherwise toolchain changes fail
        LlvmUIPlugin.log("ClangTabToolchains.createControls()"); //$NON-NLS-1$

        if (!page.isForProject()) {
            LlvmUIPlugin.log("not this project"); //$NON-NLS-1$
            return;
        }

        super.createControls(parent);

        fConfig = getCfg();

        LlvmUIPlugin.log("createControls() fConfig=" + fConfig); //$NON-NLS-1$

        // usercomp is defined in parent class

        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        // layout.marginHeight = 0;
        // layout.marginWidth = 0;
        usercomp.setLayout(layout);

        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        usercomp.setLayoutData(layoutData);

        // fWasUpdateRefused = false;

        // ----- Toolchain ----------------------------------------------------
        Label toolchainLbl = new Label(usercomp, SWT.NONE);
        toolchainLbl.setLayoutData(new GridData(GridData.BEGINNING));
        toolchainLbl.setText(Messages.ToolChainSettingsTab_name);

        fToolchainCombo = new Combo(usercomp, SWT.DROP_DOWN);
        layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        fToolchainCombo.setLayoutData(layoutData);

        fToolchainCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {

                updateInterfaceAfterToolchainChange();
            }
        });

        // ----- Architecture -------------------------------------------------
        Label architectureLbl = new Label(usercomp, SWT.NONE);
        architectureLbl.setLayoutData(new GridData(GridData.BEGINNING));
        architectureLbl.setText(Messages.ToolChainSettingsTab_architecture);

        fArchitectureCombo = new Combo(usercomp, SWT.DROP_DOWN);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.widthHint = WIDTH_HINT;
        fArchitectureCombo.setLayoutData(layoutData);

        // ----- Prefix -------------------------------------------------------
        Label prefixLabel = new Label(usercomp, SWT.NONE);
        prefixLabel.setText(Messages.ToolChainSettingsTab_prefix);

        fPrefixText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.widthHint = WIDTH_HINT;
        fPrefixText.setLayoutData(layoutData);

        // ----- Suffix -------------------------------------------------------
        Label suffixLabel = new Label(usercomp, SWT.NONE);
        suffixLabel.setText(Messages.ToolChainSettingsTab_suffix);

        fSuffixText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.widthHint = WIDTH_HINT;
        fSuffixText.setLayoutData(layoutData);

        // ----- Command c ----------------------------------------------------
        Label commandCLabel = new Label(usercomp, SWT.NONE);
        commandCLabel.setText(Messages.ToolChainSettingsTab_cCmd);

        fCommandCText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.widthHint = WIDTH_HINT;
        fCommandCText.setLayoutData(layoutData);

        // ----- Command cpp --------------------------------------------------
        Label commandCppLabel = new Label(usercomp, SWT.NONE);
        commandCppLabel.setText(Messages.ToolChainSettingsTab_cppCmd);

        fCommandCppText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
        layoutData = new GridData();
        layoutData.horizontalSpan = 2;
        layoutData.widthHint = WIDTH_HINT;
        fCommandCppText.setLayoutData(layoutData);

        if (isManaged()) {
            // ----- Command ar
            // ---------------------------------------------------
            Label commandArLabel = new Label(usercomp, SWT.NONE);
            commandArLabel.setText(Messages.ToolChainSettingsTab_arCmd);

            fCommandArText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData();
            layoutData.horizontalSpan = 2;
            layoutData.widthHint = WIDTH_HINT;
            fCommandArText.setLayoutData(layoutData);

            // ----- Command objcopy ------------------------------------------
            Label commandObjcopyLabel = new Label(usercomp, SWT.NONE);
            commandObjcopyLabel.setText(Messages.ToolChainSettingsTab_objcopyCmd);

            fCommandObjcopyText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData();
            layoutData.horizontalSpan = 2;
            layoutData.widthHint = WIDTH_HINT;
            fCommandObjcopyText.setLayoutData(layoutData);

            // ----- Command objdump ------------------------------------------
            Label commandObjdumpLabel = new Label(usercomp, SWT.NONE);
            commandObjdumpLabel.setText(Messages.ToolChainSettingsTab_objdumpCmd);

            fCommandObjdumpText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData();
            layoutData.horizontalSpan = 2;
            layoutData.widthHint = WIDTH_HINT;
            fCommandObjdumpText.setLayoutData(layoutData);

            // ----- Command size ---------------------------------------------
            Label commandSizeLabel = new Label(usercomp, SWT.NONE);
            commandSizeLabel.setText(Messages.ToolChainSettingsTab_sizeCmd);

            fCommandSizeText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData();
            layoutData.horizontalSpan = 2;
            layoutData.widthHint = WIDTH_HINT;
            fCommandSizeText.setLayoutData(layoutData);

            // ----- Command make ---------------------------------------------
            Label commandMakeLabel = new Label(usercomp, SWT.NONE);
            commandMakeLabel.setText(Messages.ToolChainSettingsTab_makeCmd);

            fCommandMakeText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData();
            layoutData.horizontalSpan = 2;
            layoutData.widthHint = WIDTH_HINT;
            fCommandMakeText.setLayoutData(layoutData);

            // ----- Command rm -----------------------------------------------
            Label commandRmLabel = new Label(usercomp, SWT.NONE);
            commandRmLabel.setText(Messages.ToolChainSettingsTab_rmCmd);

            fCommandRmText = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData();
            layoutData.horizontalSpan = 2;
            layoutData.widthHint = WIDTH_HINT;
            fCommandRmText.setLayoutData(layoutData);

            fCommandRmText.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(ModifyEvent e) {
                    // LlvmUIPlugin.log("commandRm modified");
                }
            });
        } else {
            Label label = new Label(usercomp, SWT.NONE);
            label.setText(""); //$NON-NLS-1$

            Label link = new Label(usercomp, SWT.NONE);
            link.setText(Messages.ToolChainSettingsTab_warning_link);
            layoutData = new GridData();
            layoutData.horizontalSpan = 2;
            link.setLayoutData(layoutData);
        }

        {
            // @2017.5.22
            // Label empty = new Label(usercomp, SWT.NONE);
            // empty.setText("");
            // layoutData = new GridData();
            // layoutData.horizontalSpan = 3;
            // empty.setLayoutData(layoutData);
        }

        {
            Label label = new Label(usercomp, SWT.NONE);
            label.setText(Messages.ToolChainSettingsTab_path_label);

            fPathLabel = new Text(usercomp, SWT.SINGLE | SWT.BORDER);
            layoutData = new GridData(SWT.FILL, 0, true, false);
            layoutData.horizontalSpan = 2;
            fPathLabel.setLayoutData(layoutData);

            fPathLabel.setEnabled(true);
            fPathLabel.setEditable(false);
        }

        {
            // @2017.5.22
            // Label label = new Label(usercomp, SWT.NONE);
            // label.setText("");
            //
            // Link link = new Link(usercomp, SWT.NONE);
            // link.setText(Messages.ToolChainSettingsTab_path_link);
            // layoutData = new GridData();
            // layoutData.horizontalSpan = 2;
            // link.setLayoutData(layoutData);
            //
            // link.addSelectionListener(new SelectionAdapter() {
            // @Override
            // public void widgetSelected(SelectionEvent e) {
            //
            // String text = e.text;
            // if (Activator.getInstance().isDebugging()) {
            // LlvmUIPlugin.log(text);
            // }
            //
            // int ret = -1;
            // if ("global".equals(text)) {
            // ret = PreferencesUtil.createPreferenceDialogOn(parent.getShell(),
            // GlobalToolsPathsPreferencePage.ID, null, null).open();
            // } else if ("workspace".equals(text)) {
            // ret = PreferencesUtil.createPreferenceDialogOn(parent.getShell(),
            // WorkspaceToolsPathsPreferencePage.ID, null, null).open();
            // } else if ("project".equals(text)) {
            // ret = PreferencesUtil.createPropertyDialogOn(parent.getShell(), getProject(),
            // ProjectToolsPathPropertyPage.ID, null, null, 0).open();
            // }
            //
            // if (ret == Window.OK) {
            // updateToolchainPath(getSelectedToolchainName());
            // }
            // }
            // });
        }

        {
            // @2017.5.22
            // Label empty = new Label(usercomp, SWT.NONE);
            // empty.setText("");
            // layoutData = new GridData();
            // layoutData.horizontalSpan = 3;
            // empty.setLayoutData(layoutData);
        }

        if (isManaged()) {
            // ----- Flash ----------------------------------------------------
            fFlashButton = new Button(usercomp, SWT.CHECK);
            fFlashButton.setText(Messages.ToolChainSettingsTab_flash);
            layoutData = new GridData(SWT.LEFT, SWT.TOP, false, false, 3, 1);
            fFlashButton.setLayoutData(layoutData);

            // ----- Listing --------------------------------------------------
            fListingButton = new Button(usercomp, SWT.CHECK);
            fListingButton.setText(Messages.ToolChainSettingsTab_listing);
            layoutData = new GridData(SWT.LEFT, SWT.TOP, false, false, 3, 1);
            fListingButton.setLayoutData(layoutData);

            // ----- Size -----------------------------------------------------
            fSizeButton = new Button(usercomp, SWT.CHECK);
            fSizeButton.setText(Messages.ToolChainSettingsTab_size);
            layoutData = new GridData(SWT.LEFT, SWT.TOP, false, false, 3, 1);
            fSizeButton.setLayoutData(layoutData);

            // fIsCreated = true;
        }

        updateControlsForConfig(fConfig);

        // --------------------------------------------------------------------

    }

    private void updateInterfaceAfterToolchainChange() {

        LlvmUIPlugin.log("ClangTabToolchains.updateInterfaceAfterToolchainChange()"); //$NON-NLS-1$

        int index;
        try {
            String sSelectedCombo = fToolchainCombo.getText();
            index = ToolchainDefinition.findToolchainByFullName(sSelectedCombo);
        } catch (NullPointerException e) {
            index = 0;
        }
        ToolchainDefinition td = ToolchainDefinition.getToolchain(index);

        String sArchitecture = td.getArchitecture();
        if ("arm".equals(sArchitecture)) { //$NON-NLS-1$
            index = 0;
        } else if ("aarch64".equals(sArchitecture)) { //$NON-NLS-1$
            index = 1;
        } else {
            index = 0; // default is ARM
        }
        fArchitectureCombo.setText(ToolchainDefinition.getArchitecture(index));

        fPrefixText.setText(td.getPrefix());
        fSuffixText.setText(td.getSuffix());
        fCommandCText.setText(td.getCmdC());
        fCommandCppText.setText(td.getCmdCpp());

        if (isManaged()) {
            fCommandArText.setText(td.getCmdAr());
            fCommandObjcopyText.setText(td.getCmdObjcopy());
            fCommandObjdumpText.setText(td.getCmdObjdump());
            fCommandSizeText.setText(td.getCmdSize());

            fCommandMakeText.setText(td.getCmdMake());
            String oldCommandRm = fCommandRmText.getText();
            String newCommandRm = td.getCmdRm();
            if (oldCommandRm == null || !oldCommandRm.equals(newCommandRm)) {
                // if same value skip it, to avoid remove the makefile
                fCommandRmText.setText(newCommandRm);
            }
        }

        updateToolchainPath(td.getName());
    }

    /**
     * Update toolchain path.
     *
     * @param toolchainName the toolchain name
     */
    protected void updateToolchainPath(String toolchainName) {

        assert (fConfig != null);
        IProject project = (IProject) fConfig.getManagedProject().getOwner();
        String toolchainPath = PersistentPreferences.getToolchainPath(toolchainName, project);
        fPathLabel.setText(toolchainPath);
    }

    // This event comes when the tab is selected after the windows is
    // displayed, to account for content change
    // It also comes when the configuration is changed in the selection.
    @Override
    public void updateData(ICResourceDescription cfgd) {
        if (cfgd == null)
            return;

        // fConfig = getCfg();

        LlvmUIPlugin.log("ClangTabToolchains.updateData() " + getCfg().getName()); //$NON-NLS-1$

        boolean isExecutable;
        boolean isStaticLibrary;
        IBuildPropertyValue propertyValue = fConfig.getBuildArtefactType();
        if (propertyValue != null) {
            String artefactId = propertyValue.getId();
            if (Utils.BUILD_ARTEFACT_TYPE_EXE.equals(artefactId) || artefactId.endsWith(".exe")) //$NON-NLS-1$
                isExecutable = true;
            else
                isExecutable = false;

            if (Utils.BUILD_ARTEFACT_TYPE_STATICLIB.equals(artefactId) || artefactId.endsWith("Lib")) //$NON-NLS-1$
                isStaticLibrary = true;
            else
                isStaticLibrary = false;

        } else {
            isExecutable = true;
            isStaticLibrary = false;
        }

        IConfiguration config = getCfg(cfgd.getConfiguration());
        if (config instanceof MultiConfiguration) {
            MultiConfiguration multi = (MultiConfiguration) config;

            // Take the first config in the multi-config
            config = (IConfiguration) multi.getItems()[0];
        }

        updateControlsForConfig(config);

        if (isManaged()) {
            fCommandArText.setEnabled(isStaticLibrary);

            fCommandObjcopyText.setEnabled(isExecutable);
            fCommandObjdumpText.setEnabled(isExecutable);
            fCommandSizeText.setEnabled(isExecutable);

            fFlashButton.setEnabled(isExecutable);
            fListingButton.setEnabled(isExecutable);
            fSizeButton.setEnabled(isExecutable);
        }
    }

    @Override
    protected void performApply(ICResourceDescription src, ICResourceDescription dst) {

        LlvmUIPlugin.log("ClangTabToolchains.performApply() " + src.getName()); //$NON-NLS-1$

        // need to apply changes in both configurations
        // dst is the new description, will be used when saving changes on disk
        // (set project description)
        // src is the old description used in current page
        IConfiguration config1 = getCfg(src.getConfiguration());
        IConfiguration config2 = getCfg(dst.getConfiguration());
        updateOptions(config1);
        updateOptions(config2);

        // does not work like this
        // SpecsProvider.clear();

        // LlvmUIPlugin.log("performApply()");
    }

    @Override
    protected void performOK() {

        IConfiguration config = getCfg();

        LlvmUIPlugin.log("Toolchains.performOK() " + config); //$NON-NLS-1$

        if (fLastUpdatedConfig != null && fLastUpdatedConfig.equals(config)) {
            updateOptions(config);
        } else {

            LlvmUIPlugin.log("skipped " + fConfig); //$NON-NLS-1$

        }
    }

    private void updateControlsForConfig(IConfiguration config) {

        LlvmUIPlugin.log("Toolchains.updateControlsForConfig() " + config.getName()); //$NON-NLS-1$

        // int fSelectedToolchainIndex;
        // String fSelectedToolchainName;
        

        if (!isThisPlugin()) {

            LlvmUIPlugin.log("not this plugin"); //$NON-NLS-1$

            return;
        }

        
        // create the selection array
        String[] toolchains = new String[ToolchainDefinition.getSize()];
        for (int i = 0; i < ToolchainDefinition.getSize(); ++i) {
            toolchains[i] = ToolchainDefinition.getToolchain(i).getFullName();
        }
        fToolchainCombo.setItems(toolchains);

        /**
         * TODO remove the below return to fix more errors.
         */
        if(config.getName() != null) {
           // return;
        }
        fSelectedToolchainName = ClangOption.getOptionStringValue(config, ClangOption.OPTION_TOOLCHAIN_NAME);

        if (fSelectedToolchainName != null && fSelectedToolchainName.length() > 0) {
            try {
                fSelectedToolchainIndex = ToolchainDefinition.findToolchainByName(fSelectedToolchainName);
            } catch (IndexOutOfBoundsException e) {
                fSelectedToolchainIndex = ToolchainDefinition.getDefault();
            }
        } else {

            LlvmUIPlugin.log("No toolchain selected"); //$NON-NLS-1$

            // This is not a project created with the wizard
            // (most likely it is the result of a toolchain change)
            fSelectedToolchainName = PersistentPreferences.getToolchainName();
            try {
                fSelectedToolchainIndex = ToolchainDefinition.findToolchainByName(fSelectedToolchainName);
            }catch (IndexOutOfBoundsException e) {
                //e.printStackTrace();
            }
            
            // Initialise .cproject options that were not done at project
            // creation by the toolchain wizard
            try {
                setOptionsForToolchain(config, fSelectedToolchainIndex);
            } catch (BuildException e1) {
                LlvmUIPlugin.log(e1);
            }
        }

        String toolchainSel = toolchains[fSelectedToolchainIndex];
        fToolchainCombo.setText(toolchainSel);

        ToolchainDefinition toolchainDefinition = ToolchainDefinition.getToolchain(fSelectedToolchainIndex);

        fArchitectureCombo.setItems(ToolchainDefinition.getArchitectures());

        String sSelectedArchitecture = ClangOption.getOptionStringValue(config, ClangOption.OPTION_ARCHITECTURE);
        int index;
        try {
            if (sSelectedArchitecture.endsWith("." + ClangOption.ARCHITECTURE_ARM)) //$NON-NLS-1$
                index = 0;
            else if (sSelectedArchitecture.endsWith("." + ClangOption.ARCHITECTURE_AARCH64)) //$NON-NLS-1$
                index = 1;
            else
                index = 0; // default is ARM
        } catch (NullPointerException e) {
            index = 0; // default is ARM
        }
        fArchitectureCombo.setText(ToolchainDefinition.getArchitecture(index));

        String prefix = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_PREFIX);
        if (prefix != null) {
            fPrefixText.setText(prefix);
        } else {
            fPrefixText.setText(toolchainDefinition.getPrefix());
        }

        String suffix = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_SUFFIX);
        if (suffix != null) {
            fSuffixText.setText(suffix);
        } else {
            fSuffixText.setText(toolchainDefinition.getSuffix());
        }

        String commandC = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_C);
        if (commandC != null) {
            fCommandCText.setText(commandC);
        } else {
            fCommandCText.setText(toolchainDefinition.getCmdC());
        }

        String commandCpp = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_CPP);
        if (commandCpp != null) {
            fCommandCppText.setText(commandCpp);
        } else {
            fCommandCppText.setText(toolchainDefinition.getCmdCpp());
        }

        if (isManaged()) {
            String commandAr = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_AR);
            if (commandAr != null) {
                fCommandArText.setText(commandAr);
            } else {
                fCommandArText.setText(toolchainDefinition.getCmdAr());
            }

            String commandObjcopy = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_OBJCOPY);
            if (commandObjcopy != null) {
                fCommandObjcopyText.setText(commandObjcopy);
            } else {
                fCommandObjcopyText.setText(toolchainDefinition.getCmdObjcopy());
            }

            String commandObjdump = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_OBJDUMP);
            if (commandObjdump != null) {
                fCommandObjdumpText.setText(commandObjdump);
            } else {
                fCommandObjdumpText.setText(toolchainDefinition.getCmdObjdump());
            }

            String commandSize = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_SIZE);
            if (commandSize != null) {
                fCommandSizeText.setText(commandSize);
            } else {
                fCommandSizeText.setText(toolchainDefinition.getCmdSize());
            }

            String commandMake = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_MAKE);
            if (commandMake != null) {
                fCommandMakeText.setText(commandMake);
            } else {
                fCommandMakeText.setText(toolchainDefinition.getCmdMake());
            }

            String commandRm = ClangOption.getOptionStringValue(config, ClangOption.OPTION_COMMAND_RM);
            if (commandRm != null) {
                fCommandRmText.setText(commandRm);
            } else {
                fCommandRmText.setText(toolchainDefinition.getCmdRm());
            }

            Boolean isCreateFlash = ClangOption.getOptionBooleanValue(config, ClangOption.OPTION_ADDTOOLS_CREATEFLASH);
            if (isCreateFlash != null) {
                fFlashButton.setSelection(isCreateFlash);
            } else {
                fFlashButton.setSelection(ClangOption.OPTION_ADDTOOLS_CREATEFLASH_DEFAULT);
            }

            Boolean isCreateListing = ClangOption.getOptionBooleanValue(config, ClangOption.OPTION_ADDTOOLS_CREATELISTING);
            if (isCreateListing != null) {
                fListingButton.setSelection(isCreateListing);
            } else {
                fListingButton.setSelection(ClangOption.OPTION_ADDTOOLS_CREATELISTING_DEFAULT);
            }

            Boolean isPrintSize = ClangOption.getOptionBooleanValue(config, ClangOption.OPTION_ADDTOOLS_PRINTSIZE);
            if (isPrintSize != null) {
                fSizeButton.setSelection(isPrintSize);
            } else {
                fSizeButton.setSelection(ClangOption.OPTION_ADDTOOLS_PRINTSIZE_DEFAULT);
            }
        }

        fConfig = config;

        LlvmUIPlugin.log("updateControlsForConfig() fConfig=" + fConfig); //$NON-NLS-1$

        fLastUpdatedConfig = config;

        updateToolchainPath(toolchainDefinition.getName());
    }

    private void updateOptions(IConfiguration config) {

        LlvmUIPlugin.log("Toolchains.updateOptions() " + config.getName()); //$NON-NLS-1$

        if (config instanceof MultiConfiguration) {
            MultiConfiguration multi = (MultiConfiguration) config;
            for (Object obj : multi.getItems()) {
                IConfiguration cfg = (IConfiguration) obj;
                updateOptions(cfg);
            }
            return;
        }
        IToolChain toolchain = config.getToolChain();

        IOption option;
        String val;

        try {
            // Do NOT use ManagedBuildManager.setOption() to avoid sending
            // events to the option. Also do not use option.setValue()
            // since this does not propagate notifications and the
            // values are not saved to .cproject.

            String sSelectedArchitecture = fArchitectureCombo.getText();
            if (ToolchainDefinition.getArchitecture(0).equals(sSelectedArchitecture)) {
                val = ClangOption.OPTION_ARCHITECTURE_ARM;
            } else if (ToolchainDefinition.getArchitecture(1).equals(sSelectedArchitecture)) {
                val = ClangOption.OPTION_ARCHITECTURE_AARCH64;
            } else {
                val = ClangOption.OPTION_ARCHITECTURE_ARM; // default is ARM
            }
            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARCHITECTURE); // $NON-NLS-1$
            config.setOption(toolchain, option, val);

            String sSelectedCombo = fToolchainCombo.getText();
            int index = ToolchainDefinition.findToolchainByFullName(sSelectedCombo);
            ToolchainDefinition td = ToolchainDefinition.getToolchain(index);
            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_TOOLCHAIN_NAME); // $NON-NLS-1$
            config.setOption(toolchain, option, td.getName());

            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_PREFIX); // $NON-NLS-1$
            config.setOption(toolchain, option, fPrefixText.getText().trim());

            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_SUFFIX); // $NON-NLS-1$
            config.setOption(toolchain, option, fSuffixText.getText().trim());

            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_C); // $NON-NLS-1$
            config.setOption(toolchain, option, fCommandCText.getText().trim());

            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_CPP); // $NON-NLS-1$
            config.setOption(toolchain, option, fCommandCppText.getText().trim());

            if (isManaged()) {
                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_AR); // $NON-NLS-1$
                config.setOption(toolchain, option, fCommandArText.getText().trim());

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_OBJCOPY); // $NON-NLS-1$
                config.setOption(toolchain, option, fCommandObjcopyText.getText().trim());

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_OBJDUMP); // $NON-NLS-1$
                config.setOption(toolchain, option, fCommandObjdumpText.getText().trim());

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_SIZE); // $NON-NLS-1$
                config.setOption(toolchain, option, fCommandSizeText.getText().trim());

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_MAKE); // $NON-NLS-1$
                config.setOption(toolchain, option, fCommandMakeText.getText().trim());

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_RM); // $NON-NLS-1$
                String oldValue = option.getStringValue();
                String newValue = fCommandRmText.getText().trim();

                if (newValue != null && !newValue.equals(oldValue)) {
                    config.setOption(toolchain, option, newValue);

                    // propagate is expensive, run it only if needed
                    propagateCommandRmUpdate(config);
                }

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ADDTOOLS_CREATEFLASH); // $NON-NLS-1$
                config.setOption(toolchain, option, fFlashButton.getSelection());

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ADDTOOLS_CREATELISTING); // $NON-NLS-1$
                config.setOption(toolchain, option, fListingButton.getSelection());

                option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ADDTOOLS_PRINTSIZE); // $NON-NLS-1$
                config.setOption(toolchain, option, fSizeButton.getSelection());

            }

        } catch (NullPointerException e) {
            LlvmUIPlugin.log(e);
        } catch (BuildException e) {
            LlvmUIPlugin.log(e);
        }

    }

    // Used in SetCrossCommandOperation to set toolchain specific options
    // after wizard selection. The compiler command name must be set as
    /**
     * Sets the options for toolchain.
     *
     * @param config the IConfiguration
     * @param toolchainIndex the toolchain index
     * @throws BuildException the build exception
     */
    // early as possible.
    public static void setOptionsForToolchain(IConfiguration config, int toolchainIndex) throws BuildException {

        IToolChain toolchain = config.getToolChain();

        IOption option;
        String val;

        ToolchainDefinition td = ToolchainDefinition.getToolchain(toolchainIndex);

        // Do NOT use ManagedBuildManager.setOption() to avoid sending
        // events to the option. Also do not use option.setValue()
        // since this does not propagate notifications and the
        // values are not saved to .cproject.
        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_TOOLCHAIN_NAME); // $NON-NLS-1$
        String name = td.getName();
        if(name == null) {
            name = ""; //$NON-NLS-1$
        }
        config.setOption(toolchain, option, td.getName());

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARCHITECTURE); // $NON-NLS-1$
        // compose the architecture ID
        String sArchitecture = td.getArchitecture();
        val = ClangOption.OPTION_ARCHITECTURE + "." + sArchitecture; //$NON-NLS-1$
        Utils.setOptionForced(config, toolchain, option, val);

        if ("arm".equals(sArchitecture)) { //$NON-NLS-1$
            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARM_TARGET_FAMILY);
            Utils.forceOptionRewrite(config, toolchain, option);

            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ARM_TARGET_INSTRUCTIONSET);
            Utils.forceOptionRewrite(config, toolchain, option);
        } else if ("aarch64".equals(sArchitecture)) { //$NON-NLS-1$
            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_AARCH64_TARGET_FAMILY);
            Utils.setOptionForced(config, toolchain, option, ClangOption.OPTION_AARCH64_MCPU_GENERIC);

            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_AARCH64_FEATURE_SIMD);
            Utils.setOptionForced(config, toolchain, option, ClangOption.OPTION_AARCH64_FEATURE_SIMD_ENABLED);

            option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_AARCH64_CMODEL);
            Utils.setOptionForced(config, toolchain, option, ClangOption.OPTION_AARCH64_CMODEL_SMALL);
        }

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_PREFIX); // $NON-NLS-1$
        if (option != null) {
            config.setOption(toolchain, option, td.getPrefix());
        }
        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_SUFFIX); // $NON-NLS-1$
        if (option != null) {
            config.setOption(toolchain, option, td.getSuffix());
        }
        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_C); // $NON-NLS-1$
        if (option != null) {
            config.setOption(toolchain, option, td.getCmdC());
        }

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_CPP); // $NON-NLS-1$
        if (option != null) {
            config.setOption(toolchain, option, td.getCmdCpp());
        }
        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_AR); // $NON-NLS-1$
        config.setOption(toolchain, option, td.getCmdAr());

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_OBJCOPY); // $NON-NLS-1$
        config.setOption(toolchain, option, td.getCmdObjcopy());

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_OBJDUMP); // $NON-NLS-1$
        config.setOption(toolchain, option, td.getCmdObjdump());

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_SIZE); // $NON-NLS-1$
        config.setOption(toolchain, option, td.getCmdSize());

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_MAKE); // $NON-NLS-1$
        config.setOption(toolchain, option, td.getCmdMake());

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_RM); // $NON-NLS-1$
        config.setOption(toolchain, option, td.getCmdRm());

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ADDTOOLS_CREATEFLASH); // $NON-NLS-1$
        config.setOption(toolchain, option, ClangOption.OPTION_ADDTOOLS_CREATEFLASH_DEFAULT);

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ADDTOOLS_CREATELISTING); // $NON-NLS-1$
        config.setOption(toolchain, option, ClangOption.OPTION_ADDTOOLS_CREATELISTING_DEFAULT);

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_ADDTOOLS_PRINTSIZE); // $NON-NLS-1$
        config.setOption(toolchain, option, ClangOption.OPTION_ADDTOOLS_PRINTSIZE_DEFAULT);

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_READELF); // $NON-NLS-1$
        if (option != null) {
            config.setOption(toolchain, option, td.getCmdReadElf());
        }

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_NM); // $NON-NLS-1$
        if (option != null) {
            config.setOption(toolchain, option, td.getCmdNM());
        }

        option = toolchain.getOptionBySuperClassId(ClangOption.OPTION_COMMAND_ADDR2LINE); // $NON-NLS-1$
        if (option != null) {
            config.setOption(toolchain, option, td.getCmdAddr2line());
        }

        // do not set the project toolchain path
    }

    /**
     * Propagate command rm update.
     *
     * @param config the IConfiguration
     */
    private static void propagateCommandRmUpdate(IConfiguration config) {
        // LlvmUIPlugin.log("propagateCommandRmUpdate()");
        if (true) {
            IProject project = (IProject) config.getOwner();

            IPath makefilePath = project.getFullPath().append(config.getName())
                    .append(IManagedBuilderMakefileGenerator.MAKEFILE_NAME);
            IResource makefileResource = project.findMember(makefilePath.removeFirstSegments(1));
            if (makefileResource != null && makefileResource.exists()) {
                try {
                    makefileResource.delete(true, new NullProgressMonitor());

                    GnuMakefileGenerator makefileGenerator = new GnuMakefileGenerator();
                    makefileGenerator.initialize(0, config, config.getBuilder(), new NullProgressMonitor());
                    makefileGenerator.regenerateMakefiles();
                } catch (CoreException e) {
                    LlvmUIPlugin.log(e.getStatus());
                }
            }
        }
    }

    @Override
    protected void performDefaults() {

        LlvmUIPlugin.log("Toolchains.performDefaults()"); //$NON-NLS-1$

        updateInterfaceAfterToolchainChange();

        if (isManaged()) {
            fFlashButton.setSelection(ClangOption.OPTION_ADDTOOLS_CREATEFLASH_DEFAULT);
            fListingButton.setSelection(ClangOption.OPTION_ADDTOOLS_CREATELISTING_DEFAULT);
            fSizeButton.setSelection(ClangOption.OPTION_ADDTOOLS_PRINTSIZE_DEFAULT);
            // LlvmUIPlugin.log("performDefaults()");
        }
    }

    @Override
    public boolean canBeVisible() {

        if (!isThisPlugin())
            return false;

        if (page.isForProject()) {
            return true;
            // if (page.isMultiCfg()) {
            // ICMultiItemsHolder mih = (ICMultiItemsHolder) getCfg();
            // IConfiguration[] cfs = (IConfiguration[]) mih.getItems();
            // for (int i = 0; i < cfs.length; i++) {
            // if (cfs[i].getBuilder().isManagedBuildOn())
            // return true;
            // }
            // return false;
            // } else {
            //
            // return getCfg().getBuilder().isManagedBuildOn();
            // }
        }
        return false;
    }

    // Must be true, otherwise the page is not shown
    @Override
    public boolean canSupportMultiCfg() {
        return true;
    }

    @Override
    protected void updateButtons() {
        // Do nothing. No buttons to update.
    }

    private boolean isThisPlugin() {

        fConfig = getCfg();

        LlvmUIPlugin.log("isThisPlugin() fConfig=" + fConfig); //$NON-NLS-1$

        IToolChain toolchain = fConfig.getToolChain();
        String sToolchainId = toolchain.getBaseId();
        if (!sToolchainId.startsWith(ClangOption.TOOLCHAIN_PREFIX + ".")) { //$NON-NLS-1$
            return false;
        }

        return true;
    }

    /** The Constant TYPE_PREFIX. */
    public static final String TYPE_PREFIX = "com.lembed.lite.studio.managedbuild.cross.llvm.target"; //$NON-NLS-1$

    private boolean isManaged() {

        fConfig = getCfg();

        IManagedProject managedProject = fConfig.getManagedProject();
        IProjectType projectType = managedProject.getProjectType();

        if (projectType == null || !projectType.getId().startsWith(TYPE_PREFIX)) {
            return false;
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.cdt.ui.newui.AbstractCPropertyTab#handleTabEvent(int,
     * java.lang.Object)
     */
    @Override
    public void handleTabEvent(int kind, Object data) {
        super.handleTabEvent(kind, data);

        switch (kind) {
        case ICPropertyTab.UPDATE: {
            /*
             * If the page needs updating fire the handleMessage() method. This redraws the
             * tabs and if the page visibility has been modified the tab will be updated
             * (removed or added). This is necessary to solved the following problem: When
             * in project properties > C/C++ Build > Settings, if you change to another
             * configuration that uses a different toolchain (eg Linux GCC) using the
             * Configuration combo (at the top of the dialog) the tabs in Settings do not
             * get updated. Note, data is not used so set to null.
             */
            Page_BuildSettings pageConcrete = (Page_BuildSettings) ((ICPropertyProvider2) page);
            pageConcrete.handleMessage(ICPropertyTab.MANAGEDBUILDSTATE, null);
        }
            break;
        default:
            break;
        }
    }

    // ------------------------------------------------------------------------
}
