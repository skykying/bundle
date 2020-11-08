package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JCheckBox;

import com.lembed.lite.studio.qemu.model.swt.BootModel;
import com.lembed.lite.studio.qemu.model.swt.EmulatorQemuMachineModel;
import com.lembed.lite.studio.qemu.view.internal.swt.BootView;
import com.lembed.lite.studio.qemu.view.internal.swt.DeviceBaseView;
import com.lembed.lite.studio.qemu.view.internal.swt.IDeviceView;

public class BootControl implements BaseControl {

	private BootModel bootModel;
	private BootView bootView;
	private EmulatorQemuMachineControl eQctl;

	public BootControl(EmulationControl ec, EmulatorQemuMachineControl emc) {
		this.bootModel = new BootModel(ec);
		bootView = new BootView(eQctl);
		this.eQctl = emc;

		if (checkValues()) {

			String firstSel = (String) bootView.getFirstOrder().getSelectedItem();
			String secondSel = (String) bootView.getSecondOrder().getSelectedItem();
			String thirdSel = (String) bootView.getThirdOrder().getSelectedItem();
			String border = bootModel.buildOrderOrOnce(firstSel, secondSel, thirdSel);

			String firstOnce = (String) bootView.getFirstOnce().getSelectedItem();
			String secondOnce = (String) bootView.getSecondOnce().getSelectedItem();
			String thirdOnce = (String) bootView.getThirdOnce().getSelectedItem();

			String bonce = bootModel.buildOrderOrOnce(firstOnce, secondOnce, thirdOnce);
			JCheckBox splashTime = bootView.getSplashTime();
			JCheckBox rebooTime = bootView.getRebootTimeout();
			String menuSel = (String) bootView.getMenu().getSelectedItem();

			String splashName = (String) bootView.getSplashName().getText();
			String strictSel = (String) bootView.getStrict().getSelectedItem();
			String se1 = bootView.getEditor1().getTextField().getText().replace(".", "").replace(",", ".");
			String se2 = bootView.getEditor2().getTextField().getText().replace(".", "").replace(",", ".");

			String spTime = buildString(splashTime, se1);
			String reTimeout = buildString(rebooTime, se2);

			bootModel.buildIt(border, bonce, menuSel, splashName, spTime, reTimeout, strictSel);
		}
	}

	private boolean checkValues() {
		EmulatorQemuMachineModel md = eQctl.getMachineModel();

		if (md.getBootOrder1() != null) {
			return true;
		}

		if (md.getBootOrder2() != null) {
			return true;
		}

		if (md.getBootOrder3() != null) {
			return true;
		}

		if (md.getBootOnce1() != null) {
			return true;
		}

		if (md.getBootOnce2() != null) {
			return true;
		}

		if (md.getBootOnce3() != null) {
			return true;
		}

		if (md.getBootMenu() != null) {
			return true;
		}

		if (md.getBootSplash() != null) {
			return true;
		}

		if (md.getBootSplashTime() != null) {
			return true;
		}

		if (md.getBootRebootTimeout() != null) {
			return true;
		}

		if (md.getBootStrict() != null) {
			return true;
		}

		return false;
	}

	public void starts() {
		bootView.configureListener(this);
		bootView.configureStandardMode();
	}

	public void setVisible(Boolean value) {
		bootView.setVisible(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("eraseButton")) {
			if (bootView.getFirstOrder().getSelectedIndex() != 0) {
				bootView.getFirstOrder().setSelectedIndex(0);
			}
			eQctl.getMachineModel().setBootOrder1("");

			if (bootView.getSecondOrder().getSelectedIndex() != 0) {
				bootView.getSecondOrder().setSelectedIndex(0);
			}
			eQctl.getMachineModel().setBootOrder2("");

			if (bootView.getThirdOrder().getSelectedIndex() != 0) {
				bootView.getThirdOrder().setSelectedIndex(0);
			}
			eQctl.getMachineModel().setBootOrder3("");

			if (bootView.getFirstOnce().getSelectedIndex() != 0) {
				bootView.getFirstOnce().setSelectedIndex(0);
			}
			eQctl.getMachineModel().setBootOnce1("");

			if (bootView.getSecondOnce().getSelectedIndex() != 0) {
				bootView.getSecondOnce().setSelectedIndex(0);
			}
			eQctl.getMachineModel().setBootOnce2("");

			if (bootView.getThirdOnce().getSelectedIndex() != 0) {
				bootView.getThirdOnce().setSelectedIndex(0);
			}
			eQctl.getMachineModel().setBootOnce3("");

			if (bootView.getMenu().getSelectedIndex() != 0) {
				bootView.getMenu().setSelectedIndex(0);
			}
			eQctl.getMachineModel().setBootMenu((String) bootView.getMenu().getSelectedItem());

			if (!bootView.getSplashName().getText().isEmpty()) {
				bootView.getSplashName().setText("");
			}
			this.eQctl.getMachineModel().setBootSplash(bootView.getSplashName().getText());

			if (bootView.getSplashTime().isSelected()) {
				bootView.getSplashTime().setSelected(false);
				bootView.getEditor1().getTextField().setText("0");
			}
			this.eQctl.getMachineModel().setBootSplashTime("");

			if (bootView.getRebootTimeout().isSelected()) {
				bootView.getRebootTimeout().setSelected(false);
				bootView.getEditor2().getTextField().setText("-1");
			}
			this.eQctl.getMachineModel().setBootRebootTimeout("");

			if (bootView.getStrict().getSelectedIndex() != 0) {
				bootView.getStrict().setSelectedIndex(0);
			}
			this.eQctl.getMachineModel().setBootStrict((String) bootView.getStrict().getSelectedItem());

			this.bootModel.buildIt(
					this.bootModel.buildOrderOrOnce((String) bootView.getFirstOrder().getSelectedItem(),
							(String) bootView.getSecondOrder().getSelectedItem(),
							(String) bootView.getThirdOrder().getSelectedItem()),
					this.bootModel.buildOrderOrOnce((String) bootView.getFirstOnce().getSelectedItem(),
							(String) bootView.getSecondOnce().getSelectedItem(),
							(String) bootView.getThirdOnce().getSelectedItem()),
					(String) bootView.getMenu().getSelectedItem(), (String) bootView.getSplashName().getText(),
					this.buildString(bootView.getSplashTime(),
							bootView.getEditor1().getTextField().getText().replace(".", "").replace(",", ".")),
					this.buildString(bootView.getRebootTimeout(),
							bootView.getEditor2().getTextField().getText().replace(".", "").replace(",", ".")),
					(String) bootView.getStrict().getSelectedItem());
			bootView.setVisible(false);
		} else if (e.getActionCommand().equals("okButton")) {
			this.bootModel.buildIt(
					this.bootModel.buildOrderOrOnce((String) bootView.getFirstOrder().getSelectedItem(),
							(String) bootView.getSecondOrder().getSelectedItem(),
							(String) bootView.getThirdOrder().getSelectedItem()),
					this.bootModel.buildOrderOrOnce((String) bootView.getFirstOnce().getSelectedItem(),
							(String) bootView.getSecondOnce().getSelectedItem(),
							(String) bootView.getThirdOnce().getSelectedItem()),
					(String) bootView.getMenu().getSelectedItem(), (String) bootView.getSplashName().getText(),
					buildString(bootView.getSplashTime(),
							bootView.getEditor1().getTextField().getText().replace(".", "").replace(",", ".")),
					buildString(bootView.getRebootTimeout(),
							bootView.getEditor2().getTextField().getText().replace(".", "").replace(",", ".")),
					(String) bootView.getStrict().getSelectedItem());

			if (!((String) bootView.getFirstOrder().getSelectedItem()).isEmpty()) {
				eQctl.getMachineModel()
						.setBootOrder1(((String) bootView.getFirstOrder().getSelectedItem()).substring(0, 1));
			} else {
				eQctl.getMachineModel().setBootOrder1("");
			}

			String item = (String) bootView.getSecondOrder().getSelectedItem();
			if (!item.isEmpty()) {

				String oitem = (String) bootView.getSecondOrder().getSelectedItem();
				oitem = oitem.substring(0, 1);

				eQctl.getMachineModel().setBootOrder2(oitem);
			} else {
				eQctl.getMachineModel().setBootOrder2("");
			}

			if (!((String) bootView.getThirdOrder().getSelectedItem()).isEmpty()) {
				eQctl.getMachineModel()
						.setBootOrder3(((String) bootView.getThirdOrder().getSelectedItem()).substring(0, 1));
			} else {
				eQctl.getMachineModel().setBootOrder3("");
			}

			if (!((String) bootView.getFirstOnce().getSelectedItem()).isEmpty()) {
				eQctl.getMachineModel()
						.setBootOnce1(((String) bootView.getFirstOnce().getSelectedItem()).substring(0, 1));
			} else {
				eQctl.getMachineModel().setBootOnce1("");
			}

			if (!((String) bootView.getSecondOnce().getSelectedItem()).isEmpty()) {
				eQctl.getMachineModel()
						.setBootOnce2(((String) bootView.getSecondOnce().getSelectedItem()).substring(0, 1));
			} else {
				eQctl.getMachineModel().setBootOnce2("");
			}

			if (!((String) bootView.getThirdOnce().getSelectedItem()).isEmpty()) {
				eQctl.getMachineModel()
						.setBootOnce3(((String) bootView.getThirdOnce().getSelectedItem()).substring(0, 1));
			} else {
				eQctl.getMachineModel().setBootOnce3("");
			}

			eQctl.getMachineModel().setBootMenu((String) bootView.getMenu().getSelectedItem());

			eQctl.getMachineModel().setBootSplash(bootView.getSplashName().getText());

			eQctl.getMachineModel().setBootSplashTime(
					buildString(bootView.getSplashTime(), bootView.getEditor1().getTextField().getText()));

			eQctl.getMachineModel().setBootRebootTimeout(
					buildString(bootView.getRebootTimeout(), bootView.getEditor2().getTextField().getText()));

			eQctl.getMachineModel().setBootStrict((String) bootView.getStrict().getSelectedItem());

			bootView.setVisible(false);
		} else if (e.getActionCommand().equals("splashButton")) {
			if (bootView.chooseSplashPicture()) {
				bootView.getSplashName().setText(bootView.getChoice());
				this.eQctl.getMachineModel().setBootSplash(bootView.getSplashName().getText());
			}
		} else if (e.getActionCommand().equals("firstOrder") || e.getActionCommand().equals("secondOrder")
				|| e.getActionCommand().equals("thirdOrder")) {
			bootView.resolveOrderOptions();
		} else if (e.getActionCommand().equals("firstOnce") || e.getActionCommand().equals("secondOnce")
				|| e.getActionCommand().equals("thirdOnce")) {
			bootView.resolveOnceOptions();
		}
	}

	public String buildString(JCheckBox mycheckbox, String mystring) {
		if (mycheckbox.isSelected()) {
			return mystring;
		} else {
			return "";
		}
	}

	@Override
	public LinkedList<DeviceBaseView> getViews() {

		LinkedList<DeviceBaseView> list = new LinkedList<>();
		
		list.add(bootView);
		
		return list;
	}

}
