/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.vm.device;

import com.lembed.lite.studio.qemu.exec.QEmuDeviceOption;

/**
 *
 * @author shevek
 */
public interface QEmuVgaDevice extends QEmuDevice {

    public static class CirrusPci extends QEmuDeviceOption.Pci implements QEmuVgaDevice {

        public CirrusPci() {
            super("cirrus-vga");
        }
    }
}
