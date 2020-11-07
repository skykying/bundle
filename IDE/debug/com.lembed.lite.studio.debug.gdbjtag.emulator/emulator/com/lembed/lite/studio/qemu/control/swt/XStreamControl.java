package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.XStreamModel;

public class XStreamControl {

    private XStreamModel myModel;
    private static XStreamControl instance = null;

    public synchronized static XStreamControl getInstance() {
        if (instance == null) {
            instance = new XStreamControl();
        }

        return instance;
    }

    public XStreamControl() {
        myModel = new XStreamModel();
    }

    public XStreamModel getMyModel() {
        return myModel;
    }
}
