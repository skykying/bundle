package com.lembed.lite.studio.qemu.model.swt;

import java.util.HashMap;

import com.lembed.lite.studio.qemu.control.swt.EmulationControl;
import com.lembed.lite.studio.qemu.control.swt.EmulatorQemuMachineControl;
import com.lembed.lite.studio.qemu.control.swt.OptionsControl;
import com.lembed.lite.studio.qemu.model.swt.options.OptionsEnumModel;
import com.lembed.lite.studio.qemu.view.internal.NetworkManagerView;

public class NetworkWorkerModel extends OptionsControl {

    private EmulatorQemuMachineControl myfile;
    private static HashMap<Integer, Integer> gettingOptionsEnumModel;
    private int position;
    private String option;

    static {
        gettingOptionsEnumModel = new HashMap<Integer, Integer>();
        for (int i = 0; i < NetworkManagerView.networkOptionsNumber * 3; i++) {
            gettingOptionsEnumModel.put(i,
                    OptionsEnumModel.NETWORKNIC1OPTION.getValor() + i);
        }
    }

    public NetworkWorkerModel(EmulationControl myemulation, EmulatorQemuMachineControl myfile,
            int position) {
        super(myemulation);
        this.myfile = myfile;
        this.position = position;
        option = "";

        switch (position) {
            case 1:
                if (myfile.getMachineModel().getFirstNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getFirstNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getFirstNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getFirstNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getFirstNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getFirstNetworkNetdevOption().split(","));
                }
                break;
            case 2:
                if (myfile.getMachineModel().getSecondNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getSecondNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getSecondNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getSecondNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getSecondNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getSecondNetworkNetdevOption().split(","));
                }
                break;
            case 3:
                if (myfile.getMachineModel().getThirdNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getThirdNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getThirdNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getThirdNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getThirdNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getThirdNetworkNetdevOption().split(","));
                }
                break;
            case 4:
                if (myfile.getMachineModel().getFourthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getFourthNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getFourthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getFourthNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getFourthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getFourthNetworkNetdevOption().split(","));
                }
                break;
            case 5:
                if (myfile.getMachineModel().getFifthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getFifthNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getFifthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getFifthNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getFifthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getFifthNetworkNetdevOption().split(","));
                }
                break;
            case 6:
                if (myfile.getMachineModel().getSixthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getSixthNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getSixthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getSixthNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getSixthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getSixthNetworkNetdevOption().split(","));
                }
                break;
            case 7:
                if (myfile.getMachineModel().getSeventhNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getSeventhNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getSeventhNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getSeventhNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getSeventhNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getSeventhNetworkNetdevOption().split(","));
                }
                break;
            case 8:
                if (myfile.getMachineModel().getEighthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getEighthNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getEighthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getEighthNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getEighthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getEighthNetworkNetdevOption().split(","));
                }
                break;
            case 9:
                if (myfile.getMachineModel().getNinthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getNinthNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getNinthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getNinthNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getNinthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getNinthNetworkNetdevOption().split(","));
                }
                break;
            case 10:
                if (myfile.getMachineModel().getTenthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getTenthNetworkNICOption().split(","));
                }
                if (myfile.getMachineModel().getTenthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getMachineModel()
                            .getTenthNetworkExtraOption().split(","));
                }
                if (myfile.getMachineModel().getTenthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getMachineModel()
                            .getTenthNetworkNetdevOption().split(","));
                }
                break;
            default:
                break;
        }

    }

    public void buildIt(String tag, String[] options) {
        StringBuilder result = new StringBuilder(tag);
        int truePosition = 0;
        if (tag.equals("-net")) {
            if (options.length > 0) {
                if (options[0].equals("nic") || options[0].equals("none")) {
                    result.append(" ").append(options[0]);
                    for (int i = 1; i < options.length; i++) {
                        result.append(",").append(options[i]);
                    }
                    truePosition = (3 * (position - 1));
                    switch (position) {
                        case 1:
                            myfile.getMachineModel().setFirstNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 2:
                            myfile.getMachineModel().setSecondNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 3:
                            myfile.getMachineModel().setThirdNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 4:
                            myfile.getMachineModel().setFourthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 5:
                            myfile.getMachineModel().setFifthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 6:
                            myfile.getMachineModel().setSixthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 7:
                            myfile.getMachineModel().setSeventhNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 8:
                            myfile.getMachineModel().setEighthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 9:
                            myfile.getMachineModel().setNinthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 10:
                            myfile.getMachineModel().setTenthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        default:
                            break;
                    }
                } else if (options[0].equals("user")
                        || options[0].equals("tap")
                        || options[0].equals("bridge")
                        || options[0].equals("socket")
                        || options[0].equals("vde")
                        || options[0].equals("dump")) {
                    result.append(" ").append(options[0]);
                    for (int i = 1; i < options.length; i++) {
                        result.append(",").append(options[i]);
                    }
                    truePosition = (3 * (position - 1)) + 1;
                    switch (position) {
                        case 1:
                            myfile.getMachineModel().setFirstNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 2:
                            myfile.getMachineModel().setSecondNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 3:
                            myfile.getMachineModel().setThirdNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 4:
                            myfile.getMachineModel().setFourthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 5:
                            myfile.getMachineModel().setFifthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 6:
                            myfile.getMachineModel().setSixthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 7:
                            myfile.getMachineModel().setSeventhNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 8:
                            myfile.getMachineModel().setEighthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 9:
                            myfile.getMachineModel().setNinthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 10:
                            myfile.getMachineModel().setTenthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        default:
                            break;
                    }
                    option = options[0];
                }
            }
        } else if (tag.equals("-netdev")) {
            if (options.length > 0) {
                if (options[0].equals("hubport")) {
                    result.append(" ").append(options[0]);
                    for (int i = 1; i < options.length; i++) {
                        result.append(",").append(options[i]);
                    }
                    truePosition = (3 * (position - 1)) + 2;
                    switch (position) {
                        case 1:
                            myfile.getMachineModel().setFirstNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 2:
                            myfile.getMachineModel().setSecondNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 3:
                            myfile.getMachineModel().setThirdNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 4:
                            myfile.getMachineModel().setFourthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 5:
                            myfile.getMachineModel().setFifthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 6:
                            myfile.getMachineModel().setSixthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 7:
                            myfile.getMachineModel().setSeventhNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 8:
                            myfile.getMachineModel().setEighthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 9:
                            myfile.getMachineModel().setNinthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 10:
                            myfile.getMachineModel().setTenthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        if ((result.toString().equals(tag) || result.toString().equals(tag + " " + options[0]))
                && !options[0].equals("none") || result.toString().equals(tag + " user,restrict=off")) {
            this.unsetOption(tag + " ", truePosition);
        } else {
            this.setOption(
                    result.toString().substring(
                            result.toString().indexOf(" ") + 1), truePosition);
        }
    }

    public void setOption(String option, int position) {
        super.setOption(option, gettingOptionsEnumModel.get(position));
    }

    public void unsetOption(String option, int position) {
        super.unsetOption(option, gettingOptionsEnumModel.get(position));
        switch (position % 3) {
            case 0:
                int oldPosition0 = (position / 3) + 1;
                switch (oldPosition0) {
                    case 1:
                        myfile.getMachineModel().setFirstNetworkNICOption("");
                        break;
                    case 2:
                        myfile.getMachineModel().setSecondNetworkNICOption("");
                        break;
                    case 3:
                        myfile.getMachineModel().setThirdNetworkNICOption("");
                        break;
                    case 4:
                        myfile.getMachineModel().setFourthNetworkNICOption("");
                        break;
                    case 5:
                        myfile.getMachineModel().setFifthNetworkNICOption("");
                        break;
                    case 6:
                        myfile.getMachineModel().setSixthNetworkNICOption("");
                        break;
                    case 7:
                        myfile.getMachineModel().setSeventhNetworkNICOption("");
                        break;
                    case 8:
                        myfile.getMachineModel().setEighthNetworkNICOption("");
                        break;
                    case 9:
                        myfile.getMachineModel().setNinthNetworkNICOption("");
                        break;
                    case 10:
                        myfile.getMachineModel().setTenthNetworkNICOption("");
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                int oldPosition1 = ((position - 1) / 3) + 1;
                switch (oldPosition1) {
                    case 1:
                        myfile.getMachineModel().setFirstNetworkExtraOption("");
                        break;
                    case 2:
                        myfile.getMachineModel().setSecondNetworkExtraOption("");
                        break;
                    case 3:
                        myfile.getMachineModel().setThirdNetworkExtraOption("");
                        break;
                    case 4:
                        myfile.getMachineModel().setFourthNetworkExtraOption("");
                        break;
                    case 5:
                        myfile.getMachineModel().setFifthNetworkExtraOption("");
                        break;
                    case 6:
                        myfile.getMachineModel().setSixthNetworkExtraOption("");
                        break;
                    case 7:
                        myfile.getMachineModel().setSeventhNetworkExtraOption("");
                        break;
                    case 8:
                        myfile.getMachineModel().setEighthNetworkExtraOption("");
                        break;
                    case 9:
                        myfile.getMachineModel().setNinthNetworkExtraOption("");
                        break;
                    case 10:
                        myfile.getMachineModel().setTenthNetworkExtraOption("");
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                int oldPosition2 = ((position - 2) / 3) + 1;
                switch (oldPosition2) {
                    case 1:
                        myfile.getMachineModel().setFirstNetworkNetdevOption("");
                        break;
                    case 2:
                        myfile.getMachineModel().setSecondNetworkNetdevOption("");
                        break;
                    case 3:
                        myfile.getMachineModel().setThirdNetworkNetdevOption("");
                        break;
                    case 4:
                        myfile.getMachineModel().setFourthNetworkNetdevOption("");
                        break;
                    case 5:
                        myfile.getMachineModel().setFifthNetworkNetdevOption("");
                        break;
                    case 6:
                        myfile.getMachineModel().setSixthNetworkNetdevOption("");
                        break;
                    case 7:
                        myfile.getMachineModel().setSeventhNetworkNetdevOption("");
                        break;
                    case 8:
                        myfile.getMachineModel().setEighthNetworkNetdevOption("");
                        break;
                    case 9:
                        myfile.getMachineModel().setNinthNetworkNetdevOption("");
                        break;
                    case 10:
                        myfile.getMachineModel().setTenthNetworkNetdevOption("");
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    public String getOption() {
        return option;
    }
}
