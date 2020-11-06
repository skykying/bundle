package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.model.ConfigurationModel;
import com.lembed.lite.studio.qemu.model.LastUsedFileEnumModel;
import com.lembed.lite.studio.qemu.model.LastUsedFileModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderEnumModel;
import com.lembed.lite.studio.qemu.model.LastUsedFolderModel;
import com.lembed.lite.studio.qemu.model.Model;
import com.lembed.lite.studio.qemu.view.internal.ConfigurationView;

public class ConfigurationControl implements ActionListener {

    private ConfigurationView configurationView;

    private ConfigurationModel configurationModel;

    private FileControl fileControl;

    private LastUsedFolderModel lastUsedFolderModel;

    private LastUsedFileModel lastUsedFileModel;

    private Boolean isConfigured;

    public ConfigurationControl(LastUsedFolderModel myLastUsedFolderModel,
            LastUsedFileModel myLastUsedFileModel) {
        this.configurationView = new ConfigurationView();
        this.configurationView.setVisible(true);
        this.configurationView.configureListener(this);
        this.configurationView.configureStandardMode();

        this.lastUsedFolderModel = myLastUsedFolderModel;
        this.lastUsedFileModel = myLastUsedFileModel;
        this.configurationModel = new ConfigurationModel(this.lastUsedFolderModel, this.lastUsedFileModel);

        this.fileControl = new FileControl(this.configurationView.getWindowContent(), null);

        if (Model.isValidString(this.lastUsedFileModel.getLastUsedFile(
                LastUsedFileEnumModel.LOADJAVAQEMUCONFIGURATIONFILE.getValor()))) {
            if (Model.isValidString(this.lastUsedFolderModel.getLastUsedFolder(
                    LastUsedFolderEnumModel.OPENEXISTINGJAVAQEMUCONFIGURATION.getValor()))) {
                this.fileControl.getFilemodel().readConfigurationFromXML(
                        Model.combine(this.lastUsedFolderModel.getLastUsedFolder(
                                LastUsedFolderEnumModel.OPENEXISTINGJAVAQEMUCONFIGURATION.getValor()),
                                this.lastUsedFileModel.getLastUsedFile(
                                        LastUsedFileEnumModel.LOADJAVAQEMUCONFIGURATIONFILE.getValor())));
                this.fileControl.getFilemodel().getConfiguration(
                        this.configurationView.getDefault_virtual_machines_path_choice(),
                        this.configurationView.getExecute_before_start_qemu_choices(),
                        this.configurationView.getQemu_executable_path_choice(),
                        this.configurationView.getExecute_after_stop_qemu_choices(),
                        this.configurationView.getQemu_img_executable_path_choice(),
                        this.configurationView.getBios_vga_bios_keymaps_path_choice());
                this.configurationModel.setDefault_virtual_machines_path(this.fileControl.getFilemodel().getDefaultVMPath());
                this.configurationModel.setExecute_before_start_qemu(this.configurationView.getExecute_before_start_qemu_choices());
                this.configurationModel.setQemu_executable_path(this.configurationView.getQemu_executable_path_choice());
                this.configurationModel.setExecute_after_stop_qemu(this.configurationView.getExecute_after_stop_qemu_choices());
                this.configurationModel.setQemu_img_executable_path(this.configurationView.getQemu_img_executable_path_choice());
                this.configurationModel.setBios_vga_bios_keymaps_path(this.configurationView.getBios_vga_bios_keymaps_path_choice());
            }
        }

        this.isConfigured = false;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ExitCommand")) {
            this.configurationView.setVisible(false);
        } else if (e.getActionCommand().equals("DirectoryChooser")) {
            this.setChoosertitle("JavaQemu Configuration - Choose the default VM directory!");
            this.configurationView.setFileDescription("Default VM Directory");
            if (this.lastUsedFolderModel
                    .getLastUsedFolder(LastUsedFolderEnumModel.SETDEFAULTVMDIRECTORY
                            .getValor()) != null) {
                if (!this.lastUsedFolderModel.getLastUsedFolder(
                        LastUsedFolderEnumModel.SETDEFAULTVMDIRECTORY
                        .getValor()).equals(".")) {
                    this.configurationView
                            .setCurrentDirectory(this.lastUsedFolderModel
                                    .getLastUsedFolder(LastUsedFolderEnumModel.SETDEFAULTVMDIRECTORY
                                            .getValor()));
                } else if (this.configurationView.getDefault_virtual_machines_path_choice()
                        .getText().isEmpty()) {
                    this.configurationView.setCurrentDirectory(".");
                } else {
                    this.configurationView.setCurrentDirectory(this.configurationView
                            .getDefault_virtual_machines_path_choice()
                            .getText());
                }
            } else if (this.configurationView.getDefault_virtual_machines_path_choice()
                    .getText().isEmpty()) {
                this.configurationView.setCurrentDirectory(".");
            } else {
                this.configurationView.setCurrentDirectory(this.configurationView
                        .getDefault_virtual_machines_path_choice()
                        .getText());
            }

            if (this.configurationView.chooseDirectoryForDefaultVMPath()) {
                this.configurationModel.setDefault_virtual_machines_path(this.configurationView
                        .getChoice());
            }
            this.configurationModel.setDefault_virtual_machines_option(true);
        } else if (e.getActionCommand().equals("OK")) {
            boolean do_my_view_invisible = true;
            if (this.configurationModel.isDefault_virtual_machines_option()
                    && !this.checks_if_JTextField_is_changed(this.configurationView
                            .getDefault_virtual_machines_path_choice(),
                            this.configurationModel.getDefault_virtual_machines_path())) {
                if (this.configurationView.getChooser() != null) {
                    this.configurationModel.setDefault_virtual_machines_path(this.configurationView
                            .getDefault_virtual_machines_path_choice()
                            .getText());
                }
            }
            if (this.checks_if_JTextField_is_changed(
                    this.configurationView.getDefault_virtual_machines_path_choice(),
                    this.configurationModel.getDefault_virtual_machines_path())) {
                File file = new File(this.configurationView
                        .getDefault_virtual_machines_path_choice().getText());
                if (file.isDirectory()) {
                    this.configurationModel
                            .setDefault_virtual_machines_path_with_boolean(this.configurationView
                                    .getDefault_virtual_machines_path_choice()
                                    .getText());
                } else {
                    this.configurationView
                            .showMessage("Please, reconfigure the path of the default Virtual Machines first!\n"
                                    + "This path should be the path of a directory!\n"
                                    + "You can to cancel the configuration and the pending choices will be saved!");
                    do_my_view_invisible = false;
                }
            }

            this.configurationModel.setExecute_before_start_qemu(this.configurationView
                    .getExecute_before_start_qemu_choices());
            File file = new File(this.configurationView.getQemu_executable_path_choice()
                    .getText());
            if (file.isFile()) {
                this.configurationModel.setQemu_executable_path(this.configurationView
                        .getQemu_executable_path_choice());
            } else {
                this.configurationView
                        .showMessage("Please, reconfigure the path of the qemu executable first!\n"
                                + "This path should be the path of a file!\n"
                                + "You can to cancel the configuration and the pending choices will be saved!");
                do_my_view_invisible = false;
            }
            this.configurationModel.setExecute_after_stop_qemu(this.configurationView
                    .getExecute_after_stop_qemu_choices());
            File anotherfile = new File(this.configurationView
                    .getQemu_img_executable_path_choice().getText());
            if (anotherfile.isFile()) {
                this.configurationModel.setQemu_img_executable_path(this.configurationView
                        .getQemu_img_executable_path_choice());
            } else {
                this.configurationView
                        .showMessage("Please, reconfigure the path of the qemu-img executable first!\n"
                                + "This path should be the path of a file!\n"
                                + "You can to cancel the configuration and the pending choices will be saved!");
                do_my_view_invisible = false;
            }

            if (this.configurationModel.isBios_vga_bios_keymaps_option()
                    && !this.checks_if_JTextField_is_changed(this.configurationView
                            .getBios_vga_bios_keymaps_path_choice(),
                            this.configurationModel.getBios_vga_bios_keymaps_path().getText())) {
                if (this.configurationView.getChooser() != null) {
                    this.configurationModel.setBios_vga_bios_keymaps_path(this.configurationView
                            .getBios_vga_bios_keymaps_path_choice());
                }
            }

            if (do_my_view_invisible) {
                this.configurationView.setVisible(false);
                if (!this.isConfigured) {
                    this.isConfigured = true;
                }

                if (this.configurationView.getJavaQemu_configuration_file_path() != null) {
                    this.fileControl.getFilemodel().setConfiguration(
                            this.configurationView.getDefault_virtual_machines_path_choice(),
                            this.configurationView.getExecute_before_start_qemu_choices(),
                            this.configurationView.getQemu_executable_path_choice(),
                            this.configurationView.getExecute_after_stop_qemu_choices(),
                            this.configurationView.getQemu_img_executable_path_choice(),
                            this.configurationView.getBios_vga_bios_keymaps_path_choice());
                    this.fileControl.getFilemodel().saveConfigurationToXML(
                            this.configurationView.getJavaQemu_configuration_file_path());
                }
            } else if (this.isConfigured) {
                this.isConfigured = false;
            }
        } else if (e.getActionCommand().equals("Hide")) {
            this.configurationView.setVisible(false);
        } else if (e.getActionCommand().equals("QemuChooser")) {
            this.setChoosertitle("JavaQemu Configuration - Choose the qemu executable file!");
            if (Model
                    .isValidString(this.lastUsedFolderModel
                            .getLastUsedFolder(LastUsedFolderEnumModel.SETQEMUEXECUTABLEDIRECTORY
                                    .getValor()))) {
                this.configurationView
                        .setCurrentDirectory(this.lastUsedFolderModel
                                .getLastUsedFolder(LastUsedFolderEnumModel.SETQEMUEXECUTABLEDIRECTORY
                                        .getValor()));
            }
            this.configurationView.setFileDescription("Qemu Executable File");
            this.configurationView.chooseFile(true);
        } else if (e.getActionCommand().equals("QemuImgChooser")) {
            this.setChoosertitle("JavaQemu Configuration - Choose the qemu-img executable file!");
            if (Model.isValidString(this.lastUsedFolderModel.getLastUsedFolder(
                    LastUsedFolderEnumModel.SETQEMUIMGEXECUTABLEDIRECTORY.getValor()))) {
                this.configurationView
                        .setCurrentDirectory(this.lastUsedFolderModel
                                .getLastUsedFolder(LastUsedFolderEnumModel.SETQEMUIMGEXECUTABLEDIRECTORY.getValor()));
            }
            this.configurationView.setFileDescription("Qemu-img Executable File");
            this.configurationView.chooseFile(false);
        } else if (e.getActionCommand().equals("SaveConfiguration")) {
            this.setChoosertitle("JavaQemu Configuration - Choose the JavaQemu Configuration XML File!");
            this.configurationView.setCurrentDirectory(
                    this.lastUsedFolderModel.getLastUsedFolder(
                            LastUsedFolderEnumModel.SAVEEXISTINGJAVAQEMUCONFIGURATION.getValor())
            );
            if (this.configurationView.chooseConfigurationFileToBeSaved()) {
                this.lastUsedFolderModel.setLastUsedFolder(
                        LastUsedFolderEnumModel.SAVEEXISTINGJAVAQEMUCONFIGURATION.getValor(), (new File(this.configurationView.getJavaQemu_configuration_file_path())).getParent());
                this.fileControl.getFilemodel().setConfiguration(
                        this.configurationView.getDefault_virtual_machines_path_choice(),
                        this.configurationView.getExecute_before_start_qemu_choices(),
                        this.configurationView.getQemu_executable_path_choice(),
                        this.configurationView.getExecute_after_stop_qemu_choices(),
                        this.configurationView.getQemu_img_executable_path_choice(),
                        this.configurationView.getBios_vga_bios_keymaps_path_choice());
                this.fileControl.getFilemodel().saveConfigurationToXML(
                        this.configurationView.getJavaQemu_configuration_file_path());
            }
        } else if (e.getActionCommand().equals("OpenConfiguration")) {
            this.setChoosertitle("JavaQemu Configuration - Choose the JavaQemu Configuration XML File!");
            this.configurationView.setCurrentDirectory(
                    this.lastUsedFolderModel.getLastUsedFolder(
                            LastUsedFolderEnumModel.OPENEXISTINGJAVAQEMUCONFIGURATION.getValor())
            );
            if (this.configurationView.chooseConfigurationFileToBeOpened()) {
                this.lastUsedFolderModel.setLastUsedFolder(
                        LastUsedFolderEnumModel.OPENEXISTINGJAVAQEMUCONFIGURATION.getValor(), (new File(this.configurationView.getJavaQemu_configuration_file_path())).getParent());
                this.lastUsedFileModel.setLastUsedFile(
                        LastUsedFileEnumModel.LOADJAVAQEMUCONFIGURATIONFILE.getValor(),
                        (new File(this.configurationView.getJavaQemu_configuration_file_path())).getName());
                this.fileControl.getFilemodel().readConfigurationFromXML(
                        this.configurationView.getJavaQemu_configuration_file_path());
                this.fileControl.getFilemodel().getConfiguration(
                        this.configurationView.getDefault_virtual_machines_path_choice(),
                        this.configurationView.getExecute_before_start_qemu_choices(),
                        this.configurationView.getQemu_executable_path_choice(),
                        this.configurationView.getExecute_after_stop_qemu_choices(),
                        this.configurationView.getQemu_img_executable_path_choice(),
                        this.configurationView.getBios_vga_bios_keymaps_path_choice());
                this.configurationModel.setDefault_virtual_machines_path(this.fileControl.getFilemodel().getDefaultVMPath());
                this.configurationModel.setExecute_before_start_qemu(this.configurationView.getExecute_before_start_qemu_choices());
                this.configurationModel.setQemu_executable_path(this.configurationView.getQemu_executable_path_choice());
                this.configurationModel.setExecute_after_stop_qemu(this.configurationView.getExecute_after_stop_qemu_choices());
                this.configurationModel.setQemu_img_executable_path(this.configurationView.getQemu_img_executable_path_choice());
                this.configurationModel.setBios_vga_bios_keymaps_path(this.configurationView.getBios_vga_bios_keymaps_path_choice());
            }
        } else if (e.getActionCommand().equals("LDirectoryPathChooser")) {
            this.setChoosertitle("JavaQemu Configuration - Choose the directory for the BIOS, VGA BIOS and keymaps!");
            this.configurationView.setCurrentDirectory(
                    this.lastUsedFolderModel.getLastUsedFolder(
                            LastUsedFolderEnumModel.SETLOPTIONDIRECTORY.getValor()));
            this.configurationView.setFileDescription("Directory for the BIOS, VGA BIOS and keymaps");
            if (this.configurationView.chooseDirectoryForBiosVgaBiosKeymapsPath()) {
                this.configurationModel.setBios_vga_bios_keymaps_path(
                        new JTextField(this.configurationView.getChoice()));
                this.lastUsedFolderModel.setLastUsedFolder(LastUsedFolderEnumModel.SETLOPTIONDIRECTORY.getValor(),
                        this.configurationView.getChoice());
            }
            this.configurationModel.setBios_vga_bios_keymaps_option(true);
        }
    }

    public void do_my_view_visible() {
        this.configurationView.setVisible(true);
    }

    public void setQemu_executable_path() {
        this.configurationModel.setQemu_executable_path(this.configurationView
                .getQemu_executable_path_choice());
    }

    public void setBios_vga_bios_keymaps_path() {
        this.configurationModel.setBios_vga_bios_keymaps_path(
                this.configurationView.getBios_vga_bios_keymaps_path_choice());
    }

    public boolean checks_if_JTextField_is_changed(JTextField textField,
            String string) {
        if (textField.getText().equals(string)) {
            return false;
        } else {
            return true;
        }
    }

    public String getDefault_virtual_machines_path() {
        return this.configurationModel.getDefault_virtual_machines_path();
    }

    public JTextArea getExecute_before_start_qemu() {
        return this.configurationModel.getExecute_before_start_qemu();
    }

    public JTextField getQemu_executable_path() {
        return this.configurationModel.getQemu_executable_path();
    }

    public JTextField getQemu_img_executable_path() {
        return this.configurationModel.getQemu_img_executable_path();
    }

    public JTextArea getExecute_after_stop_qemu() {
        return this.configurationModel.getExecute_after_stop_qemu();
    }

    public JTextField getBios_vga_bios_keymaps_path() {
        return this.configurationModel.getBios_vga_bios_keymaps_path();
    }

    public void setChoosertitle(String title) {
        this.configurationView.setChoosertitle(title);
    }

    public Boolean getIsConfigured() {
        return isConfigured;
    }
}
