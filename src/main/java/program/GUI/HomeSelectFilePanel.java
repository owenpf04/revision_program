package program.GUI;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class HomeSelectFilePanel extends JPanel {
    private JPanel titlePanel;
    private JPanel filePanel;

    public HomeSelectFilePanel() {
        setBorder(new EmptyBorder(20,20,20,20));
        setLayout(new BorderLayout());
        titlePanel = new TitlePanel();
        filePanel = new FilePanel();

        add(titlePanel, BorderLayout.NORTH);
        add(filePanel, BorderLayout.CENTER);
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
            return ("Good " + getTimeGreeting() + ", " + "Owen");
        }

        private String getTimeGreeting() {
            int currentHour = LocalDateTime.now().getHour();
            if (currentHour >= 5 && currentHour < 12) {
                return "morning";
            } else if (currentHour >= 12 && currentHour < 18) {
                return "afternoon";
            } else {
                return "evening";
            }
        }
    }

    private class FilePanel extends JPanel {
        private JPanel recentFilesPanel;
        private JPanel openFilePanel;

        public FilePanel() {
            setLayout(new BorderLayout());

            recentFilesPanel = new RecentFilesPanel();
            openFilePanel = new OpenFilePanel();

            add(recentFilesPanel, BorderLayout.CENTER);
            add(openFilePanel, BorderLayout.SOUTH);
        }

        private class RecentFilesPanel extends JPanel {
            private JLabel openFilePromptLabel;
            private JPanel recentFilesButtonsPanel;

            public RecentFilesPanel() {
                setLayout(new BorderLayout());

                openFilePromptLabel = new JLabel("Recent files", SwingConstants.LEFT);
                openFilePromptLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 36));
                openFilePromptLabel.setBorder(new EmptyBorder(30,0,0,0));

                recentFilesButtonsPanel = createRecentFilesPanel();
                recentFilesButtonsPanel.setPreferredSize(new Dimension(400,300));

                add(openFilePromptLabel, BorderLayout.NORTH);
                add(recentFilesButtonsPanel, BorderLayout.CENTER);
            }

            private JPanel createRecentFilesPanel() {
                return new JPanel();
            }
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
                chooseFileButton = new JButton("Select a file", FontIcon.of(CarbonIcons.FOLDER, 30));
                chooseFileButton.setPreferredSize(new Dimension(200,100));
                chooseFileButtonPanel.add(chooseFileButton);
                chooseFileButtonPanel.setBorder(new EmptyBorder(30,0,10,0));

                add(openFilePromptLabel, BorderLayout.NORTH);
                add(chooseFileButtonPanel, BorderLayout.CENTER);

                chooseFileButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //TODO customise default opening location?
                        JFileChooser fileChooser = new JFileChooser();
                        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                ".csv files", "csv");
                        fileChooser.setFileFilter(filter);
                        int returnVal = fileChooser.showOpenDialog(null);
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            System.out.println("You chose to open this file: " +
                                    fileChooser.getSelectedFile().getName());
                        }
                    }
                });
            }
        }
    }
}
