package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;

public class VMOpeningModel {

    private EmulationControl myemulation;
    private FileControl myfile;

    public VMOpeningModel(EmulationControl myemulation, FileControl myfile) {

        this.myemulation = myemulation;
        this.myfile = myfile;
    }

    public boolean starts() {
        if (this.myfile.getFilemodel().getFirstHardDiskOption() != null) {
            this.myemulation.define_first_hard_disk_option(this.myfile.getFilemodel()
                    .getFirstHardDiskOption());
        }
        if (this.myfile.getFilemodel().getSecondHardDiskOption() != null) {
            this.myemulation.define_second_hard_disk_option(this.myfile
                    .getFilemodel().getSecondHardDiskOption());
        }
        if (this.myfile.getFilemodel().getThirdHardDiskOption() != null) {
            this.myemulation.define_third_hard_disk_option(this.myfile
                    .getFilemodel().getThirdHardDiskOption());
        }
        if (this.myfile.getFilemodel().getFourthHardDiskOption() != null) {
            this.myemulation.define_fourth_hard_disk_option(this.myfile
                    .getFilemodel().getFourthHardDiskOption());
        }
        if (this.myfile.getFilemodel().getRamSize() != null) {
            this.myemulation.change_options(
                    OptionsEnumModel.RAMSIZE.getValor(), "-m "
                    + this.myfile.getFilemodel().getRamSize());
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

    public void setMyfile(FileControl myfile) {
        this.myfile = myfile;
    }

    public FileControl getMyfile() {
        return myfile;
    }

    public String getDiskImagePath() {
        return this.myfile.getFilemodel().getFirstHardDiskOption();
    }
}
