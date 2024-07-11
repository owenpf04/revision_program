package program.GUI.questionFiltering;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.QuestionList;
import program.helpers.Misc;
import program.helpers.ReformatString;

import javax.swing.*;
import java.awt.*;

public abstract class Filter extends JPanel {
    protected JButton headingButton;
    protected JPanel optionsPanel;
    protected boolean expanded;
    protected int columnNum;
    protected String name;
    protected String tooltip;
    protected QuestionList questionsFromFile;
    protected QuestionsTableScrollPane tablePane;

    public Filter(int columnNum, QuestionList questionsFromFile,
            QuestionsTableScrollPane tablePane, String attributeName, String tooltip) {
        expanded = false;
        this.columnNum = columnNum;
        this.questionsFromFile = questionsFromFile;
        this.tablePane = tablePane;
        this.name = attributeName;

        setLayout(new BorderLayout());

        headingButton = createHeadingButton(tooltip);
        add(headingButton, BorderLayout.NORTH);
    }
    public JButton createHeadingButton(String tooltip) {
        JButton button = new JButton();
        button.setAlignmentY(CENTER_ALIGNMENT);
//        button.putClientProperty("JButton.buttonType", "toolBarButton");

        JPanel buttonPanel = new JPanel(new BorderLayout(15, 0));

        JLabel helpLabel = new JLabel(FontIcon.of(CarbonIcons.HELP, 20));
        helpLabel.setToolTipText("<html>" +
                ReformatString.wrapString(tooltip, 80, 0, true) +
                "</html>");

        JLabel buttonLabel = new JLabel(ReformatString.toPlainText(name,
                false), SwingConstants.LEFT);
        Icon icon = expanded ? FontIcon.of(CarbonIcons.SUBTRACT_ALT, 25,
                Misc.getUIManagerColor("colorForeground")) :
                FontIcon.of(CarbonIcons.ADD_ALT, 25,
                        Misc.getUIManagerColor("colorForeground"));
        buttonLabel.setIcon(icon);
        buttonLabel.setIconTextGap(15);
        buttonLabel.putClientProperty("FlatLaf.styleClass", "h1.regular");

        buttonPanel.add(buttonLabel, BorderLayout.CENTER);
        buttonPanel.add(helpLabel, BorderLayout.EAST);
        buttonPanel.setOpaque(false);

        button.add(buttonPanel);
        button.setPreferredSize(new Dimension((int) (8.75 * button.getPreferredSize().height),
                button.getPreferredSize().height));

        return button;
    }
}
