package com.lembed.lite.studio.qemu.model.swt;

import java.util.ArrayList;
import java.util.List;

public class NetworkGuestfwdUserWorkerModel {

    public List<String> buildIt(String guestfwd) {
        List<String> result = new ArrayList<String>();
        String[] options = guestfwd.split("\n");
        for (String option : options) {
            result.add(option);
        }
        return result;
    }
}
