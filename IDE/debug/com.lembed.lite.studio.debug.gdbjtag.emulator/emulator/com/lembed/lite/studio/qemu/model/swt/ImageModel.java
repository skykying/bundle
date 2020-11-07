package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class ImageModel extends OptionsControl {

    private EmulatorQemuMachineControl eMachineControl;
    private String mtdblock;
    private String sd;
    private String pflash;

    public ImageModel(EmulationControl myemulation, EmulatorQemuMachineControl c) {
        super(myemulation);
        this.eMachineControl = c;
        if (eMachineControl.getMachineModel().getMtdblockOption() != null) {
            this.mtdblock = eMachineControl.getMachineModel().getMtdblockOption();
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()]);
        }
        if (eMachineControl.getMachineModel().getSdOption() != null) {
            this.sd = eMachineControl.getMachineModel().getSdOption();
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()]);
        }
        if (eMachineControl.getMachineModel().getPflashOption() != null) {
            this.pflash = eMachineControl.getMachineModel().getPflashOption();
            this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()]);
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()])) {
            if (this.mtdblock != null)
            {
                this.eMachineControl.getMachineModel().setMtdblockOption(this.mtdblock);
                this.mtdblock = UsabilityModel.fixPath(mtdblock);
                super.setOption(this.mtdblock, OptionsEnumModel.MTDBLOCKOPTION.getValor());
            }
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()])) {
            if (this.sd != null)
            {
                this.eMachineControl.getMachineModel().setSdOption(this.sd);
                sd = UsabilityModel.fixPath(sd);
                super.setOption(this.sd, OptionsEnumModel.SDOPTION.getValor());
            }            
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()])) {
            if (this.pflash != null)
            {
                this.eMachineControl.getMachineModel().setPflashOption(this.pflash);
                pflash = UsabilityModel.fixPath(pflash);
                super.setOption(this.pflash, OptionsEnumModel.PFLASHOPTION.getValor());
            }
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MTDBLOCKOPTION.getValor()])) {
            this.eMachineControl.getMachineModel().setMtdblockOption("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SDOPTION.getValor()])) {
            this.eMachineControl.getMachineModel().setSdOption("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.PFLASHOPTION.getValor()])) {
            this.eMachineControl.getMachineModel().setPflashOption("");
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
