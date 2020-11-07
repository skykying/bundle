package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.lembed.lite.studio.qemu.model.swt.NetworkGuestfwdUserWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.swt.NetworkGuestfwdUserWorkerView;

public class NetworkGuestfwdUserWorkerControl implements BaseControl {

    private NetworkGuestfwdUserWorkerModel mymodel;
    private NetworkGuestfwdUserWorkerView myview;
    private List<String> myResults;

    public NetworkGuestfwdUserWorkerControl(EmulatorQemuMachineControl myfile, int position) {
        this.mymodel = new NetworkGuestfwdUserWorkerModel();
        this.myview = new NetworkGuestfwdUserWorkerView(myfile, position);
        this.myview.configureListener(this);
        this.myview.configureStandardMode();
        myResults = new ArrayList<String>();
    }

    public void change_my_visibility(boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            this.myview.getGuestfwd().setText("");
            this.myview.getOption().setText("");
            this.myview.getIpAddrServer().setText("");
            this.myview.getPort().setText("");
            this.myview.getSecondOption().setText("");
            this.myResults.clear();
            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            this.myResults = this.mymodel.buildIt(this.myview.getGuestfwd()
                    .getText());
            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("add")) {
            if (!this.myview.getIpAddrServer().getText().isEmpty()
                    || !this.myview.getPort().getText().isEmpty()
                    || !this.myview.getSecondOption().getText().isEmpty()) {
                this.myview.buildMe("tcp:"
                        + this.myview.getIpAddrServer().getText() + ":"
                        + this.myview.getPort().getText() + "-"
                        + this.myview.getSecondOption().getText());
            }
        } else if (e.getActionCommand().equals("remove")) {
            this.myview.removeMe(this.myview.getOption().getText());
        }
    }

    public List<String> getMyResults() {
        return this.myResults;
    }
}
