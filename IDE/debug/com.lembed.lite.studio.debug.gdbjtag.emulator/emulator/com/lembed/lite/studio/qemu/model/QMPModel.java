package com.lembed.lite.studio.qemu.model;

import java.util.HashMap;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class QMPModel extends OptionsControl {

    private FileControl myfile;

    // Association "Description" - "Option".
    private static HashMap<String, String> trueOptions;

    // Association "Option" - "Description".
    private static HashMap<String, String> falseOptions;

    static {
        trueOptions = new HashMap<String, String>();
        trueOptions.put("", "");
        trueOptions.put("Graphical Mode", "vc");
        trueOptions.put("Non Graphical Mode", "stdio");

        falseOptions = new HashMap<String, String>();
        falseOptions.put("", "");
        falseOptions.put("vc", "Graphical Mode");
        falseOptions.put("stdio", "Non Graphical Mode");
    }

    public QMPModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getFilemodel().getQmpOption() != null) {
            this.setOption(falseOptions.get(this.myfile.getFilemodel().getQmpOption()));
        }
    }

    public void setOption(String option) {
        super.setOption(trueOptions.get(option),
                OptionsEnumModel.QMPOPTION.getValor());
        this.myfile.getFilemodel()
                .setQmpOption(trueOptions.get(option));
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getFilemodel().setQmpOption("");
    }
}
