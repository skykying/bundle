package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.OptionsEnumModel;
import com.lembed.lite.studio.qemu.model.TimeModel;
import com.lembed.lite.studio.qemu.model.VMConfigurationModel;
import com.lembed.lite.studio.qemu.view.internal.TimeView;

public class TimeControl implements ActionListener {

    private TimeView myview;
    private TimeModel mymodel;

    public TimeControl(EmulationControl myemulation, FileControl myfile) {
        this.myview = new TimeView(myfile);
        this.mymodel = new TimeModel(myemulation, myfile);
    }

    public void starts() {
        this.myview.configureStandardMode();
        this.myview.configureListener(this);
    }

    public void change_the_visibility_of_view(Boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.RTCOPTION.getValor()]);
            if (this.myview.getIsEnabled().isSelected()) {
                this.myview.getIsEnabled().setSelected(false);
            }
            this.myview.getBase().setSelectedIndex(0);
            this.myview.getDate().setText("");
            this.myview.getClock().setSelectedIndex(0);
            this.myview.getDriftfix().setSelectedIndex(0);
            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (this.myview.getIsEnabled().isSelected()) {
                this.mymodel.buildIt((String) this.myview.getBase()
                        .getSelectedItem(), this.myview.getDate().getText(),
                        (String) this.myview.getClock().getSelectedItem(),
                        (String) this.myview.getDriftfix().getSelectedItem());
            } else {
                this.myview.getBase().setSelectedIndex(0);
                this.myview.getDate().setText("");
                this.myview.getClock().setSelectedIndex(0);
                this.myview.getDriftfix().setSelectedIndex(0);
                this.mymodel.buildIt((String) this.myview.getBase()
                        .getSelectedItem(), this.myview.getDate().getText(),
                        (String) this.myview.getClock().getSelectedItem(),
                        (String) this.myview.getDriftfix().getSelectedItem());
            }
            this.myview.setVisible(false);
        }
    }

}
