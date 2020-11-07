package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.options.OptionsModel;

public class OptionsControl {

    private OptionsModel optionsModel;

    public OptionsControl(EmulationControl ec) {
        this.optionsModel = new OptionsModel(ec);
    }

    public String getOption() {
        return this.optionsModel.getOption();
    }

    public void setOption(String option, int position) {
        this.optionsModel.setOption(option, position);
    }

    public void unsetOption(String option) {
        this.optionsModel.unsetOption(option);
    }

    public void unsetOption(String option, int position) {
        this.optionsModel.unsetOption(option, position);
    }
}
