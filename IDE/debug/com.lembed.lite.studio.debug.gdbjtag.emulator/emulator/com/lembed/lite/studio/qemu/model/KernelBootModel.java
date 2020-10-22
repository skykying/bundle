package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class KernelBootModel extends OptionsControl {

    private FileControl myfile;

    public KernelBootModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getFilemodel().getKernelBootOption() != null) {
            if (!this.myfile.getFilemodel().getKernelBootOption().isEmpty()) {
                this.setOption(this.myfile.getFilemodel().getKernelBootOption());
            }
        }
    }

    public void setOption(String option) {
        this.myfile.getFilemodel()
                .setKernelBootOption(option);
        option = UsabilityModel.fixPath(option);
        super.setOption(option,
                OptionsEnumModel.KERNELBOOTOPTION.getValor());
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getFilemodel().setKernelBootOption("");
    }
}
