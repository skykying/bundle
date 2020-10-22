/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.vm.device;

import com.lembed.lite.studio.qemu.exec.QEmuDeviceOption;
import com.lembed.lite.studio.qemu.exec.QEmuDisplayOption;
import com.lembed.lite.studio.qemu.exec.QEmuDriveOption;
import com.lembed.lite.studio.qemu.exec.QEmuNetdevOption;
import com.lembed.lite.studio.qemu.exec.QEmuOption;

/**
 * A QEmuDevice is a device frontend visible to the VM on a bus.
 *
 * Many devices are paired with device backends expressed via
 * {@link QEmuDriveOption}, {@link QEmuNetdevOption},
 * {@link QEmuDisplayOption}, etc.
 *
 * This interface is a convenience for documentation and may go away or
 * be replaced.
 *
 * @see QEmuDeviceOption
 * @author shevek
 */
// TODO: Replace this with QEmuDeviceOption?
public interface QEmuDevice extends QEmuOption {

}
