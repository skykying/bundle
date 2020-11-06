package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.lembed.lite.studio.qemu.model.NetworkDnssearchUserWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.NetworkDnssearchUserWorkerView;

public class NetworkDnssearchUserWorkerControl implements ActionListener {

    private NetworkDnssearchUserWorkerView myview;
    private NetworkDnssearchUserWorkerModel mymodel;
    private List<String> myResults;

    public NetworkDnssearchUserWorkerControl(FileControl myfile, int position) {
        mymodel = new NetworkDnssearchUserWorkerModel();
        myview = new NetworkDnssearchUserWorkerView(myfile, position);
        myview.configureListener(this);
        myview.configureStandardMode();
        myResults = new ArrayList<String>();
    }

    public void change_my_visibility(boolean value) {
        myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            myview.getDnssearch().setText("");
            myview.getText().setText("");
            myview.getOption().setText("");
            myResults.clear();
            myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            myResults = mymodel.buildIt(myview.getDnssearch()
                    .getText());
            myview.setVisible(false);
        } else if (e.getActionCommand().equals("add")) {
            if (!myview.getText().getText().isEmpty()) {
                myview.buildMe(myview.getText().getText());
            }
        } else if (e.getActionCommand().equals("remove")) {
            myview.removeMe(myview.getOption().getText());
        }
    }

    public List<String> getMyResults() {
        return myResults;
    }

}
