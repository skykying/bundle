/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.util;

import java.net.InetSocketAddress;
import java.util.UUID;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.lembed.lite.studio.qemu.exec.QEmuChardevOption;
import com.lembed.lite.studio.qemu.exec.QEmuCommandLine;
import com.lembed.lite.studio.qemu.exec.QEmuIdOption;
import com.lembed.lite.studio.qemu.exec.QEmuMonitorOption;
import com.lembed.lite.studio.qemu.exec.host.chardev.TcpCharDevice;

/**
 *
 * @author shevek
 */
public class QEmuCommandLineUtils {

    @CheckForNull
    public static UUID getUuid(@Nonnull QEmuCommandLine commandLine) {
        QEmuIdOption idOption = commandLine.getOption(QEmuIdOption.class);
        if (idOption == null) {
            return null;
        }
        
        return idOption.getUuid();
    }

    @CheckForNull
    public static InetSocketAddress getMonitorAddress(@Nonnull QEmuCommandLine commandLine) {
        QEmuMonitorOption monitorOption = commandLine.getOption(QEmuMonitorOption.class);
        if (monitorOption == null) {
            return null;
        }
        
        String monitorChardev = monitorOption.chardev;
        for (QEmuChardevOption chardevOption : commandLine.getOptions(QEmuChardevOption.class)) {
            if (chardevOption.id.equals(monitorChardev)) {
                if (chardevOption.device instanceof TcpCharDevice) {
                    TcpCharDevice tcpDevice = (TcpCharDevice) chardevOption.device;
                    return tcpDevice.getAddress();
                }
            }
        }
        return null;
    }

    /** TODO: Write a thread-based implementation of this method if anyone complains. */
//    @IgnoreJRERequirement
    public static void redirectIO(@Nonnull ProcessBuilder builder) {
        builder.inheritIO();
    }

    private QEmuCommandLineUtils() {
    }
}
