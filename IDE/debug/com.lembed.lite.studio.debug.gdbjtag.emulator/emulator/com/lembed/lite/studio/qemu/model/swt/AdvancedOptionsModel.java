package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class AdvancedOptionsModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;
    private String name;

    public AdvancedOptionsModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        if (this.myfile.getMachineModel().getNoacpiOption() != null) {
            if (this.myfile.getMachineModel().getNoacpiOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()]);
            }
        }
        if (this.myfile.getMachineModel().getWin2khackOption() != null) {
            if (this.myfile.getMachineModel().getWin2khackOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()]);
            }
        }
        if (this.myfile.getMachineModel().getNameOption() != null) {
            if (!this.myfile.getMachineModel().getNameOption().isEmpty()) {
                this.name = this.myfile.getMachineModel().getNameOption();
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
            }
        }
        if (this.myfile.getMachineModel().getSnapshotOption() != null) {
            if (this.myfile.getMachineModel().getSnapshotOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()]);
            }
        }
        if (this.myfile.getMachineModel().getNoFdBootchkOption() != null) {
            if (this.myfile.getMachineModel().getNoFdBootchkOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()]);
            }
        }
        if (this.myfile.getMachineModel().getNoHpetOption() != null) {
            if (this.myfile.getMachineModel().getNoHpetOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.WIN2KHACKOPTION.getValor());
            this.myfile.getMachineModel().setWin2khackOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOACPIOPTION.getValor());
            this.myfile.getMachineModel().setNoacpiOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()])) {
            super.setOption(name, OptionsEnumModel.NAMEOPTION.getValor());
            this.myfile.getMachineModel().setNameOption(name);
            this.myfile.getMachineModel().setMachineName(name);
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.SNAPSHOTOPTION.getValor());
            this.myfile.getMachineModel().setSnapshotOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOFDBOOTCHKOPTION.getValor());
            this.myfile.getMachineModel().setNoFdBootchkOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOHPETOPTION.getValor());
            this.myfile.getMachineModel().setNoHpetOption("true");
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()])) {
            this.myfile.getMachineModel().setWin2khackOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()])) {
            this.myfile.getMachineModel().setNoacpiOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()])) {
            this.myfile.getMachineModel().setNameOption("");
            this.myfile.getMachineModel().setMachineName("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()])) {
            this.myfile.getMachineModel().setSnapshotOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()])) {
            this.myfile.getMachineModel().setNoFdBootchkOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()])) {
            this.myfile.getMachineModel().setNoHpetOption("false");
        }
    }

    public void setName(String name) {
        this.name = name;
    }
}
