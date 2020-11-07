package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.NetworkDumpWorkerModel;
import com.lembed.lite.studio.qemu.model.swt.NetworkWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.swt.NetworkDumpWorkerView;

public class NetworkDumpWorkerControl implements ActionListener {

    private NetworkDumpWorkerModel mymodel;
    private NetworkDumpWorkerView myview;

    public NetworkDumpWorkerControl(EmulatorQemuMachineControl myfile,
            NetworkWorkerModel nmodel, int position) {
        mymodel = new NetworkDumpWorkerModel(nmodel);
        myview = new NetworkDumpWorkerView(myfile, position);
        myview.configureListener(this);
        myview.configureStandardMode();
    }

    public void change_my_visibility(boolean value) {
        myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            if (myview.getIsEnabled().isSelected()) {
                myview.getIsEnabled().setSelected(false);
            }
            if (myview.getVlan().getSelectedIndex() != 0) {
                myview.getVlan().setSelectedIndex(0);
            }
            if (!myview.getFile().getText().isEmpty()) {
                myview.getFile().setText("");
            }
            if (!myview.getLen().getText().isEmpty()) {
                myview.getLen().setText("");
            }
            mymodel.buildIt((String) myview.getVlan().getSelectedItem(),
                    myview.getFile().getText(),
                    myview.getLen().getText());
            myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (myview.getIsEnabled().isSelected()) {
                mymodel.buildIt((String) myview.getVlan().getSelectedItem(),
                        myview.getFile().getText(),
                        myview.getLen().getText());
            } else {
                if (myview.getVlan().getSelectedIndex() != 0) {
                    myview.getVlan().setSelectedIndex(0);
                }
                if (!myview.getFile().getText().isEmpty()) {
                    myview.getFile().setText("");
                }
                if (!myview.getLen().getText().isEmpty()) {
                    myview.getLen().setText("");
                }
                mymodel.buildIt((String) myview.getVlan().getSelectedItem(),
                        myview.getFile().getText(),
                        myview.getLen().getText());
            }
            myview.setVisible(false);
        } else if (e.getActionCommand().equals("fileChooser")) {
            myview.setChoosertitle("Choose the file!");
            myview.setFileDescription("File");
            if (myview.chooseAnyFile()) {
                myview.getFile().setText(myview.getChoice());
            }
        }

    }

    public void cleanMe() {
        if (myview.getIsEnabled().isSelected()) {
            myview.getIsEnabled().setSelected(false);
        }
    }

    public boolean isSelected() {
        return myview.getIsEnabled().isSelected();
    }
}
