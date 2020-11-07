package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;

public class HardDiskModel {

    private String firstHardDiskOption;

    private String secondHardDiskOption;

    private String thirdHardDiskOption;

    private String fourthHardDiskOption;

    private EmulationControl emulationControl;

    private EmulatorQemuMachineControl emulatorQemuMachineControl;

    public HardDiskModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        this.emulationControl = myemulation;
        this.emulatorQemuMachineControl = myfile;
        if (this.emulatorQemuMachineControl.getMachineModel().getFirstHardDiskOption() != null) {
            this.firstHardDiskOption = this.emulatorQemuMachineControl.getMachineModel().getFirstHardDiskOption();
        }
        if (this.emulatorQemuMachineControl.getMachineModel().getSecondHardDiskOption() != null) {
            this.secondHardDiskOption = this.emulatorQemuMachineControl.getMachineModel().getSecondHardDiskOption();
        }
        if (this.emulatorQemuMachineControl.getMachineModel().getThirdHardDiskOption() != null) {
            this.thirdHardDiskOption = this.emulatorQemuMachineControl.getMachineModel().getThirdHardDiskOption();
        }
        if (this.emulatorQemuMachineControl.getMachineModel().getFourthHardDiskOption() != null) {
            this.fourthHardDiskOption = this.emulatorQemuMachineControl.getMachineModel().getFourthHardDiskOption();
        }
    }

    public String getFirstHardDiskOption() {
        return firstHardDiskOption;
    }

    public String getSecondHardDiskOption() {
        return secondHardDiskOption;
    }

    public String getThirdHardDiskOption() {
        return thirdHardDiskOption;
    }

    public String getFourthHardDiskOption() {
        return fourthHardDiskOption;
    }

    public void setFirstHardDiskOption(String firstHardDiskOption) {
        this.firstHardDiskOption = firstHardDiskOption;
        this.emulationControl.define_first_hard_disk_option(firstHardDiskOption);
        this.emulatorQemuMachineControl.getMachineModel().setFirstHardDiskOption(firstHardDiskOption);
        this.emulationControl.setJPanel();
    }

    public void setSecondHardDiskOption(String secondHardDiskOption) {
        this.secondHardDiskOption = secondHardDiskOption;
        this.emulationControl.define_second_hard_disk_option(secondHardDiskOption);
        this.emulatorQemuMachineControl.getMachineModel().setSecondHardDiskOption(secondHardDiskOption);
        this.emulationControl.setJPanel();
    }

    public void setThirdHardDiskOption(String thirdHardDiskOption) {
        this.thirdHardDiskOption = thirdHardDiskOption;
        this.emulationControl.define_third_hard_disk_option(thirdHardDiskOption);
        this.emulatorQemuMachineControl.getMachineModel().setThirdHardDiskOption(thirdHardDiskOption);
        this.emulationControl.setJPanel();
    }

    public void setFourthHardDiskOption(String fourthHardDiskOption) {
        this.fourthHardDiskOption = fourthHardDiskOption;
        this.emulationControl.define_fourth_hard_disk_option(fourthHardDiskOption);
        this.emulatorQemuMachineControl.getMachineModel().setFourthHardDiskOption(fourthHardDiskOption);
        this.emulationControl.setJPanel();
    }

}
