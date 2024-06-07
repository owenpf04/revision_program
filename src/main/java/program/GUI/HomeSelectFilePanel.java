package program.GUI;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.FileQuestionsInterface;
import program.QuestionList;
import program.Settings;
import program.exceptions.InvalidQuestionFileException;
import program.helpers.ReformatDate;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HomeSelectFilePanel extends JPanel {
    private JPanel titlePanel;
    private JPanel filePanel;
    private HomeScrollPane parentScrollPane;
    private Settings settings;

    public HomeSelectFilePanel(HomeScrollPane parentScrollPane, Settings settings) {
        this.settings = settings;
        setBorder(new EmptyBorder(20,20,20,20));
        setLayout(new BorderLayout());
        titlePanel = new TitlePanel();
        filePanel = new FilePanel();

        add(titlePanel, BorderLayout.NORTH);
        add(filePanel, BorderLayout.CENTER);

        this.parentScrollPane = parentScrollPane;
    }

    private class TitlePanel extends JPanel {
        private JLabel greetingLabel;
        private JLabel openFilePromptLabel;
        private JSeparator separator;

        public TitlePanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            greetingLabel = new JLabel();
            greetingLabel.setText(getGreetingMessage());
            greetingLabel.setFont(new Font("Bahnschrift", Font.BOLD, 72));
            greetingLabel.setHorizontalAlignment(SwingConstants.LEFT);
            greetingLabel.setAlignmentX(LEFT_ALIGNMENT);

            openFilePromptLabel = new JLabel("Open a question file to get revising:", SwingConstants.LEFT);
            openFilePromptLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 24));
            openFilePromptLabel.setBorder(new EmptyBorder(0,0,15,0));
            openFilePromptLabel.setAlignmentX(LEFT_ALIGNMENT);

            separator = new JSeparator(JSeparator.HORIZONTAL);
            separator.setAlignmentX(LEFT_ALIGNMENT);

            add(greetingLabel);
            add(openFilePromptLabel);
            add(separator);
        }

        private String getGreetingMessage() {
            return ("Good " + ReformatDate.getTimeOfDay(LocalDateTime.now(), false) +
                    ", " + settings.getUserName());
        }
    }

    private class FilePanel extends JPanel {
        private JPanel recentFilesPanel;
        private JPanel openFilePanel;

        public FilePanel() {
            setLayout(new BorderLayout());

            recentFilesPanel = new RecentFilesPanel(settings);
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

                chooseFileButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //TODO customise default opening location?
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
                            } catch (InvalidQuestionFileException exc) {
                                showInvalidFileDialog(parentScrollPane, exc);
                            } catch (FileNotFoundException exc) {
                                JOptionPane.showMessageDialog(parentScrollPane,
                                        "The selected file could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
            }
        }

        private void showInvalidFileDialog(HomeScrollPane parentScrollPane,
                                           InvalidQuestionFileException e) {
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

            JLabel mainErrorMessage = new JLabel("The selected file is invalid. Details:");
            mainErrorMessage.setAlignmentX(CENTER_ALIGNMENT);
            mainPanel.add(mainErrorMessage);

            String detailsMessage = "File location: " +
                    ReformatString.wrapString(e.getFileLocation(), 60, 15) +
                    "\nLine number:   " + e.getLineNumber() +
                    "\nLine:          \"" + ReformatString.wrapString(e.getLine(), 60, 15) +
                    "\"\nDescription:   " + ReformatString.wrapString(e.getDescription(), 60, 15);
            JTextArea details = new JTextArea(detailsMessage);
            details.setEditable(false);
            details.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 12));
            details.setBorder(new EmptyBorder(20,0,0,0));

            mainPanel.add(details);

            JOptionPane.showMessageDialog(parentScrollPane, mainPanel, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}