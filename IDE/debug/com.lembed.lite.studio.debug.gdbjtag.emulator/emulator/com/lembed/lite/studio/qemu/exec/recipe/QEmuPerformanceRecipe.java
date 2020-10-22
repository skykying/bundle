/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.recipe;

import com.lembed.lite.studio.qemu.exec.QEmuCustomOption;
import com.lembed.lite.studio.qemu.exec.QEmuDeviceOption;
import com.lembed.lite.studio.qemu.exec.QEmuMachineOption;
import com.lembed.lite.studio.qemu.exec.util.QEmuOptionsList;

/**
 * Generic performance options for QEmu.
 *
 * @author shevek
 */
public class QEmuPerformanceRecipe extends QEmuOptionsList {

    public QEmuPerformanceRecipe() {
        add(new QEmuMachineOption().withAcceleration(QEmuMachineOption.Acceleration.kvm, QEmuMachineOption.Acceleration.tcg));
        add(new QEmuDeviceOption.VirtioBalloon());
        // add(new QEmuCustomOption("-global", "virtio-blk-pci.scsi=off"));
    }
}
