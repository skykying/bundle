package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class OptionsDisplayModel extends OptionsControl {

    private String displayTypeOption;

    private FileControl myfile;

    public OptionsDisplayModel(EmulationControl myemulation,
            FileControl myfile) {
        super(myemulation);
        displayTypeOption = "";
        this.myfile = myfile;
        if (this.myfile.getFilemodel().getDisplayType() != null) {
            this.setDisplayTypeOption(this.myfile.getFilemodel().getDisplayType());
        }
        if (this.myfile.getFilemodel().getNographicOption() != null) {
            if (this.myfile.getFilemodel().getNographicOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOGRAPHICOPTION.getValor()]);
            }
        }
        if (this.myfile.getFilemodel().getVgaType() != null) {
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.VGAOPTION.getValor()] + this.myfile.getFilemodel().getVgaType());
        }
        if (this.myfile.getFilemodel().getFullscreenOption() != null) {
            if (this.myfile.getFilemodel().getFullscreenOption().equals("true")) {
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FULLSCREENOPTION.getValor()]);
            }
        }
        if (this.myfile.getFilemodel().getNoFrameOption() != null) {
            if (this.myfile.getFilemodel().getNoFrameOption().equals("true")) {
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
            this.myfile.getFilemodel().setDisplayType("sdl");
        } else if (displayTypeOption.equals("curses")) {
            this.myfile.getFilemodel().setDisplayType("curses");
        } else if (displayTypeOption.equals("none")) {
            this.myfile.getFilemodel().setDisplayType("none");
        } else if (displayTypeOption.substring(0, 3).equals("vnc")) {
            this.myfile.getFilemodel().setDisplayType(displayTypeOption);
        }
        super.setOption(displayTypeOption,
                OptionsEnumModel.DISPLAYOPTION.getValor());
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOGRAPHICOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOGRAPHICOPTION.getValor());
            this.myfile.getFilemodel().setNographicOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FULLSCREENOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.FULLSCREENOPTION.getValor());
            this.myfile.getFilemodel().setFullscreenOption("true");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFRAMEOPTION.getValor()])) {
            super.setOption("", OptionsEnumModel.NOFRAMEOPTION.getValor());
            this.myfile.getFilemodel().setNoFrameOption("true");
        } else if (option.indexOf(" ") != -1) {
            if (option.substring(0, option.indexOf(" ")).equals("-vga")) {
                super.setOption(option.substring(option.indexOf(" ") + 1),
                        OptionsEnumModel.VGAOPTION.getValor());
                this.myfile.getFilemodel().setVgaType(
                        option.substring(option.indexOf(" ") + 1));
            }
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.DISPLAYOPTION.getValor()])
                && this.myfile.getFilemodel().getDisplayType() != null) {
            this.myfile.getFilemodel().setDisplayType("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOGRAPHICOPTION.getValor()])) {
            this.myfile.getFilemodel().setNographicOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.VGAOPTION.getValor()])) {
            this.myfile.getFilemodel().setVgaType(null);
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FULLSCREENOPTION.getValor()])) {
            this.myfile.getFilemodel().setFullscreenOption("false");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFRAMEOPTION.getValor()])) {
            this.myfile.getFilemodel().setNoFrameOption("false");
        }
    }
}
