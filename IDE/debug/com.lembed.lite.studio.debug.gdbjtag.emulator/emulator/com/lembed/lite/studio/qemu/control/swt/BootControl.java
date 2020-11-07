package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.lembed.lite.studio.qemu.model.swt.BootModel;
import com.lembed.lite.studio.qemu.view.internal.swt.BootView;

public class BootControl implements ActionListener {

    private BootModel bootModel;
    private BootView bootView;
    private EmulatorQemuMachineControl bootFileControl;

    public BootControl(EmulationControl myemulation, EmulatorQemuMachineControl fileControl) {
        this.bootModel = new BootModel(myemulation);
        bootView = new BootView(bootFileControl);
        this.bootFileControl = fileControl;
        if (bootFileControl.getMachineModel().getBootOrder1() != null
                || bootFileControl.getMachineModel().getBootOrder2() != null
                || bootFileControl.getMachineModel().getBootOrder3() != null
                || bootFileControl.getMachineModel().getBootOnce1() != null
                || bootFileControl.getMachineModel().getBootOnce2() != null
                || bootFileControl.getMachineModel().getBootOnce3() != null
                || bootFileControl.getMachineModel().getBootMenu() != null
                || bootFileControl.getMachineModel().getBootSplash() != null
                || bootFileControl.getMachineModel().getBootSplashTime() != null
                || bootFileControl.getMachineModel().getBootRebootTimeout() != null
                || bootFileControl.getMachineModel().getBootStrict() != null) {
            this.bootModel.buildIt(bootModel.buildOrderOrOnce(
                    (String) bootView.getFirstOrder().getSelectedItem(),
                    (String) bootView.getSecondOrder().getSelectedItem(),
                    (String) bootView.getThirdOrder().getSelectedItem()),
                    bootModel.buildOrderOrOnce((String) bootView
                            .getFirstOnce().getSelectedItem(),
                            (String) bootView.getSecondOnce()
                            .getSelectedItem(), (String) bootView
                            .getThirdOnce().getSelectedItem()),
                    (String) bootView.getMenu().getSelectedItem(),
                    (String) bootView.getSplashName().getText(), this
                    .buildString(bootView.getSplashTime(),
                            bootView.getEditor1().getTextField()
                            .getText().replace(".", "")
                            .replace(",", ".")), 
                    buildString(bootView.getRebootTimeout(),
                            bootView.getEditor2().getTextField()
                            .getText().replace(".", "")
                            .replace(",", ".")),
                    (String) bootView.getStrict().getSelectedItem());
        }
    }

    public void starts() {
        bootView.configureListener(this);
        bootView.configureStandardMode();
    }

    public void change_my_visibility(Boolean value) {
        bootView.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            if (bootView.getFirstOrder().getSelectedIndex() != 0) {
                bootView.getFirstOrder().setSelectedIndex(0);
            }
            bootFileControl.getMachineModel().setBootOrder1("");

            if (bootView.getSecondOrder().getSelectedIndex() != 0) {
                bootView.getSecondOrder().setSelectedIndex(0);
            }
            bootFileControl.getMachineModel().setBootOrder2("");

            if (bootView.getThirdOrder().getSelectedIndex() != 0) {
                bootView.getThirdOrder().setSelectedIndex(0);
            }
            bootFileControl.getMachineModel().setBootOrder3("");

            if (bootView.getFirstOnce().getSelectedIndex() != 0) {
                bootView.getFirstOnce().setSelectedIndex(0);
            }
            bootFileControl.getMachineModel().setBootOnce1("");

            if (bootView.getSecondOnce().getSelectedIndex() != 0) {
                bootView.getSecondOnce().setSelectedIndex(0);
            }
            bootFileControl.getMachineModel().setBootOnce2("");

            if (bootView.getThirdOnce().getSelectedIndex() != 0) {
                bootView.getThirdOnce().setSelectedIndex(0);
            }
            bootFileControl.getMachineModel().setBootOnce3("");

            if (bootView.getMenu().getSelectedIndex() != 0) {
                bootView.getMenu().setSelectedIndex(0);
            }
            bootFileControl.getMachineModel().setBootMenu(
                    (String) bootView.getMenu().getSelectedItem());

            if (!bootView.getSplashName().getText().isEmpty()) {
                bootView.getSplashName().setText("");
            }
            this.bootFileControl.getMachineModel().setBootSplash(
                    bootView.getSplashName().getText());

            if (bootView.getSplashTime().isSelected()) {
                bootView.getSplashTime().setSelected(false);
                bootView.getEditor1().getTextField().setText("0");
            }
            this.bootFileControl.getMachineModel().setBootSplashTime("");

            if (bootView.getRebootTimeout().isSelected()) {
                bootView.getRebootTimeout().setSelected(false);
                bootView.getEditor2().getTextField().setText("-1");
            }
            this.bootFileControl.getMachineModel().setBootRebootTimeout("");

            if (bootView.getStrict().getSelectedIndex() != 0) {
                bootView.getStrict().setSelectedIndex(0);
            }
            this.bootFileControl.getMachineModel().setBootStrict(
                    (String) bootView.getStrict().getSelectedItem());

            this.bootModel.buildIt(this.bootModel.buildOrderOrOnce(
                    (String) bootView.getFirstOrder().getSelectedItem(),
                    (String) bootView.getSecondOrder().getSelectedItem(),
                    (String) bootView.getThirdOrder().getSelectedItem()),
                    this.bootModel.buildOrderOrOnce((String) bootView
                            .getFirstOnce().getSelectedItem(),
                            (String) bootView.getSecondOnce()
                            .getSelectedItem(), (String) bootView
                            .getThirdOnce().getSelectedItem()),
                    (String) bootView.getMenu().getSelectedItem(),
                    (String) bootView.getSplashName().getText(), this
                    .buildString(bootView.getSplashTime(),
                            bootView.getEditor1().getTextField()
                            .getText().replace(".", "")
                            .replace(",", ".")), this
                    .buildString(bootView.getRebootTimeout(),
                            bootView.getEditor2().getTextField()
                            .getText().replace(".", "")
                            .replace(",", ".")),
                    (String) bootView.getStrict().getSelectedItem());
            bootView.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            this.bootModel.buildIt(this.bootModel.buildOrderOrOnce(
                    (String) bootView.getFirstOrder().getSelectedItem(),
                    (String) bootView.getSecondOrder().getSelectedItem(),
                    (String) bootView.getThirdOrder().getSelectedItem()),
                    this.bootModel.buildOrderOrOnce((String) bootView
                            .getFirstOnce().getSelectedItem(),
                            (String) bootView.getSecondOnce()
                            .getSelectedItem(), (String) bootView
                            .getThirdOnce().getSelectedItem()),
                    (String) bootView.getMenu().getSelectedItem(),
                    (String) bootView.getSplashName().getText(), 
                    buildString(bootView.getSplashTime(),
                            bootView.getEditor1().getTextField()
                            .getText().replace(".", "")
                            .replace(",", ".")), 
                    buildString(bootView.getRebootTimeout(),
                            bootView.getEditor2().getTextField()
                            .getText().replace(".", "")
                            .replace(",", ".")),
                    (String) bootView.getStrict().getSelectedItem());

            if (!((String) bootView.getFirstOrder().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getMachineModel()
                        .setBootOrder1(
                                ((String) bootView.getFirstOrder()
                                .getSelectedItem()).substring(0, 1));
            } else {
                bootFileControl.getMachineModel().setBootOrder1("");
            }

            
           String item = (String) bootView.getSecondOrder().getSelectedItem();
            if (!item.isEmpty()) {
            	
            	String oitem =  (String) bootView.getSecondOrder().getSelectedItem();
            	oitem = oitem.substring(0, 1);
            	
                bootFileControl.getMachineModel().setBootOrder2(oitem);
            } else {
                bootFileControl.getMachineModel().setBootOrder2("");
            }

            if (!((String) bootView.getThirdOrder().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getMachineModel()
                        .setBootOrder3(
                                ((String) bootView.getThirdOrder()
                                .getSelectedItem()).substring(0, 1));
            } else {
                bootFileControl.getMachineModel().setBootOrder3("");
            }

            if (!((String) bootView.getFirstOnce().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getMachineModel().setBootOnce1(
                        ((String) bootView.getFirstOnce().getSelectedItem())
                        .substring(0, 1));
            } else {
                bootFileControl.getMachineModel().setBootOnce1("");
            }

            if (!((String) bootView.getSecondOnce().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getMachineModel()
                        .setBootOnce2(
                                ((String) bootView.getSecondOnce()
                                .getSelectedItem()).substring(0, 1));
            } else {
                bootFileControl.getMachineModel().setBootOnce2("");
            }

            if (!((String) bootView.getThirdOnce().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getMachineModel().setBootOnce3(
                        ((String) bootView.getThirdOnce().getSelectedItem())
                        .substring(0, 1));
            } else {
                bootFileControl.getMachineModel().setBootOnce3("");
            }

            bootFileControl.getMachineModel().setBootMenu(
                    (String) bootView.getMenu().getSelectedItem());

           bootFileControl.getMachineModel().setBootSplash(
                    bootView.getSplashName().getText());

            bootFileControl.getMachineModel().setBootSplashTime(
                    buildString(bootView.getSplashTime(), bootView
                            .getEditor1().getTextField().getText()));

            bootFileControl.getMachineModel().setBootRebootTimeout(
                    buildString(bootView.getRebootTimeout(),
                            bootView.getEditor2().getTextField().getText()));

            bootFileControl.getMachineModel().setBootStrict(
                    (String) bootView.getStrict().getSelectedItem());

            bootView.setVisible(false);
        } else if (e.getActionCommand().equals("splashButton")) {
            if (bootView.chooseSplashPicture()) {
                bootView.getSplashName().setText(bootView.getChoice());
                this.bootFileControl.getMachineModel().setBootSplash(
                        bootView.getSplashName().getText());
            }
        } else if (e.getActionCommand().equals("firstOrder")
                || e.getActionCommand().equals("secondOrder")
                || e.getActionCommand().equals("thirdOrder")) {
            bootView.resolveOrderOptions();
        } else if (e.getActionCommand().equals("firstOnce")
                || e.getActionCommand().equals("secondOnce")
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

}
