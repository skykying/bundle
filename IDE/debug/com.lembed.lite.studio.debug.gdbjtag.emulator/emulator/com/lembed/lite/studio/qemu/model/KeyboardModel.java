package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class KeyboardModel extends OptionsControl {

    private FileControl myfile;

    public KeyboardModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        if (this.myfile.getFilemodel().getKeyboardLayoutLanguage() != null) {
            this.setOption(this.myfile.getFilemodel().getKeyboardLayoutLanguage());
        }
    }

    public void setOption(String option) {
        super.setOption(option, OptionsEnumModel.KEYBOARDOPTION.getValor());
        this.myfile.getFilemodel().setKeyboardLayoutLanguage(option);
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getFilemodel().setKeyboardLayoutLanguage("");
    }
}
