/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.recipe;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.lembed.lite.studio.qemu.exec.QEmuChardevOption;
import com.lembed.lite.studio.qemu.exec.QEmuMonitorOption;
import com.lembed.lite.studio.qemu.exec.host.chardev.CharDevice;
import com.lembed.lite.studio.qemu.exec.host.chardev.TcpCharDevice;
import com.lembed.lite.studio.qemu.exec.util.QEmuOptionsList;

/**
 * A recipe for creating a QEmu monitor speaking the QApi protocol.
 *
 * @author shevek
 */
public class QEmuMonitorRecipe extends QEmuOptionsList implements QEmuRecipe {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8249082703797408187L;
	
	public final QEmuChardevOption chardevOption;
    public final QEmuMonitorOption monitorOption;

    /** Creates a QEmuMonitor on a given {@link CharDevice}. */
    public QEmuMonitorRecipe(@Nonnull CharDevice device) {
        int index = 0;
        chardevOption = new QEmuChardevOption(device);
        chardevOption
                .withId("monitor-" + index);
        add(chardevOption);
        monitorOption = new QEmuMonitorOption(chardevOption);
        add(monitorOption);
    }

    /** Creates a QEmuMonitor on a given TCP port. */
    public QEmuMonitorRecipe(@Nonnegative int port) {
        this(new TcpCharDevice(port));
    }
}
