package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import com.lembed.lite.studio.qemu.model.swt.USBBrailleModel;
import com.lembed.lite.studio.qemu.model.swt.USBDiskModel;
import com.lembed.lite.studio.qemu.model.swt.USBDriverModel;
import com.lembed.lite.studio.qemu.model.swt.USBKeyboardModel;
import com.lembed.lite.studio.qemu.model.swt.USBMouseModel;
import com.lembed.lite.studio.qemu.model.swt.USBNetModel;
import com.lembed.lite.studio.qemu.model.swt.USBSerialModel;
import com.lembed.lite.studio.qemu.model.swt.USBTabletModel;
import com.lembed.lite.studio.qemu.model.swt.USBWacomTabletModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;
import com.lembed.lite.studio.qemu.view.internal.swt.USBView;

public class USBControl implements BaseControl {

	private USBView usbView;

	private USBDriverModel mydrivermodel;
	private USBMouseModel mymousemodel;
	private USBTabletModel mytabletmodel;
	private USBWacomTabletModel mywacomtabletmodel;
	private USBKeyboardModel mykeyboardmodel;
	private USBBrailleModel mybraillemodel;
	private USBDiskModel mydiskmodel;
	private USBSerialModel myserialmodel;
	private USBNetModel mynetmodel;

	public USBControl(EmulationControl myemulation, EmulatorQemuMachineControl emc) {
		this.usbView = new USBView(emc);
		this.usbView.configureListener(this);
		this.usbView.configureStandardMode();
		this.mydrivermodel = new USBDriverModel(myemulation, emc);
		this.mymousemodel = new USBMouseModel(myemulation, emc);
		this.mytabletmodel = new USBTabletModel(myemulation, emc);
		this.mywacomtabletmodel = new USBWacomTabletModel(myemulation, emc);
		this.mykeyboardmodel = new USBKeyboardModel(myemulation, emc);
		this.mybraillemodel = new USBBrailleModel(myemulation, emc);
		this.mydiskmodel = new USBDiskModel(myemulation, emc);
		this.myserialmodel = new USBSerialModel(myemulation, emc);
		this.mynetmodel = new USBNetModel(myemulation, emc);
	}

	public void setVisible(boolean value) {
		this.usbView.setVisible(value);
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {
		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(usbView);
		
		return list;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("eraseButton")) {

			if (this.usbView.getUsb().isSelected()) {
				this.mydrivermodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDRIVEROPTION.getValor()]);
				this.usbView.getUsb().setSelected(false);
			}

			if (this.usbView.getMouse().isSelected()) {
				this.mymousemodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBMOUSEOPTION.getValor()],
						OptionsEnumModel.USBMOUSEOPTION.getValor());
				this.usbView.getMouse().setSelected(false);
			}

			if (this.usbView.getTablet().isSelected()) {
				this.mytabletmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBTABLETOPTION.getValor()]);
				this.usbView.getTablet().setSelected(false);
			}

			if (this.usbView.getWacomTablet().isSelected()) {
				this.mywacomtabletmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBWACOMTABLETOPTION.getValor()]);
				this.usbView.getWacomTablet().setSelected(false);
			}

			if (this.usbView.getKeyboard().isSelected()) {
				this.mykeyboardmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBKEYBOARDOPTION.getValor()]);
				this.usbView.getKeyboard().setSelected(false);
			}

			if (this.usbView.getBraille().isSelected()) {
				this.mybraillemodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBBRAILLEOPTION.getValor()]);
				this.usbView.getBraille().setSelected(false);
			}

			if (this.usbView.getDisk().isSelected()) {
				this.mydiskmodel
						.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDISKOPTION.getValor()]);
				this.usbView.getDisk().setSelected(false);
				this.usbView.getFile().setText("");
			}

			if (this.usbView.getSerial().isSelected()) {
				this.myserialmodel.setVendorid("");
				this.myserialmodel.setProductid("");
				this.myserialmodel.setDev("");
				this.myserialmodel.setOption();
				this.myserialmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBSERIALOPTION.getValor()]);
				this.usbView.getSerial().setSelected(false);
				this.usbView.getVendorid().setText("");
				this.usbView.getProductid().setText("");
				this.usbView.getDev().setText("");
			}

			if (this.usbView.getNet().isSelected()) {
				this.mynetmodel.setVlan("");
				this.mynetmodel.setMacaddr("");
				this.mynetmodel.setName("");
				this.mynetmodel.setAddr("");
				this.mynetmodel.setVectors("");
				this.mynetmodel.setOption();
				this.mynetmodel
						.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBNETOPTION.getValor()]);
				this.usbView.getNet().setSelected(false);
				this.usbView.getVlan().setSelectedIndex(0);
				this.usbView.getMacaddr().setText("");
				this.usbView.setName("");
				this.usbView.getAddr().setText("");
				this.usbView.getVectorsNumber().setSelectedIndex(0);
			}

			this.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {

			if (this.usbView.getUsb().isSelected()) {
				this.mydrivermodel
						.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDRIVEROPTION.getValor()]);
			} else {
				this.mydrivermodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDRIVEROPTION.getValor()]);
			}

			if (this.usbView.getMouse().isSelected()) {
				this.mymousemodel
						.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBMOUSEOPTION.getValor()]);
			} else {
				this.mymousemodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBMOUSEOPTION.getValor()],
						OptionsEnumModel.USBMOUSEOPTION.getValor());
			}

			if (this.usbView.getTablet().isSelected()) {
				this.mytabletmodel
						.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBTABLETOPTION.getValor()]);
			} else {
				this.mytabletmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBTABLETOPTION.getValor()],
						OptionsEnumModel.USBTABLETOPTION.getValor());
			}

			if (this.usbView.getWacomTablet().isSelected()) {
				this.mywacomtabletmodel.setOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBWACOMTABLETOPTION.getValor()]);
			} else {
				this.mywacomtabletmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBWACOMTABLETOPTION.getValor()]);
			}

			if (this.usbView.getKeyboard().isSelected()) {
				this.mykeyboardmodel.setOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBKEYBOARDOPTION.getValor()]);
			} else {
				this.mykeyboardmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBKEYBOARDOPTION.getValor()]);
			}

			if (this.usbView.getBraille().isSelected()) {
				this.mybraillemodel
						.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBBRAILLEOPTION.getValor()]);
			} else {
				this.mybraillemodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBBRAILLEOPTION.getValor()]);
			}

			if (this.usbView.getDisk().isSelected()) {
				if (!this.usbView.getFile().getText().isEmpty()) {
					this.mydiskmodel.setFile(this.usbView.getFile().getText());
					this.mydiskmodel.setOption(
							VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDISKOPTION.getValor()]);
				} else {
					this.usbView.getFile().setText(this.mydiskmodel.getFile());
				}
			} else {
				this.mydiskmodel
						.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBDISKOPTION.getValor()]);
				this.usbView.getFile().setText("");
			}

			if (this.usbView.getSerial().isSelected()) {
				this.myserialmodel.setDev(this.usbView.getDev().getText());
				this.myserialmodel.setVendorid(this.usbView.getVendorid().getText());
				this.myserialmodel.setProductid(this.usbView.getProductid().getText());
				this.myserialmodel.setOption();
				this.myserialmodel
						.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBSERIALOPTION.getValor()]);
			} else {
				this.myserialmodel.setVendorid("");
				this.myserialmodel.setProductid("");
				this.myserialmodel.setDev("");
				this.myserialmodel.setOption();
				this.myserialmodel.unsetOption(
						VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBSERIALOPTION.getValor()]);
				this.usbView.getVendorid().setText("");
				this.usbView.getProductid().setText("");
				this.usbView.getDev().setText("");
			}

			if (this.usbView.getNet().isSelected()) {
				this.mynetmodel.setVlan((String) this.usbView.getVlan().getSelectedItem());
				this.mynetmodel.setMacaddr(this.usbView.getMacaddr().getText());
				this.mynetmodel.setName(this.usbView.getName());
				this.mynetmodel.setAddr(this.usbView.getAddr().getText());
				this.mynetmodel.setVectors((String) this.usbView.getVectorsNumber().getSelectedItem());
				this.mynetmodel.setOption();
				this.mynetmodel
						.setOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBNETOPTION.getValor()]);
			} else {
				this.mynetmodel.setVlan("");
				this.mynetmodel.setMacaddr("");
				this.mynetmodel.setName("");
				this.mynetmodel.setAddr("");
				this.mynetmodel.setVectors("");
				this.mynetmodel.setOption();
				this.mynetmodel
						.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.USBNETOPTION.getValor()]);
				this.usbView.getNet().setSelected(false);
				this.usbView.getVlan().setSelectedIndex(0);
				this.usbView.getMacaddr().setText("");
				this.usbView.setName("");
				this.usbView.getAddr().setText("");
				this.usbView.getVectorsNumber().setSelectedIndex(0);
			}

			this.setVisible(false);
		} else if (e.getActionCommand().equals("fileChooser")) {
			this.usbView.setChoosertitle("Choose the file  that is the basis for the USB Mass storage device!");
			if (this.usbView.chooseFiles()) {
				this.usbView.getFile().setText(this.usbView.getChoice());
			}
		}
	}

}
