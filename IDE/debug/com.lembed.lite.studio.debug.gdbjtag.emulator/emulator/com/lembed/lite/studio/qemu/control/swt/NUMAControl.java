package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.NUMAModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.NUMAView;

public class NUMAControl implements BaseControl {

	private NUMAView myview;
	private NUMAModel mymodel;

	public NUMAControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
		this.myview = new NUMAView(myfile);
		this.mymodel = new NUMAModel(myemulation, myfile);
	}

	public void starts() {
		this.myview.configureStandardMode();
		this.myview.configureListener(this);
	}

	public void setVisible(Boolean value) {
		this.myview.setVisible(value);
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(myview);
		
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("eraseButton")) {
			for (int i = 0; i < NUMAView.itemNumbers; i++) {
				this.myview.getEnabledOptions()[i].setSelected(false);
				this.myview.getEditor(i).getTextField().setText("");
				this.myview.getCpuLeftNumbers()[i].setSelectedIndex(0);
				this.myview.getCpuRightNumbers()[i].setSelectedIndex(0);
				this.mymodel.buildIt(this.myview.getEditor(i).getTextField().getText(),
						(String) this.myview.getCpuLeftNumbers()[i].getSelectedItem(),
						(String) this.myview.getCpuRightNumbers()[i].getSelectedItem(), i);
			}
			this.myview.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			for (int i = 0; i < NUMAView.itemNumbers; i++) {
				if (this.myview.getEnabledOptions()[i].isSelected()) {
					this.mymodel.buildIt(this.myview.getEditor(i).getTextField().getText(),
							(String) this.myview.getCpuLeftNumbers()[i].getSelectedItem(),
							(String) this.myview.getCpuRightNumbers()[i].getSelectedItem(), i);
				} else {
					this.myview.getEditor(i).getTextField().setText("");
					this.myview.getCpuLeftNumbers()[i].setSelectedIndex(0);
					this.myview.getCpuRightNumbers()[i].setSelectedIndex(0);
					this.mymodel.buildIt(this.myview.getEditor(i).getTextField().getText(),
							(String) this.myview.getCpuLeftNumbers()[i].getSelectedItem(),
							(String) this.myview.getCpuRightNumbers()[i].getSelectedItem(), i);
				}
			}
			this.myview.setVisible(false);
		}
	}

}
