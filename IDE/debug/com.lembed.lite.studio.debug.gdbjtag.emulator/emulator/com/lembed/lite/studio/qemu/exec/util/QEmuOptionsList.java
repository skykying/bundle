/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lembed.lite.studio.qemu.exec.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.lembed.lite.studio.qemu.exec.QEmuCommandLine;
import com.lembed.lite.studio.qemu.exec.QEmuOption;

/**
 * A list of {@link QEmuOption QEmuOptions}, represented as a QEmuOption.
 *
 * This makes a {@link QEmuCommandLine} be a tree of QEmuOptions.
 *
 * @author shevek
 */
public class QEmuOptionsList extends ArrayList<QEmuOption> implements QEmuOption, QEmuOption.Container, QEmuIdAllocator.Consumer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7754676483372988585L;

	public QEmuOptionsList() {
    }

    public QEmuOptionsList(Collection<? extends QEmuOption> c) {
        super(c);
    }

    public QEmuOptionsList(QEmuOption... c) {
        this(Arrays.asList(c));
    }

    @Override
    public void withAllocator(QEmuIdAllocator allocator) {
        for (QEmuOption option : this) {
            if (option instanceof QEmuIdAllocator.Consumer) {
                ((QEmuIdAllocator.Consumer) option).withAllocator(allocator);
            }
        }
    }

    @Override
    public void appendTo(List<? super String> line) {
        for (QEmuOption option : this) {
            if (option != null) {
                option.appendTo(line);
            }
        }
    }
}
