package FileTesting;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class FileDialogTest {

    public static void main(String[] args) {


        final JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 4));
        frame.add(new JButton(new AbstractAction("Load Year") {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setVisible(true);
                fc.showOpenDialog(frame);
                System.out.println(fc.getSelectedFile().toString());
            }
        }));
        frame.add(new JButton(new AbstractAction("Save") {

            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(frame, "Test", FileDialog.SAVE);
                fd.setVisible(true);
                System.out.println(fd.getFile());
            }
        }));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}