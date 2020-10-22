package com.lembed.lite.studio.qemu.model;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;

public class USBDiskModel extends OptionsControl {

    private FileControl myfile;

    private String file;

    public USBDiskModel(EmulationControl myemulation, FileControl myfile) {
        super(myemulation);
        this.myfile = myfile;
        this.file = "";

        if (this.myfile.getFilemodel().getUsbDiskOption() != null) {
            if (!this.myfile.getFilemodel().getUsbDiskOption().isEmpty()) {
                file = this.myfile.getFilemodel().getUsbDiskOption();
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDISKOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDISKOPTION.getValor()])) {
            this.myfile.getFilemodel()
                    .setUsbDiskOption(file);
            super.setOption(getTrueString(file),
                    OptionsEnumModel.USBDISKOPTION.getValor());
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBDISKOPTION.getValor());
        this.myfile.getFilemodel().setUsbDiskOption("");
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    private String getTrueString(String file) {
        if (file.endsWith(".img")) {
            return "disk:format=raw:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".qcow2")) {
            return "disk:format=qcow2:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".qed")) {
            return "disk:format=qed:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".qcow")) {
            return "disk:format=qcow:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".cow")) {
            return "disk:format=cow:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".vdi")) {
            return "disk:format=vdi:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".vmdk")) {
            return "disk:format=vmdk:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".vpc")) {
            return "disk:format=vpc:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".bochs")) {
            return "disk:format=bochs:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".cloop")) {
            return "disk:format=cloop:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".dmg")) {
            return "disk:format=dmg:" + UsabilityModel.fixPath(file);
        } else if (file.endsWith(".parallels")) {
            return "disk:format=parallels:" + UsabilityModel.fixPath(file);
        }
        return null;
    }

}
