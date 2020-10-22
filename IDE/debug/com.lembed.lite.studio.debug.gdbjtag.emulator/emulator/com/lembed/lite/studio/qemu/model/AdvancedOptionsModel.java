package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class AdvancedOptionsModel extends OptionsControl {

    private FileControl myfile;
    private String name;

    public AdvancedOptionsModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        if (this.myfile.getFilemodel().getNoacpiOption() != null) {
            if (this.myfile.getFilemodel().getNoacpiOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()]);
            }
        }
        if (this.myfile.getFilemodel().getWin2khackOption() != null) {
            if (this.myfile.getFilemodel().getWin2khackOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()]);
            }
        }
        if (this.myfile.getFilemodel().getNameOption() != null) {
            if (!this.myfile.getFilemodel().getNameOption().isEmpty()) {
                this.name = this.myfile.getFilemodel().getNameOption();
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
            }
        }
        if (this.myfile.getFilemodel().getSnapshotOption() != null) {
            if (this.myfile.getFilemodel().getSnapshotOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()]);
            }
        }
        if (this.myfile.getFilemodel().getNoFdBootchkOption() != null) {
            if (this.myfile.getFilemodel().getNoFdBootchkOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()]);
            }
        }
        if (this.myfile.getFilemodel().getNoHpetOption() != null) {
            if (this.myfile.getFilemodel().getNoHpetOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.WIN2KHACKOPTION.getValor());
            this.myfile.getFilemodel().setWin2khackOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOACPIOPTION.getValor());
            this.myfile.getFilemodel().setNoacpiOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()])) {
            super.setOption(name, OptionsEnumModel.NAMEOPTION.getValor());
            this.myfile.getFilemodel().setNameOption(name);
            this.myfile.getFilemodel().setMachineName(name);
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.SNAPSHOTOPTION.getValor());
            this.myfile.getFilemodel().setSnapshotOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOFDBOOTCHKOPTION.getValor());
            this.myfile.getFilemodel().setNoFdBootchkOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOHPETOPTION.getValor());
            this.myfile.getFilemodel().setNoHpetOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()])) {
            this.myfile.getFilemodel().setWin2khackOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()])) {
            this.myfile.getFilemodel().setNoacpiOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()])) {
            this.myfile.getFilemodel().setNameOption("");
            this.myfile.getFilemodel().setMachineName("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()])) {
            this.myfile.getFilemodel().setSnapshotOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()])) {
            this.myfile.getFilemodel().setNoFdBootchkOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()])) {
            this.myfile.getFilemodel().setNoHpetOption("false");
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
