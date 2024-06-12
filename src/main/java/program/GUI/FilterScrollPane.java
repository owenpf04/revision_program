package program.GUI;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class FilterScrollPane extends JScrollPane {
    private QuestionList questionsFromFile;
    private QuestionsTablePanel tablePanel;

    private ArrayList<Filter> filters;

    public FilterScrollPane(QuestionList questionsFromFile, QuestionsTablePanel tablePanel) {
        this.questionsFromFile = questionsFromFile;
        this.tablePanel = tablePanel;
        this.filters = new ArrayList<>();

        getVerticalScrollBar().setUnitIncrement(16);

        JPanel innerPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        setViewportView(innerPanel);

        QuestionAttribute[] attributes = QuestionAttribute.valuesReversed();
        System.out.println(attributes.length);
        for (int i = 0; i < attributes.length - 1; i++) {
            gbc.gridy = i;
            Filter filter = new Filter(attributes[i]);
            filters.add(filter);
            innerPanel.add(filter, gbc);
        }

        gbc.gridy = attributes.length - 1;
        gbc.weighty = 1;
        // filler panel
        innerPanel.add(new JPanel(), gbc);
    }

    private class Filter extends JPanel {
        private JPanel mainPanel;
        private JButton headingButton;
        private boolean expanded;
        private QuestionAttribute attribute;

        private Set<String> options;
        private Set<String> selectedOptions;

        public Filter(QuestionAttribute attribute) {
            expanded = false;
            this.attribute = attribute;
            options = questionsFromFile.getValues(attribute);
            selectedOptions = new TreeSet<>();

            setLayout(new BorderLayout());

            mainPanel = new JPanel(new BorderLayout());
            headingButton = createHeadingButton();

            mainPanel.add(headingButton, BorderLayout.NORTH);
            add(mainPanel, BorderLayout.CENTER);
        }

        public JButton createHeadingButton() {
            JButton button = new JButton();
            button.setAlignmentY(CENTER_ALIGNMENT);

            JLabel buttonLabel = new JLabel(ReformatString.toPlainText(attribute.toString(),
                    false), SwingConstants.LEFT);
            Icon icon = expanded ? FontIcon.of(CarbonIcons.SUBTRACT_ALT, 25):
                    FontIcon.of(CarbonIcons.ADD_ALT, 25);
            buttonLabel.setIcon(icon);
            buttonLabel.setIconTextGap(10);
            buttonLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 24));

            button.add(buttonLabel);
            button.addActionListener(new HeadingButtonListener());

            return button;
        }

        private class HeadingButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (expanded) {
                    expanded = false;

                    mainPanel.removeAll();
                    headingButton = createHeadingButton();
                    mainPanel.add(headingButton, BorderLayout.NORTH);
                } else {
                    expanded = true;

                    mainPanel.removeAll();
                    headingButton = createHeadingButton();
                    mainPanel.add(headingButton, BorderLayout.NORTH);

                    JPanel checkboxPanel = new JPanel();
                    checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
                    for (String option : options) {
                        JCheckBox checkBox = new JCheckBox(option);
                        if (selectedOptions.contains(option)) {
                            checkBox.setSelected(true);
                        }
                        checkBox.addActionListener(new CheckboxListener());
                        checkBox.setIcon(FontIcon.of(CarbonIcons.CHECKBOX, 21));
                        checkBox.setSelectedIcon(FontIcon.of(CarbonIcons.CHECKBOX_CHECKED_FILLED, 21));
                        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                        checkboxPanel.add(checkBox);
                    }

                    mainPanel.add(checkboxPanel, BorderLayout.CENTER);
                }
            }
        }

        private class CheckboxListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (((JCheckBox) e.getSource()).isSelected()) {
                    selectedOptions.add(e.getActionCommand());
                } else {
                    selectedOptions.remove(e.getActionCommand());
                }
                tablePanel.updateData(getQuestionsMatchingSelection());
            }
        }
    }

    private QuestionList getQuestionsMatchingSelection() {
        QuestionList questions = new QuestionList(questionsFromFile.getQuestions());

        for (Filter filter : filters) {
            Set<String> selectedOptions = filter.selectedOptions;

            if (selectedOptions.isEmpty()) {
                continue;
            }

            questions = questions.selectByAttributeArray(selectedOptions, filter.attribute);
        }

        return questions;
    }
}
