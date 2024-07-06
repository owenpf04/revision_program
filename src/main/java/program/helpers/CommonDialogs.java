package program.helpers;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import program.GUI.HomeScrollPane;
import program.exceptions.InvalidQuestionFileException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CommonDialogs {
    public static void showInvalidFileDialog(Component parent,
                                       InvalidQuestionFileException e) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel mainErrorMessage = new JLabel("The selected file is invalid. Details:");
        mainErrorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(mainErrorMessage);

        String detailsMessage = "File location: " +
                ReformatString.wrapString(e.getFileLocation(), 60, 15, false) +
                "\nLine number:   " + e.getLineNumber() +
                "\nLine:          \"" + ReformatString.wrapString(e.getLine(), 60, 15, false) +
                "\"\nDescription:   " + ReformatString.wrapString(e.getDescription(), 60, 15, false);
        JTextArea details = new JTextArea(detailsMessage);
        details.setEditable(false);
        details.putClientProperty("FlatLaf.styleClass", "monospaced");
        details.setBorder(new EmptyBorder(20,0,0,0));

        mainPanel.add(details);

        JOptionPane.showMessageDialog(parent, mainPanel, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
