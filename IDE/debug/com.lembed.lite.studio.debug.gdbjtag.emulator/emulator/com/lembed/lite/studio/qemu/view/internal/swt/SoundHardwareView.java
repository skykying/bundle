package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class SoundHardwareView extends DeviceBaseView {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JLabel soundHardwareLabel;

    private JComboBox<String> soundHardware;

    private JButton eraseButton;

    private JButton okButton;

    // Association "option" - "Description".
    private HashMap<String, String> falseOptions;


    public SoundHardwareView(EmulatorQemuMachineControl emc) {
        super(emc);

        windowContent = new JPanel();

        gridLayout = new GridLayout(2, 2);

        windowContent.setLayout(gridLayout);

        soundHardwareLabel = new JLabel("Choose the sound hardware:");

        windowContent.add(soundHardwareLabel);

        String[] soundHardwareOptions = {"", "Creative Sound Blaster 16",
            "PC speaker", "Intel HD Audio", "Gravis Ultrasound GF1",
            "ENSONIQ AudioPCI ES1370", "CS4231A", "Yamaha YM3812 (OPL2)",
            "Intel 82801AA AC97 Audio", "All of the above"};

        this.soundHardware = new JComboBox<String>(soundHardwareOptions);

        windowContent.add(soundHardware);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

        this.add(windowContent);

        this.setTitle("JavaQemu - Sound Hardware Choice");

    }

    private void rechecks() {
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
    }

    public JComboBox<String> getSoundHardware() {
        return soundHardware;
    }
    

	@Override
	public void applyView(IemultorStore store) {
        this.falseOptions = new HashMap<String, String>();
        falseOptions.put("", "");
        falseOptions.put("sb16", "Creative Sound Blaster 16");
        falseOptions.put("pcspk", "PC speaker");
        falseOptions.put("hda", "Intel HD Audio");
        falseOptions.put("gus", "Gravis Ultrasound GF1");
        falseOptions.put("es1370", "ENSONIQ AudioPCI ES1370");
        falseOptions.put("cs4231a", "CS4231A");
        falseOptions.put("adlib", "Yamaha YM3812 (OPL2)");
        falseOptions.put("ac97", "Intel 82801AA AC97 Audio");
        falseOptions.put("all", "All of the above");

        if (eQControl.getMachineModel().getSoundHardwareOption() != null) {
            getSoundHardware().setSelectedItem(
                    falseOptions.get(eQControl.getMachineModel()
                            .getSoundHardwareOption()));
        }

        this.rechecks();
		
	}
}
