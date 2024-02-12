/*
 * easy-berichtsheft-generator
 * Copyright (c) 2024. Timon Schneider (timon-schneider.com).
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.ebg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.ebg.Main.dateFormat;
import static org.ebg.Main.getMessageFromBundle;

public class SchooldaysGenerator {
    public SchooldaysGenerator() {

        fromDateFormattedTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JFormattedTextField field = (JFormattedTextField) input;
                try {
                    Date dateObject = dateFormat.parse(field.getText());
                    String dateString = dateFormat.format(dateObject);
                    field.setText(dateString);
                    return true;
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, getMessageFromBundle("invalid.date.format.please.enter.date.as.dd.mm.yyyy"), getMessageFromBundle("error"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        });
        fromDateFormattedTextField.setText("01.01.2024");

        toDateFormattedTextField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JFormattedTextField field = (JFormattedTextField) input;
                try {
                    Date dateObject = dateFormat.parse(field.getText());
                    String dateString = dateFormat.format(dateObject);
                    field.setText(dateString);
                    return true;
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, getMessageFromBundle("invalid.date.format.please.enter.date.as.dd.mm.yyyy"), getMessageFromBundle("error"), JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        });
        toDateFormattedTextField.setText("01.02.2024");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = descriptionTextField.getText();

                HashMap<Integer, Boolean> schoolDaysCheckBoxStates = new HashMap<>(); // Counting: see PLACEHOLDERS
                schoolDaysCheckBoxStates.put(11, mondayCheckBox.isSelected());
                schoolDaysCheckBoxStates.put(12, tuesdayCheckBox.isSelected());
                schoolDaysCheckBoxStates.put(13, wednesdayCheckBox.isSelected());
                schoolDaysCheckBoxStates.put(14, thursdayCheckBox.isSelected());
                schoolDaysCheckBoxStates.put(15, fridayCheckBox.isSelected());
                schoolDaysCheckBoxStates.put(16, saturdayCheckBox.isSelected());
                schoolDaysCheckBoxStates.put(17, sundayCheckBox.isSelected());

                Date fromDate = null;
                JFormattedTextField fromDateField = fromDateFormattedTextField;
                try {
                    fromDate = dateFormat.parse(fromDateField.getText());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                Date toDate = null;
                JFormattedTextField toDateField = toDateFormattedTextField;
                try {
                    toDate = dateFormat.parse(toDateField.getText());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                if (toDate.before(fromDate)) {
                    JOptionPane.showMessageDialog(null, getMessageFromBundle("error.to.date.cannot.be.before.from.date"), getMessageFromBundle("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate fromDateLocal = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate toDateLocal = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                int amountWeeks = (int) ChronoUnit.WEEKS.between(fromDateLocal, toDateLocal);

                amountWeeks++;

                LocalDate firstDayOfWeekOfFromDate = fromDateLocal.with(DayOfWeek.MONDAY);

                LocalDate tempLastDayOfWeekOfFromDate = firstDayOfWeekOfFromDate.plusWeeks(1);
                LocalDate lastDayOfWeekOfFromDate = tempLastDayOfWeekOfFromDate.minusDays(1);


                for (int i = 0; i < amountWeeks; i++) {

                    LocalDate localTempDate = firstDayOfWeekOfFromDate;

                    for (int weekdayCounter = 11; weekdayCounter < 18; weekdayCounter++) { // Counting: see PLACEHOLDERS
                        boolean isDateInRange = !localTempDate.isBefore(fromDateLocal) && !localTempDate.isAfter(toDateLocal);
                        boolean isSchoolday = schoolDaysCheckBoxStates.get(weekdayCounter);

                        if (isDateInRange && isSchoolday) {

                            Main.addSchoolday(description, localTempDate.getDayOfMonth() + "." + localTempDate.getMonthValue() + "." + localTempDate.getYear());
                        }

                        localTempDate = localTempDate.plusDays(1);
                    }

                    firstDayOfWeekOfFromDate = firstDayOfWeekOfFromDate.plusWeeks(1);
                    lastDayOfWeekOfFromDate = lastDayOfWeekOfFromDate.plusWeeks(1);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SchooldaysGenerator");
        frame.setContentPane(new SchooldaysGenerator().schooldaysGeneratorPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    JPanel schooldaysGeneratorPanel;
    private JCheckBox mondayCheckBox;
    private JCheckBox tuesdayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;
    private JCheckBox saturdayCheckBox;
    private JCheckBox sundayCheckBox;
    private JLabel fromDateLabel;
    private JFormattedTextField fromDateFormattedTextField;
    private JLabel toDateLabel;
    private JFormattedTextField toDateFormattedTextField;
    private JButton generateButton;
    private JTextField descriptionTextField;
    private JLabel descriptionLabel;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        schooldaysGeneratorPanel = new JPanel();
        schooldaysGeneratorPanel.setLayout(new GridBagLayout());
        mondayCheckBox = new JCheckBox();
        mondayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(mondayCheckBox, this.$$$getMessageFromBundle$$$("languages", "monday"));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        schooldaysGeneratorPanel.add(mondayCheckBox, gbc);
        tuesdayCheckBox = new JCheckBox();
        tuesdayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(tuesdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "tuesday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        schooldaysGeneratorPanel.add(tuesdayCheckBox, gbc);
        wednesdayCheckBox = new JCheckBox();
        wednesdayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(wednesdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "wednesday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        schooldaysGeneratorPanel.add(wednesdayCheckBox, gbc);
        thursdayCheckBox = new JCheckBox();
        thursdayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(thursdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "thursday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        schooldaysGeneratorPanel.add(thursdayCheckBox, gbc);
        fridayCheckBox = new JCheckBox();
        fridayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(fridayCheckBox, this.$$$getMessageFromBundle$$$("languages", "friday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        schooldaysGeneratorPanel.add(fridayCheckBox, gbc);
        saturdayCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(saturdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "saturday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        schooldaysGeneratorPanel.add(saturdayCheckBox, gbc);
        sundayCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(sundayCheckBox, this.$$$getMessageFromBundle$$$("languages", "sunday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        schooldaysGeneratorPanel.add(sundayCheckBox, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        schooldaysGeneratorPanel.add(spacer1, gbc);
        fromDateLabel = new JLabel();
        this.$$$loadLabelText$$$(fromDateLabel, this.$$$getMessageFromBundle$$$("languages", "from.date"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.ipadx = 10;
        schooldaysGeneratorPanel.add(fromDateLabel, gbc);
        fromDateFormattedTextField = new JFormattedTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        schooldaysGeneratorPanel.add(fromDateFormattedTextField, gbc);
        toDateLabel = new JLabel();
        this.$$$loadLabelText$$$(toDateLabel, this.$$$getMessageFromBundle$$$("languages", "to.date"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.ipadx = 10;
        schooldaysGeneratorPanel.add(toDateLabel, gbc);
        toDateFormattedTextField = new JFormattedTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        schooldaysGeneratorPanel.add(toDateFormattedTextField, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        schooldaysGeneratorPanel.add(spacer2, gbc);
        descriptionLabel = new JLabel();
        this.$$$loadLabelText$$$(descriptionLabel, this.$$$getMessageFromBundle$$$("languages", "description"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.ipadx = 10;
        schooldaysGeneratorPanel.add(descriptionLabel, gbc);
        descriptionTextField = new JTextField();
        descriptionTextField.setText("Berufsschule");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        schooldaysGeneratorPanel.add(descriptionTextField, gbc);
        generateButton = new JButton();
        this.$$$loadButtonText$$$(generateButton, this.$$$getMessageFromBundle$$$("languages", "generate"));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        schooldaysGeneratorPanel.add(generateButton, gbc);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return schooldaysGeneratorPanel;
    }

}
