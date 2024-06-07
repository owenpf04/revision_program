package program.GUI;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.QuestionFile;
import program.Settings;
import program.helpers.ReformatDate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Set;

public class RecentFilesPanel extends JPanel {
    private JLabel openFilePromptLabel;
    private JPanel recentFilesButtonsPanel;
    private Settings settings;

    public RecentFilesPanel(Settings settings) {
        this.settings = settings;

        setLayout(new BorderLayout());

        openFilePromptLabel = new JLabel("Recent files", SwingConstants.LEFT);
        openFilePromptLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 36));
        openFilePromptLabel.setBorder(new EmptyBorder(30, 0, 0, 0));

        recentFilesButtonsPanel = createRecentFilesPanel();

        add(openFilePromptLabel, BorderLayout.NORTH);
        add(recentFilesButtonsPanel, BorderLayout.CENTER);
    }

    private JPanel createRecentFilesPanel() {
        if (!settings.hasRecentFiles()) {
            return createNoRecentFilesPanel();
        } else {
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            JPanel recentButtonsPanel = new JPanel();
            recentButtonsPanel.setLayout(new BoxLayout(recentButtonsPanel, BoxLayout.Y_AXIS));
            Set<QuestionFile> recentFiles = settings.getRecentFiles();

            for (QuestionFile file : recentFiles) {
                recentButtonsPanel.add(createRecentFilesButtonPanel(file));
            }

            mainPanel.add(Box.createVerticalGlue());
            mainPanel.add(recentButtonsPanel);
            mainPanel.add(Box.createVerticalGlue());
            return mainPanel;
        }
    }

    private JPanel createNoRecentFilesPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel(FontIcon.of(CarbonIcons.FOLDER_OFF, 200));
        iconLabel.setAlignmentX(CENTER_ALIGNMENT);
        iconLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel noRecentFilesLabel = new JLabel("You haven't opened any files " +
                "recently. Select a file below.");
        noRecentFilesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        noRecentFilesLabel.setAlignmentX(CENTER_ALIGNMENT);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(iconLabel);
        mainPanel.add(noRecentFilesLabel);
        mainPanel.add(Box.createVerticalGlue());

        mainPanel.setBorder(new EmptyBorder(30, 0, 50, 0));
        return mainPanel;
    }

    private JPanel createRecentFilesButtonPanel(QuestionFile questionFile) {
        JPanel mainPanel = new JPanel();

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);

        JLabel fileNameLabel = new JLabel(questionFile.getFileName());
        fileNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        fileNameLabel.setBorder(new EmptyBorder(10,0,20,0));

        JLabel fileOpenedDateLabel = new JLabel("<html>Last opened: <br><i>" +
                ReformatDate.describeInWords(questionFile.getLastOpened()) + " (" +
                ReformatDate.formatAppropriately(questionFile.getLastOpened()) + ")</i></html>");
        fileOpenedDateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel filePathLabel = new JLabel(questionFile.getFilePath());
        filePathLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filePathLabel.setBorder(new EmptyBorder(5,0,10,0));

        buttonPanel.add(fileNameLabel, BorderLayout.NORTH);
        buttonPanel.add(fileOpenedDateLabel, BorderLayout.CENTER);
        buttonPanel.add(filePathLabel, BorderLayout.SOUTH);

        RecentFilesButton chooseFileButton = new RecentFilesButton(questionFile);
        chooseFileButton.setPreferredSize(new Dimension(400,150));
        chooseFileButton.add(buttonPanel);

        mainPanel.add(chooseFileButton);
        return mainPanel;
    }

    private class RecentFilesButton extends JButton {
        private QuestionFile questionFile;

        public RecentFilesButton(QuestionFile questionFile) {
            this.questionFile = questionFile;
        }

        public QuestionFile getQuestionFile() {
            return questionFile;
        }
    }
}