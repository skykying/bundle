package com.lembed.lite.studio.qemu.view.internal;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.control.FileControl;

public class KeyboardView extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel windowContent;

    private GridLayout gridLayout;

    private JLabel keyboardLayoutLanguageLabel;

    private JComboBox<String> keyboardLayoutLanguage;

    private JButton eraseButton;

    private JButton okButton;

    public KeyboardView(FileControl myfile) {
        super();

        windowContent = new JPanel();

        gridLayout = new GridLayout(2, 2);

        windowContent.setLayout(gridLayout);

        keyboardLayoutLanguageLabel = new JLabel(
                "Choose the keyboard layout language:");

        windowContent.add(keyboardLayoutLanguageLabel);

        String[] keyboardLayoutLanguageOptions = {"", "Arabic: ar",
            "German (Switzerland): de-ch", "Spanish: es",
            "Faroese (Faroe Islands): fo", "French (Canada): fr-ca",
            "Hungarian: hu", "Japanese: ja",
            "FYRO Macedonian (Former Yugoslav Republic of Macedonia): mk",
            "Norwegian (Bokmal, Nynorsk): no",
            "Portuguese (Brazil ABNT, ABNT2): pt-br", "Swedish: sv",
            "Danish: da", "English (United Kingdom): en-gb",
            "Estonian: et", "French: fr", "French (Switzerland): fr-ch",
            "Icelandic: is", "Lithuanian: lt", "Dutch: nl", "Polish: pl",
            "Russian: ru", "Thai: th", "German: de",
            "English (United States): en-us", "Finnish (Finland): fi",
            "French (Belgium): fr-be", "Croatian: hr", "Italian: it",
            "Latvian (Latvia): lv", "Dutch (Belgium): nl-be",
            "Portuguese (Portugal): pt", "Slovenian (Slovenia): sl",
            "Turkish (Turkey): tr"};

        this.keyboardLayoutLanguage = new JComboBox<String>(
                keyboardLayoutLanguageOptions);

        windowContent.add(keyboardLayoutLanguage);

        okButton = new JButton("OK");

        eraseButton = new JButton("Erase");

        windowContent.add(okButton);

        windowContent.add(eraseButton);

        this.setContentPane(windowContent);

        this.setTitle("JavaQemu - Keyboard Choice");

        if (myfile.getFilemodel().getKeyboardLayoutLanguage() != null) {
            if (myfile.getFilemodel().getKeyboardLayoutLanguage().isEmpty()) {
                this.keyboardLayoutLanguage.setSelectedItem(myfile.getFilemodel()
                        .getKeyboardLayoutLanguage());
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("ar")) {
                this.keyboardLayoutLanguage.setSelectedItem("Arabic: ar");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("de-ch")) {
                this.keyboardLayoutLanguage.setSelectedItem("German (Switzerland): de-ch");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("es")) {
                this.keyboardLayoutLanguage.setSelectedItem("Spanish: es");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("fo")) {
                this.keyboardLayoutLanguage.setSelectedItem("Faroese (Faroe Islands): fo");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("fr-ca")) {
                this.keyboardLayoutLanguage.setSelectedItem("French (Canada): fr-ca");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("hu")) {
                this.keyboardLayoutLanguage.setSelectedItem("Hungarian: hu");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("ja")) {
                this.keyboardLayoutLanguage.setSelectedItem("Japanese: ja");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("mk")) {
                this.keyboardLayoutLanguage.setSelectedItem("FYRO Macedonian (Former Yugoslav Republic of Macedonia): mk");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("no")) {
                this.keyboardLayoutLanguage.setSelectedItem("Norwegian (Bokmal, Nynorsk): no");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("pt-br")) {
                this.keyboardLayoutLanguage.setSelectedItem("Portuguese (Brazil ABNT, ABNT2): pt-br");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("sv")) {
                this.keyboardLayoutLanguage.setSelectedItem("Swedish: sv");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("da")) {
                this.keyboardLayoutLanguage.setSelectedItem("Danish: da");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("en-gb")) {
                this.keyboardLayoutLanguage.setSelectedItem("English (United Kingdom): en-gb");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("et")) {
                this.keyboardLayoutLanguage.setSelectedItem("Estonian: et");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("fr")) {
                this.keyboardLayoutLanguage.setSelectedItem("French: fr");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("fr-ch")) {
                this.keyboardLayoutLanguage.setSelectedItem("French (Switzerland): fr-ch");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("is")) {
                this.keyboardLayoutLanguage.setSelectedItem("Icelandic: is");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("lt")) {
                this.keyboardLayoutLanguage.setSelectedItem("Lithuanian: lt");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("nl")) {
                this.keyboardLayoutLanguage.setSelectedItem("Dutch: nl");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("pl")) {
                this.keyboardLayoutLanguage.setSelectedItem("Polish: pl");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("ru")) {
                this.keyboardLayoutLanguage.setSelectedItem("Russian: ru");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("th")) {
                this.keyboardLayoutLanguage.setSelectedItem("Thai: th");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("de")) {
                this.keyboardLayoutLanguage.setSelectedItem("German: de");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("en-us")) {
                this.keyboardLayoutLanguage.setSelectedItem("English (United States): en-us");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("fi")) {
                this.keyboardLayoutLanguage.setSelectedItem("Finnish (Finland): fi");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("fr-be")) {
                this.keyboardLayoutLanguage.setSelectedItem("French (Belgium): fr-be");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("hr")) {
                this.keyboardLayoutLanguage.setSelectedItem("Croatian: hr");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("it")) {
                this.keyboardLayoutLanguage.setSelectedItem("Italian: it");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("lv")) {
                this.keyboardLayoutLanguage.setSelectedItem("Latvian (Latvia): lv");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("nl-be")) {
                this.keyboardLayoutLanguage.setSelectedItem("Dutch (Belgium): nl-be");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("pt")) {
                this.keyboardLayoutLanguage.setSelectedItem("Portuguese (Portugal): pt");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("sl")) {
                this.keyboardLayoutLanguage.setSelectedItem("Slovenian (Slovenia): sl");
            } else if (myfile.getFilemodel().getKeyboardLayoutLanguage()
                    .equals("tr")) {
                this.keyboardLayoutLanguage.setSelectedItem("Turkish (Turkey): tr");
            }
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
    }

    public void configureStandardMode() {
        eraseButton.setActionCommand("eraseButton");
        okButton.setActionCommand("okButton");
    }

    public JComboBox<String> getKeyboardLayoutLanguage() {
        return keyboardLayoutLanguage;
    }

}
