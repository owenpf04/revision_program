package program.GUI.questionFiltering;

import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.swing.FontIcon;
import program.QuestionList;
import program.attributes.fields.QuestionAttribute;
import program.helpers.Misc;
import program.helpers.ReformatString;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class FilterScrollPane extends JScrollPane {
    private QuestionList questionsFromFile;
    private QuestionsTableScrollPane tablePanel;

    private ArrayList<Filter> filters;

    public FilterScrollPane(QuestionList questionsFromFile, QuestionsTableScrollPane tablePanel) {
        this.questionsFromFile = questionsFromFile;
        this.tablePanel = tablePanel;
        this.filters = new ArrayList<>();

        // see HomeScrollPane for an explanation
        getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        getVerticalScrollBar().setUnitIncrement(16);
        getHorizontalScrollBar().setUnitIncrement(16);
        setPreferredSize(new Dimension(400,600));

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

            headingButton = createHeadingButton();
            add(headingButton, BorderLayout.NORTH);
        }

        public JButton createHeadingButton() {
            JButton button = new JButton();
            button.setAlignmentY(CENTER_ALIGNMENT);

            JLabel buttonLabel = new JLabel(ReformatString.toPlainText(attribute.toString(),
                    false), SwingConstants.LEFT);
            Icon icon = expanded ? FontIcon.of(CarbonIcons.SUBTRACT_ALT, 25,
                    Misc.getUIManagerColor("colorForeground")):
                    FontIcon.of(CarbonIcons.ADD_ALT, 25,
                            Misc.getUIManagerColor("colorForeground"));
            buttonLabel.setIcon(icon);
            buttonLabel.setIconTextGap(15);
            buttonLabel.putClientProperty("FlatLaf.styleClass", "h1.regular");

            button.add(buttonLabel);
            button.addActionListener(new HeadingButtonListener());

            return button;
        }

        private class HeadingButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (expanded) {
                    expanded = false;

                    removeAll();
                    headingButton = createHeadingButton();
                    add(headingButton, BorderLayout.NORTH);
                } else {
                    expanded = true;

                    removeAll();
                    headingButton = createHeadingButton();
                    add(headingButton, BorderLayout.NORTH);

                    JPanel checkboxPanel = new JPanel();
                    checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
                    checkboxPanel.setBorder(new EmptyBorder(0,30,5,5));
                    for (String option : options) {
                        JCheckBox checkBox = new JCheckBox(option);
                        if (selectedOptions.contains(option)) {
                            checkBox.setSelected(true);
                        }
                        Misc.updateCheckboxIcons(checkBox, 21);
                        checkBox.addActionListener(new CheckboxListener());
                        checkboxPanel.add(checkBox);
                    }

                    add(checkboxPanel, BorderLayout.CENTER);
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
