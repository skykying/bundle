package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.lembed.lite.studio.qemu.model.swt.NetworkDnssearchUserWorkerModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.NetworkDnssearchUserWorkerView;

public class NetworkDnssearchUserWorkerControl implements BaseControl {

	private NetworkDnssearchUserWorkerView myview;
	private NetworkDnssearchUserWorkerModel mymodel;
	private List<String> myResults;

	public NetworkDnssearchUserWorkerControl(EmulatorQemuMachineControl myfile, int position) {
		mymodel = new NetworkDnssearchUserWorkerModel();
		myview = new NetworkDnssearchUserWorkerView(myfile, position);
		myview.configureListener(this);
		myview.configureStandardMode();
		myResults = new ArrayList<String>();
	}

	public void change_my_visibility(boolean value) {
		myview.setVisible(value);
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
			myview.getDnssearch().setText("");
			myview.getText().setText("");
			myview.getOption().setText("");
			myResults.clear();
			myview.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			myResults = mymodel.buildIt(myview.getDnssearch().getText());
			myview.setVisible(false);
		} else if (e.getActionCommand().equals("add")) {
			if (!myview.getText().getText().isEmpty()) {
				myview.buildMe(myview.getText().getText());
			}
		} else if (e.getActionCommand().equals("remove")) {
			myview.removeMe(myview.getOption().getText());
		}
	}

	public List<String> getMyResults() {
		return myResults;
	}

}
