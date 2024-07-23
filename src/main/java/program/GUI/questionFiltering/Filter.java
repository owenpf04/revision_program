package program.GUI.questionFiltering;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.QuestionList;
import program.helpers.Misc;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class Filter extends JPanel {
    protected JButton headingButton;
    protected JPanel optionsPanel;
    protected boolean expanded;
    protected int columnNum;
    protected String name;
    protected String tooltip;
    protected QuestionList questionsFromFile;
    protected QuestionsSelectionTableScrollPane tablePane;

    public Filter(int columnNum, QuestionList questionsFromFile,
            QuestionsSelectionTableScrollPane tablePane, String attributeName, String tooltip) {
        expanded = false;
        this.columnNum = columnNum;
        this.questionsFromFile = questionsFromFile;
        this.tablePane = tablePane;
        this.name = attributeName;
        this.tooltip = tooltip;

        setLayout(new BorderLayout());

        headingButton = createHeadingButton();
        add(headingButton, BorderLayout.NORTH);

//        setBorder(new FlatBorder());
    }
    public JButton createHeadingButton() {
        JButton button = new JButton();
        button.putClientProperty("JButton.buttonType", "toolBarButton");
        button.setAlignmentY(CENTER_ALIGNMENT);
//        button.putClientProperty("JButton.buttonType", "toolBarButton");

        JPanel buttonPanel = new JPanel(new BorderLayout(15, 0));

        JLabel buttonLabel = new JLabel(ReformatString.toPlainText(name,
                false), SwingConstants.LEFT);
        buttonLabel.putClientProperty("FlatLaf.styleClass", "h1.regular");

        int textHeight = getFontMetrics(buttonLabel.getFont()).getHeight();
        Icon icon = expanded ? FontIcon.of(CarbonIcons.SUBTRACT_ALT, (int)
                (0.9 * textHeight),Misc.getUIManagerColor("colorForeground")) :
                FontIcon.of(CarbonIcons.ADD_ALT, (int)
                                (0.9 * textHeight),
                        Misc.getUIManagerColor("colorForeground"));
        buttonLabel.setIcon(icon);
        buttonLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        buttonLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        buttonLabel.setIconTextGap(15);

        JLabel helpLabel = new JLabel(FontIcon.of(CarbonIcons.HELP, (int)
                (0.75 * textHeight)));
        helpLabel.setToolTipText("<html>" +
                ReformatString.wrapString(tooltip, 80, 0, true) +
                "</html>");
        UIManager.put("ToolTip.font", UIManager.getFont("medium.font"));

        buttonPanel.add(buttonLabel, BorderLayout.CENTER);
        buttonPanel.add(helpLabel, BorderLayout.EAST);
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension((int) (buttonPanel.getPreferredSize().getWidth()),
                (int) (1.2 * buttonPanel.getPreferredSize().getHeight())));

        button.add(buttonPanel);
        button.setPreferredSize(new Dimension((int) (12 * textHeight),
                button.getPreferredSize().height));

        return button;
    }
}
