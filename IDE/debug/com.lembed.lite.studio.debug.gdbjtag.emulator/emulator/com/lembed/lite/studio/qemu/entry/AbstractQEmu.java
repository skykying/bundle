/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.entry;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.lembed.lite.studio.qemu.exec.QEmuMemoryOption;
import com.lembed.lite.studio.qemu.image.QEmuImage;
import com.lembed.lite.studio.qemu.image.QEmuImageFormat;
import com.lembed.lite.studio.qemu.manager.QEmuManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class AbstractQEmu {

    protected final QEmuManager manager = new QEmuManager();

    @Nonnull
    protected QEmuImage newImage(@Nonnull String name) throws IOException {
        File dir = new File("build/images/local");
        dir.mkdirs();
        return new QEmuImage(new File(dir, getClass().getSimpleName() + "-" + name));
    }

    @Nonnull
    protected QEmuImage newImage(@Nonnull String name, @Nonnegative long size, @Nonnull QEmuMemoryOption.Magnitude magnitude) throws IOException {
        QEmuImageFormat format = QEmuImageFormat.qcow2;
        QEmuImage image = newImage(name + "." + format);
        image.create(format, magnitude.toUnit(size));
        return image;
    }

    @Nonnull
    private File download(@Nonnull URI source) throws IOException {
        File dir = new File("build/images/downloaded");
        HashCode hash = Hashing.md5().hashString(source.toString(), Charsets.UTF_8);
        File file = new File(dir, hash.toString());
        if (!file.exists()) {
            InputStream in = source.toURL().openStream();
            try {
                Files.asByteSink(file).writeFrom(in);
            } finally {
                Closeables.close(in, false);
            }
        }
        return file;
    }

    @Nonnull
    protected QEmuImage newImage(@Nonnull String name, @Nonnull URI source) throws IOException {
        QEmuImageFormat format = QEmuImageFormat.qcow2;
        QEmuImage image = newImage(name + "." + format);

        File file = download(source);
        image.create(QEmuImageFormat.qcow2, file);
        return image;
    }

    public abstract void invoke(@Nonnull String[] args) throws Exception;

}
