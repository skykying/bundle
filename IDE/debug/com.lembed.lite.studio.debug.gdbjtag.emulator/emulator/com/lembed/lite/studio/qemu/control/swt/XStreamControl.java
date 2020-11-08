package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.XStreamModel;

public class XStreamControl {

	private XStreamModel xStreamModel;
	private static XStreamControl instance = null;

	public synchronized static XStreamControl getInstance() {
		if (instance == null) {
			instance = new XStreamControl();
		}

		return instance;
	}

	public XStreamControl() {
		xStreamModel = new XStreamModel();
	}

	public XStreamModel getXstreamModel() {
		return xStreamModel;
	}
}
