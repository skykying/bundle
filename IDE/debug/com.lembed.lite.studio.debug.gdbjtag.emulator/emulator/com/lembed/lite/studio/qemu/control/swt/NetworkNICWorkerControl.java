package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.NetworkNICWorkerModel;
import com.lembed.lite.studio.qemu.model.swt.NetworkWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.swt.NetworkNICWorkerView;

public class NetworkNICWorkerControl implements BaseControl {

    private NetworkNICWorkerView myview;
    private NetworkNICWorkerModel mymodel;

    public NetworkNICWorkerControl(EmulatorQemuMachineControl myfile,
            NetworkWorkerModel mymodel, int position) {
        this.myview = new NetworkNICWorkerView(myfile, position);
        this.mymodel = new NetworkNICWorkerModel(mymodel);
        this.myview.configureListener(this);
        this.myview.configureStandardMode();
    }

    public void setVisible(boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            if (this.myview.getIsEnabled().isSelected()) {
                this.myview.getIsEnabled().setSelected(false);
            }
            if (this.myview.getVlan().getSelectedIndex() != 0) {
                this.myview.getVlan().setSelectedIndex(0);
            }
            if (!this.myview.getMacaddr().getText().isEmpty()) {
                this.myview.getMacaddr().setText("");
            }
            if (this.myview.getModel().getSelectedIndex() != 0) {
                this.myview.getModel().setSelectedIndex(0);
            }
            if (!this.myview.getNameContents().getText().isEmpty()) {
                this.myview.getNameContents().setText("");
            }
            if (!this.myview.getAddr().getText().isEmpty()) {
                this.myview.getAddr().setText("");
            }
            if (this.myview.getVectorsNumber().getSelectedIndex() != 0) {
                this.myview.getVectorsNumber().setSelectedIndex(0);
            }
            this.mymodel.buildIt((String) this.myview.getVlan().getSelectedItem(),
                    this.myview.getMacaddr().getText(),
                    (String) this.myview.getModel().getSelectedItem(),
                    this.myview.getNameContents().getText(),
                    this.myview.getAddr().getText(),
                    (String) this.myview.getVectorsNumber().getSelectedItem());
            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (this.myview.getIsEnabled().isSelected()) {
                this.mymodel.buildIt((String) this.myview.getVlan().getSelectedItem(),
                        this.myview.getMacaddr().getText(),
                        (String) this.myview.getModel().getSelectedItem(),
                        this.myview.getNameContents().getText(),
                        this.myview.getAddr().getText(),
                        (String) this.myview.getVectorsNumber().getSelectedItem());
            } else {
                if (this.myview.getVlan().getSelectedIndex() != 0) {
                    this.myview.getVlan().setSelectedIndex(0);
                }
                if (!this.myview.getMacaddr().getText().isEmpty()) {
                    this.myview.getMacaddr().setText("");
                }
                if (this.myview.getModel().getSelectedIndex() != 0) {
                    this.myview.getModel().setSelectedIndex(0);
                }
                if (!this.myview.getNameContents().getText().isEmpty()) {
                    this.myview.getNameContents().setText("");
                }
                if (!this.myview.getAddr().getText().isEmpty()) {
                    this.myview.getAddr().setText("");
                }
                if (this.myview.getVectorsNumber().getSelectedIndex() != 0) {
                    this.myview.getVectorsNumber().setSelectedIndex(0);
                }
                this.mymodel.buildIt((String) this.myview.getVlan().getSelectedItem(),
                        this.myview.getMacaddr().getText(),
                        (String) this.myview.getModel().getSelectedItem(),
                        this.myview.getNameContents().getText(),
                        this.myview.getAddr().getText(),
                        (String) this.myview.getVectorsNumber().getSelectedItem());
            }
            this.myview.setVisible(false);
        }
    }

    public boolean isSelected() {
        return this.myview.getIsEnabled().isSelected();
    }
}
