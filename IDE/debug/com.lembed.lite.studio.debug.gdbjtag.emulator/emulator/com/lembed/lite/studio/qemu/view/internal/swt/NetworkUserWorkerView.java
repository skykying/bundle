package com.lembed.lite.studio.qemu.view.internal.swt;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.view.IemultorStore;

public class NetworkUserWorkerView extends DeviceViewWithFileChooser {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JCheckBox isEnabled;

    private JLabel temp[];

    private JLabel vlanDescription;

    private JComboBox<String> vlan;

    private JLabel nameDescription;

    private JTextField name;

    private JLabel netDescription;

    private JTextField net;

    private JLabel hostDescription;

    private JTextField host;

    private JCheckBox restrict;

    private JLabel hostnameDescription;

    private JTextField hostname;

    private JLabel dhcpstartDescription;

    private JTextField dhcpstart;

    private JLabel dnsDescription;

    private JTextField dns;

    private JButton dnssearch;

    private JTextField tftp;

    private JButton tftpChooser;

    private JTextField bootfile;

    private JButton bootfileChooser;

    private JTextField smb;

    private JButton smbChooser;

    private JLabel smbserverDescription;

    private JTextField smbserver;

    private JButton hostfwd;

    private JButton guestfwd;

    private JButton eraseButton;

    private JButton okButton;

	private int position;

    public NetworkUserWorkerView(EmulatorQemuMachineControl myfile, int position) {
        super(myfile,null);
        this.position = position;

        windowContent = new JPanel();

        setJpanel(windowContent);

        gridLayout = new GridLayout(16, 2);

        windowContent.setLayout(gridLayout);

        isEnabled = new JCheckBox("Enable this option!");

        windowContent.add(this.isEnabled);

        temp = new JLabel[3];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = new JLabel();
        }

        windowContent.add(temp[0]);

        vlanDescription = new JLabel("Choose the VLAN:");
        windowContent.add(vlanDescription);

        String[] numberOptions = {"", "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
            "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
            "49", "50", "51", "52", "53", "54", "55", "56", "57", "58",
            "59", "60", "61", "62", "63", "64", "65", "66", "67", "68",
            "69", "70", "71", "72", "73", "74", "75", "76", "77", "78",
            "79", "80", "81", "82", "83", "84", "85", "86", "87", "88",
            "89", "90", "91", "92", "93", "94", "95", "96", "97", "98",
            "99", "100"};

        this.vlan = new JComboBox<String>(numberOptions);
        windowContent.add(vlan);

        nameDescription = new JLabel("Choose the name or id:");
        windowContent.add(this.nameDescription);

        name = new JTextField();
        windowContent.add(this.name);

        netDescription = new JLabel("Choose the IP network address[/mask]:");
        windowContent.add(this.netDescription);

        net = new JTextField();
        windowContent.add(this.net);

        hostDescription = new JLabel("Choose the guess-visible host address:");
        windowContent.add(this.hostDescription);

        host = new JTextField();
        windowContent.add(this.host);

        restrict = new JCheckBox("Enable the 'restrict' option!");
        windowContent.add(this.restrict);

        windowContent.add(this.temp[1]);

        hostnameDescription = new JLabel(
                "Client hostname from built-in DHCP server:");

        windowContent.add(this.hostnameDescription);

        hostname = new JTextField();
        windowContent.add(this.hostname);

        dhcpstartDescription = new JLabel(
                "First of the 16 IPs the built-in DHCP server can assign:");

        windowContent.add(this.dhcpstartDescription);

        dhcpstart = new JTextField();
        windowContent.add(this.dhcpstart);

        dnsDescription = new JLabel(
                "Guess-visible address of the virtual nameserver:");
        windowContent.add(this.dnsDescription);

        dns = new JTextField();
        windowContent.add(this.dns);

        dnssearch = new JDoubledLineButton("Choose the entry for the domain-search list",
                " sent by the built-in DHCP server!");

        windowContent.add(this.dnssearch);

        windowContent.add(this.temp[2]);

        tftpChooser = new JButton("Choose the root directory of the built-in TFTP server:");
        windowContent.add(this.tftpChooser);

        tftp = new JTextField();
        windowContent.add(this.tftp);

        bootfileChooser = new JButton("Choose the BOOTP filename for broadcasting:");
        windowContent.add(this.bootfileChooser);

        bootfile = new JTextField();
        windowContent.add(this.bootfile);

        smbChooser = new JButton(
                "Choose the root directory of the built-in SMB server:");

        windowContent.add(this.smbChooser);

        smb = new JTextField();

        windowContent.add(this.smb);

        smbserverDescription = new JLabel(
                "Choose the IP address of the built-in SMB server:");

        windowContent.add(this.smbserverDescription);

        smbserver = new JTextField();

        windowContent.add(this.smbserver);

        hostfwd = new JDoubledLineButton("Set the redirection of incoming TCP, or UDP,",
                " connections from host to guest!");

        windowContent.add(this.hostfwd);

        guestfwd = new JButton(
                "Forward guest TCP connections to the IP address server!");

        windowContent.add(this.guestfwd);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

        this.add(windowContent);

        this.setTitle("JavaQemu - Network User Choice");

      
    }

    private void rechecks() {
//        this.pack();
        this.repaint();
    }

    public void configureListener(ActionListener listener) {
        eraseButton.addActionListener(listener);
        okButton.addActionListener(listener);
        dnssearch.addActionListener(listener);
        tftpChooser.addActionListener(listener);
        bootfileChooser.addActionListener(listener);
        smbChooser.addActionListener(listener);
        hostfwd.addActionListener(listener);
        guestfwd.addActionListener(listener);
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
        dnssearch.setActionCommand("dnssearch");
        tftpChooser.setActionCommand("tftpChooser");
        bootfileChooser.setActionCommand("bootfileChooser");
        smbChooser.setActionCommand("smbChooser");
        hostfwd.setActionCommand("hostfwd");
        guestfwd.setActionCommand("guestfwd");
    }

    private void buildMe(String optionString) {
        String options[] = optionString.split(",");
        for (String option : options) {
            if (option.startsWith("vlan=")) {
                this.vlan
                        .setSelectedItem(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("name=")) {
                this.name.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("net=")) {
                this.net.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("host=")) {
                this.host.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("restrict=")) {
                if (option.substring(option.indexOf("=") + 1).equals("on")) {
                    this.restrict.setSelected(true);
                }
            } else if (option.startsWith("hostname=")) {
                this.hostname
                        .setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("dhcpstart=")) {
                this.dhcpstart
                        .setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("dns=")) {
                this.dns.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("tftp=")) {
                this.tftp.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("bootfile=")) {
                this.bootfile
                        .setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("smb=")) {
                this.smb.setText(option.substring(option.indexOf("=") + 1));
            } else if (option.startsWith("smbserver=")) {
                this.smbserver
                        .setText(option.substring(option.indexOf("=") + 1));
            }
        }
    }

    public JCheckBox getIsEnabled() {
        return isEnabled;
    }

    public JComboBox<String> getVlan() {
        return vlan;
    }

    public JTextField getNameContents() {
        return name;
    }

    public JTextField getNet() {
        return net;
    }

    public JTextField getHost() {
        return host;
    }

    public JCheckBox getRestrict() {
        return restrict;
    }

    public JTextField getHostname() {
        return hostname;
    }

    public JTextField getDhcpstart() {
        return dhcpstart;
    }

    public JTextField getDns() {
        return dns;
    }

    public JTextField getTftp() {
        return tftp;
    }

    public JTextField getBootfile() {
        return bootfile;
    }

    public JTextField getSmb() {
        return smb;
    }

    public JTextField getSmbserver() {
        return smbserver;
    }

    private boolean contains(String option) {
        if (option.contains("user")) {
            return true;
        }
        return false;
    }

	@Override
	public void applyView(IemultorStore store) {
		  switch (position) {
          case 1:
              if (eQControl.getMachineModel().getFirstNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel()
                          .getFirstNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel()
                              .getFirstNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 2:
              if (eQControl.getMachineModel().getSecondNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel()
                          .getSecondNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel()
                              .getSecondNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 3:
              if (eQControl.getMachineModel().getThirdNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getThirdNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getThirdNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 4:
              if (eQControl.getMachineModel().getFourthNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getFourthNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getFourthNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 5:
              if (eQControl.getMachineModel().getFifthNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getFifthNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getFifthNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 6:
              if (eQControl.getMachineModel().getSixthNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getSixthNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getSixthNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 7:
              if (eQControl.getMachineModel().getSeventhNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getSeventhNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getSeventhNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 8:
              if (eQControl.getMachineModel().getEighthNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getEighthNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getEighthNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 9:
              if (eQControl.getMachineModel().getNinthNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getNinthNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getNinthNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          case 10:
              if (eQControl.getMachineModel().getTenthNetworkExtraOption() != null) {
                  if (this.contains(eQControl.getMachineModel().getTenthNetworkExtraOption())) {
                      this.buildMe(eQControl.getMachineModel().getTenthNetworkExtraOption());
                      this.isEnabled.setSelected(true);
                  }
              }
              break;
          default:
              break;
      }

      this.rechecks();
		
	}
}
