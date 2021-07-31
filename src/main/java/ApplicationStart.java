import assembler.DataAssembler;
import buttons.MainFrame;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class ApplicationStart implements Runnable {

    private final List<Integer> selectedYears = new ArrayList<>();
    private final List<Integer> yearsToDownload = new ArrayList<>();

    public static void main(String[] args){
        new ApplicationStart().run();
    }

    @Override
    public void run() {
        final JFrame frame = new JFrame();
        DataAssembler assembler = new DataAssembler();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 2));
        frame.add(MainFrame.selectYearsToDownloadButton(yearsToDownload, assembler));
        frame.add(MainFrame.loadYearButton(selectedYears));
        frame.add(MainFrame.checkFilesButton(assembler, yearsToDownload));
        frame.add(MainFrame.selectDateRange());
        frame.add(MainFrame.unzipAndCompileFilesButton(assembler, yearsToDownload));
        frame.add(MainFrame.getCountButton(assembler, selectedYears));
        frame.add(MainFrame.leastSquaresButton(assembler, selectedYears));
        frame.add(MainFrame.meanProcessorButton(assembler, selectedYears));
        frame.add(MainFrame.standardDeviationProcessorButton(assembler, selectedYears));
        frame.add(MainFrame.quitProgramButton(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}