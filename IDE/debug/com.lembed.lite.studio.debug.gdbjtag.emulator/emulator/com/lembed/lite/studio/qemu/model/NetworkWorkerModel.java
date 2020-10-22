package com.lembed.lite.studio.qemu.model;

import java.util.HashMap;

import com.lembed.lite.studio.qemu.control.EmulationControl;
import com.lembed.lite.studio.qemu.control.FileControl;
import com.lembed.lite.studio.qemu.control.OptionsControl;
import com.lembed.lite.studio.qemu.view.internal.NetworkManagerView;

public class NetworkWorkerModel extends OptionsControl {

    private FileControl myfile;
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

    public NetworkWorkerModel(EmulationControl myemulation, FileControl myfile,
            int position) {
        super(myemulation);
        this.myfile = myfile;
        this.position = position;
        option = "";

        switch (position) {
            case 1:
                if (myfile.getFilemodel().getFirstNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getFirstNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getFirstNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getFirstNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getFirstNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getFirstNetworkNetdevOption().split(","));
                }
                break;
            case 2:
                if (myfile.getFilemodel().getSecondNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getSecondNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getSecondNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getSecondNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getSecondNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getSecondNetworkNetdevOption().split(","));
                }
                break;
            case 3:
                if (myfile.getFilemodel().getThirdNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getThirdNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getThirdNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getThirdNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getThirdNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getThirdNetworkNetdevOption().split(","));
                }
                break;
            case 4:
                if (myfile.getFilemodel().getFourthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getFourthNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getFourthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getFourthNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getFourthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getFourthNetworkNetdevOption().split(","));
                }
                break;
            case 5:
                if (myfile.getFilemodel().getFifthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getFifthNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getFifthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getFifthNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getFifthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getFifthNetworkNetdevOption().split(","));
                }
                break;
            case 6:
                if (myfile.getFilemodel().getSixthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getSixthNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getSixthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getSixthNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getSixthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getSixthNetworkNetdevOption().split(","));
                }
                break;
            case 7:
                if (myfile.getFilemodel().getSeventhNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getSeventhNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getSeventhNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getSeventhNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getSeventhNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getSeventhNetworkNetdevOption().split(","));
                }
                break;
            case 8:
                if (myfile.getFilemodel().getEighthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getEighthNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getEighthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getEighthNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getEighthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getEighthNetworkNetdevOption().split(","));
                }
                break;
            case 9:
                if (myfile.getFilemodel().getNinthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getNinthNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getNinthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getNinthNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getNinthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
                            .getNinthNetworkNetdevOption().split(","));
                }
                break;
            case 10:
                if (myfile.getFilemodel().getTenthNetworkNICOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getTenthNetworkNICOption().split(","));
                }
                if (myfile.getFilemodel().getTenthNetworkExtraOption() != null) {
                    this.buildIt("-net", myfile.getFilemodel()
                            .getTenthNetworkExtraOption().split(","));
                }
                if (myfile.getFilemodel().getTenthNetworkNetdevOption() != null) {
                    this.buildIt("-netdev", myfile.getFilemodel()
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
                            myfile.getFilemodel().setFirstNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 2:
                            myfile.getFilemodel().setSecondNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 3:
                            myfile.getFilemodel().setThirdNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 4:
                            myfile.getFilemodel().setFourthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 5:
                            myfile.getFilemodel().setFifthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 6:
                            myfile.getFilemodel().setSixthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 7:
                            myfile.getFilemodel().setSeventhNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 8:
                            myfile.getFilemodel().setEighthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 9:
                            myfile.getFilemodel().setNinthNetworkNICOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 10:
                            myfile.getFilemodel().setTenthNetworkNICOption(
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
                            myfile.getFilemodel().setFirstNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 2:
                            myfile.getFilemodel().setSecondNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 3:
                            myfile.getFilemodel().setThirdNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 4:
                            myfile.getFilemodel().setFourthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 5:
                            myfile.getFilemodel().setFifthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 6:
                            myfile.getFilemodel().setSixthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 7:
                            myfile.getFilemodel().setSeventhNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 8:
                            myfile.getFilemodel().setEighthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 9:
                            myfile.getFilemodel().setNinthNetworkExtraOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 10:
                            myfile.getFilemodel().setTenthNetworkExtraOption(
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
                            myfile.getFilemodel().setFirstNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 2:
                            myfile.getFilemodel().setSecondNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 3:
                            myfile.getFilemodel().setThirdNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 4:
                            myfile.getFilemodel().setFourthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 5:
                            myfile.getFilemodel().setFifthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 6:
                            myfile.getFilemodel().setSixthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 7:
                            myfile.getFilemodel().setSeventhNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 8:
                            myfile.getFilemodel().setEighthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 9:
                            myfile.getFilemodel().setNinthNetworkNetdevOption(
                                    result.toString().substring(
                                            result.toString().indexOf(" ") + 1));
                            break;
                        case 10:
                            myfile.getFilemodel().setTenthNetworkNetdevOption(
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
                        myfile.getFilemodel().setFirstNetworkNICOption("");
                        break;
                    case 2:
                        myfile.getFilemodel().setSecondNetworkNICOption("");
                        break;
                    case 3:
                        myfile.getFilemodel().setThirdNetworkNICOption("");
                        break;
                    case 4:
                        myfile.getFilemodel().setFourthNetworkNICOption("");
                        break;
                    case 5:
                        myfile.getFilemodel().setFifthNetworkNICOption("");
                        break;
                    case 6:
                        myfile.getFilemodel().setSixthNetworkNICOption("");
                        break;
                    case 7:
                        myfile.getFilemodel().setSeventhNetworkNICOption("");
                        break;
                    case 8:
                        myfile.getFilemodel().setEighthNetworkNICOption("");
                        break;
                    case 9:
                        myfile.getFilemodel().setNinthNetworkNICOption("");
                        break;
                    case 10:
                        myfile.getFilemodel().setTenthNetworkNICOption("");
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                int oldPosition1 = ((position - 1) / 3) + 1;
                switch (oldPosition1) {
                    case 1:
                        myfile.getFilemodel().setFirstNetworkExtraOption("");
                        break;
                    case 2:
                        myfile.getFilemodel().setSecondNetworkExtraOption("");
                        break;
                    case 3:
                        myfile.getFilemodel().setThirdNetworkExtraOption("");
                        break;
                    case 4:
                        myfile.getFilemodel().setFourthNetworkExtraOption("");
                        break;
                    case 5:
                        myfile.getFilemodel().setFifthNetworkExtraOption("");
                        break;
                    case 6:
                        myfile.getFilemodel().setSixthNetworkExtraOption("");
                        break;
                    case 7:
                        myfile.getFilemodel().setSeventhNetworkExtraOption("");
                        break;
                    case 8:
                        myfile.getFilemodel().setEighthNetworkExtraOption("");
                        break;
                    case 9:
                        myfile.getFilemodel().setNinthNetworkExtraOption("");
                        break;
                    case 10:
                        myfile.getFilemodel().setTenthNetworkExtraOption("");
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                int oldPosition2 = ((position - 2) / 3) + 1;
                switch (oldPosition2) {
                    case 1:
                        myfile.getFilemodel().setFirstNetworkNetdevOption("");
                        break;
                    case 2:
                        myfile.getFilemodel().setSecondNetworkNetdevOption("");
                        break;
                    case 3:
                        myfile.getFilemodel().setThirdNetworkNetdevOption("");
                        break;
                    case 4:
                        myfile.getFilemodel().setFourthNetworkNetdevOption("");
                        break;
                    case 5:
                        myfile.getFilemodel().setFifthNetworkNetdevOption("");
                        break;
                    case 6:
                        myfile.getFilemodel().setSixthNetworkNetdevOption("");
                        break;
                    case 7:
                        myfile.getFilemodel().setSeventhNetworkNetdevOption("");
                        break;
                    case 8:
                        myfile.getFilemodel().setEighthNetworkNetdevOption("");
                        break;
                    case 9:
                        myfile.getFilemodel().setNinthNetworkNetdevOption("");
                        break;
                    case 10:
                        myfile.getFilemodel().setTenthNetworkNetdevOption("");
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
