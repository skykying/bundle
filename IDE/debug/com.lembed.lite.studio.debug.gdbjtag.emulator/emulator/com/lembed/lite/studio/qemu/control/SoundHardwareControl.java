package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.OptionsEnumModel;
import com.lembed.lite.studio.qemu.model.SoundHardwareModel;
import com.lembed.lite.studio.qemu.model.VMConfigurationModel;
import com.lembed.lite.studio.qemu.view.internal.SoundHardwareView;

public class SoundHardwareControl implements ActionListener {

    private SoundHardwareModel mymodel;
    private SoundHardwareView myview;

    public SoundHardwareControl(EmulationControl myemulation, FileControl myfile) {
        this.myview = new SoundHardwareView(myfile);
        this.myview.configureListener(this);
        this.myview.configureStandardMode();
        this.mymodel = new SoundHardwareModel(myemulation, myfile);
    }

    public void change_my_visibility(boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {

            if (this.myview.getSoundHardware().getSelectedIndex() != 0) {
                this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SOUNDHARDWAREOPTION.getValor()]);
                this.myview.getSoundHardware().setSelectedIndex(0);
            }

            this.change_my_visibility(false);
        } else if (e.getActionCommand().equals("okButton")) {

            if (this.myview.getSoundHardware().getSelectedIndex() != 0) {
                this.mymodel.setOption((String) this.myview
                        .getSoundHardware().getSelectedItem());
            } else {
                this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SOUNDHARDWAREOPTION.getValor()]);
            }

            this.change_my_visibility(false);
        }
    }

}
