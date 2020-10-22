package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class NameModel extends OptionsControl {

    private FileControl myfile;

    public NameModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);

        this.myfile = myfile;

        if (this.myfile.getFilemodel().getNameOption() != null) {
            this.setOption(this.myfile.getFilemodel().getNameOption());
        }
    }

    public void setOption(String option) {
        super.setOption(option,
                OptionsEnumModel.NAMEOPTION.getValor());
        this.myfile.getFilemodel()
                .setNameOption(option);
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getFilemodel().setNameOption("");
    }
}
