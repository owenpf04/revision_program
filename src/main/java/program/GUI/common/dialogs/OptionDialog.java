package program.GUI.common.dialogs;

import program.exceptions.InvalidPropertiesException;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public abstract class OptionDialog {
    public static int displayOptionDialog(String windowTitle, String contentTitle, String desc,
            int optionType, int messageType, List<DialogOption> options) {
        JLabel description = new JLabel("<html>" + ReformatString.wrapString(desc,
                100, 0, true) + "</html>");
        description.putClientProperty("FlatLaf.styleClass", "large");

        return displayOptionDialog(windowTitle, contentTitle, description, null, optionType,
                messageType, options);
    }

    public static int displayOptionDialog(String windowTitle, String contentTitle,
            InvalidPropertiesException e, int optionType, int messageType,
            List<DialogOption> options) {
        String summaryText = "We couldn't fully interpret the properties file for the following ";
        if (e.getInvalidPropertySet().size() == 1) {
            summaryText += "reason:";
        } else {
            summaryText += e.getInvalidPropertySet().size() + " reasons:";
        }

        JLabel summaryLabel = new JLabel(summaryText);
        summaryLabel.putClientProperty("FlatLaf.styleClass", "large");

        return displayOptionDialog(windowTitle, contentTitle, summaryLabel,
                DialogComponents.invalidPropertiesPanel(e), optionType, messageType,
                options);
    }


    private static int displayOptionDialog(String windowTitle, String contentTitle,
            JComponent description, JComponent details, int optionType, int messageType,
            List<DialogOption> options) {
        if (options.size() < 2 || options.size() > 3) {
            throw new IllegalArgumentException("Expected 2 or 3 options, got " + options.size() +
                    ".");
        }

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));

        JLabel titleLabel = DialogComponents.titleLabel(contentTitle);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 8));
        JPanel instructionsPanel = optionInstructionsPanel(options);

        contentPanel.add(description, BorderLayout.NORTH);
        if (details != null) {
            contentPanel.add(details, BorderLayout.CENTER);
        }
        contentPanel.add(instructionsPanel, BorderLayout.SOUTH);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        String[] optionNames = new String[options.size()];
        for (int i = 0; i < options.size(); i++) {
            optionNames[i] = options.get(i).getName();
        }

        return JOptionPane.showOptionDialog(null, mainPanel, windowTitle,
                optionType, messageType, null, optionNames, optionNames[0]);
    }

    private static JPanel optionInstructionsPanel(List<DialogOption> options) {
        JPanel panel = new JPanel(new BorderLayout(0,10));

        String mainMessage = "<b>Click \"" + options.getFirst().getName() + "\"</b> to " +
                options.getFirst().getDescription();
        JLabel mainOption = new JLabel("<html>" + ReformatString.wrapString(mainMessage,
                100, 0, true) + "</html>");
        mainOption.putClientProperty("FlatLaf.styleClass", "large");
        panel.add(mainOption, BorderLayout.CENTER);

        if (options.size() == 2) {
            String alternativeMessage = "Alternatively, click <b>\"" + options.getLast().getName() +
                    "\"</b> to " + options.getLast().getDescription();
            JLabel alternativeOption = new JLabel("<html>" + ReformatString.wrapString(
                    alternativeMessage, 100, 0, true) + "</html>");
            alternativeOption.putClientProperty("FlatLaf.styleClass", "medium");
            panel.add(alternativeOption, BorderLayout.SOUTH);
        } else if (options.size() == 3) {
            JPanel alternativeOptions = new JPanel(new BorderLayout());

            JLabel headingLabel = new JLabel("Alternatively:");
            headingLabel.putClientProperty("FlatLaf.styleClass", "medium");
            alternativeOptions.add(headingLabel, BorderLayout.NORTH);

            String[] positions = {BorderLayout.CENTER, BorderLayout.SOUTH};

            for (int i = 0; i < 2; i++) {
                String message = "Click <b>\"" + options.get(i + 1).getName() + "\"</b> to " +
                        options.get(i + 1).getDescription();
                JLabel option = new JLabel("<html>" + ReformatString.wrapString(message,
                        100, 0, true) + "</html>");
                option.setBorder(new EmptyBorder(10,15,0,0));
                option.putClientProperty("FlatLaf.styleClass", "medium");
                alternativeOptions.add(option, positions[i]);
            }

            panel.add(alternativeOptions, BorderLayout.SOUTH);
        }

        return panel;
    }
}
