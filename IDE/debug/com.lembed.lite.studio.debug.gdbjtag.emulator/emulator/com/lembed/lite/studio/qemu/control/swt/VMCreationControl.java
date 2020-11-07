package com.lembed.lite.studio.qemu.control.swt;

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import com.lembed.lite.studio.qemu.model.swt.VMCreationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.JQemuView;
import com.lembed.lite.studio.qemu.view.internal.swt.VMCreationViewEnd;
import com.lembed.lite.studio.qemu.view.internal.swt.VMCreationViewPart1;
import com.lembed.lite.studio.qemu.view.internal.swt.VMCreationViewPart2;
import com.lembed.lite.studio.qemu.view.internal.swt.VMCreationViewPart3;

public class VMCreationControl implements ActionListener {

    private VMCreationViewPart1 myfirstview;

    private VMCreationViewPart2 mysecondview;

    private VMCreationViewPart3 mythirdview;

    private VMCreationViewEnd vmCreationViewEnd;

    private VMCreationModel vmCreationModel;

    private DiskCreationControl diskCreationControl;

    private EmulatorQemuMachineControl fileControl;

    private JQemuView jQemuView;

    private EmulationControl emulationControl;

    private VMOpeningControl vmOpeningControl;

    private List<VMConfigurationControl> vmConfigurationControlList;

    public VMCreationControl(DiskCreationControl mydisk, String pathQemu_img,
            String default_virtual_machines_path, JQemuView view, EmulatorQemuMachineControl file,
            EmulationControl emulation,
            List<VMConfigurationControl> vmcontrol) {
        myfirstview = new VMCreationViewPart1();
        myfirstview.setVisible(true);
        myfirstview.configureListener(this);
        myfirstview.configureStandardMode();

        vmCreationModel = new VMCreationModel();
        vmCreationModel.setQemu_imgPath(pathQemu_img);

        mysecondview = new VMCreationViewPart2(
                default_virtual_machines_path,
                vmCreationModel.checks_extension(default_virtual_machines_path));
        mysecondview.setVisible(false);
        mysecondview.configureListener(this);
        mysecondview.configureStandardMode();

        mythirdview = new VMCreationViewPart3();
        mythirdview.setVisible(false);
        mythirdview.configureListener(this);
        mythirdview.configureStandardMode();

        vmCreationViewEnd = new VMCreationViewEnd();
        vmCreationViewEnd.setVisible(false);
        vmCreationViewEnd.configureListener(this);
        vmCreationViewEnd.configureStandardMode();

        diskCreationControl = mydisk;
        jQemuView = view;

        fileControl = file;

        emulationControl = emulation;

        vmConfigurationControlList = vmcontrol;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Cancel1")) {
            myfirstview.setVisible(false);
        } else if (e.getActionCommand().equals("Next1")) {
            if (myfirstview.getFieldChoooseMachineName().getText()
                    .isEmpty()) {
                myfirstview.showMessage("Please, type a valid name!");
            } else {
                myfirstview.setVisible(false);
                vmCreationModel.setChosenMachineName(myfirstview
                        .getFieldChoooseMachineName().getText());
                mysecondview.setChooseMachineName(vmCreationModel
                        .getChosenMachineName());
                mysecondview.rechecks();
                mysecondview.setVisible(true);

            }
        } else if (e.getActionCommand().equals("Cancel2")) {
            mysecondview.setVisible(false);
        } else if (e.getActionCommand().equals("Back1")) {
            mysecondview.setVisible(false);
            myfirstview.setVisible(true);
        } else if (e.getActionCommand().equals("Next2")) {
            if (mysecondview.getFieldChooseVMPath().getText().isEmpty()) {
                mysecondview
                        .showMessage("Please, type a valid directory!");
            } else if (!vmCreationModel
                    .checks_if_is_a_valid_directory(mysecondview
                            .getBasePath())) {
                mysecondview
                        .showMessage("Please, type a valid directory for the default machines path choice in the configuration section!");
            } else {
                mythirdview.getDiskName().setText(
                        myfirstview.getFieldChoooseMachineName()
                        .getText());
                File f = new File(mysecondview.getFieldChooseVMPath()
                        .getText()
                        + vmCreationModel.checks_extension(mysecondview
                                .getFieldChooseVMPath().getText())
                        + myfirstview.getFieldChoooseMachineName()
                        .getText() + ".xml");
                if (f.exists()) {
                    mysecondview.showMessage("Warning:\nThe file '"
                            + (mysecondview.getFieldChooseVMPath()
                            .getText()
                            + vmCreationModel.checks_extension(mysecondview
                                    .getFieldChooseVMPath().getText())
                            + myfirstview.getFieldChoooseMachineName()
                            .getText() + ".xml")
                            + "' already exists!\n"
                            + "old VM will be overwritten!");
                }
                mysecondview.setVisible(false);
                mythirdview.setVisible(true);
            }

        } else if (e.getActionCommand().equals("Cancel3")) {
            mythirdview.setVisible(false);
        } else if (e.getActionCommand().equals("Back2")) {
            mythirdview.setVisible(false);
            mysecondview.setVisible(true);
        } else if (e.getActionCommand().equals("Finish")) {
            vmCreationModel.setDiskImageSize(Double.parseDouble(mythirdview
                    .getEditor().getTextField().getText().replace(",", ".")));
            diskCreationControl = new DiskCreationControl(
                    (vmCreationModel.getDiskImageSize() * 1024) + "M", null);
            diskCreationControl.setPathQemu_img(vmCreationModel.getQemu_imgPath());
            diskCreationControl.setDefault_virtual_machines_path(mysecondview
                    .getFieldChooseVMPath().getText());
            if (mythirdview.getDiskExtension().getItemAt(
                            mythirdview.getDiskExtension()
                            .getSelectedIndex()) != null)
            {
                diskCreationControl.setFileName(
                    mythirdview.getDiskName().getText(),
                    mythirdview.getDiskExtension().getItemAt(
                            mythirdview.getDiskExtension()
                            .getSelectedIndex()));
            }
            mythirdview.setVisible(false);
            diskCreationControl.createsAdditionalDirectory(mysecondview
                    .getFieldChooseVMPath().getText());
            try {
                diskCreationControl
                        .runsThisIfFalse((String) mythirdview.getDiskExtension()
                                .getSelectedItem(),
                                setOptionCreationNewVM(
                                        (String) mythirdview
                                        .getDiskExtension()
                                        .getSelectedItem(),
                                        getObjects((String) mythirdview
                                                .getDiskExtension()
                                                .getSelectedItem()),
                                        getMoreOptions(getObjects((String) mythirdview
                                                .getDiskExtension()
                                                .getSelectedItem()))));
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                diskCreationControl.showsOutput();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (diskCreationControl.getMessage() != null) {
                if (diskCreationControl.getMessage().contains("Formatting")
                        || diskCreationControl.getMessage().contains("Creating")) {
                    jQemuView.showMessage("The disk image was created!");
                }
            }
            vmCreationViewEnd.setVisible(true);
        } else if (e.getActionCommand().equals("No_option_pos_creation")) {
            vmCreationViewEnd.setVisible(false);
            fileControl.getMachineModel()
                    .setFirstHardDiskOption(diskCreationControl.getFileName());
            fileControl.getMachineModel().setMachineName(
                    vmCreationModel.getChosenMachineName());
            fileControl.getMachineModel().setRamSize("128");
            fileControl.getMachineModel().saveToXML(
                    mysecondview.getFieldChooseVMPath().getText()
                    + fileControl.getMachineModel().checks_extension(
                            mysecondview.getFieldChooseVMPath()
                            .getText())
                    + vmCreationModel.getChosenMachineName() + ".xml");
        } else if (e.getActionCommand().equals("Yes_option_pos_creation")) {
            vmCreationViewEnd
                    .showMessage("You chose to open the new virtual machine!");
            vmCreationViewEnd.setVisible(false);

            fileControl.getMachineModel()
                    .setFirstHardDiskOption(diskCreationControl.getFileName());
            fileControl.getMachineModel().setMachineName(
                    vmCreationModel.getChosenMachineName());
            fileControl.getMachineModel().setRamSize("128");
            fileControl.getMachineModel().saveToXML(
                    mysecondview.getFieldChooseVMPath().getText()
                    + fileControl.getMachineModel().checks_extension(
                            mysecondview.getFieldChooseVMPath()
                            .getText())
                    + vmCreationModel.getChosenMachineName() + ".xml");

            if (vmOpeningControl == null) {
                vmOpeningControl = new VMOpeningControl(jQemuView,
                        emulationControl, fileControl);
            }
            vmOpeningControl.starts(fileControl);
            int position;
            if (jQemuView.getActivePanel() == 0) {
                position = jQemuView.getSizeOfJTabbedPane();
            } else {
                position = jQemuView.getActivePanel();
            }
            if (vmConfigurationControlList.size() <= position) {
                for (int i = vmConfigurationControlList.size(); i <= position; i++) {
                    vmConfigurationControlList.add(i, null);
                }
            }
            if (vmConfigurationControlList.get(position) == null) {
                vmConfigurationControlList.set(position,
                        new VMConfigurationControl(emulationControl,
                                jQemuView, fileControl));
                vmConfigurationControlList.get(position).starts();
                vmConfigurationControlList.get(position).getMyName().updateMe();
            }

            emulationControl.setJPanel();
        } else if (e.getActionCommand().equals("VM_Path_Chooser")) {
            mysecondview.setFileDescription("VM Path Directory");
            if (mysecondview.chooseDirectoryForDefaultVMPath()) {
                mysecondview.getFieldChooseVMPath().setText(
                        mysecondview.getChoice());
            }
        } else if (e.getActionCommand().equals("DiskExtension")) {
            mythirdview.addsComponent((String) mythirdview
                    .getDiskExtension().getSelectedItem());
        }
        emulationControl.change_options(OptionsEnumModel.RAMSIZE.getValor(),
                "-m " + fileControl.getMachineModel().getRamSize());
        emulationControl.appends_options();
    }

    public void starts() {
        myfirstview.initialize();
        myfirstview.setVisible(true);

        mysecondview.initialize();

        mythirdview.initialize();

        vmCreationViewEnd.initialize();
    }

    public List<String> setOptionCreationNewVM(String option, List<ItemSelectable> objects,
            List<JComboBox<String>> moreOptions) {
        List<String> options = new ArrayList<String>();
        /*
		 * From:
		 * https://www.google.com.br/?gfe_rd=cr&ei=CzqIVIr2O4mX8Qe484CwAQ#q=check+if+object+is+instance+of+class+java&safe=active
		 * To:
		 * http://stackoverflow.com/questions/541749/how-to-determine-an-objects-class-in-java
         */
        if (option.equals(".qcow")) {
            for (Object object : objects) {
                if (object instanceof JRadioButton) {
                    if (((JRadioButton) object).isSelected()) {
                        options.add("encryption");
                    }
                } else if (object instanceof JCheckBox) {
                    if (((JCheckBox) object).isSelected()) {
                        options.add("encryption");
                    }
                }
            }
        } else if (option.equals(".qcow2")) {
            for (Object object : objects) {
                if (object instanceof JCheckBox) {
                    if (((JCheckBox) object).getText().equals("Encryption mode: on")) {
                        if (((JCheckBox) object).isSelected()) {
                            options.add("encryption");
                        }
                    }
                    if (((JCheckBox) object).getText().equals("Preallocation mode: metadata")) {
                        if (((JCheckBox) object).isSelected()) {
                            options.add("preallocation=metadata");
                        }
                    }
                    if (((JCheckBox) object).getText().equals("Cluster_size:")) {
                        for (JComboBox<String> anOption : moreOptions) {
                            if (anOption.getSelectedItem().equals("512k")) {
                                options.add("cluster_size=512k");
                            } else if (anOption.getSelectedItem().equals("1M")) {
                                options.add("cluster_size=1M");
                            } else if (anOption.getSelectedItem().equals("2M")) {
                                options.add("cluster_size=2M");
                            }
                        }
                    }
                }
            }
        } else if (option.equals(".vdi")) {
            for (Object object : objects) {
                if (object instanceof JRadioButton) {
                    if (((JRadioButton) object).isSelected()) {
                        options.add("static");
                    }
                }
            }
        } else if (option.equals(".vmdk")) {
            for (Object object : objects) {
                if (object instanceof JCheckBox) {
                    if (((JCheckBox) object).getText().equals("Compat6 option: on")) {
                        if (((JCheckBox) object).isSelected()) {
                            options.add("compat6");
                        }
                    }
                    if (((JCheckBox) object).getText().equals("VMDK subformat option:")) {
                        if (((JCheckBox) object).isSelected()) {
                            for (JComboBox<String> anOption : moreOptions) {
                                if (anOption.getSelectedItem().equals("monolithicSparse")) {
                                    options.add("subformat=monolithicSparse");
                                } else if (anOption.getSelectedItem().equals(
                                        "monolithicFlat")) {
                                    options.add("subformat=monolithicFlat");
                                } else if (anOption.getSelectedItem().equals(
                                        "twoGbMaxExtentSparse")) {
                                    options.add("subformat=twoGbMaxExtentSparse");
                                } else if (anOption.getSelectedItem().equals(
                                        "twoGbMaxExtentFlat")) {
                                    options.add("subformat=twoGbMaxExtentFlat");
                                } else if (anOption.getSelectedItem().equals(
                                        "streamOptimized")) {
                                    options.add("subformat=streamOptimized");
                                }
                            }
                        }
                    }
                }
            }
        } else if (option.equals(".vpc")) {
            for (Object object : objects) {
                if (object instanceof JRadioButton) {
                    if (((JRadioButton) object).isSelected()) {
                        for (JComboBox<String> anOption : moreOptions) {
                            if (anOption.getSelectedItem().equals("dynamic")) {
                                options.add("subformat=dynamic");
                            } else if (anOption.getSelectedItem().equals("fixed")) {
                                options.add("subformat=fixed");
                            }
                        }
                    }
                }
            }
        }
        return options;

    }

    public List<ItemSelectable> getObjects(String option) {
        List<ItemSelectable> result = new ArrayList<ItemSelectable>();
        /*
		 * From:
		 * https://www.google.com.br/?gfe_rd=cr&ei=PDGIVJGwJ4WX8Qeht4HgDg#q=add+all+elements+in+list+to+another+list+java&safe=active
		 * to:
		 * http://stackoverflow.com/questions/11273440/adding-one-list-to-another-list-in-java
         */
        if (option.equals(".qcow")
                || option.equals(".qcow2")
                || option.equals(".vmdk")) {
            result.addAll(getJCheckBox(option));
        } else if (option.equals(".vdi")
                || option.equals(".vpc")) {
            result.addAll(getJRadioButton(option));
        }

        return result;
    }

    private List<JCheckBox> getJCheckBox(String option) {
        List<JCheckBox> result = new ArrayList<JCheckBox>();
        if (option.equals(".qcow")) {
            result.add(mythirdview.getEncryption_box());
        } else if (option.equals(".qcow2")) {
            if (mythirdview.getEncryption_box().isSelected()) {
                result.add(mythirdview.getEncryption_box());
            }
            if (mythirdview.getPreallocation_metadata_box()
                    .isSelected()) {
                result.add(mythirdview.getPreallocation_metadata_box());
            }
            if (mythirdview.getCluster_size_box().isSelected()) {
                result.add(mythirdview.getCluster_size_box());
            }
        } else if (option.equals(".vmdk")) {
            if (mythirdview.getCompat6_vmdk_box().isSelected()) {
                result.add(mythirdview.getCompat6_vmdk_box());
            }
            if (mythirdview.getSubformat_vmdk_box().isSelected()) {
                result.add(mythirdview.getSubformat_vmdk_box());
            }
        }
        return result;
    }

    private List<ItemSelectable> getJRadioButton(String option) {
        List<ItemSelectable> result = new ArrayList<ItemSelectable>();
        if (option.equals(".vdi")) {
            if (mythirdview.getStatic_vdi_box().isSelected()) {
                result.add(mythirdview.getStatic_vdi_box());
            }
        } else if (option.equals(".vpc")) {
            if (mythirdview.getSubformat_vpc_box().isSelected()) {
                result.add(mythirdview.getSubformat_vpc_box());
            }
        }
        return result;
    }

    public List<JComboBox<String>> getMoreOptions(List<ItemSelectable> items) {
        List<JComboBox<String>> result = new ArrayList<JComboBox<String>>();
        for (ItemSelectable item : items) {
            if (item instanceof JCheckBox) {
                Object selected[] = item.getSelectedObjects();
                if (selected != null) {
                    if (((String) selected[0]).equals("Cluster_size:")) {
                        result.add(mythirdview.getCluster_size_options());
                    } else if (((String) selected[0]).equals("VMDK subformat option:")) {
                        result.add(mythirdview.getSubformat_vmdk_combo());
                    }
                }
            } else if (item instanceof JRadioButton) {
                Object selected[] = item.getSelectedObjects();
                if (selected != null) {
                    if (((String) selected[0]).equals("VPC subformat option:")) {
                        result.add(mythirdview.getSubformat_vpc_combo());
                    }
                }
            }
        }
        return result;
    }
}
