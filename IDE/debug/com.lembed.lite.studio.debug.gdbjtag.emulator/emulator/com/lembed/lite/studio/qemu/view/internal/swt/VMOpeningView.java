package com.lembed.lite.studio.qemu.view.internal.swt;

import com.lembed.lite.studio.qemu.view.IemultorStore;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;

public class VMOpeningView extends DeviceBaseView {

	private JSwtQemuView parent;

	private SwtPanelCreationView view;

	private String chosenMachineName;

	public VMOpeningView(JSwtQemuView parent, String chosenMachineName) {
		this.view = null;
		this.parent = parent;
		this.chosenMachineName = chosenMachineName;
	}

	public boolean starts(String diskImagePath, String secondDiskImagePath, String thirdDiskImagePath,
			String fourthDiskImagePath, String ramSize) {
		// TODO 2020.11.7
		// this.view = (SwtPanelCreationView)
		// parent.makeVMPanel(chosenMachineName);
		// if (ramSize != null) {
		// if (!ramSize.isEmpty()) {
		// view.setRamSize(ramSize);
		// }
		// } else {
		// this.view.setRamSize("128");
		// }
		// this.parent.addCreationNewJPanel(view, chosenMachineName);
		return true;
	}

	public void setView(JSwtQemuView parent) {
		this.parent = parent;
	}

	public void setChosenMachineName(String chosenMachineName) {
		this.chosenMachineName = chosenMachineName;
	}

	@Override
	public void applyView(IemultorStore store) {
		// TODO Auto-generated method stub

	}
}
