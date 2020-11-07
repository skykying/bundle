package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class VMOpeningModel {

    private EmulationControl myemulation;
    private EmulatorQemuMachineControl myfile;

    public VMOpeningModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {

        this.myemulation = myemulation;
        this.myfile = myfile;
    }

    public boolean starts() {
        if (this.myfile.getMachineModel().getFirstHardDiskOption() != null) {
            this.myemulation.define_first_hard_disk_option(this.myfile.getMachineModel()
                    .getFirstHardDiskOption());
        }
        if (this.myfile.getMachineModel().getSecondHardDiskOption() != null) {
            this.myemulation.define_second_hard_disk_option(this.myfile
                    .getMachineModel().getSecondHardDiskOption());
        }
        if (this.myfile.getMachineModel().getThirdHardDiskOption() != null) {
            this.myemulation.define_third_hard_disk_option(this.myfile
                    .getMachineModel().getThirdHardDiskOption());
        }
        if (this.myfile.getMachineModel().getFourthHardDiskOption() != null) {
            this.myemulation.define_fourth_hard_disk_option(this.myfile
                    .getMachineModel().getFourthHardDiskOption());
        }
        if (this.myfile.getMachineModel().getRamSize() != null) {
            this.myemulation.change_options(
                    OptionsEnumModel.RAMSIZE.getValor(), "-m "
                    + this.myfile.getMachineModel().getRamSize());
        } else {
            this.myemulation.change_options(
                    OptionsEnumModel.RAMSIZE.getValor(), "-m 128");
        }
        this.myemulation.appends_options();
        return true;
    }

    public void setMyemulation(EmulationControl myemulation) {
        this.myemulation = myemulation;
    }

    public void setMyfile(EmulatorQemuMachineControl myfile) {
        this.myfile = myfile;
    }

    public EmulatorQemuMachineControl getMyfile() {
        return myfile;
    }

    public String getDiskImagePath() {
        return this.myfile.getMachineModel().getFirstHardDiskOption();
    }
}
