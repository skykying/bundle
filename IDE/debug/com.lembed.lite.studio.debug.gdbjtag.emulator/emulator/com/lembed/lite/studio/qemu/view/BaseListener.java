package com.lembed.lite.studio.qemu.view;

import java.awt.event.ActionListener;

public interface BaseListener extends ActionListener {

    /**
     * Invoked when an action occurs.
     * @param e the event to be processed
     */
    public void actionPerformed(BaseEvent e);

}