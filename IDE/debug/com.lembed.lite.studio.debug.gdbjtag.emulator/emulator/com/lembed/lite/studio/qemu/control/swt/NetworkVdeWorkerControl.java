package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.NetworkVdeWorkerModel;
import com.lembed.lite.studio.qemu.model.swt.NetworkWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.NetworkVdeWorkerView;

public class NetworkVdeWorkerControl implements BaseControl {

	private NetworkVdeWorkerModel mymodel;
	private NetworkVdeWorkerView myview;

	public NetworkVdeWorkerControl(EmulatorQemuMachineControl myfile, NetworkWorkerModel mymodel, int position) {
		this.mymodel = new NetworkVdeWorkerModel(mymodel);
		this.myview = new NetworkVdeWorkerView(myfile, position);
		this.myview.configureListener(this);
		this.myview.configureStandardMode();
	}

	public void setVisible(boolean value) {
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
			if (this.myview.getIsEnabled().isSelected()) {
				this.myview.getIsEnabled().setSelected(false);
			}
			if (this.myview.getVlan().getSelectedIndex() != 0) {
				this.myview.getVlan().setSelectedIndex(0);
			}
			if (!this.myview.getNameContents().getText().isEmpty()) {
				this.myview.getNameContents().setText("");
			}
			if (!this.myview.getSocketpath().getText().isEmpty()) {
				this.myview.getSocketpath().setText("");
			}
			if (!this.myview.getPort().getText().isEmpty()) {
				this.myview.getPort().setText("");
			}
			if (!this.myview.getGroupname().getText().isEmpty()) {
				this.myview.getGroupname().setText("");
			}
			if (!this.myview.getOctalmode().getText().isEmpty()) {
				this.myview.getOctalmode().setText("");
			}
			this.mymodel.buildIt((String) this.myview.getVlan().getSelectedItem(),
					this.myview.getNameContents().getText(), this.myview.getSocketpath().getText(),
					this.myview.getPort().getText(), this.myview.getGroupname().getText(),
					this.myview.getOctalmode().getText());
			this.myview.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			if (this.myview.getIsEnabled().isSelected()) {
				this.mymodel.buildIt((String) this.myview.getVlan().getSelectedItem(),
						this.myview.getNameContents().getText(), this.myview.getSocketpath().getText(),
						this.myview.getPort().getText(), this.myview.getGroupname().getText(),
						this.myview.getOctalmode().getText());
			} else {
				if (this.myview.getVlan().getSelectedIndex() != 0) {
					this.myview.getVlan().setSelectedIndex(0);
				}
				if (!this.myview.getNameContents().getText().isEmpty()) {
					this.myview.getNameContents().setText("");
				}
				if (!this.myview.getSocketpath().getText().isEmpty()) {
					this.myview.getSocketpath().setText("");
				}
				if (!this.myview.getPort().getText().isEmpty()) {
					this.myview.getPort().setText("");
				}
				if (!this.myview.getGroupname().getText().isEmpty()) {
					this.myview.getGroupname().setText("");
				}
				if (!this.myview.getOctalmode().getText().isEmpty()) {
					this.myview.getOctalmode().setText("");
				}
				this.mymodel.buildIt((String) this.myview.getVlan().getSelectedItem(),
						this.myview.getNameContents().getText(), this.myview.getSocketpath().getText(),
						this.myview.getPort().getText(), this.myview.getGroupname().getText(),
						this.myview.getOctalmode().getText());
			}
			this.myview.setVisible(false);
		} else if (e.getActionCommand().equals("socketpathChooser")) {
			this.myview.setChoosertitle("Choose the socket path!");
			this.myview.setFileDescription("Socket Path");
			if (this.myview.chooseAnyFile()) {
				this.myview.getSocketpath().setText(this.myview.getChoice());
			}
		}
	}

	public void cleanMe() {
		if (this.myview.getIsEnabled().isSelected()) {
			this.myview.getIsEnabled().setSelected(false);
		}
	}

	public boolean isSelected() {
		return this.myview.getIsEnabled().isSelected();
	}
}
