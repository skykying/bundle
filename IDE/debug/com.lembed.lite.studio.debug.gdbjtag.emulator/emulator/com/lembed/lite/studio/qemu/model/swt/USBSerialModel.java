package com.lembed.lite.studio.qemu.model.swt;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class USBSerialModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    private String option;

    private String vendorid;

    private String productid;

    private String dev;

    public USBSerialModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (myfile.getMachineModel().getUsbSerialOption() != null) {
            if (!myfile.getMachineModel().getUsbSerialOption().isEmpty()) {
                this.option = myfile.getMachineModel().getUsbSerialOption();
                this.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBSERIALOPTION.getValor()]);
            }
        }
    }

    public void setOption(String option) {
        if (option.equals(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBSERIALOPTION.getValor()])) {
            super.setOption("serial:" + this.option,
                    OptionsEnumModel.USBSERIALOPTION.getValor());
            this.myfile.getMachineModel()
                    .setUsbSerialOption(this.option);
        }
    }

    public void unsetOption(String option) {
        super.unsetOption(option, OptionsEnumModel.USBSERIALOPTION.getValor());
        this.myfile.getMachineModel().setUsbSerialOption("");
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public void setOption() {
        StringBuilder result = new StringBuilder("");
        if (!vendorid.isEmpty()) {
            result.append("vendorid=").append(vendorid);
            if (!productid.isEmpty()) {
                result.append(",productid=").append(productid);
            }
            if (!dev.isEmpty()) {
                result.append(":").append(dev);
            }
        } else if (!productid.isEmpty()) {
            result.append("productid=").append(productid);
            if (!dev.isEmpty()) {
                result.append(":").append(dev);
            }
        } else {
            result.append(dev);
        }
        option = result.toString();
    }
}
