package com.lembed.lite.studio.qemu.model.swt;

import java.util.Locale;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class FloppyModel extends OptionsControl {

    public FloppyModel(EmulationControl em, EmulatorQemuMachineControl ec) {
        super(em);
        if (ec.getMachineModel().getFloppyDiskA() != null) {
            this.setFloppyDiskAOption(ec.getMachineModel().getFloppyDiskA(), "");
        }
    }

    public void setFloppyDiskAOption(String floppyDiskOption, String option) {
        if (option.equals("drive")) {
            if (checks_extension(floppyDiskOption).equals("/")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKAOPTION.getValor());
            } else if (checks_extension(floppyDiskOption).equals("\\")) {
                if (floppyDiskOption.length() == 3) {
                    if (floppyDiskOption.charAt(1) == ':' && (floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) >= 'a' && floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) <= 'z')) {
                        super.setOption("\\" + "\\" + "." + "\\"
                                + floppyDiskOption.substring(0, 2)
                                .toLowerCase(Locale.ENGLISH), OptionsEnumModel.FLOPPYDISKAOPTION
                                .getValor());
                    }
                } else {
                    super.setOption(floppyDiskOption,
                            OptionsEnumModel.FLOPPYDISKAOPTION.getValor());
                }
            }
        } else if (option.equals("image")) {
            if (floppyDiskOption.endsWith(".img") || floppyDiskOption.endsWith(".iso")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKAOPTION.getValor());
            } else {
                super.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FLOPPYDISKAOPTION.getValor()]);
            }
        } else if (option.isEmpty()) {
            if (floppyDiskOption.endsWith(".img") || floppyDiskOption.endsWith(".iso")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKAOPTION.getValor());
            } else if (checks_extension(floppyDiskOption).equals("/")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKAOPTION.getValor());
            } else if (checks_extension(floppyDiskOption).equals("\\")) {
                if (floppyDiskOption.length() == 3) {
                    if (floppyDiskOption.charAt(1) == ':' && (floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) >= 'a' && floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) <= 'z')) {
                        super.setOption("\\" + "\\" + "." + "\\"
                                + floppyDiskOption.substring(0, 2)
                                .toLowerCase(Locale.ENGLISH),
                                OptionsEnumModel.FLOPPYDISKAOPTION.getValor());
                    }
                } else {
                    super.setOption(floppyDiskOption,
                            OptionsEnumModel.FLOPPYDISKAOPTION.getValor());
                }
            } else {
                super.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FLOPPYDISKAOPTION.getValor()]);
            }
        }
    }

    public void setFloppyDiskBOption(String floppyDiskOption, String option) {
        if (option.equals("drive")) {
            if (checks_extension(floppyDiskOption).equals("/")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKBOPTION.getValor());
            } else if (checks_extension(floppyDiskOption).equals("\\")) {
                if (floppyDiskOption.length() == 3) {
                    if (floppyDiskOption.charAt(1) == ':' && (floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) >= 'a' && floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) <= 'z')) {
                        super.setOption("\\" + "\\" + "." + "\\"
                                + floppyDiskOption.substring(0, 2)
                                .toLowerCase(Locale.ENGLISH), OptionsEnumModel.FLOPPYDISKBOPTION
                                .getValor());
                    }
                } else {
                    super.setOption(floppyDiskOption,
                            OptionsEnumModel.FLOPPYDISKBOPTION.getValor());
                }
            }
        } else if (option.equals("image")) {
            if (floppyDiskOption.endsWith(".img") || floppyDiskOption.endsWith(".iso")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKBOPTION.getValor());
            } else {
                super.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FLOPPYDISKBOPTION.getValor()]);
            }
        } else if (option.isEmpty()) {
            if (floppyDiskOption.endsWith(".img") || floppyDiskOption.endsWith(".iso")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKBOPTION.getValor());
            } else if (checks_extension(floppyDiskOption).equals("/")) {
                super.setOption(floppyDiskOption,
                        OptionsEnumModel.FLOPPYDISKBOPTION.getValor());
            } else if (checks_extension(floppyDiskOption).equals("\\")) {
                if (floppyDiskOption.length() == 3) {
                    if (floppyDiskOption.charAt(1) == ':' && (floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) >= 'a' && floppyDiskOption.toLowerCase(Locale.ENGLISH).charAt(0) <= 'z')) {
                        super.setOption("\\" + "\\" + "." + "\\"
                                + floppyDiskOption.substring(0, 2)
                                .toLowerCase(Locale.ENGLISH),
                                OptionsEnumModel.FLOPPYDISKBOPTION.getValor());
                    }
                } else {
                    super.setOption(floppyDiskOption,
                            OptionsEnumModel.FLOPPYDISKBOPTION.getValor());
                }
            } else {
                super.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.FLOPPYDISKBOPTION.getValor()]);
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
