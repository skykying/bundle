package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class NetworkManagerModel extends OptionsControl {

    private FileControl myfile;

    public NetworkManagerModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;
    }

    public void setOption(String option) {
        super.setOption(option, OptionsEnumModel.NETWORKNIC1OPTION.getValor());
    }

    public void buildIt(String option) {
        myfile.getFilemodel().setFirstNetworkNICOption(option);
    }
}
