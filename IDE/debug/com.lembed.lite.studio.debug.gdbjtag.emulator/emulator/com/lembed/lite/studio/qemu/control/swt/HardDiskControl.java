package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.HardDiskModel;
import com.lembed.lite.studio.qemu.view.internal.swt.HardDiskView;

public class HardDiskControl implements BaseControl {

    private HardDiskModel hardDiskModel;
    private HardDiskView hardDiskView;
    private PhysicalDriveControl[] physicalDriveControls;
    private EmulatorQemuMachineControl eQctl;

    public HardDiskControl(EmulationControl myemulation, EmulatorQemuMachineControl emc) {
        this.hardDiskView = new HardDiskView(emc.getMachineModel().getFirstHardDiskOption(),
        		emc.getMachineModel().getSecondHardDiskOption(), emc
                .getMachineModel().getThirdHardDiskOption(), emc
                .getMachineModel().getFourthHardDiskOption());
        this.hardDiskModel = new HardDiskModel(myemulation, emc);
        this.eQctl = emc;
        this.physicalDriveControls = new PhysicalDriveControl[4];
    }

    public void starts() {
        this.hardDiskView.configureListener(this);
        this.hardDiskView.configureStandardMode();
    }

    public void setVisible(boolean value) {
        this.hardDiskView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ChangeDiskImagePathButton")) {
            this.hardDiskView
                    .setChoosertitle("Choose the first disk image path!");
            if (this.hardDiskView.chooseFiles()) {
                this.hardDiskModel.setFirstHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesFirstHardDiskOptionField(this.hardDiskView.getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetFirstPhysicalHardDiskDriveButton")) {
            if (this.physicalDriveControls[0] == null) {
                this.physicalDriveControls[0] = new PhysicalDriveControl(this, eQctl, 1);
                this.physicalDriveControls[0].change_my_visibility(true);
            } else {
                this.physicalDriveControls[0].change_my_visibility(true);
            }
        } else if (e.getActionCommand().equals("SetFirstLinuxPhysicalHardDiskDriveButton")) {
            this.hardDiskView.setChoosertitle("JavaQemu - First Linux Physical Drive Choice");
            this.hardDiskView.setFileDescription("Linux Hard Disk Device");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setFirstHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesFirstHardDiskOptionField(this.hardDiskView.getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetFirstReadOnlyVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the First Read-Only Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Only Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setFirstHardDiskOption(this.convertFatReadOnly(this.hardDiskView.getChoice()));
                this.hardDiskView.changesFirstHardDiskOptionField(this.convertFatReadOnly(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand().equals(
                "SetFirstReadWriteVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the First Read-Write Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Write Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setFirstHardDiskOption(this.convertFatReadWrite(this.hardDiskView.getChoice()));
                this.hardDiskView.changesFirstHardDiskOptionField(this.convertFatReadWrite(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand().equals(
                "ChangeSecondDiskImagePathButton")) {
            this.hardDiskView
                    .setChoosertitle("Choose the second disk image path!");
            if (this.hardDiskView.chooseFiles()) {
                this.hardDiskModel.setSecondHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesSecondHardDiskOptionField(this.hardDiskView
                        .getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetSecondPhysicalHardDiskDriveButton")) {
            if (this.physicalDriveControls[1] == null) {
                this.physicalDriveControls[1] = new PhysicalDriveControl(this, eQctl, 2);
                this.physicalDriveControls[1].change_my_visibility(true);
            } else {
                this.physicalDriveControls[1].change_my_visibility(true);
            }
        } else if (e.getActionCommand().equals("SetSecondLinuxPhysicalHardDiskDriveButton")) {
            this.hardDiskView.setChoosertitle("JavaQemu - Second Linux Physical Drive Choice");
            this.hardDiskView.setFileDescription("Linux Hard Disk Device");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setSecondHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesSecondHardDiskOptionField(this.hardDiskView.getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetSecondReadOnlyVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the Second Read-Only Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Only Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setSecondHardDiskOption(this.convertFatReadOnly(this.hardDiskView.getChoice()));
                this.hardDiskView.changesSecondHardDiskOptionField(this.convertFatReadOnly(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand().equals(
                "SetSecondReadWriteVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the Second Read-Write Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Write Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setSecondHardDiskOption(this.convertFatReadWrite(this.hardDiskView.getChoice()));
                this.hardDiskView.changesSecondHardDiskOptionField(this.convertFatReadWrite(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand()
                .equals("ChangeThirdDiskImagePathButton")) {
            this.hardDiskView
                    .setChoosertitle("Choose the third disk image path!");
            if (this.hardDiskView.chooseFiles()) {
                this.hardDiskModel.setThirdHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesThirdHardDiskOptionField(this.hardDiskView
                        .getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetThirdPhysicalHardDiskDriveButton")) {
            if (this.physicalDriveControls[2] == null) {
                this.physicalDriveControls[2] = new PhysicalDriveControl(this, eQctl, 3);
                this.physicalDriveControls[2].change_my_visibility(true);
            } else {
                this.physicalDriveControls[2].change_my_visibility(true);
            }
        } else if (e.getActionCommand().equals("SetThirdLinuxPhysicalHardDiskDriveButton")) {
            this.hardDiskView.setChoosertitle("JavaQemu - Third Linux Physical Drive Choice");
            this.hardDiskView.setFileDescription("Linux Hard Disk Device");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setThirdHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesThirdHardDiskOptionField(this.hardDiskView.getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetThirdReadOnlyVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the Third Read-Only Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Only Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setThirdHardDiskOption(this.convertFatReadOnly(this.hardDiskView.getChoice()));
                this.hardDiskView.changesThirdHardDiskOptionField(this.convertFatReadOnly(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand().equals(
                "SetThirdReadWriteVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the Third Read-Write Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Write Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setThirdHardDiskOption(this.convertFatReadWrite(this.hardDiskView.getChoice()));
                this.hardDiskView.changesThirdHardDiskOptionField(this.convertFatReadWrite(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand().equals(
                "ChangeFourthDiskImagePathButton")) {
            this.hardDiskView
                    .setChoosertitle("Choose the fourth disk image path!");
            if (this.hardDiskView.chooseFiles()) {
                this.hardDiskModel.setFourthHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesFourthHardDiskOptionField(this.hardDiskView
                        .getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetFourthPhysicalHardDiskDriveButton")) {
            if (this.physicalDriveControls[3] == null) {
                this.physicalDriveControls[3] = new PhysicalDriveControl(this, eQctl, 4);
                this.physicalDriveControls[3].change_my_visibility(true);
            } else {
                this.physicalDriveControls[3].change_my_visibility(true);
            }
        } else if (e.getActionCommand().equals("SetFourthLinuxPhysicalHardDiskDriveButton")) {
            this.hardDiskView.setChoosertitle("JavaQemu - Fourth Linux Physical Drive Choice");
            this.hardDiskView.setFileDescription("Linux Hard Disk Device");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setFourthHardDiskOption(this.hardDiskView.getChoice());
                this.hardDiskView.changesFourthHardDiskOptionField(this.hardDiskView.getChoice());
            }
        } else if (e.getActionCommand().equals(
                "SetFourthReadOnlyVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the Fourth Read-Only Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Only Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setFourthHardDiskOption(this.convertFatReadOnly(this.hardDiskView.getChoice()));
                this.hardDiskView.changesFourthHardDiskOptionField(this.convertFatReadOnly(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand().equals(
                "SetFourthReadWriteVirtualFATHardDiskPathButton")) {
            this.hardDiskView.setChoosertitle("Choose the Fourth Read-Write Virtual VFAT Path!");
            this.hardDiskView.setFileDescription("Read-Write Virtual VFAT Directory");
            if (this.hardDiskView.chooseDirectoryForDefaultVMPath()) {
                this.hardDiskModel.setFourthHardDiskOption(this.convertFatReadWrite(this.hardDiskView.getChoice()));
                this.hardDiskView.changesFourthHardDiskOptionField(this.convertFatReadWrite(this.hardDiskView.getChoice()));
            }
        } else if (e.getActionCommand().equals("okButtonHDI")) {
            if (!this.hardDiskView.getFirstHardDiskOption().equals(
                    this.hardDiskModel.getFirstHardDiskOption())) {
                this.hardDiskModel.setFirstHardDiskOption(this.hardDiskView.getFirstHardDiskOption());
                this.hardDiskView.changesFirstHardDiskOptionField(this.hardDiskView
                        .getFirstHardDiskOption());
            }

            if (!this.hardDiskView.getSecondHardDiskOption().equals(
                    this.hardDiskModel.getSecondHardDiskOption())) {
                this.hardDiskModel.setSecondHardDiskOption(this.hardDiskView
                        .getSecondHardDiskOption());
                this.hardDiskView.changesSecondHardDiskOptionField(this.hardDiskView
                        .getSecondHardDiskOption());
            }

            if (!this.hardDiskView.getThirdHardDiskOption().equals(
                    this.hardDiskModel.getThirdHardDiskOption())) {
                this.hardDiskModel.setThirdHardDiskOption(this.hardDiskView
                        .getThirdHardDiskOption());
                this.hardDiskView.changesThirdHardDiskOptionField(this.hardDiskView
                        .getThirdHardDiskOption());
            }

            if (!this.hardDiskView.getFourthHardDiskOption().equals(
                    this.hardDiskModel.getFourthHardDiskOption())) {
                this.hardDiskModel.setFourthHardDiskOption(this.hardDiskView
                        .getFourthHardDiskOption());
                this.hardDiskView.changesFourthHardDiskOptionField(this.hardDiskView
                        .getFourthHardDiskOption());
            }

            this.hardDiskView.setVisible(false);
        } else if (e.getActionCommand().equals("eraseButtonHDI")) {

            this.hardDiskModel.setFirstHardDiskOption("");
            this.hardDiskView.changesFirstHardDiskOptionField("");

            this.hardDiskModel.setSecondHardDiskOption("");
            this.hardDiskView.changesSecondHardDiskOptionField("");

            this.hardDiskModel.setThirdHardDiskOption("");
            this.hardDiskView.changesThirdHardDiskOptionField("");

            this.hardDiskModel.setFourthHardDiskOption("");
            this.hardDiskView.changesFourthHardDiskOptionField("");

            this.hardDiskView.setVisible(false);
        }
    }

    public void setPhysicalDriveChoice(int position, String choice) {
        if (position == 1) {
            if (!choice.isEmpty()) {
                this.hardDiskModel.setFirstHardDiskOption(choice);
                this.hardDiskView.changesFirstHardDiskOptionField(choice);
            } else if (this.hardDiskView.getFirstHardDiskOption().startsWith(
                    "\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                this.hardDiskModel.setFirstHardDiskOption("");
                this.hardDiskView.changesFirstHardDiskOptionField("");
            }
        } else if (position == 2) {
            if (!choice.isEmpty()) {
                this.hardDiskModel.setSecondHardDiskOption(choice);
                this.hardDiskView.changesSecondHardDiskOptionField(choice);
            } else if (this.hardDiskView.getSecondHardDiskOption().startsWith(
                    "\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                this.hardDiskModel.setSecondHardDiskOption("");
                this.hardDiskView.changesSecondHardDiskOptionField("");
            }
        } else if (position == 3) {
            if (!choice.isEmpty()) {
                this.hardDiskModel.setThirdHardDiskOption(choice);
                this.hardDiskView.changesThirdHardDiskOptionField(choice);
            } else if (this.hardDiskView.getThirdHardDiskOption().startsWith(
                    "\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                this.hardDiskModel.setThirdHardDiskOption("");
                this.hardDiskView.changesThirdHardDiskOptionField("");
            }
        } else if (position == 4) {
            if (!choice.isEmpty()) {
                this.hardDiskModel.setFourthHardDiskOption(choice);
                this.hardDiskView.changesFourthHardDiskOptionField(choice);
            } else if (this.hardDiskView.getFourthHardDiskOption().startsWith(
                    "\\" + "\\" + "." + "\\" + "PhysicalDrive")) {
                this.hardDiskModel.setFourthHardDiskOption("");
                this.hardDiskView.changesFourthHardDiskOptionField("");
            }
        }
    }

    public String convertFatReadOnly(String basis) {
        return "fat:" + basis;
    }

    public String convertFatReadWrite(String basis) {
        return "fat:rw:" + basis;
    }
}
