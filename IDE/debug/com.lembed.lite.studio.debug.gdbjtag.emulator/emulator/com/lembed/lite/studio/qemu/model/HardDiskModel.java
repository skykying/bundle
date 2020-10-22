package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;

public class HardDiskModel {

    private String firstHardDiskOption;

    private String secondHardDiskOption;

    private String thirdHardDiskOption;

    private String fourthHardDiskOption;

    private EmulationControl myemulation;

    private FileControl myfile;

    public HardDiskModel(EmulationControl myemulation, FileControl myfile) {
        this.myemulation = myemulation;
        this.myfile = myfile;
        if (this.myfile.getFilemodel().getFirstHardDiskOption() != null) {
            this.firstHardDiskOption = this.myfile.getFilemodel().getFirstHardDiskOption();
        }
        if (this.myfile.getFilemodel().getSecondHardDiskOption() != null) {
            this.secondHardDiskOption = this.myfile.getFilemodel().getSecondHardDiskOption();
        }
        if (this.myfile.getFilemodel().getThirdHardDiskOption() != null) {
            this.thirdHardDiskOption = this.myfile.getFilemodel().getThirdHardDiskOption();
        }
        if (this.myfile.getFilemodel().getFourthHardDiskOption() != null) {
            this.fourthHardDiskOption = this.myfile.getFilemodel().getFourthHardDiskOption();
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
        this.myemulation.define_first_hard_disk_option(firstHardDiskOption);
        this.myfile.getFilemodel().setFirstHardDiskOption(firstHardDiskOption);
        this.myemulation.setJPanel();
    }

    public void setSecondHardDiskOption(String secondHardDiskOption) {
        this.secondHardDiskOption = secondHardDiskOption;
        this.myemulation.define_second_hard_disk_option(secondHardDiskOption);
        this.myfile.getFilemodel().setSecondHardDiskOption(secondHardDiskOption);
        this.myemulation.setJPanel();
    }

    public void setThirdHardDiskOption(String thirdHardDiskOption) {
        this.thirdHardDiskOption = thirdHardDiskOption;
        this.myemulation.define_third_hard_disk_option(thirdHardDiskOption);
        this.myfile.getFilemodel().setThirdHardDiskOption(thirdHardDiskOption);
        this.myemulation.setJPanel();
    }

    public void setFourthHardDiskOption(String fourthHardDiskOption) {
        this.fourthHardDiskOption = fourthHardDiskOption;
        this.myemulation.define_fourth_hard_disk_option(fourthHardDiskOption);
        this.myfile.getFilemodel().setFourthHardDiskOption(fourthHardDiskOption);
        this.myemulation.setJPanel();
    }

}
