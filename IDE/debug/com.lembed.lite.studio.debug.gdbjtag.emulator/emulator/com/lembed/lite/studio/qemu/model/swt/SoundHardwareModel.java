package com.lembed.lite.studio.qemu.model.swt;

import java.util.HashMap;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;

public class SoundHardwareModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;

    // Association "Description" - "Option".
    private static HashMap<String, String> trueOptions;

    // Association "Option" - "Description".
    private static HashMap<String, String> falseOptions;

    static {
        trueOptions = new HashMap<String, String>();
        trueOptions.put("", "");
        trueOptions.put("Creative Sound Blaster 16", "sb16");
        trueOptions.put("PC speaker", "pcspk");
        trueOptions.put("Intel HD Audio", "hda");
        trueOptions.put("Gravis Ultrasound GF1", "gus");
        trueOptions.put("ENSONIQ AudioPCI ES1370", "es1370");
        trueOptions.put("CS4231A", "cs4231a");
        trueOptions.put("Yamaha YM3812 (OPL2)", "adlib");
        trueOptions.put("Intel 82801AA AC97 Audio", "ac97");
        trueOptions.put("All of the above", "all");

        falseOptions = new HashMap<String, String>();
        falseOptions.put("", "");
        falseOptions.put("sb16", "Creative Sound Blaster 16");
        falseOptions.put("pcspk", "PC speaker");
        falseOptions.put("hda", "Intel HD Audio");
        falseOptions.put("gus", "Gravis Ultrasound GF1");
        falseOptions.put("es1370", "ENSONIQ AudioPCI ES1370");
        falseOptions.put("cs4231a", "CS4231A");
        falseOptions.put("adlib", "Yamaha YM3812 (OPL2)");
        falseOptions.put("ac97", "Intel 82801AA AC97 Audio");
        falseOptions.put("all", "All of the above");
    }

    public SoundHardwareModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        super(myemulation);
        this.myfile = myfile;

        if (this.myfile.getMachineModel().getSoundHardwareOption() != null) {
            this.setOption(falseOptions.get(this.myfile.getMachineModel().getSoundHardwareOption()));
        }

    }

    public void setOption(String option) {
        super.setOption(trueOptions.get(option),
                OptionsEnumModel.SOUNDHARDWAREOPTION.getValor());
        this.myfile.getMachineModel()
                .setSoundHardwareOption(trueOptions.get(option));
    }

    public void unsetOption(String option) {
        super.unsetOption(option);
        this.myfile.getMachineModel().setSoundHardwareOption("");
    }

}
