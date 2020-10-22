/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.entry;

//import com.google.auto.service.AutoService;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.lembed.lite.studio.qemu.exec.QEmuArchitecture;
import com.lembed.lite.studio.qemu.exec.QEmuCommandLine;
import com.lembed.lite.studio.qemu.exec.QEmuDisplayOption;
import com.lembed.lite.studio.qemu.exec.QEmuMemoryOption;
import com.lembed.lite.studio.qemu.exec.VncDisplay;
import com.lembed.lite.studio.qemu.exec.recipe.QEmuMonitorRecipe;
import com.lembed.lite.studio.qemu.exec.recipe.QEmuPerformanceRecipe;
import com.lembed.lite.studio.qemu.exec.recipe.QEmuVirtioDriveRecipe;
import com.lembed.lite.studio.qemu.image.QEmuImage;
import com.lembed.lite.studio.qemu.manager.QEmuProcess;
import com.lembed.lite.studio.qemu.qapi.api.AddClientCommand.Response;
import com.lembed.lite.studio.qemu.qapi.api.DeviceDelCommand;
import com.lembed.lite.studio.qemu.qapi.common.QApiConnection;
import com.lembed.lite.studio.qemu.qapi.common.QApiResponse;

import java.io.ByteArrayInputStream;

/**
 *
 * @author shevek
 */
//@AutoService(QEmuExample.class)
public class QEmuRAID extends AbstractQEmu {

    @SuppressWarnings("unused")
	@Override
    public void invoke(String[] args) throws Exception {
//        QEmuImage root = newImage("root", URI.create("http://ubuntu.com/cloud.img"));
        QEmuImage sdb = newImage("sdb", 1, QEmuMemoryOption.Magnitude.GIGA);
        QEmuImage sdc = newImage("sdc", 1, QEmuMemoryOption.Magnitude.GIGA);

        QEmuCommandLine line = new QEmuCommandLine(QEmuArchitecture.x86_64);
        line.addOptions(
                new QEmuPerformanceRecipe(),
                new QEmuMonitorRecipe(4447),
                new QEmuDisplayOption(new VncDisplay.Socket(4)),
//                new QEmuVirtioDriveRecipe(line.getAllocator(), root),
                new QEmuVirtioDriveRecipe(line.getAllocator(), sdb),
                new QEmuVirtioDriveRecipe(line.getAllocator(), sdc)
        );

        QEmuProcess process = manager.execute(line);
        QApiConnection connection = process.getConnection();

        JSch jsch = new JSch();
        Session session = jsch.getSession("root", "10.42.43.2");
        session.connect();

        UPLOAD:
        {
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp-server");
            sftp.connect();
            sftp.put(new ByteArrayInputStream(new byte[]{}), "/root/script0.sh");
            sftp.put(new ByteArrayInputStream(new byte[]{}), "/root/script1.sh");
            sftp.disconnect();
        }

        SETUP:
        {
            ChannelExec exec = (ChannelExec) session.openChannel("exec");
            exec.setCommand("/root/script0.sh");
            exec.connect();
            exec.disconnect();
        }
        
        DeviceDelCommand devDelCmd = new DeviceDelCommand("virtio-disk-2");
        connection.call(devDelCmd);
        

        TEST:
        {
            ChannelExec exec = (ChannelExec) session.openChannel("exec");
            exec.setCommand("/root/script1.sh");
            exec.connect();
            exec.disconnect();
        }

        process.destroy();
        connection.close();
    }
}
