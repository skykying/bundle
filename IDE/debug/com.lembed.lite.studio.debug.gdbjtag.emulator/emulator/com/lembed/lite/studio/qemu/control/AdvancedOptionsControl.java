package com.lembed.lite.studio.qemu.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.AdvancedOptionsModel;
import com.lembed.lite.studio.qemu.model.OptionsEnumModel;
import com.lembed.lite.studio.qemu.model.VMConfigurationModel;
import com.lembed.lite.studio.qemu.view.JContainerView;
import com.lembed.lite.studio.qemu.view.internal.AdvancedOptionsView;

public class AdvancedOptionsControl implements ActionListener {

    private AdvancedOptionsModel mymodel;
    private AdvancedOptionsView myview;
    private JContainerView myMainView;

    public AdvancedOptionsControl(EmulationControl myemulation,
            FileControl myfile, JContainerView myView) {
        myview = new AdvancedOptionsView(myfile);
        myview.configureListener(this);
        myview.configureStandardMode();
        mymodel = new AdvancedOptionsModel(myemulation, myfile);
        myMainView = myView;
    }

    public void change_my_visibility(boolean value) {
        /*
		 http://stackoverflow.com/questions/17305584/textfield-not-updating-dynamically
         */
        if (value) {
            myview.getNameContents().setText(myMainView.getActiveTitle());
            myview.getNameContents().repaint();
        }
        myview.setVisible(value);
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
        myMainView.changeNameJPanel("");
        mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NAMEOPTION.getValor()]);

        mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.SNAPSHOTOPTION.getValor()]);
        myview.getSnapshotOption().setSelected(false);

        mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOFDBOOTCHKOPTION.getValor()]);
        myview.getNoFdBootchkOption().setSelected(false);

        mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.NOHPETOPTION.getValor()]);
        myview.getNoHpetOption().setSelected(false);

        change_my_visibility(false);
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
            myMainView.changeNameJPanel(myview.getNameContents().getText());
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
        change_my_visibility(false);
    }
}
