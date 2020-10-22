package com.lembed.lite.studio.qemu.view.internal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.FileControl;

public class ImageView extends JFileChooserView {

    private static final long serialVersionUID = 1L;

    private JPanel jpanel;

    private JButton mtdblockChooser;

    private JTextField mtdblock;

    private JButton sdChooser;

    private JTextField sd;

    private JButton pflashChooser;

    private JTextField pflash;

    private JButton okButton, eraseButton;

    private GridLayout gridLayout;

    public ImageView(FileControl myfile) {
        super(null);

        this.jpanel = new JPanel();

        this.gridLayout = new GridLayout(4, 2);

        this.setTitle("JavaQemu - Image Options");

        this.setContentPane(jpanel);

        this.setJpanel(jpanel);

        this.jpanel.setLayout(gridLayout);

        mtdblockChooser = new JButton("Choose the on-board Flash memory image:");

        this.jpanel.add(this.mtdblockChooser);

        this.mtdblock = new JTextField();

        this.jpanel.add(this.mtdblock);

        this.sdChooser = new JButton("Choose the SecureDigital card image:");

        this.jpanel.add(this.sdChooser);

        this.sd = new JTextField();

        this.jpanel.add(this.sd);

        this.pflashChooser = new JButton("Choose the parallel flash image:");

        this.jpanel.add(this.pflashChooser);

        this.pflash = new JTextField();

        this.jpanel.add(this.pflash);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        this.jpanel.add(this.okButton);
        this.jpanel.add(this.eraseButton);

        if (myfile.getFilemodel().getMtdblockOption() != null) {
            this.mtdblock.setText(myfile.getFilemodel().getMtdblockOption());
        }

        if (myfile.getFilemodel().getSdOption() != null) {
            this.sd.setText(myfile.getFilemodel().getSdOption());
        }

        if (myfile.getFilemodel().getPflashOption() != null) {
            this.pflash.setText(myfile.getFilemodel().getPflashOption());
        }

        this.rechecks();
    }

    private void rechecks() {
        this.pack();
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        mtdblockChooser.addActionListener(listener);
        sdChooser.addActionListener(listener);
        pflashChooser.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        mtdblockChooser.setActionCommand("mtdblockChooser");
        sdChooser.setActionCommand("sdChooser");
        pflashChooser.setActionCommand("pflashChooser");
    }

    public JTextField getMtdblock() {
        return mtdblock;
    }

    public JTextField getSd() {
        return sd;
    }

    public JTextField getPflash() {
        return pflash;
    }
}
