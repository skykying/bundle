package com.lembed.lite.studio.qemu.control.swt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lembed.lite.studio.qemu.model.swt.MemoryModel;
import com.lembed.lite.studio.qemu.model.swt.VMConfigurationModel;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.swt.MemoryView;

public class MemoryControl implements BaseControl {

    private MemoryView myview;
    private MemoryModel mymodel;

    public MemoryControl(EmulationControl myemulation, EmulatorQemuMachineControl myfile) {
        myview = new MemoryView(myfile);
        this.mymodel = new MemoryModel(myemulation, myfile);
    }

    public void starts() {
        this.myview.configureStandardMode();
        this.myview.configureListener(this);
    }

    public void change_the_visibility_of_view(Boolean value) {
        this.myview.setVisible(value);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("eraseButton")) {
            this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMORYPATHOPTION.getValor()]);
            this.myview.getMemoryPath().setText("");
            this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMPREALLOC.getValor()]);
            this.myview.getMemPrealloc().setSelected(false);
            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("okButton")) {
            this.mymodel.buildIt(this.myview.getMemoryPath().getText());
            if (this.myview.getMemPrealloc().isSelected()) {
                this.mymodel.buildIt();
            } else {
                this.mymodel.unsetOption(VMConfigurationModel.getTagsOptions()[OptionsEnumModel.MEMPREALLOC.getValor()]);
            }
            this.myview.setVisible(false);
        } else if (e.getActionCommand().equals("memoryPathChooser")) {
            this.myview
                    .setChoosertitle("Choose a directory as the memory path!");
            this.myview.setFileDescription("Directory - Memory Path");
            if (this.myview.chooseDirectoryForDefaultVMPath()) {
                this.myview.getMemoryPath().setText(this.myview.getChoice());
            }
        }
    }

}
