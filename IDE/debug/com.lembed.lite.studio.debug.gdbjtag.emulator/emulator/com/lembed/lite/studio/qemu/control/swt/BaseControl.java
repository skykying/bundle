package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionListener;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;

public interface BaseControl extends ActionListener {

	public LinkedList<DeviceBaseView> getViews();

}
