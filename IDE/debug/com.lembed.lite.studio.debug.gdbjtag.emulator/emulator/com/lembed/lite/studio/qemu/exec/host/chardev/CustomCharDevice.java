/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.host.chardev;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author shevek
 */
public class CustomCharDevice extends AbstractCharDevice {

    public final Map<String, Object> properties = new HashMap<String, Object>();

    public CustomCharDevice(String name) {
        super(name);
    }

    @Override
    protected void addProperties(Map<String, Object> m) {
        m.putAll(properties);
    }
}