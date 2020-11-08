package com.lembed.lite.studio.qemu.control.swt;

import com.lembed.lite.studio.qemu.model.swt.VMClosingModel;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class VMClosingControl {

	private VMClosingModel vmClosingModel;

	public VMClosingControl(JSwtQemuView parent, EmulationControl ec) {
		this.vmClosingModel = new VMClosingModel(parent, ec);
	}

	public boolean starts(Boolean removeAll) {
		return this.vmClosingModel.starts(removeAll);
	}

	public void setView(JSwtQemuView view) {
		this.vmClosingModel.setView(view);
	}

	public void setEmulation(EmulationControl ec) {
		this.vmClosingModel.setEmulationControl(ec);
	}
}
