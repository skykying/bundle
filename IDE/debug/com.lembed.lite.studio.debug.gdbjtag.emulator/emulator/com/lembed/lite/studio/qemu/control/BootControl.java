package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.lembed.lite.studio.qemu.model.BootModel;
import com.lembed.lite.studio.qemu.view.internal.BootView;

public class BootControl implements ActionListener {

    private BootModel bootModel;
    private BootView bootView;
    private FileControl bootFileControl;

    public BootControl(EmulationControl myemulation, FileControl fileControl) {
        this.bootModel = new BootModel(myemulation);
        bootView = new BootView(bootFileControl);
        this.bootFileControl = fileControl;
        if (bootFileControl.getFilemodel().getBootOrder1() != null
                || bootFileControl.getFilemodel().getBootOrder2() != null
                || bootFileControl.getFilemodel().getBootOrder3() != null
                || bootFileControl.getFilemodel().getBootOnce1() != null
                || bootFileControl.getFilemodel().getBootOnce2() != null
                || bootFileControl.getFilemodel().getBootOnce3() != null
                || bootFileControl.getFilemodel().getBootMenu() != null
                || bootFileControl.getFilemodel().getBootSplash() != null
                || bootFileControl.getFilemodel().getBootSplashTime() != null
                || bootFileControl.getFilemodel().getBootRebootTimeout() != null
                || bootFileControl.getFilemodel().getBootStrict() != null) {
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
            bootFileControl.getFilemodel().setBootOrder1("");

            if (bootView.getSecondOrder().getSelectedIndex() != 0) {
                bootView.getSecondOrder().setSelectedIndex(0);
            }
            bootFileControl.getFilemodel().setBootOrder2("");

            if (bootView.getThirdOrder().getSelectedIndex() != 0) {
                bootView.getThirdOrder().setSelectedIndex(0);
            }
            bootFileControl.getFilemodel().setBootOrder3("");

            if (bootView.getFirstOnce().getSelectedIndex() != 0) {
                bootView.getFirstOnce().setSelectedIndex(0);
            }
            bootFileControl.getFilemodel().setBootOnce1("");

            if (bootView.getSecondOnce().getSelectedIndex() != 0) {
                bootView.getSecondOnce().setSelectedIndex(0);
            }
            bootFileControl.getFilemodel().setBootOnce2("");

            if (bootView.getThirdOnce().getSelectedIndex() != 0) {
                bootView.getThirdOnce().setSelectedIndex(0);
            }
            bootFileControl.getFilemodel().setBootOnce3("");

            if (bootView.getMenu().getSelectedIndex() != 0) {
                bootView.getMenu().setSelectedIndex(0);
            }
            bootFileControl.getFilemodel().setBootMenu(
                    (String) bootView.getMenu().getSelectedItem());

            if (!bootView.getSplashName().getText().isEmpty()) {
                bootView.getSplashName().setText("");
            }
            this.bootFileControl.getFilemodel().setBootSplash(
                    bootView.getSplashName().getText());

            if (bootView.getSplashTime().isSelected()) {
                bootView.getSplashTime().setSelected(false);
                bootView.getEditor1().getTextField().setText("0");
            }
            this.bootFileControl.getFilemodel().setBootSplashTime("");

            if (bootView.getRebootTimeout().isSelected()) {
                bootView.getRebootTimeout().setSelected(false);
                bootView.getEditor2().getTextField().setText("-1");
            }
            this.bootFileControl.getFilemodel().setBootRebootTimeout("");

            if (bootView.getStrict().getSelectedIndex() != 0) {
                bootView.getStrict().setSelectedIndex(0);
            }
            this.bootFileControl.getFilemodel().setBootStrict(
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

            if (!((String) bootView.getFirstOrder().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getFilemodel()
                        .setBootOrder1(
                                ((String) bootView.getFirstOrder()
                                .getSelectedItem()).substring(0, 1));
            } else {
                bootFileControl.getFilemodel().setBootOrder1("");
            }

            if (!((String) bootView.getSecondOrder().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getFilemodel().setBootOrder2(
                        ((String) bootView.getSecondOrder()
                        .getSelectedItem()).substring(0, 1));
            } else {
                bootFileControl.getFilemodel().setBootOrder2("");
            }

            if (!((String) bootView.getThirdOrder().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getFilemodel()
                        .setBootOrder3(
                                ((String) bootView.getThirdOrder()
                                .getSelectedItem()).substring(0, 1));
            } else {
                bootFileControl.getFilemodel().setBootOrder3("");
            }

            if (!((String) bootView.getFirstOnce().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getFilemodel().setBootOnce1(
                        ((String) bootView.getFirstOnce().getSelectedItem())
                        .substring(0, 1));
            } else {
                bootFileControl.getFilemodel().setBootOnce1("");
            }

            if (!((String) bootView.getSecondOnce().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getFilemodel()
                        .setBootOnce2(
                                ((String) bootView.getSecondOnce()
                                .getSelectedItem()).substring(0, 1));
            } else {
                bootFileControl.getFilemodel().setBootOnce2("");
            }

            if (!((String) bootView.getThirdOnce().getSelectedItem())
                    .isEmpty()) {
                bootFileControl.getFilemodel().setBootOnce3(
                        ((String) bootView.getThirdOnce().getSelectedItem())
                        .substring(0, 1));
            } else {
                bootFileControl.getFilemodel().setBootOnce3("");
            }

            bootFileControl.getFilemodel().setBootMenu(
                    (String) bootView.getMenu().getSelectedItem());

            this.bootFileControl.getFilemodel().setBootSplash(
                    bootView.getSplashName().getText());

            this.bootFileControl.getFilemodel().setBootSplashTime(
                    this.buildString(bootView.getSplashTime(), bootView
                            .getEditor1().getTextField().getText()));

            this.bootFileControl.getFilemodel().setBootRebootTimeout(
                    this.buildString(bootView.getRebootTimeout(),
                            bootView.getEditor2().getTextField().getText()));

            this.bootFileControl.getFilemodel().setBootStrict(
                    (String) bootView.getStrict().getSelectedItem());

            bootView.setVisible(false);
        } else if (e.getActionCommand().equals("splashButton")) {
            if (bootView.chooseSplashPicture()) {
                bootView.getSplashName().setText(bootView.getChoice());
                this.bootFileControl.getFilemodel().setBootSplash(
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
