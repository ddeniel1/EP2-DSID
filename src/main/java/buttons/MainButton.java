package buttons;

import assembler.DataAssembler;
import util.DatasetUtils;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class MainButton {

    public static Component loadYearButton(List<Integer> selectedYears) {
        return new JButton(new AbstractAction("Select Year") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Select Years");
                List<JCheckBox> checkBoxes = new ArrayList<>();
                createJBoxForAllYears(frame,checkBoxes,selectedYears);
                frame.setLayout(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setFocusable(true);
                frame.setSize(800, 300);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static Component saveButton(JFrame frame) {
        return new JButton(new AbstractAction("Save") {

            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(frame, "Test", FileDialog.SAVE);
                fd.setVisible(true);
                System.out.println(fd.getFile());
            }
        });
    }

    public static Component selectYearsButton(List<Integer> yearsToDownload, DataAssembler assembler) {
        return new JButton(new AbstractAction("Select years to download") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(yearsToDownload.size());
                JFrame selectYears = new JFrame("Select years to download");
                selectYears.requestFocus();
                Label label = new Label();
                label.setAlignment(Label.CENTER);
                label.setSize(400, 100);
                List<JCheckBox> checkboxes = new ArrayList<>();
                selectYears.setLayout(null);
                selectYears.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                selectYears.setFocusable(true);
                createJBoxForAllYears(selectYears, checkboxes, yearsToDownload);



                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Executors.newSingleThreadExecutor()
                                .execute(() -> assembler.downloadFiles(yearsToDownload));
                        selectYears.dispose();
                    }
                });
                JCheckBox checkBoxOthers = new JCheckBox("invert.", yearsToDownload.size()==88);
                checkBoxOthers.addItemListener(e1 -> {
                    if (!yearsToDownload.isEmpty() && yearsToDownload.size()<88){
                        List<Integer> allYears = new ArrayList<>();
                        addAllYears(allYears);
                        allYears.removeAll(yearsToDownload);
                        yearsToDownload.clear();
                        yearsToDownload.addAll(allYears);
                    }else addAllYears(yearsToDownload);

                });
                checkBoxOthers.setBounds(590,210,60,20);
                checkBoxOthers.setVisible(true);
                selectYears.add(checkBoxOthers);


                confirmationButton.setBounds(690,220,100,30);
                confirmationButton.setVisible(true);
                selectYears.add(confirmationButton);
                selectYears.add(label);
                selectYears.setSize(800, 300);
                selectYears.setLocationRelativeTo(null);
                selectYears.setVisible(true);
            }
        });
    }

    private static void createJBoxForAllYears(JFrame selectYears, List<JCheckBox> checkboxes, List<Integer> yearsToDownload) {
        for (int i = 1929; i <= 2016; i++) {
            Integer valueOfI = i;
            JCheckBox checkbox1 = new JCheckBox(String.valueOf(valueOfI), yearsToDownload.contains(valueOfI));
            checkbox1.addItemListener(e1 -> {
                if (yearsToDownload.contains(valueOfI)) {
                    yearsToDownload.remove(valueOfI);
                } else
                    yearsToDownload.add(valueOfI);
            });
            int x, y;
            x = 50 +(((i-1929)%10)*60);
            y = 50 + (((i-1929)/10)*20);
            checkbox1.setBounds(x, y, 60, 20);
            checkbox1.setVisible(true);
            checkboxes.add(checkbox1);
        }
        JCheckBox checkBoxAll = new JCheckBox("all", yearsToDownload.size()==88);
        checkBoxAll.addItemListener(e1 -> {
            if (!yearsToDownload.isEmpty() && yearsToDownload.size()<88){
                yearsToDownload.clear();
                addAllYears(yearsToDownload);
            }else if (yearsToDownload.size()==88){
                yearsToDownload.clear();
            }else addAllYears(yearsToDownload);

        });
        checkBoxAll.setBounds(530,210,60,20);
        checkBoxAll.setVisible(true);
        checkboxes.add(checkBoxAll);
        checkboxes.forEach(selectYears::add);
    }

    private static void addAllYears(List<Integer> yearsPaths) {
        for (int i = 1929; i <= 2016; i++) yearsPaths.add(i);
    }

    public static Component quitProgramButton(JFrame frame) {
        return new JButton(new AbstractAction("Quit") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public static Component unzipAndCompileFilesButton(DataAssembler assembler, List<Integer> yearsToDownload) {
        return new JButton(new AbstractAction("UnzipFiles") {

            @Override
            public void actionPerformed(ActionEvent e) {
                assembler.unzipAndCompileFiles(yearsToDownload);
            }
        });
    }
    public static Component checkFilesButton ( DataAssembler assembler, List<Integer> yearsToDownload) {
        return new JButton(new AbstractAction("Check Files") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (yearsToDownload.size()==0){
                    addAllYears(yearsToDownload);
                }
                yearsToDownload.removeAll(assembler.checkFiles());
            }
        });
    }

    public static Component getCountButton(DataAssembler assembler, List<Integer> years) {
        return new JButton(new AbstractAction("Obter Count sobre a coluna DEWP") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Selecionar Campos");
                frame.setLayout(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setFocusable(true);
                frame.setSize(1000, 500);
                List<String> columsToProcess = new ArrayList<>();
                List<JCheckBox> checkBoxes = new ArrayList<>();

                String[] colums = DatasetUtils.schema.fieldNames();

                addAllCollums(frame, columsToProcess, checkBoxes, colums);

                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Executors.newSingleThreadExecutor()
                                .execute(() ->assembler.processData(years, columsToProcess.toArray(String[]::new)));
                        frame.dispose();
                    }
                });


                frame.add(confirmationButton);
                confirmationButton.setBounds(900,400,120,30);
                confirmationButton.setVisible(true);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
    }

    private static void addAllCollums(JFrame frame, List<String> columsToProcess, List<JCheckBox> checkBoxes, String[] colums) {
        for (int i = 0; i < colums.length; i++) {
            String colum = colums[i];
//                    System.out.println(colum);

            JCheckBox checkbox1 = new JCheckBox(colum,false);
            checkbox1.addItemListener(e1 -> {
                if (columsToProcess.contains(colum)) {
                    columsToProcess.remove(colum);
                } else
                    columsToProcess.add(colum);
            });
            int x, y;
            x = 50 + ((i % 5) * 200);
            y = 50 + ((i / 5) * 30);
            checkbox1.setBounds(x, y, 200, 30);
            checkbox1.setVisible(true);
            checkBoxes.add(checkbox1);
        }
        JCheckBox checkBoxAll = new JCheckBox("all", columsToProcess.size()==27);
        checkBoxAll.addItemListener(e1 -> {
            if (!columsToProcess.isEmpty() && columsToProcess.size()<27){
                columsToProcess.clear();
                columsToProcess.addAll(Arrays.asList(colums));
            }else if (columsToProcess.size()==88){
                columsToProcess.clear();
            }else columsToProcess.addAll(Arrays.asList(colums));

        });
        checkBoxAll.setBounds(780,400,60,20);
        checkBoxAll.setVisible(true);
        checkBoxes.add(checkBoxAll);
        checkBoxes.forEach(frame::add);
    }
}
