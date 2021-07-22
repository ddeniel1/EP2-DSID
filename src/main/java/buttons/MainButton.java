package buttons;

import assembler.DataAssembler;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainButton {

    public static Component loadYearButton(JFrame frame, List<String> yearsPaths) {
        return new JButton(new AbstractAction("Load Year") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setVisible(true);
                fc.showOpenDialog(frame);
                yearsPaths.add(fc.getSelectedFile().toString());
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
                JFrame selectYears = new JFrame("Select years to download");
                selectYears.requestFocus();
                Label label = new Label();
                label.setAlignment(Label.CENTER);
                label.setSize(400, 100);
                List<JCheckBox> checkboxes = new ArrayList<>();
                selectYears.setLayout(null);
                selectYears.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                selectYears.setFocusable(true);
                for (int i = 1929; i <= 2016; i++) {
                    Integer valueOfI = i;
                    JCheckBox checkbox1 = new JCheckBox(String.valueOf(valueOfI),yearsToDownload.contains(valueOfI));
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

                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Executors.newSingleThreadExecutor()
                                .execute(() -> assembler.downloadFiles(yearsToDownload));
                        selectYears.dispose();
                    }
                });


                confirmationButton.setBounds(600,220,120,30);
                confirmationButton.setVisible(true);
                selectYears.add(confirmationButton);
                selectYears.add(label);
                selectYears.setSize(800, 300);
                selectYears.setVisible(true);
            }
        });
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

    public static Component checkFilesButton(JFrame frame, DataAssembler assembler, List<Integer> yearsToDownload) {
        return new JButton(new AbstractAction("Check Files") {

            @Override
            public void actionPerformed(ActionEvent e) {
                yearsToDownload.removeAll(assembler.checkFiles());
                yearsToDownload.addAll(assembler.checkFiles());
            }
        });
    }
}
