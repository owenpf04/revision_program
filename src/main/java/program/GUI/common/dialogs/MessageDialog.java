package program.GUI.common.dialogs;

import program.exceptions.InvalidQuestionFileException;
import program.helpers.ReformatString;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public abstract class MessageDialog {
    private static final String EMAIL_MESSAGE =
            "Please send me an email at owenpf.work@gmail.com " +
                    "with these details. The program will now exit.";

    private static void displayMessage(Component parent, String windowTitle, String contentTitle, String desc,
            JPanel details, String instructions, int messageType) {
        JPanel mainPanel = new JPanel(new BorderLayout(0,20));

        JLabel titleLabel = DialogComponents.titleLabel(contentTitle);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));

        JLabel description = new JLabel("<html>" + ReformatString.wrapString(desc,
                100, 0, true) + "</html>");
        description.putClientProperty("FlatLaf.styleClass", "large");

        contentPanel.add(description, BorderLayout.NORTH);
        if (details != null) {
            contentPanel.add(details,
                    BorderLayout.CENTER);
        }
        if (instructions != null) {
            contentPanel.add(messageInstructionsPanel(instructions), BorderLayout.SOUTH);
        }

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(parent, mainPanel, windowTitle, messageType);
    }

    public static void displayUnexpectedErrorMessage(Component parent, Exception e) {
        displayMessage(parent, "Unexpected error", "An unexpected error has occurred.",
                "We don't really know what caused this error; the best we can do is provide " +
                        "you with the details below.", exceptionDetailsPanel(e), "As stated above, we " +
                        "don't know what caused this error, so we can't really offer any advice " +
                        "other than to try again.", JOptionPane.ERROR_MESSAGE);
    }

    public static void displayExpectedErrorMessage(Component parent, String context, Exception e) {
        displayMessage(parent, "Error", "An error occurred while " + context + ".",
                "This is an error which we're aware could occur in theory, but didn't think " +
                        "would ever happen in practice.", exceptionDetailsPanel(e), "As stated above, we " +
                        "didn't think this error would ever actually happen (great assumption), so " +
                        "the best advice we can give is to try again.", JOptionPane.ERROR_MESSAGE);
    }

    public static void displayInvalidFileMessage(Component parent, InvalidQuestionFileException e) {
        displayMessage(parent, "Invalid file", "We can't read that.",
                "The selected file is invalid - see below for the first issue (although there " +
                        "may be others).", invalidQuestionFilePanel(e), null,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void displayInfoMessage(Component parent, String windowTitle, String contentTitle, String desc) {
        displayMessage(parent, windowTitle, contentTitle, desc, null, null,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void displayInfoMessage(Component parent, String windowTitle, String contentTitle,
            String desc, int messageType) {
        displayMessage(parent, windowTitle, contentTitle, desc, null, null,
                messageType);
    }

    //TODO add dialog for if file cannot be found

    static JPanel exceptionDetailsPanel(Exception e) {
        JLabel titleLabel = new JLabel("Details:");
        titleLabel.putClientProperty("FlatLaf.styleClass", "h3");

        String detailsMessage = "Cause:                 " +
                ReformatString.wrapString(String.valueOf(e), 60, 23, false) +
                "\n\nStacktrace:            " +
                ReformatString.wrapString(Arrays.toString(e.getStackTrace()),
                        60, 23, false);

        JTextArea details = new JTextArea(detailsMessage);
        // Workaround to avoid caret being insertable into text, without changing text colour
        details.setEnabled(false);
        details.setDisabledTextColor(details.getForeground());
        details.putClientProperty("FlatLaf.styleClass", "monospaced");

        JPanel mainPanel = new JPanel(new BorderLayout(0, 8));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(details, BorderLayout.CENTER);

        return mainPanel;
    }

    static JPanel messageInstructionsPanel(String instructions) {
        JLabel titleLabel = new JLabel("What to do:");
        titleLabel.putClientProperty("FlatLaf.styleClass", "h3");

        JPanel mainPanel = new JPanel(new BorderLayout(0, 8));

        JLabel instructionArea = new JLabel("<html>" + ReformatString.wrapString(instructions,
                100, 0, true) + "<br><br>" + ReformatString.wrapString(EMAIL_MESSAGE,
                100, 0, true) + "</html>");
        instructionArea.putClientProperty("FlatLaf.styleClass", "medium");

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(instructionArea, BorderLayout.CENTER);

        return mainPanel;
    }

    static JPanel invalidQuestionFilePanel(InvalidQuestionFileException e) {
        JLabel titleLabel = new JLabel("Details:");
        titleLabel.putClientProperty("FlatLaf.styleClass", "h3");

        String detailsMessage = "File location: " +
                ReformatString.wrapString(e.getFileLocation(), 60, 15, false) +
                "\nLine number:   " + e.getLineNumber() +
                "\nLine:          \"" + ReformatString.wrapString(e.getLine(), 60, 15, false) +
                "\"\nDescription:   " +
                ReformatString.wrapString(e.getDescription(), 60, 15, false);

        JTextArea details = new JTextArea(detailsMessage);
        // Workaround to avoid caret being insertable into text, without changing text colour
        details.setEnabled(false);
        details.setDisabledTextColor(details.getForeground());
        details.putClientProperty("FlatLaf.styleClass", "monospaced");

        JPanel mainPanel = new JPanel(new BorderLayout(0, 8));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(details, BorderLayout.CENTER);

        return mainPanel;
    }
}
