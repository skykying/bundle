/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.host.chardev;

/**
 *
 * @author shevek
 */
public class NullCharDevice extends AbstractCharDevice {

    public NullCharDevice() {
        super("null");
    }

    @Override
    public String toString() {
        return "null";
    }
}
