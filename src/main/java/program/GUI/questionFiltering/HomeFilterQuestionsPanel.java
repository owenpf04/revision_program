package program.GUI.questionFiltering;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.GUI.TitlePanel;
import program.QuestionList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeFilterQuestionsPanel extends JPanel {
    private QuestionList questionsFromFile;

    public HomeFilterQuestionsPanel(QuestionList questionsFromFile) {
        this.questionsFromFile = questionsFromFile;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20,20,20,20));

        add(new TitlePanel("View and filter questions",
                "Use the filters to select a number of questions to revise"),
                BorderLayout.NORTH);

        QuestionsTableScrollPane tablePanel = new QuestionsTableScrollPane(
                new QuestionList(questionsFromFile.getQuestions()));
        add(tablePanel, BorderLayout.CENTER);


        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Back to file selection",
                FontIcon.of(CarbonIcons.ARROW_LEFT, 30));
        backButton.setIconTextGap(10);
        backButton.setPreferredSize(new Dimension(260,80));
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JButton continueButton = new JButton("Continue with selected questions",
                FontIcon.of(CarbonIcons.ARROW_RIGHT, 30));
        continueButton.setHorizontalTextPosition(SwingConstants.LEFT);
        continueButton.setIconTextGap(10);
        continueButton.setPreferredSize(new Dimension(400,80));
        continueButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        navButtonPanel.add(backButton);
        navButtonPanel.add(continueButton);
        navButtonPanel.setBorder(new EmptyBorder(10,0,0,0));

        add(new FilterScrollPane(questionsFromFile, tablePanel), BorderLayout.WEST);
        add(navButtonPanel, BorderLayout.SOUTH);
    }
}
