package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.MonitorModel;
import com.lembed.lite.studio.qemu.model.swt.QMPModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.MonitorView;

public class MonitorControl implements BaseControl {

    private MonitorView myview;
    private MonitorModel mymodel;
    private QMPModel myqmpmodel;

    public MonitorControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        this.myview = new MonitorView(myfile);
        this.myview.configureListener(this);
        this.myview.configureStandardMode();
        this.mymodel = new MonitorModel(myemulation, myfile);
        this.myqmpmodel = new QMPModel(myemulation, myfile);
    }

    public void setVisible(boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {

            if (this.myview.getMonitor().getSelectedIndex() != 0) {
                this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MONITOROPTION.getValor()]);
                this.myview.getMonitor().setSelectedIndex(0);
            }

            if (this.myview.getQmp().getSelectedIndex() != 0) {
                this.myqmpmodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.QMPOPTION.getValor()]);
                this.myview.getQmp().setSelectedIndex(0);
            }

            this.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {

            if (this.myview.getMonitor().getSelectedIndex() != 0) {
                this.mymodel.setOption((String) this.myview
                        .getMonitor().getSelectedItem());
            } else {
                this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MONITOROPTION.getValor()]);
            }

            if (this.myview.getQmp().getSelectedIndex() != 0) {
                this.myqmpmodel.setOption((String) this.myview
                        .getQmp().getSelectedItem());
            } else {
                this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.QMPOPTION.getValor()]);
            }

            this.setVisible(false);
        }
    }

}
