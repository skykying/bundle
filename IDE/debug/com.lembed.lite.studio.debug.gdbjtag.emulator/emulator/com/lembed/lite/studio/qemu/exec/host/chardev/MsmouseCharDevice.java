/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.host.chardev;

/**
 *
 * @author shevek
 */
public class MsmouseCharDevice extends AbstractCharDevice {

    public MsmouseCharDevice() {
        super("msmouse");
    }

    @Override
    public String toString() {
        return "msmouse";
    }
}
