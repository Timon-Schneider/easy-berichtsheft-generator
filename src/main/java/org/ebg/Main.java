

/*
 * easy-berichtsheft-generator
 * Copyright (c) 2024. Timon Schneider (timon-schneider.com).
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.ebg;

import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.dml.CTBlip;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.*;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Body;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    private static ResourceBundle bundle;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JPanel MainPanel;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTable schoolDaysTable;
    private static DateConversionTableModel schoolDaysModel;
    private JScrollPane paneSchoolDays;
    private JTable holidaysTable;
    private static DateConversionTableModel holidaysModel;
    private JScrollPane paneHolidays;
    private JButton generateButton;
    private JLabel jobLabel;
    private JTextField jobTextField;
    private JButton schoolDaysPlusButton;
    private JButton schoolDaysMinusButton;
    private JButton holidaysMinusButton;
    private JButton holidaysPlusButton;
    private JLabel schoolDaysLabel;
    private JLabel holidaysLabel;
    private JTable tasksTable;
    private JButton tasksPlusButton;
    private JButton tasksMinusButton;
    private JScrollPane paneTasks;
    private JLabel tasksLabel;
    private JLabel workDaysLabel;
    private JCheckBox mondayCheckBox;
    private JCheckBox tuesdayCheckBox;
    private JCheckBox wednesdayCheckBox;
    private JCheckBox thursdayCheckBox;
    private JCheckBox fridayCheckBox;
    private JCheckBox saturdayCheckBox;
    private JCheckBox sundayCheckBox;
    private JLabel templateLabel;
    private JTextField chooseATemplateTextField;
    private JButton chooseFileButton;
    private JButton generateSchooldaysButton;
    private JButton generateFromICSFileButton;
    private JFormattedTextField fromDateFormattedTextField;
    private JFormattedTextField toDateFormattedTextField;
    private JLabel fromDateLabel;
    private JLabel toDateLabel;
    private JLabel generationPeriodLabel;
    private JLabel apprenticeshipYearLabel;
    private JSpinner apprenticeshipYearSpinner;
    private JButton apprenticeshipYearHelpButton;
    private JLabel hiringMonthLabel;
    private JSpinner hiringMonthSpinner;
    private JButton hiringMonthHelpButton;
    private JLabel minDailyTasksLabel;
    private JSpinner minDailyTasksSpinner;
    private JSpinner maxDailyTasksSpinner;
    private JLabel maxDailyTasksLabel;
    private JButton dailyTasksHelpButton;
    private JSpinner startPageSpinner;
    private JLabel startPageLabel;
    private JLabel workingHoursLabel;
    private JSpinner workingHoursSpinner;
    private JButton generateFromICSFileHelpButton;
    private JButton schoolDaysHelpButton;
    private JButton holidaysHelpButton;
    private JButton tasksHelpButton;
    private static JMenuBar menuBar;
    private static JMenu importMenu;
    private JMenuItem importSubmenu1;
    private JMenuItem importSubmenu2;
    private JMenuItem importSubmenu3;
    private static JMenu exportMenu;
    private JMenuItem exportSubmenu1;
    private JMenuItem exportSubmenu2;
    private JMenuItem exportSubmenu3;
    //    private static JMenu settingsMenu;
//    private JMenuItem settingsSubmenu1;
    public static SimpleDateFormat dateFormat;
    public static SimpleDateFormat wrongDateFormat;
    private static SpinnerModel apprenticeshipYearSpinnerModel;
    private static SpinnerModel hiringMonthSpinnerModel;
    private static SpinnerModel minDailyTasksSpinnerModel;
    private static SpinnerModel maxDailyTasksSpinnerModel;
    private static SpinnerModel startPageSpinnerModel;
    private static SpinnerModel workingHoursSpinnerModel;
    //    private static JFrame settingsFrame;
    private static JFrame schooldaysGeneratorFrame;
    // private static Preferences prefs = Preferences.userNodeForPackage(Settings.class);
    private String font;
    private static final String[] PLACEHOLDERS = {
            "${name}",
            "${profession}",
            "${trainingYear}",
            "${fD}",
            "${fM}",
            "${fY}",
            "${lD}",
            "${lM}",
            "${lY}",
            "${y}",
            "${p}",
            "${moTask}",
            "${tuTask}",
            "${weTask}",
            "${thTask}",
            "${frTask}",
            "${saTask}",
            "${suTask}",
            "${hMo}",
            "${hTu}",
            "${hWe}",
            "${hTh}",
            "${hFr}",
            "${hSa}",
            "${hSu}"
    };


    public Main() {
        dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);

        wrongDateFormat = new SimpleDateFormat("yyyyMMdd");
        wrongDateFormat.setLenient(false);

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
        toDateFormattedTextField.setText("14.01.2024");

        String[] columnNames = {getMessageFromBundle("description"), getMessageFromBundle("date")};
        int rowCount = 2;
        schoolDaysModel = new DateConversionTableModel(columnNames, rowCount);

        schoolDaysModel.setValueAt(getMessageFromBundle("school"), 0, 0);
        schoolDaysModel.setValueAt("12.01.2024", 0, 1);

        schoolDaysTable.setModel(schoolDaysModel);

        holidaysModel = new DateConversionTableModel(columnNames, rowCount);

        holidaysModel.setValueAt(getMessageFromBundle("new.year"), 0, 0);
        holidaysModel.setValueAt("01.01.2024", 0, 1);

        holidaysTable.setModel(holidaysModel);

        String[] columnNames2 = {getMessageFromBundle("tasks")};
        int rowCount2 = 10;
        DefaultTableModel tasksModel = new DefaultTableModel(columnNames2, rowCount2);

        tasksTable.setModel(tasksModel);

        tasksModel.setValueAt(getMessageFromBundle("task1"), 0, 0);
        tasksModel.setValueAt(getMessageFromBundle("task2"), 1, 0);
        tasksModel.setValueAt(getMessageFromBundle("task3"), 2, 0);
        tasksModel.setValueAt(getMessageFromBundle("task4"), 3, 0);
        tasksModel.setValueAt(getMessageFromBundle("task5"), 4, 0);
        tasksModel.setValueAt(getMessageFromBundle("task6"), 5, 0);
        tasksModel.setValueAt(getMessageFromBundle("task7"), 6, 0);
        tasksModel.setValueAt(getMessageFromBundle("task8"), 7, 0);
        tasksModel.setValueAt(getMessageFromBundle("task9"), 8, 0);
        tasksModel.setValueAt(getMessageFromBundle("task10"), 9, 0);

        menuBar = new JMenuBar();

        importMenu = new JMenu(getMessageFromBundle("import"));
        importSubmenu1 = new JMenuItem(getMessageFromBundle("schooldays"));
        importSubmenu2 = new JMenuItem(getMessageFromBundle("holidays"));
        importSubmenu3 = new JMenuItem(getMessageFromBundle("tasks"));
        importMenu.add(importSubmenu1);
        importMenu.add(importSubmenu2);
        importMenu.add(importSubmenu3);

        exportMenu = new JMenu(getMessageFromBundle("export"));
        exportSubmenu1 = new JMenuItem(getMessageFromBundle("schooldays"));
        exportSubmenu2 = new JMenuItem(getMessageFromBundle("holidays"));
        exportSubmenu3 = new JMenuItem(getMessageFromBundle("tasks"));
        exportMenu.add(exportSubmenu1);
        exportMenu.add(exportSubmenu2);
        exportMenu.add(exportSubmenu3);

//        settingsMenu = new JMenu("Settings");
//        settingsSubmenu1 = new JMenuItem("Settings");
//        settingsMenu.add(settingsSubmenu1);

        menuBar.add(importMenu);
        menuBar.add(exportMenu);
//        menuBar.add(settingsMenu);

        apprenticeshipYearSpinnerModel = new SpinnerNumberModel(1, 1, 50, 1);
        hiringMonthSpinnerModel = new SpinnerNumberModel(8, 1, 12, 1);
        minDailyTasksSpinnerModel = new SpinnerNumberModel(7, 1, 50, 1);
        maxDailyTasksSpinnerModel = new SpinnerNumberModel(10, 1, 50, 1);
        startPageSpinnerModel = new SpinnerNumberModel(1, 1, 9999, 1);
        workingHoursSpinnerModel = new SpinnerNumberModel(8, 1, 24, 1);


        apprenticeshipYearSpinner.setModel(apprenticeshipYearSpinnerModel);
        hiringMonthSpinner.setModel(hiringMonthSpinnerModel);
        minDailyTasksSpinner.setModel(minDailyTasksSpinnerModel);
        maxDailyTasksSpinner.setModel(maxDailyTasksSpinnerModel);
        startPageSpinner.setModel(startPageSpinnerModel);
        workingHoursSpinner.setModel(workingHoursSpinnerModel);


        ((JSpinner.DefaultEditor) apprenticeshipYearSpinner.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) hiringMonthSpinner.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) minDailyTasksSpinner.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) maxDailyTasksSpinner.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) startPageSpinner.getEditor()).getTextField().setEditable(false);
        ((JSpinner.DefaultEditor) workingHoursSpinner.getEditor()).getTextField().setEditable(false);


        exportSubmenu1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("csv.files"), "csv");
                chooser.setFileFilter(filter);
                int result = chooser.showSaveDialog(exportSubmenu1);
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
                Vector schoolDaysVector = schoolDaysModel.getDataVector();
                try (PrintWriter writer = new PrintWriter(file + ".csv")) {
                    for (int index = 0; index < schoolDaysVector.size(); index++) {
                        if (((Vector) schoolDaysVector.elementAt(index)).elementAt(1) != null) {
                            String schoolDayDescription = (String) ((Vector) schoolDaysVector.elementAt(index)).elementAt(0);
                            String schoolDayDate = dateFormat.format((Date) ((Vector) schoolDaysVector.elementAt(index)).elementAt(1));
                            writer.write(schoolDayDescription + ";" + schoolDayDate + "\n");
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        exportSubmenu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("csv.files"), "csv");
                chooser.setFileFilter(filter);
                int result = chooser.showSaveDialog(exportSubmenu2);
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
                Vector holidaysVector = holidaysModel.getDataVector();
                try (PrintWriter writer = new PrintWriter(file + ".csv")) {
                    for (int index = 0; index < holidaysVector.size(); index++) {
                        if (((Vector) holidaysVector.elementAt(index)).elementAt(1) != null) {
                            String holidayDescription = (String) ((Vector) holidaysVector.elementAt(index)).elementAt(0);
                            String holidayDate = dateFormat.format((Date) ((Vector) holidaysVector.elementAt(index)).elementAt(1));
                            writer.write(holidayDescription + ";" + holidayDate + "\n");
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        exportSubmenu3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("csv.files"), "csv");
                chooser.setFileFilter(filter);
                int result = chooser.showSaveDialog(exportSubmenu3);
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
                Vector tasksVector = tasksModel.getDataVector();
                try (PrintWriter writer = new PrintWriter(file + ".csv")) {
                    for (int index = 0; index < tasksVector.size(); index++) {
                        if (((Vector) tasksVector.elementAt(index)).elementAt(0) != null) {
                            String tasksDescription = (String) ((Vector) tasksVector.elementAt(index)).elementAt(0);
                            writer.write(tasksDescription + "\n");
                        }
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        importSubmenu1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("importSubmenu1");
                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("file.format.information"), JOptionPane.INFORMATION_MESSAGE);
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("csv.files"), "csv");
                chooser.setFileFilter(filter);
                int result = chooser.showOpenDialog(importSubmenu1);
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(";");
                        if (parts.length == 2) {
                            String schoolDayDescription = parts[0];
                            Date schoolDayDate = dateFormat.parse(parts[1]);
                            Vector row = new Vector();
                            row.add(schoolDayDescription);
                            row.add(schoolDayDate);
                            schoolDaysModel.addRow(row);
                        }
                    }
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
        importSubmenu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("importSubmenu2");
                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("file.format.information"), JOptionPane.INFORMATION_MESSAGE);
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("csv.files"), "csv");
                chooser.setFileFilter(filter);
                int result = chooser.showOpenDialog(importSubmenu2);
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(";");
                        if (parts.length == 2) {
                            String holidayDescription = parts[0];
                            Date holidayDate = dateFormat.parse(parts[1]);
                            Vector row = new Vector();
                            row.add(holidayDescription);
                            row.add(holidayDate);
                            holidaysModel.addRow(row);
                        }
                    }
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });
        importSubmenu3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("importSubmenu3");
                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("file.format.information"), JOptionPane.INFORMATION_MESSAGE);
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("csv.files"), "csv");
                chooser.setFileFilter(filter);
                int result = chooser.showOpenDialog(importSubmenu3);
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Vector row = new Vector();
                        row.add(line);
                        tasksModel.addRow(row);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
//        settingsSubmenu1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                settingsFrame.setVisible(true);
//            }
//        });
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, getMessageFromBundle("generating.and.saving.the.file.can.take.several.seconds.you.will.be.notified.once.the.process.is.finished"), getMessageFromBundle("generator.information"), JOptionPane.INFORMATION_MESSAGE);


                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String fullName = firstName + " " + lastName;
                String job = jobTextField.getText();
                int apprenticeshipYear = (int) apprenticeshipYearSpinner.getValue();
                int minDailyTasks = (int) minDailyTasksSpinner.getValue();
                int maxDailyTasks = (int) maxDailyTasksSpinner.getValue();
                String chooseATemplateText = chooseATemplateTextField.getText();
                int hiringMonth = (int) hiringMonthSpinner.getValue();
                int startPage = (int) startPageSpinner.getValue();
                int workingHours = (int) workingHoursSpinner.getValue();

                try {
                    if (!Files.exists(Paths.get(chooseATemplateText))) {
                        chooseATemplateText = "/template.docx";
                        JOptionPane.showMessageDialog(null, getMessageFromBundle("template.file.not.found.the.default.template.will.be.used"), getMessageFromBundle("info"), JOptionPane.INFORMATION_MESSAGE);

                    }
                } catch (Exception ex) {
                    chooseATemplateText = "/template.docx";
                    JOptionPane.showMessageDialog(null, getMessageFromBundle("template.file.not.found.the.default.template.will.be.used"), getMessageFromBundle("info"), JOptionPane.INFORMATION_MESSAGE);
                }

                HashMap<Date, String> schoolDaysMap = new HashMap<>();
                for (int i = 0; i < schoolDaysModel.getRowCount(); i++) {
                    String description = (String) schoolDaysModel.getValueAt(i, 0);
                    String dateString = (String) schoolDaysModel.getValueAt(i, 1);
                    if (dateString != null) {
                        Date date = null;
                        try {
                            date = dateFormat.parse(dateString);
                            schoolDaysMap.put(date, description);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                HashMap<Date, String> holidaysMap = new HashMap<>();
                for (int i = 0; i < holidaysModel.getRowCount(); i++) {
                    String description = (String) holidaysModel.getValueAt(i, 0);
                    String dateString = (String) holidaysModel.getValueAt(i, 1);
                    if (dateString != null) {
                        Date date = null;
                        try {
                            date = dateFormat.parse(dateString);
                            holidaysMap.put(date, description);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                ArrayList<String> tasksArrayList = new ArrayList<>();
                for (int i = 0; i < tasksModel.getRowCount(); i++) {
                    String task = (String) tasksModel.getValueAt(i, 0);
                    if (task != null) {
                        tasksArrayList.add(task);
                    }
                }

                if (tasksArrayList.size() < maxDailyTasks) {
                    JOptionPane.showMessageDialog(null, MessageFormat.format(getMessageFromBundle("error.you.need.to.add.at.least.0.tasks.to.the.tasks.table"), maxDailyTasks), getMessageFromBundle("error"), JOptionPane.ERROR_MESSAGE);
                    return;
                }

                HashMap<Integer, Boolean> workDaysCheckBoxStates = new HashMap<>(); // Counting: see PLACEHOLDERS
                workDaysCheckBoxStates.put(11, mondayCheckBox.isSelected());
                workDaysCheckBoxStates.put(12, tuesdayCheckBox.isSelected());
                workDaysCheckBoxStates.put(13, wednesdayCheckBox.isSelected());
                workDaysCheckBoxStates.put(14, thursdayCheckBox.isSelected());
                workDaysCheckBoxStates.put(15, fridayCheckBox.isSelected());
                workDaysCheckBoxStates.put(16, saturdayCheckBox.isSelected());
                workDaysCheckBoxStates.put(17, sundayCheckBox.isSelected());

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

                File outputFile;
                try {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("docx.files"), "docx");
                    chooser.setFileFilter(filter);
                    int result = chooser.showSaveDialog(generateButton);
                    if (result == chooser.APPROVE_OPTION) {
                        outputFile = chooser.getSelectedFile();
                    } else {
                        return;
                    }
                } catch (HeadlessException ex) {
                    throw new RuntimeException(ex);
                }

                String templatePathString = chooseATemplateText;

                List<byte[]> documentBytesList = new ArrayList<>();

                int prevMonth = lastDayOfWeekOfFromDate.getMonthValue();

                int page = startPage;

                for (int i = 0; i < amountWeeks; i++) {

                    int firstDay = firstDayOfWeekOfFromDate.getDayOfMonth();
                    int firstMonth = firstDayOfWeekOfFromDate.getMonthValue();
                    int firstYear = firstDayOfWeekOfFromDate.getYear();

                    int lastDay = lastDayOfWeekOfFromDate.getDayOfMonth();
                    int lastMonth = lastDayOfWeekOfFromDate.getMonthValue();
                    int lastYear = lastDayOfWeekOfFromDate.getYear();

                    if (lastMonth == hiringMonth && prevMonth == hiringMonth - 1) {
                        apprenticeshipYear++;
                    }
                    prevMonth = lastMonth;

                    HashMap<String, String> replacements = new HashMap<>();
                    replacements.put(PLACEHOLDERS[0], fullName);
                    replacements.put(PLACEHOLDERS[1], job);
                    replacements.put(PLACEHOLDERS[2], String.valueOf(apprenticeshipYear));
                    replacements.put(PLACEHOLDERS[3], String.valueOf(firstDay));
                    replacements.put(PLACEHOLDERS[4], String.valueOf(firstMonth));
                    replacements.put(PLACEHOLDERS[5], String.valueOf(firstYear));
                    replacements.put(PLACEHOLDERS[6], String.valueOf(lastDay));
                    replacements.put(PLACEHOLDERS[7], String.valueOf(lastMonth));
                    replacements.put(PLACEHOLDERS[8], String.valueOf(lastYear));
                    replacements.put(PLACEHOLDERS[9], String.valueOf(lastYear));
                    replacements.put(PLACEHOLDERS[10], String.valueOf(page));
                    replacements.put(PLACEHOLDERS[11], "");
                    replacements.put(PLACEHOLDERS[12], "");
                    replacements.put(PLACEHOLDERS[13], "");
                    replacements.put(PLACEHOLDERS[14], "");
                    replacements.put(PLACEHOLDERS[15], "");
                    replacements.put(PLACEHOLDERS[16], "");
                    replacements.put(PLACEHOLDERS[17], "");
                    replacements.put(PLACEHOLDERS[18], "");
                    replacements.put(PLACEHOLDERS[19], "");
                    replacements.put(PLACEHOLDERS[20], "");
                    replacements.put(PLACEHOLDERS[21], "");
                    replacements.put(PLACEHOLDERS[22], "");
                    replacements.put(PLACEHOLDERS[23], "");
                    replacements.put(PLACEHOLDERS[24], "");

                    page++;

                    int randomDailyTasks;

                    LocalDate localTempDate = firstDayOfWeekOfFromDate;
                    Date tempDate = Date.from(localTempDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    for (int weekdayCounter = 11; weekdayCounter < 18; weekdayCounter++) { // Counting: see PLACEHOLDERS
                        boolean isDateInRange = !localTempDate.isBefore(fromDateLocal) && !localTempDate.isAfter(toDateLocal);
                        boolean isWorkday = workDaysCheckBoxStates.get(weekdayCounter);
                        boolean isHoliday = holidaysMap.get(tempDate) != null;
                        boolean isSchoolDay = schoolDaysMap.get(tempDate) != null;

                        if (isDateInRange && isWorkday && !isHoliday && !isSchoolDay) {
                            replacements.put(PLACEHOLDERS[weekdayCounter + 7], String.valueOf(workingHours));
                        }

                        String placeholderBase = PLACEHOLDERS[weekdayCounter].substring(0, PLACEHOLDERS[weekdayCounter].length() - 1);
                        for (int i3 = 1; i3 < maxDailyTasks + 1; i3++) {
                            replacements.put(placeholderBase + i3 + "}", "");
                        }

                        if (isDateInRange && isHoliday) {
                            replacements.put(placeholderBase + "1}", holidaysMap.get(tempDate));
                        }

                        if (isDateInRange && !isHoliday && isSchoolDay) {
                            replacements.put(placeholderBase + "1}", schoolDaysMap.get(tempDate));
                            replacements.put(PLACEHOLDERS[weekdayCounter + 7], String.valueOf(workingHours));
                        }

                        Collections.shuffle(tasksArrayList);
                        randomDailyTasks = ThreadLocalRandom.current().nextInt(minDailyTasks, maxDailyTasks + 1);
                        for (int i3 = 1; i3 < randomDailyTasks + 1; i3++) {
                            if (isDateInRange && isWorkday && !isHoliday && !isSchoolDay) {
                                replacements.put(placeholderBase + i3 + "}", tasksArrayList.get(i3 - 1));
                            }
                        }

                        localTempDate = localTempDate.plusDays(1);
                        tempDate = Date.from(localTempDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }

                    firstDayOfWeekOfFromDate = firstDayOfWeekOfFromDate.plusWeeks(1);
                    lastDayOfWeekOfFromDate = lastDayOfWeekOfFromDate.plusWeeks(1);

                    URL resource = getClass().getResource(templatePathString);
                    if (resource != null) {
                        try (InputStream in = getClass().getResourceAsStream(templatePathString); XWPFDocument doc = new XWPFDocument(in)) {

                            try (XWPFDocument docAfterReplace = replaceTextAndAddSectionBreak(doc, replacements);
                                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                                docAfterReplace.write(out);
                                documentBytesList.add(out.toByteArray());
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        Path templatePath = Paths.get(templatePathString);

                        try (InputStream in = Files.newInputStream(templatePath); XWPFDocument doc = new XWPFDocument(in)) {

                            try (XWPFDocument docAfterReplace = replaceTextAndAddSectionBreak(doc, replacements);
                                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                                docAfterReplace.write(out);
                                documentBytesList.add(out.toByteArray());
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

                mergeDocuments(documentBytesList, outputFile.getAbsolutePath() + ".docx"); // inspiration from: https://stackoverflow.com/a/48535550
                JOptionPane.showMessageDialog(null, getMessageFromBundle("file.saved"), getMessageFromBundle("file.information"), JOptionPane.INFORMATION_MESSAGE);
                documentBytesList.clear();


            }
        });
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder messageBuilder = new StringBuilder(getMessageFromBundle("chooseFileButton"));
                for (String placeholder : Main.PLACEHOLDERS) {
                    messageBuilder.append(placeholder).append("\n");
                }
                messageBuilder.append(getMessageFromBundle("chooseFileButton1"));
                String message = messageBuilder.toString();

                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("file.format.information"), JOptionPane.INFORMATION_MESSAGE);
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("docx.files"), "docx");
                chooser.setFileFilter(filter);
                int result = chooser.showOpenDialog(chooseFileButton);
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                } else {
                    return;
                }
                chooseATemplateTextField.setText(file.getAbsolutePath());
            }
        });
        schoolDaysPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schoolDaysModel.addRow(new Object[]{});
            }
        });
        holidaysPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                holidaysModel.addRow(new Object[]{});
            }
        });
        tasksPlusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tasksModel.addRow(new Object[]{});
            }
        });
        schoolDaysMinusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = schoolDaysTable.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) schoolDaysTable.getModel();
                    model.removeRow(selectedRow);
                }
            }
        });
        holidaysMinusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = holidaysTable.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) holidaysTable.getModel();
                    model.removeRow(selectedRow);
                }
            }
        });
        tasksMinusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tasksTable.getSelectedRow();
                if (selectedRow != -1) {
                    DefaultTableModel model = (DefaultTableModel) tasksTable.getModel();
                    model.removeRow(selectedRow);
                }
            }
        });
        generateSchooldaysButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                schooldaysGeneratorFrame.setVisible(true);
            }
        });
        generateFromICSFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file;
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(getMessageFromBundle("ics.files"), "ics");
                chooser.setFileFilter(filter);
                int result = chooser.showOpenDialog(importSubmenu3);
                String input = null;
                if (result == chooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                    try {
                        input = new String(Files.readAllBytes(file.toPath()));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, getMessageFromBundle("error.reading.file"), getMessageFromBundle("error"), JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    return;
                }

                HashMap<Date, String> map = new HashMap<>();

                Pattern pattern1 = Pattern.compile("DTSTART;VALUE=DATE:(\\d+)");
                Pattern pattern2 = Pattern.compile("SUMMARY:([^\\n]*)");

                Matcher matcher1 = pattern1.matcher(input);
                Matcher matcher2 = pattern2.matcher(input);

                List<Date> dates = new ArrayList<>();
                while (matcher1.find()) {
                    try {
                        dates.add(wrongDateFormat.parse(matcher1.group(1)));
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                List<String> summaries = new ArrayList<>();
                while (matcher2.find()) {
                    summaries.add(matcher2.group(1));
                }

                if (dates.size() != summaries.size()) {
                    JOptionPane.showMessageDialog(null, getMessageFromBundle("mismatch.between.number.of.dates.and.summaries"), getMessageFromBundle("error"), JOptionPane.ERROR_MESSAGE);
                } else {
                    for (int i = 0; i < dates.size(); i++) {
                        map.put(dates.get(i), summaries.get(i));
                    }
                }

                for (Map.Entry<Date, String> entry : map.entrySet()) {
                    Main.addHoliday(entry.getValue(), dateFormat.format(entry.getKey()));
                }
            }
        });
        dailyTasksHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("dailyTasksHelpButton");
                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("information"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        generateFromICSFileHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("generateFromICSFileHelpButton");
                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("help"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        apprenticeshipYearHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("apprenticeshipYearHelpButton");

                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("apprenticeship.year.information"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        hiringMonthHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("hiringMonthHelpButton");

                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("hiring.month.information"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        tasksHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("tasksHelpButton");

                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("tasks.table.information"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        schoolDaysHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("schoolDaysHelpButton");

                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("schooldays.table.information"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
        holidaysHelpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = getMessageFromBundle("holidaysHelpButton");

                JOptionPane.showMessageDialog(null, message, getMessageFromBundle("holidays.table.information"), JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static String getMessageFromBundle(String key) {
        String path = "languages";

        bundle = ResourceBundle.getBundle(path);

        return bundle.getString(key);
    }

    public static void addSchoolday(String description, String date) {
        int currentRowCount = schoolDaysModel.getRowCount();
        schoolDaysModel.setRowCount(currentRowCount + 1);
        schoolDaysModel.setValueAt(description, currentRowCount, 0);
        schoolDaysModel.setValueAt(date, currentRowCount, 1);
    }

    public static void addHoliday(String description, String date) {
        int currentRowCount = holidaysModel.getRowCount();
        holidaysModel.setRowCount(currentRowCount + 1);
        holidaysModel.setValueAt(description, currentRowCount, 0);
        holidaysModel.setValueAt(date, currentRowCount, 1);
    }

    public void mergeDocuments(List<byte[]> documentBytesList, String outputPathString) {
        WordprocessingMLPackage mainPackage = loadPackage(documentBytesList.get(0));

        for (int i = 1; i < documentBytesList.size(); i++) {
            WordprocessingMLPackage secondaryPackage = loadPackage(documentBytesList.get(i));
            mergePackages(mainPackage, secondaryPackage);
        }

        savePackage(mainPackage, outputPathString);
    }

    private WordprocessingMLPackage loadPackage(byte[] documentBytes) {
        try (InputStream is = new ByteArrayInputStream(documentBytes)) {
            return WordprocessingMLPackage.load(is);
        } catch (Docx4JException | IOException ex) {
            throw new RuntimeException("Failed to load package", ex);
        }
    }

    private void mergePackages(WordprocessingMLPackage mainPackage, WordprocessingMLPackage secondaryPackage) {
        List<Object> bodies = getJAXBNodes(secondaryPackage, "//w:body");
        for (Object body : bodies) {
            List<Object> content = ((Body) body).getContent();
            for (Object item : content) {
                mainPackage.getMainDocumentPart().addObject(item);
            }
        }

        List<Object> blips = getJAXBNodes(secondaryPackage, "//a:blip");
        for (Object blip : blips) {
            processBlip(mainPackage, secondaryPackage, (CTBlip) blip);
        }
    }

    private List<Object> getJAXBNodes(WordprocessingMLPackage pkg, String xPath) {
        try {
            return pkg.getMainDocumentPart().getJAXBNodesViaXPath(xPath, false);
        } catch (JAXBException | XPathBinderAssociationIsPartialException ex) {
            throw new RuntimeException("Failed to get JAXB nodes", ex);
        }
    }

    private void processBlip(WordprocessingMLPackage mainPackage, WordprocessingMLPackage secondaryPackage, CTBlip blip) {
        try {
            RelationshipsPart parts = secondaryPackage.getMainDocumentPart().getRelationshipsPart();
            Relationship rel = parts.getRelationshipByID(blip.getEmbed());
            Part part = parts.getPart(rel);

            printBytesIfImagePart(part);

            Relationship newRel = mainPackage.getMainDocumentPart().addTargetPart(part, RelationshipsPart.AddPartBehaviour.RENAME_IF_NAME_EXISTS);
            blip.setEmbed(newRel.getId());
            mainPackage.getMainDocumentPart().addTargetPart(secondaryPackage.getParts().getParts().get(new PartName("/word/" + rel.getTarget())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printBytesIfImagePart(Part part) {
        if (part instanceof ImagePngPart)
            System.out.println(((ImagePngPart) part).getBytes());
        if (part instanceof ImageJpegPart)
            System.out.println(((ImageJpegPart) part).getBytes());
        if (part instanceof ImageBmpPart)
            System.out.println(((ImageBmpPart) part).getBytes());
        if (part instanceof ImageGifPart)
            System.out.println(((ImageGifPart) part).getBytes());
        if (part instanceof ImageEpsPart)
            System.out.println(((ImageEpsPart) part).getBytes());
        if (part instanceof ImageTiffPart)
            System.out.println(((ImageTiffPart) part).getBytes());
    }

    private void savePackage(WordprocessingMLPackage pkg, String outputPathString) {
        File saved = new File(outputPathString);
        try {
            pkg.save(saved);
        } catch (Docx4JException ex) {
            throw new RuntimeException("Failed to save package", ex);
        }
    }

    private XWPFDocument replaceTextAndAddSectionBreak(XWPFDocument doc, HashMap<String, String> replacements) {
        CTBody body = doc.getDocument().getBody();
        CTSectPr sectPr = body.getSectPr();
        if (sectPr != null) {
            // https://stackoverflow.com/a/58526872
            XWPFParagraph lastParagraph = doc.createParagraph();
            lastParagraph.getCTP().addNewPPr().setSectPr(sectPr);
            body.unsetSectPr();
        }
        replaceTextInParagraphs(doc.getParagraphs(), replacements);
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    replaceTextInParagraphs(cell.getParagraphs(), replacements);
                }
            }
        }
        return doc;
    }

    private void replaceTextInParagraphs(List<XWPFParagraph> paragraphs, HashMap<String, String> replacements) {
        paragraphs.forEach(paragraph -> replaceTextInParagraph(paragraph, replacements));
    }

    private void replaceTextInParagraph(XWPFParagraph paragraph, HashMap<String, String> replacements) {
    /* https://github.com/eugenp/tutorials/blob/6ada5de93ea5b609d97bf31ae7f5e2d718cdbc8f/apache-poi-2/src/main/java/com/baeldung/poi/replacevariables/DocxTextReplacer.java
    Thank you baeldung.com    */
        String paragraphText = paragraph.getParagraphText();
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String originalText = entry.getKey();
            String updatedText = entry.getValue();
            if (paragraphText.contains(originalText)) {
                paragraphText = paragraphText.replace(originalText, updatedText);

                while (paragraph.getRuns().size() > 0) {
                    paragraph.removeRun(0);
                }
                XWPFRun newRun = paragraph.createRun();
                newRun.setText(paragraphText);
            }
        }

    }

    public static void main(String[] args) {
        try {
            Object[] possibleValues = {"German", "English"};
            Object selectedValue = JOptionPane.showInputDialog(null,
                    "Language / Sprache", "Input",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    possibleValues, possibleValues[0]);

            if (selectedValue.equals("English")) {
                Locale.setDefault(new Locale("en", "US"));
            } else if (selectedValue.equals("German")) {
                Locale.setDefault(new Locale("de", "DE"));
            }
        } catch (NullPointerException e) {
            System.out.println("No language selected, closing the program.");
            System.exit(0);
        }

        String message = getMessageFromBundle("welcome.text");
        JOptionPane.showMessageDialog(null, message, getMessageFromBundle("welcome"), JOptionPane.INFORMATION_MESSAGE);

        JFrame mainFrame = new JFrame("Easy Berichtsheft Generator");

        mainFrame.setContentPane(new Main().MainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.setJMenuBar(menuBar);

        mainFrame.pack();
        mainFrame.setVisible(true);

        schooldaysGeneratorFrame = new JFrame(getMessageFromBundle("schooldays.generator"));
        schooldaysGeneratorFrame.setContentPane(new SchooldaysGenerator().schooldaysGeneratorPanel);
        schooldaysGeneratorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        schooldaysGeneratorFrame.pack();

//        settingsFrame = new JFrame("Settings");
//        settingsFrame.setContentPane(new Settings().settingsPanel);
//        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        settingsFrame.pack();
    }

    static class DateConversionTableModel extends DefaultTableModel {


        public DateConversionTableModel(String[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            if (value instanceof String && column == 1) {
                String dateString = (String) value;
                try {
                    Date date = dateFormat.parse(dateString);
                    super.setValueAt(date, row, column);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, getMessageFromBundle("invalid.date.format.please.enter.date.as.dd.mm.yyyy"), getMessageFromBundle("error"), JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                super.setValueAt(value, row, column);
            }
        }

        public Object getValueAt(int row, int column) {
            if (column == 1) {
                Object dateObject = super.getValueAt(row, column);
                if (dateObject instanceof Date) {
                    // Format the date using SimpleDateFormat before returning
                    return dateFormat.format((Date) dateObject);
                }
            }
            return super.getValueAt(row, column);
        }
    }

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
        MainPanel = new JPanel();
        MainPanel.setLayout(new GridBagLayout());
        paneSchoolDays = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.gridheight = 18;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        MainPanel.add(paneSchoolDays, gbc);
        schoolDaysTable = new JTable();
        schoolDaysTable.setPreferredScrollableViewportSize(new Dimension(0, 0));
        paneSchoolDays.setViewportView(schoolDaysTable);
        paneHolidays = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 23;
        gbc.gridwidth = 6;
        gbc.fill = GridBagConstraints.BOTH;
        MainPanel.add(paneHolidays, gbc);
        holidaysTable = new JTable();
        holidaysTable.setPreferredScrollableViewportSize(new Dimension(0, 0));
        paneHolidays.setViewportView(holidaysTable);
        firstNameLabel = new JLabel();
        this.$$$loadLabelText$$$(firstNameLabel, this.$$$getMessageFromBundle$$$("languages", "first.name"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 20;
        MainPanel.add(firstNameLabel, gbc);
        firstNameTextField = new JTextField();
        firstNameTextField.setText(this.$$$getMessageFromBundle$$$("languages", "first.name1"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 60;
        MainPanel.add(firstNameTextField, gbc);
        lastNameLabel = new JLabel();
        this.$$$loadLabelText$$$(lastNameLabel, this.$$$getMessageFromBundle$$$("languages", "last.name"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 20;
        MainPanel.add(lastNameLabel, gbc);
        lastNameTextField = new JTextField();
        lastNameTextField.setText(this.$$$getMessageFromBundle$$$("languages", "last.name1"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(lastNameTextField, gbc);
        jobLabel = new JLabel();
        this.$$$loadLabelText$$$(jobLabel, this.$$$getMessageFromBundle$$$("languages", "job"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 20;
        MainPanel.add(jobLabel, gbc);
        jobTextField = new JTextField();
        jobTextField.setText(this.$$$getMessageFromBundle$$$("languages", "job1"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(jobTextField, gbc);
        schoolDaysPlusButton = new JButton();
        schoolDaysPlusButton.setText("+");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 20;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(schoolDaysPlusButton, gbc);
        schoolDaysMinusButton = new JButton();
        schoolDaysMinusButton.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 20;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(schoolDaysMinusButton, gbc);
        holidaysMinusButton = new JButton();
        holidaysMinusButton.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 24;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(holidaysMinusButton, gbc);
        generateButton = new JButton();
        this.$$$loadButtonText$$$(generateButton, this.$$$getMessageFromBundle$$$("languages", "generate"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 28;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(generateButton, gbc);
        holidaysPlusButton = new JButton();
        holidaysPlusButton.setText("+");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(holidaysPlusButton, gbc);
        schoolDaysLabel = new JLabel();
        this.$$$loadLabelText$$$(schoolDaysLabel, this.$$$getMessageFromBundle$$$("languages", "schooldays"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(schoolDaysLabel, gbc);
        holidaysLabel = new JLabel();
        this.$$$loadLabelText$$$(holidaysLabel, this.$$$getMessageFromBundle$$$("languages", "holidays"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(holidaysLabel, gbc);
        paneTasks = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 23;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        MainPanel.add(paneTasks, gbc);
        tasksTable = new JTable();
        tasksTable.setPreferredScrollableViewportSize(new Dimension(450, 100));
        paneTasks.setViewportView(tasksTable);
        tasksPlusButton = new JButton();
        tasksPlusButton.setText("+");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(tasksPlusButton, gbc);
        tasksMinusButton = new JButton();
        tasksMinusButton.setText("-");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(tasksMinusButton, gbc);
        tasksLabel = new JLabel();
        this.$$$loadLabelText$$$(tasksLabel, this.$$$getMessageFromBundle$$$("languages", "tasks"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 21;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(tasksLabel, gbc);
        workDaysLabel = new JLabel();
        this.$$$loadLabelText$$$(workDaysLabel, this.$$$getMessageFromBundle$$$("languages", "workdays"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(workDaysLabel, gbc);
        mondayCheckBox = new JCheckBox();
        mondayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(mondayCheckBox, this.$$$getMessageFromBundle$$$("languages", "monday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(mondayCheckBox, gbc);
        tuesdayCheckBox = new JCheckBox();
        tuesdayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(tuesdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "tuesday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(tuesdayCheckBox, gbc);
        wednesdayCheckBox = new JCheckBox();
        wednesdayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(wednesdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "wednesday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(wednesdayCheckBox, gbc);
        thursdayCheckBox = new JCheckBox();
        thursdayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(thursdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "thursday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(thursdayCheckBox, gbc);
        fridayCheckBox = new JCheckBox();
        fridayCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(fridayCheckBox, this.$$$getMessageFromBundle$$$("languages", "friday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(fridayCheckBox, gbc);
        saturdayCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(saturdayCheckBox, this.$$$getMessageFromBundle$$$("languages", "saturday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(saturdayCheckBox, gbc);
        sundayCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(sundayCheckBox, this.$$$getMessageFromBundle$$$("languages", "sunday"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(sundayCheckBox, gbc);
        templateLabel = new JLabel();
        this.$$$loadLabelText$$$(templateLabel, this.$$$getMessageFromBundle$$$("languages", "template"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(templateLabel, gbc);
        chooseATemplateTextField = new JTextField();
        chooseATemplateTextField.setEditable(false);
        chooseATemplateTextField.setText(this.$$$getMessageFromBundle$$$("languages", "choose.a.template"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(chooseATemplateTextField, gbc);
        chooseFileButton = new JButton();
        this.$$$loadButtonText$$$(chooseFileButton, this.$$$getMessageFromBundle$$$("languages", "choose.file"));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(chooseFileButton, gbc);
        generateSchooldaysButton = new JButton();
        this.$$$loadButtonText$$$(generateSchooldaysButton, this.$$$getMessageFromBundle$$$("languages", "generate.schooldays"));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 20;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(generateSchooldaysButton, gbc);
        generateFromICSFileButton = new JButton();
        this.$$$loadButtonText$$$(generateFromICSFileButton, this.$$$getMessageFromBundle$$$("languages", "generate.from.ics.ical.file"));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 24;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(generateFromICSFileButton, gbc);
        fromDateLabel = new JLabel();
        this.$$$loadLabelText$$$(fromDateLabel, this.$$$getMessageFromBundle$$$("languages", "from.date"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 26;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(fromDateLabel, gbc);
        fromDateFormattedTextField = new JFormattedTextField();
        fromDateFormattedTextField.setFocusLostBehavior(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 26;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(fromDateFormattedTextField, gbc);
        toDateLabel = new JLabel();
        this.$$$loadLabelText$$$(toDateLabel, this.$$$getMessageFromBundle$$$("languages", "to.date"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 27;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(toDateLabel, gbc);
        toDateFormattedTextField = new JFormattedTextField();
        toDateFormattedTextField.setFocusLostBehavior(0);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 27;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(toDateFormattedTextField, gbc);
        generationPeriodLabel = new JLabel();
        this.$$$loadLabelText$$$(generationPeriodLabel, this.$$$getMessageFromBundle$$$("languages", "generation.period"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 25;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(generationPeriodLabel, gbc);
        apprenticeshipYearLabel = new JLabel();
        this.$$$loadLabelText$$$(apprenticeshipYearLabel, this.$$$getMessageFromBundle$$$("languages", "apprenticeship.year"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 26;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.ipadx = 10;
        MainPanel.add(apprenticeshipYearLabel, gbc);
        apprenticeshipYearSpinner = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 26;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        MainPanel.add(apprenticeshipYearSpinner, gbc);
        apprenticeshipYearHelpButton = new JButton();
        apprenticeshipYearHelpButton.setText("?");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 26;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(apprenticeshipYearHelpButton, gbc);
        hiringMonthLabel = new JLabel();
        this.$$$loadLabelText$$$(hiringMonthLabel, this.$$$getMessageFromBundle$$$("languages", "hiring.month"));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 27;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.ipadx = 10;
        MainPanel.add(hiringMonthLabel, gbc);
        hiringMonthSpinner = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 27;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(hiringMonthSpinner, gbc);
        hiringMonthHelpButton = new JButton();
        hiringMonthHelpButton.setText("?");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 27;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(hiringMonthHelpButton, gbc);
        minDailyTasksLabel = new JLabel();
        this.$$$loadLabelText$$$(minDailyTasksLabel, this.$$$getMessageFromBundle$$$("languages", "min.daily.tasks"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(minDailyTasksLabel, gbc);
        minDailyTasksSpinner = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 15;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(minDailyTasksSpinner, gbc);
        maxDailyTasksSpinner = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(maxDailyTasksSpinner, gbc);
        maxDailyTasksLabel = new JLabel();
        this.$$$loadLabelText$$$(maxDailyTasksLabel, this.$$$getMessageFromBundle$$$("languages", "max.daily.tasks"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(maxDailyTasksLabel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.ipady = 10;
        MainPanel.add(spacer1, gbc);
        dailyTasksHelpButton = new JButton();
        dailyTasksHelpButton.setText("?");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(dailyTasksHelpButton, gbc);
        startPageLabel = new JLabel();
        this.$$$loadLabelText$$$(startPageLabel, this.$$$getMessageFromBundle$$$("languages", "start.page"));
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 26;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 10;
        MainPanel.add(startPageLabel, gbc);
        startPageSpinner = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 26;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 20;
        MainPanel.add(startPageSpinner, gbc);
        workingHoursLabel = new JLabel();
        this.$$$loadLabelText$$$(workingHoursLabel, this.$$$getMessageFromBundle$$$("languages", "working.hours"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(workingHoursLabel, gbc);
        workingHoursSpinner = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 18;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(workingHoursSpinner, gbc);
        generateFromICSFileHelpButton = new JButton();
        generateFromICSFileHelpButton.setText("?");
        gbc = new GridBagConstraints();
        gbc.gridx = 7;
        gbc.gridy = 24;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        MainPanel.add(generateFromICSFileHelpButton, gbc);
        tasksHelpButton = new JButton();
        tasksHelpButton.setText("?");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 22;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(tasksHelpButton, gbc);
        schoolDaysHelpButton = new JButton();
        schoolDaysHelpButton.setText("?");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(schoolDaysHelpButton, gbc);
        holidaysHelpButton = new JButton();
        holidaysHelpButton.setText("?");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 22;
        gbc.anchor = GridBagConstraints.WEST;
        MainPanel.add(holidaysHelpButton, gbc);
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
        return MainPanel;
    }

}
