package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.AdvancedOptionsModel;
import com.lembed.lite.studio.qemu.model.OptionsEnumModel;
import com.lembed.lite.studio.qemu.model.VMConfigurationModel;
import com.lembed.lite.studio.qemu.view.JSwtQemuView;
import com.lembed.lite.studio.qemu.view.internal.swt.AdvancedOptionsView;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;

public class AdvancedOptionsControl implements BaseControl {

	private AdvancedOptionsModel mymodel;
	private AdvancedOptionsView myview;
//	private JSwtQemuView myMainView;

	public AdvancedOptionsControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
		myview = new AdvancedOptionsView(myfile);
		myview.configureListener(this);
		myview.configureStandardMode();
		mymodel = new AdvancedOptionsModel(myemulation, myfile);
//		myMainView = myView;
	}

	public void setVisible(boolean value) {
		/*
		 * http://stackoverflow.com/questions/17305584/textfield-not-updating-
		 * dynamically
		 */
//		if (value) {
//			myview.getNameContents().setText(myMainView.getActiveTitle());
//			myview.getNameContents().repaint();
//		}
//		myview.setVisible(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("eraseButton")) {
			handleErase();
		} else if (e.getActionCommand().equals("okButton")) {

			handleOK();
		}
	}

	private void handleErase() {
		mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()]);
		myview.getWin2kHackOption().setSelected(false);

		mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()]);
		myview.getNoAcpiOption().setSelected(false);

		myview.getNameContents().setText("");
		myview.getNameContents().repaint();
//		myMainView.changeNameJPanel("");
		mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);

		mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()]);
		myview.getSnapshotOption().setSelected(false);

		mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()]);
		myview.getNoFdBootchkOption().setSelected(false);

		mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()]);
		myview.getNoHpetOption().setSelected(false);

		setVisible(false);
	}

	private void handleOK() {
		if (myview.getWin2kHackOption().isSelected()) {
			mymodel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()]);
		} else {
			mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.WIN2KHACKOPTION.getValor()]);
		}

		if (myview.getNoAcpiOption().isSelected()) {
			mymodel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()]);
		} else {
			mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOACPIOPTION.getValor()]);
		}

		if (!myview.getNameContents().getText().isEmpty()) {
			mymodel.setName(myview.getNameContents().getText());
//			myMainView.changeNameJPanel(myview.getNameContents().getText());
			mymodel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
		} else {
			mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);
		}

		if (myview.getSnapshotOption().isSelected()) {
			mymodel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()]);
		} else {
			mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()]);
		}

		if (myview.getNoFdBootchkOption().isSelected()) {
			mymodel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()]);
		} else {
			mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()]);
		}

		if (myview.getNoHpetOption().isSelected()) {
			mymodel.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()]);
		} else {
			mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()]);
		}
		setVisible(false);
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(myview);
		
		return list;
	}

}
