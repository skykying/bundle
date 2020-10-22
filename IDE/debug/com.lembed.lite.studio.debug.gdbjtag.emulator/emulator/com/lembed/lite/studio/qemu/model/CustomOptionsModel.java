package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class CustomOptionsModel extends OptionsControl {

    private FileControl myfile;

    public CustomOptionsModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getFilemodel().getCustomOptions() != null) {
            this.setOption(this.myfile.getFilemodel().getCustomOptions().replace("\n", " "));
        }
    }

    public void setOption(String option) {
        super.setOption(option,
                OptionsEnumModel.CUSTOMOPTIONS.getValor());
    }

    public void setFileOption(String option) {
        this.myfile.getFilemodel().setCustomOptions(option);
    }
}
