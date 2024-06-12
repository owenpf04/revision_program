package program.GUI;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.FileQuestionsInterface;
import program.QuestionList;
import program.Settings;
import program.exceptions.InvalidQuestionFileException;
import program.helpers.CommonDialogs;
import program.helpers.ReformatDate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class HomeSelectFilePanel extends JPanel {
    private JPanel titlePanel;
    private JPanel filePanel;
    private HomeScrollPane parentScrollPane;
    private Settings settings;

    public HomeSelectFilePanel(HomeScrollPane parentScrollPane, Settings settings) {
        this.settings = settings;
        this.parentScrollPane = parentScrollPane;

        setBorder(new EmptyBorder(20,20,20,20));
        setLayout(new BorderLayout());
        titlePanel = new TitlePanel(getGreetingMessage(), 72, "Open a question file to get revising:", 24);
        filePanel = new FilePanel();

        add(titlePanel, BorderLayout.NORTH);
        add(filePanel, BorderLayout.CENTER);
    }

    private String getGreetingMessage() {
        return ("Good " + ReformatDate.getTimeOfDay(LocalDateTime.now(), false) +
                ", " + settings.getUserName());
    }

    private class FilePanel extends JPanel {
        private JPanel recentFilesPanel;
        private JPanel openFilePanel;

        public FilePanel() {
            setLayout(new BorderLayout());

            recentFilesPanel = new RecentFilesPanel(settings, parentScrollPane);
            recentFilesPanel.setBorder(new EmptyBorder(0,0,20,0));
            openFilePanel = new OpenFilePanel();

            add(recentFilesPanel, BorderLayout.CENTER);
            add(openFilePanel, BorderLayout.SOUTH);
        }

        private class OpenFilePanel extends JPanel {
            private JLabel openFilePromptLabel;
            private JPanel chooseFileButtonPanel;
            private JButton chooseFileButton;

            public OpenFilePanel() {
                setLayout(new BorderLayout());

                openFilePromptLabel = new JLabel("Choose a file to open", SwingConstants.LEFT);
                openFilePromptLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 36));

                chooseFileButtonPanel = new JPanel();
                chooseFileButton = new JButton("Select a file", FontIcon.of(CarbonIcons.FOLDER, 50));
                chooseFileButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                chooseFileButton.setIconTextGap(15);
                chooseFileButton.setPreferredSize(new Dimension(200,100));
                chooseFileButtonPanel.add(chooseFileButton);
                chooseFileButtonPanel.setBorder(new EmptyBorder(30,0,10,0));

                add(openFilePromptLabel, BorderLayout.NORTH);
                add(chooseFileButtonPanel, BorderLayout.CENTER);

                chooseFileButton.addActionListener(new chooseFileActionListener());
            }

            private class chooseFileActionListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser(settings.getDefaultFileOpeningDirectory());
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            ".csv files", "csv");
                    fileChooser.setFileFilter(filter);

                    Action details = fileChooser.getActionMap().get("viewTypeDetails");
                    details.actionPerformed(null);

                    int returnVal = fileChooser.showOpenDialog(parentScrollPane);

                    if (returnVal == JFileChooser.ERROR_OPTION) {
                        JOptionPane.showMessageDialog(parentScrollPane,
                                "An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File chosenFile = fileChooser.getSelectedFile();

                        try {
                            FileQuestionsInterface fQInterface = new FileQuestionsInterface(settings,
                                    chosenFile.getAbsolutePath());
                            QuestionList questionsFromFile = fQInterface.getQuestionList();
                            parentScrollPane.addFilterQuestionsPanel(questionsFromFile);
                            parentScrollPane.getCardPanelLayout().show(
                                    parentScrollPane.getCardPanel(), "homeFilterQuestionsPanel");
                        } catch (InvalidQuestionFileException exc) {
                            CommonDialogs.showInvalidFileDialog(parentScrollPane, exc);
                        } catch (FileNotFoundException exc) {
                            JOptionPane.showMessageDialog(parentScrollPane,
                                    "The selected file could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
    }
}