package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.NetworkBridgeWorkerModel;
import com.lembed.lite.studio.qemu.model.NetworkWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.NetworkBridgeWorkerView;

public class NetworkBridgeWorkerControl implements ActionListener {

    private NetworkBridgeWorkerView myview;
    private NetworkBridgeWorkerModel mymodel;

    public NetworkBridgeWorkerControl(FileControl myfile,
            NetworkWorkerModel model, int position) {
        myview = new NetworkBridgeWorkerView(myfile, position);
        mymodel = new NetworkBridgeWorkerModel(model);
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
            if (!myview.getNameContents().getText().isEmpty()) {
                myview.getNameContents().setText("");
            }
            if (!myview.getBridgeDevice().getText().isEmpty()) {
                myview.getBridgeDevice().setText("");
            }
            if (!myview.getHelper().getText().isEmpty()) {
                myview.getHelper().setText("");
            }
            mymodel.buildIt((String) myview.getVlan()
                    .getSelectedItem(),
                    myview.getNameContents().getText(), myview
                    .getBridgeDevice().getText(), myview
                    .getHelper().getText());
            myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            if (myview.getIsEnabled().isSelected()) {
                mymodel.buildIt((String) myview.getVlan()
                        .getSelectedItem(), myview.getNameContents()
                        .getText(), myview.getBridgeDevice().getText(),
                        myview.getHelper().getText());
            } else {
                if (myview.getVlan().getSelectedIndex() != 0) {
                    myview.getVlan().setSelectedIndex(0);
                }
                if (!myview.getNameContents().getText().isEmpty()) {
                    myview.getNameContents().setText("");
                }
                if (!myview.getBridgeDevice().getText().isEmpty()) {
                    myview.getBridgeDevice().setText("");
                }
                if (!myview.getHelper().getText().isEmpty()) {
                    myview.getHelper().setText("");
                }
                mymodel.buildIt((String) myview.getVlan()
                        .getSelectedItem(),
                        myview.getNameContents().getText(), myview
                        .getBridgeDevice().getText(), myview
                        .getHelper().getText());
            }
            myview.setVisible(false);
        } else if (e.getActionCommand().equals("helperChooser")) {
            myview
                    .setChoosertitle("Choose the network helper to configure the TAP interface!");
            myview.setFileDescription("File - Network helper");
            if (myview.chooseAnyFile()) {
                myview.getHelper().setText(myview.getChoice());
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
