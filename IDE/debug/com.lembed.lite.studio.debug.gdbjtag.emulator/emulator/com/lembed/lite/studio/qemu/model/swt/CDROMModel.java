package com.lembed.lite.studio.qemu.model.swt;

import java.util.Locale;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class CDROMModel extends OptionsControl {

    public CDROMModel(EmulationControl ec, EmulatorQemuMachineControl emc) {
        super(ec);
        if (emc.getMachineModel().getCdrom() != null) {
            this.setCdromOption(emc.getMachineModel().getCdrom(), "");
        }
    }

    public void setCdromOption(String cdromOption, String option) {
        if (option.equals("drive")) {
            cdromOption = UsabilityModel.fixPath(cdromOption);
            if (checks_extension(cdromOption).equals("/")) {
                super.setOption(cdromOption,
                        OptionsEnumModel.CDROMOPTION.getValor());
            } else if (checks_extension(cdromOption).equals("\\")) {
                if (cdromOption.length() == 3) {
                    if (cdromOption.charAt(1) == ':' && (cdromOption.toLowerCase(Locale.ENGLISH).charAt(0) >= 'a' && cdromOption.toLowerCase(Locale.ENGLISH).charAt(0) <= 'z')) {
                        super.setOption(cdromOption.substring(0, 2)
                                .toLowerCase(Locale.ENGLISH), OptionsEnumModel.CDROMOPTION
                                .getValor());
                    }
                } else {
                    super.setOption(cdromOption,
                            OptionsEnumModel.CDROMOPTION.getValor());
                }
            }
        } else if (option.equals("image")) {
            if (cdromOption.endsWith(".img") || cdromOption.endsWith(".iso")) {
                cdromOption = UsabilityModel.fixPath(cdromOption);
                super.setOption(cdromOption,
                        OptionsEnumModel.CDROMOPTION.getValor());
            } else {
                super.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.CDROMOPTION.getValor()]);
            }
        } else if (option.isEmpty()) {
            cdromOption = UsabilityModel.fixPath(cdromOption);
            if (cdromOption.endsWith(".img") || cdromOption.endsWith(".iso")) {
                super.setOption(cdromOption,
                        OptionsEnumModel.CDROMOPTION.getValor());
            } else if (checks_extension(cdromOption).equals("/")) {
                super.setOption(cdromOption,
                        OptionsEnumModel.CDROMOPTION.getValor());
            } else if (checks_extension(cdromOption).equals("\\")) {
                if (cdromOption.length() == 3) {
                    if (cdromOption.charAt(1) == ':' && (cdromOption.toLowerCase(Locale.ENGLISH).charAt(0) >= 'a' && cdromOption.toLowerCase(Locale.ENGLISH).charAt(0) <= 'z')) {
                        super.setOption(cdromOption.substring(0, 2)
                                .toLowerCase(Locale.ENGLISH),
                                OptionsEnumModel.CDROMOPTION.getValor());
                    }
                } else {
                    super.setOption(cdromOption,
                            OptionsEnumModel.CDROMOPTION.getValor());
                }
            } else {
                super.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.CDROMOPTION.getValor()]);
            }
        }
    }

    public String checks_extension(String path) {
        String result = "";
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                result = "/";
                break;
            }
            if (path.charAt(i) == '\\') {
                result = "\\";
                break;
            }
        }
        return result;
    }
}
