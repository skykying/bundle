package com.lembed.lite.studio.qemu.view.internal.swt;

import com.lembed.lite.studio.qemu.view.IemultorStore;

public interface IDeviceView {

	void setTitle(String string);

	String getTitle();

	void doSave(IemultorStore store);

}
