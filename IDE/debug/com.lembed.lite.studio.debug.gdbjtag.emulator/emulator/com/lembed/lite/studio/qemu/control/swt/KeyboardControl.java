package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.KeyboardModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.KeyboardView;

public class KeyboardControl implements ActionListener {

    private KeyboardModel mymodel;
    private KeyboardView myview;

    public KeyboardControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        this.myview = new KeyboardView(myfile);
        this.myview.configureListener(this);
        this.myview.configureStandardMode();
        this.mymodel = new KeyboardModel(myemulation, myfile);
    }

    public void change_my_visibility(boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {

            if (this.myview.getKeyboardLayoutLanguage().getSelectedIndex() != 0) {
                this.myview.getKeyboardLayoutLanguage().setSelectedIndex(0);
            }

            this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.KEYBOARDOPTION.getValor()]);

            this.change_my_visibility(false);
        } else if (e.getActionCommand().equals("okButton")) {

            if (this.myview.getKeyboardLayoutLanguage().getSelectedIndex() != 0) {
                this.mymodel.setOption(this.myview
                        .getKeyboardLayoutLanguage()
                        .getSelectedItem()
                        .toString()
                        .substring(myview.getKeyboardLayoutLanguage().getSelectedItem().toString()
                                .indexOf(":") + 2));
            } else {
                this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.KEYBOARDOPTION.getValor()]);
            }

            this.change_my_visibility(false);
        }
    }

}
