package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class MemoryModel extends OptionsControl {

    private FileControl myfile;

    public MemoryModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getFilemodel().getMemPathOption() != null) {
            if (!this.myfile.getFilemodel().getMemPathOption().isEmpty()) {
                this.buildIt(this.myfile.getFilemodel().getMemPathOption());
            }
        }

        if (this.myfile.getFilemodel().getMemPreallocOption() != null) {
            if (!this.myfile.getFilemodel().getMemPreallocOption().isEmpty()) {
                if (this.myfile.getFilemodel().getMemPreallocOption().equals("true")) {
                    this.buildIt();
                }
            }
        }
    }

    public void buildIt(String path) {
        if (!path.isEmpty()) {
            path = UsabilityModel.fixPath(path);
            this.setOption(path);
            this.myfile.getFilemodel().setMemPathOption(path);
        } else {
            this.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMORYPATHOPTION.getValor()]);
        }
    }

    public void buildIt() {
        this.setOption("");
        this.myfile.getFilemodel().setMemPreallocOption("true");
    }

    private void setOption(String option) {
        if (!option.isEmpty()) {
            super.setOption(option,
                    OptionsEnumModel.MEMORYPATHOPTION.getValor());
        } else {
            super.setOption(option, OptionsEnumModel.MEMPREALLOC.getValor());
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMORYPATHOPTION.getValor()])) {
            this.myfile.getFilemodel().setMemPathOption("");
        } else if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMPREALLOC.getValor()])) {
            this.myfile.getFilemodel().setMemPreallocOption("");
        }
    }
}
