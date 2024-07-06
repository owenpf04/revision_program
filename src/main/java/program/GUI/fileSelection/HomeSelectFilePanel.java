package program.GUI.fileSelection;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.FileQuestionsInterface;
import program.GUI.HomeScrollPane;
import program.GUI.TitlePanel;
import program.GUI.questionFiltering.HomeFilterQuestionsPanel;
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

    public HomeSelectFilePanel(HomeScrollPane parentScrollPane) {
        this.parentScrollPane = parentScrollPane;

        setBorder(new EmptyBorder(20,20,20,20));
        setLayout(new BorderLayout());
        titlePanel = new TitlePanel(getGreetingMessage(),
                "Open a question file to get revising:");
        filePanel = new FilePanel();

        add(titlePanel, BorderLayout.NORTH);
        add(filePanel, BorderLayout.CENTER);
    }

    public void updateRecentFiles() {
        remove(filePanel);
        filePanel = new FilePanel();
        add(filePanel, BorderLayout.CENTER);
        repaint();
    }

    private String getGreetingMessage() {
        return ("Good " + ReformatDate.getTimeOfDay(LocalDateTime.now(), false) +
                ", " + Settings.getUserName());
    }

    private class FilePanel extends JPanel {
        private JPanel recentFilesPanel;
        private JPanel openFilePanel;

        public FilePanel() {
            setLayout(new BorderLayout());

            recentFilesPanel = new RecentFilesPanel(parentScrollPane);
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
                openFilePromptLabel.putClientProperty("FlatLaf.styleClass", "h0");

                chooseFileButtonPanel = new JPanel();
                chooseFileButton = new JButton("Select a file", FontIcon.of(CarbonIcons.FOLDER, 50));
                chooseFileButton.putClientProperty("FlatLaf.styleClass", "large");
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
                    JFileChooser fileChooser = new JFileChooser(Settings.getDefaultFileOpeningDirectory());
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
                            FileQuestionsInterface fQInterface =
                                    new FileQuestionsInterface(chosenFile.getAbsolutePath());
                            QuestionList questionsFromFile = fQInterface.getQuestionList();

                            Settings.addRecentFile(chosenFile.getAbsolutePath());

                            parentScrollPane.showFilterQuestionsPanel(questionsFromFile);
//                            parentScrollPane.getInnerPanel().remove();
//                            parentScrollPane.getInnerPanel().add(
//                                    new HomeFilterQuestionsPanel(questionsFromFile, parentScrollPane),
//                                    "homeFilterQuestionsPanel");
//                            parentScrollPane.getCardPanelLayout().show(
//                                    parentScrollPane.getInnerPanel(),
//                                    "homeFilterQuestionsPanel");
                        } catch (InvalidQuestionFileException exc) {
                            CommonDialogs.showInvalidFileDialog(parentScrollPane, exc);
                        } catch (FileNotFoundException exc) {
                            JOptionPane.showMessageDialog(parentScrollPane,
                                    "The selected file could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception exc) {

                        }
                    }
                }
            }
        }
    }
}
