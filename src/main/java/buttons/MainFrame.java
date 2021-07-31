package buttons;

import assembler.DataAssembler;
import org.apache.spark.sql.types.StructField;
import scala.collection.Iterator;
import util.DatasetUtils;
import util.DateUtils;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class MainFrame {

    public static Component selectYearsToDownloadButton(List<Integer> yearsToDownload, DataAssembler assembler) {
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
                JCheckBox checkBoxAll = getCheckBoxAll(yearsToDownload);
                createJBoxForAllYears(selectYears, checkboxes, yearsToDownload);
                selectYears.add(checkBoxAll);


                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Executors.newSingleThreadExecutor()
                                .execute(() -> assembler.downloadFiles(yearsToDownload));
                        createConfirmationDialog("Download initialized","o Download foi iniciado, é recomendar esperar acabar, mesmo que demore");
                        selectYears.dispose();
                    }
                });
                confirmationButton.setBounds(690, 220, 100, 30);
                confirmationButton.setVisible(true);
                JCheckBox checkBoxOthers = new JCheckBox("invert.", yearsToDownload.size() == 88);
                checkBoxOthers.addItemListener(e1 -> {
                    selectYears.dispose();
                    if (!yearsToDownload.isEmpty() && yearsToDownload.size() <= 88) {
                        List<Integer> allYears = new ArrayList<>();
                        addAllYears(allYears);
                        allYears.removeAll(yearsToDownload);
                        yearsToDownload.clear();
                        yearsToDownload.addAll(allYears);
                        for (JCheckBox checkbox : checkboxes) {
                            checkbox.setSelected(yearsToDownload.contains(Integer.parseInt(checkbox.getName())));
                        }
                    } else {
                        addAllYears(yearsToDownload);
                        checkboxes.forEach(jCheckBox -> jCheckBox.setSelected(true));
                    }

                    selectYears.setVisible(true);

                });
                checkBoxOthers.setBounds(590, 210, 60, 20);
                checkBoxOthers.setVisible(true);
                selectYears.add(checkBoxOthers);


                selectYears.add(confirmationButton);
                selectYears.add(label);
                selectYears.setSize(800, 300);
                selectYears.setLocationRelativeTo(null);
                selectYears.setVisible(true);
            }
        });
    }

    public static Component loadYearButton(List<Integer> selectedYears) {
        return new JButton(new AbstractAction("Select Year") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Select Years To Process");
                List<JCheckBox> checkBoxes = new ArrayList<>();
                createJBoxForAllYears(frame, checkBoxes, selectedYears);
                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        String years = "";
                        for (Integer integer : selectedYears) {
                            years = years + ", " + integer;
                        }
                        years = years.replaceFirst(",", "");
                        createConfirmationDialog("Anos selecionados para processamento:", years);
                    }
                });
                confirmationButton.setBounds(690, 200, 100, 30);
                confirmationButton.setVisible(true);
                frame.add(confirmationButton);
                frame.setLayout(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setFocusable(true);
                frame.setSize(800, 300);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static Component selectDateRange() {
        return new JButton(new AbstractAction("Select Date Range") {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Selecionar Colunas");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                String[] colums = new String[]{"year", "month", "week", "quarter", "day"};
                JList list = new JList(colums); //data has type Object[]
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                list.setLayoutOrientation(JList.VERTICAL);
                list.addListSelectionListener(e1 -> {
                    if (e1.getValueIsAdjusting()) {
                        DateUtils.setDate(colums[list.getSelectedIndex()]);

                    } else {
                        frame.dispose();
                        createConfirmationDialog("Selected range: " + DateUtils.getDate());
                    }
                });
                list.setVisibleRowCount(-1);
                list.setVisible(true);
                JScrollPane listScroller = new JScrollPane(list);
                listScroller.setPreferredSize(new Dimension(250, 80));
                listScroller.setVisible(true);

                frame.setSize(300, 200);
                frame.add(listScroller);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static Component quitProgramButton(JFrame frame) {
        return new JButton(new AbstractAction("Quit") {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
    }

    public static Component unzipAndCompileFilesButton(DataAssembler assembler, List<Integer> yearsToDownload) {
        return new JButton(new AbstractAction("UnzipFiles") {

            @Override
            public void actionPerformed(ActionEvent e) {
                assembler.unzipAndCompileFiles(yearsToDownload);
                createConfirmationDialog("Files Unzipped!");
            }
        });
    }

    public static Component checkFilesButton(DataAssembler assembler, List<Integer> yearsToDownload) {
        return new JButton(new AbstractAction("Check Files") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (yearsToDownload.size() == 0) {
                    addAllYears(yearsToDownload);
                }
                yearsToDownload.removeAll(assembler.checkFiles());
                createConfirmationDialog("Files Verified!");
            }
        });
    }

    public static Component getCountButton(DataAssembler assembler, List<Integer> years) {
        return new JButton(new AbstractAction("Obter Count sobre a coluna escolhida") {
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
                                .execute(() -> assembler.countProcessData(years, columsToProcess.toArray(String[]::new)));
                        frame.dispose();
                    }
                });


                frame.add(confirmationButton);
                confirmationButton.setBounds(900, 400, 120, 30);
                confirmationButton.setVisible(true);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
    }

    public static Component leastSquaresButton(DataAssembler assembler, List<Integer> years) {
        return new JButton(new AbstractAction("Obter quadrados mínimos") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Selecionar Colunas");
                frame.setLayout(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                final String[] xSelection = {""};
                JButton xButton = new JButton(new AbstractAction("Selecionar x") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame xFrame = new JFrame("Selecionar x");
                        getColumList(xFrame, xSelection);
                    }
                });
                xButton.setBounds(50, 50, 150, 50);
                frame.add(xButton);
                final String[] ySelection = {""};

                JButton yButton = new JButton(new AbstractAction("Selecionar y") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame xFrame = new JFrame("Selecionar y");
                        getColumList(xFrame, ySelection);
                    }
                });

                yButton.setBounds(250, 50, 150, 50);
                frame.add(yButton);


                frame.setSize(450, 300);

                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Executors.newSingleThreadExecutor()
                                .execute(() -> assembler.leastSquaresProcess(years, xSelection[0], ySelection[0]));
                        frame.dispose();
                        createConfirmationDialog("Gerando gráfico...", "Isso deve levar até 4 minutos!");
                    }
                });

                confirmationButton.setBounds(180, 120, 100, 50);
                frame.add(confirmationButton);


                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static Component meanProcessorButton(DataAssembler assembler, List<Integer> years) {
        return new JButton(new AbstractAction("Obter médias") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final List<String> xSelection = new ArrayList<>();
                final List<String> ySelection = new ArrayList<>();
                JFrame frame = new JFrame("Selecionar Colunas");
                frame.setLayout(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                getDimensionList(xSelection, frame);

                getValueList(ySelection, frame);

                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Executors.newSingleThreadExecutor()
                                .execute(() -> assembler.meanProcess(years, xSelection.toArray(String[]::new), ySelection.toArray(String[]::new)));
                        frame.dispose();
                    }
                });

                confirmationButton.setBounds(220, 120, 100, 50);
                frame.add(confirmationButton);


                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static void getDimensionList(List<String> xSelection, JFrame frame) {
        JButton xButton = new JButton(new AbstractAction("Selecionar dimensões") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame xFrame = new JFrame("Selecionar dimensões");
                createYearButtons(xFrame, xSelection, DatasetUtils.schema.fieldNames());
            }
        });
        xButton.setBounds(50, 50, 200, 50);
        frame.add(xButton);
    }

    private static void getValueList(List<String> ySelection, JFrame frame) {
        JButton yButton = new JButton(new AbstractAction("Selecionar valores") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame xFrame = new JFrame("Selecionar valores");
                Iterator<StructField> iterator = DatasetUtils.schema.iterator();
                List<String> columsList = new ArrayList();
                while (iterator.hasNext()) {
                    StructField next = iterator.next();
                    if (next.dataType().typeName().equals("double")) {
                        columsList.add(next.name());
                    }
                }
                String[] colums = columsList.toArray(String[]::new);
                createYearButtons(xFrame, ySelection, colums);
            }
        });

        yButton.setBounds(300, 50, 200, 50);
        frame.add(yButton);


        frame.setSize(550, 300);
    }

    public static Component standardDeviationProcessorButton(DataAssembler assembler, List<Integer> years) {
        return new JButton(new AbstractAction("Obter desvio padrão") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Selecionar Colunas");
                frame.setLayout(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                final List<String> xSelection = new ArrayList<>();
                getDimensionList(xSelection, frame);
                final List<String> ySelection = new ArrayList<>();

                getValueList(ySelection, frame);

                JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Executors.newSingleThreadExecutor()
                                .execute(() -> assembler.standardDeviationProcess(years, xSelection.toArray(String[]::new), ySelection.toArray(String[]::new)));
                        frame.dispose();
                    }
                });

                confirmationButton.setBounds(220, 120, 100, 50);
                frame.add(confirmationButton);


                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private static void createYearButtons(JFrame xFrame, List<String> selection, String[] colums) {
        xFrame.setLayout(null);
        xFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        xFrame.setFocusable(true);
        xFrame.setSize(1000, 350);
        List<JCheckBox> checkBoxes = new ArrayList<>();

        addAllCollums(xFrame, selection, checkBoxes, colums);

        JButton confirmationButton = new JButton(new AbstractAction("Confirm") {
            @Override
            public void actionPerformed(ActionEvent e) {
                xFrame.dispose();
            }
        });
        confirmationButton.setBounds(500, 250, 120, 30);
        confirmationButton.setVisible(true);
        xFrame.add(confirmationButton);
        xFrame.setLocationRelativeTo(null);
        xFrame.setVisible(true);
    }

    private static void getColumList(JFrame xFrame, String[] selection) {
        xFrame.setLayout(new BorderLayout());
        xFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        List<String> columsList = new ArrayList<>();

        Iterator<StructField> iterator = DatasetUtils.schema.iterator();
        while (iterator.hasNext()) {
            StructField next = iterator.next();
            if (next.dataType().typeName().equals("double")) {
                columsList.add(next.name());
            }
        }
        String[] colums = columsList.toArray(String[]::new);


        JList list = new JList(colums); //data has type Object[]
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.addListSelectionListener(e1 -> {
            if (e1.getValueIsAdjusting()) {
                selection[0] = colums[list.getSelectedIndex()];
                xFrame.dispose();
            }
        });
        list.setVisibleRowCount(-1);
        list.setVisible(true);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setVisible(true);
        xFrame.add(listScroller);

        xFrame.setSize(250, 200);

        xFrame.setLocationRelativeTo(null);
        xFrame.setVisible(true);
    }

    private static void addAllCollums(JFrame frame, List<String> columsToProcess, List<JCheckBox> checkBoxes, String[] colums) {
        for (int i = 0; i < colums.length; i++) {
            String colum = colums[i];
//                    System.out.println(colum);

            JCheckBox checkbox1 = new JCheckBox(colum, columsToProcess.contains(colum));
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
        JCheckBox checkBoxAll = new JCheckBox("all", columsToProcess.size() == 27);
        checkBoxAll.addItemListener(e1 -> {
            if (!columsToProcess.isEmpty() && columsToProcess.size() < 27) {
                columsToProcess.clear();
                columsToProcess.addAll(Arrays.asList(colums));
            } else if (columsToProcess.size() == 27) {
                columsToProcess.clear();
            } else columsToProcess.addAll(Arrays.asList(colums));

        });
        checkBoxAll.setBounds(780, 400, 60, 20);
        checkBoxAll.setVisible(true);
        checkBoxes.add(checkBoxAll);
        checkBoxes.forEach(frame::add);
    }

    private static void createJBoxForAllYears(JFrame selectYears, List<JCheckBox> checkboxes, List<Integer> yearsToDownload) {
        for (int i = 1929; i <= 2016; i++) {
            Integer valueOfI = i;
            JCheckBox checkbox1 = new JCheckBox(String.valueOf(valueOfI), yearsToDownload.contains(valueOfI));
            checkbox1.setName(String.valueOf(valueOfI));
            checkbox1.addItemListener(e1 -> {
                if (yearsToDownload.contains(valueOfI)) {
                    yearsToDownload.remove(valueOfI);
                } else
                    yearsToDownload.add(valueOfI);
            });
            int x, y;
            x = 50 + (((i - 1929) % 10) * 60);
            y = 50 + (((i - 1929) / 10) * 20);
            checkbox1.setBounds(x, y, 60, 20);
            checkbox1.setVisible(true);
            checkboxes.add(checkbox1);
        }

        checkboxes.forEach(selectYears::add);
    }

    private static JCheckBox getCheckBoxAll(List<Integer> yearsToDownload) {
        JCheckBox checkBoxAll = new JCheckBox("all", yearsToDownload.size() == 88);
        checkBoxAll.addItemListener(e1 -> {
            if (!yearsToDownload.isEmpty() && yearsToDownload.size() < 88) {
                yearsToDownload.clear();
                addAllYears(yearsToDownload);
            } else if (yearsToDownload.size() == 88) {
                yearsToDownload.clear();
            } else addAllYears(yearsToDownload);

        });
        checkBoxAll.setBounds(530, 210, 60, 20);
        checkBoxAll.setVisible(true);
        return checkBoxAll;
    }

    private static void addAllYears(List<Integer> yearsPaths) {
        for (int i = 1929; i <= 2016; i++) yearsPaths.add(i);
    }

    private static void createConfirmationDialog(String title, String message) {
        JFrame f = new JFrame();
        f.setLayout(null);
        JDialog d = new JDialog(f, title, true);
        d.setLayout(new FlowLayout());
        JButton b = new JButton("OK");
        b.addActionListener(e -> d.setVisible(false));
        d.add(new JLabel(message));
        d.add(b);
        d.setLocationRelativeTo(null);
        f.setLocationRelativeTo(null);
        d.setSize(300, 100);
        d.setVisible(true);
    }

    public static void createConfirmationDialog(String message) {
        createConfirmationDialog(message, message);
    }
}
