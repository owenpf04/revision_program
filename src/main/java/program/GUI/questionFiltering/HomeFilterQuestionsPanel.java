package program.GUI.questionFiltering;

import program.GUI.HomeScrollPane;
import program.GUI.common.NavBar;
import program.GUI.common.TitlePanel;
import program.QuestionList;

import javax.swing.*;
import java.awt.*;

public class HomeFilterQuestionsPanel extends JPanel {
    private HomeScrollPane parentScrollPane;
    private QuestionList questionsFromFile;

    public HomeFilterQuestionsPanel(HomeScrollPane parentScrollPane, QuestionList questionsFromFile) {
        this.parentScrollPane = parentScrollPane;
        this.questionsFromFile = questionsFromFile;


        setLayout(new BorderLayout());

        add(new TitlePanel("View and filter questions",
                "Use the filters to select a set of questions to revise"),
                BorderLayout.NORTH);


        QuestionsSelectionTableScrollPane table = new QuestionsSelectionTableScrollPane(
                new QuestionList(questionsFromFile.getQuestions()));
        add(table, BorderLayout.CENTER);

        JPanel navBar = new NavBar("Back to file selection", "Continue " +
                "with selected questions") {
            @Override
            protected void backButtonActionPerformed() {
                parentScrollPane.showSelectFilePanel();
            }

            @Override
            protected void continueButtonActionPerformed() {
                parentScrollPane.showSelectModePanel(table.getSelectedQuestions());
            }
        };

        add(new FilterPanel(questionsFromFile, table), BorderLayout.WEST);
        add(navBar, BorderLayout.SOUTH);
    }
}
