package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class RAMModel extends OptionsControl {

    private FileControl myfile;

    public RAMModel(EmulationControl myemulation,
            FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        if (this.myfile.getFilemodel().getRamSize() == null) {
            this.setOption("128.000");
        } else {
            this.setOption(this.myfile.getFilemodel().getRamSize());
        }
    }

    public void setOption(String option) {
        super.setOption(option, OptionsEnumModel.RAMSIZE.getValor());
        this.myfile.getFilemodel().setRamSize(option);
    }
}
