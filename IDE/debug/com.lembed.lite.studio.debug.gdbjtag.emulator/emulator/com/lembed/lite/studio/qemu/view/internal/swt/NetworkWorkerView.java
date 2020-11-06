package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkWorkerView extends JPanel implements IDeviceView {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JCheckBox isEnabled;

    private JButton nicOptions;

    private JButton userOption;

    private JButton tapOption;

    private JButton bridgeOption;

    private JButton tcpSocketOption;

    private JButton udpSocketOption;

    private JButton vdeOption;

    private JButton hubportOption;

    private JButton dumpOption;

    private JButton disableButton;

    private JButton okButton;

    private JLabel temp[];
    private String title;

	private int position;

	private FileControl myfile;

    public NetworkWorkerView(FileControl myfile, int position) {
        super();
        this.myfile = myfile;
        this.position = position;

        windowContent = new JPanel();

        gridLayout = new GridLayout(7, 2);

        windowContent.setLayout(gridLayout);

        isEnabled = new JCheckBox("Enable these options!");

        windowContent.add(this.isEnabled);

        this.temp = new JLabel[2];
        for (int i = 0; i < this.temp.length; i++) {
            this.temp[i] = new JLabel();
        }

        windowContent.add(this.temp[0]);

        nicOptions = new JButton("Use the nic option!");

        userOption = new JButton("Use the user option!");

        windowContent.add(this.nicOptions);

        windowContent.add(this.userOption);

        tapOption = new JButton("Use the tap option!");

        bridgeOption = new JButton("Use the bridge option!");

        windowContent.add(this.tapOption);

        windowContent.add(this.bridgeOption);

        tcpSocketOption = new JButton("Use the tcp socket option!");

        udpSocketOption = new JButton("Use the udp socket option!");

        windowContent.add(this.tcpSocketOption);

        windowContent.add(this.udpSocketOption);

        vdeOption = new JButton("Use the vde option!");

        windowContent.add(this.vdeOption);

        hubportOption = new JButton("Use the hubport option!");

        windowContent.add(this.hubportOption);

        dumpOption = new JButton("Use the dump option!");

        windowContent.add(this.dumpOption);

        windowContent.add(this.temp[1]);

        okButton = new JButton("OK");

        disableButton = new JButton("Disable");

        windowContent.add(okButton);

        windowContent.add(disableButton);

        setTitle("JavaQemu - Network Option Choice");
        
        add(windowContent);


    }

    private void rechecks() {
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        disableButton.addActionListener(listener);
        okButton.addActionListener(listener);
        nicOptions.addActionListener(listener);
        userOption.addActionListener(listener);
        tapOption.addActionListener(listener);
        bridgeOption.addActionListener(listener);
        tcpSocketOption.addActionListener(listener);
        udpSocketOption.addActionListener(listener);
        vdeOption.addActionListener(listener);
        hubportOption.addActionListener(listener);
        dumpOption.addActionListener(listener);
    }

    public void configureStandardMode() {
        disableButton.setActionCommand("disableButton");
        okButton.setActionCommand("okButton");
        nicOptions.setActionCommand("nicOptions");
        userOption.setActionCommand("userOption");
        tapOption.setActionCommand("tapOption");
        bridgeOption.setActionCommand("bridgeOption");
        tcpSocketOption.setActionCommand("tcpSocketOption");
        udpSocketOption.setActionCommand("udpSocketOption");
        vdeOption.setActionCommand("vdeOption");
        hubportOption.setActionCommand("hubportOption");
        dumpOption.setActionCommand("dumpOption");
    }

    public JCheckBox getIsEnabled() {
        return isEnabled;
    }

	@Override
	public void setTitle(String string) {
		title = string;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void doSave(IemultorStore store) {
		 switch (position) {
         case 1:
             if (myfile.getFilemodel().getFirstNetworkNICOption() != null
                     || (myfile.getFilemodel().getFirstNetworkExtraOption() != null
                     || myfile.getFilemodel().getFirstNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             } else if (myfile.getFilemodel().getFirstNetworkNICOption() == null
                     && myfile.getFilemodel().getFirstNetworkExtraOption() == null
                     && myfile.getFilemodel().getFirstNetworkNetdevOption() == null
                     && myfile.getFilemodel().getSecondNetworkNICOption() == null
                     && myfile.getFilemodel().getSecondNetworkExtraOption() == null
                     && myfile.getFilemodel().getSecondNetworkNetdevOption() == null
                     && myfile.getFilemodel().getThirdNetworkNICOption() == null
                     && myfile.getFilemodel().getThirdNetworkExtraOption() == null
                     && myfile.getFilemodel().getThirdNetworkNetdevOption() == null
                     && myfile.getFilemodel().getFourthNetworkNICOption() == null
                     && myfile.getFilemodel().getFourthNetworkExtraOption() == null
                     && myfile.getFilemodel().getFourthNetworkNetdevOption() == null
                     && myfile.getFilemodel().getFifthNetworkNICOption() == null
                     && myfile.getFilemodel().getFifthNetworkExtraOption() == null
                     && myfile.getFilemodel().getFifthNetworkNetdevOption() == null
                     && myfile.getFilemodel().getSixthNetworkNICOption() == null
                     && myfile.getFilemodel().getSixthNetworkExtraOption() == null
                     && myfile.getFilemodel().getSixthNetworkNetdevOption() == null
                     && myfile.getFilemodel().getSeventhNetworkNICOption() == null
                     && myfile.getFilemodel().getSeventhNetworkExtraOption() == null
                     && myfile.getFilemodel().getSeventhNetworkNetdevOption() == null
                     && myfile.getFilemodel().getEighthNetworkNICOption() == null
                     && myfile.getFilemodel().getEighthNetworkExtraOption() == null
                     && myfile.getFilemodel().getEighthNetworkNetdevOption() == null
                     && myfile.getFilemodel().getNinthNetworkNICOption() == null
                     && myfile.getFilemodel().getNinthNetworkExtraOption() == null
                     && myfile.getFilemodel().getNinthNetworkNetdevOption() == null
                     && myfile.getFilemodel().getTenthNetworkNICOption() == null
                     && myfile.getFilemodel().getTenthNetworkExtraOption() == null
                     && myfile.getFilemodel().getTenthNetworkNetdevOption() == null) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 2:
             if (myfile.getFilemodel().getSecondNetworkNICOption() != null
                     || (myfile.getFilemodel().getSecondNetworkExtraOption() != null
                     || myfile.getFilemodel().getSecondNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 3:
             if (myfile.getFilemodel().getThirdNetworkNICOption() != null
                     || (myfile.getFilemodel().getThirdNetworkExtraOption() != null
                     || myfile.getFilemodel().getThirdNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 4:
             if (myfile.getFilemodel().getFourthNetworkNICOption() != null
                     || (myfile.getFilemodel().getFourthNetworkExtraOption() != null
                     || myfile.getFilemodel().getFourthNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 5:
             if (myfile.getFilemodel().getFifthNetworkNICOption() != null
                     || (myfile.getFilemodel().getFifthNetworkExtraOption() != null
                     || myfile.getFilemodel().getFifthNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 6:
             if (myfile.getFilemodel().getSixthNetworkNICOption() != null
                     || (myfile.getFilemodel().getSixthNetworkExtraOption() != null
                     || myfile.getFilemodel().getSixthNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 7:
             if (myfile.getFilemodel().getSeventhNetworkNICOption() != null
                     || (myfile.getFilemodel().getSeventhNetworkExtraOption() != null
                     || myfile.getFilemodel().getSeventhNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 8:
             if (myfile.getFilemodel().getEighthNetworkNICOption() != null
                     || (myfile.getFilemodel().getEighthNetworkExtraOption() != null
                     || myfile.getFilemodel().getEighthNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 9:
             if (myfile.getFilemodel().getNinthNetworkNICOption() != null
                     || (myfile.getFilemodel().getNinthNetworkExtraOption() != null
                     || myfile.getFilemodel().getNinthNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         case 10:
             if (myfile.getFilemodel().getTenthNetworkNICOption() != null
                     || (myfile.getFilemodel().getTenthNetworkExtraOption() != null
                     || myfile.getFilemodel().getTenthNetworkNetdevOption() != null)) {
                 this.isEnabled.setSelected(true);
             }
             break;
         default:
             break;
     }

     this.rechecks();
		
	}
}
