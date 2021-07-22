import assembler.DataAssembler;
import buttons.MainButton;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class ApplicationStart implements Runnable {

    private List<String> yearsPaths = new ArrayList<>();
    private List<Integer> yearsToDownload = new ArrayList<>();

    public static void main(String[] args) {
        new ApplicationStart().run();
    }

    @Override
    public void run() {
        final JFrame frame = new JFrame();
        DataAssembler assembler = new DataAssembler();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 1));
        frame.add(MainButton.loadYearButton(frame,yearsPaths));
        frame.add(MainButton.saveButton(frame));
        frame.add(MainButton.selectYearsButton(yearsToDownload, assembler));
        frame.add(MainButton.checkFilesButton(frame, assembler, yearsToDownload));


        frame.add(MainButton.quitProgramButton(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }




    public List<String> getYearsPaths() {
        return yearsPaths;
    }

    public void setYearsPaths(List<String> yearsPaths) {
        this.yearsPaths = yearsPaths;
    }

}