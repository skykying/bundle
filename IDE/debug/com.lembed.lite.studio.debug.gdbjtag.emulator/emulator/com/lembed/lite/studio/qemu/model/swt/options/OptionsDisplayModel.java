package com.lembed.lite.studio.qemu.model.swt.options;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;

public class OptionsDisplayModel extends OptionsControl {

    private String displayTypeOption;

    private EmulatorQemuMachineControl myfile;

    public OptionsDisplayModel(EmulationControl myemulation,
            EmulatorQemuMachineControl myfile) {
        super(myemulation);
        displayTypeOption = "";
        this.myfile = myfile;
        if (this.myfile.getMachineModel().getDisplayType() != null) {
            this.setDisplayTypeOption(this.myfile.getMachineModel().getDisplayType());
        }
        if (this.myfile.getMachineModel().getNographicOption() != null) {
            if (this.myfile.getMachineModel().getNographicOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOGRAPHICOPTION.getValor()]);
            }
        }
        if (this.myfile.getMachineModel().getVgaType() != null) {
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.VGAOPTION.getValor()] + this.myfile.getMachineModel().getVgaType());
        }
        if (this.myfile.getMachineModel().getFullscreenOption() != null) {
            if (this.myfile.getMachineModel().getFullscreenOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FULLSCREENOPTION.getValor()]);
            }
        }
        if (this.myfile.getMachineModel().getNoFrameOption() != null) {
            if (this.myfile.getMachineModel().getNoFrameOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFRAMEOPTION.getValor()]);
            }
        }
    }

    public String getDisplayTypeOption() {
        return displayTypeOption;
    }

    public void setDisplayTypeOption(String displayTypeOption) {
        this.displayTypeOption = displayTypeOption;
        if (displayTypeOption.equals("sdl")) {
            this.myfile.getMachineModel().setDisplayType("sdl");
        } else if (displayTypeOption.equals("curses")) {
            this.myfile.getMachineModel().setDisplayType("curses");
        } else if (displayTypeOption.equals("none")) {
            this.myfile.getMachineModel().setDisplayType("none");
        } else if (displayTypeOption.substring(0, 3).equals("vnc")) {
            this.myfile.getMachineModel().setDisplayType(displayTypeOption);
        }
        super.setOption(displayTypeOption,
                OptionsEnumModel.DISPLAYOPTION.getValor());
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOGRAPHICOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOGRAPHICOPTION.getValor());
            this.myfile.getMachineModel().setNographicOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FULLSCREENOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.FULLSCREENOPTION.getValor());
            this.myfile.getMachineModel().setFullscreenOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFRAMEOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOFRAMEOPTION.getValor());
            this.myfile.getMachineModel().setNoFrameOption("true");
        } else if (option.indexOf(" ") != -1) {
            if (option.substring(0, option.indexOf(" ")).equals("-vga")) {
                super.setOption(option.substring(option.indexOf(" ") + 1),
                        OptionsEnumModel.VGAOPTION.getValor());
                this.myfile.getMachineModel().setVgaType(
                        option.substring(option.indexOf(" ") + 1));
            }
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.DISPLAYOPTION.getValor()])
                && this.myfile.getMachineModel().getDisplayType() != null) {
            this.myfile.getMachineModel().setDisplayType("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOGRAPHICOPTION.getValor()])) {
            this.myfile.getMachineModel().setNographicOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.VGAOPTION.getValor()])) {
            this.myfile.getMachineModel().setVgaType(null);
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FULLSCREENOPTION.getValor()])) {
            this.myfile.getMachineModel().setFullscreenOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFRAMEOPTION.getValor()])) {
            this.myfile.getMachineModel().setNoFrameOption("false");
        }
    }
}
