package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.KeyboardModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.KeyboardView;

public class KeyboardControl implements BaseControl {

    private KeyboardModel keyboardModel;
    private KeyboardView keyboardView;

    public KeyboardControl(EmulationControl ec, EmulatorQemuMachineControl emc) {
        this.keyboardView = new KeyboardView(emc);
        this.keyboardView.configureListener(this);
        this.keyboardView.configureStandardMode();
        this.keyboardModel = new KeyboardModel(ec, emc);
    }

    public void setVisible(boolean value) {
        this.keyboardView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {

            if (this.keyboardView.getKeyboardLayoutLanguage().getSelectedIndex() != 0) {
                this.keyboardView.getKeyboardLayoutLanguage().setSelectedIndex(0);
            }

            this.keyboardModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.KEYBOARDOPTION.getValor()]);

            this.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {

            if (this.keyboardView.getKeyboardLayoutLanguage().getSelectedIndex() != 0) {
                this.keyboardModel.setOption(this.keyboardView
                        .getKeyboardLayoutLanguage()
                        .getSelectedItem()
                        .toString()
                        .substring(keyboardView.getKeyboardLayoutLanguage().getSelectedItem().toString()
                                .indexOf(":") + 2));
            } else {
                this.keyboardModel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.KEYBOARDOPTION.getValor()]);
            }

            this.setVisible(false);
        }
    }

}
