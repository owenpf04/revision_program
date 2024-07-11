package program.GUI.questionFiltering;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.GUI.HomeScrollPane;
import program.GUI.TitlePanel;
import program.QuestionList;
import program.helpers.Misc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFilterQuestionsPanel extends JPanel {
    private final JFrame mainFrame;
    private HomeScrollPane parentScrollPane;
    private QuestionList questionsFromFile;

    public HomeFilterQuestionsPanel(JFrame mainFrame, HomeScrollPane parentScrollPane, QuestionList questionsFromFile) {
        this.mainFrame = mainFrame;
        this.parentScrollPane = parentScrollPane;
        this.questionsFromFile = questionsFromFile;


        setLayout(new BorderLayout());

        add(new TitlePanel("View and filter questions",
                "Use the filters to select a number of questions to revise"),
                BorderLayout.NORTH);


        QuestionsTableScrollPane table = new QuestionsTableScrollPane(
                new QuestionList(questionsFromFile.getQuestions()));
        add(table, BorderLayout.CENTER);

        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back to file selection",
                FontIcon.of(CarbonIcons.ARROW_LEFT, 30,
                        Misc.getUIManagerColor("colorForeground")));
        backButton.setIconTextGap(10);
        backButton.setPreferredSize(new Dimension(270,50));
        backButton.putClientProperty("FlatLaf.styleClass", "h2.regular");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentScrollPane.showSelectFilePanel();
            }
        });

        JButton continueButton = new JButton("Continue with selected questions",
                FontIcon.of(CarbonIcons.ARROW_RIGHT, 30,
                        Misc.getUIManagerColor("colorSelectionForeground")));
        continueButton.setHorizontalTextPosition(SwingConstants.LEFT);
        continueButton.setIconTextGap(10);
        continueButton.setPreferredSize(new Dimension(380,50));
        continueButton.putClientProperty("FlatLaf.styleClass", "h2.regular");
        continueButton.setBackground(Misc.getUIManagerColor("colorSelectionBackground"));
        continueButton.setForeground(Misc.getUIManagerColor("colorSelectionForeground"));

        navButtonPanel.add(backButton);
        navButtonPanel.add(continueButton);
        navButtonPanel.setBorder(new EmptyBorder(10,0,0,0));

        add(new FilterPanel(questionsFromFile, table), BorderLayout.WEST);
        add(navButtonPanel, BorderLayout.SOUTH);
    }
}
