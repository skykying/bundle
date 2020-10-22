package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class ImageModel extends OptionsControl {

    private FileControl myfile;
    private String mtdblock;
    private String sd;
    private String pflash;

    public ImageModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        if (myfile.getFilemodel().getMtdblockOption() != null) {
            this.mtdblock = myfile.getFilemodel().getMtdblockOption();
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()]);
        }
        if (myfile.getFilemodel().getSdOption() != null) {
            this.sd = myfile.getFilemodel().getSdOption();
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()]);
        }
        if (myfile.getFilemodel().getPflashOption() != null) {
            this.pflash = myfile.getFilemodel().getPflashOption();
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()]);
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()])) {
            if (this.mtdblock != null)
            {
                this.myfile.getFilemodel().setMtdblockOption(this.mtdblock);
                this.mtdblock = UsabilityModel.fixPath(mtdblock);
                super.setOption(this.mtdblock, OptionsEnumModel.MTDBLOCKOPTION.getValor());
            }
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()])) {
            if (this.sd != null)
            {
                this.myfile.getFilemodel().setSdOption(this.sd);
                sd = UsabilityModel.fixPath(sd);
                super.setOption(this.sd, OptionsEnumModel.SDOPTION.getValor());
            }            
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()])) {
            if (this.pflash != null)
            {
                this.myfile.getFilemodel().setPflashOption(this.pflash);
                pflash = UsabilityModel.fixPath(pflash);
                super.setOption(this.pflash, OptionsEnumModel.PFLASHOPTION.getValor());
            }
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()])) {
            this.myfile.getFilemodel().setMtdblockOption("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()])) {
            this.myfile.getFilemodel().setSdOption("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()])) {
            this.myfile.getFilemodel().setPflashOption("");
        }
    }

    public void setMtdblock(String mtdblock) {
        this.mtdblock = mtdblock;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public void setPflash(String pflash) {
        this.pflash = pflash;
    }
}
