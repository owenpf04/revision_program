package program.GUI.fileSelection;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.FileQuestionsInterface;
import program.GUI.HomeScrollPane;
import program.QuestionFile;
import program.QuestionList;
import program.Settings;
import program.exceptions.InvalidQuestionFileException;
import program.helpers.CommonDialogs;
import program.helpers.ReformatDate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;

public class RecentFilesPanel extends JPanel {
    private HomeScrollPane parentScrollPane;

    private JLabel openFilePromptLabel;
    private JPanel recentFilesButtonsPanel;

    public RecentFilesPanel(HomeScrollPane parentScrollPane) {
        this.parentScrollPane = parentScrollPane;

        setLayout(new BorderLayout());

        openFilePromptLabel = new JLabel("Recent files", SwingConstants.LEFT);
        openFilePromptLabel.putClientProperty("FlatLaf.styleClass", "h0");

        recentFilesButtonsPanel = new RecentFilesButtonsPanel();

        add(openFilePromptLabel, BorderLayout.NORTH);
        add(recentFilesButtonsPanel, BorderLayout.CENTER);
    }

    private class RecentFilesButtonsPanel extends JPanel {
        public RecentFilesButtonsPanel() {
            if (!Settings.hasRecentFiles()) {
                createNoRecentFilesPanel();
            } else {
                setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                JPanel recentButtonsPanel = new JPanel();
                recentButtonsPanel.setLayout(new BoxLayout(recentButtonsPanel, BoxLayout.Y_AXIS));
                List<QuestionFile> recentFiles = Settings.getRecentFiles();

                for (QuestionFile file : recentFiles) {
                    recentButtonsPanel.add(createRecentFilesButtonPanel(file));
                }

                add(Box.createVerticalGlue());
                add(recentButtonsPanel);
                add(Box.createVerticalGlue());
            }
        }

        private void createNoRecentFilesPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JLabel iconLabel = new JLabel(FontIcon.of(CarbonIcons.FOLDER_OFF, 200));
            iconLabel.setAlignmentX(CENTER_ALIGNMENT);
            iconLabel.setBorder(new EmptyBorder(0, 0, 30, 0));

            JLabel noRecentFilesLabel = new JLabel("You haven't opened any files " +
                    "recently. Select a file below.");
            noRecentFilesLabel.putClientProperty("FlatLaf.styleClass", "h2.regular");
            noRecentFilesLabel.setAlignmentX(CENTER_ALIGNMENT);

            add(Box.createVerticalGlue());
            add(iconLabel);
            add(noRecentFilesLabel);
            add(Box.createVerticalGlue());

            setBorder(new EmptyBorder(30, 0, 50, 0));
        }

        private JPanel createRecentFilesButtonPanel(QuestionFile questionFile) {
            JPanel mainPanel = new JPanel();

            JPanel buttonPanel = new JPanel(new BorderLayout());
            buttonPanel.setOpaque(false);

            JLabel fileNameLabel = new JLabel(questionFile.getFileName());
            fileNameLabel.putClientProperty("FlatLaf.styleClass", "h1");
//            fileNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            fileNameLabel.setBorder(new EmptyBorder(10,0,20,0));

            JLabel fileOpenedDateLabel = new JLabel("<html>Last opened: <br><i>" +
                    ReformatDate.describeInWords(questionFile.getLastOpened()) + " (" +
                    ReformatDate.formatAppropriately(questionFile.getLastOpened()) + ")</i></html>");
            fileOpenedDateLabel.putClientProperty("FlatLaf.styleClass", "large");

            JLabel filePathLabel = new JLabel(questionFile.getFilePath());
            filePathLabel.putClientProperty("FlatLaf.styleClass", "monospaced");
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
    }

    private class RecentFilesButton extends JButton {
        private QuestionFile questionFile;

        public RecentFilesButton(QuestionFile questionFile) {
            this.questionFile = questionFile;
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        FileQuestionsInterface fQInterface = new FileQuestionsInterface(
                                questionFile.getFilePath());
                        QuestionList questionsFromFile = fQInterface.getQuestionList();

                        Settings.addRecentFile(questionFile.getFilePath());

                        parentScrollPane.showFilterQuestionsPanel(questionsFromFile);
//                        parentScrollPane.addFilterQuestionsPanel(questionsFromFile);
//                        parentScrollPane.getCardPanelLayout().show(
//                                parentScrollPane.getInnerPanel(), "homeFilterQuestionsPanel");
                    } catch (InvalidQuestionFileException exc) {
                        CommonDialogs.showInvalidFileDialog(parentScrollPane, exc);
                    } catch (FileNotFoundException exc) {
                        Settings.getRecentFiles().remove(questionFile);
                        RecentFilesPanel.this.remove(1);
                        RecentFilesPanel.this.add(new RecentFilesButtonsPanel());
                        JOptionPane.showMessageDialog(parentScrollPane,
                                "The selected file could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
                        //TODO: remove question details from app.properties
                    } catch (Exception exc) {

                    }
                }
            });
        }

        public QuestionFile getQuestionFile() {
            return questionFile;
        }
    }
}
