/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.recipe;

import javax.annotation.Nonnull;

import com.lembed.lite.studio.qemu.exec.QEmuDisplayOption;
import com.lembed.lite.studio.qemu.exec.VncDisplay;
import com.lembed.lite.studio.qemu.exec.util.QEmuOptionsList;
import com.lembed.lite.studio.qemu.exec.vm.device.QEmuVgaDevice;

/**
 *
 * @author shevek
 */
@Deprecated // Too specialized to be really useful.
public class QEmuDisplayRecipe extends QEmuOptionsList implements QEmuRecipe {

    public final QEmuDisplayOption displayOption;
    public final QEmuVgaDevice.CirrusPci vgaOption;

    public QEmuDisplayRecipe(@Nonnull VncDisplay display) {
        this.displayOption = new QEmuDisplayOption(display);
        add(displayOption);
        this.vgaOption = new QEmuVgaDevice.CirrusPci();
        add(vgaOption);
    }
}
