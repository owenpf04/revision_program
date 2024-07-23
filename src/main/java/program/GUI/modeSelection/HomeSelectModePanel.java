package program.GUI.modeSelection;

import program.GUI.HomeScrollPane;
import program.GUI.common.QuestionsTableScrollPane;
import program.GUI.common.NavBar;
import program.GUI.common.TitlePanel;
import program.GUI.common.dialogs.DialogOption;
import program.GUI.common.dialogs.MessageDialog;
import program.GUI.common.dialogs.OptionDialog;
import program.Question;
import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.attributes.fields.QuestionNumericalAttribute;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HomeSelectModePanel extends JPanel {
    private JFrame mainFrame;
    private HomeScrollPane parentScrollPane;
    private QuestionList questions;
    //TODO allow customisable columns in question preview
    public HomeSelectModePanel(JFrame mainFrame, HomeScrollPane parentScrollPane,
            QuestionList questions) {
        this.mainFrame = mainFrame;
        this.parentScrollPane = parentScrollPane;
        this.questions = questions;

        setLayout(new BorderLayout());

        add(new TitlePanel("Select revision mode",
                        "Choose how the order in which questions are presented " +
                                "is determined"),
                BorderLayout.NORTH);

        List<QuestionAttribute> qAttributes = new ArrayList<>();
        qAttributes.add(QuestionAttribute.TITLE);
        qAttributes.add(QuestionAttribute.TOPIC);
        qAttributes.add(QuestionAttribute.SUBJECT);

        List<QuestionNumericalAttribute> qNAttributes = new ArrayList<>();
        qNAttributes.add(QuestionNumericalAttribute.INDEX);
        JScrollPane questionsPreview = new QuestionsTableScrollPane(questions,
                qAttributes, qNAttributes,
                false);
        add(questionsPreview, BorderLayout.CENTER);

        add(new ModeSelectionPanel(), BorderLayout.WEST);

        JPanel navBar = new NavBar("Back to question filtering", "Start " +
                "revising") {
            @Override
            protected void backButtonActionPerformed() {
                DialogOption option1 = new DialogOption("Go back", "confirm " +
                        "returning to question filtering.");
                DialogOption option2 = new DialogOption("Cancel", "cancel.");

                ArrayList<DialogOption> options = new ArrayList<>();
                options.add(option1);
                options.add(option2);
                int response = OptionDialog.displayOptionDialog("Confirmation", "Are you sure?",
                        "Are you sure you want to go back to question filtering? This will " +
                                "reset any filters you currently have applied.",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, options);
                if (response == JOptionPane.YES_OPTION) {
                    parentScrollPane.showFilterQuestionsPanel();
                }
            }

            @Override
            protected void continueButtonActionPerformed() {
                MessageDialog.displayInfoMessage(mainFrame, "This does nothing",
                        "This button does nothing", "For now...",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        };

        add(navBar, BorderLayout.SOUTH);
    }
}
