package program.GUI;

import program.QuestionList;
import program.attributes.fields.QuestionAttribute;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeFilterQuestionsPanel extends JPanel {
    private QuestionList questionsFromFile;

    public HomeFilterQuestionsPanel(QuestionList questionsFromFile) {
        this.questionsFromFile = questionsFromFile;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20,20,20,20));

        add(new TitlePanel("View and filter questions", 72,
                "Use the filters to select a number of questions to revise", 24),
                BorderLayout.NORTH);

        QuestionsTablePanel tablePanel = new QuestionsTablePanel(
                new QuestionList(questionsFromFile.getQuestions()));
        add(tablePanel, BorderLayout.CENTER);

        add(new FilterScrollPane(questionsFromFile, tablePanel), BorderLayout.WEST);
        add(new JButton("sdfsdf"), BorderLayout.SOUTH);
    }
}
